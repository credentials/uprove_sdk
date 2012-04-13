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

import java.security.Security;
import com.microsoft.uprove.ConfigImpl;
import junit.framework.TestCase;

/**
 * This test installs a {@link SecurityManager}, and therefore MUST be run
 * in its own VM.  Otherwise it will cause other tests to fail.
 */
public class ConfigImplTest extends TestCase {

    public final void testNoSecurityPermissions() {
        // what if we don't have perms to look into Security properties?
        // first, prepare by setting odd values for things
        Security.setProperty("com.microsoft.uprove.securerandom.algorithm", "one");
        Security.setProperty("com.microsoft.uprove.securerandom.provider", "two");
        Security.setProperty("com.microsoft.uprove.messagedigest.provider", "three");
        Security.setProperty("com.microsoft.uprove.math.primeconfidencelevel", "22");
        // next, reset all config options
        ConfigImpl.setSecureRandomAlgorithm(null);
        ConfigImpl.setSecureRandomProvider(null);
        ConfigImpl.setMessageDigestProvider(null);
        ConfigImpl.setPrimeConfidenceLevel(0);
        // now make sure we get those odd values when we ask
        assertEquals("one", ConfigImpl.secureRandomAlgorithm());
        assertEquals("two", ConfigImpl.secureRandomProvider());
        assertEquals("three", ConfigImpl.messageDigestProvider());
        assertEquals(22, ConfigImpl.primeConfidenceLevel());
        // next, reset all config options again
        ConfigImpl.setSecureRandomAlgorithm(null);
        ConfigImpl.setSecureRandomProvider(null);
        ConfigImpl.setMessageDigestProvider(null);
        ConfigImpl.setPrimeConfidenceLevel(0);
        // now install a security manager so we won't have access to
        // the odd values
        System.setSecurityManager(new SecurityManager());
        // and make sure we get back the defaults when we ask
        assertEquals("SHA1PRNG", ConfigImpl.secureRandomAlgorithm());
        assertNull(ConfigImpl.secureRandomProvider());
        assertNull(ConfigImpl.messageDigestProvider());
        assertEquals(100, ConfigImpl.primeConfidenceLevel());
    }
    
}
