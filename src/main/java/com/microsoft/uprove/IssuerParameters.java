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
import java.util.Arrays;

/**
 * Specifies a issuer parameters.
 */
public final class IssuerParameters {

	// the parameters UID
    private byte[] parametersUID;

    // the prime order group
    private PrimeOrderGroup group;
    
    // the hash algorithm UID
    private String hashAlgorithmUID;

    // the public key elements
    private byte[][] publicKey;
    
    // the hash booleans
    private byte[] encodingBytes;
    
    // prover's issuance values
    private byte[][] proverIssuanceValues;
    
    // the specification
    private byte[] specification;
    
    public IssuerParameters() {
		super();
	}

	/**
     * Indicates whether some other object is "equal to" this one.
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the
     * <code>obj</code> argument; <code>false</code> otherwise.
     */
    public boolean equals(final Object obj) {

        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IssuerParameters)) {
            return false;
        }
        IssuerParameters ip = (IssuerParameters) obj;

        if (!Arrays.equals(this.parametersUID, ip.parametersUID)
            || !this.group.equals(ip.group)
            || !this.hashAlgorithmUID.equals(ip.hashAlgorithmUID)
            || !Arrays.equals(this.publicKey, ip.publicKey)
            || !Arrays.equals(this.encodingBytes, ip.encodingBytes)
            || !Arrays.equals(this.proverIssuanceValues, this.proverIssuanceValues)
            || !Arrays.equals(this.specification, ip.specification))
        {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code value for the object.
     * @return a hash code value for the object.
     */
    public int hashCode() {
        int result = 233;
        result = 229 * result + Arrays.hashCode(this.parametersUID);
        result = 229 * result + this.group.hashCode();
        result = 229 * result + this.hashAlgorithmUID.hashCode();
        result = 229 * result + Arrays.hashCode(this.publicKey);
        result = 229 * result + Arrays.hashCode(this.encodingBytes);
        result = 229 * result + Arrays.hashCode(this.proverIssuanceValues);
        result = 229 * result + Arrays.hashCode(this.specification);
        
        return result;
    }

    /**
     * Returns a string representation of the Issuer parameters object.
     * <p>This method returns a string equal to the value of:</p>
     * <pre>
     *    getClass().getName() + '@' + Integer.toHexString(hashCode())
     *        + ':' + getUID()
     * </pre>
     * @return a string representation of the Issuer parameters object.
     */
    public String toString() {
        return new StringBuffer(super.toString()).append(':').append(ByteArrays.toString(parametersUID))
                .toString();
    }

    /**
     * Returns true if Device-protected is supported, false otherwise.
     * @return if Device-protected is supported.
     */
    public boolean supportsDevice() {
    	return publicKey.length == encodingBytes.length + 3;
    }

    /**
     * Gets the parameters UID value.
	 * @return the parametersUID value.
	 */
	public byte[] getParametersUID() {
		return parametersUID;
	}

	/**
	 * Sets the parameters UID value.
	 * @param parametersUID the parameters UID value to set.
	 */
	public void setParametersUID(byte[] parametersUID) {
		this.parametersUID = parametersUID;
	}

	/**
	 * Gets the prime order group.
	 * @return the prime order group.
	 */
	public PrimeOrderGroup getGroup() {
		return group;
	}

	/**
	 * Sets the prime order group.
	 * @param group the prime order group to set.
	 */
	public void setGroup(PrimeOrderGroup group) {
		this.group = group;
	}

	/**
	 * Gets the hash algorithm UID value.
	 * @return the hash algorithm UID value. 
	 */
	public String getHashAlgorithmUID() {
		return hashAlgorithmUID;
	}

	/**
	 * Sets the hash algorithm UID value.
	 * @param hashAlgorithmUID the hash algorithm UID value to set.
	 */
	public void setHashAlgorithmUID(String hashAlgorithmUID) {
		this.hashAlgorithmUID = hashAlgorithmUID;
	}

	/**
	 * Gets the public key value.
	 * @return the public key value.
	 */
	public byte[][] getPublicKey() {
		return publicKey;
	}

	/**
	 * Sets the public key value.
	 * @param publicKey the public key value to set.
	 */
	public void setPublicKey(byte[][] publicKey) {
		this.publicKey = publicKey;
	}

	/**
	 * Gets the encoding bytes value.
	 * @return the encoding bytes value.
	 */
	public byte[] getEncodingBytes() {
		return encodingBytes;
	}

	/**
	 * Sets the encoding bytes value.
	 * @param encodingBytes the encoding bytes value to set.
	 */
	public void setEncodingBytes(byte[] encodingBytes) {
		this.encodingBytes = encodingBytes;
	}

	/**
	 * Gets the prover issuance values.
	 * @return the prover issuance values.
	 */
	public byte[][] getProverIssuanceValues() {
		return proverIssuanceValues;
	}

	/**
	 * Sets the prover issuance values.
	 * @param proverIssuanceValues the prover issuance values to set.
	 */
	public void setProverIssuanceValues(byte[][] proverIssuanceValues) {
		this.proverIssuanceValues = proverIssuanceValues;
	}

	/**
	 * Gets the specification value.
	 * @return the specification value.
	 */
	public byte[] getSpecification() {
		return specification;
	}

	/**
	 * Sets the specification value.
	 * @param specification the specification value to set.
	 */
	public void setSpecification(byte[] specification) {
		this.specification = specification;
	}
	
	/**
     * Validates the consistency of the Issuer parameters's elements. This method
     * should be called once for every externally received Issuer parameters.
     * @throws IllegalStateException if the Issuer parameters are mathematically invalid.
	 * @throws IOException if the Issuer parameters are malformed. 
     */	
	public void validate() throws IllegalStateException, IOException {
		IssuerParametersInternal.generate(this).validate();
	}
}
