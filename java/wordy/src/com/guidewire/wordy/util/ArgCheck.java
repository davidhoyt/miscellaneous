package com.guidewire.wordy.util;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 * This is a simple refactoring of a chunks of common code called all
 * the time to ensure a parameter to a method meets some precondition.
 * <p/>
 * A reminder to be cautious with the range checks on floating point
 * values.
 */

public class ArgCheck {

  /**
   * Checks to make sure its parameter is null
   *
   * @param arg     The argument to check
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the param is not null
   */
  public static void isNull(Object arg, String argName) {
    if (arg != null) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must be null");
    }
  }

  /**
   * Checks to make sure its parameter is null
   *
   * @param arg     The argument to check
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @param message Additional description to include in the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the param is not null
   */
  public static void isNull(Object arg, String argName, String message) {
    if (arg != null) {
      throw new IllegalArgumentException("Argument \"" + argName + "\": " + message);
    }
  }

  /**
   * Checks to make sure its parameter is not null
   *
   * @param arg     The argument to check
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the param is null
   */
  public static void nonNull(Object arg, String argName) {
    if (arg == null) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" cannot be null");
    }
  }

  /**
   * Checks to make sure its parameter is not null
   *
   * @param arg     The argument to check
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @param message Additional description to include in the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the param is null
   */
  public static void nonNull(Object arg, String argName, String message) {
    if (arg == null) {
      throw new IllegalArgumentException("Argument \"" + argName + "\": " + message);
    }
  }

  /**
   * Checks to see if an argument is less than the specified value
   *
   * @param arg     The argument to check
   * @param max     The value arg must be less than
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void lessThan(int arg, int max, String argName) {
    if (arg >= max) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must be less than " + max);
    }
  }

  /**
   * Checks to see if an argument is greater than the specified value
   *
   * @param arg     The argument to check
   * @param min     The value arg must be greater than
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void greaterThan(int arg, int min, String argName) {
    if (arg <= min) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must be greater than " + min);
    }
  }

  /**
   * Checks to see if an argument is greater than or equal to the specified value
   *
   * @param arg     The argument to check
   * @param min     The value arg must be greater than or equal to
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void greaterThanOrEqual(int arg, int min, String argName) {
    if (arg < min) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must be greater than or equal to " + min);
    }
  }

  /**
   * Checks to see if an argument is between than the specified value
   *
   * @param arg     The argument to check
   * @param min     The value arg must be greater than
   * @param max     The value arg must be less than
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void between(int arg, int min, int max, String argName) {
    lessThan(arg, max, argName);
    greaterThan(arg, min, argName);
  }

  /**
   * Checks to see if an argument is equal to the specified value
   *
   * @param arg      The argument to check
   * @param expected The value arg must be equal to
   * @param argName  The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void equals(int arg, int expected, String argName) {
    if (arg != expected) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must be equal to " + expected);
    }
  }

  /**
   * Checks to see if an argument is less than the specified value
   *
   * @param arg     The argument to check
   * @param max     The value arg must be less than
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void lessThan(double arg, double max, String argName) {
    if (arg >= max) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must be less than " + max);
    }
  }

  /**
   * Checks to see if an argument is greater than the specified value
   *
   * @param arg     The argument to check
   * @param min     The value arg must be greater than
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void greaterThan(double arg, double min, String argName) {
    if (arg <= min) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must be greater than " + min);
    }
  }

  /**
   * Checks to see if an argument is greater than or equal to the specified value
   *
   * @param arg     The argument to check
   * @param min     The value arg must be greater than or equal to
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void greaterThanOrEqual(double arg, double min, String argName) {
    if (arg < min) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must be greater than or equal to " + min);
    }
  }

  /**
   * Checks to see if an argument is between than the specified value
   *
   * @param arg     The argument to check
   * @param min     The value arg must be greater than
   * @param max     The value arg must be less than
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void between(double arg, double min, double max, String argName) {
    lessThan(arg, max, argName);
    greaterThan(arg, min, argName);
  }

  /**
   * Checks to see if an array is shorter than the specified value
   *
   * @param arg     The argument to check
   * @param max     The value the array.length must be less than
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void shorterThan(Object[] arg, int max, String argName) {
    nonNull(arg, argName);
    if (arg.length >= max) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must have length less than " + max);
    }
  }

  /**
   * Checks to see if a Collection is shorter than the specified value
   *
   * @param arg     The argument to check
   * @param max     The value Collection.size() must be less than
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void shorterThan(Collection arg, int max, String argName) {
    nonNull(arg, argName);
    if (arg.size() >= max) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must have length less than " + max);
    }
  }

  /**
   * Checks to see if an array is longer than the specified value
   *
   * @param arg     The argument to check
   * @param min     The value the array.length must be greater than
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void longerThan(Object[] arg, int min, String argName) {
    nonNull(arg, argName);
    if (arg.length <= min) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must have length greater than " + min);
    }
  }

  /**
   * Checks to see if a Collection is longer than the specified value
   *
   * @param arg     The argument to check
   * @param min     The value the Collection.size() must be greater than
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void longerThan(Collection arg, int min, String argName) {
    nonNull(arg, argName);
    if (arg.size() <= min) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must have length greater than " + min);
    }
  }


  /**
   * Checks to see if an array length is between the specified values
   *
   * @param arg     The argument to check
   * @param min     The value the array must be greater than
   * @param max     The value the array must be less than
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void lenBetween(Object[] arg, int min, int max, String argName) {
    shorterThan(arg, max, argName);
    longerThan(arg, min, argName);
  }

  /**
   * Checks to see if a String is shorter than the specified value
   *
   * @param arg     The argument to check
   * @param max     The value the String must be less than
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void shorterThan(String arg, int max, String argName) {
    nonNull(arg, argName);
    if (arg.length() >= max) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must have length less than " + max);
    }
  }

  /**
   * Checks to see if a String is longer than the specified value
   *
   * @param arg     The argument to check
   * @param min     The value the String must be less than
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void longerThan(String arg, int min, String argName) {
    nonNull(arg, argName);
    if (arg.length() <= min) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must have length greater than " + min);
    }
  }

  /**
   * Checks to see if a String length is between the specified values
   *
   * @param arg     The argument to check
   * @param min     The value the String must be greater than
   * @param max     The value the String must be less than
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void lenBetween(String arg, int min, int max, String argName) {
    shorterThan(arg, max, argName);
    longerThan(arg, min, argName);
  }

  /**
   * Checks to see if a String is not null and has a length greater than 0. Equivalent to
   * longerThan(String, 0)
   *
   * @param arg     The argument to check
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void nonBlank(String arg, String argName) {
    longerThan(arg, 0, argName);
  }

  /**
   * Checks to see if a trimmed String a length greater than 0. Equivalent to
   * longerThan(String.trim(), 0)
   *
   * @param arg     The argument to check
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void nonWhitespace(String arg, String argName) {
    longerThan(arg == null ? arg : arg.trim(), 0, argName);
  }

  /**
   * Checks to make sure that a class is assignable from another class.  Essentially, this
   * means making sure that the argument class is a subclass of the given baseClass,
   * or implements the base class if the base class is an interface
   *
   * @param argClass  The argument to check
   * @param baseClass The class that argument must be a subclass (or implementer) of
   * @param argName   The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  @SuppressWarnings("unchecked")
  public static void isAssignable(Class argClass, Class baseClass, String argName) {
    if (!baseClass.isAssignableFrom(argClass)) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must be a subclass or implementer of the class " + baseClass.getName());
    }
  }

  /**
   * Checks to make sure that the argument passed in is true
   *
   * @param b       The argument to check
   * @param argName The name of the argument to be check. Used as part of the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void isTrue(boolean b, String argName) {
    if (!b) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" should be true.");
    }
  }

  /**
   * Checks to make sure that the argument passed in is true
   *
   * @param expression The expression related to the argument to check
   * @param comment    The explanation for the failure - used as the message for the IllegalArgumentException
   * @throws IllegalArgumentException if the test fails
   */
  public static void satisfiesCriteria(boolean expression, String comment) {
    if (!expression) {
      throw new IllegalArgumentException(comment);
    }
  }

  public static void after(Date argDate, Date baseDate, String argName) {
    nonNull(argDate, argName);
    nonNull(baseDate, "baseDate");
    if (!argDate.after(baseDate)) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" should be after \"" + baseDate + "\"");
    }
  }

  /**
   * Checks that the given number is positive.
   */
  public static void positive(BigDecimal n, String argName) {
    nonNull(n, argName);
    if (n.signum() <= 0) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must be positive. Got " + n);
    }
  }

  /**
   * Checks that the given number is negative.
   */
  public static void negative(BigDecimal n, String argName) {
    nonNull(n, argName);
    if (n.signum() >= 0) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must be negative. Got " + n);
    }
  }

  /**
   * Checks that the given number is not positive.
   */
  public static void nonPositive(BigDecimal n, String argName) {
    nonNull(n, argName);
    if (n.signum() > 0) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must be non-positive. Got " + n);
    }
  }

  /**
   * Checks that the given number is not negative.
   */
  public static void nonNegative(BigDecimal n, String argName) {
    nonNull(n, argName);
    if (n.signum() < 0) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must be non-negative. Got " + n);
    }
  }

  /**
   * Checks that the given number is not zero.
   */
  public static void nonZero(BigDecimal n, String argName) {
    nonNull(n, argName);
    if (n.signum() == 0) {
      throw new IllegalArgumentException("Argument \"" + argName + "\" must be non-zero. Got " + n);
    }
  }
}
