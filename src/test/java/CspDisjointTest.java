import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CspDisjointTest extends JClingConTest {

	@Parameterized.Parameters(name = "{0}")
	public static Collection<String[]> programs() {
		return List.of(
				new String[] { "&dom{1..2}=x. &dom{1..1}=y. {a}. &disjoint{x@1;y@1} :- a.", "a - x=2 y=1 ; x=1 y=1 ; x=2 y=1" },
				new String[] { "&dom{1..2}=x. &dom{1..1}=y. &dom{1..1}=z. {a}. &disjoint{x@1;y@1;z@1} :- a.", "x=1 y=1 z=1 ; x=2 y=1" },
				new String[] { "&dom{1..2}=x. &dom{1..1}=y. &dom{3..3}=z. {a}. &disjoint{x@1;y@1;z@1} :- a.", "a - x=2 y=1 z=3 ; x=1 y=1 z=3 ; x=2 y=1 z=3" },
				new String[] { "&dom{1..4}=x. &dom{1..4}=y. &disjoint{x@3;y@3}.", "x=1 y=4 ; x=4 y=1" },
				new String[] { "&dom{1..5}=x. &dom{1..5}=y. &dom{1..5}=z. &disjoint{x@2; y@2; z@2}.", "x=1 y=3 z=5 ; x=1 y=5 z=3 ; x=3 y=1 z=5 ; x=3 y=5 z=1 ; x=5 y=1 z=3 ; x=5 y=3 z=1" },
				new String[] { "#const n = 6. #show. &show{ q/1 }. p(1..n). &dom{ 1..n } = q(N) :- p(N). &sum{ r(N) } = q(N)-N :- p(N). &sum{ s(N) } = q(N)+N :- p(N). &disjoint{ q(N)@1 : p(N) }. &disjoint{ r(N)@1 : p(N) }. &disjoint{ s(N)@1 : p(N) }.", "q(1)=2 q(2)=4 q(3)=6 q(4)=1 q(5)=3 q(6)=5 ; q(1)=3 q(2)=6 q(3)=2 q(4)=5 q(5)=1 q(6)=4 ; q(1)=4 q(2)=1 q(3)=5 q(4)=2 q(5)=6 q(6)=3 ; q(1)=5 q(2)=3 q(3)=1 q(4)=6 q(5)=4 q(6)=2" }
		);
	}

	private final String program;
	private final String expected;

	public CspDisjointTest(String program, String expected) {
		this.program = program;
		System.out.println(program);
		this.expected = expected;
	}

	@Test
	public void testDisjoint() {
		Set<Result> results = solve(program);
		assertEquals(expected, results);
	}
}
