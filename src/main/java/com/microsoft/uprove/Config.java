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

import com.microsoft.uprove.ConfigImpl;

/**
 * Manages the configuration for the SDK. Default configuration values are
 * read from the {@link java.security.Security} class's property set.
 * Alternate settings may be supplied via the various <code>set</code> methods
 * on this class.
 * <h3>Default Settings</h3>
 * <p>If no {@link java.lang.SecurityManager} is installed, or if the SDK
 * is granted access to the properties named below, then the SDK will retrieve
 * the default settings for all options from the <code>Security</code> class
 * via its {@link java.security.Security#getProperty(java.lang.String)
 * getProperty}
 * method. The security properties read by the SDK are:
 * <ul>
 * <li><code>com.microsoft.uprove.securerandom.algorithm</code></li>
 * <li><code>com.microsoft.uprove.securerandom.provider</code></li>
 * <li><code>com.microsoft.uprove.messagedigest.provider</code></li>
 * </ul>
 * <p>If the SDK cannot access the named properties (because it has not been
 * granted the <code>"getProperty.&lt;property name>"</code>
 * {@link java.security.SecurityPermission}) then it will use the built-in
 * SDK default values as indicated below.</p>
 * <p>Site administrators may set the values for these Java security
 * properties by editing the site-wide Java security properties file, located
 * at <code>"${java.home}/lib/security/java.policy"</code> where
 * <code>"${java.home}"</code> is the value of the system property named
 * <code>java.home</code>, and <code>'/'</code> is the system path separator.
 * </p>
 * <p>Alternatively, applications may set the default values via the
 * <code>Security</code> class's
 * {@link
 * java.security.Security#setProperty(java.lang.String, java.lang.String)
 * setProperty}
 * method before invoking any SDK methods.</p>
 * <h3>Runtime Settings</h3>
 * <p>Applications may override the default values by invoking
 * any of the <code>set</code> methods on this class.  All <code>set</code>
 * methods require that both the SDK and the application using it have been
 * granted the {@link com.microsoft.uprove.UProveSDKPermission} named
 * <code>"setOption.&lt;option name>"</code> where
 * <code>"&lt;option name>"</code> is the name of the option as indicated
 * below. Likewise, applications may only retrieve the current value of a
 * setting via a <code>get</code> method if both the application and the SDK
 * have been granted the <code>UProveSDKPermission</code> named
 * <code>"getOption.&lt;option name>"</code>.
 * <h3>Configuration Options</h3>
 * <p><b>Random number generator</b><br>
 * SDK users can set the {@link java.security.SecureRandom SecureRandom}
 * implementation that will be used by the SDK by specifying the algorithm and
 * optionally the provider implementing it.
 * The algorithm must be supported by the provider, which must be registered
 * with the JCA, otherwise an exception
 * will be thrown when the SDK tries to instantiate the
 * <code>SecureRandom</code> object.
 * <p>Modifications to these options at runtime via the <code>set</code>
 * methods take effect immediately.</p>
 * <ul>
 * <li><b>Option Name</b>: <code>securerandom.algorithm</code></li>
 * <li><b>Default Value</b>: <code>SHA1PRNG</code></li>
 * <li><b>Security Property Name</b>:
 * <code>com.microsoft.uprove.securerandom.algorithm</code></li>
 * <li><b>Setter</b>: {@link #setSecureRandomAlgorithm(String)}</li>
 * <li><b>Getter</b>: {@link #getSecureRandomAlgorithm()}</li>
 * </ul>
 * <ul>
 * <li><b>Option Name</b>: <code>securerandom.provider</code></li>
 * <li><b>Default Value</b>: <code>null</code></li>
 * <li><b>Security Property Name</b>:
 * <code>com.microsoft.uprove.securerandom.provider</code></li>
 * <li><b>Setter</b>: {@link #setSecureRandomProvider(String)}</li>
 * <li><b>Getter</b>: {@link #getSecureRandomProvider()}</li>
 * </ul>
 *
 * <p><b><code>MessageDigest</code> provider</b><br>
 * SDK user can set the {@link java.security.MessageDigest MessageDigest}
 * provider implementing the digest algorithm specified in different
 * components. The provider must be registered with the JCA, otherwise an
 * exception will be thrown when the SDK tries to instantiate the
 * <code>MessageDigest</code> object.</p>
 * <p>Modification of this option at runtime via the <code>set</code> method
 * only affects subsequently created <code>MessageDigest</code> instances.</p>
 * <ul>
 * <li><b>Option Name</b>: <code>messagedigest.provider</code></li>
 * <li><b>Default Value</b>: <code>null</code></li>
 * <li><b>Security Property Name</b>:
 * <code>com.microsoft.uprove.messagedigest.provider</code></li>
 * <li><b>Setter</b>: {@link #setMessageDigestProvider(String)}</li>
 * <li><b>Getter</b>: {@link #getMessageDigestProvider()}</li>
 * </ul>
 */
public final class Config {

    /*
     * Non-instantiable class
     */
    private Config() {
        super();
    }

