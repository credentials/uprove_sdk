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


import com.microsoft.uprove.RandomSourceImpl;


/**
 * Utility class that generates random data for the SDK. This class only
 * provides static methods, which use a
 * {@link java.security.SecureRandom SecureRandom} object as a randomness
 * source.
 * <p>
 * The <code>SecureRandom</code> object is initialized the first time a
 * method is called. The PRNG algorithm used can be specified by using the
 * {@link com.microsoft.uprove.Config#setSecureRandomAlgorithm(String)
 * setSecureRandomAlgorithm} method of the <code>Config</code> class. The
 * Cryptographic Service Provider supplying the implementation may be specified
 * via the {@link com.microsoft.uprove.Config#setSecureRandomProvider(String)
 * setSecureRandomProvider} method. By default, the class will use the
 * <code>SHA1PRNG</code> algorithm supplied by the first Cryptographic Service
 * Provider that implements it. These defaults may be modified on a
 * per-application or site-wide manner via the techniques described in the
 * {@link com.microsoft.uprove.Config} class.
 * </p>
 * <p>
 * Applications SHOULD seed the <code>RandomSource</code> at initialization
 * time using the {@link #seed(byte[]) seed} method. Seed data, which should
 * be unpredictable random data (User input, network traffic, etc.), will
 * complement the internal PRNG randomness pool (the <code>RandomSource</code>
 * will call the <code>SecureRandom</code>'s <code>nextBytes</code> method to
 * force the object to seed itself from Java's internal entropy source, as
 * mentioned in the
 * {@link java.security.SecureRandom#getInstance(java.lang.String) getInstance}
 * description).
 * </p>
 * <p>
 * To summarize, applications should, at startup, specify the required PRNG
 * algorithm and provider by calling
 * <pre>    Config.setSecureRandomAlgorithm(algorithm);
 *    Config.setSecureRandomProvider(provider);</pre>
 * or by having those values specified in the SDK configuration. Then, they
 * should seed the PRNG with a call to
 * <pre>    RandomSource.seed(data);</pre>
 * </p>
 *
 * @see java.security.SecureRandom
 */
final class RandomSource {

    /**
     * Non instantiable class.
     */
    private RandomSource() {
        super();
    }

    /**
     * Generates a random byte array of given <code>size</code>.
     * @param size number of bytes to generate.
     * @return a random byte array. Ownership of the referent is given to the
     * caller.
     */
    public static byte[] getRandomBytes(final int size) {
        return RandomSourceImpl.getRandomBytes(size);
    }

    /**
     * Returns a random positive <code>BigInteger</code> of specified size,
     * uniformly distributed over
     * the range <tt>0</tt> to <tt>(2<sup>numBits</sup> - 1)</tt>, inclusive.
     * @param numBits number of bits of the requested <code>BigInteger</code>.
     * @return a random <code>BigInteger</code>.
     */
    public static java.math.BigInteger getRandomBigInteger(final int numBits) {
        return RandomSourceImpl.getRandomBigInteger(numBits);
    }

    /**
     * Seeds the random source with the provided <code>data</code>. This method
     * can be called several times; the given seed supplements, rather than
     * replaces, the existing seed.
     * @param data random data that will be used to seed the underlying
     * <code>SecureRandom</code> object. The caller's ownership of the referent
     * is preserved.
     */
    public static void seed(final byte[] data) {
        RandomSourceImpl.seed(data);
    }

}
