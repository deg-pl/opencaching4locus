/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bogus.domowygpx.utils;

import java.io.UnsupportedEncodingException;

/**
 * Converts hexadecimal Strings. The charset used for certain operation can be set, the default is set in
 * {@link #DEFAULT_CHARSET_NAME}
 * 
 * <p>So, take your crystal ball and find out, which classes may be silently loaded by the Android, 
 * and replace your classes 
 * <p>
<pre>11-25 22:41:37.325 I/dalvikvm(12743): Could not find method org.apache.commons.codec.binary.Base64.<init>, referenced from method org.apache.commons.codec.binary.Base64InputStream.<init>
11-25 22:41:37.325 W/dalvikvm(12743): VFY: unable to resolve direct method 5946: Lorg/apache/commons/codec/binary/Base64;.<init> (Z)V
11-25 22:41:37.325 D/dalvikvm(12743): VFY: replacing opcode 0x70 at 0x0003
11-25 22:41:37.325 I/dalvikvm(12743): Could not find method org.apache.commons.codec.binary.Base64.<init>, referenced from method org.apache.commons.codec.binary.Base64InputStream.<init>
11-25 22:41:37.325 W/dalvikvm(12743): VFY: unable to resolve direct method 5944: Lorg/apache/commons/codec/binary/Base64;.<init> (I[B)V
11-25 22:41:37.325 D/dalvikvm(12743): VFY: replacing opcode 0x70 at 0x0002
</pre>
 * @since 1.1
 * @author Apache Software Foundation
 * @version $Id: Hex.java 1157192 2011-08-12 17:27:38Z ggregory $
 */
public class Hex {

