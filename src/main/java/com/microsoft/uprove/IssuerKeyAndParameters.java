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
 * Specifies the issuer parameters and its associated private key.
 */
public final class IssuerKeyAndParameters {

	private IssuerParameters ip;
    private byte[] privateKey;

    IssuerKeyAndParameters(final IssuerParameters ip, final byte[] privateKey) {
        super();
        this.privateKey = privateKey;
        this.ip = ip;
    }

    /**
     * Sets the private key.
     * @param privateKey the private key.
     */
    public void setPrivateKey(byte[] privateKey) {
    	this.privateKey = privateKey;
    }
    
    /**
     * Gets the private key.
	 * @return the private key.
	 */
	public byte[] getPrivateKey() {
		return privateKey;
	}

	/**
	 * Sets the issuer parameters.
	 * @param ip the issuer parameters.
	 */
	public void setIssuerParameters(IssuerParameters ip) {
		this.ip = ip;
	}
	
	/**
	 * Gets the issuer parameters.
	 * @return the issuer parameters.
	 */
	public IssuerParameters getIssuerParameters() {
		return ip;
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

        if (!(o instanceof IssuerKeyAndParameters)) {
            return false;
        }

        IssuerKeyAndParameters ikap = (IssuerKeyAndParameters) o;

        return ip.equals(ikap.ip) &&
               Arrays.equals(privateKey, ikap.privateKey);
    }

    /**
     * Returns a hash code value for the object.
     * @return a hash code value for the object.
     */
    public int hashCode() {
        int result = 273;
        result = 47 * result + ip.hashCode();
        result = 47 * result + Arrays.hashCode(privateKey);
        return result;
    }
}
