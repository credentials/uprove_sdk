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
import java.util.HashMap;
import java.util.Map;

//TODO: support the pregenerated groups from the U-Prove Recommended Parameters Profile V1.1.

/**
 * Generates default subgroup instances. Three group order bit sizes are offered: 160, 256, and 512.
 */
public final class DefaultSubgroupFactory {

    private static Map<Integer, Subgroup> defaultSpecMap = new HashMap<Integer, Subgroup>();
    static {
        // these were generated with a q/(2^|q|) ratio
        // of at least 0.999. Therefore, random number
        // generation of Zq elements will be efficient.
        try {
            defaultSpecMap.put(
            	Integer.valueOf(160),
                new Subgroup(
                	// p
                	new BigInteger(1, Base64.decode("ANIa6NZubGs87Q6z3xomyRve7QE8F9hJ0w7DCYE+TTeZ8m2w1JToLsYeqf3HC7XLyvLl8YqDZJT1jmfG1hZIDDen8jBhAfyfD0do+cl5PCvhdrC3yXm0Bl0+g1aGo/C4QgxoNMsXkwOG3tqysH3Uc0SaSLqrMWKGtCEFJHXRNM07")),
                    // q (ratio: 0.99987)
                	new BigInteger(1, Base64.decode("AP/4CuGdrrxh9GNWrwk13A6BFI6x")),
                    // g
                	new BigInteger(1, Base64.decode("AKvOyXLpqd2NEzJwz+rCb3JuVn2WR1djDWvUNGDQkjpGrsCs4lXr893UscQmT1PmizYa+3d6E88AZ9rjZKNNVaCWWmzM94hSeCkjgTz4cIg02R9lV9eD7HW183zZGF8CewQsHHLhIbEmakCL4LtycNZZF7aQg2M+HzzWBiRhL8jB"))));

            defaultSpecMap.put(
            	Integer.valueOf(256),
                new Subgroup(
                    // p
                    new BigInteger(1, Base64.decode("AJgEaluYCdoRiTYD2LvMD4aR2S9YKmzg/qXVJ+kmFci/7wmMZBCBtnWMBf21ye4ZSSGcfxSAlfU+EdFZUK7Sw9x2SScARPXwk4IkDvIPkEh7IdaUCgWAymeZaJE6VTGevUvhX+3klPMRv8s1ztQ10nuS0t81EctSsH+vwNOvlvnughY9BI0KFJvl25qeJGQDVNvqgkDyEkLQ3yzp1GnDSF8=")),
                    // q (ratio: 0.99993)
                    new BigInteger(1, Base64.decode("AP/7vWYLlEEq5h6tnCkGo0QRbjFqJW/Th4dMbGdbHVh9")),
                    // g
                    new BigInteger(1, Base64.decode("ZY/qS3f2qmXBzrjKwhnkM47qM4Gmc4dIWB2UV8J1YdD8rOUjWuV5KVvm7QG3Orc9bY4apE3mdjNLVYUxal9Fp1R4yMJ2Js4hyIMQlUkiTVCwwe9DT8KQ3YYtrNH7b1vpFEpYNS3hRJ9tyaCXOj6UiH/bxMqEnHK7b6Hx4ZCFDmArz7eKE//rQ991/nR7ip1iBuzlXSBxqT0bnZlx3g0eNw=="))));

            defaultSpecMap.put(
            	Integer.valueOf(512),
            	new Subgroup(
                    // p
            		new BigInteger(1, Base64.decode("AOTD8AJWE6z1R1P8vOWL3mUtq88zBBYPnh7cag7imrMRG3p0rUjerMDi6d27yyQ0cc+vB3gpxl2Jpriyr44XVrTTWqaALwBgpNGYlsF4Z5e4MoizvDKq2ygWg/htYxCOPlENCgp2j5nq+N5d0i4eSrkALmKBZBdTcgz/Y7O8hTsLZefJd2QR7tWzun7ZvEt+wgq2xFgxHbplfEyUbUTeEahgluv9ojYCbK++WOaFQ5bm9abS58WaOJ8UfXwm6N69O9HeR2wdeoUHW1YY3D1B3Fg6CYGJCERA9tXol7TuVukGo6lNV5vBUkfRq6JXuUeESobC4QVMXyhTI012QGRCuus=")),
                    // q (ratio: 0.99948)
            		new BigInteger(1, Base64.decode("AP/ehdZiIzmFLN5xmIcQ2lbjV2ovZXwyLJRw9PvBKaRZ/xa0C+Q4zAoh2ggqvSD3TYpIuTbO4IPsg2v+aIaRSH8=")),
                    // g
            		new BigInteger(1, Base64.decode("VcW7g6F/Pn5ih4DzBIZh/nFyz2/7eqCbUQvZPrJVPQkLBkAzdYSL7MSxGGpmw1ITlYIBy9WngaQJC5JlxLGGyJhFFlZKwFGXGWlFOi3Fg3/SxuxPPJxy4MNpXJaeAJRTTbIgwRXxt3wBxJB48JQlPKoNd3gUbUORTmUoGPAdZzZdyn2vAI/au2R7bUCwDLq4LOQPd5Ow6ivQ9uF82q0+JNWYmYAeMsFzIiVHfusdNhMdyunPZVlCVZv1HT8LysEZlAewIloB/btBQhBcIeE2ytSMgI5icyZp2Ma4G3+z1KUIe+6lIHhx3sWfvNBwRbmo/nIFcArQbEyO7vqXEdlRyQ=="))));

        } catch (final IOException e) {
            AssertionError ae = new AssertionError("impossible exception");
            ae.initCause(e);
            throw ae;
        }
    }

    /**
     * Non-instantiable class.
     */
    private DefaultSubgroupFactory() {
        super();
    }

    /**
     * Returns a pregenerated subgroup of specific order if one is available.
     * @param orderSize the desired size of the group's order <code>q</code>.
     * @return a pregenerated subgroup, or <code>null</code> if none is available
     * for the given <code>q</code> size.
     */
    public static Subgroup getDefaultSubroup(final int orderSize) {
    	if (defaultSpecMap.containsKey(Integer.valueOf(orderSize))) {
    		return defaultSpecMap.get(Integer.valueOf(orderSize));
    	} else {
    		return null;
    	}
    }
    
}
