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

import java.io.OutputStream;
import java.security.MessageDigest;


/**
 * An output stream that passes all data to a <code>MessageDigest</code>.
 */
final class MDOutputStream extends OutputStream {

    private final MessageDigest md;

    /**
     * @param md the digest to which all data will be written.
     */
    MDOutputStream(final MessageDigest md) {
        super();
        if (md == null) {
            throw new NullPointerException();
        }
        this.md = md;
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(int)
     */
    public void write(final int b) {
        md.update((byte) b);
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    public void write(final byte[] b, final int off, final int len) {
        md.update(b, off, len);
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(byte[])
     */
    public void write(final byte[] b) {
        md.update(b);
    }

}
