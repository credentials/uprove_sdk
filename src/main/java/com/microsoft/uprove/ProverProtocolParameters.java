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

/**
 * Specifies the Prover protocol parameters for the issuance protocol. 
 */
public final class ProverProtocolParameters {

	private int numberOfTokens = 1;
	private IssuerParameters issuerParameters = null;
    private byte[][] tokenAttributes; // = null;
    private byte[] tokenInformation;
    private byte[] proverInformation;
    private ProverCommonInput input;
	private byte[] deviceZetaParameter;
	private byte[] devicePublicKey;

    /**
     * Constructs a <code>ProverProtocolParameters</code> instance. 
     */
    public ProverProtocolParameters() {
    	super();
    }

    /**
     * Constructs a <code>ProverProtocolParameters</code> instance.
     * @param numberOfTokens the number of tokens to issue.
     * @param issuerParameters the Issuer parameters.
     * @param tokenAttributes the token attributes.
     * @param tokenInformation the token information value.
     * @param proverInformation the prover information value.
     */
    public ProverProtocolParameters(int numberOfTokens,
			IssuerParameters issuerParameters,
			byte[][] tokenAttributes,
			byte[] tokenInformation,
			byte[] proverInformation) {
    	this.numberOfTokens = numberOfTokens;
    	this.issuerParameters = issuerParameters;
    	this.tokenAttributes = tokenAttributes;
    	this.tokenInformation = tokenInformation;
    	this.proverInformation = proverInformation;
    }
    
    /**
     * Gets the issuer parameters.
	 * @return the issuer parameters.
	 */
	public IssuerParameters getIssuerParameters() {
		return issuerParameters;
	}

	/**
	 * Sets the issuer parameters.
	 * @param issuerParameters the issuer parameters.
	 */
	public void setIssuerParameters(
			IssuerParameters issuerParameters) {
		this.issuerParameters = issuerParameters;
	}

	/**
	 * Gets the token attributes.
	 * @return the token attributes.
	 */
	public byte[][] getTokenAttributes() {
		return tokenAttributes;
	}

	/**
	 * Sets the token attributes.
	 * @param tokenAttributes the token attributes.
	 */
	public void setTokenAttributes(byte[][] tokenAttributes) {
		this.tokenAttributes = tokenAttributes;
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
	 * @param tokenInformation the token information value.
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
	 * @param proverInformation the prover information value. 
	 */
	public void setProverInformation(byte[] proverInformation) {
		this.proverInformation = proverInformation;
	}
	
	/**
	 * Gets the number of tokens to issue.
	 * @return the number of tokens to issue.
	 */
	public int getNumberOfTokens() {
		return numberOfTokens;
	}
    
    /**
     * Sets the number of tokens to issue.
     * @param numberOfTokens the number of tokens to issue.
     * @throws IllegalArgumentException if <code>numberOfTokens</code> is
     * less than 1.
     */
    public void setNumberOfTokens(final int numberOfTokens) {
        if (numberOfTokens < 1) {
            throw new IllegalArgumentException(
                    "Number of tokens must be greater than zero");
        }
        this.numberOfTokens = numberOfTokens;
    }

    /**
     * Gets the Device public key.
     * @return the Device public key.
     */
    public byte[] getDevicePublicKey() {
    	return devicePublicKey;
    }
    
    /**
     * Gets the Device zeta parameter.
     * @return the Device zeta parameter.
     */
    public byte[] getDeviceZetaParameter() {
    	return deviceZetaParameter;
    }

    /**
     * Sets the Device parameters.
     * @param devicePublicKey the Device public key.
     * @param deviceZetaParameter the Device zeta parameter.
     */
	public void setDeviceParameters(byte[] devicePublicKey, byte[] deviceZetaParameter) {
		this.devicePublicKey = devicePublicKey;
		this.deviceZetaParameter = deviceZetaParameter;
	}

    /**
     * Tests the contents of <code>this</code> for validity.
     * @throws IllegalStateException if the parameters are invalid.
     * @throws IOException if the parameters are malformed.
     */
    public void validate() throws IllegalStateException, IOException {
        // compute the input, if not already computed. This will
        // validate the parameters.
        if (input == null) {
            input = ProverFactory.computeInput(this);
        }
    }

    /**
     * Generates a {@link Prover} instance to run the issuance protocol.
     * @return a <code>Prover</code> object.
     * @throws IllegalStateException if the parameters are invalid.
     * @throws IOException if the parameters are malformed.
     */
    public Prover generate()
        throws IOException {
        // first validate the parameters
        validate();
        // then generate the Prover instance
        return ProverFactory.generate(numberOfTokens, input);
    }

}