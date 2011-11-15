package com.guidewire.wordy;

/**
 * The client application that uses the Wordy implementation.
 */
public class WordyGame {

  public static void main(String[] args) {
    IWordy wordy = null; // Instantiate your Wordy implementation here
    new WordyFrame(wordy).setVisible(true);
  }

}
