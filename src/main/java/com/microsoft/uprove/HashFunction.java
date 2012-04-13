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

import com.microsoft.uprove.HashUpdater;
import com.microsoft.uprove.FieldZq.ZqElement;

/**
 * Describes a hash algorithm interface that take various inputs
 * and can output a byte digest or elements of a specific Zq field.
 * The <code>reset</code> and <code>update</code> methods behave like Java's
 * {@link java.security.MessageDigest MessageDigest}.
 * <p>Implementations of this interface perform specific formatting on the
 * data passed to the various <code>update</code> methods declared in
 * the {@link com.microsoft.uprove.HashUpdater HashUpdater}
 * interface.</p>
 */
interface HashFunction extends HashUpdater {
    /**
     * Resets the hash function to its initial state.
     */
    void reset();

    /**
     * Returns the digest of the hash function state as a byte array.
     * This automatically resets the hash function.
     * @return the digest of the hash function state.
     */
    byte[] getByteDigest();

    /**
     * Returns the size, in bytes, of the byte digest.
     * @return the size, in bytes, of the byte digest.
     */
    int getDigestSize();

    /**
     * Returns the digest of the hash function state as a Zq element.
     * The returned element is the one obtained by calling
     * {@link #getByteDigest() getByteDigest}() <code>mod q</code>.
     * This automatically resets the hash function.
     * @return the digest of the hash function state as a Zq element.
     */
    ZqElement getZqDigest();

    /**
     * Returns the field Zq to which digest are mapped.
     * @return the field Zq to which digest are mapped.
     */
    FieldZq getFieldZq();

    /**
     * Returns a clone if the implementation is cloneable.
     * @return a clone if the implementation is cloneable.
     * @throws CloneNotSupportedException if this is called on an
     * implementation that does not support the <code>clone</code> operation.
     */
    Object clone() throws CloneNotSupportedException;

}
