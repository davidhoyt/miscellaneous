package com.guidewire.wordy.impl;

import com.guidewire.wordy.IBoard;
import com.guidewire.wordy.IWordInBoardValidator;
import com.guidewire.wordy.util.ArgCheck;

/**
 * Validates whether a given word appears on a Wordy board. The strategy is to look 
 * in all directions for tiles/cells that match the word letter-by-letter. You 
 * cannot repeat or reuse tiles you've already visited. For more information, please 
 * see the documentation for validateWord().
 * <br /><br />
 * This class is NOT thread-safe.
 * 
 * @author David Hoyt <dhoyt@hoytsoft.org>
 */
public final class WordInBoardValidatorImpl implements IWordInBoardValidator {
	//<editor-fold defaultstate="collapsed" desc="Variables">
	private boolean[][] visited = new boolean[IBoard.BOARD_COLUMNS][IBoard.BOARD_ROWS];
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Initialization">
	public WordInBoardValidatorImpl() {
	}
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Utilities">
	/**
	 * Resets our multidimensional boolean visited array back to false.
	 */
	private void cleanVisited() {
		for(int i = 0; i < visited.length; ++i)
			for(int j = 0; j < visited[i].length; ++j)
				visited[i][j] = false;
	}
	//</editor-fold>
	
	/**
	 * {@see IWordInBoardValidator#isWordInBoard(com.guidewire.wordy.IBoard, java.lang.String)}
	 */
	@Override
	public boolean isWordInBoard(IBoard board, String word) {
		//<editor-fold defaultstate="collapsed" desc="Validate and initialize parameters">
		ArgCheck.nonNull(word, "word");
		ArgCheck.nonNull(board, "board");
		
		word = word.trim().toUpperCase();
		int len = word.length();
		if (len <= 0)
			return false;
		//</editor-fold>
		
		// Get the first letter of the word.
		char first_letter = word.charAt(0);
		
		// The number of letters left to check is decremented.
		--len;
		
		// We'll be reusing a multidimensional boolean array to mark cells that we have 
		// already visited so we don't attempt to reuse them.
		//
		// Cycle through the board and locate any place that starts with the first letter.
		// This will be our starting point.
		for(int i = 0; i < IBoard.BOARD_ROWS; ++i) {
			for(int j = 0; j < IBoard.BOARD_COLUMNS; ++j) {
				if (first_letter != board.getCell(i, j))
					continue;
				
				cleanVisited();
				if (validateWord(j, i, 1, len, visited, word, board))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Recursively examines the board one letter at a time by shifting an index whenever we find 
	 * an adjacent, unvisited cell whose letter matches the one we're currently on.
	 * <p/>
	 * Issues with this method:
	 * <ol>
	 *  <li>
	 *		If the board is much larger, you could potentially run out of stack space and cause 
	 *		a {@link StackOverflowError}. Since the board size is effectively hardcoded at 
	 *		16 due to {@link IBoard#BOARD_CELLS}, our maximum depth should not exceed 
	 *		15 since the caller will save us the trouble of finding valid starting points.
	 *		<br /><br />
	 *	</li>
	 *  <li>
	 *		You could potentially eliminate many of the recursion issues through tail recursion 
	 *		or a stack-based solution. But in terms of simplicity and being able to read and 
	 *		understand the code, this one seemed best.<br /><br />
	 *	</li>
	 *  <li>
	 *		It recurses more than it has to. We could evaluate some of the conditions before 
	 *		recursively calling the method and thus avoid unnecessary calls, but to better  
	 *		understand the algorithm, this solution was chosen instead.<br /><br />
	 *	</li>
	 *  <li>
	 *		You could also use a bit array instead of a multidimensional boolean array. Again, 
	 *		it was chosen for aesthetic and illustrative reasons only.
	 *	</li>
	 * </ol>
	 * 
	 * @param x         The column of the board you want to look at next.
	 * @param y         The row of the board you want to look at next.
	 * @param idx       The current position within the word that you're at.
	 * @param remaining How many letters are left to evaluate.
	 * @param word      The original word you're searching for.
	 * @param board     The board that has the randomized grid of letters.
	 * 
	 * @return True if the word is in the board according to Wordy rules.
	 */
	private boolean validateWord(final int x, final int y, final int idx, final int remaining, final boolean[][] visited, final String word, final IBoard board) {
		if (remaining == 0)
			return true;
		
		int proposed_x, proposed_y;
		
		// Examine the cells all around the current one. Using the nested loop 
		// just helps to compact the code a bit.
		visited[x][y] = true;
		for(int i = -1; i <= 1; ++i) {
			for(int j = -1; j <= 1; ++j) {
				if (i == 0 && j == 0)
					continue;
				proposed_x = x + i;
				proposed_y = y + j;
				
				// The order here matters since board.getCell() will throw an exception 
				// if we attempt to get a cell's value and it's not in the valid board 
				// range.
				if (
					   proposed_x >= 0 
					&& proposed_y >= 0 
					&& proposed_x < IBoard.BOARD_COLUMNS 
					&& proposed_y < IBoard.BOARD_ROWS 
					&& !visited[proposed_x][proposed_y] 
					&& board.getCell(proposed_y, proposed_x) == word.charAt(idx) 
					&& validateWord(proposed_x, proposed_y, idx + 1, remaining - 1, visited, word, board)
				)
					return true;
			}
		}
		visited[x][y] = false;
		
		return false;
	}
}
