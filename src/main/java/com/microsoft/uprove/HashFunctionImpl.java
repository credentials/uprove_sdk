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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import com.microsoft.uprove.Hashable;
import com.microsoft.uprove.HashFunction;
import com.microsoft.uprove.FieldZq.ZqElement;

/*
 * LOW-LEVEL IMPLEMENTATION CLASS. NOT PART OF PUBLIC API.
 */

/**
 * Private implementation of a <code>HashFunction</code>.
 */
final class HashFunctionImpl implements HashFunction {
	
    private final MessageDigest md;
    private final FieldZq Zq;
    private final MDOutputStream mdSink;
    private final HashFormatter formatter;

    /**
     * Constructs a new instance of this implementation.
     * @param md the underlying <code>MessageDigest</code>.
     * @param Zq the field into which resulting elements may be created.
     */
    private HashFunctionImpl(final MessageDigest md, final FieldZq Zq) {
        if (Zq == null) {
        	throw new NullPointerException();
        }
    	this.md = md;
        this.Zq = Zq;
        md.reset();
        this.mdSink = new MDOutputStream(md);
        try {
            this.formatter = new HashFormatter(mdSink);
        } catch (IOException e) {
            throw wrapIOException(e);    // impossible
        }
    }

    /**
     * Copy constructor.
     * @param hfi
     * @throws CloneNotSupportedException
     */
    private HashFunctionImpl(final HashFunctionImpl hfi)
            throws CloneNotSupportedException {
        this.md = (MessageDigest) hfi.md.clone();
        this.Zq = hfi.Zq;
        this.mdSink = new MDOutputStream(this.md);
        this.formatter = new HashFormatter(hfi.formatter, mdSink);
    }

    /**
     * Wraps an IOException in an AssertionError.
     * @param e an IOException.
     * @return an AssertionError holding <code>e</code>.
     */
    private static AssertionError wrapIOException(final IOException e) {
        AssertionError ae = new AssertionError("Impossible exception");
        ae.initCause(e);
        return ae;
    }

    private void doReset() {
        try {
            formatter.reset(mdSink);
        } catch (IOException e) {
            throw wrapIOException(e);    // impossible
        }
    }

    /* (non-Javadoc)
     * @see com.microsoft.uprove.crypto.HashFunction#reset()
     */
    public void reset() {
        md.reset();
        doReset();
    }

    /* (non-Javadoc)
     * @see com.microsoft.uprove.crypto.HashFunction#getByteDigest()
     */
    public byte[] getByteDigest() {
        byte[] retVal = md.digest();
        doReset();
        return retVal;
    }

    /* (non-Javadoc)
     * @see com.microsoft.uprove.crypto.HashFunction#getDigestSize()
     */
    public int getDigestSize() {
        return md.getDigestLength();
    }

    /* (non-Javadoc)
     * @see com.microsoft.uprove.crypto.HashFunction#getZqDigest()
     */
    public ZqElement getZqDigest() {
        // Zq constructor will do the mod q
        ZqElement retVal = Zq.determineElement(new BigInteger(1, md.digest()));
        doReset();
        return retVal;
    }

    /* (non-Javadoc)
     * @see com.microsoft.uprove.crypto.HashFunction#getFieldZq()
     */
    public FieldZq getFieldZq() {
        return Zq;
    }

    /* (non-Javadoc)
     * @see com.microsoft.uprove.crypto.HashUpdater#update(byte)
     */
    public void update(final byte b) {
        try {
            formatter.encode(b);
        } catch (IOException e) {
            throw wrapIOException(e);    // impossible
        }
    }

    /* (non-Javadoc)
     * @see com.microsoft.uprove.crypto.HashUpdater#update(int)
     */
    public void update(final int i) {
        try {
            formatter.encode(i);
        } catch (IOException e) {
            throw wrapIOException(e);    // impossible
        }
    }

    /* (non-Javadoc)
     * @see com.microsoft.uprove.crypto.HashUpdater#update(byte[])
     */
    public void update(final byte[] opaque) {
        try {
            formatter.encode(opaque);
        } catch (IOException e) {
            throw wrapIOException(e);    // impossible
        }
    }

    /* (non-Javadoc)
     * @see com.microsoft.uprove.crypto.HashUpdater#update(byte[], int, int)
     */
    public void update(final byte[] opaque, final int offset, final int len) {
        try {
            formatter.encode(opaque, offset, len);
        } catch (IOException e) {
            throw wrapIOException(e);    // impossible
        }
    }

    /* (non-Javadoc)
     * @see com.microsoft.uprove.crypto.HashUpdater#update(BigInteger)
     */
    public void update(final BigInteger i) {
    	if (i.signum() < 0) {
    		throw new IllegalArgumentException("i must be positive or zero");
    	}
    	byte[] bytes = i.toByteArray();
    	int index = (bytes[0] == (byte)0) ? 1 : 0;
    	
    	update(bytes, index, bytes.length - index); // todo: optimize length computation!!!
    }
    
    /* (non-Javadoc)
     * @see com.microsoft.uprove.crypto.HashUpdater#update(com.microsoft.uprove.crypto.Hashable)
     */
    public void update(final Hashable d) {
        d.addToDigest(this);
    }

    /*
     * (non-Javadoc)
     * @see com.microsoft.uprove.HashUpdater#updateNull()
     */
    public void updateNull() {
        update(0);
    }

    /**
     * Creates a new hasher.
     * @param algorithm the underlying <code>MessageDigest</code> algorithm to
     * use.
     * @param provider the provider to use, or <code>null</code> to use the
     * default.
     * @param Zq the field in which <code>Zq</code> digests are to be made.
     * @return a new hash function instance.
     * @throws NoSuchAlgorithmException if no providers implement
     * <code>algorithm</code>.
     * @throws NoSuchProviderException if <code>provider</code> is not
     * registered.
     */
    public static HashFunction getInstance(final String algorithm,
            final String provider, final FieldZq Zq)
            throws NoSuchAlgorithmException, NoSuchProviderException {
        final MessageDigest md;
        if (provider == null || provider.length() == 0) {
            // look in config to find default provider
            md = ConfigImpl.getMessageDigest(algorithm);
        } else {
            // we specify a provider
            md = MessageDigest.getInstance(algorithm, provider);
        }
        return new HashFunctionImpl(md, Zq);
    }

    /**
     * Creates a new hasher.
     * @param algorithm the underlying <code>MessageDigest</code> algorithm to
     * use.
     * @param Zq the field in which <code>Zq</code> digests are to be made.
     * @return a new hash function instance.
     * @throws NoSuchAlgorithmException if no providers implement
     * <code>algorithm</code>.
     * @throws NoSuchProviderException if <code>provider</code> is not
     * registered.
     */
    public static HashFunction getInstance(final String algorithm,
            final FieldZq Zq) throws NoSuchAlgorithmException,
            NoSuchProviderException {
        return getInstance(algorithm, null, Zq);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException {
        // we can't call super.clone() due to final fields
        return new HashFunctionImpl(this);
    }

}
