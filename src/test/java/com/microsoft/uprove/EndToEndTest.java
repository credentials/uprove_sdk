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
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import junit.framework.TestCase;

public class EndToEndTest extends TestCase {

    public EndToEndTest(String name) {
    	super(name);
	}

    public void testLongRun() throws NoSuchProviderException, NoSuchAlgorithmException, IllegalStateException, IOException, InvalidProofException {
    	/*
		 * issuer parameters setup
		 */
    	int numberOfAttribs = 100;
    	IssuerSetupParameters isp = new IssuerSetupParameters();
		byte[] encodingBytes = new byte[numberOfAttribs];
		for (int i = 0; i < numberOfAttribs; i++)
        {
            encodingBytes[i] = (byte) (i%2); // alternate between 0 (direct encoding) and 1 (hash)
        }
    	isp.setEncodingBytes(encodingBytes);
		isp.setHashAlgorithmUID("SHA1");
		isp.setParametersUID("unique UID".getBytes());
		isp.setSpecification("specification".getBytes());
		IssuerKeyAndParameters ikap = isp.generate();
		IssuerParameters ip = ikap.getIssuerParameters();
		ip.validate();

    	/*
		 * token issuance
		 */

		// protocol parameters
        byte[][] attributes = new byte[numberOfAttribs][];
        attributes[0] = BigInteger.ZERO.toByteArray();
        attributes[1] = null;
        attributes[2] = BigInteger.ONE.toByteArray();
        attributes[3] = "This is a very long value that doesn't fit in one attribute, but this is ok since we hash this value".getBytes();
        FieldZq Zq = ip.getGroup().getZq(); 
        for (int index = 4; index < numberOfAttribs; index++)
        {
            // for the rest, we just encode random Zq values
            attributes[index] = Zq.getRandomElement(false).toByteArray();
        }
        byte[] tokenInformation = new byte[] { };
        byte[] proverInformation = new byte[] { };
        int numberOfTokens = 250;


		// issuer generates first issuance message
		IssuerProtocolParameters issuerProtocolParams = new IssuerProtocolParameters();
		issuerProtocolParams.setIssuerKeyAndParameters(ikap);
		issuerProtocolParams.setNumberOfTokens(numberOfTokens);
		issuerProtocolParams.setTokenAttributes(attributes);
		issuerProtocolParams.setTokenInformation(tokenInformation);
		Issuer issuer = issuerProtocolParams.generate();
		byte[][] message1 = issuer.generateFirstMessage();

		// prover generates second issuance message
		ProverProtocolParameters proverProtocolParams = new ProverProtocolParameters();
		proverProtocolParams.setIssuerParameters(ip);
		proverProtocolParams.setNumberOfTokens(numberOfTokens);
		proverProtocolParams.setTokenAttributes(attributes);
		proverProtocolParams.setTokenInformation(tokenInformation);
		proverProtocolParams.setProverInformation(proverInformation);
		Prover prover = proverProtocolParams.generate();
		byte[][] message2 = prover.generateSecondMessage(message1);

		// issuer generates third issuance message
		byte[][] message3 = issuer.generateThirdMessage(message2);

		// prover generates the U-Prove tokens
		UProveKeyAndToken[] upkt = prover.generateTokens(message3);

		/*
		 * token presentation
		 */

        // Presentation
        for (int i = 1; i <= numberOfAttribs; i++)
        {
            // disclose each attribute one by one
            int[] disclosed = new int[] { i };
            byte[] message = "this is the presentation message, this can be a very long message".getBytes();

    		// prover chooses a token to use
    		UProveKeyAndToken keyAndToken = upkt[0];

    		// prover generates the presentation proof
    		PresentationProof proof = PresentationProtocol.generatePresentationProof(ip, disclosed, message, null, keyAndToken, attributes);

    		// prover transmits the U-Prove token and presentation proof to the verifier 
    		UProveToken token = keyAndToken.getToken();

    		// verifier verifies the presentation proof
    		PresentationProtocol.verifyPresentationProof(ip, disclosed, message, null, token, proof);    
        }
        
    }

