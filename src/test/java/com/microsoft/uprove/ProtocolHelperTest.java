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

import java.util.Arrays;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ProtocolHelperTest extends TestCase {

	/**
	  * Constructor for TestVectorsTest.
	  * @param arg0
	  */
   public ProtocolHelperTest(String arg0){
       super(arg0);
   }

   public void testGetUndisclosedIndices() {
	   Assert.assertTrue(Arrays.equals(new int[] {2,4}, ProtocolHelper.getUndisclosedIndices(5, new int[] {1,3,5})));
	   Assert.assertTrue(Arrays.equals(new int[] {}, ProtocolHelper.getUndisclosedIndices(3, new int[] {1,2,3})));
	   Assert.assertTrue(Arrays.equals(new int[] {1,2,3}, ProtocolHelper.getUndisclosedIndices(3, new int[] {})));
	   Assert.assertTrue(Arrays.equals(new int[] {2}, ProtocolHelper.getUndisclosedIndices(4, new int[] {1,3,4})));
	   Assert.assertTrue(Arrays.equals(new int[] {1,3,4}, ProtocolHelper.getUndisclosedIndices(4, new int[] {2})));
	   Assert.assertTrue(Arrays.equals(new int[] {}, ProtocolHelper.getUndisclosedIndices(0, new int[] {})));
   }

   /* TODO: add this test
	public void testGetMagnitude() throws IOException {
		// zero
		assertTrue(Arrays.equals(new byte[] { 0 },
								 Zq.getZero().getMagnitude()));
    // some values with no extra sign byte
    assertTrue(Arrays.equals(new byte[] { 1 },
                             Zq.getOne().getMagnitude()));
    assertTrue(Arrays.equals(new byte[] { 127 },
                             Zq.getElement(new byte[] { 127 } ).getMagnitude()));
    // a value with an extra sign byte
    byte[] twoscomp = BigInteger.valueOf(128).toByteArray();
    assertEquals(2, twoscomp.length);
    assertEquals(0, twoscomp[0]);
    assertTrue(Arrays.equals(new byte[] { (byte) 128 },
                             Zq.getElement(twoscomp).getMagnitude()));
	}
*/

}
