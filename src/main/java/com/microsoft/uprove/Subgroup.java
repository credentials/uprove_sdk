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


/**
 * Implements the subgroup construction, which is a
 * subgroup of prime order <code>q</code> of
 * Z<code><sub>p</sub><sup>*</sup></code>.
 */
public final class Subgroup extends PrimeOrderGroup {

    /**
     * The group's <code>p</code> prime modulus.
     */
    private final BigInteger p;

    /**
     * The group's generator.
     */
    private final BigInteger g;

    /**
     * Constructs a new <code>Subgroup</code>.
     * @param p the encoded <code>p</code> value.
     * @param q the encoded <code>q</code> value.
     * @param g the encoded <code>g</code> value.
     */
    public Subgroup(final byte[] p, final byte[] q, final byte[] g) {
    	super(new BigInteger(1, q));
    	this.p = new BigInteger(1, p);
    	this.g = new BigInteger(1, g);
        validatePQG();
    }
    
    /**
     * Constructs the group <code>Subgroup</code> of prime order q.
     * Note that <code>p</code> and <code>q</code> should be prime numbers,
     * <code>q</code> should divide <code>(p-1)</code>, and <code>g</code>
     * should be a group member different than 1 (and therefore a generator).
     * None of these checks are performed by the constructor, as they are
     * expensive operations and should only be performed once (not every time
     * the values are used to instantiate a <code>Subgroup</code>
     * object). The method {@link #validate()} can be used to perform such a
     * validation.
     * @param p the group's prime modulus <code>p</code>.
     * @param q the group's prime order <code>q</code>.
     * @param g the group's generator <code>g</code>.
     * @throws IllegalArgumentException if <code>p</code>, <code>q</code>
     * or <code>g</code> are not greater than 1 or if <code>g</code> is not
     * less than <code>p</code>.
     */
    public Subgroup(final BigInteger p, final BigInteger q, final BigInteger g) {
        super(q);
        this.p = p;
        this.g = g;
        validatePQG();
    }

    private void validatePQG() {
        // the PrimeOrderGroup constructor verifies that q is > 1
    	
    	// check that p > 1
        if (p.compareTo(BigInteger.ONE) <= 0) {
            throw new IllegalArgumentException(
                    "Invalid value for p (smaller or equal to 1)");
        }

        // check that 1 < g < p
        if (g.compareTo(BigInteger.ONE) <= 0) {
            throw new IllegalArgumentException(
                    "Invalid value for g (smaller or equal to 1)");
        }
        if (g.compareTo(p) >= 0) {
            throw new IllegalArgumentException(
                    "Invalid value for g (bigger or equal to p)");
        }
    }
    
    /**
     * Returns a big integer representing an element's value.
     * @param element an element in a <code>Subgroup</code>.
     * @return a big integer representing the element's value.
     * @throws ClassCastException if <code>element</code> is not an element
     * of a <code>Subgroup</code>.
     */
    public static BigInteger getElementValue(final GroupElement element)
            throws ClassCastException {
        return ((ModInteger) element).toBigInteger();
    }

    /* (non-Javadoc)
     * @see com.microsoft.uprove.math.PrimeOrderGroup#getIdentity()
     */
    public GroupElement getIdentity() {
        return new ModInteger(BigInteger.ONE);
    }

    /* (non-Javadoc)
     * @see com.microsoft.uprove.math.PrimeOrderGroup#getGenerator()
     */
    public GroupElement getGenerator() {
        return new ModInteger(g);
    }

    /**
     * Returns the parameter <code>p</code> of the group as a BigInteger.
     * @return the parameter <code>p</code> of the group as a BigInteger.
     */
    public BigInteger getP() {
        return p;
    }

    /**
     * Returns the parameter <code>q</code> of the group as a BigInteger.
     * @return the parameter <code>q</code> of the group as a BigInteger.
     */
    public BigInteger getQ() {
        return getOrder();
    }

    /**
     * Returns the parameter <code>g</code> of the group as a BigInteger.
     * @return the parameter <code>g</code> of the group as a BigInteger.
     */
    public BigInteger getG() {
        return g;
    }

