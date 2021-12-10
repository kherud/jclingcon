package org.potassco.clingcon.api;

import com.sun.jna.Pointer;
import org.potassco.clingo.ErrorChecking;
import org.potassco.clingo.internal.NativeSize;
import org.potassco.clingo.internal.NativeSizeByReference;
import org.potassco.clingo.symbol.Symbol;

import java.util.Iterator;

public class Assignment implements Iterable<Assignment.Tuple>, ErrorChecking {

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
                return Clingcon.INSTANCE.clingcon_assignment_next(theory, threadId, index);
            }

            @Override
            public Tuple next() {
                NativeSize index = new NativeSize(this.index.getValue());
                Value.ByReference valueByReference = new Value.ByReference();
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
    }
}
