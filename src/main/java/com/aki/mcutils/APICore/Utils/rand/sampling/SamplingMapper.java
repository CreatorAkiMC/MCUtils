package com.aki.mcutils.APICore.Utils.rand.sampling;

import com.aki.mcutils.APICore.Utils.MathNumUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class SamplingMapper {

    //You use return value.
    public static <T> List<T> ListSampler(List<T> sample_list) {
        Collections.shuffle(sample_list);
        return sample_list;
    }

    /**
     * Mapを中身を変えず逆参照をせずシャッフルすることができます。
     * */
    public static <K, V> Map<K, V> MapSampler(Map<K, V> sample_map, Random rand) {
        int size = sample_map.size();

        if(size > 0) {
            int[] ID = IntStream.range(0, size).toArray();

            for(int i = 0; i < size; i++) {
                int oldID = ID[i];
                int ChangeIndex = rand.nextInt(i + 1);
                ID[i] = ID[ChangeIndex];
                ID[ChangeIndex] = oldID;
            }

            Map<K, V> map = new HashMap<>();

            List<Map.Entry<K, V>> Lentry = new ArrayList<>(sample_map.entrySet());

            IntStream.range(0, size).forEach(i -> {
                Map.Entry<K, V> entry = Lentry.get(ID[i]);
                map.put(entry.getKey(), entry.getValue());
            });
            return map;
        }

        return sample_map;
    }

    /**
     * Double == Map Value でソートする。
     *
     * comparator には [Comparator.naturalOrder] や [Comparator.reverseOrder]を使う ・・・ (Comparator<Double>...)
     * */
    public static <K> Map<K, Double> MapDoubleSorter(Map<K, Double> sort_map, Comparator<Map.Entry<K, Double>> comparator) {
        List<Map.Entry<K, Double>> sortList = sort_map.entrySet().stream().sorted(comparator).collect(Collectors.toList());

        Map<K, Double> sortedMap = new LinkedHashMap<>();

        for(Map.Entry<K, Double> entry : sortList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    /**
     * A: 100
     * B: 80
     * C: 40
     * D: 10
     * E: 0
     *
     * A-B = 20 - 1 = 19
     * B-C = 40 - 1 = 39
     * C-D = 30 - 1 = 29
     * D-E = 10 - 1 = 9
     * のようにすることで、
     * ifや範囲を使わなくても
     * 乱数の偏りを作れる。
     * */

    /**
     * 周波数の高いものほど、多く出ます。
     * */
    public static <K> List<Map.Entry<K, Double>> GetBiased_FrequencyMap(Map<K, Double> FrequencyMap, Random rand) {
        Comparator<Map.Entry<K, Double>> comp = (o1, o2) -> o2.getValue().compareTo(o1.getValue());

        List<Map.Entry<K, Double>> SortedListEntry = new ArrayList<>(MapDoubleSorter(FrequencyMap, comp).entrySet());//出やすいものを一番上に

        //Freqからこの値を引いたものが範囲の最小値
        double[] SubtractFreq = new double[SortedListEntry.size()];//ソートしたmapと対応している。

        double rangeMax = SortedListEntry.get(0).getValue();

        //Map.Entry<K, Double> OldEntry = null;
        double subs = 0;
        double MaxRandIndex = 0;//桁数調整用
        for(int i = 0; i < SortedListEntry.size(); i++) {
            Map.Entry<K, Double> entry = SortedListEntry.get(i);
            MaxRandIndex += entry.getValue();

            if(SortedListEntry.size() > 1) {
                if(i + 2 <= FrequencyMap.size()) {
                    subs = entry.getValue() - SortedListEntry.get(i + 1).getValue();
                    if (i + 1 < SortedListEntry.size()) {//index が要素の最後に当たった場合を省く -> 乱数で0が出た時の対策。
                        subs -= 1.0d;//周波数の低いものとの距離。
                    }
                } else {
                    subs = entry.getValue();
                }
            } else {//要素数が1の時
                subs = entry.getValue();
            }

            SubtractFreq[i] = subs;
        }

        List<Map.Entry<K, Double>> OutMap = new ArrayList<>();

        //MaxDigitをMaxRandIndexにかけて、少数を消し、乱数を生成してMaxDigitで割って、doubleにする。
        int MaxDigit = MathNumUtils.GetStringOfDigits(String.valueOf(MaxRandIndex), ".", "");

        for(int i = 0; i < FrequencyMap.size(); i++) {
            double InRange = rand.nextInt((int)(rangeMax * (double)MaxDigit)) / (double)MaxDigit;

            for(int i2 = 0; i2 < FrequencyMap.size(); i2++) {
                double Subrange = SubtractFreq[i2];
                final double[] Freq = {SortedListEntry.get(i2).getValue()};

                if(InRange <= Freq[0] && (Freq[0] - Subrange) <= InRange) {
                    int finalI = i2;
                    OutMap.add(new Map.Entry<K, Double>() {
                        @Override
                        public K getKey() {
                            return SortedListEntry.get(finalI).getKey();
                        }

                        @Override
                        public Double getValue() {
                            return Freq[0];
                        }

                        @Override
                        public Double setValue(Double value) {
                            Freq[0] = value;
                            return value;
                        }
                    });
                }
            }
        }

        return OutMap;
    }
}
