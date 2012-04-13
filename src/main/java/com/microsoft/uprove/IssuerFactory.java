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
 * A factory capable of creating {@link com.microsoft.uprove.Issuer} objects.
 */
class IssuerFactory {

    /**
     * Private constructor to prevent instantiation.
     */
    private IssuerFactory() {
        super();
    }

    /**
     * Generates a new {@link Issuer} instance.
     * @param numberOfTokens the number of tokens to issue.
     * @param input the 
     * @return
     */
    static Issuer generate(
            final int numberOfTokens,
            final IssuerCommonInput input) {
        if (numberOfTokens <= 0) {
            throw new IllegalArgumentException("numberOfTokens must be > 0");
        }
        if (input == null) {
            throw new NullPointerException("input must not be null");
        }

        return new IssuerImpl(numberOfTokens,
        		(IssuerCommonInput) input);
    }

    static IssuerCommonInput computeInput(
            final IssuerProtocolParameters parameters)
        throws IllegalStateException, IOException {

        assert parameters != null;
        IssuerParametersInternal ip = IssuerParametersInternal.generate(
        		parameters.getIssuerKeyAndParameters().getIssuerParameters());
        IssuerCommonInput input = new IssuerCommonInput();

        
        // compute gamma
    	ZqElement[] x = ProtocolHelper.computeXArray(ip, parameters.getTokenAttributes(), parameters.getTokenInformation());
    	GroupElement gamma = ProtocolHelper.computeProduct(ip.getPublicKey(), x); 
    	if (parameters.getDevicePublicKey() != null) {
    		if (!ip.supportsDevice()) {
    			throw new IllegalArgumentException("Issuer parameters do not support Device-protection");
    		}
    		gamma.multiplyAssign(ip.getGroup().getElement(parameters.getDevicePublicKey()));
    	}
    	input.setGamma(gamma);
        
        // y0
    	input.setY0(ip.getGroup().getZq().getPositiveElement(parameters.getIssuerKeyAndParameters().getPrivateKey()));
        
        return input;
    }

}
