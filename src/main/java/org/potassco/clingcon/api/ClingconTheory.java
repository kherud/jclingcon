package org.potassco.clingcon.api;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import org.potassco.clingo.Clingo;
import org.potassco.clingo.ErrorChecking;
import org.potassco.clingo.ast.Ast;
import org.potassco.clingo.ast.AstCallback;
import org.potassco.clingo.control.Control;
import org.potassco.clingo.internal.NativeSize;
import org.potassco.clingo.internal.NativeSizeByReference;
import org.potassco.clingo.solving.Model;
import org.potassco.clingo.solving.SolveEventCallback;
import org.potassco.clingo.symbol.Symbol;
import org.potassco.clingo.theory.Theory;

import java.util.function.Function;

public class ClingconTheory extends Theory implements ErrorChecking {

    private final Pointer theory;
    private Control control;

    public ClingconTheory() {
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

    public org.potassco.clingo.ast.AstCallback rewriteAst(Function<Ast, Void> add) {
        AstCallback addCallback = (Pointer ast, Pointer data) -> {
            add.apply(new Ast(ast));
            return true;
        };
        return (Pointer ast, Pointer data) -> {
            Clingo.INSTANCE.clingo_ast_acquire(ast);
            checkError(Clingcon.INSTANCE.clingcon_rewrite_ast(theory, ast, addCallback, control.getPointer()));
            return true;
        };
    }

    public SolveEventCallback onModel() {
        return new SolveEventCallback() {
            @Override
            public void onModel(Model model) {
                checkError(Clingcon.INSTANCE.clingcon_on_model(theory, model.getPointer()));
            }
        };
    }

    public Assignment getAssignment(Model model) {
        return new Assignment(theory, model.getThreadId());
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

    // public class RewriteAstCallback extends AstCallback {
    //     @Override
    //     public boolean call(Ast ast, Pointer data) {
    //         Clingcon.INSTANCE.clingcon_rewrite_ast(theory);
    //         return false;
    //     }
    // }

}
