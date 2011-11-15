package com.guidewire.wordy;

/**
 * Interface for the board implementation.  A board is a 4x4 grid of English letters.
 */
public interface IBoard {

  /** Number of rows in the board */
  public static final int BOARD_ROWS = 4;

  /** Number of columns in the board */
  public static final int BOARD_COLUMNS = 4;

  /** Number of cells in the board */
  public static final int BOARD_CELLS = BOARD_ROWS * BOARD_COLUMNS;

  /**
   * Returns the character value at the row and column position specified.
   *
   * @param row    the row to fetch
   * @param column the column to fetch
   * @return the character at the given position
   * @throws IllegalArgumentException if row or column is less than 0 or greater than or equal to 4.
   */
  public char getCell(int row, int column);
}
