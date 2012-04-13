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
 * An interface for the Issuer-side of the U-Prove issuance protocol.
 */
public interface Issuer {

	/**
     * Perform issuance precomputation. This is the first operation that
     * takes place in the protocol. This method may be invoked to perform
     * precomputation independently from generation of the first issuance
     * protocol message.
     * <p>
     * Invoking this method is optional. If it is not called, precomputation
     * will take place when {@link #generateFirstMessage()
     * generateFirstMessage} is invoked.
     * </p>
     * @throws IOException if an encoding error occurs. 
     * @throws IllegalStateException if either this method or
     * {@link #generateFirstMessage() generateFirstMessage} has already been
     * invoked.
     */
    void precomputation() throws IOException;

    /**
     * Generates the first issuance message.
     * @return the first issuance message.
     * @throws IOException if an encoding error occurs.
     */
    byte[][] generateFirstMessage() throws IOException;

    /**
     * Generates the third issuance message.
     * @param message2 the second issuance message.
     * @return the third issuance message.
     * @throws IOException if an encoding error occurs.
     */
    byte[][] generateThirdMessage(final byte[][] message2) throws IOException;

}
