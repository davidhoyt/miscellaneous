package com.guidewire.wordy.impl;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

public class WordValidatorImplTest extends TestCase {

  public void testIsRealWordTrueIfWordInDictionaryFile() throws IOException {
    WordValidatorImpl validator = new WordValidatorImpl(new File("test/SimpleDictionary.txt"));
    assertValid(validator, "one", "two", "three", "four", "five");
  }

  public void testIsRealWordFalseIfWordNotInDictionaryFile() throws IOException {
    WordValidatorImpl validator = new WordValidatorImpl(new File("test/SimpleDictionary.txt"));
    assertNotValid(validator, "ones", "six");
  }

  public void testDuplicatesInDictionaryAreIgnored() throws IOException {
    WordValidatorImpl validator = new WordValidatorImpl(new File("test/DictionaryWithDuplicates.txt"));
    assertValid(validator, "one", "two", "three", "four", "five");
    assertNotValid(validator, "ones", "six");
  }

  public void testWordsInDictionaryAreNormalized() throws IOException {
    WordValidatorImpl validator = new WordValidatorImpl(new File("test/NonNormalizedDictionary.txt"));
    assertValid(validator, "one", "two", "three", "four", "five");
    assertNotValid(validator, "ones", "six");
  }

  public void testIsRealWorldNormalizedItsArgument() throws IOException {
    WordValidatorImpl validator = new WordValidatorImpl(new File("test/SimpleDictionary.txt"));
    assertValid(validator, "one ", "TWO", " three", " foUr ", "FIVE   ");
    assertNotValid(validator, "ones", "six", "ONES  ", " SIX  ");
  }

  public void testIsRealWordThrowsIfWordIsNull() throws IOException {
    WordValidatorImpl validator = new WordValidatorImpl(new File("test/SimpleDictionary.txt"));
    try {
      validator.isRealWord(null);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  private void assertValid(WordValidatorImpl validator, String... validWords) {
    for (String validWord : validWords) {
      assertTrue("Word " + validWord + " should have been valid", validator.isRealWord(validWord));
    }
  }

  private void assertNotValid(WordValidatorImpl validator, String... invalidWords) {
    for (String invalidWord : invalidWords) {
      assertFalse("Word " + invalidWord + " should not have been valid", validator.isRealWord(invalidWord));
    }
  }

}
