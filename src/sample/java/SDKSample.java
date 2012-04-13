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

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import com.microsoft.uprove.Device;
import com.microsoft.uprove.DeviceManager;
import com.microsoft.uprove.DeviceSetupParameters;
import com.microsoft.uprove.InvalidProofException;
import com.microsoft.uprove.Issuer;
import com.microsoft.uprove.IssuerKeyAndParameters;
import com.microsoft.uprove.IssuerParameters;
import com.microsoft.uprove.IssuerProtocolParameters;
import com.microsoft.uprove.IssuerSetupParameters;
import com.microsoft.uprove.PresentationProof;
import com.microsoft.uprove.PresentationProtocol;
import com.microsoft.uprove.Prover;
import com.microsoft.uprove.ProverProtocolParameters;
import com.microsoft.uprove.UProveKeyAndToken;
import com.microsoft.uprove.UProveToken;

/**
 * This samples demonstrates how to issue and present U-Prove tokens.
 */
public class SDKSample {

	/**
     * Runs the sample.
     */
    public static void Sample() throws IllegalStateException, IOException, InvalidProofException, NoSuchProviderException, NoSuchAlgorithmException {
    	System.out.println("U-Prove Java SDK sample");

    	try {
    		/*
    		 * issuer parameters setup
    		 */

    		IssuerSetupParameters isp = new IssuerSetupParameters();
    		isp.setEncodingBytes(new byte[] {1,0});
    		isp.setHashAlgorithmUID("SHA-256");
    		isp.setParametersUID("unique UID".getBytes());
    		isp.setSpecification("specification".getBytes());
    		IssuerKeyAndParameters ikap = isp.generate();
    		IssuerParameters ip = ikap.getIssuerParameters();

    		// issuer distributes the issuer parameters


    		// prover and verifier should validate the issuer parameters upon reception
    		ip.validate();

    		/*
    		 *  token issuance
    		 */

    		System.out.println("Issuing U-Prove tokens");
    		
    		// protocol parameters
    		byte[][] attributes = new byte[][] {
    				"first attribute".getBytes(),
    				"second attribute".getBytes()
    		};
    		byte[] tokenInformation = "token information".getBytes();
    		byte[] proverInformation = "prover information".getBytes();
    		int numberOfTokens = 5;

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

    		// application specific storage of keys, tokens, and attributes

    		/*
    		 * token presentation
    		 */

    		System.out.println("Presenting a U-Prove token");
    		
    		// protocol parameters (shared by prover and verifier)
    		int[] disclosed = new int[] {2};
    		byte[] message = "message".getBytes();

    		// prover chooses a token to use
    		UProveKeyAndToken keyAndToken = upkt[0];

    		// prover generates the presentation proof
    		PresentationProof proof = PresentationProtocol.generatePresentationProof(ip, disclosed, message, null, keyAndToken, attributes);

    		// prover transmits the U-Prove token and presentation proof to the verifier 
    		UProveToken token = keyAndToken.getToken();

    		// verifier verifies the presentation proof
    		PresentationProtocol.verifyPresentationProof(ip, disclosed, message, null, token, proof);
    	}
    	catch (Exception e) {
    		System.out.println(e.toString());
    		e.printStackTrace(System.out);
    		return;
    	}
    	
    	System.out.println("Sample completed successfully");
    }

