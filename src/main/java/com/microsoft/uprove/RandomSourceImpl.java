//*********************************************************
//
//    Copyright (c) Microsoft. All rights reserved.
//    This code is licensed under the Apache License Version 2.0.
//    THIS CODE IS PROVIDED *AS IS* WITHOUT WARRANTY OF
//    ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING ANY
//    IMPLIED WARRANTIES OF FITNESS FOR A PARTICULAR
//    PURPOSE, MERCHANTABILITY, OR NON-INFRINGEMENT.
//
//*********************************************************

package com.microsoft.uprove;


import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;


/**
 * Implementation for the SDK's source of randomness.
 *
 * @see com.microsoft.uprove.RandomSource
 * @see java.security.SecureRandom
 */
final class RandomSourceImpl {

    private static SecureRandom random; // = null;

    /**
     * Non instantiable class.
     */
    private RandomSourceImpl() {
        super();
    }

    /**
     * Initializes the SecureRandom.
     */
    private static synchronized void init() {
        if (random == null) {
            // get values from ConfigImpl
            final String algo = ConfigImpl.secureRandomAlgorithm();
            final String prov = ConfigImpl.secureRandomProvider();

            try {
                if (prov != null) {
                    // we specify a provider
                    random = SecureRandom.getInstance(algo, prov);
                } else {
                    // we use the default provider
                    random = SecureRandom.getInstance(algo);
                }
                // Forces the SecureRandom to use Java's seed, that hopefully
                // the caller will supplement with a call to seed()
                // From the SecureRandom's getInstance() javadoc:
                //    Note that the returned instance of SecureRandom has not
                //    been seeded.
                //    [...] If a call is not made to setSeed, the first call to
                //    the nextBytes method will force the SecureRandom object
                //    to seed itself.
                random.nextBytes(new byte[1]);
            } catch (NoSuchAlgorithmException e) {
                IllegalStateException ise =
                    new IllegalStateException(
                        "Cannot find SecureRandom algorithm named " + algo);
                ise.initCause(e);
                throw ise;
            } catch (NoSuchProviderException e) {
                IllegalStateException ise =
                    new IllegalStateException(
                        "Cannot find SecureRandom provider named " + prov);
                ise.initCause(e);
                throw ise;
            }
        }
    }

    /**
     * Resets so that the source is reset from the configured algo/provider.
     */
    static synchronized void reset() {
        random = null;
    }

    /**
     * Returns a random <code>BigInteger</code> of specified size,
     * uniformly distributed over
     * the range <tt>0</tt> to <tt>(2<sup>numBits</sup> - 1)</tt>, inclusive.
     * The uniformity of the distribution is dependent on the quality
     * of the underlying RNG.
     * @param numBits number of bits of the requested <code>BigInteger</code>.
     * @return a random <code>BigInteger</code>.
     * @throws IllegalArgumentException if <code>numBits</code> is negative.
     */
    public static java.math.BigInteger getRandomBigInteger(final int numBits) {
        if (numBits < 0) {
            throw new IllegalArgumentException();
        }
        init();
        return new java.math.BigInteger(numBits, random);
    }

    /**
     * Generates a random byte array of given <code>size</code>.
     * @param size number of bytes to generate.
     * @return random byte array.
     * @throws IllegalArgumentException if <code>size</code> is negative.
     */
    public static byte[] getRandomBytes(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        init();
        byte[] bytes = new byte[size];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * Seeds the random source with the provided <code>data</code>. This method
     * can be called several times; the given seed supplements, rather than
     * replaces, the existing seed.
     * @param data random data that will be used to seed the underlying
     * <code>SecureRandom</code> object.
     */
    public static void seed(final byte[] data) {
        if (data == null) {
            throw new NullPointerException();
        }
        init();
        random.setSeed(data);
    }

}
