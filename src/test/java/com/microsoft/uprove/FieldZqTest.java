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

import com.microsoft.uprove.FieldZq;

import junit.framework.TestCase;

public class FieldZqTest extends TestCase {

    /**
     * Pregenerated values of q of a certain bit size
     */
    public static final BigInteger q160 = new BigInteger("864205495604807476120572616017955259175325408501",10);
    public static final BigInteger q256 = new BigInteger("84896877756360412237896161046774990303075827592397574017530475511940562831901",10);
    public static final BigInteger q384 = new BigInteger("21297606062079454981372686954256862334743956898303978801823397414393876830488298086801781128692120185700345215369907",10);
    public static final BigInteger q512 = new BigInteger("9843189626150050642595283784130947961596960524231801216407524773737766233603174410968673794154193660113416005333765900201018714634989316951618559475276051",10);
    public static final FieldZq Zq160 = FieldZq.getInstance(FieldZqTest.q160);
    public static final FieldZq Zq256 = FieldZq.getInstance(FieldZqTest.q256);
    public static final FieldZq Zq384 = FieldZq.getInstance(FieldZqTest.q384);
    public static final FieldZq Zq512 = FieldZq.getInstance(FieldZqTest.q512);
    public static final FieldZq Zq2 = FieldZq.getInstance(BigInteger.valueOf(2));
    
    final BigInteger q;
    final FieldZq Zq;

    /**
     * Constructor for FieldZqTest.
     * @param name
     */
    public FieldZqTest(String name) {
        super(name);

        // create field
        q = q160;
        Zq = FieldZq.getInstance(q);
    }

    public void testField() {
        try {
            FieldZq.getInstance(BigInteger.ZERO);
            fail("where's my IAE?");
        } catch (IllegalArgumentException iae) {
            // expected
        }
        try {
            FieldZq.getInstance(BigInteger.ONE);
            fail("where's my IAE?");
        } catch (IllegalArgumentException iae) {
            // expected
        }
        assertEquals(q, Zq.getQ());
        assertEquals(Zq.determineElement(BigInteger.ZERO), Zq.getZero());
        assertEquals(Zq.determineElement(BigInteger.ONE), Zq.getOne());
    }

    public void testCreation() throws IOException {
        BigInteger qMinOne = q160.add(BigInteger.ONE.negate());
        assertEquals(qMinOne, Zq.determineElement(qMinOne).toBigInteger());
        assertEquals(qMinOne, Zq.getElement(qMinOne.toByteArray()).toBigInteger());

        assertEquals(BigInteger.ZERO, Zq.determineElement(q160).toBigInteger());
        try {
            Zq.getElement(q160.toByteArray());
            fail("where's my meee?");
        } catch (IOException e) {
            // expected
        }

        BigInteger qPluOne = q160.add(BigInteger.ONE);
        assertEquals(BigInteger.ONE, Zq.determineElement(qPluOne).toBigInteger());
        try {
            Zq.getElement(qPluOne.toByteArray());
            fail("where's my meee?");
        } catch (IOException e) {
            // expected
        }

        FieldZq smallQ = FieldZq.getInstance(new BigInteger("11", 10));
        for (int i = 0; i < 1000; ++i) {
            assertTrue(smallQ.getRandomElement(true).toBigInteger().compareTo(smallQ.getQ()) < 0);
        }
    }

