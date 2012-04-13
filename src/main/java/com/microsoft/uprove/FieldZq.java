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
 * Class representing the field of integers modulo a prime number
 * <code>q</code>.
 * <p>
 * This is primarily an internal class used by the SDK's implementation. It is
 * exposed for developers of new {@link PrimeOrderGroup} implementations, as
 * they must support exponentiation of their group's elements to elements of a
 * <code>FieldZq</code> whose <code>q</code> is equal to the group's prime
 * order.
 * </p>
 */
final class FieldZq {

    private final BigInteger q;

    /**
     * Constructs the field of integers modulo <code>q</code>, which should
     * be a prime number. Note that the primality of <code>q</code> is not
     * check by the constructor, since it only needs to be checked once (and
     * not every time the same value is used to instantiate a
     * <code>FieldZq</code> object). It's up to the caller to validate
     * primality of <code>q</code>.
     * @param q number specifying the size of the field.
     * @throws NullPointerException if <code>q</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>q</code> is smaller or equal
     * to 1.
     */
    private FieldZq(final BigInteger q) {
        // check that q > 1
        if (q.compareTo(BigInteger.ONE) <= 0) {
            throw new IllegalArgumentException(
                    "Invalid value for q (negative or zero)");
        }
        this.q = q;
    }

    /**
     * Generates an instance of the field Zq.
     * @param q number specifying the size of the field.
     * @return a new field instance.
     * @throws NullPointerException if <code>q</code> is <code>null</code>.
     */
    static FieldZq getInstance(final BigInteger q) {
        return new FieldZq(q);
    }

    /**
     * Returns the size <code>q</code> of the field.
     * @return size <code>q</code> of the field.
     */
    public BigInteger getQ() {
        // NOTE: no need for a defensive copy since BigInteger is immutable.
        return q;
    }

    /**
     * Returns an element of the field of value <code>0</code>.
     * @return an element of the field of value <code>0</code>. Ownership of
     * the referent is given to the caller.
     */
    public ZqElement getZero() {
        return new ZqElement(BigInteger.ZERO);
    }

    /**
     * Returns an element of the field of value <code>1</code>.
     * @return an element of the field of value <code>1</code>. Ownership of
     * the referent is given to the caller.
     */
    public ZqElement getOne() {
        return new ZqElement(BigInteger.ONE);
    }

    /**
     * Makes a defensive copy of a <code>BigInteger</code> originating from
     * outside.
     * @param i an integer.
     * @return a copy of <code>i</code>.
     */
    private static BigInteger makeSafe(final BigInteger i) {
        if (i.getClass() == BigInteger.class) {
            // it's already a safe instance, no need to copy
            return i;
        }
        final byte[] buffer = i.toByteArray();
        final BigInteger retVal = new BigInteger(buffer);
        // clear the temporary buffer in case this is sensitive data
        ByteArrays.erase(buffer);
        return retVal;
    }

    /**
     * Determines an element in the field of value <code>i mod q</code>.
     * <p><b>NOTE:</b> As indicated, the value of the element returned will
     * not be equal to <code>i</code> if <code>i</code> lies outside the
     * field.</p>
     *
     * @param i a value from which an element in the field is to be derived.
     * The caller's ownership of the referent is preserved.
     * @return field element <code>i mod q</code>. Ownership of the referent is
     * given to the caller.
     * @throws NullPointerException if <code>i</code> is <code>null</code>.
     */
    public ZqElement determineElement(final BigInteger i) {
        // make a defensive copy of i since ZqElement's constructor will
        // invoke mod() on its input parameter, and we don't know for sure
        // that it won't return an instance of a malicious class derived from
        // BigInteger.
        return new ZqElement(makeSafe(i));
    }

    /**
     * Returns an element in the field with the value specified by
     * <code>i</code>.
     * @param i the value of the desired field element.
     * @return a field element of value <code>i</code>. Ownership of the
     * referent is given to the caller.
     * @throws IOException if <code>i</code> lies
     * outside the field.
     */
    private ZqElement getElementSafe(final BigInteger i)
            throws IOException {
        // negative values or values greater or equal to q are invalid
        if (i.signum() < 0 || i.compareTo(getQ()) >= 0) {
            throw new IOException("FieldZq.ZqElement value out of bounds");
        }
        return new ZqElement(i);
    }

