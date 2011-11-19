package com.guidewire.wordy.impl;

import com.guidewire.wordy.IBoard;
import com.guidewire.wordy.IWordInBoardValidator;
import com.guidewire.wordy.IWordScorer;
import com.guidewire.wordy.IWordValidator;
import com.guidewire.wordy.IWordy;
import com.guidewire.wordy.util.ArgCheck;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implements {@link IWordy}.
 * <p/>
 * Assumptions:
 * <ol>
 *  <li>
 *		Multiple words can be on one line in the input.<br /><br />
 *		
 *		The word list used in {@link WordyImpl#scoreWords(java.util.List)} 
 *		may actually contain a line consisting of multiple words 
 *		delineated by whitespace.<br /><br />
 *  </li>
 *  <li>
 *		Words are not longer than the maximum possible word size 
 *		(16 for a 4x4 board).<br /><br />
 *  </li>
 *  <li>
 *		Invalid words are automatically skipped.<br /><br />
 *  </li>
 *  <li>
 *		Punctuation is considered to be part of a word.<br /><br />
 *  </li>
 *  <li>
 *		Tiles/cells can be reused across words for scoring.<br /><br />
 *  </li>
 * </ol>
 * 
 * This class is NOT thread-safe.
 * 
 * @author David Hoyt <dhoyt@hoytsoft.org>
 */
public final class WordyImpl implements IWordy {
	//<editor-fold defaultstate="collapsed" desc="Constants">
	private static final int 
		  MIN_WORD_SIZE = 3
		, MAX_WORD_SIZE = IBoard.BOARD_CELLS
	;
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Variables">
	/** Reference to our current game board. */
	private IBoard board;
	
	/** Reference to our current board strategy. */
	private IGenerateBoardStrategy board_strategy;
	
	/** Reference to our current word scorer. */
	private IWordScorer scorer;
	
	/** Reference to our current word validator. */
	private IWordValidator word_validator;
	
	/** Reference to our current validator to see if a word is on the board. */
	private IWordInBoardValidator word_in_board_validator;
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Initialization">
	/**
	 * Instantiates a new Wordy game. The board is randomly generated.
	 * 
	 * @return An instance of {@link IWordy}.
	 */
	public static IWordy createStandardGame() {
		return new WordyImpl(
			new WordScorerImpl(), 
			WordValidatorImpl.createStandardValidator(), 
			new WordInBoardValidatorImpl(), 
			
			new IGenerateBoardStrategy() {
				@Override
				public IBoard generateNewBoard() {
					return new BoardImpl();
				}
			}
		);
	}
	
	/**
	 * Instantiates a new Wordy game. This one allows you to create a game with a 
	 * given board instead of a randomly generated one.
	 * 
	 * @return An instance of {@link IWordy}.
	 */
	public static IWordy createKnownGame(final char[][] board) {
		ArgCheck.nonNull(board, "board");
		ArgCheck.equals(board.length, IBoard.BOARD_ROWS, "board.length");
		for(int i = 0; i < IBoard.BOARD_ROWS; ++i) {
			ArgCheck.nonNull(board[i], "board[" + i + "]");
			ArgCheck.equals(board[i].length, IBoard.BOARD_COLUMNS, "board[" + i + "].length");
		}
		
		return new WordyImpl(
			new WordScorerImpl(), 
			WordValidatorImpl.createStandardValidator(), 
			new WordInBoardValidatorImpl(), 
			
			new IGenerateBoardStrategy() {
				@Override
				public IBoard generateNewBoard() {
					return new IBoard() {
						@Override
						public char getCell(int row, int column) {
							ArgCheck.between(row, -1, IBoard.BOARD_ROWS, "row");
							ArgCheck.between(column, -1, IBoard.BOARD_COLUMNS, "column");
							return Character.toUpperCase(board[row][column]);
						}
					};
				}
			}
		);
	}
	
	/**
	 * Instantiates a new Wordy game. This one allows you to create a game with a 
	 * given board instead of a randomly generated one.
	 * 
	 * @return An instance of {@link IWordy}.
	 */
	public static IWordy createKnownGame(final String... board) {
		ArgCheck.nonNull(board, "board");
		ArgCheck.equals(board.length, IBoard.BOARD_ROWS, "board.length");
		for(int i = 0; i < IBoard.BOARD_ROWS; ++i) {
			ArgCheck.nonNull(board[i], "board[" + i + "]");
			ArgCheck.equals(board[i].length(), IBoard.BOARD_COLUMNS, "board[" + i + "].length()");
		}
		
		return new WordyImpl(
			new WordScorerImpl(), 
			WordValidatorImpl.createStandardValidator(), 
			new WordInBoardValidatorImpl(), 
			
			new IGenerateBoardStrategy() {
				@Override
				public IBoard generateNewBoard() {
					return new IBoard() {
						@Override
						public char getCell(int row, int column) {
							ArgCheck.between(row, -1, IBoard.BOARD_ROWS, "row");
							ArgCheck.between(column, -1, IBoard.BOARD_COLUMNS, "column");
							return Character.toUpperCase(board[row].charAt(column));
						}
					};
				}
			}
		);
	}
	
	/**
	 * Don't allow instances to be directly created. Please use the static 
	 * method for instantiation.
	 */
	private WordyImpl(IWordScorer scorer, IWordValidator word_validator, IWordInBoardValidator word_in_board_validator, IGenerateBoardStrategy board_strategy) {
		ArgCheck.nonNull(scorer, "scorer");
		ArgCheck.nonNull(word_validator, "word_validator");
		ArgCheck.nonNull(word_in_board_validator, "word_in_board_validator");
		
		this.board = null;
		this.scorer = scorer;
		this.board_strategy = board_strategy;
		this.word_validator = word_validator;
		this.word_in_board_validator = word_in_board_validator;
	}
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Generate Strategy">
	/**
	 * Used to indicate how we'll generate new boards.
	 */
	private interface IGenerateBoardStrategy {
		public IBoard generateNewBoard();
	}
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Utilities">
	/**
	 * Takes a list of Strings and converts them into a list.
	 * 
	 * @param words List of words to migrate into a {@link List}.
	 * 
	 * @return A {@link List} containing the words passed in.
	 */
	private static List<String> list(String... words) {
		List<String> lst = new ArrayList<String>(words.length);
		for(String word : words)
			if (word != null && !"".equalsIgnoreCase(word))
				lst.add(word);
		return lst;
	}
	//</editor-fold>

	/**
	 * {@see IWordy#generateNewBoard()}
	 */
	@Override
	public IBoard generateNewBoard() {
		return (this.board = this.board_strategy.generateNewBoard());
	}
	
	/**
	 * {@see IWordy#scoreWords(java.util.List)}
	 */
	public int scoreWords(String... words) {
		return scoreWords(list(words));
	}

	/**
	 * {@see IWordy#scoreWords(java.util.List)}
	 */
	@Override
	public int scoreWords(List<String> words) {
		ArgCheck.nonNull(board, "board", "The board has not been generated yet.");
		ArgCheck.nonNull(words, "words");
		
		int sum = 0;
		int len = 0;
		Set<String> word_set = new HashSet<String>(words.size(), 1.0f);
		
		for(String possible_word : words) {
			// Discard null or blank words/lines, normalizing it by removing whitespace along the way.
			if (null == possible_word || "".equalsIgnoreCase(possible_word = possible_word.trim()))
				continue;
			
			// A single line may contain multiple words. Split them up by whitespace.
			// Note that the spec. did not say if the list of words was guaranteed to be 1 
			// per line or how to treat multiple words per line. The caller simply splits by 
			// newline without further normalization. So the extra step to further split by 
			// whitespace is taken as an extra precaution.
			//
			// This could be optimized by caching a regex and then reusing it here.
			//
			// Punctuation is not automatically removed and is assumed to be part of the 
			// word.
			//
			// You would also want to double check overflow when calculating the sum of the 
			// scored words.
			for(String word : possible_word.trim().split("\\s+")) {
				if (null == word || "".equalsIgnoreCase(word = word.trim().toUpperCase()) || (len = word.length()) < MIN_WORD_SIZE || len > MAX_WORD_SIZE || !word_set.add(word))
					continue;
				
				// Maintain a sum of scored, valid words. Attempt to short circuit the 
				// potentially intensive operation of validating words in the board by first 
				// looking them up in our dictionary (presumably using a hashtable or 
				// trie-style implementation).
				if (word_validator.isRealWord(word) && word_in_board_validator.isWordInBoard(board, word))
					sum += scorer.scoreWord(word);
			}
		}
		
		// Try to be a bit proactive and clear out the collection now so the garbage collector 
		// can potentially wipe it out sooner.
		word_set.clear();
		
		return sum;
	}
}
