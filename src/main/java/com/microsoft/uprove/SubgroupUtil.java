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

import java.io.IOException;
import java.math.BigInteger;

/**
 * Utility class holding methods relating to the subgroup construction.
 */
final class SubgroupUtil {

    /**
     * Private constructor to prevent subclassing or instantiation.
     */
    private SubgroupUtil() {
        super();
    }

    /**
     * Decodes a Java <code>BigInteger</code> representing an element in a
     * group from a byte array.
     * <p>
     * <b>Note:</b> The <code>PrimeOrderGroup</code>
     * {@link com.microsoft.uprove.PrimeOrderGroup#getElement(byte[])
     * getElement} method is the proper way to retrieve a
     * <code>GroupElement</code> from its portable encoding.
     * </p>
     * @param data a byte array holding the encoded value of a
     * subgroup element.
     * @return a big integer representing the group element's value.
     * @throws IOException if <code>data</code> is
     * malformed.
     * @see com.microsoft.uprove.PrimeOrderGroup#getElement(byte[])
     * @see com.microsoft.uprove.GroupElement#toByteArray()
     */
    public static BigInteger decodeElementValue(final byte[] data)
            throws IOException {
        /*
         * The encoded form of an element is simply the big-endian 
         * representation of the element's value.
         */
        if (data.length == 0) {
            // BigInteger's byte[] ctor would throw an NFE
            throw new IOException(
                    "Zero-length encoded Subgroup.ModInteger value");
        }
        return new BigInteger(1, data);
    }

    /**
     * Encodes a Java <code>BigInteger</code> representing an element in a
     * group into a byte array suitable for storage or transmission.
     * <p>
     * <b>Note:</b> The <code>GroupElement</code>
     * {@link com.microsoft.uprove.GroupElement#toByteArray() toByteArray}
     * method is the proper way to retrieve a portable encoding of a group
     * element.
     * </p>
     * @param i a big integer representing a subgroup element's value.
     * @return a byte array holding the encoded value.
     * @see com.microsoft.uprove.GroupElement#toByteArray()
     * @see com.microsoft.uprove.PrimeOrderGroup#getElement(byte[])
     */
    public static byte[] encodeElementValue(final BigInteger i) {
        return ProtocolHelper.getMagnitude(i);
    }

    /**
     * Returns the size, in bytes, of a subgroup element of
     * <code>bitLength</code> bits.
     * @param bitLength the length, in bits, of an element.
     * @return the size of the encoded element.
     */
    public static int getEncodedElementSize(final int bitLength) {
        // add one to account for the added sign bit
        // add seven to round up if needed (int division truncates, of course)
        return bitLength / 8 + 1;
    }

}
