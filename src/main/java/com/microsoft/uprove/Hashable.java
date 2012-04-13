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
 * A mix-in interface that indicates that an object can add its contents to a
 * formatted digest.
 */
interface Hashable {

    /**
     * Adds an object's contents to a formatted digest via the provided
     * visitor. The means by which the visitor formats any data given to
     * it via implementations of this method is up to the specific
     * <code>HashUpdater</code> implementation, and is of no concern to
     * implementors of this method.
     * <p><b>Note</b>: Implementation of this method implies a specific
     * contract. It is critical that two distinct instances of a particular
     * implementing class that contain the same data add their data to the
     * provided visitor in the same well-defined fashion. Additionally, any
     * changes to the way data is added to the visitor, such as changing the
     * order in which data members are added or changing their data types, is
     * extremely likely to break the established contract. As such, great care
     * must be taken when implementing this method, as such changes are likely
     * to have adverse side effects.</p>
     * @param dv the visitor with which the object must interact to add its
     * contents to a formatted hash digest.
     */
    void addToDigest(HashUpdater dv);

}
