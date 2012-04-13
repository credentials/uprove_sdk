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


import junit.framework.Assert;
import junit.framework.TestCase;

public class DefaultSubgroupFactoryTest extends TestCase {

	public final void testDefaultGroups() {
		int[] sizes = new int[] {160, 256, 512};
		for (int i=0; i<sizes.length; i++) {
			Subgroup group = DefaultSubgroupFactory.getDefaultSubroup(sizes[i]);
			group.validate();
			Assert.assertEquals(sizes[i], group.getOrder().bitLength());
		}
	}
}
