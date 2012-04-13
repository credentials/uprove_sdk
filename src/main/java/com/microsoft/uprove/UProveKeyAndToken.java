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
 * A U-Prove private key and token.
 */
public class UProveKeyAndToken {

    private final byte[] privateKey; 
    private final UProveToken upt;

    /**
     * Constructs a <code>UserKeyAndToken</code> object.
     * @param upt the U-Prove token.
     * @param privateKey the private key corresponding to <code>token</code>.
     */
    public UProveKeyAndToken(
            final UProveToken upt,
            final byte[] privateKey) {
        assert upt != null;
        assert privateKey != null;

        this.privateKey = privateKey;
        this.upt = upt;
    }
    
    /**
     * Gets the U-Prove token.
     * @return the U-Prove token.
     */
    public UProveToken getToken() {
        return upt;
    }
    
    /**
     * Gets the U-Prove token private key.
     * @return the U-Prove token private key.
     */
    public byte[] getTokenPrivateKey() {
        return privateKey;
    }

    
    /**
     * Indicates whether some other object is "equal to" this one.
     * @param o the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the
     * <code>o</code> argument; <code>false</code> otherwise.
     */
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UProveKeyAndToken)) {
            return false;
        }

        UProveKeyAndToken upkat = (UProveKeyAndToken) o;

        if (!Arrays.equals( privateKey, upkat.privateKey)
            || !upt.equals(upkat.upt)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code value for the object.
     * @return a hash code value for the object.
     */
    public int hashCode() {
        int result = 9;
        result = 7 * result + Arrays.hashCode( privateKey );
        result = 7 * result + upt.hashCode();
        return result;
    }
 }
