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
 * Thrown when a presentation proof is invalid.
 */
public class InvalidProofException extends Exception {
	private static final long serialVersionUID = -6229214036602913149L;

	/**
	 * Constructs an <code>InvalidProofException</code> instance.
	 * @param message the exception message.
	 */
	public InvalidProofException(String message) {
		super(message);
	}
}
