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
 * Specifies the Issuer protocol parameters for the issuance protocol. 
 */
public final class IssuerProtocolParameters {

	private int numberOfTokens = 1;
	private IssuerKeyAndParameters issuerKeyAndParameters = null;
    private byte[][] tokenAttributes; // = null;
    private byte[] tokenInformation;
	private byte[] devicePublicKey;
    private IssuerCommonInput input;


    /**
     * Constructs a <code>IssuerProtocolParameters</code> instance.
     */
    public IssuerProtocolParameters() {
    	super();
    }

    /**
     * Constructs a <code>IssuerProtocolParameters</code> instance.
     * @param numberOfTokens the number of tokens to issue.
     * @param issuerKeyAndParameters the Issuer key and parameters.
     * @param tokenAttributes the token attributes.
     * @param tokenInformation the token information value.
     */
    public IssuerProtocolParameters(int numberOfTokens,
			IssuerKeyAndParameters issuerKeyAndParameters,
			byte[][] tokenAttributes, byte[] tokenInformation, byte[] devicePublicKey) {
		super();
		this.numberOfTokens = numberOfTokens;
		this.issuerKeyAndParameters = issuerKeyAndParameters;
		this.tokenAttributes = tokenAttributes;
		this.tokenInformation = tokenInformation;
		this.devicePublicKey = devicePublicKey;
	}


	/**
	 * Gets the Issuer key and parameters.
	 * @return the Issuer key and parameters.
	 */
	public IssuerKeyAndParameters getIssuerKeyAndParameters() {
		return issuerKeyAndParameters;
	}

	/**
	 * Sets the the Issuer key and parameters.
	 * @param issuerKeyAndParameters the the Issuer key and parameters.
	 */
	public void setIssuerKeyAndParameters(
			IssuerKeyAndParameters issuerKeyAndParameters) {
		this.issuerKeyAndParameters = issuerKeyAndParameters;
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
     * Sets the Device public key.
     * @param devicePublicKey the Device public key hd.
     */
	public void setDevicePublicKey(byte[] devicePublicKey) {
		if (devicePublicKey == null) {
			throw new NullPointerException();
		}
		this.devicePublicKey = devicePublicKey;
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
            input = IssuerFactory.computeInput(this);
        }
    }

    /**
     * Generates an {@link Issuer} instance to run the issuance protocol.
     * @return an <code>Issuer</code>.
     * @throws IllegalStateException if the parameters are invalid.
     * @throws IOException if the parameters are malformed.
     * @see #validate()
     */
    public Issuer generate()
        throws IllegalStateException, IOException {
        // first validate the parameters
        validate();
        // then generate the Issuer instance
        return IssuerFactory.generate(numberOfTokens,
                                           input);
    }

}