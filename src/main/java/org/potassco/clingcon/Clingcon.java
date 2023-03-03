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

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import org.potassco.clingo.ast.AstCallback;
import org.potassco.clingo.internal.NativeSize;
import org.potassco.clingo.internal.NativeSizeByReference;

import java.util.Collections;

public interface Clingcon extends Library {
    Clingcon INSTANCE = initLibrary();

    static Clingcon initLibrary() {
        DefaultTypeMapper mapper = new DefaultTypeMapper();
        TypeConverter converter = new TypeConverter() {
            @Override
            public Object toNative(Object value, ToNativeContext ctx) {
                if (value == null) {
                    return ValueType.SYMBOL.getValue();
                } else {
                    return ((ValueType)value).getValue();
                }
            }
            @Override
            public Object fromNative(Object value, FromNativeContext context) {
                return ValueType.fromValue(((Integer)value));
            }
            @Override
            public Class<?> nativeType() {
                return Integer.class;
            }
        };
        mapper.addTypeConverter(ValueType.class, converter);
        return Native.load("clingcon", Clingcon.class, Collections.singletonMap(Library.OPTION_TYPE_MAPPER, mapper));
    }

    static void check(byte success) {
        if (success == 0)
            throw new IllegalStateException("there was an internal error");
    }

    static String getVersion() {
        IntByReference major = new IntByReference();
        IntByReference minor = new IntByReference();
        IntByReference revision = new IntByReference();
        INSTANCE.clingcon_version(major, minor, revision);
        return String.format("%d.%d.%d", major.getValue(), minor.getValue(), revision.getValue());
    }

    /**
     * Return the version of the theory.
     */
    void clingcon_version(IntByReference major, IntByReference minor, IntByReference patch);

    /**
     * Creates the theory.
     */
    byte clingcon_create(PointerByReference theory);

    /**
     * Register the theory with a control object.
     */
    byte clingcon_register(Pointer theory, Pointer control);

    /**
     * Rewrite asts before adding them via the given callback.
     */
    byte clingcon_rewrite_ast(Pointer theory, Pointer ast, AstCallback add, Pointer data);

    /**
     * Prepare the theory between grounding and solving.
     */
    byte clingcon_prepare(Pointer theory, Pointer control);

    /**
     * Destroy the theory.
     * Currently, no way to unregister a theory.
     */
    byte clingcon_destroy(Pointer theory);

    /**
     * Configure theory manually (without using clingo's options facility).
     * <p>
     * Note that the theory has to be configured before registering it and cannot
     * be reconfigured.
     */
    byte clingcon_configure(Pointer theory, String key, String value);

    /**
     * Register options of the theory.
     */
    byte clingcon_register_options(Pointer theory, Pointer options);

    /**
     * Validate options of the theory.
     */
    byte clingcon_validate_options(Pointer theory);

    /**
     * Callback for models.
     */
    byte clingcon_on_model(Pointer theory, Pointer model);

    /**
     * Obtain a symbol index which can be used to get the value of a symbol.
     * <p>
     * Returns true if the symbol exists.
     * Does not throw.
     */
    byte clingcon_lookup_symbol(Pointer theory, long symbol, NativeSizeByReference index);

    /**
     * Obtain the symbol at the given index.
     * <p>
     * Does not throw.
     */
    long clingcon_get_symbol(Pointer theory, NativeSize index);

    /**
     * Initialize index so that it can be used with clingcon_assignment_next.
     * <p>
     * Does not throw.
     */
    void clingcon_assignment_begin(Pointer theory, int thread_id, NativeSizeByReference index);

    /**
     * Move to the next index that has a value.
     * <p>
     * Returns true if the updated index is valid.
     * Does not throw.
     */
    byte clingcon_assignment_next(Pointer theory, int thread_id, NativeSizeByReference index);

    /**
     * Check if the symbol at the given index has a value.
     * <p>
     * Does not throw.
     */
    byte clingcon_assignment_has_value(Pointer theory, int thread_id, NativeSize index);

    /**
     * Get the symbol and it's value at the given index.
     * <p>
     * Does not throw.
     */
    void clingcon_assignment_get_value(Pointer theory, int thread_id, NativeSize index, Value value);

    /**
     * Callback for statistic updates.
     * <p>
     * Best add statistics under a subkey with the name of your theory.
     */
    byte clingcon_on_statistics(Pointer theory, Pointer step, Pointer accu);


}
