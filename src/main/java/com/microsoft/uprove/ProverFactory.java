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
 * A factory capable of creating {@link com.microsoft.uprove.Prover} objects.
 */
class ProverFactory {

    /**
     * Private constructor to prevent instantiation.
     */
    private ProverFactory() {
        super();
    }

    static Prover generate(
            final int numberOfTokens,
            final ProverCommonInput input) {
        if (numberOfTokens <= 0) {
            throw new IllegalArgumentException("numberOfTokens must be > 0");
        }
        if (input == null) {
            throw new NullPointerException("input must not be null");
        }

        return new ProverImpl(numberOfTokens,
        		(ProverCommonInput) input);
    }

    static ProverCommonInput computeInput(
            final ProverProtocolParameters parameters)
        throws IOException {
    	
    	assert parameters != null;
        IssuerParametersInternal ipi = IssuerParametersInternal.generate(parameters.getIssuerParameters());
    	ProverCommonInput input = new ProverCommonInput();
    	
    	ZqElement[] x = ProtocolHelper.computeXArray(ipi,parameters.getTokenAttributes(), parameters.getTokenInformation());
    	GroupElement gamma = ProtocolHelper.computeProduct(ipi.getPublicKey(), x); 
    	GroupElement sigmaZ = ProtocolHelper.computeProduct(ipi.getProverIssuanceValues(), x);
    	if (parameters.getDevicePublicKey() != null || parameters.getDeviceZetaParameter() != null) {
    		if (parameters.getDevicePublicKey() == null || parameters.getDeviceZetaParameter() == null) {
    			throw new NullPointerException("Missing Device parameter");
    		}
    		if (!ipi.supportsDevice()) {
    			throw new IllegalArgumentException("Issuer parameters do not support Device-protection");
    		}
    		
    		gamma.multiplyAssign(ipi.getGroup().getElement(parameters.getDevicePublicKey()));
    		sigmaZ.multiplyAssign(ipi.getGroup().getElement(parameters.getDeviceZetaParameter()));
    	}
    	input.setGamma(gamma);
    	input.setSigmaZ(sigmaZ);
    	input.setIssuerParameters(ipi);
    	input.setProverParams(parameters);

    	return input;
    }

}
