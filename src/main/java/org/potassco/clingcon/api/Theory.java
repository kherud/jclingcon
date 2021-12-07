package org.potassco.clingcon.api;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import org.potassco.clingo.api.ErrorChecking;
import org.potassco.clingo.api.types.NativeSize;
import org.potassco.clingo.api.types.NativeSizeByReference;
import org.potassco.clingo.control.Control;
import org.potassco.clingo.symbol.Function;
import org.potassco.clingo.symbol.Symbol;

public class Theory implements ErrorChecking {

    private final Pointer theory;
    private Control control;

    public Theory() {
        PointerByReference theory = new PointerByReference();
        checkError(Clingcon.INSTANCE.clingcon_create(theory));
        this.theory = theory.getValue();
    }

    public void configure(String key, String value) {
        if (this.control != null)
            throw new IllegalStateException("you have to configure the theory before registering it");
        checkError(Clingcon.INSTANCE.clingcon_configure(this.theory, key, value));
    }

    public void register(Control control) {
        this.control = control;
        checkError(Clingcon.INSTANCE.clingcon_register(this.theory, control.getPointer()));
    }

    public void prepare() {
        if (this.control == null)
            throw new IllegalStateException("you have to register the theory first");
        checkError(Clingcon.INSTANCE.clingcon_prepare(this.theory, this.control.getPointer()));
    }

    public void destroy() {
        checkError(Clingcon.INSTANCE.clingcon_destroy(this.theory));
    }

    public Symbol getSymbol(Symbol symbol) {
        NativeSizeByReference nativeSizeByRef = new NativeSizeByReference();
        Clingcon.INSTANCE.clingcon_lookup_symbol(theory, symbol.getLong(), nativeSizeByRef);
        NativeSize index = new NativeSize(nativeSizeByRef.getValue());
        long symbolId = Clingcon.INSTANCE.clingcon_get_symbol(theory, index);
        return Symbol.fromLong(symbolId);
    }

    public Pointer getPointer() {
        return theory;
    }

}
