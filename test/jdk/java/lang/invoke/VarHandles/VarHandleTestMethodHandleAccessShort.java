/*
 * Copyright (c) 2015, 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

// -- This file was mechanically generated: Do not edit! -- //

/*
 * @test
 * @comment Set CompileThresholdScaling to 0.1 so that the warmup loop sets to 2000 iterations
 *          to hit compilation thresholds
 * @run testng/othervm -Diters=2000 -XX:CompileThresholdScaling=0.1 VarHandleTestMethodHandleAccessShort
 */

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class VarHandleTestMethodHandleAccessShort extends VarHandleBaseTest {
    static final Class<?> type = short.class;

    static final short static_final_v = (short)0x0123;

    static short static_v = (short)0x0123;

    final short final_v = (short)0x0123;

    short v;

    VarHandle vhFinalField;

    VarHandle vhField;

    VarHandle vhStaticField;

    VarHandle vhStaticFinalField;

    VarHandle vhArray;

    @BeforeClass
    public void setup() throws Exception {
        vhFinalField = MethodHandles.lookup().findVarHandle(
                VarHandleTestMethodHandleAccessShort.class, "final_v", type);

        vhField = MethodHandles.lookup().findVarHandle(
                VarHandleTestMethodHandleAccessShort.class, "v", type);

        vhStaticFinalField = MethodHandles.lookup().findStaticVarHandle(
            VarHandleTestMethodHandleAccessShort.class, "static_final_v", type);

        vhStaticField = MethodHandles.lookup().findStaticVarHandle(
            VarHandleTestMethodHandleAccessShort.class, "static_v", type);

        vhArray = MethodHandles.arrayElementVarHandle(short[].class);
    }


    @DataProvider
    public Object[][] accessTestCaseProvider() throws Exception {
        List<AccessTestCase<?>> cases = new ArrayList<>();

        for (VarHandleToMethodHandle f : VarHandleToMethodHandle.values()) {
            cases.add(new MethodHandleAccessTestCase("Instance field",
                                                     vhField, f, hs -> testInstanceField(this, hs)));
            cases.add(new MethodHandleAccessTestCase("Instance field unsupported",
                                                     vhField, f, hs -> testInstanceFieldUnsupported(this, hs),
                                                     false));

            cases.add(new MethodHandleAccessTestCase("Static field",
                                                     vhStaticField, f, VarHandleTestMethodHandleAccessShort::testStaticField));
            cases.add(new MethodHandleAccessTestCase("Static field unsupported",
                                                     vhStaticField, f, VarHandleTestMethodHandleAccessShort::testStaticFieldUnsupported,
                                                     false));

            cases.add(new MethodHandleAccessTestCase("Array",
                                                     vhArray, f, VarHandleTestMethodHandleAccessShort::testArray));
            cases.add(new MethodHandleAccessTestCase("Array unsupported",
                                                     vhArray, f, VarHandleTestMethodHandleAccessShort::testArrayUnsupported,
                                                     false));
            cases.add(new MethodHandleAccessTestCase("Array index out of bounds",
                                                     vhArray, f, VarHandleTestMethodHandleAccessShort::testArrayIndexOutOfBounds,
                                                     false));
        }

        // Work around issue with jtreg summary reporting which truncates
        // the String result of Object.toString to 30 characters, hence
        // the first dummy argument
        return cases.stream().map(tc -> new Object[]{tc.toString(), tc}).toArray(Object[][]::new);
    }

    @Test(dataProvider = "accessTestCaseProvider")
    public <T> void testAccess(String desc, AccessTestCase<T> atc) throws Throwable {
        T t = atc.get();
        int iters = atc.requiresLoop() ? ITERS : 1;
        for (int c = 0; c < iters; c++) {
            atc.testAccess(t);
        }
    }


    static void testInstanceField(VarHandleTestMethodHandleAccessShort recv, Handles hs) throws Throwable {
        // Plain
        {
            hs.get(TestAccessMode.SET).invokeExact(recv, (short)0x0123);
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x0123, "set short value");
        }


        // Volatile
        {
            hs.get(TestAccessMode.SET_VOLATILE).invokeExact(recv, (short)0x4567);
            short x = (short) hs.get(TestAccessMode.GET_VOLATILE).invokeExact(recv);
            assertEquals(x, (short)0x4567, "setVolatile short value");
        }

        // Lazy
        {
            hs.get(TestAccessMode.SET_RELEASE).invokeExact(recv, (short)0x0123);
            short x = (short) hs.get(TestAccessMode.GET_ACQUIRE).invokeExact(recv);
            assertEquals(x, (short)0x0123, "setRelease short value");
        }

        // Opaque
        {
            hs.get(TestAccessMode.SET_OPAQUE).invokeExact(recv, (short)0x4567);
            short x = (short) hs.get(TestAccessMode.GET_OPAQUE).invokeExact(recv);
            assertEquals(x, (short)0x4567, "setOpaque short value");
        }

        hs.get(TestAccessMode.SET).invokeExact(recv, (short)0x0123);

        // Compare
        {
            boolean r = (boolean) hs.get(TestAccessMode.COMPARE_AND_SET).invokeExact(recv, (short)0x0123, (short)0x4567);
            assertEquals(r, true, "success compareAndSet short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x4567, "success compareAndSet short value");
        }

        {
            boolean r = (boolean) hs.get(TestAccessMode.COMPARE_AND_SET).invokeExact(recv, (short)0x0123, (short)0x89AB);
            assertEquals(r, false, "failing compareAndSet short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x4567, "failing compareAndSet short value");
        }

        {
            short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE).invokeExact(recv, (short)0x4567, (short)0x0123);
            assertEquals(r, (short)0x4567, "success compareAndExchange short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x0123, "success compareAndExchange short value");
        }

        {
            short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE).invokeExact(recv, (short)0x4567, (short)0x89AB);
            assertEquals(r, (short)0x0123, "failing compareAndExchange short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x0123, "failing compareAndExchange short value");
        }

        {
            short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE_ACQUIRE).invokeExact(recv, (short)0x0123, (short)0x4567);
            assertEquals(r, (short)0x0123, "success compareAndExchangeAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x4567, "success compareAndExchangeAcquire short value");
        }

        {
            short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE_ACQUIRE).invokeExact(recv, (short)0x0123, (short)0x89AB);
            assertEquals(r, (short)0x4567, "failing compareAndExchangeAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x4567, "failing compareAndExchangeAcquire short value");
        }

        {
            short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE_RELEASE).invokeExact(recv, (short)0x4567, (short)0x0123);
            assertEquals(r, (short)0x4567, "success compareAndExchangeRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x0123, "success compareAndExchangeRelease short value");
        }

        {
            short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE_RELEASE).invokeExact(recv, (short)0x4567, (short)0x89AB);
            assertEquals(r, (short)0x0123, "failing compareAndExchangeRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x0123, "failing compareAndExchangeRelease short value");
        }

        {
            MethodHandle mh = hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_PLAIN);
            boolean success = false;
            for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                success = (boolean) mh.invokeExact(recv, (short)0x0123, (short)0x4567);
                if (!success) weakDelay();
            }
            assertEquals(success, true, "success weakCompareAndSetPlain short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x4567, "success weakCompareAndSetPlain short value");
        }

        {
            boolean success = (boolean) hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_PLAIN).invokeExact(recv, (short)0x0123, (short)0x89AB);
            assertEquals(success, false, "failing weakCompareAndSetPlain short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x4567, "failing weakCompareAndSetPlain short value");
        }

        {
            MethodHandle mh = hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_ACQUIRE);
            boolean success = false;
            for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                success = (boolean) mh.invokeExact(recv, (short)0x4567, (short)0x0123);
                if (!success) weakDelay();
            }
            assertEquals(success, true, "success weakCompareAndSetAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x0123, "success weakCompareAndSetAcquire short");
        }

        {
            boolean success = (boolean) hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_ACQUIRE).invokeExact(recv, (short)0x4567, (short)0x89AB);
            assertEquals(success, false, "failing weakCompareAndSetAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x0123, "failing weakCompareAndSetAcquire short value");
        }

        {
            MethodHandle mh = hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_RELEASE);
            boolean success = false;
            for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                success = (boolean) mh.invokeExact(recv, (short)0x0123, (short)0x4567);
                if (!success) weakDelay();
            }
            assertEquals(success, true, "success weakCompareAndSetRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x4567, "success weakCompareAndSetRelease short");
        }

        {
            boolean success = (boolean) hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_RELEASE).invokeExact(recv, (short)0x0123, (short)0x89AB);
            assertEquals(success, false, "failing weakCompareAndSetRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x4567, "failing weakCompareAndSetRelease short value");
        }

        {
            boolean success = false;
            MethodHandle mh = hs.get(TestAccessMode.WEAK_COMPARE_AND_SET);
            for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                success = (boolean) mh.invokeExact(recv, (short)0x4567, (short)0x0123);
                if (!success) weakDelay();
            }
            assertEquals(success, true, "success weakCompareAndSet short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x0123, "success weakCompareAndSet short");
        }

        {
            boolean success = (boolean) hs.get(TestAccessMode.WEAK_COMPARE_AND_SET).invokeExact(recv, (short)0x4567, (short)0x89AB);
            assertEquals(success, false, "failing weakCompareAndSet short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x0123, "failing weakCompareAndSet short value");
        }

        // Compare set and get
        {
            short o = (short) hs.get(TestAccessMode.GET_AND_SET).invokeExact(recv, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndSet short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)0x4567, "getAndSet short value");
        }

        // get and add, add and get
        {
            hs.get(TestAccessMode.SET).invokeExact(recv, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_ADD).invokeExact(recv, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndAdd short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)((short)0x0123 + (short)0x4567), "getAndAdd short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact(recv, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_ADD_ACQUIRE).invokeExact(recv, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndAddAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)((short)0x0123 + (short)0x4567), "getAndAddAcquire short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact(recv, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_ADD_RELEASE).invokeExact(recv, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndAddRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)((short)0x0123 + (short)0x4567), "getAndAddRelease short value");
        }

        // get and bitwise or
        {
            hs.get(TestAccessMode.SET).invokeExact(recv, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_OR).invokeExact(recv, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseOr short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)((short)0x0123 | (short)0x4567), "getAndBitwiseOr short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact(recv, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_OR_ACQUIRE).invokeExact(recv, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseOrAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)((short)0x0123 | (short)0x4567), "getAndBitwiseOrAcquire short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact(recv, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_OR_RELEASE).invokeExact(recv, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseOrRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)((short)0x0123 | (short)0x4567), "getAndBitwiseOrRelease short value");
        }

        // get and bitwise and
        {
            hs.get(TestAccessMode.SET).invokeExact(recv, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_AND).invokeExact(recv, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseAnd short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)((short)0x0123 & (short)0x4567), "getAndBitwiseAnd short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact(recv, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_AND_ACQUIRE).invokeExact(recv, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseAndAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)((short)0x0123 & (short)0x4567), "getAndBitwiseAndAcquire short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact(recv, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_AND_RELEASE).invokeExact(recv, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseAndRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)((short)0x0123 & (short)0x4567), "getAndBitwiseAndRelease short value");
        }

        // get and bitwise xor
        {
            hs.get(TestAccessMode.SET).invokeExact(recv, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_XOR).invokeExact(recv, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseXor short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)((short)0x0123 ^ (short)0x4567), "getAndBitwiseXor short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact(recv, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_XOR_ACQUIRE).invokeExact(recv, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseXorAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)((short)0x0123 ^ (short)0x4567), "getAndBitwiseXorAcquire short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact(recv, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_XOR_RELEASE).invokeExact(recv, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseXorRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(recv);
            assertEquals(x, (short)((short)0x0123 ^ (short)0x4567), "getAndBitwiseXorRelease short value");
        }
    }

    static void testInstanceFieldUnsupported(VarHandleTestMethodHandleAccessShort recv, Handles hs) throws Throwable {


    }


    static void testStaticField(Handles hs) throws Throwable {
        // Plain
        {
            hs.get(TestAccessMode.SET).invokeExact((short)0x0123);
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x0123, "set short value");
        }


        // Volatile
        {
            hs.get(TestAccessMode.SET_VOLATILE).invokeExact((short)0x4567);
            short x = (short) hs.get(TestAccessMode.GET_VOLATILE).invokeExact();
            assertEquals(x, (short)0x4567, "setVolatile short value");
        }

        // Lazy
        {
            hs.get(TestAccessMode.SET_RELEASE).invokeExact((short)0x0123);
            short x = (short) hs.get(TestAccessMode.GET_ACQUIRE).invokeExact();
            assertEquals(x, (short)0x0123, "setRelease short value");
        }

        // Opaque
        {
            hs.get(TestAccessMode.SET_OPAQUE).invokeExact((short)0x4567);
            short x = (short) hs.get(TestAccessMode.GET_OPAQUE).invokeExact();
            assertEquals(x, (short)0x4567, "setOpaque short value");
        }

        hs.get(TestAccessMode.SET).invokeExact((short)0x0123);

        // Compare
        {
            boolean r = (boolean) hs.get(TestAccessMode.COMPARE_AND_SET).invokeExact((short)0x0123, (short)0x4567);
            assertEquals(r, true, "success compareAndSet short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x4567, "success compareAndSet short value");
        }

        {
            boolean r = (boolean) hs.get(TestAccessMode.COMPARE_AND_SET).invokeExact((short)0x0123, (short)0x89AB);
            assertEquals(r, false, "failing compareAndSet short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x4567, "failing compareAndSet short value");
        }

        {
            short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE).invokeExact((short)0x4567, (short)0x0123);
            assertEquals(r, (short)0x4567, "success compareAndExchange short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x0123, "success compareAndExchange short value");
        }

        {
            short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE).invokeExact((short)0x4567, (short)0x89AB);
            assertEquals(r, (short)0x0123, "failing compareAndExchange short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x0123, "failing compareAndExchange short value");
        }

        {
            short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE_ACQUIRE).invokeExact((short)0x0123, (short)0x4567);
            assertEquals(r, (short)0x0123, "success compareAndExchangeAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x4567, "success compareAndExchangeAcquire short value");
        }

        {
            short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE_ACQUIRE).invokeExact((short)0x0123, (short)0x89AB);
            assertEquals(r, (short)0x4567, "failing compareAndExchangeAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x4567, "failing compareAndExchangeAcquire short value");
        }

        {
            short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE_RELEASE).invokeExact((short)0x4567, (short)0x0123);
            assertEquals(r, (short)0x4567, "success compareAndExchangeRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x0123, "success compareAndExchangeRelease short value");
        }

        {
            short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE_RELEASE).invokeExact((short)0x4567, (short)0x89AB);
            assertEquals(r, (short)0x0123, "failing compareAndExchangeRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x0123, "failing compareAndExchangeRelease short value");
        }

        {
            MethodHandle mh = hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_PLAIN);
            boolean success = false;
            for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                success = (boolean) mh.invokeExact((short)0x0123, (short)0x4567);
                if (!success) weakDelay();
            }
            assertEquals(success, true, "success weakCompareAndSetPlain short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x4567, "success weakCompareAndSetPlain short value");
        }

        {
            boolean success = (boolean) hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_PLAIN).invokeExact((short)0x0123, (short)0x89AB);
            assertEquals(success, false, "failing weakCompareAndSetPlain short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x4567, "failing weakCompareAndSetPlain short value");
        }

        {
            MethodHandle mh = hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_ACQUIRE);
            boolean success = false;
            for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                success = (boolean) mh.invokeExact((short)0x4567, (short)0x0123);
                if (!success) weakDelay();
            }
            assertEquals(success, true, "success weakCompareAndSetAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x0123, "success weakCompareAndSetAcquire short");
        }

        {
            MethodHandle mh = hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_ACQUIRE);
            boolean success = (boolean) mh.invokeExact((short)0x4567, (short)0x89AB);
            assertEquals(success, false, "failing weakCompareAndSetAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x0123, "failing weakCompareAndSetAcquire short value");
        }

        {
            MethodHandle mh = hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_RELEASE);
            boolean success = false;
            for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                success = (boolean) mh.invokeExact((short)0x0123, (short)0x4567);
                if (!success) weakDelay();
            }
            assertEquals(success, true, "success weakCompareAndSetRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x4567, "success weakCompareAndSetRelease short");
        }

        {
            boolean success = (boolean) hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_RELEASE).invokeExact((short)0x0123, (short)0x89AB);
            assertEquals(success, false, "failing weakCompareAndSetRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x4567, "failing weakCompareAndSetRelease short value");
        }

        {
            MethodHandle mh = hs.get(TestAccessMode.WEAK_COMPARE_AND_SET);
            boolean success = false;
            for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                success = (boolean) mh.invokeExact((short)0x4567, (short)0x0123);
                if (!success) weakDelay();
            }
            assertEquals(success, true, "success weakCompareAndSet short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x0123, "success weakCompareAndSet short");
        }

        {
            boolean success = (boolean) hs.get(TestAccessMode.WEAK_COMPARE_AND_SET).invokeExact((short)0x4567, (short)0x89AB);
            assertEquals(success, false, "failing weakCompareAndSet short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x0123, "failing weakCompareAndSetRe short value");
        }

        // Compare set and get
        {
            hs.get(TestAccessMode.SET).invokeExact((short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_SET).invokeExact((short)0x4567);
            assertEquals(o, (short)0x0123, "getAndSet short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x4567, "getAndSet short value");
        }

        // Compare set and get
        {
            hs.get(TestAccessMode.SET).invokeExact((short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_SET_ACQUIRE).invokeExact((short)0x4567);
            assertEquals(o, (short)0x0123, "getAndSetAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x4567, "getAndSetAcquire short value");
        }

        // Compare set and get
        {
            hs.get(TestAccessMode.SET).invokeExact((short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_SET_RELEASE).invokeExact((short)0x4567);
            assertEquals(o, (short)0x0123, "getAndSetRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)0x4567, "getAndSetRelease short value");
        }

        // get and add, add and get
        {
            hs.get(TestAccessMode.SET).invokeExact((short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_ADD).invokeExact((short)0x4567);
            assertEquals(o, (short)0x0123, "getAndAdd short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)((short)0x0123 + (short)0x4567), "getAndAdd short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact((short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_ADD_ACQUIRE).invokeExact((short)0x4567);
            assertEquals(o, (short)0x0123, "getAndAddAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)((short)0x0123 + (short)0x4567), "getAndAddAcquire short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact((short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_ADD_RELEASE).invokeExact((short)0x4567);
            assertEquals(o, (short)0x0123, "getAndAddRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)((short)0x0123 + (short)0x4567), "getAndAddRelease short value");
        }

        // get and bitwise or
        {
            hs.get(TestAccessMode.SET).invokeExact((short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_OR).invokeExact((short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseOr short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)((short)0x0123 | (short)0x4567), "getAndBitwiseOr short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact((short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_OR_ACQUIRE).invokeExact((short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseOrAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)((short)0x0123 | (short)0x4567), "getAndBitwiseOrAcquire short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact((short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_OR_RELEASE).invokeExact((short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseOrRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)((short)0x0123 | (short)0x4567), "getAndBitwiseOrRelease short value");
        }

        // get and bitwise and
        {
            hs.get(TestAccessMode.SET).invokeExact((short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_AND).invokeExact((short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseAnd short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)((short)0x0123 & (short)0x4567), "getAndBitwiseAnd short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact((short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_AND_ACQUIRE).invokeExact((short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseAndAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)((short)0x0123 & (short)0x4567), "getAndBitwiseAndAcquire short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact((short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_AND_RELEASE).invokeExact((short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseAndRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)((short)0x0123 & (short)0x4567), "getAndBitwiseAndRelease short value");
        }

        // get and bitwise xor
        {
            hs.get(TestAccessMode.SET).invokeExact((short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_XOR).invokeExact((short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseXor short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)((short)0x0123 ^ (short)0x4567), "getAndBitwiseXor short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact((short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_XOR_ACQUIRE).invokeExact((short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseXorAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)((short)0x0123 ^ (short)0x4567), "getAndBitwiseXorAcquire short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact((short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_XOR_RELEASE).invokeExact((short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseXorRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact();
            assertEquals(x, (short)((short)0x0123 ^ (short)0x4567), "getAndBitwiseXorRelease short value");
        }
    }

    static void testStaticFieldUnsupported(Handles hs) throws Throwable {


    }


    static void testArray(Handles hs) throws Throwable {
        short[] array = new short[10];

        for (int i = 0; i < array.length; i++) {
            // Plain
            {
                hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x0123, "get short value");
            }


            // Volatile
            {
                hs.get(TestAccessMode.SET_VOLATILE).invokeExact(array, i, (short)0x4567);
                short x = (short) hs.get(TestAccessMode.GET_VOLATILE).invokeExact(array, i);
                assertEquals(x, (short)0x4567, "setVolatile short value");
            }

            // Lazy
            {
                hs.get(TestAccessMode.SET_RELEASE).invokeExact(array, i, (short)0x0123);
                short x = (short) hs.get(TestAccessMode.GET_ACQUIRE).invokeExact(array, i);
                assertEquals(x, (short)0x0123, "setRelease short value");
            }

            // Opaque
            {
                hs.get(TestAccessMode.SET_OPAQUE).invokeExact(array, i, (short)0x4567);
                short x = (short) hs.get(TestAccessMode.GET_OPAQUE).invokeExact(array, i);
                assertEquals(x, (short)0x4567, "setOpaque short value");
            }

            hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);

            // Compare
            {
                boolean r = (boolean) hs.get(TestAccessMode.COMPARE_AND_SET).invokeExact(array, i, (short)0x0123, (short)0x4567);
                assertEquals(r, true, "success compareAndSet short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x4567, "success compareAndSet short value");
            }

            {
                boolean r = (boolean) hs.get(TestAccessMode.COMPARE_AND_SET).invokeExact(array, i, (short)0x0123, (short)0x89AB);
                assertEquals(r, false, "failing compareAndSet short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x4567, "failing compareAndSet short value");
            }

            {
                short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE).invokeExact(array, i, (short)0x4567, (short)0x0123);
                assertEquals(r, (short)0x4567, "success compareAndExchange short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x0123, "success compareAndExchange short value");
            }

            {
                short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE).invokeExact(array, i, (short)0x4567, (short)0x89AB);
                assertEquals(r, (short)0x0123, "failing compareAndExchange short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x0123, "failing compareAndExchange short value");
            }

            {
                short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE_ACQUIRE).invokeExact(array, i, (short)0x0123, (short)0x4567);
                assertEquals(r, (short)0x0123, "success compareAndExchangeAcquire short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x4567, "success compareAndExchangeAcquire short value");
            }

            {
                short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE_ACQUIRE).invokeExact(array, i, (short)0x0123, (short)0x89AB);
                assertEquals(r, (short)0x4567, "failing compareAndExchangeAcquire short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x4567, "failing compareAndExchangeAcquire short value");
            }

            {
                short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE_RELEASE).invokeExact(array, i, (short)0x4567, (short)0x0123);
                assertEquals(r, (short)0x4567, "success compareAndExchangeRelease short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x0123, "success compareAndExchangeRelease short value");
            }

            {
                short r = (short) hs.get(TestAccessMode.COMPARE_AND_EXCHANGE_RELEASE).invokeExact(array, i, (short)0x4567, (short)0x89AB);
                assertEquals(r, (short)0x0123, "failing compareAndExchangeRelease short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x0123, "failing compareAndExchangeRelease short value");
            }

            {
                MethodHandle mh = hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_PLAIN);
                boolean success = false;
                for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                    success = (boolean) mh.invokeExact(array, i, (short)0x0123, (short)0x4567);
                    if (!success) weakDelay();
                }
                assertEquals(success, true, "success weakCompareAndSetPlain short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x4567, "success weakCompareAndSetPlain short value");
            }

            {
                boolean success = (boolean) hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_PLAIN).invokeExact(array, i, (short)0x0123, (short)0x89AB);
                assertEquals(success, false, "failing weakCompareAndSetPlain short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x4567, "failing weakCompareAndSetPlain short value");
            }

            {
                MethodHandle mh = hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_ACQUIRE);
                boolean success = false;
                for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                    success = (boolean) mh.invokeExact(array, i, (short)0x4567, (short)0x0123);
                    if (!success) weakDelay();
                }
                assertEquals(success, true, "success weakCompareAndSetAcquire short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x0123, "success weakCompareAndSetAcquire short");
            }

            {
                boolean success = (boolean) hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_ACQUIRE).invokeExact(array, i, (short)0x4567, (short)0x89AB);
                assertEquals(success, false, "failing weakCompareAndSetAcquire short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x0123, "failing weakCompareAndSetAcquire short value");
            }

            {
                MethodHandle mh = hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_RELEASE);
                boolean success = false;
                for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                    success = (boolean) mh.invokeExact(array, i, (short)0x0123, (short)0x4567);
                    if (!success) weakDelay();
                }
                assertEquals(success, true, "success weakCompareAndSetRelease short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x4567, "success weakCompareAndSetRelease short");
            }

            {
                boolean success = (boolean) hs.get(TestAccessMode.WEAK_COMPARE_AND_SET_ACQUIRE).invokeExact(array, i, (short)0x0123, (short)0x89AB);
                assertEquals(success, false, "failing weakCompareAndSetAcquire short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x4567, "failing weakCompareAndSetAcquire short value");
            }

            {
                MethodHandle mh = hs.get(TestAccessMode.WEAK_COMPARE_AND_SET);
                boolean success = false;
                for (int c = 0; c < WEAK_ATTEMPTS && !success; c++) {
                    success = (boolean) mh.invokeExact(array, i, (short)0x4567, (short)0x0123);
                    if (!success) weakDelay();
                }
                assertEquals(success, true, "success weakCompareAndSet short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x0123, "success weakCompareAndSet short");
            }

            {
                boolean success = (boolean) hs.get(TestAccessMode.WEAK_COMPARE_AND_SET).invokeExact(array, i, (short)0x4567, (short)0x89AB);
                assertEquals(success, false, "failing weakCompareAndSet short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x0123, "failing weakCompareAndSet short value");
            }

            // Compare set and get
            {
                hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);

                short o = (short) hs.get(TestAccessMode.GET_AND_SET).invokeExact(array, i, (short)0x4567);
                assertEquals(o, (short)0x0123, "getAndSet short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x4567, "getAndSet short value");
            }

            {
                hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);

                short o = (short) hs.get(TestAccessMode.GET_AND_SET_ACQUIRE).invokeExact(array, i, (short)0x4567);
                assertEquals(o, (short)0x0123, "getAndSetAcquire short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x4567, "getAndSetAcquire short value");
            }

            {
                hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);

                short o = (short) hs.get(TestAccessMode.GET_AND_SET_RELEASE).invokeExact(array, i, (short)0x4567);
                assertEquals(o, (short)0x0123, "getAndSetRelease short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)0x4567, "getAndSetRelease short value");
            }

            // get and add, add and get
            {
                hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);

                short o = (short) hs.get(TestAccessMode.GET_AND_ADD).invokeExact(array, i, (short)0x4567);
                assertEquals(o, (short)0x0123, "getAndAdd short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)((short)0x0123 + (short)0x4567), "getAndAdd short value");
            }

            {
                hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);

                short o = (short) hs.get(TestAccessMode.GET_AND_ADD_ACQUIRE).invokeExact(array, i, (short)0x4567);
                assertEquals(o, (short)0x0123, "getAndAddAcquire short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)((short)0x0123 + (short)0x4567), "getAndAddAcquire short value");
            }

            {
                hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);

                short o = (short) hs.get(TestAccessMode.GET_AND_ADD_RELEASE).invokeExact(array, i, (short)0x4567);
                assertEquals(o, (short)0x0123, "getAndAddRelease short");
                short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
                assertEquals(x, (short)((short)0x0123 + (short)0x4567), "getAndAddRelease short value");
            }

        // get and bitwise or
        {
            hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_OR).invokeExact(array, i, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseOr short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
            assertEquals(x, (short)((short)0x0123 | (short)0x4567), "getAndBitwiseOr short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_OR_ACQUIRE).invokeExact(array, i, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseOrAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
            assertEquals(x, (short)((short)0x0123 | (short)0x4567), "getAndBitwiseOrAcquire short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_OR_RELEASE).invokeExact(array, i, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseOrRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
            assertEquals(x, (short)((short)0x0123 | (short)0x4567), "getAndBitwiseOrRelease short value");
        }

        // get and bitwise and
        {
            hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_AND).invokeExact(array, i, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseAnd short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
            assertEquals(x, (short)((short)0x0123 & (short)0x4567), "getAndBitwiseAnd short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_AND_ACQUIRE).invokeExact(array, i, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseAndAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
            assertEquals(x, (short)((short)0x0123 & (short)0x4567), "getAndBitwiseAndAcquire short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_AND_RELEASE).invokeExact(array, i, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseAndRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
            assertEquals(x, (short)((short)0x0123 & (short)0x4567), "getAndBitwiseAndRelease short value");
        }

        // get and bitwise xor
        {
            hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_XOR).invokeExact(array, i, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseXor short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
            assertEquals(x, (short)((short)0x0123 ^ (short)0x4567), "getAndBitwiseXor short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_XOR_ACQUIRE).invokeExact(array, i, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseXorAcquire short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
            assertEquals(x, (short)((short)0x0123 ^ (short)0x4567), "getAndBitwiseXorAcquire short value");
        }

        {
            hs.get(TestAccessMode.SET).invokeExact(array, i, (short)0x0123);

            short o = (short) hs.get(TestAccessMode.GET_AND_BITWISE_XOR_RELEASE).invokeExact(array, i, (short)0x4567);
            assertEquals(o, (short)0x0123, "getAndBitwiseXorRelease short");
            short x = (short) hs.get(TestAccessMode.GET).invokeExact(array, i);
            assertEquals(x, (short)((short)0x0123 ^ (short)0x4567), "getAndBitwiseXorRelease short value");
        }
        }
    }

    static void testArrayUnsupported(Handles hs) throws Throwable {
        short[] array = new short[10];

        final int i = 0;


    }

    static void testArrayIndexOutOfBounds(Handles hs) throws Throwable {
        short[] array = new short[10];

        for (int i : new int[]{-1, Integer.MIN_VALUE, 10, 11, Integer.MAX_VALUE}) {
            final int ci = i;

            for (TestAccessMode am : testAccessModesOfType(TestAccessType.GET)) {
                checkAIOOBE(am, () -> {
                    short x = (short) hs.get(am).invokeExact(array, ci);
                });
            }

            for (TestAccessMode am : testAccessModesOfType(TestAccessType.SET)) {
                checkAIOOBE(am, () -> {
                    hs.get(am).invokeExact(array, ci, (short)0x0123);
                });
            }

            for (TestAccessMode am : testAccessModesOfType(TestAccessType.COMPARE_AND_SET)) {
                checkAIOOBE(am, () -> {
                    boolean r = (boolean) hs.get(am).invokeExact(array, ci, (short)0x0123, (short)0x4567);
                });
            }

            for (TestAccessMode am : testAccessModesOfType(TestAccessType.COMPARE_AND_EXCHANGE)) {
                checkAIOOBE(am, () -> {
                    short r = (short) hs.get(am).invokeExact(array, ci, (short)0x4567, (short)0x0123);
                });
            }

            for (TestAccessMode am : testAccessModesOfType(TestAccessType.GET_AND_SET)) {
                checkAIOOBE(am, () -> {
                    short o = (short) hs.get(am).invokeExact(array, ci, (short)0x0123);
                });
            }

            for (TestAccessMode am : testAccessModesOfType(TestAccessType.GET_AND_ADD)) {
                checkAIOOBE(am, () -> {
                    short o = (short) hs.get(am).invokeExact(array, ci, (short)0x89AB);
                });
            }

            for (TestAccessMode am : testAccessModesOfType(TestAccessType.GET_AND_BITWISE)) {
                checkAIOOBE(am, () -> {
                    short o = (short) hs.get(am).invokeExact(array, ci, (short)0x89AB);
                });
            }
        }
    }
}

