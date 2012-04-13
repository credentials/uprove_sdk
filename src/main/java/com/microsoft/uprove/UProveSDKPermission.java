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

import java.security.BasicPermission;

/**
 * The SDK's permissions class. An <code>UProveSDKPermission</code> contains a
 * name (also referred to as a "target name"), but no actions list; you either
 * have the named permission or you don't.
 * <p>
 * The target name is the name of an SDK security configuration parameter
 * (see below). The <code>UProveSDKPermission</code> object is used to guard
 * access to the SDK's global configuration settings.
 * </p>
 * <p>
 * The following table lists all of the possible
 * <code>UProveSDKPermission</code> target names, a description of what each
 * permission allows, and a discussion of the risks of granting code that
 * permission.
 * </p>
 * <table border="1" cellpadding="5" summary="target name, what the permission
 * allows, and associated risks">
 * <tr>
 * <th>Permission Target Name</th>
 * <th>What the Permission Allows</th>
 * <th>Risks of Allowing this Permission</th>
 * </tr>
 *
 * <tr>
 * <td>getOption.securerandom.algorithm</td>
 * <td>Access to the <code>SecureRandom</code> algorithm name.</td>
 * <td rowspan="2">These allow code to determine what the SDK's source of
 * randomness is. This could help malicious code exploit any known weaknesses
 * in the source of randomness.</td>
 * </tr>
 * <tr>
 * <td>getOption.securerandom.provider</td>
 * <td>Access to the name of the provider of the <code>SecureRandom</code>
 * implementation.</td>
 * </tr>
 *
 * <tr>
 * <td>setOption.securerandom.algorithm</td>
 * <td>Modification of the <code>SecureRandom</code> algorithm name.</td>
 * <td rowspan="2">These allow code to set the SDK's source of randomness.
 * Malicious code could set the source to a known bad implementation.</td>
 * </tr>
 * <tr>
 * <td>setOption.securerandom.provider</td>
 * <td>Modification of the name of the provider of the
 * <code>SecureRandom</code> implementation.</td>
 * </tr>
 *
 * <tr>
 * <td>getOption.messagedigest.provider</td>
 * <td>Access to the name of the provider of all <code>MessageDigest</code>
 * implementations.</td>
 * <td>This allows code to determine what implementation(s) of all digest
 * algorithms are being used. This could help malicious code exploit any known
 * weaknesses in those implementations.</td>
 * </tr>
 *
 * <tr>
 * <td>setOption.messagedigest.provider</td>
 * <td>Modification of the name of the provider of all
 * <code>MessageDigest</code> implementations.</td>
 * <td>This allows code to set the implementation of all digest algorithms.
 * Malicious code could use a provider with known bad implementations.</td>
 * </tr>
 * </table>
 */
public final class UProveSDKPermission extends BasicPermission {

    /**
     * UID generated with <code>serialver
     * com.microsoft.uprove.UProveSDKPermission</code>.
     */
    private static final long serialVersionUID = 5314374563868642919L;

    /**
     * Creates a new <code>UProveSDKPermission</code> instance with the
     * specified name. The name is the symbolic name of the
     * <code>UProveSDKPermission</code>. An asterisk may appear at the end
     * of the name, following a ".", or by itself, to signify a wildcard match.
     * @param name the name of the <code>UProveSDKPermission</code>.
     */
    public UProveSDKPermission(final String name) {
        super(name);
    }

}
