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

import java.io.EOFException;
import java.io.IOException;
import java.io.Writer;


/**
 * Provides base64 encoding/decoding. See
 * <a href="http://www.ietf.org/rfc/rfc2045.txt?number=2045">RFC2045</a>
 * for details about base64.
 *
 * @see <a href="http://www.ietf.org/rfc/rfc2045.txt" target="_top">RFC2045</a>
 */
final class Base64 {

    /**
     * A simple interface to allow the implementation of Base64.encode() to
     * efficiently support writing to a variety of data sinks.
     *
     */
    private interface EncodeTarget {

        /**
         * Write an array of characters to the data sink.
         * @param chars an array of characters.
         * @throws IOException if an error occurs while encoding.
         */
        void append(char[] chars) throws IOException;

        /**
         * Write part of an array of characters to the data sink.
         * @param chars an array of characters.
         * @param off offset into the array.
         * @param len number of characters to write.
         * @throws IOException if an error occurs while encoding.
         */
        void append(char[] chars, int off, int len) throws IOException;

    }

    /**
     * Identifier of the standard base64 alphabet.
     */
    public static final int ALPHA_B64 = 0;

    /**
     * Identifier of the base64url (URL and filename safe ) alphabet.
     */
    public static final int ALPHA_B64URL = 1;

    /**
     * Default line separator as specified in RFC2045.
     */
    private static final String LINE_SEP = "\r\n";

    /**
     * Default line length as given in RFC2045.
     */
    private static final int LINE_LEN = 76;

