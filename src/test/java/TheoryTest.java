import org.junit.Test;
import org.potassco.clingcon.api.Clingcon;
import org.potassco.clingcon.api.ClingconTheory;
import org.potassco.clingo.Clingo;
import org.potassco.clingo.control.Control;

public class TheoryTest {

    @Test
    public void testGetVersion() {
        System.out.println(Clingo.getVersion());
        System.out.println(Clingcon.getVersion());
    }

    @Test
    public void testCreateTheory() {
        ClingconTheory theory = new ClingconTheory();
        theory.destroy();
    }

    @Test
    public void testRegisterTheory() {
        Control control = new Control("{a}.", null);
        ClingconTheory theory = new ClingconTheory();
        theory.register(control);
        theory.destroy();
    }

    @Test
    public void testPrepare() {
        Control control = new Control("{a}.", null);
        ClingconTheory theory = new ClingconTheory();
        theory.register(control);
        theory.prepare();
        theory.destroy();
    }
}
