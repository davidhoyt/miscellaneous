package com.guidewire.wordy.impl;

import com.guidewire.wordy.util.ArgCheck;
import com.guidewire.wordy.util.FileUtil;
import com.guidewire.wordy.util.LineBlock;
import com.guidewire.wordy.util.StringUtil;
import com.guidewire.wordy.IWordValidator;

import java.util.Set;
import java.util.HashSet;
import java.io.File;
import java.io.IOException;

public class WordValidatorImpl implements IWordValidator {

  private Set<String> _validWords = new HashSet<String>();

  /**
   * Reads the given dictionary file which should contain one word per line. Each word is normalized
   * before it is stored.
   * @param dictionaryFile file containing the list of real words
   * @throws IOException if the file cannot be opened or fully read
   */
  public WordValidatorImpl(File dictionaryFile) throws IOException {
    FileUtil.readLines(dictionaryFile, new LineBlock() {
      @Override
      public boolean run(String line) {
        _validWords.add(StringUtil.normalizeWord(line));
        return true;
      }
    });
  }

  /**
   * Creates a word validator, based on the standard CROSSWD.TXT dictionary file
   * @return a new word validator
   * @exception IllegalStateException if cannot read the CROSSWD.TXT dictionary file
   */
  public static IWordValidator createStandardValidator() {
    try {
      return new WordValidatorImpl(new File("CROSSWD.TXT"));
    } catch (IOException e) {
      throw new IllegalStateException("Unable to read dictionary file", e);
    }
  }

  @Override
  public boolean isRealWord(String word) {
    ArgCheck.nonNull(word, "word");
    return _validWords.contains(StringUtil.normalizeWord(word));
  }

}
