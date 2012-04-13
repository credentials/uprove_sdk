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

/**
 * Specifies a presentation proof.
 */
public class PresentationProof {
	private byte[][] disclosedAttributes;
	private byte[] a;
	private byte[] r0;
	private byte[][] r;
	private byte[] rd;
	
	/**
	 * Constructs a presentation proof.
	 * @param disclosedAttributes the ordered list of disclosed attribute indices.
	 * @param a the <code>a</code> value.
	 * @param r0 the <code>r0</code> value.
	 * @param r the <code>r</code> value.
	 * @param rd the <code>rd</code> value.
	 */
	public PresentationProof(byte[][] disclosedAttributes, byte[] a, byte[] r0, byte[][] r, byte[] rd) {
		super();
		this.disclosedAttributes = disclosedAttributes;
		this.a = a;
		this.r0 = r0;
		this.r = r;
		this.rd = rd;
	}

	/**
	 * Gets the ordered list of disclosed attribute indices.
	 * @return the disclosedAttributes the disclosed attribute indices.
	 */
	public byte[][] getDisclosedAttributes() {
		return disclosedAttributes;
	}

	/**
	 * Sets the ordered list of disclosed attribute indices.
	 * @param disclosedAttributes the disclosed attribute indices.
	 */
	public void setDisclosedAttributes(byte[][] disclosedAttributes) {
		this.disclosedAttributes = disclosedAttributes;
	}

	/**
	 * Gets the <code>a</code> value.
	 * @return the <code>a</code> value.
	 */
	public byte[] getA() {
		return a;
	}

	/**
	 * Sets the <code>a</code> value.
	 * @param a the <code>a</code> value.
	 */
	public void setA(byte[] a) {
		this.a = a;
	}

	/**
	 * Gets the <code>r0</code> value.
	 * @return the <code>r0</code> value.
	 */
	public byte[] getR0() {
		return r0;
	}

	/**
	 * Sets the <code>r0</code> value.
	 * @param r0 the <code>r0</code> value.
	 */
	public void setR0(byte[] r0) {
		this.r0 = r0;
	}

	/**
	 * Gets the <code>r</code> value.
	 * @return the <code>r</code> value.
	 */
	public byte[][] getR() {
		return r;
	}

	/**
	 * Sets the <code>r</code> value.
	 * @param r the <code>r</code> value.
	 */
	public void setR(byte[][] r) {
		this.r = r;
	}

	/**
	 * Gets the <code>rd</code> value.
	 * @return the <code>rd</code> value.
	 */
	public byte[] getRd() {
		return rd;
	}

	/**
	 * Sets the <code>rd</code> value.
	 * @param rd the <code>rd</code> value.
	 */
	public void setRd(byte[] rd) {
		this.rd = rd;
	}

	/**
     * Indicates whether some other object is "equal to" this one.
     * @param o the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the
     * <code>o</code> argument; <code>false</code> otherwise.
     */
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PresentationProof)) {
            return false;
        }

        PresentationProof pp = (PresentationProof) o;

        return
        	ByteArrays.equals(this.disclosedAttributes, pp.disclosedAttributes) &&
        	Arrays.equals(this.a, pp.a) &&
        	Arrays.equals(this.r0, pp.r0) &&
        	ByteArrays.equals(this.r, pp.r) &&
        	Arrays.equals(this.rd, pp.rd);
    }

    /**
     * Returns a hash code value for the object.
     * @return a hash code value for the object.
     */
    public int hashCode() {
    	int result = 491;
    	result = 37 * result + ByteArrays.hashCode(this.disclosedAttributes);
    	result = 37 * result + Arrays.hashCode(this.a);
    	result = 37 * result + Arrays.hashCode(this.r0);
    	result = 37 * result + ByteArrays.hashCode(this.r);
    	result = 37 * result + Arrays.hashCode(this.rd);
        return result;
    }
}
