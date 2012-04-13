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
 * An interface for the Prover-side of the U-Prove issuance protocol. 
 */
public interface Prover {

	/**
     * Perform issuance precomputation. This is the first operation that
     * takes place in the protocol. This method may be invoked to perform
     * precomputation independently from generation of the first issuance
     * protocol message.
     * <p>
     * Invoking this method is optional. If it is not called, precomputation
     * will take place when {@link #generateSecondMessage(byte[][])
     * generateSecondMessage} is invoked.
     * </p>
     * @throws IOException if an encoding error occurs.
     * @throws IllegalStateException if either this method or
     * {@link #generateSecondMessage(byte[][]) generateSecondMessage} has already been
     * invoked.
     */
    void precomputation() throws IOException;

    /**
     * Generates the second issuance message.
     * @param message1 the first issuance message.
     * @return the second issuance message.
     * @throws IOException if an encoding error occurs.
     */
    byte[][] generateSecondMessage(byte[][] message1) throws IOException;

    /**
     * Generates the U-Prove keys and tokens.
     * @param message3 the third issuance message.
     * @return an array of U-Prove keys and tokens.
     * @throws IOException if an encoding error occurs.
     */
    UProveKeyAndToken[] generateTokens(final byte[][] message3) throws IOException;

}