    /**
     * Returns an element in the field with the value specified by
     * <code>i</code>.
     * @param i the value of the desired field element.
     * @return a field element of value <code>i</code>. Ownership of the
     * referent is given to the caller.
     * @throws NullPointerException if <code>i</code> is <code>null</code>.
     * @throws IOException if <code>i</code> lies
     * outside the field.
     * @see ZqElement#toBigInteger()
     */
    public ZqElement getElement(final BigInteger i)
            throws IOException {
        return getElementSafe(makeSafe(i));
    }

    /**
     * Constructs a new element from the given byte array.
     * @param isUnsigned if <code>true</code>, <code>data</code> does not
     * carry a sign bit, and is therefore interpreted as the magnitude of
     * a positive element; otherwise, it is interpreted as the two's
     * complement representation of an element.
     * @param data an encoding of an element. The caller's ownership of the
     * referent is preserved.
     * @return a new element.
     * @throws NullPointerException if <code>data</code> is
     * <code>null</code>.
     * @throws IOException if the value is out of range.
     */
    private ZqElement getElement(final boolean isUnsigned,
            final byte[] data) throws IOException {
        if (!isUnsigned && data.length == 0) {
            // BigInteger's byte[] ctor would throw an NFE
            throw new IOException(
                    "Zero-length encoded FieldZq.ZqElement value");
        }
        final BigInteger i = isUnsigned
                ? new BigInteger(1, data)
                : new BigInteger(data);
        return getElementSafe(i);
    }

    /**
     * Returns an element in the field with the value specified by
     * <code>data</code>.
     * @param data the two's complement value, in big-endian byte-order, of
     * the desired field element. The caller's ownership of the referent is
     * preserved.
     * @return a field element of value <code>data</code>. Ownership of the
     * referent is given to the caller.
     * @throws NullPointerException if <code>data</code> is <code>null</code>.
     * @throws IOException if <code>data</code> lies
     * outside the field, or cannot be converted into a value.
     * @see ZqElement#toByteArray()
     */
    public ZqElement getElement(final byte[] data)
            throws IOException {
        // getElement will ensure that the element has the same value
        // as the given byte array.
        return getElement(false, data);
    }

    /**
     * Returns an element in the field with the value specified by
     * <code>data</code>.
     * @param data big-endian unsigned integer value of the desired
     * field element. The caller's ownership of the referent is preserved.
     * @return a field element of value <code>data</code>. Ownership of the
     * referent is given to the caller.
     * @throws NullPointerException if <code>data</code> is <code>null</code>.
     * @throws IOException if <code>data</code> lies
     * outside of the field, or cannot be converted into a value.
     * @see ZqElement#getMagnitude()
     */
    public ZqElement getPositiveElement(final byte[] data)
            throws IOException {
        // getElement will ensure that the element has the same value
        // as the given byte array.
        return getElement(true, data);
    }

    /**
     * Determines an element in the field of value <code>e mod q</code>.
     * <p>
     * <b>NOTE:</b> As indicated, the value of the element returned will not
     * be equal to <code>e</code> if <code>e</code> lies outside of this
     * field.
     * </p>
     * @param e element of a field. The caller's ownership of the referent is
     * preserved.
     * @return an element of value <code>e mod q</code>. Ownership of the
     * referent is given to the caller.
     * @throws NullPointerException if <code>e</code> is <code>null</code>.
     */
    public ZqElement determineElement(final ZqElement e) {
        // ZqElement is final, so we don't need to make a defensive copy.
        return new ZqElement(e.toBigInteger());
    }

