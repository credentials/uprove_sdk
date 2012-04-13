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
 * Parameters indicating how the Device is generated. 
  */
public class DeviceSetupParameters {

	private IssuerParameters ip;
	private byte[] privateKey;
	
	/**
	 * Sets the Issuer parameters.
	 * @param ip the Issuer parameters.
	 */
	public void setIssuerParameters(IssuerParameters ip) {
		if (ip == null) {
			throw new NullPointerException("ip");
		}
		
		if (!ip.supportsDevice()) {
			throw new IllegalArgumentException("Issuer parameters does not support Device");
		}
		this.ip = ip;
	}

	/**
	 * Sets the Device private key.
	 * @param privateKey the Device private key.
	 */
	public void setDevicePrivateKey(byte[] privateKey) {
		this.privateKey = privateKey;
	}

	/**
	 * Validates <code>this</code> parameters instance.
	 */
	public void validate() {
		// nothing to validate
	}
	
	/**
	 * Generates a Device implementation.
	 * @return a Device implementation.
	 * @throws IOException 
	 */
	public Device generate() throws IOException {
    	// make sure the parameters are valid
    	validate();

    	IssuerParametersInternal ipi = IssuerParametersInternal.generate(ip);
    	if (privateKey != null) {
    		return new DeviceImpl(ipi, ipi.getGroup().getZq().getElement(privateKey));
    	} else {
    		return new DeviceImpl(ipi);
    	}
	}

}
