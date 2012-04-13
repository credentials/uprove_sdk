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
import java.math.BigInteger;
import com.microsoft.uprove.FieldZq.ZqElement;

/**
 * Provides protocol helper functions.
 */
final class ProtocolHelper {
	
	/**
     * Private constructor to prevent instantiation.
     */
    private ProtocolHelper() {
        super();
    }
    
    /**
     * Computes the product of a series of exponentiations.
     * @param bases an array of base elements.
     * @param exponents an array of exponents, each of which may be
     * <code>null</code> (treated as if it were <code>1</code>).
     * @return the desired product.
     */
    static GroupElement computeProduct(final GroupElement[] bases,
            final ZqElement[] exponents) {

        final GroupElement retVal = bases[0].getGroup().getIdentity();

        for (int i = 0; i < exponents.length; ++i) {
            retVal.multiplyAssign(
                exponents[i] != null
                    ? bases[i].exponentiate(exponents[i])
                    : bases[i]);
        }

        return retVal;
    }
    
    /**
     * Computes the protocol value xt.
     * @param ip the issuer parameters.
     * @param tokenInformation the token information field value.
     * @return the xt element.
     */
    static ZqElement computeXt(IssuerParametersInternal ip, byte[] tokenInformation) {
    	HashFunction H = ip.getHashFunction();
    	
    	// 0x01
    	H.update((byte) 1);
    	
    	// P
    	H.update(ip.getIssuerParametersDigest());
    	
    	// TI
    	H.update(tokenInformation);
    	
    	return H.getZqDigest(); 
    }
    
    /**
     * Computes the protocol value xi.
     * @param ip the issuer parameters.
     * @param index the attribute index, one-based.
     * @param A the attribute value A_index.
     * @return the x_index element.
     * @throws IOException
     */
    static ZqElement computeXi(IssuerParametersInternal ip, int index, byte[] A) throws IOException {
    	byte ei = ip.getEncodingBytes()[index-1];
    	if (ei == (byte)1) {
    		if (A == null || A.length == 0) {
    			return ip.getGroup().getZq().getZero();
    		} else {
    			HashFunction H = ip.getHashFunction();		
    	    	H.update(A);
    			return H.getZqDigest();
    		}
    	} else if (ei == (byte)0) {
    		if (A == null) {
    			throw new NullPointerException("Array A can't be null when e_index == 0");
    		}
    	
    		return ip.getGroup().getZq().getPositiveElement(A);
    	} else {
    		throw new IllegalArgumentException("unsupported encoding byte value: " + ei);
    	}
    }
    
    /**
     * Computes the token ID
     * @param ip
     * @return
     */
    static byte[] computeTokenID(IssuerParametersInternal ip, UProveTokenInternal upti) {
    	HashFunction H = ip.getHashFunction();
    	H.update(upti.getPublicKey());
    	H.update(upti.getSigmaZ());
    	H.update(upti.getSigmaC());
    	H.update(upti.getSigmaR());
    	return H.getByteDigest();
    }
    
    static boolean isTokenSignatureValid(IssuerParametersInternal ip, UProveTokenInternal upti) throws IOException {
    	PrimeOrderGroup Gq = ip.getGroup();

    	// check that h != 1
    	if (Gq.getIdentity().equals(upti.getPublicKey())) {
    		return false;
    	}
    	
    	// check sigma_c'
    	HashFunction H = ip.getHashFunction();
    	H.update(upti.getPublicKey());
    	H.update(upti.getProverInformation());
    	H.update(upti.getSigmaZ());
    	GroupElement[] base;
    	ZqElement[] exponents;
    	
    	base = new GroupElement[] {Gq.getGenerator(), ip.getPublicKey()[0]};
    	exponents = new ZqElement[] {upti.getSigmaR(), upti.getSigmaC().negate()};
    	H.update(computeProduct(base, exponents));
    	
    	base = new GroupElement[] {upti.getPublicKey(), upti.getSigmaZ()};
    	exponents = new ZqElement[] {upti.getSigmaR(), upti.getSigmaC().negate()};
    	H.update(computeProduct(base, exponents));
    	
    	if (!H.getZqDigest().equals(upti.getSigmaC())){
    		return false;
    	}

    	// signature is valid
    	return true;
    }
    
    /**
     * Computes the x array from an array of attributes. The returned array contains:
     * x[0] = 1, x[1] = a[0], ..., x[n] = a[-1n], x[n+1] = xt
     * @param ip the issuer parameters
     * @param attributes the attributes array
     * @param tokenInformation the token information field
     * @return the x array
     * @throws IOException
     */
    static ZqElement[] computeXArray(IssuerParametersInternal ip, byte[][] attributes, byte[] tokenInformation) throws IOException {
        // the attribute indices run from 1 to n
    	int[] attributeIndices = new int[attributes.length];
        for (int i=0; i<attributes.length; i++) {
        	attributeIndices[i] = i+1;
        }
    	return computeXArray(ip, attributeIndices, attributes, tokenInformation);
    }

    /**
     * Computes the x array from an array of attributes. The returned array contains:
     * x[0] = 1, x[1] = a[0], ..., x[n] = a[-1n], x[n+1] = xt
     * @param ip the issuer parameters
	 * @param attributeIndices the indices of the attributes
     * @param attributes the attributes array
     * @param tokenInformation the token information field
     * @return the x array
     * @throws IOException
     */
    static ZqElement[] computeXArray(IssuerParametersInternal ip, int[] attributeIndices, byte[][] attributes, byte[] tokenInformation) throws IOException {
    	if (attributeIndices.length != attributes.length) {
    		throw new IllegalArgumentException("attributeIndices and attributes array must have the same length");
    	}
    	int n = attributes.length + 2;

    	// compute the x_i
        ZqElement[] x = new ZqElement[n];
        x[0] = ip.getGroup().getZq().getOne();
        for (int index=1; index<n-1; index++) {
        	x[index] = computeXi(ip, attributeIndices[index-1], attributes[index-1]);
        }
        x[n-1] = computeXt(ip, tokenInformation);
        
        return x;
    }

