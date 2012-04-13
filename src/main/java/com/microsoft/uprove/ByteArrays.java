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

import java.util.Arrays;


/**
 * Convenience methods for operating on byte arrays.
 */
final class ByteArrays {

    /**
     * Private constructor to prevent instantiation.
     */
    private ByteArrays() {
        super();
    }

    /**
     * Indicates whether two arrays of byte arrays are equal.
     * @param one an array of byte arrays.
     * @param two an array of byte arrays.
     * @return <code>true</code> if the two arrays of byte arrays are equal.
     */
    public static boolean equals(final byte[][] one, final byte[][] two) {
        if (one == two) {
            return true;
        }
        if (one == null || two == null) {
            return false;
        }

        final int len = one.length;

        if (len != two.length) {
            return false;
        }

        for (int i = 0; i < len; ++i) {
            if (!Arrays.equals(one[i], two[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Erases a byte array.  The array is filled with zero bytes.
     * @param data a byte array.
     */
    public static void erase(final byte[] data) {
        if (data != null) {
            Arrays.fill(data, (byte) 0);
        }
    }

    /**
     * Erases an array of byte arrays.  All byte arrays in the array are
     * filled with zero bytes and their references in the main array are
     * set to <code>null</code>.
     * @param data an array of byte arrays.
     */
    public static void erase(final byte[][] data) {
        if (data != null) {
            for (int i = 0; i < data.length; ++i) {
                erase(data[i]);
                data[i] = null;
            }
        }
    }

    /**
     * Computes the hash code for a byte array.
     * @param data a byte array.
     * @return <code>0</code> if <code>data</code> is <code>null</code>,
     * otherwise a hash computed over its elements.
     */
    public static int hashCode(final byte[] data) {
        if (data == null) {
            return 0;
        }
        int retVal = 17;
        for (int i = 0; i < data.length; ++i) {
            retVal = retVal * 37 + data[i];
        }
        return retVal;
    }

    /**
     * Computes the hash code for an array of byte arrays.
     * @param data an array of byte arrays.
     * @return <code>0</code> if <code>data</code> is <code>null</code>,
     * otherwise a hash computed over its elements.
     */
    public static int hashCode(final byte[][] data) {
        if (data == null) {
            return 0;
        }
        int retVal = 17;
        for (int i = 0; i < data.length; ++i) {
            retVal = retVal * 37 + hashCode(data[i]);
        }
        return retVal;
    }

    /**
     * Returns a clone of a byte array.
     * @param data a byte array.
     * @return <code>null</code> if <code>data</code> is <code>null</code>,
     * otherwise a distinct byte array containing the same data as
     * <code>data</code>.
     */
    public static byte[] clone(final byte[] data) {
        if (data == null) {
            return null;
        }
        return (byte[]) data.clone();
    }

    /**
     * Returns a clone of an array of byte arrays.
     * @param data an array of byte arrays.
     * @return <code>null</code> if <code>data</code> is <code>null</code>,
     * otherwise a distinct array containing clones of the byte arrays held
     * in the array <code>data</code>.
     */
    public static byte[][] clone(final byte[][] data) {
        if (data == null) {
            return null;
        }
        byte[][] retVal = new byte[data.length][];
        for (int i = 0; i < data.length; ++i) {
            retVal[i] = clone(data[i]);
        }
        return retVal;
    }

    /**
     * Returns a string representation of a byte array.
     * @param data a byte array.
     * @return <code>"null"</code> if <code>data</code> is <code>null</code>;
     * otherwise, the Base64 encoded value of <code>data</code>.
     */
    public static String toString(final byte[] data) {
        if (data == null) {
            return String.valueOf((Object) null);
        }
        return Base64.encode(data);
    }

    /**
     * Returns a string representation of an array of byte arrays.
     * @param data an array of byte arrays.
     * @return <code>"null"</code> if <code>data</code> is <code>null</code>;
     * otherwise, a list of the Base64 encoded values held in
     * <code>data</code>.
     */
    public static String toString(final byte[][] data) {
        if (data == null) {
            return String.valueOf((Object) null);
        }
        final StringBuffer sb = new StringBuffer("[");
        final int len = data.length;
        final int lastIdx = len - 1;
        for (int i = 0; i < len; ++i) {
            sb.append(toString(data[i]));
            if (i != lastIdx) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Returns the concatenation of all the byte arrays. Note that if one
     * arrays is <code>null</code>, then it is simply skipped.
     * @param arrays array of byte arrays
     * @return <code>"null"</code> if <code>arrays</code> is
     * <code>null</code>; otherwise, the concatenation of all arrays.
     */
    public static byte[] concatenate(final byte[][] arrays) {
        if (arrays == null) {
            return null;
        }
        int size = 0;
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i] != null) {
                size += arrays[i].length;
            }
        }
        byte[] concatenation = new byte[size];
        int index = 0;
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i] != null) {
                System.arraycopy(arrays[i], 0, concatenation, index,
                                 arrays[i].length);
                index += arrays[i].length;
            }
        }
        return concatenation;
    }
}
