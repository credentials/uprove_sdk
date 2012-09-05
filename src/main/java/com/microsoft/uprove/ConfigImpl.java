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

import java.math.BigInteger;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivilegedAction;
import java.security.Security;

import com.microsoft.uprove.UProveSDKPermission;

/*
 * LOW-LEVEL IMPLEMENTATION CLASS. NOT PART OF PUBLIC API.
 */

/**
 * Implementation for SDK configuration options.
 * <p>For more detail regarding this class's strategy for discovering defaults
 * and allowing SDK users to get and set configuration options, see
 * {@link com.microsoft.uprove.Config}.</p>
 */
final class ConfigImpl {

    // option names for which we test when we allow outside code to get/set
    // config options
    private static final String OPTION_SECURERANDOM_ALGORITHM =
        "securerandom.algorithm";
    private static final String OPTION_SECURERANDOM_PROVIDER =
        "securerandom.provider";
    private static final String OPTION_MESSAGEDIGEST_PROVIDER =
        "messagedigest.provider";
    private static final String OPTION_MATH_PRIMECONFIDENCELEVEL =
        "math.primeconfidencelevel";

    // the base for all security properties we use
    private static final String SECURITY_PROPERTY_BASE =
        "com.microsoft.uprove.";

    // property names we use when calling java.security.Security.getProperty
    private static final String PROPERTY_SECURERANDOM_ALGORITHM =
        SECURITY_PROPERTY_BASE + OPTION_SECURERANDOM_ALGORITHM;
    private static final String PROPERTY_SECURERANDOM_PROVIDER =
        SECURITY_PROPERTY_BASE + OPTION_SECURERANDOM_PROVIDER;
    private static final String PROPERTY_MESSAGEDIGEST_PROVIDER =
        SECURITY_PROPERTY_BASE + OPTION_MESSAGEDIGEST_PROVIDER;
    private static final String PROPERTY_MATH_PRIMECONFIDENCELEVEL =
        SECURITY_PROPERTY_BASE + OPTION_MATH_PRIMECONFIDENCELEVEL;

    // prefixes for permission checks
    private static final String PREFIX_GET_OPTION = "getOption.";
    private static final String PREFIX_SET_OPTION = "setOption.";

    private static final String DEFAULT_SECURE_RANDOM_ALGORITHM = "SHA1PRNG";

    /**
     * We choose 100 as the default, because that's what Java 1.4 chooses for
     * the default confidence for
     * {@link BigInteger#probablePrime(int, java.util.Random)}.
     */
    private static final int DEFAULT_PRIME_CONFIDENCE_LEVEL = 100;

    // configuration settings
    // note: we create a new string so that our "unset" value is distinct from
    // any value that we'll possibly get from the User. if we simply set
    // OPTION_UNSET to "option unset", then we'd end up with an interned
    // String that would be reference-equal to an "option unset" String
    // given to us by a User.
    private static final String OPTION_UNSET = new String("option unset");
    private static final int LEVEL_UNSET = -1;
    private static String secureRandomAlgorithm = OPTION_UNSET;
    private static String secureRandomProvider = OPTION_UNSET;
    private static String messageDigestProvider = OPTION_UNSET;
    private static int primeConfidenceLevel = LEVEL_UNSET;

    /**
     * Private constructor to prevent instantiation or subclassing.
     */
    private ConfigImpl() {
        super();
    }

    /**
     * Gets the default for an option, as specified in
     * java.security.Security's property set.
     * @param propertyName the name of the property holding our option.
     * @return the default setting, or <code>null</code> if either none
     * is set or we don't have permission to read the requested property.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static String getDefault(final String propertyName) {
        try {
            // permission java.security.SecurityPermission
            // "getProperty.{propertyName}";
            return (String) AccessController.doPrivileged(
                new PrivilegedAction() {
                    public Object run() {
                        return Security.getProperty(propertyName);
                    }
            });
        } catch (SecurityException se) {
            // we don't have the required permission, so just...
            return null;
        }
    }

    /**
     * Returns the configured secure random algorithm, selecting the default
     * if none is configured.
     * @return the configured secure random algorithm.
     */
    static synchronized String secureRandomAlgorithm() {
        if (secureRandomAlgorithm == OPTION_UNSET) {
            secureRandomAlgorithm =
                getDefault(PROPERTY_SECURERANDOM_ALGORITHM);
            // if there's none set or it's empty, use our default
            if (secureRandomAlgorithm == null
                || secureRandomAlgorithm.length() == 0) {
                secureRandomAlgorithm = DEFAULT_SECURE_RANDOM_ALGORITHM;
            }
        }
        return secureRandomAlgorithm;
    }