    /**
     * Default charset name is {@link CharEncoding#UTF_8}
     * 
     * @since 1.4
     */
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * Converts an array of characters representing hexadecimal values into an array of bytes of those same values. The
     * returned array will be half the length of the passed array, as it takes two characters to represent any given
     * byte. An exception is thrown if the passed char array has an odd number of elements.
     * 
     * @param data
     *            An array of characters containing hexadecimal digits
     * @return A byte array containing binary data decoded from the supplied char array.
     * @throws IllegalArgumentException
     *             Thrown if an odd number or illegal of characters is supplied
     */
    public static byte[] decodeHex(char[] data) throws IllegalArgumentException {

        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new IllegalArgumentException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     * 
     * @param data
     *            a byte[] to convert to Hex characters
     * @return A char[] containing hexadecimal characters
     */
    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     * 
     * @param data
     *            a byte[] to convert to Hex characters
     * @param toLowerCase
     *            <code>true</code> converts to lowercase, <code>false</code> to uppercase
     * @return A char[] containing hexadecimal characters
     * @since 1.4
     */
    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     * 
     * @param data
     *            a byte[] to convert to Hex characters
     * @param toDigits
     *            the output alphabet
     * @return A char[] containing hexadecimal characters
     * @since 1.4
     */
    protected static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    /**
     * Converts an array of bytes into a String representing the hexadecimal values of each byte in order. The returned
     * String will be double the length of the passed array, as it takes two characters to represent any given byte.
     * 
     * @param data
     *            a byte[] to convert to Hex characters
     * @return A String containing hexadecimal characters
     * @since 1.4
     */
    public static String encodeHexString(byte[] data) {
        return new String(encodeHex(data));
    }

    /**
     * Converts a hexadecimal character to an integer.
     * 
     * @param ch
     *            A character to convert to an integer digit
     * @param index
     *            The index of the character in the source
     * @return An integer
     * @throws IllegalArgumentException
     *             Thrown if ch is an illegal hex character
     */
    protected static int toDigit(char ch, int index) throws IllegalArgumentException {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new IllegalArgumentException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    private final String charsetName;

    /**
     * Creates a new codec with the default charset name {@link #DEFAULT_CHARSET_NAME}
     */
    public Hex() {
        // use default encoding
        this.charsetName = DEFAULT_CHARSET_NAME;
    }

    /**
     * Creates a new codec with the given charset name.
     * 
     * @param csName
     *            the charset name.
     * @since 1.4
     */
    public Hex(String csName) {
        this.charsetName = csName;
    }

    /**
     * Converts an array of character bytes representing hexadecimal values into an array of bytes of those same values.
     * The returned array will be half the length of the passed array, as it takes two characters to represent any given
     * byte. An exception is thrown if the passed char array has an odd number of elements.
     * 
     * @param array
     *            An array of character bytes containing hexadecimal digits
     * @return A byte array containing binary data decoded from the supplied byte array (representing characters).
     * @throws IllegalArgumentException
     *             Thrown if an odd number of characters is supplied to this function
     * @see #decodeHex(char[])
     */
    public byte[] decode(byte[] array) throws IllegalArgumentException {
        try {
            return decodeHex(new String(array, getCharsetName()).toCharArray());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * Converts a String or an array of character bytes representing hexadecimal values into an array of bytes of those
     * same values. The returned array will be half the length of the passed String or array, as it takes two characters
     * to represent any given byte. An exception is thrown if the passed char array has an odd number of elements.
     * 
     * @param object
     *            A String or, an array of character bytes containing hexadecimal digits
     * @return A byte array containing binary data decoded from the supplied byte array (representing characters).
     * @throws DecoderException
     *             Thrown if an odd number of characters is supplied to this function or the object is not a String or
     *             char[]
     * @see #decodeHex(char[])
     */
    public Object decode(Object object) throws IllegalArgumentException {
        try {
            char[] charArray = object instanceof String ? ((String) object).toCharArray() : (char[]) object;
            return decodeHex(charArray);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * Converts an array of bytes into an array of bytes for the characters representing the hexadecimal values of each
     * byte in order. The returned array will be double the length of the passed array, as it takes two characters to
     * represent any given byte.
     * <p>
     * The conversion from hexadecimal characters to the returned bytes is performed with the charset named by
     * {@link #getCharsetName()}.
     * </p>
     * 
     * @param array
     *            a byte[] to convert to Hex characters
     * @return A byte[] containing the bytes of the hexadecimal characters
     * @throws IllegalStateException
     *             if the charsetName is invalid. This API throws {@link IllegalStateException} instead of
     *             {@link UnsupportedEncodingException} for backward compatibility.
     * @see #encodeHex(byte[])
     */
    public byte[] encode(byte[] array) {
        return getBytesUnchecked(encodeHexString(array), getCharsetName());
    }

    /**
     * Converts a String or an array of bytes into an array of characters representing the hexadecimal values of each
     * byte in order. The returned array will be double the length of the passed String or array, as it takes two
     * characters to represent any given byte.
     * <p>
     * The conversion from hexadecimal characters to bytes to be encoded to performed with the charset named by
     * {@link #getCharsetName()}.
     * </p>
     * 
     * @param object
     *            a String, or byte[] to convert to Hex characters
     * @return A char[] containing hexadecimal characters
     * @throws EncoderException
     *             Thrown if the given object is not a String or byte[]
     * @see #encodeHex(byte[])
     */
    public Object encode(Object object) throws IllegalArgumentException {
        try {
            byte[] byteArray = object instanceof String ? ((String) object).getBytes(getCharsetName()) : (byte[]) object;
            return encodeHex(byteArray);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * Gets the charset name.
     * 
     * @return the charset name.
     * @since 1.4
     */
    public String getCharsetName() {
        return this.charsetName;
    }

    /**
     * Returns a string representation of the object, which includes the charset name.
     * 
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return super.toString() + "[charsetName=" + this.charsetName + "]";
    }

    /**
     * Encodes the given string into a sequence of bytes using the named charset, storing the result into a new byte
     * array.
     * <p>
     * This method catches {@link UnsupportedEncodingException} and rethrows it as {@link IllegalStateException}, which
     * should never happen for a required charset name. Use this method when the encoding is required to be in the JRE.
     * </p>
     * 
     * @param string
     *            the String to encode, may be <code>null</code>
     * @param charsetName
     *            The name of a required {@link java.nio.charset.Charset}
     * @return encoded bytes, or <code>null</code> if the input string was <code>null</code>
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen for a
     *             required charset name.
     * @see CharEncoding
     * @see String#getBytes(String)
     */
    public static byte[] getBytesUnchecked(String string, String charsetName) {
        if (string == null) {
            return null;
        }
        try {
            return string.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(charsetName, e);
        }
    }
}