    /**
     * Returns a random element in the field, with uniform distribution.
     * @param nonZero <code>true</code> to indicate that the result must
     * not be equal to zero.
     * @return an element in the field chosen uniformly at random.
     * Ownership of the referent is given to the caller.
     */
    public ZqElement getRandomElement(final boolean nonZero) {
        BigInteger bi;
        do {
            bi = RandomSourceImpl.getRandomBigInteger(q.bitLength());
        } while (
                // Make sure the value is smaller than q. The constructor
                // of ZqElement mods the value to make it a Zq element.
                // If values between q and 2^(q.bitLength) were allowed,
                // they would get mapped to Zq elements and this would
                // introduce a bias in the RNG. This would open up
                // attacks on the system (see Bleichenbacher's attack
                // on FIPS 186's RNG). This technique is equivalent to
                // NIST SP 800-90 (draft)'s "simple discard method"
                // (section B.5.1.1). Maybe a future version will
                // implement the "simple modular method" (section B.5.1.3)
                // if the ratio q/2^(q.bitLength) is close to 0.5
                (bi.compareTo(q) >= 0)
                // Make sure that if nonZero is true, that the value
                // is not zero.
                || (nonZero && bi.signum() == 0));
        return new ZqElement(bi);
    }

    /**
     * Returns an array of random element in the field, with uniform
     * distribution.
     * @param n number of elements to return, must be greater than zero.
     * @param nonZero <code>true</code> to indicate that the elements must
     * not be equal to zero.
     * @return an array of random elements. Ownership of the referent and its
     * contents are given to the caller.
     */
    public ZqElement[] getRandomElements(final int n, final boolean nonZero) {
        if (n <= 0) {
            throw new IllegalArgumentException(
                    "n must be positive and greater than zero");
        }
        ZqElement[] values = new ZqElement[n];
        for (int i = 0; i < n; i++) {
            values[i] = getRandomElement(nonZero);
        }
        return values;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the
     * <code>obj</code> argument; <code>false</code> otherwise.
     */
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FieldZq)) {
            return false;
        }
        FieldZq f = (FieldZq) obj;

        return q.equals(f.q);
    }

    /**
     * Returns a hash code value for the object.
     * @return a hash code value for the object.
     */
    public int hashCode() {
        return q.hashCode();
    }

    /**
     * Returns an array of <code>ZqElement</code> objects corresponding to
     * the given <code>byte[]</code> array elements.
     * @param values array of <code>byte[]</code> to convert. The caller's
     * ownership of the referent is preserved.
     * @return an array of <code>ZqElement</code>. Ownership of the referent
     * and its contents are given to the caller.
     * @throws IOException if the data in the
     * <code>values</code> array cannot be converted into elements.
     * @see #getElement(byte[])
     */
    public ZqElement[] getElementArray(final BigInteger[] values)
            throws IOException {
        ZqElement[] array = new ZqElement[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = getElement(values[i]);
        }
        return array;
    }

    /**
     * Returns the size (in bytes) of the largest encoded element.
     * @return the size (in bytes) of the largest encoded element.
     */
    public int getMaxEncodedElementSize() {
        // add one to account for the added sign bit
        // add seven to round up if needed (int division truncates, of course)
        return (q.bitLength() + 8) / 8;
    }

    /**
     * Validates that the field is well-formed, i.e. that <code>q</code> is
     * prime.
     * @throws IllegalStateException if the field is malformed.
     */
    public void validate() {
        // the constructor ensures that q > 1

        // verify that q is prime
        if (!q.isProbablePrime(ConfigImpl.getPrimeConfidenceLevel())) {
            throw new IllegalStateException(
                    "Invalid value for q (not a prime)");
        }
    }

    /**
     * Represents an element in a <code>FieldZq</code>.
     * <p>
     * This is primarily an internal class used by the SDK's implementation. It
     * is exposed for developers of new {@link PrimeOrderGroup}
     * implementations, as they must support exponentiation of their group's
     * elements by elements of a <code>FieldZq</code> whose <code>q</code>
     * is equal to the group's prime order.
     * </p>
     */
    public final class ZqElement implements Element {
        private BigInteger i;

        /**
         * Constructs a ZqElement.
         * @param i integer representation of the element.
         */
        ZqElement(final BigInteger i) {
            // optimization: only i.mod(q) if (i lt zero) or (i ge q)
            final BigInteger qVal = getQ();
            this.i = ((i.signum() >= 0 && i.compareTo(qVal) < 0)
                      ? i : i.mod(qVal));
        }

        /**
         * Copy constructor of a ZqElement.
         * <p>
         * If <code>element</code> belongs to a <code>FieldZq</code> with a
         * larger value for <code>q</code> than the field in which the new
         * instance is to be created, the value of the new instance may not
         * match that of <code>element</code>.
         * </p>
         * @param element an element.
         * @throws NullPointerException if <code>element</code> is
         * <code>null</code>.
         */
        public ZqElement(final ZqElement element) throws NullPointerException {
            // no need for a defensive copy since element is final and
            // BigInteger is immutable.
            this(element.i);
        }

        /**
         * Returns the field to which this element belongs.
         * @return this element's <code>FieldZq</code>.
         */
        public FieldZq getField() {
            return FieldZq.this;
        }

        /**
         * Returns a ZqElement whose value is <code>this + val</code>.
         * @param val value to add to this ZqElement.
         * @return <code>this + val</code>.
         * @throws IllegalArgumentException if <code>val</code> belongs to a
         * field with a different <code>q</code>.
         */
        public ZqElement add(final ZqElement val) {
            if (!val.getField().equals(FieldZq.this)) {
                throw new IllegalArgumentException();
            }
            return new ZqElement(i.add(val.i)); // constructor will "mod q" it
        }

        /**
         * Assigns the value <code>this + val</code> to this ZqElement.
         * @param val value to add to this ZqElement.
         * @return <code>this = this + val</code>.
         * @throws IllegalArgumentException if <code>val</code> belongs to a
         * field with a different <code>q</code>.
         */
        public ZqElement addAssign(final ZqElement val) {
            if (!val.getField().equals(FieldZq.this)) {
                throw new IllegalArgumentException();
            }

            i = i.add(val.i).mod(getQ());
            return this;
        }

        /**
         * Returns a ZqElement whose value is <code>this - val</code>.
         * @param val value to subtract from this ZqElement.
         * @return <code>this - val</code>.
         * @throws IllegalArgumentException if <code>val</code> belongs to a
         * field with a different <code>q</code>.
         */
        public ZqElement subtract(final ZqElement val) {
            if (!val.getField().equals(FieldZq.this)) {
                throw new IllegalArgumentException();
            }

            // constructor will "mod q" result
            return new ZqElement(i.subtract(val.i));
        }

        /**
         * Assigns the value <code>this - val</code> to this ZqElement.
         * @param val value to subtract from this ZqElement.
         * @return <code>this = this - val</code>.
         * @throws IllegalArgumentException if <code>val</code> belongs to a
         * field with a different <code>q</code>.
         */
        public ZqElement subtractAssign(final ZqElement val) {
            if (!val.getField().equals(FieldZq.this)) {
                throw new IllegalArgumentException();
            }

            i = i.subtract(val.i).mod(getQ());
            return this;
        }

        /**
         * Returns a ZqElement whose value is <code>this * val</code>.
         * @param val value to multiply to this ZqElement.
         * @return <code>this * val</code>.
         * @throws IllegalArgumentException if <code>val</code> belongs to a
         * field with a different <code>q</code>.
         */
        public ZqElement multiply(final ZqElement val) {
            if (!val.getField().equals(FieldZq.this)) {
                throw new IllegalArgumentException();
            }

            // constructor will "mod q" result
            return new ZqElement(i.multiply(val.i));
        }

        /**
         * Assigns the value <code>this * val</code> to this ZqElement.
         * @param val value to multiply to this ZqElement.
         * @return <code>this = this * val</code>.
         * @throws IllegalArgumentException if <code>val</code> belongs to a
         * field with a different <code>q</code>.
         */
        public ZqElement multiplyAssign(final ZqElement val) {
            if (!val.getField().equals(FieldZq.this)) {
                throw new IllegalArgumentException();
            }

            i = i.multiply(val.i).mod(getQ());
            return this;
        }

        /**
         * Returns a ZqElement whose value is <code>this / val</code>.
         * @param val value to divide from this ZqElement.
         * @return <code>this / val</code>.
         * @throws IllegalArgumentException if <code>val</code> belongs to a
         * field with a different <code>q</code>.
         */
        public ZqElement divide(final ZqElement val) {
            if (!val.getField().equals(FieldZq.this)) {
                throw new IllegalArgumentException();
            }

            return multiply(val.inverse());
        }

        /**
         * Assigns the value <code>this / val</code> to this ZqElement.
         * @param val value to divide from this ZqElement.
         * @return <code>this = this / val</code>.
         * @throws IllegalArgumentException if <code>val</code> belongs to a
         * field with a different <code>q</code>.
         */
        public ZqElement divideAssign(final ZqElement val) {
            if (!val.getField().equals(FieldZq.this)) {
                throw new IllegalArgumentException();
            }

            multiplyAssign(val.inverse());
            return this;
        }

        /**
         * Returns the multiplicative inverse of this ZqElement.
         * @return <code>this^(-1)</code>.
         * @throws IllegalArgumentException if <code>this</code> is zero.
         */
        public ZqElement inverse() {
             if (i.equals(BigInteger.ZERO)) {
                 throw new ArithmeticException("Zero is not invertible");
             }
             return new ZqElement(i.modInverse(getQ()));
        }

        /**
         * Assigns the multiplicative inverse of this ZqElement to
         * <code>this</code>.
         * @return <code>this = this^(-1)</code>.
         * @throws IllegalArgumentException if <code>this</code> is zero.
         */
        public ZqElement inverseAssign() {
            if (i.equals(BigInteger.ZERO)) {
               throw new ArithmeticException("Zero is not invertible");
            }
            i = i.modInverse(getQ());
            return this;
        }

        /**
         * Returns the additive negation of this ZqElement.
         * @return <code>-this</code>.
         */
        public ZqElement negate() {
            return new ZqElement(i.negate()); // constructor will "mod q" it
        }

        /**
         * Assigns the additive negation of this ZqElement to
         * <code>this</code>.
         * @return <code>this = -this</code>.
         */
        public ZqElement negateAssign() {
            i = i.negate().mod(getQ());
            return this;
        }

        /**
         * Returns a BigInteger representing this ZqElement.
         * @return a BigInteger representing this ZqElement.
         */
        public BigInteger toBigInteger() {
            // NOTE: BigInteger is immutable, so no need for a defensive copy.
            return i;
        }

        /* (non-Javadoc)
         * @see com.microsoft.uprove.math.Element#length()
         */
        public int length() {
            return i.bitLength() / 8 + 1;
        }

        /**
         * Returns an encoded representation of this element.
         * @return an encoded representation of this element.
         * @see FieldZq#getElement(byte[])
         */
        public byte[] toByteArray() {
        	return ProtocolHelper.getMagnitude(i);
        }

        /**
         * Returns the hexadecimal representation of this ZqElement.
         * @return the hexadecimal representation of this ZqElement.
         */
        public String toString() {
            return i.toString(16);
        }

        /**
         * Returns a String representation of the ZqElement in a given radix.
         * If the radix is outside the range from {@link Character#MIN_RADIX}
         * to {@link Character#MAX_RADIX} inclusive, it will default to 10.
         * @param radix radix (i.e. base) of the String representation.
         * @return a String representation of the ZqElement in a given radix.
         * @see BigInteger#toString(int)
         */
        public String toString(final int radix) {
            return i.toString(radix);
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
            if (!(o instanceof ZqElement)) {
                return false;
            }
            ZqElement element = (ZqElement) o;
            return i.equals(element.i);
        }

        /**
         * Returns a hash code value for the object.
         * @return a hash code value for the object.
         */
        public int hashCode() {
            return i.hashCode();
        }

        /**
         * Adds the element to the hash updater.
         * @param dv the hash updater.
         */
        public void addToDigest(final HashUpdater dv) {
            dv.update(i);
        }
    }
}
