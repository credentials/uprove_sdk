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
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

/**
 * Internal representation of issuer parameters.
 */
class IssuerParametersInternal {

	// the parameters UID
    private byte[] parametersUID;

    // the prime order group
    private PrimeOrderGroup group;
    
    // the hash algorithm UID
    private String hashAlgorithmUID;

    // the public key elements
    private GroupElement[] publicKey;
    
    // the hash booleans
    private byte[] encodingBytes;
    
    // prover's issuance values
    private GroupElement[] proverIssuanceValues;
    
    // the specification
    private byte[] specification;
    
    // private protocol data
    private byte[] issuerParametersDigest;
    private HashFunction hashPrototype;
    private boolean triedCloningPrototype = false;
    
    public IssuerParametersInternal() {
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
        if (!(obj instanceof IssuerParametersInternal)) {
            return false;
        }
        IssuerParametersInternal ip = (IssuerParametersInternal) obj;

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
	 * @return the parametersUID
	 */
	public byte[] getParametersUID() {
		return parametersUID;
	}

	/**
	 * @param parametersUID the parametersUID to set
	 */
	public void setParametersUID(byte[] parametersUID) {
		this.parametersUID = parametersUID;
	}

	/**
	 * @return the group
	 */
	public PrimeOrderGroup getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(PrimeOrderGroup group) {
		this.group = group;
	}

	/**
	 * @return the hashAlgorithmUID
	 */
	public String getHashAlgorithmUID() {
		return hashAlgorithmUID;
	}

	/**
	 * @param hashAlgorithmUID the hashAlgorithmUID to set
	 */
	public void setHashAlgorithmUID(String hashAlgorithmUID) {
		this.hashAlgorithmUID = hashAlgorithmUID;
	}

	/**
	 * @return the publicKey
	 */
	public GroupElement[] getPublicKey() {
		return publicKey;
	}

	/**
	 * @param publicKey the publicKey to set
	 */
	public void setPublicKey(GroupElement[] publicKey) {
		this.publicKey = publicKey;
	}

	public GroupElement getDeviceGenerator() {
		if (!supportsDevice()) {
			throw new IllegalStateException("Issuer parameters do not support Device");
		}
		return publicKey[publicKey.length-1]; // gd
	}
	
	/**
	 * @return the encodingBytes
	 */
	public byte[] getEncodingBytes() {
		return encodingBytes;
	}

	/**
	 * @param encodingBytes the encodingBytes to set
	 */
	public void setEncodingBytes(byte[] encodingBytes) {
		this.encodingBytes = encodingBytes;
	}

	/**
	 * @return the proverIssuanceValues
	 */
	public GroupElement[] getProverIssuanceValues() {
		return proverIssuanceValues;
	}

	/**
	 * @param proverIssuanceValues the proverIssuanceValues to set
	 */
	public void setProverIssuanceValues(GroupElement[] proverIssuanceValues) {
		this.proverIssuanceValues = proverIssuanceValues;
	}

	/**
	 * @return the specification
	 */
	public byte[] getSpecification() {
		return specification;
	}

	/**
	 * @param specification the specification to set
	 */
	public void setSpecification(byte[] specification) {
		this.specification = specification;
	}

    /**
     * Validates the consistency of the Issuer Parameters's elements. This method
     * should be called once for every externally received Issuer Parameters.
     * @throws IllegalStateException if the Issuer Parameters's mathematical
     * group is malformed or if the Issuer public key elements are not part of
     * the group.
     */	
	void validate() throws IllegalStateException {
        // validate the group
        group.validate();

        // make sure that all Issuer public key elements are member of Gq
        int numElements = publicKey.length;
        for (int i = 0; i < numElements; i++) {
        	// check 1 < g_i < p
        	if (group.getIdentity().equals(publicKey[i])) {
        		throw new IllegalStateException("Public key element equals 1");
        	}
        	
        	// it is guaranteed that all pubKey elements will belong
            // to the Gq instance, otherwise they won't be usable
            if (!publicKey[i].isValid()) {
                throw new IllegalStateException("Public key element " + i + " is not in group");
            }
        }
	}
	
	   /**
     * Instantiate a new <code>HashFunction</code> object using the
     * hash algorithm specified in the parameters (obtainable by
     * calling {@link #getHashAlgorithmUID()}.
     * @return a <code>HashFunction</code> object.
     * @throws IllegalStateException if hash function can't be initialized
     */
    HashFunction getHashFunction() {

    	if (!triedCloningPrototype) {
    		// this is the first time this method is called, let's try to create a prototype

         	// see if we can use the chosen hashAlgorithm and if we can clone
             // a prototype rather than creating a new one each time around.
             HashFunction hf = null;
             HashFunction clone = null;
             try {
                 hf = HashFunctionImpl.getInstance(hashAlgorithmUID, group.getZq());
                 clone = (HashFunction) hf.clone();
             } catch (CloneNotSupportedException cnse) {
                 // oh well, can't use a prototype
             } catch (NoSuchAlgorithmException e) {
                 // defer notifying the user until they try to get an instance
             } catch (NoSuchProviderException e) {
                 // defer notifying the user until they try to get an instance
             }
             // if we were able to clone a prototype, keep it around for later
             hashPrototype = (clone == null ? null : hf);
             triedCloningPrototype = true;
         }
    	
    	// clone the prototype if possible
        if (hashPrototype != null) {
            try {
                return (HashFunction) hashPrototype.clone();
            } catch (CloneNotSupportedException e) {
                AssertionError ae = new AssertionError("Impossible exception");
                ae.initCause(e);
                throw ae;
            }
        }
    	
        // return a new instance
        IllegalStateException ise = null;
        try {
            return HashFunctionImpl.getInstance(this.hashAlgorithmUID, group.getZq());
        } catch (NoSuchAlgorithmException e) {
            ise = new IllegalStateException(
                    "Unable to initialize hash function");
            ise.initCause(e);
        } catch (NoSuchProviderException e) {
            ise = new IllegalStateException(
                    "Unable to initialize hash function");
            ise.initCause(e);
        }
        throw ise;
    }

    byte[] getIssuerParametersDigest() {
    	if (this.issuerParametersDigest != null) {
    		return this.issuerParametersDigest;
    	}
    	
    	// compute the issuer parameters digest
    	HashFunction H = getHashFunction();

    	// UIDp
    	H.update(this.parametersUID);
    	
    	// p, q, g
    	H.update(group);
    	
    	// TODO: support the case if IP supports device but token is not device-protected.
    	// <g0, g1, ..., gn, gt, [g_d]>
    	H.update(this.publicKey.length); // size
    	for (int i=0; i<(this.publicKey.length); i++) {
    		H.update(this.publicKey[i]);
    	}
    	
    	// <e1, ..., en>
    	H.update(this.encodingBytes.length); // size
    	for (int i=0; i<this.encodingBytes.length; i++) {
    		H.update(this.encodingBytes[i]);
    	}

    	// S
    	H.update(this.specification);
    	
    	this.issuerParametersDigest = H.getByteDigest();

    	return this.issuerParametersDigest;
    }

    /**
     * Generates an internal representation of an issuer parameters.
     * @param ip the issuer parameters.
     * @return an internal representation of the issuer parameters.
     * @throws IOException if the issuer parameters are malformed.
     */
    static IssuerParametersInternal generate(IssuerParameters ip) throws IOException {
    	IssuerParametersInternal ipi = new IssuerParametersInternal();
    	ipi.setParametersUID(ip.getParametersUID());
    	ipi.setEncodingBytes(ip.getEncodingBytes());
    	ipi.setHashAlgorithmUID(ip.getHashAlgorithmUID());
    	ipi.setSpecification(ip.getSpecification());
    	
    	PrimeOrderGroup Gq = ip.getGroup();
    	ipi.setGroup(Gq);
    	ipi.setProverIssuanceValues(Gq.getElementArray(ip.getProverIssuanceValues()));
    	ipi.setPublicKey(Gq.getElementArray(ip.getPublicKey()));

    	return ipi;
    }
}
