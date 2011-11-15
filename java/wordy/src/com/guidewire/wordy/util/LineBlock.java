package com.guidewire.wordy.util;

/**
 * Represents a block of code to be run on a line of text. Used by {@link FileUtil#readLines(java.io.File, LineBlock)}
 */
public interface LineBlock {
  /**
   * {@link FileUtil#readLines(java.io.File, LineBlock)} will call this method for each line in the file.
   * <p/>
   * NOTE: The line will NOT contain an EOL character.
   *
   * @param line the text for a line.
   * @return true to continue processing, false to stop.
   */
  boolean run(String line);
}
