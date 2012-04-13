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

import java.math.BigInteger;


/**
 * An interface implemented by objects capable of adding data to a formatted
 * hash digest.
 */
interface HashUpdater {

    /**
     * Formats a byte value into a hash.
     * @param b the <code>byte</code> to add to a hash.
     */
    void update(byte b);

    /**
     * Formats an integer value into a hash.
     * @param i the <code>int</code> to add to a hash.
     */
    void update(int i);

    /**
     * Formats an array of bytes into a hash.
     * @param opaque the data to add to a hash.
     */
    void update(byte[] opaque);

    /**
     * Formats a portion of an array of bytes into a hash.
     * @param opaque the data to add to a hash.
     * @param offset the offset from the start of <code>opaque</code> from
     * which the data is to be hashed.
     * @param len the number of bytes to hash.
     */
    void update(byte[] opaque, int offset, int len);

    /**
     * Formats a big integer value into a hash.
     * @param i the <code>BigInteger</code> to add to the hash.
     */
    void update(BigInteger i);
    
    /**
     * Formats a <code>Hashable</code> object into a hash.
     * @param h the object to add to a hash.
     */
    void update(Hashable h);

    /**
     * Formats an integer with the value <code>0</code> into a hash.
     */
    void updateNull();
}
