package com.guidewire.wordy.impl;

import com.guidewire.wordy.IWordScorer;
import junit.framework.TestCase;

/**
 * Test class for WordScorerImpl.
 *
 * @author dbrewster
 */
public class WordScorerImplTest extends TestCase {

  public void testScoreWordThrowsForNullArgument() {
    IWordScorer scorer = new WordScorerImpl();
    try {
      scorer.scoreWord(null);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException ex) {
      // ok
    }
  }

  public void testScoreWordReturnsZeroForZeroOneOrTwoLengthWords() {
    IWordScorer scorer = new WordScorerImpl();
    assertEquals(0, scorer.scoreWord(""));
    assertEquals(0, scorer.scoreWord("A"));
    assertEquals(0, scorer.scoreWord("AA"));
  }

  public void testScoreWordReturns1ForThreeOrFourLengthWords() {
    IWordScorer scorer = new WordScorerImpl();
    assertEquals(1, scorer.scoreWord("AAA"));
    assertEquals(1, scorer.scoreWord("AAAA"));
  }

  public void testScoreWordReturns2For5LengthWord() {
    IWordScorer scorer = new WordScorerImpl();
    assertEquals(2, scorer.scoreWord("AAAAA"));
  }

  public void testScoreWordReturns3For6LengthWord() {
    IWordScorer scorer = new WordScorerImpl();
    assertEquals(3, scorer.scoreWord("AAAAAA"));
  }

  public void testScoreWordReturns5For7LengthWord() {
    IWordScorer scorer = new WordScorerImpl();
    assertEquals(5, scorer.scoreWord("AAAAAAA"));
  }

  public void testScoreWordReturns11For8OrGreaterLengthWord() {
    IWordScorer scorer = new WordScorerImpl();
    assertEquals(11, scorer.scoreWord("AAAAAAAA"));

    // Test n+1
    for (int i = 9; i < 100; i++) {
      assertEquals(11, scorer.scoreWord(repeat('A', i)));
    }
  }

  private String repeat(char chr, int count) {
    StringBuffer buf = new StringBuffer(count);
    for (int i = 0; i < count; i++) {
      buf.append(chr);
    }
    return buf.toString();
  }
}
