package org.potassco.clingcon.api;

import org.potassco.clingo.api.ErrorChecking;

public class Assignment implements ErrorChecking {

    private final Theory theory;

    public Assignment(Theory theory) {
        this.theory = theory;
    }

}
