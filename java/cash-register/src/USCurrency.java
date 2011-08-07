
/**
 * Describes United States currency.
 * 
 * @author David Hoyt <dhoyt@hoytsoft.org>
 */
public final class USCurrency implements Currency {
	public static final int 
		  PENNY         = 1
		, NICKEL        = 5
		, DIME          = 10
		, QUARTER       = 25
		, HALF_DOLLAR   = 50
	;
	
	/**
	 * Array containing a list of valid coin denominations in the United States.
	 */
	public static final int[] 
		DENOMINATIONS = {
			  HALF_DOLLAR
			, QUARTER
			, DIME
			, NICKEL
			, PENNY
		}
	;
	
	/**
	 * Singleton for accessing US currency.
	 */
	public static final Currency INSTANCE = new USCurrency();

	/**
	 * Private constructor to prevent others from instantiating this class.
	 */
	private USCurrency() {
	}
	
	/**
	 * Retrieves a list of coin denominations for this currency.
	 * 
	 * @return An integer array containing a list of coin denominations.
	 */
	@Override
	public int[] getDenominations() {
		return DENOMINATIONS;
	}
}
