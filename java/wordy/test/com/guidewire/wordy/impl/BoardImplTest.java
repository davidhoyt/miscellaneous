package com.guidewire.wordy.impl;

import com.guidewire.wordy.IBoard;
import junit.framework.TestCase;

/**
 * Tests the basics of the BoardImpl implementation. Does not do a good job of testing whether the
 * generated board is random; testing for randomness is tricky and beyond the scope of this problem.
 */
public class BoardImplTest extends TestCase {
  
  private IBoard _board = new BoardImpl();

  public void testGetCellThrowsIllegalArgumentExceptionIfRowNegative() {
    try {
      _board.getCell(-1, 0);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  public void testGetCellThrowsIllegalArgumentExceptionIfColumnNegative() {
    try {
      _board.getCell(0, -1);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  public void testGetCellThrowsIllegalArgumentExceptionIfRowTooLarge() {
    try {
      _board.getCell(4, 0);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  public void testGetCellThrowsIllegalArgumentExceptionIfColumnTooLarge() {
    try {
      _board.getCell(0, 4);
      fail("Should have thrown IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  public void testAllBoardCellsAreUpperCaseLetters() {
    for (int row = 0; row < IBoard.BOARD_ROWS; row++) {
      for (int column = 0; column < IBoard.BOARD_COLUMNS; column++) {
        char ch = _board.getCell(row, column);
        if (!('A' <= ch && ch <= 'Z')) {
          fail("Character at [" + row + "," + column + "] was not a letter: " + ch);
        }
      }
    }
  }
}
