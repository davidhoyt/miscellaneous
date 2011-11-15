package com.guidewire.wordy;



import java.util.List;

/**
 * Interface that defines the main "Wordy" application class that external clients, such as WordyGame, will use.
 */
public interface IWordy {

  /**
   * Request that a new "board" letters be generated.
   * <p/>
   * The board should be contain a random grid of upper case Characters only
   *
   * @return a board of randomly generated upper case characters.
   */
  IBoard generateNewBoard();

  /**
   * Request that the list of words be scored according to the game's scoring algorithm.
   *
   * @param words list of words
   * @return the score based on the given word list
   */
  int scoreWords(List<String> words);

}
