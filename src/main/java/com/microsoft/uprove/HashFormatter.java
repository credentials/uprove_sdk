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
import java.io.OutputStream;

/*
 * LOW-LEVEL IMPLEMENTATION CLASS. NOT PART OF PUBLIC API.
 */

/**
 * U-Prove hash input formatter.
 */
final class HashFormatter {

	private OutputStream output;
	private byte[] sizeBuffer = new byte[4];
	
	public HashFormatter(final OutputStream output) throws NullPointerException, IOException {
        super();
        this.output = output;
	}

	public HashFormatter(final HashFormatter formatter, final OutputStream output) throws NullPointerException {
        super();
        if (output == null) {
            throw new NullPointerException();
        }
        this.output = output;
	}

	public void reset(MDOutputStream mdSink) throws IOException {
		// TODO Auto-generated method stub
	}

	public void encode(byte b) throws IOException {
		output.write(b);
	}

	public void encode(int i) throws IOException {
		sizeBuffer[0] = (byte) (i >> 24);
        sizeBuffer[1] = (byte) (i >> 16);
        sizeBuffer[2] = (byte) (i >>  8);
        sizeBuffer[3] = (byte)  i;
        output.write(sizeBuffer);
	}

	public void encode(byte[] opaque) throws IOException {
		encode(opaque, 0, opaque != null ? opaque.length : 0);		
	}

	public void encode(byte[] opaque, int offset, int len) throws IOException {
        /*
         * Sequences are encoded as an unsigned long value, followed by the
         * elements of the sequence. The initial unsigned long contains the
         * number of elements in the sequence. The elements of the sequence
         * are encoded as specified for their type.
         */
        encode(len);
        if (len != 0) {
        	output.write(opaque, offset, len);
        }
	}

}
