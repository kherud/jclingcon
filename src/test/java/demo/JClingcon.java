package demo;

import com.sun.jna.Pointer;
import org.junit.Test;
import org.potassco.clingcon.api.Assignment;
import org.potassco.clingcon.api.ClingconTheory;
import org.potassco.clingo.ast.Ast;
import org.potassco.clingo.ast.ProgramBuilder;
import org.potassco.clingo.control.Control;
import org.potassco.clingo.control.LoggerCallback;
import org.potassco.clingo.control.WarningCode;
import org.potassco.clingo.solving.Model;
import org.potassco.clingo.solving.SolveHandle;
import org.potassco.clingo.solving.SolveMode;

import java.util.Collections;

public class JClingcon {

    private final String program = "&sum { x } >= 1. &sum { x } <= 3.";

    @Test
    public void run() {
        LoggerCallback logger = new LoggerCallback() {
            @Override
            public void call(WarningCode code, String message) {
                System.out.printf("[%s] %s", code.name(), message);
            }
        };
        Control control = new Control(logger, 10000);
        ClingconTheory theory = new ClingconTheory();
        theory.register(control);
        try (ProgramBuilder builder = new ProgramBuilder(control)) {
            Ast.parseString(program, theory.rewriteAst(builder::add), logger, 10000);
        }
        control.ground();
        theory.prepare();
        try (SolveHandle handle = control.solve(Collections.emptyList(), theory.onModel(), SolveMode.YIELD)) {
            while (handle.hasNext()) {
                Model model = handle.next();
                System.out.println("Model:" + model);
                for (Assignment.Tuple tuple : theory.getAssignment(model)) {
                    System.out.println("Assignment:" + tuple.getSymbol() + ": " + tuple.getValue());
                }
            }
        };
    }
}
