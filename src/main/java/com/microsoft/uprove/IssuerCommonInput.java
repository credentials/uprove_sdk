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

import com.microsoft.uprove.FieldZq.ZqElement;

/**
 * Common Issuer input for the issuance protocol.
 */
class IssuerCommonInput {
	private GroupElement gamma;
	private ZqElement y0;

	public IssuerCommonInput() {
	}

	public GroupElement getGamma() {
		return gamma;
	}

	public void setGamma(GroupElement gamma) {
		this.gamma = gamma;
	}

	public ZqElement getY0() {
		return y0;
	}

	public void setY0(ZqElement y0) {
		this.y0 = y0;
	}

}
