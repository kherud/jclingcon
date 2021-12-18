/*
 * Copyright (C) 2021 denkbares GmbH, Germany
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */

package org.potassco.clingcon.api;

import com.sun.jna.Pointer;
import org.potassco.clingo.internal.NativeSize;
import org.potassco.clingo.internal.NativeSizeByReference;
import org.potassco.clingo.symbol.Symbol;

import java.util.Iterator;

public class Assignment implements Iterable<Assignment.Tuple> {

    private final Pointer theory;
    private final int threadId;

    public Assignment(Pointer theory, int threadId) {
        this.theory = theory;
        this.threadId = threadId;
    }


    @Override
    public Iterator<Tuple> iterator() {
        NativeSizeByReference nativeSizeByReference = new NativeSizeByReference();
        Clingcon.INSTANCE.clingcon_assignment_begin(theory, threadId, nativeSizeByReference);

        return new Iterator<>() {
            private final NativeSizeByReference index = nativeSizeByReference;

            @Override
            public boolean hasNext() {
                return Clingcon.INSTANCE.clingcon_assignment_next(theory, threadId, index) > 0;
            }

            @Override
            public Tuple next() {
                NativeSize index = new NativeSize(this.index.getValue());
                Value valueByReference = new Value();
                long symbolLong = Clingcon.INSTANCE.clingcon_get_symbol(theory, index);
                Symbol symbol = Symbol.fromLong(symbolLong);

                Clingcon.INSTANCE.clingcon_assignment_get_value(theory, threadId, index, valueByReference);

                return new Tuple(symbol, valueByReference);
            }
        };
    }

    public static class Tuple {
        private final Symbol symbol;
        private final Value value;

        private Tuple(Symbol symbol, Value value) {
            this.symbol = symbol;
            this.value = value;
        }

        public Symbol getSymbol() {
            return symbol;
        }

        public Value getValue() {
            return value;
        }

        @Override
        public String toString() {
            return symbol.toString() + "=" + value.toString();
        }
    }
}
