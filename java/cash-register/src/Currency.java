
/**
 * Describes a currency.
 * 
 * @author David Hoyt <dhoyt@hoytsoft.org>
 */
public interface Currency {
	/**
	 * Retrieves a list of coin denominations for this currency.
	 * 
	 * @return An integer array containing a list of coin denominations.
	 */
	int[] getDenominations();
}