    static class GenerateChallengeOutput {
    	private ZqElement c;
    	private byte[] mdPrime;
    	
    	GenerateChallengeOutput(ZqElement c, byte[] mdPrime) {
    		this.c = c;
    		this.mdPrime = mdPrime;
    	}
    	
    	ZqElement getC() {
    		return c;
    	}
    	
    	byte[] getMdPrime() {
    		return mdPrime;
    	}
    }
    
    static GenerateChallengeOutput genChallenge(IssuerParametersInternal ip, UProveTokenInternal upti, byte[] a, byte[] m, byte[] md, int[] disclosed, ZqElement[] disclosedX) {
    	byte[] UIDt = computeTokenID(ip, upti);
    	int n = ip.getEncodingBytes().length;
    	ZqElement[] f = new ZqElement[n]; // null
    	for (int i=0; i<disclosed.length ; i++) {
    		int index = disclosed[i];
    		f[index-1] = disclosedX[i]; 
    	}
    	
    	HashFunction H = ip.getHashFunction();
    	H.update(disclosed.length);
    	for (int i=0; i<disclosed.length; i++) {
    		H.update(disclosed[i]);
    	}
    	H.update(f.length);
    	for (int i=0; i<f.length; i++) {
    		if (f[i] == null) {
    			H.updateNull();
    		} else {
    			H.update(f[i]);
    		}
    	}
    	byte[] F = H.getByteDigest();
    	
    	H.reset();
    	byte[] mdPrime = null;
    	ZqElement c = null;
    	if (upti.isDeviceProtected()) {
    		H.update(UIDt);
    		H.update(a);
    		H.update(m);
    		H.update(F);
    		mdPrime = H.getByteDigest();
    		H.reset();
    		H.update(md);
    		H.update(mdPrime);
    		c = H.getZqDigest();
    	} else {
    		H.update(UIDt);
    		H.update(a);
    		H.update(m);
    		H.update(F);
    		c = H.getZqDigest();
    	}
    	
    	return new GenerateChallengeOutput(c, mdPrime);
    }

	static ZqElement genChallenge(IssuerParametersInternal ipi,
			byte[] md, byte[] mdPrime) {
		HashFunction H = ipi.getHashFunction();
		H.update(md);
		H.update(mdPrime);
		return H.getZqDigest();
	}
    
    /**
     * Returns the list of undisclosed attribute indices from the list of disclosed ones.
     * E.g., for numOfAttribs = 5 and disclosed = {1,3,5}, the returned value is {2,4}.
     * @param numOfAttribs total number of attributes
     * @param disclosed the list of disclosed attribute indices.
     * @return list of undisclosed attribute indices.
     */
    static int[] getUndisclosedIndices(int numOfAttribs, int[] disclosed) {
    	if (numOfAttribs < 0) {
    		throw new IllegalArgumentException("numOfAttribs must be greater or equal to 0.");
    	}
    	if (disclosed.length > numOfAttribs) {
    		throw new IllegalArgumentException("invalid size for disclosed.");
    	}
    	int[] undisclosed = new int[numOfAttribs - disclosed.length];
    	int d_index = 0;
    	int u_index = 0;
    	
    	for (int i=1; i<=numOfAttribs; i++) {
    		if (d_index < disclosed.length && // we don't want to run over
    			disclosed[d_index] == i) {
    			d_index++;
    		} else {
    			undisclosed[u_index++] = i; 
    		}
    	}
    	return undisclosed;
    }	

    static ZqElement[] getZqElementArray(FieldZq Zq, byte[][] encoded) throws IOException {
    	ZqElement[] elements = new ZqElement[encoded.length];
    	for (int i=0; i<encoded.length; i++) {
    		elements[i] = Zq.getPositiveElement(encoded[i]);
    	}
    	return elements;
    }

    static GroupElement[] getGroupElementArray(PrimeOrderGroup Gq, byte[][] encoded) throws IOException {
    	return Gq.getElementArray(encoded);
    }

    static byte[][] getEncodedArray(Element[] elements) {
    	byte[][] encoded = new byte[elements.length][];
    	for (int i=0; i<elements.length; i++) {
    		encoded[i] = elements[i].toByteArray();
    	}
    	return encoded;
    }
    
    /**
     */
    static public byte[] getMagnitude(BigInteger i) {
        if (i.signum() == 0) {
            // return a zero byte rather than an empty byte array
            return new byte[] { 0 };
        }
        // all non-zero elements are positive
        assert 1 == i.signum();
        // how many non-sign bits are there?
        final int bitLen = i.bitLength();
        assert bitLen > 0 : bitLen;
        // how many bytes should that take up?
        final int byteLen = (bitLen + 7) / 8;
        // get the two's complement representation
        final byte[] twosComp = i.toByteArray();
        // is there an extra leading byte for the sign bit?
        if (twosComp.length != byteLen) {
            // must be at least two bytes.
            assert twosComp.length > 1;
            // the most significant byte must be only the sign bit.
            assert 0 == twosComp[0] : twosComp[0];
            // the most significant bit of the next byte must be opposite
            assert 0 != (twosComp[1] & 0x80) : (twosComp[1] & 0x80);
            // strip off the extra byte holding the sign bit
            final byte[] retVal = new byte[twosComp.length - 1];
            System.arraycopy(twosComp, 1, retVal, 0, retVal.length);
            return retVal;
        }
        // else the most significant byte isn't only there for the sign bit
        return twosComp;
    }

}