    /* (non-Javadoc)
     * @see com.microsoft.uprove.math.PrimeOrderGroup#getElement(byte[])
     */
    public GroupElement getElement(final byte[] data)
            throws IOException {
        try {
            return new ModInteger(SubgroupUtil
                    .decodeElementValue(data));
        } catch (IllegalArgumentException iae) {
            throw new IOException(iae.getMessage());
        }
    }

    /**
     * Checks that the given integer belongs to the group, i.e.
     * checks that <code>0 &lt; i &lt; p</code> and that
     * <code>i^q (mod p) = 1</code>.
     * @param i the <code>BigInteger</code> to test.
     * @return <code>true</code> if the integer belongs to the group,
     * <code>false</code> otherwise.
     */
    boolean isValidElementValue(final BigInteger i) {
        return
            // check that i > 0
            i.signum() > 0
            // check that i < p
            && i.compareTo(p) < 0
            // check that i^q (mod p) == 1
            && i.modPow(getOrder(), p).equals(BigInteger.ONE);
    }

    /* (non-Javadoc)
     * @see com.microsoft.uprove.math.PrimeOrderGroup#getMaxEncodedElementSize()
     */
    public int getMaxEncodedElementSize() {
        return SubgroupUtil.getEncodedElementSize(p.bitLength());
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
        if (!(o instanceof Subgroup) || !super.equals(o)) {
            return false;
        }

        final Subgroup G = (Subgroup) o;
        return p.equals(G.p) && g.equals(G.g);
    }

    /**
     * Returns a hash code value for the object.
     * @return a hash code value for the object.
     */
    public int hashCode() {
        int retVal = 83;
        retVal = retVal * 593 + p.hashCode();
        retVal = retVal * 593 + super.hashCode();
        retVal = retVal * 593 + g.hashCode();
        return retVal;
    }

    /* (non-Javadoc)
     * @see com.microsoft.uprove.crypto.Hashable#addToDigest(com.microsoft.uprove.crypto.HashUpdater)
     */
    public void addToDigest(final HashUpdater dv) {
        dv.update(p);
        dv.update(getOrder()); // q
        dv.update(g);
    }

    /**
     * Represents an integer of a Subgroup.
     */
    private final class ModInteger implements GroupElement {

        /*
         * Note that this class uses an immutable BigInteger, which is not
         * ideal for fast crypto operations and to erase the content.
         * We plan to migrate to a mutable big integer class eventually.
         */
        private BigInteger integer;

        /**
         * Constructs a ModInteger. The constructor simply checks that
         * the integer is between zero and <code>p</code>, exclusive;
         * not that it has order <code>q</code> (i.e. that
         * <code>integer^q = 1</code>), since this is an expensive
         * operation. Therefore, it's possible to construct a
         * modular integer that is not part of the group. The method
         * {@link GroupElement#isValid()} can be used to verify group
         * membership.
         * @param integer integer value of the element.
         * @throws IllegalArgumentException if integer is smaller or
         * equal to 0, or is bigger or equal to <code>p</code>.
         * @see GroupElement#isValid()
         */
        ModInteger(final BigInteger integer) {
            if (integer.signum() <= 0 || integer.compareTo(p) >= 0) {
                throw new IllegalArgumentException(
                        "Integer not in group range: " + integer);
            }
            this.integer = integer;
        }

        /**
         * @return a <code>BigInteger</code> representation of this
         * <code>ModInteger</code>.
         */
        public BigInteger toBigInteger() {
            return integer;
        }

        /* (non-Javadoc)
         * @see com.microsoft.uprove.math.Element#length()
         */
        public int length() {
            return SubgroupUtil.getEncodedElementSize(integer.bitLength());
        }

        /* (non-Javadoc)
         * @see com.microsoft.uprove.math.GroupElement#toByteArray()
         */
        public byte[] toByteArray() {
            return SubgroupUtil.encodeElementValue(integer);
        }

        /* (non-Javadoc)
         * @see com.microsoft.uprove.math.GroupElement#getGroup()
         */
        public PrimeOrderGroup getGroup() {
            return Subgroup.this;
        }

