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


class ProverCommonInput {
	private GroupElement gamma;
	private GroupElement sigmaZ;
	private ProverProtocolParameters proverParams;
	private IssuerParametersInternal ipi;
	
	public ProverCommonInput() {
	}

	/**
	 * @return the gamma
	 */
	public GroupElement getGamma() {
		return gamma;
	}
	/**
	 * @param gamma the gamma to set
	 */
	public void setGamma(GroupElement gamma) {
		this.gamma = gamma;
	}

	/**
	 * @return the sigmaZ
	 */
	public GroupElement getSigmaZ() {
		return sigmaZ;
	}

	/**
	 * @param sigmaZ the sigmaZ to set
	 */
	public void setSigmaZ(GroupElement sigmaZ) {
		this.sigmaZ = sigmaZ;
	}

	/**
	 * @return the proverParams
	 */
	public ProverProtocolParameters getProverParams() {
		return proverParams;
	}

	/**
	 * @param proverParams the proverParams to set
	 */
	public void setProverParams(ProverProtocolParameters proverParams) {
		this.proverParams = proverParams;
	}

	public IssuerParametersInternal getIssuerParameters() {
		return ipi;
	}

	public void setIssuerParameters(IssuerParametersInternal ipi) {
		this.ipi = ipi;
	}

}