    public void testElements() {
        FieldZq.ZqElement a = Zq.determineElement(new BigInteger("615556367274179507480860823060192060549126308847", 10));
        FieldZq.ZqElement b = Zq.determineElement(new BigInteger("532517197526854872745926325154138865381270270073", 10));
        FieldZq.ZqElement c = Zq.determineElement(new BigInteger("233209155160143235844489815906552104186982775335", 10));

        // addition
        FieldZq.ZqElement add = a.add(b);
        assertEquals(add.toString(10),"283868069196226904106214532196375666755071170419");
        assertEquals(add, b.add(a)); // commutativity
        FieldZq.ZqElement add2 = add.add(c);
        add.addAssign(c);
        assertEquals(add,add2);

        // subtraction
        FieldZq.ZqElement sub = a.subtract(b);
        assertEquals(sub.toString(10),"83039169747324634734934497906053195167856038774");
        FieldZq.ZqElement sub2 = sub.subtract(c);
        sub.subtractAssign(c);
        assertEquals(sub,sub2);

        // addition & subtraction
        assertEquals(a, a.add(b).subtract(b));

        // multiplication
        FieldZq.ZqElement mul = a.multiply(b);
        assertEquals(mul.toString(10),"478722251673057547001372113231863232660313384287");
        assertEquals(mul, b.multiply(a)); // commutativity
        FieldZq.ZqElement mul2 = mul.multiply(c);
        mul.multiplyAssign(c);
        assertEquals(mul,mul2);

        // division
        FieldZq.ZqElement div = a.divide(b);
        assertEquals(div.toString(10),"83605468028075288842616905298252392917879191791");
        FieldZq.ZqElement div2 = div.divide(c);
        div.divideAssign(c);
        assertEquals(div,div2);

        // multiplication & division
        assertEquals(a, a.multiply(b).divide(b));

        // negate
        FieldZq.ZqElement neg = a.negate();
        assertEquals(a,neg.negate());
        assertEquals(Zq.getZero(), neg.add(a));
        assertEquals(Zq.getZero(), a.add(neg));
        neg.negateAssign();
        assertEquals(a,neg);

        // addition & negation
        assertEquals(Zq.getZero(), a.add(a.negate()));

        // inverse
        FieldZq.ZqElement inv = a.inverse();
        assertEquals(a,inv.inverse());
        assertEquals(Zq.getOne(), inv.multiply(a));
        assertEquals(Zq.getOne(), a.multiply(inv));
        inv.inverseAssign();
        assertEquals(a,inv);

        // multiplication & inverse
        assertEquals(Zq.getOne(), a.multiply(a.inverse()));

        // misc
        assertEquals(Zq, a.getField());
        assertEquals(Zq.determineElement(BigInteger.ZERO),Zq.determineElement(q));
        assertEquals(Zq.determineElement(BigInteger.ONE),Zq.determineElement(q.add(BigInteger.ONE)));

    }

    public void testNonElements() throws IOException {
        // make sure non-group elements are not accepted by constructor

        // test -1
        try {
            Zq.getElement(new BigInteger("-1").toByteArray());
            fail("expected IOException");
        } catch (IOException e) {
            // expected
        }

        // test q
        try {
            Zq.getElement(q.toByteArray());
        fail("expected IOException");
        } catch (IOException e) {
            // expected
        }

        // test q+1
        try {
            Zq.getElement(q.add(BigInteger.ONE).toByteArray());
            fail("expected IOException");
        } catch (IOException e) {
            // expected
        }

    }

    // small test with known field Z13
    public void testZ13() {

        FieldZq Z13 = FieldZq.getInstance(new BigInteger("13", 10));

        FieldZq.ZqElement two   = Z13.determineElement(new BigInteger("2", 10));
        FieldZq.ZqElement seven = Z13.determineElement(new BigInteger("7", 10));
        FieldZq.ZqElement nine  = Z13.determineElement(new BigInteger("9", 10));

        assertEquals(nine, two.add(seven));

        // make sure we can't operate with field Zq
        boolean caught = false;
        try {
            two.add(Zq.getOne());
        } catch (IllegalArgumentException iae) {
            caught = true;
        }
        assertEquals(true, caught);
        assertNotSame(two.getField(), Zq);
    }

    public void testEquality() {
        FieldZq.ZqElement a = Zq.determineElement(new BigInteger("274179507486082306019054912630", 10));
        FieldZq.ZqElement b = a.add(a);
        FieldZq.ZqElement c = b.subtract(a);

        // a != b
        assertTrue(!a.equals(b));
        assertTrue(a.hashCode() != b.hashCode());

        // a == c
        assertTrue(a.equals(c));
        assertTrue(a.hashCode() == c.hashCode());

        // element copy constructor
        FieldZq.ZqElement d = Zq.new ZqElement(a);
        assertNotSame(a, d);
        assertEquals(a, d);
        assertSame(a.getField(), d.getField());
    }

}
