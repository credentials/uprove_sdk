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
 * An interface for the U-Prove Device.
 */
public interface Device {
	/**
	 * Returns the Device public key.
	 * @return the Device public key.
	 */
	byte[] GetDevicePublicKey();
	
	/**
	 * Returns the Device parameters <code>zeta_d</code>.
	 * @param zd the Issuer parameters <code>z_d</code>.
	 * @return the Device parameter.
	 * @throws IOException if an encoding error occurs.
	 */
	byte[] GetDeviceParameter(byte[] zd) throws IOException;
	
	/**
	 * Returns the Device initial witness.
	 * @return the Device initial witness.
	 */
	byte[] GetInitialWitness();
	
	/**
	 * Returns the Device response.
	 * @param messageForDevice message for the Device.
	 * @param partialChallengeDigest partial challenge digest.
	 * @return the Device response.
	 */
	byte[] GetResponse(byte[] messageForDevice, byte[] partialChallengeDigest);
}
