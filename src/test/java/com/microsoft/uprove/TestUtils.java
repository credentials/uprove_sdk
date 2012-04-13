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

import javax.crypto.ShortBufferException;

/**
 * Utility methods for various tests.
 */
public class TestUtils {
    private TestUtils() {
        super();
    }


    /**
     * Returns a byte array of the 2 hex char in <code>s</code> repeated <code>n</code> times.
     * @param s 2 hex char to repeat (with no prepending 0x)
     * @param n number of repetition
     * @return a byte array
     */
    public static byte[] repeatedHexToBytes(String s, int n) {
        if (s.length() != 2) {
            throw new IllegalArgumentException("s must be 2 hex characters");
        }
        String hexString = "";
        for (int i=0; i<n; i++) {
            hexString += s;
        }
        return hexToBytes(hexString);
    }

    /**
     * Returns the byte array corresponding to the hex string in <code>s</code>.
     * @param s hex string (with no prepending 0x)
     * @return byte array
     */
    public static byte[] hexToBytes(String s) {
        byte[] bs = new byte[(1 + s.length()) / 2];
        try {
            hexToBytes(s, bs, 0);
        } catch (ShortBufferException e) {
            throw new AssertionError(e.toString());
        }
        return bs;
    }

    /**
     * Creates a byte array corresponding to the hex string in <code>s</code>.
     * @param hexStr hex string (with no prepending 0x)
     * @param out output buffer
     * @param offset offset for output buffer
     * @return byte array
     * @throws ShortBufferException
     */

    public static final void hexToBytes(String hexStr, byte[] out, int offset)
    throws NumberFormatException, ShortBufferException {
        int length = hexStr.length();
        if ((length % 2) != 0) {
            // prepend 0
        	hexStr = "0" + hexStr;
        }

        if (out.length < offset + length / 2) {
            throw new ShortBufferException("output buffer to small");
        }

        byte b1, b2;
        for (int i = 0; i < length; i += 2) {
            b1 = (byte) Character.digit(hexStr.charAt(i), 16);
            b2 = (byte) Character.digit(hexStr.charAt(i + 1), 16);
            if (b1 < 0 || b2 < 0) {
                throw new IllegalArgumentException(hexStr.substring(i, i+2) + " is not a valid hex byte");
            }
            out[offset + i / 2] = (byte) (b1 << 4 | b2);
        }
    }
}
