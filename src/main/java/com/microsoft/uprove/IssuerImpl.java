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

class IssuerImpl implements Issuer {

    /**
     * Type-safe enum for the state machine.
     */
    private static final class State {
        static final State INIT = new State("initial");
        static final State COMPUTED = new State("precomputed");
        static final State FIRST = new State("first message");
        private final String name;
        private State(final String name) { super(); this.name = name; }
        public String toString() { return name; }
    }
	
    private int numberOfTokens;
	private IssuerCommonInput input;
	private ZqElement[] w;
	private GroupElement[] sigmaA;
	private GroupElement[] sigmaB;
	private PrimeOrderGroup Gq;
	private State state;
	
	public IssuerImpl(int numberOfTokens, IssuerCommonInput input) {
        if (numberOfTokens <= 0) {
            throw new IllegalArgumentException("numberOfTokens must be > 0");
        }
        this.numberOfTokens = numberOfTokens;
        this.input = input;
        Gq = input.getGamma().getGroup();
        
        // set the state
        this.state = State.INIT;
	}

    /**
     * Check to make sure we're in the proper state.
     * @param expected the expected state.
     * @throws IllegalStateException if the object is in the wrong state
     */
    private void checkState(final State expected) {
        if (state != expected) {
            throw new IllegalStateException(
                    "Invalid state. Expected \"" + expected
                    + "\" was \"" + state + "\"");
        }
    }
	
    public void precomputation() throws IOException {
    	precomputation(null);
    }
    
	public void precomputation(byte[][] preGenW) throws IOException {
        // check the state
        checkState(State.INIT);

		FieldZq Zq = Gq.getZq();
		if (preGenW == null) {
			w = Zq.getRandomElements(numberOfTokens, false);
		} else {
			w = ProtocolHelper.getZqElementArray(Zq, preGenW);
		}
		sigmaA = new GroupElement[numberOfTokens];
		sigmaB = new GroupElement[numberOfTokens];
		for (int i=0; i<numberOfTokens; i++) {
			sigmaA[i] = Gq.getGenerator().exponentiate(w[i]); 
			sigmaB[i] = input.getGamma().exponentiate(w[i]);
		}

		// advance the state
        state = State.COMPUTED;
	}
	
	public byte[][] generateFirstMessage() throws IOException {
        // check the state
        if (state == State.INIT) {
        	precomputation();
        } else {
            checkState(State.COMPUTED);
        }

        // advance the state
        state = State.FIRST;

        // return the sigmaA and sigmaB values
        byte[][] message1 = new byte[numberOfTokens*2][];
        for (int i=0; i<numberOfTokens; i++) {
        	message1[2*i] = sigmaA[i].toByteArray(); 
        	message1[2*i+1] = sigmaB[i].toByteArray(); 
        }
        return message1;
	}

	public byte[][] generateThirdMessage(byte[][] message2) throws IOException {
        // check the state
        checkState(State.FIRST);

        // check message
        if (message2.length != numberOfTokens) {
        	throw new IllegalArgumentException("wrong number of elements in message2");
        }
        
		ZqElement[] sigmaC = ProtocolHelper.getZqElementArray(Gq.getZq(), message2);
        byte[][] sigmaR = new byte[numberOfTokens][];
		for (int i=0; i<numberOfTokens; i++) {
			sigmaR[i] = sigmaC[i].multiply(input.getY0()).add(w[i]).toByteArray();
		}
		return sigmaR;
	}


}
