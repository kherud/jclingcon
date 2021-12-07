import org.junit.Test;
import org.potassco.clingcon.api.Clingcon;
import org.potassco.clingcon.api.Theory;
import org.potassco.clingo.api.Clingo;
import org.potassco.clingo.control.Control;

public class TheoryTest {

    @Test
    public void testGetVersion() {
        System.out.println(Clingo.getVersion());
        System.out.println(Clingcon.getVersion());
    }

    @Test
    public void testCreateTheory() {
        Theory theory = new Theory();
        theory.destroy();
    }

    @Test
    public void testRegisterTheory() {
        Control control = new Control("{a}.", null);
        Theory theory = new Theory();
        theory.register(control);
        theory.destroy();
    }

    @Test
    public void testPrepare() {
        Control control = new Control("{a}.", null);
        Theory theory = new Theory();
        theory.register(control);
        theory.prepare();
        theory.destroy();
    }
}