    /**
     * Returns the name of the
     * {@link java.security.SecureRandom SecureRandom} algorithm used by the
     * SDK.
     * <p>First, if there is a security manager, its
     * <code>checkPermission</code> method is called with a
     * <code>com.microsoft.uprove.UProveSDKPermission("getOption.securerandom.algorithm")</code>
     * permission.</p>
     * @return a <code>SecureRandom</code> algorithm name.
     * @throws SecurityException if a security manager exists and its
     * {@link SecurityManager#checkPermission(java.security.Permission)}
     * method denies access based on the current security policy.
     * @see #setSecureRandomAlgorithm(String)
     * @see UProveSDKPermission
     */
    public static String getSecureRandomAlgorithm() throws SecurityException {
        return ConfigImpl.getSecureRandomAlgorithm();
    }

    /**
     * Sets the name of the
     * {@link java.security.SecureRandom SecureRandom} algorithm used by the
     * SDK.
     * <p>First, if there is a security manager, its
     * <code>checkPermission</code> method is called with a
     * <code>com.microsoft.uprove.UProveSDKPermission("setOption.securerandom.algorithm")</code>
     * permission.</p>
     * @param algorithm a <code>SecureRandom</code> algorithm name, or
     * <code>null</code> to select the default according to the site-wide
     * default.
     * @throws SecurityException if a security manager exists and its
     * {@link SecurityManager#checkPermission(java.security.Permission)}
     * method denies access based on the current security policy.
     * @see #getSecureRandomAlgorithm()
     * @see UProveSDKPermission
     */
    public static void setSecureRandomAlgorithm(final String algorithm)
            throws SecurityException {
        ConfigImpl.setSecureRandomAlgorithm(algorithm);
    }

    /**
     * Returns the name of the
     * {@link java.security.SecureRandom SecureRandom} provider used by the
     * SDK.
     * <p>First, if there is a security manager, its
     * <code>checkPermission</code> method is called with a
     * <code>com.microsoft.uprove.UProveSDKPermission("getOption.securerandom.provider")</code>
     * permission.</p>
     * @return a <code>SecureRandom</code> provider name.
     * @throws SecurityException if a security manager exists and its
     * {@link SecurityManager#checkPermission(java.security.Permission)}
     * method denies access based on the current security policy.
     * @see #setSecureRandomProvider(String)
     * @see UProveSDKPermission
     */
    public static String getSecureRandomProvider() throws SecurityException {
        return ConfigImpl.getSecureRandomProvider();
    }

    /**
     * Sets the name of the
     * {@link java.security.SecureRandom SecureRandom} provider used
     * by the SDK.
     * <p>First, if there is a security manager, its
     * <code>checkPermission</code> method is called with a
     * <code>com.microsoft.uprove.UProveSDKPermission("setOption.securerandom.provider")</code>
     * permission.</p>
     * @param provider the name of a provider of a <code>SecureRandom</code>
     * algorithm, or <code>null</code> to use the default according to the
     * site-wide configuration.
     * @throws SecurityException if a security manager exists and its
     * {@link SecurityManager#checkPermission(java.security.Permission)}
     * method denies access based on the current security policy.
     * @see #getSecureRandomProvider()
     * @see UProveSDKPermission
     */
    public static void setSecureRandomProvider(final String provider)
            throws SecurityException {
        ConfigImpl.setSecureRandomProvider(provider);
    }

    /**
     * Returns the name of the
     * {@link java.security.MessageDigest MessageDigest} provider used by the
     * SDK.
     * <p>First, if there is a security manager, its
     * <code>checkPermission</code> method is called with a
     * <code>com.microsoft.uprove.UProveSDKPermission("getOption.messagedigest.provider")</code>
     * permission.</p>
     * @return a <code>MessageDigest</code> provider name.
     * @throws SecurityException if a security manager exists and its
     * {@link SecurityManager#checkPermission(java.security.Permission)}
     * method denies access based on the current security policy.
     * @see #setMessageDigestProvider(String)
     * @see UProveSDKPermission
     */
    public static String getMessageDigestProvider() throws SecurityException {
        return ConfigImpl.getMessageDigestProvider();
    }

    /**
     * Sets the name of the
     * {@link java.security.MessageDigest MessageDigest} provider used by the
     * SDK.
     * <p>First, if there is a security manager, its
     * <code>checkPermission</code> method is called with a
     * <code>com.microsoft.uprove.UProveSDKPermission("setOption.messagedigest.provider")</code>
     * permission.</p>
     * <p>Note: this method does not check if the provider is installed
     * on the system, in which case an error will be thrown whenever
     * the provider is used by the SDK.</p>
     * @param provider a <code>MessageDigest</code> provider name, or
     * <code>null</code> to indicate that the SDK should use the
     * <code>java.security</code> default.
     * @throws SecurityException if a security manager exists and its
     * {@link SecurityManager#checkPermission(java.security.Permission)}
     * method denies access based on the current security policy.
     * @see #getMessageDigestProvider()
     * @see UProveSDKPermission
     */
    public static void setMessageDigestProvider(final String provider)
            throws SecurityException {
        ConfigImpl.setMessageDigestProvider(provider);
    }
}
