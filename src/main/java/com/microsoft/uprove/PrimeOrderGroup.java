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
 * Represents an immutable group of prime order.
 * <p>
 * In addition to implementing all abstract methods in this class, extenders
 * must provide an implementation of
 * {@link Hashable#addToDigest(com.microsoft.uprove.HashUpdater) addToDigest}
 * that adds the parameters that define the group to a hash. For example, the
 * subgroup implementation updates the digest with the
 * big-endian ordered two's complement encodings of its <code>P</code>,
 * <code>Q</code>, and <code>G</code> values.
 * </p>
 * <p>
 * The elements within the group are represented by an implementation of the
 * {@link GroupElement} interface.
 * </p>
 * <p>
 * Additionally, all groups possess an instance of the {@link FieldZq} class,
 * representing the field defined by the group's order.
 * </p>
 */
public abstract class PrimeOrderGroup implements Hashable {

    private final FieldZq Zq;

    /**
     * Constructor for use by implementors.
     * @param q the group's prime order.
     * @throws NullPointerException if <code>q</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>q</code> is not a positive
     * prime number.
     */
    protected PrimeOrderGroup(final BigInteger q) {
        // we may be called by user code (our implementors), so make a
        // defensive copy of q
        this.Zq = FieldZq.getInstance(new BigInteger(q.toByteArray()));
    }

    /**
     * Returns the order of the group.
     * @return the order of the group.
     */
    public final BigInteger getOrder() {
        return Zq.getQ();
    }

    /**
     * Returns the generator of the group.
     * <p>
     * Implementations MUST return an instance of a construction-specific
     * element class that is a generator within the group. Additionally, the
     * instance returned MUST be distinct from any other element instances
     * since the <code>GroupElement</code> interface defines a mutable and
     * erasable object.
     * </p>
     * @return the generator of the group. Ownership of the referent is given
     * to the caller.
     */
    public abstract GroupElement getGenerator();

    /**
     * Returns the identity element of the group.
     * <p>
     * Implementations MUST return an instance of a construction-specific
     * element class that holds the group's identity element. Additionally, the
     * instance returned MUST be distinct from any other element instances
     * since the <code>GroupElement</code> interface defines a mutable and
     * erasable object.
     * </p>
     * @return the identity element of the group. Ownership of the referent is
     * given to the caller.
     */
    public abstract GroupElement getIdentity();

    /**
     * Returns the field <code>Z<sub>q</sub></code> defined by the group's
     * order.
     * @return the group's <code>Z<sub>q</sub></code> field.
     */
    public final FieldZq getZq() {
        return Zq;
    }

    /**
     * Returns a <code>GroupElement</code> with the value encoded in the
     * given byte array.
     * <p>
     * Implementations MUST return an instance of a construction-specific
     * element class that holds the value encoded in <code>data</code>. If
     * the format of the bytes in <code>data</code> is invalid or if the
     * value decoded from <code>data</code> does not represent an element in
     * <code>this</code> group, the implementation MUST throw a
     * <code>IOException</code>.
     * </p>
     * @param data group element data. The caller's ownership of the referent
     * is preserved.
     * @return an element in the group. Ownership of the referent is given to
     * the caller.
     * @throws IOException if <code>data</code> is
     * malformed, or does not represent an element within <code>this</code>
     * group.
     * @see GroupElement#toByteArray()
     */
    public abstract GroupElement getElement(byte[] data)
            throws IOException;

    /**
     * Returns an array of <code>GroupElement</code> instances with the values
     * indicated in the provided array of encoded group elements.
     * @param specs an array of encoded group elements. The caller's ownership
     * of the referent is preserved.
     * @return an array of <code>GroupElement</code> instances. Ownership of
     * the referent is given to the caller.
     * @throws IOException if any element in
     * <code>specs</code> is malformed or does not represent an element within
     * <code>this</code> group.
     * @see GroupElement#toByteArray()
     * @see #getElement(byte[])
     */
    public final GroupElement[] getElementArray(final byte[][] specs)
            throws IOException {
        final int len = specs.length;
        final GroupElement[] retVal = new GroupElement[len];
        for (int i = 0; i < len; ++i) {
            retVal[i] = getElement(specs[i]);
        }
        return retVal;
    }

    /**
     * Returns an array of encoded group elements corresponding to the
     * elements in the provided array.
     * @param elements an array of <code>GroupElement</code> instances.
     * The caller's ownership of the referent is preserved.
     * @return an array of encoded group elements. Ownership of the referent is
     * given to the caller.
     * @throws IllegalArgumentException if the list contains elements that
     * belong to a group other than <code>this</code>.
     * @see GroupElement#toByteArray()
     */
    public final byte[][] getEncodedElements(final GroupElement[] elements) {
        // create the resulting array
        byte[][] retVal = new byte[elements.length][];
        // populate it, checking each item as we go
        for (int i = 0; i < elements.length; i++) {
            final GroupElement elem = elements[i];
            if (elem.getGroup() != this) {
                throw new IllegalArgumentException();
            }
            retVal[i] = elem.toByteArray();
        }
        return retVal;
    }

    /**
     * Returns the size (in bytes) of the largest encoded element.
     * @return the size (in bytes) of the largest encoded element.
     */
    public abstract int getMaxEncodedElementSize();

    /**
     * Indicates whether some other object is "equal to" this group.
     * <p>
     * This method returns <code>true</code> if <code>obj</code> is a
     * <code>PrimeOrderGroup</code> of the same prime order as
     * <code>this</code> group. Subclasses MUST override this method with
     * their own implementation that ensures that <code>obj</code> is of the
     * same type as <code>this</code> and that it was constructed with the same
     * parameters as <code>this</code>.
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this group is the same as the obj argument;
     * <code>false</code> otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PrimeOrderGroup)) {
            return false;
        }
        PrimeOrderGroup pog = (PrimeOrderGroup) obj;
        return Zq.equals(pog.Zq);
    }

    /**
     * Returns a hash code value for the group.
     * <p>
     * This method computes a hash based on the value of the group's prime
     * order. Subclasses MUST override this method with their own
     * implementation that ensures that it obeys the contract between
     * {@link Object#hashCode()} and {@link Object#equals(java.lang.Object)}.
     * </p>
     * @return {@inheritDoc}
     */
    public int hashCode() {
        return 421 * 163 + Zq.hashCode();
    }

    /**
     * Validates that the group is well-formed, i.e. that the order
     * <code>q</code> is prime, that the generator is a member of the group,
     * and any other checks required by a particular group construction.
     * @throws IllegalStateException if the group is malformed.
     */
    public final void validate() {
        // the constructor ensures that q is in range

        // validate the exponent field (i.e. q is prime)
        Zq.validate();

        // delegate group-specific validation to the implementation
        doGroupSpecificValidate();
    }

    /**
     * Invoked during group validation to allow implementations to provide
     * group-specific validation.
     * @throws IllegalStateException if the group is malformed.
     */
    protected abstract void doGroupSpecificValidate();

}
