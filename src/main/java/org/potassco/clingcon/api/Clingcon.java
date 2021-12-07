package org.potassco.clingcon.api;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import org.potassco.clingo.api.types.NativeSize;
import org.potassco.clingo.api.types.NativeSizeByReference;

public interface Clingcon extends Library {
    // A shared library is a singleton
    Clingcon INSTANCE = Native.load("clingcon", Clingcon.class);

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
    boolean clingcon_create(PointerByReference theory);

    /**
     * Register the theory with a control object.
     */
    boolean clingcon_register(Pointer theory, Pointer control);

    /**
     * Rewrite asts before adding them via the given callback.
     */
    boolean clingcon_rewrite_ast(Pointer theory, Pointer ast, AstCallback add, Pointer data);

    /**
     * Prepare the theory between grounding and solving.
     */
    boolean clingcon_prepare(Pointer theory, Pointer control);

    /**
     * Destroy the theory.
     * Currently no way to unregister a theory.
     */
    boolean clingcon_destroy(Pointer theory);

    /**
     * Configure theory manually (without using clingo's options facility).
     *
     * Note that the theory has to be configured before registering it and cannot
     * be reconfigured.
     */
    boolean clingcon_configure(Pointer theory, String key, String value);

    /**
     * Register options of the theory.
     */
    boolean clingcon_register_options(Pointer theory, Pointer options);

    /**
     * Validate options of the theory.
     */
    boolean clingcon_validate_options(Pointer theory);

    /**
     * Callback for models.
     */
    boolean clingcon_on_model(Pointer theory, Pointer model);

    /**
     * Obtain a symbol index which can be used to get the value of a symbol.
     *
     * Returns true if the symbol exists.
     * Does not throw.
     */
    boolean clingcon_lookup_symbol(Pointer theory, long symbol, NativeSizeByReference index);

    /**
     * Obtain the symbol at the given index.
     *
     * Does not throw.
     */
    long clingcon_get_symbol(Pointer theory, NativeSize index);

    /**
     * Initialize index so that it can be used with clingcon_assignment_next.
     *
     * Does not throw.
     */
    void clingcon_assignment_begin(Pointer theory, int thread_id, NativeSizeByReference index);

    /**
     * Move to the next index that has a value.
     *
     * Returns true if the updated index is valid.
     * Does not throw.
     */
    boolean clingcon_assignment_next(Pointer theory, int thread_id, NativeSizeByReference index);

    /**
     * Check if the symbol at the given index has a value.
     *
     * Does not throw.
     */
    boolean clingcon_assignment_has_value(Pointer theory, int thread_id, NativeSize index);

    /**
     * Get the symbol and it's value at the given index.
     *
     * Does not throw.
     */
    void clingcon_assignment_get_value(Pointer theory, int thread_id, NativeSize index, Value value);

    /**
     * Callback for statistic updates.
     *
     * Best add statistics under a subkey with the name of your theory.
     */
    boolean clingcon_on_statistics(Pointer theory, Pointer step, Pointer accu);


}
