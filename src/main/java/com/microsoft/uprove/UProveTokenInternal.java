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

import com.microsoft.uprove.FieldZq.ZqElement;

/**
 * Internal representation of a U-Prove token.
 */
class UProveTokenInternal {

	private byte[] issuerParametersUID;
	private GroupElement publicKey;
	private byte[] tokenInformation;
	private byte[] proverInformation;
	private GroupElement sigmaZ;
	private ZqElement sigmaC;
	private ZqElement sigmaR;
	private boolean isDeviceProtected = false;

	/**
	 * Constructs a new U-Prove token.
	 */
	UProveTokenInternal() {
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
	UProveTokenInternal(byte[] issuerParametersUID, GroupElement publicKey,
			byte[] tokenInformation, byte[] proverInformation,
			GroupElement sigmaZ, ZqElement sigmaC,
			ZqElement sigmaR, boolean isDeviceProtected) {
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
	byte[] getIssuerParametersUID() {
		return issuerParametersUID;
	}

	/**
	 * Sets the issuer parameters UID value.
	 * @param issuerParametersUID the issuerParameters UID value to set.
	 */
	void setIssuerParametersUID(byte[] issuerParametersUID) {
		this.issuerParametersUID = issuerParametersUID;
	}

	/**
	 * Gets the public key value.
	 * @return the publicKey value.
	 */
	GroupElement getPublicKey() {
		return publicKey;
	}

	/**
	 * Sets the public key value.
	 * @param publicKey the public key value to set.
	 */
	void setPublicKey(GroupElement publicKey) {
		this.publicKey = publicKey;
	}

	/**
	 * Gets the token information value.
	 * @return the token information value.
	 */
	byte[] getTokenInformation() {
		return tokenInformation;
	}

	/**
	 * Sets the token information value.
	 * @param tokenInformation the token information value to set.
	 */
	void setTokenInformation(byte[] tokenInformation) {
		this.tokenInformation = tokenInformation;
	}

	/**
	 * Gets the prover information value.
	 * @return the prover information value.
	 */
	byte[] getProverInformation() {
		return proverInformation;
	}

	/**
	 * Sets the prover information value.
	 * @param proverInformation the prover information value to set.
	 */
	void setProverInformation(byte[] proverInformation) {
		this.proverInformation = proverInformation;
	}

	/**
	 * Gets the sigmaZ value.
	 * @return the sigmaZ value.
	 */
	GroupElement getSigmaZ() {
		return sigmaZ;
	}

	/**
	 * Sets the sigmaZ value.
	 * @param the sigmaZ value to set.
	 */
	void setSigmaZ(GroupElement sigmaZ) {
		this.sigmaZ = sigmaZ;
	}

	/**
	 * Gets the sigmaC value.
	 * @return the sigmaC value.
	 */
	ZqElement getSigmaC() {
		return sigmaC;
	}

	/**
	 * Sets the sigmaC value.
	 * @param sigmaC the sigmaC value to set.
	 */
	void setSigmaC(ZqElement sigmaC) {
		this.sigmaC = sigmaC;
	}

	/**
	 * Gets the sigmaR value.
	 * @return the sigmaR value.
	 */
	ZqElement getSigmaR() {
		return sigmaR;
	}

	/**
	 * Sets the sigmaR value.
	 * @param sigmaR the sigmaR value to set.
	 */
	void setSigmaR(ZqElement sigmaR) {
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
	 * Generates an internal representation of a U-Prove token.
	 * @param ip the issuer parameters corresponding to the U-Prove token.
	 * @param upt the U-Prove token.
	 * @return an internal representation of the U-Prove token.
	 * @throws IOException if a parameter are malformed.
	 */
	static UProveTokenInternal generate(IssuerParametersInternal ip, UProveToken upt) throws IOException {
		PrimeOrderGroup Gq = ip.getGroup();
		FieldZq Zq = Gq.getZq();
		return new UProveTokenInternal(
				upt.getIssuerParametersUID(),
				Gq.getElement(upt.getPublicKey()),
				upt.getTokenInformation(),
				upt.getProverInformation(),
				Gq.getElement(upt.getSigmaZ()),
				Zq.getPositiveElement(upt.getSigmaC()),
				Zq.getPositiveElement(upt.getSigmaR()),
				ip.supportsDevice());
	}
}
