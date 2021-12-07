package org.potassco.clingcon.api.callback;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

// Callback to rewrite statements
public abstract class AstCallback  implements Callback {
    /**
     * @param ast the AST
     * @param data a user data pointer
     * @return whether the call was successful
     */
    public boolean callback(Pointer ast, Pointer data) {
        return call(ast, data);
    }

    public abstract boolean call(Pointer ast, Pointer data);

}
