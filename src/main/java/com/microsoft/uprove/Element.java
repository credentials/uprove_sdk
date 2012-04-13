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
 * Represents a mathematical element.
 */
interface Element extends Hashable {

    /**
     * Returns the length, in bytes, of the byte array returned by
     * {@link #toByteArray() toByteArray}.
     * @return the element's encoded length.
     */
    int length();

    /**
     * Returns an encoded form of the <code>Element</code>. The format depends
     * on the implementing class, so no assumptions can be made about it.
     * Generally, implementors provide a way to extract an element from
     * the encoding returned from this method.
     * @return a byte array holding an opaque representation of the element.
     * Ownership of the referent is given to the caller.
     */
    byte[] toByteArray();

    /**
     * Returns a <code>String</code> representation of the
     * <code>Element</code>. The format depends on the implementation class,
     * so no assumption should be made about it.
     * @return a <code>String</code> representation of the
     * <code>Element</code>.
     */
    String toString();

}
