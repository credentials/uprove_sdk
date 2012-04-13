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
 * Represents a mutable and erasable element in a group.
 * <p>
 * In the context of this class, the group operation is referred to as
 * "multiplication", although the exact implementation of this operation is
 * dependent on the construction of the specific group to which the element
 * belongs.
 * </p>
 * <p>
 * Implementations of group constructions must provide their own
 * construction-specific class representing elements within the group that
 * implements this interface.
 * </p>
 * <p>
 * In addition to implementing all methods in this interface, implementations
 * must provide an implementation of
 * {@link com.microsoft.uprove.Hashable#addToDigest(com.microsoft.uprove.HashUpdater) addToDigest}
 * that adds the element's value to a hash. For example, the
 * subgroup implementation updates the digest with the big-endian ordered
 * two's complement encoding of the element's value.
 * </p>
 */
interface GroupElement extends Element {

    /**
     * Returns the mathematical group to which this element belongs.
     * @return the element's mathematical group.
     */
    PrimeOrderGroup getGroup();

    /**
     * Returns an encoding of the group element in a byte array.
     * @return a byte array holding the group element's value. Ownership of the
     * referent is given to the caller.
     * @see PrimeOrderGroup#getElement(byte[])
     */
    byte[] toByteArray();

    /**
     * Returns a <code>GroupElement</code> whose value is
     * <code>this * val</code>.
     * @param val value to multiply to this <code>GroupElement</code>.
     * @return <code>this * val</code>.
     */
    GroupElement multiply(GroupElement val);

    /**
     * Assigns the value <code>this * val</code> to this
     * <code>GroupElement</code>.
     * @param val value to multiply to this <code>GroupElement</code>.
     * @return <code>this = this * val</code>.
     */
    GroupElement multiplyAssign(GroupElement val);

    /**
     * Returns a <code>GroupElement</code> whose value is
     * <code>this ^ val</code>.
     * @param val the exponent.
     * @return <code>this ^ val</code>.
     */
    GroupElement exponentiate(FieldZq.ZqElement val);

    /**
     * Assigns the value <code>this ^ val</code> to this
     * <code>GroupElement</code>.
     * @param val the exponent.
     * @return <code>this = this ^ val</code>.
     */
    GroupElement exponentiateAssign(FieldZq.ZqElement val);

    /**
     * Returns the inverse of this <code>GroupElement</code>.
     * @return <code>this ^ (-1)</code>.
     */
    GroupElement inverse();

    /**
     * Assigns the inverse of this <code>GroupElement</code> to
     * <code>this</code>.
     * @return <code>this = this ^ (-1)</code>.
     */
    GroupElement inverseAssign();

    /**
     * Checks that this <code>GroupElement</code> is a valid member of its
     * mathematical group; i.e. checks that <code>this^q == 1</code>.
     * @return <code>true</code> if the element is part of the group,
     * <code>false</code> otherwise.
     */
    boolean isValid();

}
