import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class RunTest extends JClingConTest {

    private final String FSB = "#const bound=16.";
    private final String FSO = "&minimize { bound }.";

    @Test
    public void fsDTest() throws IOException {
        String program = getProgram("fsD.lp");
        String instances = getProgram("fsI.lp");
        Set<Result> assignments = solve(instances + program);
    }

    @Test
    public void test() throws IOException {
        String FSI = getProgram("fsI.lp");
        String FSE = getProgram("fsE.lp");
        String FSD = getProgram("fsD.lp");

        List<String> expected = List.of(
                "permutation(a,c) permutation(b,a) (a,1)=1 (a,2)=7 (b,1)=0 (b,2)=1 (c,1)=4 (c,2)=11",
                "permutation(a,c) permutation(b,a) (a,1)=1 (a,2)=7 (b,1)=0 (b,2)=1 (c,1)=5 (c,2)=11",
                "permutation(a,c) permutation(b,a) (a,1)=1 (a,2)=7 (b,1)=0 (b,2)=1 (c,1)=6 (c,2)=11",
                "permutation(a,c) permutation(b,a) (a,1)=2 (a,2)=7 (b,1)=0 (b,2)=1 (c,1)=5 (c,2)=11",
                "permutation(a,c) permutation(b,a) (a,1)=2 (a,2)=7 (b,1)=0 (b,2)=1 (c,1)=6 (c,2)=11",
                "permutation(a,c) permutation(b,a) (a,1)=3 (a,2)=7 (b,1)=0 (b,2)=1 (c,1)=6 (c,2)=11"
//                "permutation(b,c) permutation(c,a) (a,1)=6 (a,2)=12 (b,1)=0 (b,2)=1 (c,1)=1 (c,2)=7",
//                "permutation(b,c) permutation(c,a) (a,1)=7 (a,2)=12 (b,1)=0 (b,2)=1 (c,1)=1 (c,2)=7",
//                "permutation(b,c) permutation(c,a) (a,1)=7 (a,2)=12 (b,1)=0 (b,2)=1 (c,1)=2 (c,2)=7",
//                "permutation(b,c) permutation(c,a) (a,1)=8 (a,2)=12 (b,1)=0 (b,2)=1 (c,1)=1 (c,2)=7",
//                "permutation(b,c) permutation(c,a) (a,1)=8 (a,2)=12 (b,1)=0 (b,2)=1 (c,1)=2 (c,2)=7",
//                "permutation(b,c) permutation(c,a) (a,1)=9 (a,2)=12 (b,1)=0 (b,2)=1 (c,1)=1 (c,2)=7",
//                "permutation(b,c) permutation(c,a) (a,1)=9 (a,2)=12 (b,1)=0 (b,2)=1 (c,1)=2 (c,2)=7"
        );
        Set<Result> solutions = solve(FSB + FSE + FSI);
    }

}
