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
import com.sun.jna.ptr.PointerByReference;
import org.potassco.clingo.ast.Ast;
import org.potassco.clingo.ast.AstCallback;
import org.potassco.clingo.ast.ProgramBuilder;
import org.potassco.clingo.control.Control;
import org.potassco.clingo.internal.NativeSize;
import org.potassco.clingo.internal.NativeSizeByReference;
import org.potassco.clingo.solving.Model;
import org.potassco.clingo.solving.SolveEventCallback;
import org.potassco.clingo.symbol.Symbol;
import org.potassco.clingo.theory.Theory;

public class ClingconTheory extends Theory {

    private final Pointer theory;
    private Control control;

    public ClingconTheory() {
        PointerByReference theory = new PointerByReference();
        Clingcon.check(Clingcon.INSTANCE.clingcon_create(theory));
        this.theory = theory.getValue();
    }

    public void configure(String key, String value) {
        if (this.control != null)
            throw new IllegalStateException("you have to configure the theory before registering it");
        Clingcon.check(Clingcon.INSTANCE.clingcon_configure(this.theory, key, value));
    }

    public void register(Control control) {
        this.control = control;
        Clingcon.check(Clingcon.INSTANCE.clingcon_register(this.theory, control.getPointer()));
    }

    public AstCallback rewriteAst(ProgramBuilder builder) {
        AstCallback addCallback = builder::add;
        return (Ast ast) ->
            Clingcon.check(Clingcon.INSTANCE.clingcon_rewrite_ast(theory, ast.getPointer(), addCallback, control.getPointer()));
    }

    public SolveEventCallback onModel() {
        return new SolveEventCallback() {
            @Override
            public void onModel(Model model) {
                Clingcon.check(Clingcon.INSTANCE.clingcon_on_model(theory, model.getPointer()));
            }
        };
    }

    public Assignment getAssignment(Model model) {
        return new Assignment(theory, model.getThreadId());
    }

    public void prepare() {
        if (this.control == null)
            throw new IllegalStateException("you have to register the theory first");
        Clingcon.check(Clingcon.INSTANCE.clingcon_prepare(this.theory, this.control.getPointer()));
    }

    public void destroy() {
        Clingcon.check(Clingcon.INSTANCE.clingcon_destroy(this.theory));
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
