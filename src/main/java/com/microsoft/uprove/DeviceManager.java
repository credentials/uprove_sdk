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

/**
 * Manages the interaction with U-Prove Devices.
 */
public class DeviceManager {

	private static Device device;
	/**
	 * Registers a Device to be used by the Prover to generate presentation proofs.
	 * @param device
	 */
	public static void RegisterDevice(Device device) {
		if (device == null) {
			throw new NullPointerException("device");
		}
		DeviceManager.device = device; 
	}
	
	private DeviceManager() {}
	
	static byte[] GetInitialWitness() {
		if (device == null) {
			throw new IllegalStateException("Device is not initialized");
		}
		return device.GetInitialWitness();
	}
	
	static byte[] GetDeviceResponse(byte[] md, byte[] mdPrime) {
		if (device == null) {
			throw new IllegalStateException("Device is not initialized");
		}
		return device.GetResponse(md, mdPrime);
	}
}
