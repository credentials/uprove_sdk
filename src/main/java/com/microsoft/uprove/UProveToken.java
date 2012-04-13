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
 * Specifies a U-Prove token.
 */
public class UProveToken {

	private byte[] issuerParametersUID;
	private byte[] publicKey;
	private byte[] tokenInformation;
	private byte[] proverInformation;
	private byte[] sigmaZ;
	private byte[] sigmaC;
	private byte[] sigmaR;
	private boolean isDeviceProtected = false;
	
	/**
	 * Constructs a new U-Prove token.
	 */
	public UProveToken() {
		super();
	}
	
	/**
	 * Constructs a new U-Prove token.
	 * @param issuerParametersUID an issuer parameters UID.
	 * @param publicKey a public key.
	 * @param tokenInformation a token information value.
	 * @param proverInformation a prover information value.
	 * @param sigmaZ a sigmaZ value.
	 * @param sigmaC a sigmaC value.
	 * @param sigmaR a sigmaR value.
	 * @param isDeviceProtected indicates if the token is Device-protected.
	 */
	public UProveToken(byte[] issuerParametersUID, byte[] publicKey,
			byte[] tokenInformation, byte[] proverInformation,
			byte[] sigmaZ, byte[] sigmaC,
			byte[] sigmaR, boolean isDeviceProtected) {
		super();
		this.issuerParametersUID = issuerParametersUID;
		this.publicKey = publicKey;
		this.tokenInformation = tokenInformation;
		this.proverInformation = proverInformation;
		this.sigmaZ = sigmaZ;
		this.sigmaC = sigmaC;
		this.sigmaR = sigmaR;
		this.isDeviceProtected = isDeviceProtected;
	}
	
	/**
	 * Gets the issuer parameters UID value.
	 * @return the issuerParameters UID value.
	 */
	public byte[] getIssuerParametersUID() {
		return issuerParametersUID;
	}

	/**
	 * Sets the issuer parameters UID value.
	 * @param issuerParametersUID the issuerParameters UID value to set.
	 */
	public void setIssuerParametersUID(byte[] issuerParametersUID) {
		this.issuerParametersUID = issuerParametersUID;
	}

	/**
	 * Gets the public key value.
	 * @return the publicKey value.
	 */
	public byte[] getPublicKey() {
		return publicKey;
	}

	/**
	 * Sets the public key value.
	 * @param publicKey the public key value to set.
	 */
	public void setPublicKey(byte[] publicKey) {
		this.publicKey = publicKey;
	}

	/**
	 * Gets the token information value.
	 * @return the token information value.
	 */
	public byte[] getTokenInformation() {
		return tokenInformation;
	}

	/**
	 * Sets the token information value.
	 * @param tokenInformation the token information value to set.
	 */
	public void setTokenInformation(byte[] tokenInformation) {
		this.tokenInformation = tokenInformation;
	}

	/**
	 * Gets the prover information value.
	 * @return the prover information value.
	 */
	public byte[] getProverInformation() {
		return proverInformation;
	}

	/**
	 * Sets the prover information value.
	 * @param proverInformation the prover information value to set.
	 */
	public void setProverInformation(byte[] proverInformation) {
		this.proverInformation = proverInformation;
	}

	/**
	 * Gets the sigmaZ value.
	 * @return the sigmaZ value.
	 */
	public byte[] getSigmaZ() {
		return sigmaZ;
	}

	/**
	 * Sets the sigmaZ value.
	 * @param sigmaZ the sigmaZ value to set.
	 */
	public void setSigmaZ(byte[] sigmaZ) {
		this.sigmaZ = sigmaZ;
	}

	/**
	 * Gets the sigmaC value.
	 * @return the sigmaC value.
	 */
	public byte[] getSigmaC() {
		return sigmaC;
	}

	/**
	 * Sets the sigmaC value.
	 * @param sigmaC the sigmaC value to set.
	 */
	public void setSigmaC(byte[] sigmaC) {
		this.sigmaC = sigmaC;
	}

	/**
	 * Gets the sigmaR value.
	 * @return the sigmaR value.
	 */
	public byte[] getSigmaR() {
		return sigmaR;
	}

	/**
	 * Sets the sigmaR value.
	 * @param sigmaR the sigmaR value to set.
	 */
	public void setSigmaR(byte[] sigmaR) {
		this.sigmaR = sigmaR;
	}

	/**
	 * Returns true if the token is Device-protected, false otherwise.
	 * @return the Device-protected boolean. 
	 */
	boolean isDeviceProtected() {
		return isDeviceProtected;
	}

	/**
	 * Sets the boolean indicating if the token is Device-protected.
	 * @param isDeviceProtected true if the token is Device-protected. 
	 */
	void setIsDeviceProtected(boolean isDeviceProtected) {
		this.isDeviceProtected = isDeviceProtected;
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
        if (!(o instanceof UProveToken)) {
            return false;
        }

        UProveToken upt = (UProveToken) o;

        return
        	Arrays.equals(this.issuerParametersUID, upt.issuerParametersUID) &&
        	Arrays.equals(this.publicKey, upt.publicKey) &&
        	Arrays.equals(this.tokenInformation, upt.tokenInformation) &&
        	Arrays.equals(this.proverInformation, upt.proverInformation) &&
        	Arrays.equals(this.sigmaZ, upt.sigmaZ) &&
        	Arrays.equals(this.sigmaC, upt.sigmaC) &&
        	Arrays.equals(this.sigmaR, upt.sigmaR) &&
        	this.isDeviceProtected == upt.isDeviceProtected;
    }

    /**
     * Returns a hash code value for the object.
     * @return a hash code value for the object.
     */
    public int hashCode() {
    	int result = 237;
    	result = 201 * result + Arrays.hashCode(this.issuerParametersUID);
    	result = 201 * result + Arrays.hashCode(this.publicKey);
    	result = 201 * result + Arrays.hashCode(this.tokenInformation);
    	result = 201 * result + Arrays.hashCode(this.proverInformation);
    	result = 201 * result + Arrays.hashCode(this.sigmaZ);
    	result = 201 * result + Arrays.hashCode(this.sigmaC);
    	result = 201 * result + Arrays.hashCode(this.sigmaR);
    	result = result + (this.isDeviceProtected ? 201 : 0);
        return result;
    }
}
