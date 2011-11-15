package com.guidewire.wordy;

/**
 * Verifies that the word can be found in a particular board or instance.
 */
public interface IWordInBoardValidator {
  /**
   * Checks whether the given word can be found in the board. The rules for a "valid" word are:
   * <ul>
   * <li>Words are formed from adjoining letters.</li>
   * <li>Letters must join in the proper sequence to spell a word.</li>
   * <li>They may join horizontally, vertically, or diagonally, to the left, right, or up-and-down.</li>
   * <li>No letter cell may be used more than once within a single word.</li>
   * <li>The word must be at least one character (the empty string does not count as a word)</li>
   * </ul>
   *
   * @param board the board in which to find the word
   * @param word the word to search for
   * @return true if the word is present in the board, false if the word is empty or not present
   */
  boolean isWordInBoard(IBoard board, String word);
}
