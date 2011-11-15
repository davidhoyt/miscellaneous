package com.guidewire.wordy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * File utility methods.
 */
public class FileUtil {
  /**
   * Opens the given file in read mode, and passes each line to the given block. Lines are generated using
   * {@link java.io.BufferedReader#readLine()}, so any end-of-line characters, as defined by that method,
   * will not be included in the line.
   */
  public static void readLines(File f, final LineBlock block) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
    try {
      String line;
      boolean cont = true;
      while (cont && (line = reader.readLine()) != null) {
        cont = block.run(line);
      }
    } finally {
      reader.close();
    }
  }
}
