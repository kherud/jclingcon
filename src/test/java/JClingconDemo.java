import org.junit.Test;
import org.potassco.clingcon.api.Assignment;
import org.potassco.clingcon.api.ClingconTheory;
import org.potassco.clingo.ast.Ast;
import org.potassco.clingo.ast.ProgramBuilder;
import org.potassco.clingo.control.Control;
import org.potassco.clingo.control.LoggerCallback;
import org.potassco.clingo.solving.Model;
import org.potassco.clingo.solving.SolveHandle;
import org.potassco.clingo.solving.SolveMode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class JClingconDemo {

    @Test
    public void fsDTest() throws IOException {
        String program = getProgram("fsD.lp");
        String instances = getProgram("fsI.lp");
        List<Set<Assignment.Tuple>> assignments = run(instances + program);
    }

    private String getProgram(String fileName) throws IOException {
        URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
        Path file = Paths.get(url.getPath());
        return Files.readString(file);
    }


    private List<Set<Assignment.Tuple>> run(String program) {
        LoggerCallback logger = (code, message) -> System.out.printf("[%s] %s", code.name(), message);

        ClingconTheory theory = new ClingconTheory();
        Control control = new Control(logger, 10000, "0");
        theory.register(control);
        try (ProgramBuilder builder = new ProgramBuilder(control)) {
            Ast.parseString(program, theory.rewriteAst(builder), logger, 10000);
        }
        control.ground();
        theory.prepare();

        List<Set<Assignment.Tuple>> assignments = new ArrayList<>();
        try (SolveHandle handle = control.solve(Collections.emptyList(), theory.onModel(), SolveMode.YIELD)) {
            while (handle.hasNext()) {
                Model model = handle.next();
                System.out.println("Model:" + model);
                Set<Assignment.Tuple> modelAssignments = new HashSet<>();
                for (Assignment.Tuple tuple : theory.getAssignment(model)) {
                    System.out.println("Assignment: " + tuple);
                    modelAssignments.add(tuple);
                }
                assignments.add(modelAssignments);
            }
        }
        return assignments;
    }
}
