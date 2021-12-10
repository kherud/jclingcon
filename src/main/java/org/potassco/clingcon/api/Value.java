package org.potassco.clingcon.api;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;

@Structure.FieldOrder({"type", "value"})
public class Value extends Structure {
    public ValueType type;
    public ValueUnion value;

    @Union.FieldOrder({"intNumber", "doubleNumber", "symbol"})
    private static class ValueUnion extends Union {
        public int intNumber;
        public double doubleNumber;
        public long symbol;
    }

    public Value() {

    }

    public Value(Pointer pointer) {
        super(pointer);
        read();
    }

    @Override
    public void read() {
        super.read();
        if (type == ValueType.INT)
            value.setType(int.class);
        else if (type == ValueType.DOUBLE)
            value.setType(double.class);
        else if (type == ValueType.SYMBOL)
            value.setType(long.class);
        else
            throw new IllegalStateException("unknown value type " + type);
    }

    static class ByValue extends Value implements Structure.ByValue {
        public ByValue() { super(); }
        public ByValue(Pointer pointer) { super(pointer); }
    }

    static class ByReference extends Value implements Structure.ByReference {
        public ByReference() { super(); }
        public ByReference(Pointer pointer) { super(pointer); }
    }

}
