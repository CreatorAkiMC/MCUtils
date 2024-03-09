package com.aki.mcutils.APICore;

import java.util.ArrayList;
import java.util.List;

public class StringHelper {
    public static boolean getSpellCheck(String Search, String word) {
        boolean yes2 = false;
        //System.out.print(" search: " + Search + " word: " + word);
        String[] Searchs = Search.toLowerCase().split("");
        String[] Words = word.toLowerCase().split("");
        if(Searchs.length > Words.length || Words.length == 0)
            return false;
        if(Searchs.length == 0)
            return true;
        b:for(int x = 0; x < Searchs.length; x++) {
            yes2 = false;
            for(int y = 0; y < Words.length; y++) {
                if (Searchs[x].equals(Words[y])) {
                    Words[y] = "~";
                    Searchs[x] = "^";
                    yes2 = true;
                }
            }
            if(!yes2) {
                break b;
            }
        }
        return yes2;
    }
}
