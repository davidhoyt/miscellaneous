package com.guidewire.wordy.impl;

import com.guidewire.wordy.IBoard;
import com.guidewire.wordy.util.ArgCheck;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class BoardImpl implements IBoard {

  private static final Random RAND = new Random();

  /**
   * A die that can be "thrown" to determine the value of a single cell in the board.
   */
  public static class Die {

    /**
     * A list of dice, one for each cell in the board
     */
    public static final List<Die> ALL_DICE = createDieList(
            "AAEEGN", "ELRTTY", "AOOTTW", "ABBJOO",
            "EHRTVW", "CIMOTU", "DISTTY", "EIOSST",
            "DELRVY", "ACHOPS", "HIMNQU", "EEINSU",
            "EEGHNW", "AFFKPS", "HLNNRZ", "DEILRX"
    );

    private static List<Die> createDieList(String... allDieSides) {
      List<Die> dice = new ArrayList<Die>(allDieSides.length);
      for (String dieSides : allDieSides) {
        dice.add(new Die(dieSides));
      }
      if (dice.size() != BOARD_CELLS) {
        throw new IllegalStateException(
                "Number of cells (" + (BOARD_CELLS)
                + ") does not match the number of dice (" + dice.size() + ")");
      }
      return Collections.unmodifiableList(dice);
    }

    private static final int DIE_SIDES = 6;

    private String _chars;

    private Die(String chars) {
      ArgCheck.equals(chars.length(), DIE_SIDES, "chars.length()");
      _chars = chars;
    }

    /**
     * Return the letter from a random side of the die
     * @return an upper case letter
     */
    public char throwDie() {
      return _chars.charAt(RAND.nextInt(DIE_SIDES));
    }
  }

  private char[][] _cells = new char[BOARD_ROWS][BOARD_COLUMNS];

  /**
   * Initializes a board by throwing a random die for each cell in the board
   */
  public BoardImpl() {
    List<Die> remainingDice = new ArrayList<Die>(Die.ALL_DICE);
    for (int row = 0; row < BOARD_ROWS; row++) {
      for (int column = 0; column < BOARD_COLUMNS; column++) {
        Die nextDie = remainingDice.remove(RAND.nextInt(remainingDice.size()));
        _cells[row][column] = nextDie.throwDie();
      }
    }
  }

  @Override
  public char getCell(int row, int column) {
    ArgCheck.between(row, -1, BOARD_ROWS, "row");
    ArgCheck.between(column, -1, BOARD_COLUMNS, "column");
    return _cells[row][column];
  }

}
