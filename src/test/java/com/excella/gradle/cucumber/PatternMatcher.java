package com.excella.gradle.cucumber;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.regex.Pattern;

/**
 * Tests if the argument is a {@link CharSequence} that matches a regular expression.
 */
public class PatternMatcher extends TypeSafeMatcher<CharSequence> {

  /**
   * Creates a matcher that matches if the examined {@link CharSequence} matches the specified
   * regular expression.
   * <p/>
   * For example:
   * <pre>assertThat("myStringOfNote", pattern("[0-9]+"))</pre>
   *
   * @param regex the regular expression that the returned matcher will use to match any examined {@link CharSequence}
   */
  @Factory
  public static Matcher<CharSequence> matchesRegex(String regex) {
    return matchesRegex(Pattern.compile(regex));
  }

  /**
   * Creates a matcher that matches if the examined {@link CharSequence} matches the specified
   * regular expression.
   * <p/>
   * For example:
   * <pre>assertThat("myStringOfNote", pattern("[0-9]+"))</pre>
   *
   * @param regex the regular expression that the returned matcher will use to match any examined {@link CharSequence}
   */
  @Factory
  public static Matcher<CharSequence> matchesRegex(String regex, int flags) {
    return matchesRegex(Pattern.compile(regex, flags));
  }

  @Factory
  public static Matcher<CharSequence> containsRegex(String regex) {
    return new PatternMatcher(Pattern.compile(regex), true);
  }

  @Factory
  public static Matcher<CharSequence> containsRegex(String regex, int flags) {
    return new PatternMatcher(Pattern.compile(regex, flags), true);
  }

  /**
   * Creates a matcher that matches if the examined {@link CharSequence} matches the specified
   * {@link Pattern}.
   * <p/>
   * For example:
   * <pre>assertThat("myStringOfNote", Pattern.compile("[0-9]+"))</pre>
   *
   * @param pattern the pattern that the returned matcher will use to match any examined {@link CharSequence}
   */
  @Factory
  public static Matcher<CharSequence> matchesRegex(Pattern pattern) {
    return new PatternMatcher(pattern, false);
  }

  private final Pattern pattern;
  private final boolean finds;

  public PatternMatcher(Pattern pattern, boolean finds) {
    this.pattern = pattern;
    this.finds = finds;
  }

  @Override
  public boolean matchesSafely(CharSequence item) {
    if (finds) {
      return pattern.matcher(item).find();
    } else {
      return pattern.matcher(item).matches();
    }
  }

  @Override
  public void describeMismatchSafely(CharSequence item, org.hamcrest.Description mismatchDescription) {
    mismatchDescription.appendText("was \"").appendText(String.valueOf(item)).appendText("\"");
  }

  @Override
  public void describeTo(org.hamcrest.Description description) {
    description.appendText("a string with pattern \"").appendText(String.valueOf(pattern)).appendText("\"");
  }
}


