package ext.test4j.hamcrest.object;

import ext.test4j.hamcrest.Factory;
import ext.test4j.hamcrest.FeatureMatcher;
import ext.test4j.hamcrest.Matcher;

import static ext.test4j.hamcrest.core.IsEqual.equalTo;

public class HasToString<T> extends FeatureMatcher<T, String> {
    public HasToString(Matcher<? super String> toStringMatcher) {
      super(toStringMatcher, "with toString()", "toString()");
    }
    
    @Override
    protected String featureValueOf(T actual) {
      return actual.toString();
    }

    /**
     * Evaluates whether item.toString() satisfies a given matcher.
     */
    @Factory
    public static <T> HasToString<T> hasToString(Matcher<? super String> toStringMatcher) {
        return new HasToString<T>(toStringMatcher);
    }

    /**
     * This is a shortcut to the frequently used has_string(equalTo(x)).
     *
     * For example,  assertThat(hasToString(equal_to(x)))
     *          vs.  assertThat(hasToString(x))
     */
    @Factory
    public static <T> HasToString<T> hasToString(String expectedToString) {
        return new HasToString<T>(equalTo(expectedToString));
    }
}