    /**
     * The base64 translation table.
     */
    private static final char[] B64_ALPHABET = { 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', '+', '/' };

    private static final int[] B64_MAP = { 62, -1, -1, -1, 63, 52, 53, 54, 55,
            56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4,
            5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
            23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32,
            33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48,
            49, 50, 51 };
    private static final char B64_MAP_LOW_CHAR = '+';

    private static final int[] B64URL_MAP = { 62, -1, -1, 52, 53, 54, 55,
        56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4,
        5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
        23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32,
        33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48,
        49, 50, 51 };
    private static final char B64URL_MAP_LOW_CHAR = '-';

    /**
     * The base64url translation table.
     */
    private static final char[] B64URL_ALPHABET = { 'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', '-', '_' };

    // states for the decoder
    private static final int STATE_START = 0;
    private static final int STATE_R1 = 1;
    private static final int STATE_R2 = 2;
    private static final int STATE_R2OE = 3;
    private static final int STATE_R3 = 4;
    private static final int STATE_R3OE = 5;
    private static final int STATE_R3E = 6;

    /**
     * private constructor, prevents instantiation.
     */
    private Base64() {
        super();
    }

    /**
     * Base64-encodes the given buffer into the given target.
     * @param buffer a byte array.
     * @param target a target where the encoded data will be written.
     * @param noPad <code>true</code> to skip the padding.
     * @throws IOException
     */
    private static void encode(final char[] alphabet, final byte[] buffer,
            final EncodeTarget target, final boolean noPad)
            throws IOException {
        final int n = buffer.length;
        final char[] encoded = new char[4];
        int state = 0;
        int index = 0;

        for (int i = 0; i < n; i++) {
            int c = buffer[i] & 0xFF;

            // Take three bytes of input = 24 bits
            // Split it into 4 chunks of six bits each
            // Treat these chunks as indices into the
            // base64 table above.

            switch (state) {

            case 0:
                encoded[0] = alphabet[(c >> 2) & 0x3F];
                index = (c << 4) & 0x30;
                break;

            case 1:
                encoded[1] = alphabet[index | ((c >> 4) & 0x0F)];
                index = (c << 2) & 0x3C;
                break;

            case 2:
                encoded[2] = alphabet[index | ((c >> 6) & 0x03)];
                encoded[3] = alphabet[c & 0x3F];
                target.append(encoded);
                break;

            default:
                throw new AssertionError("Bogus switch value");
            }
            state = (state + 1) % 3;
        }

        // Complete the string with zero bits
        // and pad with "=" characters as necessary

        switch (state) {
        case 0:
            // No padding necessary
            break;
        case 1:
            encoded[1] = alphabet[index];
            if (noPad) {
                target.append(encoded, 0, 2);
            } else {
                encoded[2] = '=';
                encoded[3] = '=';
                target.append(encoded);
            }
            break;
        case 2:
            encoded[2] = alphabet[index];
            if (noPad) {
                target.append(encoded, 0, 3);
            } else {
                encoded[3] = '=';
                target.append(encoded);
            }
            break;
        default:
            throw new AssertionError("Bogus switch value");
        }
    }

    /**
     * Returns a base64-encoded string of <code>buffer</code>.
     *
     * @param alphabet either <code>ALPHA_B64</code> for the standard base64
     * encoding or <code>ALPHA_B64URL</code> for the URL and filename safe
     * encoding.
     * @param buffer Buffer to encode.
     * @param noPad <code>true</code> to omit final padding characters.
     * @return Base64-encoded string of <code>buffer</code>.
     */
    public static String encode(final int alphabet, final byte[] buffer,
            final boolean noPad) {
        if (alphabet != ALPHA_B64 && alphabet != ALPHA_B64URL) {
            throw new IllegalArgumentException("invalid alphabet");
        }
        if (buffer == null) {
            throw new IllegalArgumentException("buffer is null");
        }

        // calculate the resulting string length.
        final int strLen = ((buffer.length + 2) / 3) * 4;
        final StringBuffer sb = new StringBuffer(strLen);

        try {
            encode(alphabet == ALPHA_B64 ? B64_ALPHABET : B64URL_ALPHABET,
                   buffer, new EncodeTarget() {
                public void append(final char[] chars) {
                    sb.append(chars);
                }
                public void append(final char[] chars, final int off,
                        final int len) {
                    sb.append(chars, off, len);
                }
            },
                   noPad);
        } catch (IOException e) {
            // the exception will never be thrown from
            // StringBuffer#append(char[])
            AssertionError ae = new AssertionError("Impossible exception");
            ae.initCause(e);
            throw ae;
        }

        return sb.toString();
    }

    /**
     * Returns a base64-encoded string of <code>buffer</code>.
     *
     * @param buffer
     *            Buffer to encode.
     * @return Base64-encoded string of <code>buffer</code>.
     */
    public static String encode(final byte[] buffer) {
        return encode(ALPHA_B64, buffer, false);
    }

    /**
     * Base64-encodes a buffer, writing the results to a java.io.Writer.
     * @param buffer the data to encode.
     * @param w the writer to which the encoded data is to be written.
     * @throws IOException if there is an error while writing to the Writer.
     */
    public static void encode(final byte[] buffer, final Writer w)
            throws IOException {
        if (buffer == null) {
            throw new IllegalArgumentException("buffer is null");
        }
        if (w == null) {
            throw new IllegalArgumentException("writer is null");
        }

        encode(B64_ALPHABET, buffer, new EncodeTarget() {
            public void append(final char[] chars) throws IOException {
                w.write(chars);
            }
            public void append(final char[] chars, final int off,
                    final int len) throws IOException {
                w.write(chars, off, len);
            }
        }, false);
    }

    /**
     * Returns a base64-encoded string of <code>buffer</code>.
     *
     * Base64 encode and split results over multiple lines according to the
     * specifications given in RFC-2045 (76 char max per line) but using
     * specified lineSep as line delimiter.
     *
     * @param buffer
     *            Buffer to encode
     * @param lineSep
     *            Line separator to use
     * @return Base64-encoded string of <code>buffer</code>.
     */
    public static String encode(final byte[] buffer, final String lineSep) {
        return splitToMultLine(encode(buffer), LINE_LEN, lineSep);
    }

    /**
     * Returns a base64-encoded string of <code>buffer</code>.
     *
     * Base64 encode and split results over multiple lines according to the
     * specifications given in RFC-2045 (using CRLF as line separator) but
     * using with a given (lineLen) line length (instead of default one).
     *
     * @param buffer Buffer to encode
     * @param lineLen Maximum length for a line
     * @return Base64-encoded string of <code>buffer</code>.
     */
    public static String encode(final byte[] buffer, final int lineLen) {
        return splitToMultLine(encode(buffer), lineLen, LINE_SEP);
    }

    /**
     * Returns a base64-encoded string of <code>buffer</code>.
     *
     * Base64 encode and split results over multiple lines according to the
     * specifications given in RFC-2045 but using specified lineSep as line
     * delimiter and using specified lineLen for the length of a line (max char
     * per line).
     *
     * @param buffer
     *            Buffer to encode
     * @param lineSep
     *            Line separator to use
     * @param lineLen
     *            Maximum length for a line
     * @return Base64-encoded string of <code>buffer</code>.
     */
    public static String encode(final byte[] buffer, final String lineSep,
            final int lineLen) {
        return splitToMultLine(encode(buffer), lineLen, lineSep);
    }

    /**
     * Returns the 6-bit value corresponding to a character in the base64
     * alphabet. <b>Note: </b> the pad character ('=') is considered to have
     * the value 0.
     *
     * @param ch a character in the base64 alphabet.
     * @return the 6-bit value.
     * @throws ArrayIndexOutOfBoundsException if <code>ch</code> is not in the
     * alphabet.
     */
    private static int decodeChar(final int alphabet, final char ch) {
        final int retVal = alphabet == ALPHA_B64
                ? B64_MAP[ch - B64_MAP_LOW_CHAR]
                : B64URL_MAP[ch - B64URL_MAP_LOW_CHAR];
        if (retVal == -1) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return retVal;
    }

    /**
     * Returns the decoding of the encoded <code>base64String</code>.
     * Data is read from the source until either the end of the base64 data has
     * been reached (one or two pad chars are read) or all data from the source
     * has been read.
     *
     * @param alphabet either <code>ALPHA_B64</code> for the standard base64
     * encoding or <code>ALPHA_B64URL</code> for the URL and filename safe
     * encoding.
     * @param base64String
     *            Base64-encoded string.
     * @param lenientPad <code>true</code> to allow the padding to be omitted.
     * @return Decoding of the base64-encoded <code>base64String</code>.
     * @throws EOFException if the end of <code>base64String</code> is reached
     * before decoding is complete.
     * @throws IOException if the buffer contains invalid data.
     */
    public static byte[] decode(final int alphabet,
            final CharSequence base64String, final boolean lenientPad)
            throws IOException {
        if (alphabet != ALPHA_B64 && alphabet != ALPHA_B64URL) {
            throw new IllegalArgumentException("invalid alphabet");
        }
        final int inLen = base64String.length();
        // make an upper-bound calculation for the resulting byte array size.
        // due to whitespace in the string, this may be larger than needed.
        final int upperBound = ((inLen + 2) / 4) * 3;

        byte[] buffer = new byte[upperBound];

        int idxIn = 0;
        int idxOut = 0;
        char ch = 0;
        int state = STATE_START;
        int i = 0;
        /* note: RFC-2045 suggests that invalid characters may be ignored, but
         * let's be more strict. */
        // hand-built DFA
        try {
            parser: while (true) {
                if (idxIn < inLen) {
                    ch = base64String.charAt(idxIn++);
                    if (Character.isWhitespace(ch)) {
                        // stay in the current state
                        continue;
                    }
                } else {
                    // handle EOF here outside of the main switch below
                    switch (state) {
                    case STATE_R1: // expect second character
                    case STATE_R2: // expect third character
                    case STATE_R3: // expect fourth character
                    case STATE_R3E: // expect second pad
                        throw new EOFException();
                    case STATE_R2OE: // expect third character, pad, or end
                    case STATE_R3OE: // expect fourth character, pad, or end
                        if (!lenientPad) {
                            // missing required pad character
                            throw new EOFException();
                        }
                        // FALL THROUGH
                    default:
                        assert state == STATE_START
                                || state == STATE_R2OE
                                || state == STATE_R3OE;
                        break parser; // we've read everything
                    }
                }
                // either eof or we've read a non-whitespace character
                switch (state) {
                case STATE_START: // we're at the start
                    i = decodeChar(alphabet, ch) << 18;
                    state = STATE_R1; // we've read the first char
                    break;
                case STATE_R1: // we've read the first char
                    i |= decodeChar(alphabet, ch) << 12;
                    // push out the first octet
                    buffer[idxOut++] = (byte) (i >> 16);
                    state = ((short) i) == 0
                            ? STATE_R2OE // we've read two and may be at end
                            : STATE_R2; // we've read the second char
                    break;
                case STATE_R2: // we've read the second char
                    i |= decodeChar(alphabet, ch) << 6;
                    // push out the second octet
                    buffer[idxOut++] = (byte) (i >> 8);
                    state = ((byte) i) == 0
                            ? STATE_R3OE // we've read three and may be at end
                            : STATE_R3; // we've read the third char
                    break;
                case STATE_R2OE: // we've read two and may be at the end
                    if (ch == '=') {
                        state = STATE_R3E; // we've read two and a pad
                    } else {
                        i |= decodeChar(alphabet, ch) << 6;
                        // push out the second octet
                        buffer[idxOut++] = (byte) (i >> 8);
                        state = ((byte) i) == 0
                                ? STATE_R3OE // we've read three; may be at end
                                : STATE_R3; // we've read the third char
                    }
                    break;
                case STATE_R3: // we've read the third char
                    i |= decodeChar(alphabet, ch);
                    // push out the third octet
                    buffer[idxOut++] = (byte) i;
                    // get ready to start again
                    state = STATE_START;
                    break;
                case STATE_R3OE: // we've read three and may be at the end
                    if (ch == '=') {
                        break parser; // we've read three and a pad
                    }
                    i |= decodeChar(alphabet, ch);
                    // push out the third octet
                    buffer[idxOut++] = (byte) i;
                    // get ready to start again
                    state = STATE_START;
                    break;
                case STATE_R3E: // we've read two and a pad
                    if (ch != '=') {
                        // rethrown with a proper message below
                        throw new ArrayIndexOutOfBoundsException();
                    }
                    break parser; // we've read two chars and two pads
                default:
                    throw new AssertionError("impossible state");
                }
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            // decodeChar wasn't pleased
            throw new IOException("invalid character at position "
                                  + (idxIn - 1));
        }

        if (buffer.length == idxOut) {
            // we allocated exactly what was needed
            return buffer;
        }

        final byte[] retVal = new byte[idxOut];
        System.arraycopy(buffer, 0, retVal, 0, idxOut);
        return retVal;
    }

    /**
     * Returns the decoding of the base64-encoded <code>base64String</code>.
     * Data is read from the source until either the end of the base64 data has
     * been reached (one or two pad chars are read) or all data from the source
     * has been read.
     *
     * @param base64String
     *            Base64-encoded string.
     * @return Decoding of the base64-encoded <code>base64String</code>.
     * @throws EOFException if the end of <code>base64String</code> is reached
     * before decoding is complete.
     * @throws IOException if the buffer contains invalid data.
     */
    public static byte[] decode(final CharSequence base64String)
            throws IOException {
        // validate argument
        if (base64String == null) {
            throw new IllegalArgumentException("base64String is null");
        }
        return decode(ALPHA_B64, base64String, false);
    }

    /**
     * Split the string in multiples lines.
     *
     * Split over multiple lines or lineLen characters each, using
     * lineSep as the line separator.
     *
     * @param s String to split
     * @param lineLen Line length
     * @param lineSep Line separator string (usually CRLF, ...)
     * @return resulting string split over multiple lines
     */
    private static String splitToMultLine(final String s, final int lineLen,
            final String lineSep) {
        // validate arguments
        if (lineLen < 1) {
            throw new IllegalArgumentException(
                    "line len is negative or equals 0");
        }
        if (lineSep == null) {
            throw new IllegalArgumentException(
                    "line separator (lineSep) is null");
        }

        final StringBuffer res = new StringBuffer();
        final int origSize = s.length();

        for (int index = 0; index < origSize; index += lineLen) {
            int offset = ((index + lineLen) > origSize)
                          ? (index + (origSize % lineLen))
                          : (index + lineLen);
            res.append(s.substring(index, offset));

            // append end of line (if not at the last line)
            if (offset < origSize) {
                res.append(lineSep);
            }
        }

        return res.toString();
    }
}
