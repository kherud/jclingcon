import org.junit.Test;
import org.potassco.clingcon.api.ClingconTheory;
import org.potassco.clingo.ast.Ast;
import org.potassco.clingo.ast.ProgramBuilder;
import org.potassco.clingo.control.Control;
import org.potassco.clingo.solving.Model;
import org.potassco.clingo.solving.SolveHandle;
import org.potassco.clingo.solving.SolveMode;

import java.util.Collections;

public class RunTest {

    @Test
    public void test() {
        String program = "{a}.";
        Control control = new Control();
        ClingconTheory theory = new ClingconTheory();
        theory.register(control);
        try (ProgramBuilder builder = new ProgramBuilder(control)) {
            Ast.parseString(program, theory.rewriteAst(builder));
        }
        control.ground();
        theory.prepare();
        try (SolveHandle handle = control.solve(Collections.emptyList(), theory.onModel(), SolveMode.YIELD)) {
            while (handle.hasNext()) {
                Model model = handle.next();
            }
        };
    }
}
