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

package org.potassco.clingcon;

import com.sun.jna.Pointer;
import org.potassco.clingo.internal.NativeSize;
import org.potassco.clingo.internal.NativeSizeByReference;
import org.potassco.clingo.symbol.Symbol;
import org.potassco.clingo.theory.Value;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

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
            private boolean continueIteration = true;
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                if (continueIteration) {
                    hasNext = Clingcon.INSTANCE.clingcon_assignment_next(theory, threadId, index) > 0;
                    continueIteration = false;
                }
                return hasNext;
            }

            @Override
            public Tuple next() {
                if (hasNext()) {
                    continueIteration = true;
                    NativeSize index = new NativeSize(this.index.getValue());
                    Value valueByReference = new Value();
                    long symbolLong = Clingcon.INSTANCE.clingcon_get_symbol(theory, index);
                    Symbol symbol = Symbol.fromLong(symbolLong);

                    Clingcon.INSTANCE.clingcon_assignment_get_value(theory, threadId, index, valueByReference);

                    return new Tuple(symbol, valueByReference);
                }
                throw new NoSuchElementException();
            }
        };
    }

    public static final class Tuple {
        private final Symbol symbol;
        private final Value value;

        public Tuple(Symbol symbol, Value value) {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple tuple = (Tuple) o;
            return symbol.equals(tuple.symbol) && value.equals(tuple.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(symbol, value);
        }
    }
}