    public void testEndToEnd() throws IOException, InvalidProofException, NoSuchProviderException, NoSuchAlgorithmException {
    	String[] mdAlgos = new String[]{ "SHA-1", "SHA-256", "SHA-512" };
    	int attributeLength = 10;
    	
    	for ( String messageDigestAlg : mdAlgos )
    	{
            for (int numberOfAttribs = 0; numberOfAttribs <= 3; numberOfAttribs++)
            {
            	byte[] E = new byte[numberOfAttribs];
                byte[][] attributes = new byte[numberOfAttribs][];
                int[] indexesToDisclose = new int[numberOfAttribs];
                for (byte e = 0; e <= 1; e++)
                {
                	for (int supportDevice = 0; supportDevice <= 1; supportDevice++) 
	                {
	                    // Issuer setup
	                    IssuerSetupParameters isp = new IssuerSetupParameters();
	                    isp.setParametersUID("unique UID".getBytes());
	                    isp.setHashAlgorithmUID(messageDigestAlg);
	                    Arrays.fill(E, e);
	                    isp.setEncodingBytes(E);
	                    isp.setSpecification("specification".getBytes());
	                    isp.setSupportDevice(supportDevice == 1);
	                    IssuerKeyAndParameters ikap = isp.generate();
	                    IssuerParameters ip = ikap.getIssuerParameters();
	                    ip.validate();
	
	                    // Device setup
	                    Device device = null;
	                    byte[] deviceZetaParameter = null;
	            		byte[] devicePublicKey = null;
	                    if (supportDevice == 1)
	                    {
	                    	DeviceSetupParameters deviceSetupParams = new DeviceSetupParameters();
	                    	deviceSetupParams.setIssuerParameters(ip);
	                    	device = deviceSetupParams.generate();
	                    	int dIndex = ip.getProverIssuanceValues().length-1;
	                    	deviceZetaParameter = device.GetDeviceParameter(ip.getProverIssuanceValues()[dIndex]);
	                    	devicePublicKey = device.GetDevicePublicKey();
	                    }
	                    
	                    // Issuance
	                    for (int index = 0; index < numberOfAttribs; index++)
	                    {
	                    	attributes[index] = RandomSource.getRandomBytes( attributeLength );
	                    }
	                    byte[] tokenInformation = "token information".getBytes();
	                    byte[] proverInformation = "prover information".getBytes();
	                    int numberOfTokens = (int)Math.pow(2, numberOfAttribs);
	                    
	                    Issuer issuer = new IssuerProtocolParameters( numberOfTokens, ikap, attributes, tokenInformation, devicePublicKey ).generate();
	
	                    byte[][] msg1 = issuer.generateFirstMessage();
	
	                    ProverProtocolParameters ppp = new ProverProtocolParameters(numberOfTokens, ip, attributes, tokenInformation, proverInformation);
	                    ppp.setDeviceParameters(devicePublicKey, deviceZetaParameter);
	                    Prover prover = ppp.generate();
	                    
	                    byte[][] msg2 = prover.generateSecondMessage(msg1);
	
	                    byte[][] msg3 = issuer.generateThirdMessage(msg2);
	
	                    // issue token
	                    UProveKeyAndToken[] upkt = prover.generateTokens(msg3);
	
	                    // Presentation
	                    for (int i = 0; i < numberOfTokens; i++)
	                    {
	                    	int numToDisclose = 0;
	                        for (int index = 0; index < numberOfAttribs; index++)
	                        {
	                            if ((((int)Math.pow(2, index)) & i) != 0)
	                            {
	                                indexesToDisclose[numToDisclose++] = index + 1;
	                            }
	                        }
	
	                        int[] disclosed = Arrays.copyOf(indexesToDisclose, numToDisclose);
	                        byte[] message = "message".getBytes();
	                		byte[] deviceMessage = "message for Device".getBytes();
	
	                		if (supportDevice == 1)
	                		{
	                    		DeviceManager.RegisterDevice(device);
	                		}
	                		
	                		// generate the presentation proof
	                        PresentationProof proof =
	                        	PresentationProtocol.generatePresentationProof(ip, disclosed, message, deviceMessage, upkt[i], attributes);
	
	                        // verify the presentation proof
	                        PresentationProtocol.verifyPresentationProof(ip, disclosed, message, deviceMessage, upkt[i].getToken(), proof);
	
	                        //
	                        // negative cases
	                        //
	                        if (numberOfAttribs > 0)
	                        {
	                            // modify issuer params (swap g0 and z0);
	                            IssuerParameters ip2 = new IssuerParameters();
	                            ip2.setParametersUID(ip.getParametersUID());
	                            ip2.setEncodingBytes(ip.getEncodingBytes());
	                            ip2.setGroup(ip.getGroup());
	                            ip2.setHashAlgorithmUID(ip.getHashAlgorithmUID());
	                            ip2.setProverIssuanceValues(ip.getProverIssuanceValues());
	                            ip2.setPublicKey(ip.getPublicKey());
	                            ip2.setSpecification("wrong issuer params".getBytes());
	                            try { PresentationProtocol.verifyPresentationProof(ip2, disclosed, message, null, upkt[i].getToken(), proof); fail(); }
	                            catch (InvalidProofException ipe) { }
	
	                            // modify disclosed list
	                            int[] disclosed2;
	                            if (disclosed.length == 0)
	                            {
	                                disclosed2 = new int[] { 1 };
	                            }
	                            else
	                            {
	                                disclosed2 = new int[] { };
	                            }
	                            try { PresentationProtocol.verifyPresentationProof(ip, disclosed2, message, null, upkt[i].getToken(), proof); fail(); }
	                            catch (InvalidProofException ipe) { }
	
	                            // modify message
	                            try { PresentationProtocol.verifyPresentationProof(ip, disclosed, "wrong message".getBytes(), null, upkt[i].getToken(), proof); fail(); }
	                            catch (InvalidProofException ipe) { }
	
	                            // modify token
	                            try { PresentationProtocol.verifyPresentationProof(ip, disclosed, message, null, upkt[(i+1)%numberOfTokens].getToken(), proof); fail(); }
	                            catch (InvalidProofException ipe) { }
	                            
	                            // modify proof
	                            proof.setA("wrong proof".getBytes());
	                            try { PresentationProtocol.verifyPresentationProof(ip, disclosed, message, null, upkt[i].getToken(), proof); fail(); }
	                            catch (InvalidProofException ipe) { }
	                        }
                        }
                    }
                }
            }
    	}
    }

}