        /* (non-Javadoc)
         * @see com.microsoft.uprove.math.GroupElement#multiply(com.microsoft.uprove.math.GroupElement)
         */
        public GroupElement multiply(final GroupElement b) {
            if (!b.getGroup().equals(Subgroup.this)) {
                throw new IllegalArgumentException("b is not in same group");
            }
            return new ModInteger(integer.multiply(((ModInteger) b).integer)
                    .mod(p));
        }

        /* (non-Javadoc)
         * @see com.microsoft.uprove.math.GroupElement#multiplyAssign(com.microsoft.uprove.math.GroupElement)
         */
        public GroupElement multiplyAssign(final GroupElement b) {
            if (!b.getGroup().equals(Subgroup.this)) {
                throw new IllegalArgumentException("b is not in same group");
            }
            integer = integer.multiply(((ModInteger) b).integer).mod(p);
            return this;
        }

        /* (non-Javadoc)
         * @see com.microsoft.uprove.math.GroupElement#exponentiate(com.microsoft.uprove.math.FieldZq.ZqElement)
         */
        public GroupElement exponentiate(final FieldZq.ZqElement n) {
            return new ModInteger(integer.modPow(n.toBigInteger(), p));
        }

        /* (non-Javadoc)
         * @see com.microsoft.uprove.math.GroupElement#exponentiateAssign(com.microsoft.uprove.math.FieldZq.ZqElement)
         */
        public GroupElement exponentiateAssign(final FieldZq.ZqElement n) {
            integer = integer.modPow(n.toBigInteger(), p);
            return this;
        }

        /* (non-Javadoc)
         * @see com.microsoft.uprove.math.GroupElement#inverse()
         */
        public GroupElement inverse() {
            return new ModInteger(integer.modInverse(p));
        }

        /* (non-Javadoc)
         * @see com.microsoft.uprove.math.GroupElement#inverseAssign()
         */
        public GroupElement inverseAssign() {
            integer = integer.modInverse(p);
            return this;
        }

        /**
         * Returns the hexadecimal representation of this ModInteger.
         * @return the hexadecimal representation of this ModInteger.
         */
        public String toString() {
            return integer.toString(16);
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
            if (!(o instanceof ModInteger)) {
                return false;
            }
            ModInteger mi = (ModInteger) o;
            return integer.equals(mi.integer);
        }

        /**
         * Returns a hash code value for the object.
         * @return a hash code value for the object.
         */
        public int hashCode() {
            return integer.hashCode();
        }

        /* (non-Javadoc)
         * @see com.microsoft.uprove.crypto.Hashable#addToDigest(com.microsoft.uprove.crypto.HashUpdater)
         */
        public void addToDigest(final HashUpdater dv) {
            dv.update(integer);
        }

        /* (non-Javadoc)
         * @see com.microsoft.uprove.math.GroupElement#isValid()
         */
        public boolean isValid() {
            return Subgroup.this.isValidElementValue(integer);
        }

    }


    /**
     * Validates that the group is well-formed, i.e. that
     * the order p is a prime number, that q | (p - 1),
     * that g is not equal to 1 and is a member of the group (has order q).
     * @throws IllegalStateException if the group is malformed.
     */
    protected void doGroupSpecificValidate() throws IllegalStateException {
        // the constructor ensures that p and g are in range

        // verify that p is prime
        if (!p.isProbablePrime(ConfigImpl.getPrimeConfidenceLevel())) {
            throw new IllegalStateException(
                    "Invalid value for p (not a prime)");
        }

        // verification that q is prime is done while validating Zq

        final BigInteger q = getOrder();

        // verify that q | (p - 1)
        if (p.subtract(BigInteger.ONE).remainder(q).signum() != 0) {
            throw new IllegalStateException(
                    "Invalid value for p and q, q does not divide p - 1");
        }

        // 1 < g < p is checked by constructor (g is final, so no danger)

        // verify that g^q == 1 to make sure the generator is in the group
        if (g.modPow(q, p).compareTo(BigInteger.ONE) != 0) {
            throw new IllegalStateException(
                    "Invalid value for g (not of order q)");
        }
    }

}

