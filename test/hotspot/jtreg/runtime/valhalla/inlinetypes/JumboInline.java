/*
 * Copyright (c) 2018, 2020, Oracle and/or its affiliates. All rights reserved.
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
package runtime.valhalla.inlinetypes;

import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.ImplicitlyConstructible;

@ImplicitlyConstructible
@LooselyConsistentValue
public value class JumboInline {
    final long l0;
    final long l1;
    final long l2;
    final long l3;
    final long l4;
    final long l5;
    final long l6;
    final long l7;
    final long l8;
    final long l9;
    final long l10;
    final long l11;
    final long l12;
    final long l13;
    final long l14;
    final long l15;
    final long l16;
    final long l17;
    final long l18;
    final long l19;

    public JumboInline(long l0Val, long l1Val) {
        l0 = l0Val;
        l1 = l1Val;
        l2 = l0+l1;
        l3 = l1+l2;
        l4 = l2+l3;
        l5 = l3+l4;
        l6 = l4+l5;
        l7 = l5+l6;
        l8 = l6+l7;
        l9 = l7+l8;
        l10 = l8+l9;
        l11 = l9+l10;
        l12 = l10+l11;
        l13 = l11+l12;
        l14 = l12+l13;
        l15 = l13+l14;
        l16 = l14+l15;
        l17 = l15+l16;
        l18 = l16+l17;
        l19 = l17+l18;
    }

    public boolean verify() {
        return (l2 == (l0 + l1)  &&  l3 == (l1 + l2) && l5 == (l3 + l4)
            && l6 == (l4 + l5) && l7 == (l5 + l6) && l8 == (l6 + l7)
            && l9 == (l7 + l8) && l10 == (l8 + l9) && l11 == (l9 + l10)
            && l12 == (l10 + l11) && l13 == (l11 + l12) && l14 == (l12 + l13)
            && l15 == (l13 + l14) && l16 == (l14 + l15) && l17 == (l15 + l16)
            && l18 == (l16 + l17) && l19 == (l17 + l18));
    }

    public boolean equals(Object o) {
        if(o instanceof JumboInline) {
            JumboInline j = (JumboInline)o;
            return (l0 == j.l0 && l1 == j.l1 && l2 == j.l2 && l3 == j.l3
                    && l4 == j.l4 && l5 == j.l5 && l6 == j.l6 && l7 == j.l7
                    && l8 == j.l8 && l9 == j.l9 && l10 == j.l10 && l11 == j.l11
                    && l12 == j.l12 && l13 == j.l13 && l14 == j.l14 && l15 == j.l15
                    && l16 == j.l16 && l17 == j.l17 && l18 == j.l18 && l19 == j.l19);
        } else {
            return false;
        }
    }
}
