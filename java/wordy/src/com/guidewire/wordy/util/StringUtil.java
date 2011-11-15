package com.guidewire.wordy.util;

/**
 * String utilities
 */
public class StringUtil {

  /**
   * Normalize word by forcing it to lower case and trimming any leading and trailing whitespace
   *
   * @param word word to be normalized
   * @return normalized word
   */
  public static String normalizeWord(String word) {
    ArgCheck.nonNull(word, "word");
    return word.toLowerCase().trim();
  }

  /** Never instantiated */
  private StringUtil() {}
}