    /**
     * Returns the name of the
     * {@link java.security.SecureRandom SecureRandom} algorithm used by the
     * SDK.
     * @return a <code>SecureRandom</code> algorithm name.
     * @throws SecurityException if a security manager exists and its
     * {@link SecurityManager#checkPermission(java.security.Permission)}
     * method denies access to retrieve this configuration option's value.
     */
    public static String getSecureRandomAlgorithm() throws SecurityException {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new UProveSDKPermission(PREFIX_GET_OPTION
                + OPTION_SECURERANDOM_ALGORITHM));
        }

        return secureRandomAlgorithm();
    }

    /**
     * Sets the name of the
     * {@link java.security.SecureRandom SecureRandom} algorithm used by the
     * SDK.
     * @param algorithm a <code>SecureRandom</code> algorithm name, or
     * <code>null</code> to select the default according to the site-wide
     * default.
     * @throws SecurityException if a security manager exists and its
     * {@link SecurityManager#checkPermission(java.security.Permission)}
     * method denies access to set this configuration option's value.
     */
    public static void setSecureRandomAlgorithm(final String algorithm) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new UProveSDKPermission(PREFIX_SET_OPTION
                + OPTION_SECURERANDOM_ALGORITHM));
        }

        synchronized (ConfigImpl.class) {
            secureRandomAlgorithm =
                algorithm != null && algorithm.length() != 0
                    ? algorithm
                    : OPTION_UNSET;
            RandomSourceImpl.reset();
        }
    }

    /**
     * Returns the configured secure random provider.
     * @return the configured secure random provider.
     */
    static synchronized String secureRandomProvider() {
        if (secureRandomProvider == OPTION_UNSET) {
            secureRandomProvider =
                getDefault(PROPERTY_SECURERANDOM_PROVIDER);
            // if we got the empty string, go with null, meaning that we'll
            // use the first provider based on the site's java.security config
            if (secureRandomProvider != null
                && secureRandomProvider.length() == 0) {
                secureRandomProvider = null;
            }
        }
        return secureRandomProvider;
    }

    /**
     * Returns the name of the
     * {@link java.security.SecureRandom SecureRandom} provider used by the
     * SDK.
     * @return a <code>SecureRandom</code> provider name.
     * @throws SecurityException if a security manager exists and its
     * {@link SecurityManager#checkPermission(java.security.Permission)}
     * method denies access to retrieve this configuration option's value.
     */
    public static String getSecureRandomProvider()
            throws SecurityException {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new UProveSDKPermission(PREFIX_GET_OPTION
                + OPTION_SECURERANDOM_PROVIDER));
        }

        return secureRandomProvider();
    }

    /**
     * Sets the name of the
     * {@link java.security.SecureRandom SecureRandom} provider to be used
     * by the SDK.
     * @param provider the name of a provider of a <code>SecureRandom</code>
     * algorithm, or <code>null</code> to use the default according to the
     * site-wide configuration.
     * @throws SecurityException if a security manager exists and its
     * {@link SecurityManager#checkPermission(java.security.Permission)}
     * method denies access to set this configuration option's value.
     */
    public static void setSecureRandomProvider(final String provider) {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new UProveSDKPermission(PREFIX_SET_OPTION
                + OPTION_SECURERANDOM_PROVIDER));
        }

        synchronized (ConfigImpl.class) {
            secureRandomProvider =
                provider != null && provider.length() != 0
                    ? provider
                    : OPTION_UNSET;
            RandomSourceImpl.reset();
        }
    }

    /**
     * Returns the configured message digest provider.
     * @return the configured message digest provider.
     */
    static synchronized String messageDigestProvider() {
        if (messageDigestProvider == OPTION_UNSET) {
            messageDigestProvider =
                getDefault(PROPERTY_MESSAGEDIGEST_PROVIDER);
            // if we got the empty string, go with null, meaning that we'll
            // use the first provider based on the site's java.security config
            if (messageDigestProvider != null
                && messageDigestProvider.length() == 0) {
                messageDigestProvider = null;
            }
        }
        return messageDigestProvider;
    }

    /**
     * Returns the name of the
     * {@link java.security.MessageDigest MessageDigest} provider used by the
     * SDK.
     * @return a <code>MessageDigest</code> provider name.
     * @throws SecurityException if a security manager exists and its
     * {@link SecurityManager#checkPermission(java.security.Permission)}
     * method denies access to retrieve this configuration option's value.
     */
    public static String getMessageDigestProvider()
            throws SecurityException {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new UProveSDKPermission(PREFIX_GET_OPTION
                + OPTION_MESSAGEDIGEST_PROVIDER));
        }

        return messageDigestProvider();
    }

    /**
     * Sets the name of the
     * {@link java.security.MessageDigest MessageDigest} provider used by the
     * SDK.
     * @param provider a <code>MessageDigest</code> provider name, or
     * <code>null</code> to indicate that the SDK should use the
     * <code>java.security</code> default.
     * @throws SecurityException if a security manager exists and its
     * {@link SecurityManager#checkPermission(java.security.Permission)}
     * method denies access to set this configuration option's value.
     */
    public static void setMessageDigestProvider(final String provider)
            throws SecurityException {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new UProveSDKPermission(PREFIX_SET_OPTION
                + OPTION_MESSAGEDIGEST_PROVIDER));
        }

        synchronized (ConfigImpl.class) {
            messageDigestProvider =
                provider != null && provider.length() != 0
                        ? provider
                        : OPTION_UNSET;
        }
    }

    /**
     * Creates a new <code>MessageDigest</code> instance using the configured
     * provider.
     * @param algorithm the name of the message digest algorithm.
     * @return a digest instance.
     * @throws NoSuchProviderException if a provider is configured but not
     * available.
     * @throws NoSuchAlgorithmException if the desired algorithm is not
     * available.
     * @see #messageDigestProvider()
     */
    static MessageDigest getMessageDigest(final String algorithm)
            throws NoSuchProviderException, NoSuchAlgorithmException {
        final String provider = messageDigestProvider();
        return provider != null
                ? MessageDigest.getInstance(algorithm, provider)
                : MessageDigest.getInstance(algorithm);
    }

    /*
     * Prime number generation levels.
     */
    /**
     * Test a prime confidence level for validity.
     * <p>Numbers less than <code>1</code> are invalid due to the fact that
     * you'll never find a prime with such a level. Empirical testing (and
     * inspection of Sun's implementation) show that for integers over 1024
     * bits in length, levels above 3 are equivalent to 3.</p>
     * @param level a confidence level for testing.
     * @return <code>true</code> if <code>level</code> is valid.
     */
    private static boolean isValidPrimeConfidenceLevel(final int level) {
        return level >= 1;
    }

    /**
     * Returns the configured prime number generation confidence level.
     * @return the configured prime number generation confidence level.
     */
    static synchronized int primeConfidenceLevel() {
        if (primeConfidenceLevel == LEVEL_UNSET) {
            final String defaultLevel =
                getDefault(PROPERTY_MATH_PRIMECONFIDENCELEVEL);
            // pessimistically choose the default
            primeConfidenceLevel = DEFAULT_PRIME_CONFIDENCE_LEVEL;
            // now try to parse the default
            if (defaultLevel != null && defaultLevel.length() != 0) {
                try {
                    final int level = Integer.parseInt(defaultLevel);
                    if (isValidPrimeConfidenceLevel(level)) {
                        // we got a good one!
                        primeConfidenceLevel = level;
                    }
                } catch (NumberFormatException nfe) {
                    // stick with the default
                }
            }
        }
        return primeConfidenceLevel;
    }

    /**
     * Returns the confidence level for prime number generation.
     * @return the confidence level for prime number generation.
     * @throws SecurityException if a security manager exists and its
     * {@link SecurityManager#checkPermission(java.security.Permission)}
     * method denies access to retrieve this configuration option's value.
     */
    public static int getPrimeConfidenceLevel() throws SecurityException {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new UProveSDKPermission(PREFIX_GET_OPTION
                + OPTION_MATH_PRIMECONFIDENCELEVEL));
        }

        return primeConfidenceLevel();
    }

    /**
     * Sets the confidence level for prime number generation.
     * @param aLevel the confidence level for prime number generation, or
     * <code>0</code> to select the SDK's default value.
     * @throws SecurityException if a security manager exists and its
     * {@link SecurityManager#checkPermission(java.security.Permission)}
     * method denies access to set this configuration option's value.
     */
    public static void setPrimeConfidenceLevel(final int aLevel)
            throws SecurityException {
        final SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new UProveSDKPermission(PREFIX_SET_OPTION
                + OPTION_MATH_PRIMECONFIDENCELEVEL));
        }

        final int level;
        if (aLevel == 0) {
            level = LEVEL_UNSET;
        } else if (!isValidPrimeConfidenceLevel(aLevel)) {
            throw new IllegalArgumentException("Invalid level: " + aLevel);
        } else {
            level = aLevel;
        }

        synchronized (ConfigImpl.class) {
            primeConfidenceLevel = level;
        }
    }
}
