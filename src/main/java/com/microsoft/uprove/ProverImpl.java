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

class ProverImpl implements Prover {

    /**
     * Type-safe enum for the state machine.
     */
    private static final class State {
        static final State INIT = new State("initial");
        static final State COMPUTED = new State("precomputed");
        static final State SECOND = new State("second message");
        private final String name;
        private State(final String name) { super(); this.name = name; }
        public String toString() { return name; }
    }

	private int numberOfTokens;
	private ProverCommonInput input;
	ZqElement[] alpha, alphaInverse, beta1, beta2, sigmaCPrime;
	GroupElement[] h, sigmaAPrime, sigmaBPrime, sigmaZPrime, ta, tb;
	
	private State state;
    
	public ProverImpl(int numberOfTokens, ProverCommonInput input) {
        if (numberOfTokens <= 0) {
            throw new IllegalArgumentException("numberOfTokens must be > 0");
        }
        if (input == null) {
            throw new NullPointerException("input must not be null");
        }
        this.numberOfTokens = numberOfTokens;
        this.input = input;
        
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
    	precomputation(null, null, null);
    }
    
	public void precomputation(byte[][] preGenAlpha, byte[][] preGenBeta1, byte[][] preGenBeta2) throws IOException {
		// check the state
        checkState(State.INIT);

		IssuerParametersInternal ip = input.getIssuerParameters();
		FieldZq Zq = ip.getGroup().getZq();
		if (preGenAlpha == null) {
			alpha = Zq.getRandomElements(numberOfTokens, true);
		} else {
			alpha = ProtocolHelper.getZqElementArray(Zq, preGenAlpha);
		}
		if (preGenBeta1 == null) {
			beta1 = Zq.getRandomElements(numberOfTokens, false);
		} else {
			beta1 = ProtocolHelper.getZqElementArray(Zq, preGenBeta1);
		}
		if (preGenBeta2 == null) {
			beta2 = Zq.getRandomElements(numberOfTokens, false);
		} else {
			beta2 = ProtocolHelper.getZqElementArray(Zq, preGenBeta2);
		}

		alphaInverse = new ZqElement[numberOfTokens];
    	sigmaCPrime = new ZqElement[numberOfTokens];
		h = new GroupElement[numberOfTokens];
		sigmaAPrime = new GroupElement[numberOfTokens];
		sigmaBPrime = new GroupElement[numberOfTokens];
		sigmaZPrime = new GroupElement[numberOfTokens];
		ta = new GroupElement[numberOfTokens];
		tb = new GroupElement[numberOfTokens];

		GroupElement g0 = ip.getPublicKey()[0];
		GroupElement g = ip.getGroup().getGenerator();
		for (int i=0; i<numberOfTokens; i++) {
			h[i] = input.getGamma().exponentiate(alpha[i]);
			sigmaZPrime[i] = input.getSigmaZ().exponentiate(alpha[i]);
			GroupElement[] base = new GroupElement[] {g0, g};
			ZqElement[] exponents = new ZqElement[] {beta1[i], beta2[i]};
			ta[i] = ProtocolHelper.computeProduct(base, exponents);
			base = new GroupElement[] {sigmaZPrime[i], h[i]};
			tb[i] = ProtocolHelper.computeProduct(base, exponents);
			alphaInverse[i] = alpha[i].inverse();
		}

		// advance the state
        state = State.COMPUTED;
	}
    
    public byte[][] generateSecondMessage(byte[][] message1) throws IOException {
        // check the state
        if (state == State.INIT) {
        	precomputation();
        } else {
            checkState(State.COMPUTED);
        }

    	if (message1.length != numberOfTokens*2) {
    		throw new IllegalArgumentException("wrong number elements in message1");
    	}

    	IssuerParametersInternal ip = input.getIssuerParameters(); 
    	GroupElement[] sigmaPair = ProtocolHelper.getGroupElementArray(ip.getGroup(), message1);
    	ZqElement[] sigmaC = new ZqElement[numberOfTokens];
    	HashFunction H = ip.getHashFunction();    	
    	for (int i=0; i<numberOfTokens; i++) {
    		sigmaAPrime[i] = ta[i].multiply(sigmaPair[2*i]);
    		sigmaBPrime[i] = tb[i].multiply(sigmaPair[2*i+1].exponentiate(alpha[i]));
    		H.update(h[i]);
    		H.update(input.getProverParams().getProverInformation());
    		H.update(sigmaZPrime[i]);
    		H.update(sigmaAPrime[i]);
    		H.update(sigmaBPrime[i]);
        	sigmaCPrime[i] = H.getZqDigest();
        	sigmaC[i] = sigmaCPrime[i].add(beta1[i]);
    	}

        // advance the state
        state = State.SECOND;
    	
    	return ProtocolHelper.getEncodedArray(sigmaC);
	}

	public UProveKeyAndToken[] generateTokens(byte[][] message3) throws IOException {
        // check the state
        checkState(State.SECOND);
		
    	if (message3.length != numberOfTokens) {
    		throw new IllegalArgumentException("wrong number elements in message3");
    	}

    	UProveKeyAndToken[] upkt = new UProveKeyAndToken[numberOfTokens];
		IssuerParametersInternal ip = input.getIssuerParameters();
        ZqElement[] sigmaR = ProtocolHelper.getZqElementArray(ip.getGroup().getZq(), message3);
		GroupElement g0 = ip.getPublicKey()[0];
		GroupElement g = ip.getGroup().getGenerator();
    	for (int i=0; i<numberOfTokens; i++) {
    		ZqElement sigmaRPrime = sigmaR[i].add(beta2[i]); 
    		
    		// verify issuer signature
    		GroupElement[] base = new GroupElement[] {g.multiply(h[i]), g0.multiply(sigmaZPrime[i])};
			ZqElement[] exponents = new ZqElement[] {sigmaRPrime, sigmaCPrime[i].negate()};
			if (!sigmaAPrime[i].multiply(sigmaBPrime[i]).equals(
    				ProtocolHelper.computeProduct(base, exponents))) {
    			throw new IllegalStateException("invalid response");
    		}
			upkt[i] = new UProveKeyAndToken(new UProveToken(
					ip.getParametersUID(),
					h[i].toByteArray(),
					input.getProverParams().getTokenInformation(),
					input.getProverParams().getProverInformation(),
					sigmaZPrime[i].toByteArray(),
					sigmaCPrime[i].toByteArray(),
					sigmaRPrime.toByteArray(),
					input.getProverParams().getDevicePublicKey() != null), 
					alphaInverse[i].toByteArray());
    	}
		
    	return upkt;
	}

	
}
