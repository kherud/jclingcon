import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CspDistinctSimpleTest extends JClingConTest {

	private static class TestCase {
		private final String program;
		private final String expected;
		private final int min;
		private final int max;

		TestCase(String program, String expected, int min, int max) {
			this.program = program;
			this.expected = expected;
			this.min = min;
			this.max = max;
		}
	}

	@Parameterized.Parameters(name = "{0}")
	public static Collection<TestCase> programs() {
		return List.of(
				new TestCase("&distinct { 1; 3; x }.", "x=2", 0, 2),
				new TestCase("&distinct { x; y }.", "x=0 y=1 ; x=1 y=0", 0, 2),
				new TestCase("&distinct { 2*x; 3*y }.", "x=2 y=2 ; x=2 y=3 ; x=3 y=3", 0, 2),
				new TestCase("&distinct { 0*x; 0*y }.", null, 0, 2),
				new TestCase("&distinct { x; -x }.", "x=1", 0, 2),
				new TestCase("&distinct { x; x+1 }.", "x=0 ; x=1", 0, 2),
				new TestCase("&distinct { 0 }.", "", 0, 2),
				new TestCase("&distinct { 0; 0 }.", null, 0, 2),
				new TestCase("&distinct { 0; 0+0 }.", null, 0, 2),
				new TestCase("&distinct { 0; 1 }.", "", 0, 2),
				new TestCase("&distinct { 2*x; (1+1)*x }.", null, 0, 2),
				new TestCase("&distinct { y-x; x-y }.", "x=0 y=1 ; x=1 y=0", 0, 2),
				new TestCase("&distinct { x; y } :- c. &sum { x } = y :- not c. {c}.", "c x=0 y=1 ; c x=1 y=0 ; x=0 y=0; x=1 y=1", 0, 2)
		);
	}

	private final TestCase testCase;

	public CspDistinctSimpleTest(TestCase testCase) {
		this.testCase = testCase;
	}

	@Test
	public void testDisjoint() {
		Set<Result> results = solve(testCase.program, testCase.min, testCase.max, "20");
		assertEquals(testCase.expected, results);
	}
}
