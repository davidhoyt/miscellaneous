package com.guidewire.wordy;


/**
 * Defines the Scoring interface for a Word Scorer.
 */
public interface IWordScorer {

  /**
   * Returns the score of the word based on the following rules:
   * <p/>
   * <table>
   * <tr><th>Word Length:</th><th rowspan="7">&nbsp;&nbsp;&nbsp;</th><th>Points:</th></tr>
   * <tr><td>2 or fewer</td><td>0 (invalid)</td></tr>
   * <tr><td>3 or 4</td><td>1</td></tr>
   * <tr><td>5</td><td>2</td></tr>
   * <tr><td>6</td><td>3</td></tr>
   * <tr><td>7</td><td>5</td></tr>
   * <tr><td>8 and over</td><td>11</td></tr>
   * </table>
   * <p/>
   *
   * @param word the word to score: must not be null
   * @return the score for the given word based on the scoring rules.
   */
  int scoreWord(String word);

}
