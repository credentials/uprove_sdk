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

class DeviceImpl implements Device {

	private IssuerParametersInternal ipi;
	private ZqElement xd;
	private GroupElement hd;
	private ZqElement wdPrime;
	
	DeviceImpl(IssuerParametersInternal ipi) {
		if (!ipi.supportsDevice()) {
			throw new IllegalArgumentException("Issuer parameters doesn't support Device");
		}
		this.ipi = ipi;
		this.xd = ipi.getGroup().getZq().getRandomElement(false);
		this.hd = ipi.getDeviceGenerator().exponentiate(xd);
	}
	
	DeviceImpl(IssuerParametersInternal ipi, ZqElement xd) {
		if (!ipi.supportsDevice()) {
			throw new IllegalArgumentException("Issuer parameters doesn't support Device");
		}
		this.ipi = ipi;
		this.xd = xd;
		this.hd = ipi.getDeviceGenerator().exponentiate(xd);
	}
	
	@Override
	public byte[] GetDeviceParameter(byte[] zd) throws IOException {
		return ipi.getGroup().getElement(zd).exponentiate(xd).toByteArray();
	}

	@Override
	public byte[] GetDevicePublicKey() {
		return hd.toByteArray();
	}

	@Override
	public byte[] GetInitialWitness() {
		if (wdPrime == null) {
			wdPrime = ipi.getGroup().getZq().getRandomElement(false);
		}
		return ipi.getDeviceGenerator().exponentiate(wdPrime).toByteArray();
	}

	@Override
	public byte[] GetResponse(byte[] messageForDevice,
			byte[] partialChallengeDigest) {
		ZqElement c = ProtocolHelper.genChallenge(ipi, messageForDevice, partialChallengeDigest);
		return c.negate().multiply(xd).add(wdPrime).toByteArray();
	}

	public void setRandomizer(byte[] wdPrime) throws IOException {
		this.wdPrime = ipi.getGroup().getZq().getPositiveElement(wdPrime);
		
	}

}
