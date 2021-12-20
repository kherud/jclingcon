import org.junit.Test;
import org.potassco.clingcon.Clingcon;
import org.potassco.clingcon.ClingconTheory;
import org.potassco.clingo.internal.Clingo;
import org.potassco.clingo.control.Control;

public class TheoryTest {

    @Test
    public void testGetVersion() {
        System.out.println("Clingo Version: " + Clingo.getVersion());
        System.out.println("ClingCon Version: " + Clingcon.getVersion());
    }

    @Test
    public void testCreateTheory() {
        ClingconTheory theory = new ClingconTheory();
        theory.destroy();
    }

    @Test
    public void testRegisterTheory() {
        Control control = new Control();
        control.add("{a}.");
        ClingconTheory theory = new ClingconTheory();
        theory.register(control);
        theory.destroy();
    }

    @Test
    public void testPrepare() {
        Control control = new Control();
        control.add("{a}.");
        ClingconTheory theory = new ClingconTheory();
        theory.register(control);
        theory.prepare();
        theory.destroy();
    }
}
