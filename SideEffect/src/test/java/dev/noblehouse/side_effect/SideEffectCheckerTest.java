package dev.noblehouse.side_effect;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.errorprone.CompilationTestHelper;

@RunWith(JUnit4.class)
public class SideEffectCheckerTest {

    private final CompilationTestHelper helper
            = CompilationTestHelper.newInstance(SideEffectChecker.class, getClass());

    @Test
    public void containsImpureFunctionButIsAnnotated() {
        helper
                .addSourceLines(
                        "TestClass.java",
                        "import java.lang.Math;",
                        "import dev.noblehouse.side_effect.annotations.SideEffect;",
                        "public class TestClass {",
                        "  double x = Math.random();",
                        "  @SideEffect public int add(int x, int y) {double w = Math.random(); return x + y;}",
                        "}")
                .doTest();
    }

    @Test
    public void containsImpureFunction() {
        helper
                .addSourceLines(
                        "TestClass.java",
                        "import java.lang.Math;",
                        "public class TestClass {",
                        "  double x = Math.random();",
                        "  // BUG: Diagnostic contains: random numbers make functions un-pure",
                        "  public int add(int x, int y) {double w = Math.random(); return x + y;}",
                        "}")
                .doTest();
    }
}
