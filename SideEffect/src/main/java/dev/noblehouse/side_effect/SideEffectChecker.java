package dev.noblehouse.side_effect;

import java.util.ArrayList;
import java.util.List;

import com.google.errorprone.BugPattern;
import static com.google.errorprone.BugPattern.SeverityLevel.WARNING;
import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.bugpatterns.BugChecker.MethodTreeMatcher;
import com.google.errorprone.matchers.Description;
import com.google.errorprone.matchers.Matcher;
import static com.google.errorprone.matchers.Matchers.staticMethod;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.VariableTree;

@BugPattern(
        summary = "TODO",
        explanation
        = "TODO",
        severity = WARNING)
public final class SideEffectChecker extends BugChecker
        implements MethodTreeMatcher {

    private static final Matcher<ExpressionTree> MATH_RANDOM
            = staticMethod().onClass("java.lang.Math").named("random");

    @Override
    public Description matchMethod(MethodTree tree, VisitorState state) {

        boolean isMethodMarkedAsSideEffect = false;
        for (AnnotationTree rrr : tree.getModifiers().getAnnotations()) {
            var t = rrr.getAnnotationType();
            if (t instanceof IdentifierTree vt) {
                isMethodMarkedAsSideEffect = "SideEffect".equals(vt.getName().toString());
            }
        }

        if (isMethodMarkedAsSideEffect) {
            return Description.NO_MATCH;
        }

        List<String> errors = new ArrayList<>();

        for (StatementTree stmt : tree.getBody().getStatements()) {
            if (stmt instanceof VariableTree vt) {
                ExpressionTree aaa = vt.getInitializer();

                if (aaa instanceof MethodInvocationTree bbb) {
                    if (MATH_RANDOM.matches(bbb, state)) {
                        errors.add("random numbers make functions un-pure");
                    }
                }
            }
        }

        return errors.isEmpty()
                ? Description.NO_MATCH
                : buildDescription(tree)
                        .setMessage("random numbers make functions un-pure")
                        .build();
    }
}