	/**
     * Runs the Device sample.
     */
    public static void DeviceSample() throws IllegalStateException, IOException, InvalidProofException, NoSuchProviderException, NoSuchAlgorithmException {
    	System.out.println("U-Prove Java SDK Device sample");

    	try {
    		/*
    		 * issuer parameters setup
    		 */

    		IssuerSetupParameters isp = new IssuerSetupParameters();
    		isp.setEncodingBytes(new byte[] {1,0});
    		isp.setHashAlgorithmUID("SHA-256");
    		isp.setParametersUID("unique UID".getBytes());
    		isp.setSpecification("specification".getBytes());
    		isp.setSupportDevice(true);
    		IssuerKeyAndParameters ikap = isp.generate();
    		IssuerParameters ip = ikap.getIssuerParameters();

    		// issuer distributes the issuer parameters


    		// prover and verifier should validate the issuer parameters upon reception
    		ip.validate();

    		/*
    		 *  token issuance
    		 */

    		System.out.println("Issuing U-Prove tokens");
    		
    		// protocol parameters
    		byte[][] attributes = new byte[][] {
    				"first attribute".getBytes(),
    				"second attribute".getBytes()
    		};
    		byte[] tokenInformation = "token information".getBytes();
    		byte[] proverInformation = "prover information".getBytes();
    		int numberOfTokens = 5;

    		// Device setup
    		DeviceSetupParameters deviceSetupParams = new DeviceSetupParameters();
    		deviceSetupParams.setIssuerParameters(ip);
    		Device device = deviceSetupParams.generate();
    		int dIndex = ip.getProverIssuanceValues().length-1;
    		byte[] deviceZetaParameter = device.GetDeviceParameter(ip.getProverIssuanceValues()[dIndex]);
    		byte[] devicePublicKey = device.GetDevicePublicKey();
    		
    		// issuer generates first issuance message
    		IssuerProtocolParameters issuerProtocolParams = new IssuerProtocolParameters();
    		issuerProtocolParams.setIssuerKeyAndParameters(ikap);
    		issuerProtocolParams.setNumberOfTokens(numberOfTokens);
    		issuerProtocolParams.setTokenAttributes(attributes);
    		issuerProtocolParams.setTokenInformation(tokenInformation);
    		issuerProtocolParams.setDevicePublicKey(devicePublicKey);
    		Issuer issuer = issuerProtocolParams.generate();
    		byte[][] message1 = issuer.generateFirstMessage();

    		// prover generates second issuance message
    		ProverProtocolParameters proverProtocolParams = new ProverProtocolParameters();
    		proverProtocolParams.setIssuerParameters(ip);
    		proverProtocolParams.setNumberOfTokens(numberOfTokens);
    		proverProtocolParams.setTokenAttributes(attributes);
    		proverProtocolParams.setTokenInformation(tokenInformation);
    		proverProtocolParams.setProverInformation(proverInformation);
    		proverProtocolParams.setDeviceParameters(devicePublicKey, deviceZetaParameter);
    		Prover prover = proverProtocolParams.generate();
    		byte[][] message2 = prover.generateSecondMessage(message1);

    		// issuer generates third issuance message
    		byte[][] message3 = issuer.generateThirdMessage(message2);

    		// prover generates the U-Prove tokens
    		UProveKeyAndToken[] upkt = prover.generateTokens(message3);

    		// application specific storage of keys, tokens, and attributes

    		/*
    		 * token presentation
    		 */

    		System.out.println("Presenting a U-Prove token");
    		
    		// protocol parameters (shared by prover and verifier)
    		int[] disclosed = new int[] {2};
    		byte[] message = "message".getBytes();
    		byte[] deviceMessage = "message for Device".getBytes();

    		// prover chooses a token to use
    		UProveKeyAndToken keyAndToken = upkt[0];

    		// prover generates the presentation proof
    		DeviceManager.RegisterDevice(device);
    		PresentationProof proof = PresentationProtocol.generatePresentationProof(ip, disclosed, message, deviceMessage, keyAndToken, attributes);

    		// prover transmits the U-Prove token and presentation proof to the verifier 
    		UProveToken token = keyAndToken.getToken();

    		// verifier verifies the presentation proof
    		PresentationProtocol.verifyPresentationProof(ip, disclosed, message, deviceMessage, token, proof);
    	}
    	catch (Exception e) {
    		System.out.println(e.toString());
    		e.printStackTrace(System.out);
    		return;
    	}
    	
    	System.out.println("Sample completed successfully");
    }

    
    public static void main(final String[] args) throws IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, IOException, InvalidProofException {
    	Sample();
    	DeviceSample();
    }

}
