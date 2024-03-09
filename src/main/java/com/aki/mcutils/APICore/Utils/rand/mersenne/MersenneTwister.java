package com.aki.mcutils.APICore.Utils.rand.mersenne;

import com.aki.mcutils.APICore.Utils.rand.SplitMixRand;

import java.io.Serializable;

/**
 * 624　立体空間を用いた、高速疑似乱数生成器
 * */
public class MersenneTwister extends AbstractFastRandom implements Serializable {
    private static final long serialVersionUID = -7746671748906043888L;

    private static final int N          = 624;
    private static final int M          = 397;
    private static final int MATRIX_A   = 0x9908b0df;
    private static final int UPPER_MASK = 0x80000000;
    private static final int LOWER_MASK = 0x7fffffff;

    private int[] mt = new int[N];
    private int mti;

    private static final SplitMixRand seedUniquifier = new SplitMixRand(System.nanoTime());

    public static long randomSeed() {
        final long x;

        synchronized (MersenneTwister.seedUniquifier) {
            x = MersenneTwister.seedUniquifier.nextLong();
        }

        return x ^ System.nanoTime();
    }

    /** Constructs a random number generator. */
    public MersenneTwister() {
        this(MersenneTwister.randomSeed());
    }

    /** Constructs a random number generator seeded by an {@code int}. */
    public MersenneTwister(int seed) {
        mt = new int[N];
        setSeed(seed);
    }

    /** Constructs a random number generator seeded by a {@code long}. */
    public MersenneTwister(long seed) {
        mt = new int[N];
        setSeed(seed);
    }

    /**
     * Constructs a random number generator seeded by an {@code int} array.
     *
     * @throws NullPointerException     if seed is {@code null}
     * @throws IllegalArgumentException if seed is empty
     */
    public MersenneTwister(int[] seed) {
        mt = new int[N];
        setSeed(seed);
    }

    /** Seeds this generator with an {@code int}. */
    public void setSeed(int seed) {
        clearGaussian();
        mt[0] = seed;
        for (mti = 1; mti < N; ++mti) {
            mt[mti] = 1812433253 * (mt[mti - 1] ^ (mt[mti - 1] >>> 30)) + mti;
        }
    }

    @Override
    public void setSeed(long seed) {
        mt = new int[N];
        setSeed(new int[] { (int) (seed >> 32), (int) seed });
    }

    /**
     * Seeds this generator with an {@code int} array.
     *
     * @throws NullPointerException     if seed is {@code null}
     * @throws IllegalArgumentException if seed is empty
     */
    public void setSeed(int[] seed) {
        if (seed.length == 0) {
            throw new IllegalArgumentException("Empty seed array");
        }
        setSeed(19650218);
        int i = 1, j = 0;
        for (int k = Math.max(seed.length, N); k > 0; --k) {
            mt[i] = (mt[i] ^ (mt[i - 1] ^ (mt[i - 1] >>> 30)) * 1664525) + seed[j] + j;
            ++i; ++j;
            if (i >= N) {
                mt[0] = mt[N - 1];
                i = 1;
            }
            if (j >= seed.length) {
                j = 0;
            }
        }
        for (int k = N - 1; k > 0; --k) {
            mt[i] = (mt[i] ^ (mt[i - 1] ^ (mt[i - 1] >>> 30)) * 1566083941) - i;
            ++i;
            if (i >= N) {
                mt[0] = mt[N - 1];
                i = 1;
            }
        }
        mt[0] = 0x80000000;
    }

    @Override
    protected int next(int bits) {
        int y;
        if (mti >= N) {
            int[] mag01 = { 0, MATRIX_A };
            for (int i = 0; i < N - M; ++i) {
                y = (mt[i] & UPPER_MASK) | (mt[i + 1] & LOWER_MASK);
                mt[i] = mt[i + M] ^ (y >>> 1) ^ mag01[y & 1];
            }
            for (int i = N - M; i < N - 1; ++i) {
                y = (mt[i] & UPPER_MASK) | (mt[i + 1] & LOWER_MASK);
                mt[i] = mt[i + (M - N)] ^ (y >>> 1) ^ mag01[y & 1];
            }
            y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 1];
            mti = 0;
        }
        y = mt[mti++];
        y ^= y >>> 11;
        y ^= (y << 7) & 0x9d2c5680;
        y ^= (y << 15) & 0xefc60000;
        y ^= y >>> 18;
        return y >>> (32 - bits);
    }
}
