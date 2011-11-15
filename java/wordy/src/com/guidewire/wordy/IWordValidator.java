package com.guidewire.wordy;

/**
 * Checks that the word exists in a dictionary (i.e., is a real word)
 */
public interface IWordValidator {

  /**
   * Checks a dictionary to determine whether a word is a real word.  This does _not_ imply that the word exists in any
   * particular game board instance -- that's the purpose of {@link IWordInBoardValidator#isWordInBoard}
   *
   * @param word the word to check
   * @return <code>true</code> if the word is a real word (i.e., found in the dictionary), otherwise <code>false</code>
   */
  boolean isRealWord(String word);

}

