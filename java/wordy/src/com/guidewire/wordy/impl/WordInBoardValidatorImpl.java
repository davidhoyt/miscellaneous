package com.guidewire.wordy.impl;

import com.guidewire.wordy.IBoard;
import com.guidewire.wordy.IWordInBoardValidator;

public class WordInBoardValidatorImpl implements IWordInBoardValidator {

  @Override
  public boolean isWordInBoard(IBoard board, String word) {
    // Add your implementation here; the default "implementation" just
    // assumes all words are in the board
    return true;
  }

}
