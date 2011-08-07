
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class provides a solution for a cash register that accepts a list of coin 
 * denominations.
 * 
 * You can add and remove coins from an inventory in single or bulk quantities.
 * 
 * Sales do not add anything to the register. It is assumed you will call the various 
 * add methods if you require that. Sales do, however, remove coins from the register 
 * automatically.
 * 
 * Change is only ever produced in coins (never bills).
 * 
 * @author David Hoyt <dhoyt@hoytsoft.org>
 */
public final class CashRegister {
	/**
	 * The denominations used by this class.
	 */
	private final int[] denominations;
	
	/**
	 * Holds our current cash register inventory.
	 */
	private final CoinBundle inventory;

	/**
	 * Instantiates a new {@link CashRegister} object.
	 * By default it will use United States currency. See {@link USCurrency}.
	 */
	public CashRegister() {
		this(USCurrency.INSTANCE);
	}
	
	/**
	 * Instantiates a new {@link CashRegister} object.
	 * 
	 * @param currency An instance of {@link Currency} that provides a 
	 *                 list of denominations this {@link CashRegister} will accept.
	 *                 It must pass the checks in {@link CashRegister#checkDenominations()}.
	 */
	public CashRegister(Currency currency) {
		this(currency.getDenominations());
	}
	
	/**
	 * Instantiates a new {@link CashRegister} object.
	 * 
	 * @param denominations A list of denominations this {@link CashRegister} will accept.
	 *                      It must pass the checks in {@link CashRegister#checkDenominations()}.
	 */
	public CashRegister(int[] denominations) {
		denominations = cleanDenominations(denominations);
		if (!checkDenominations(denominations)) {
			throw new IllegalArgumentException("denominations are not cash register compliant");
		}

		this.denominations = denominations;
		this.inventory = new CoinBundle();
	}
	
	/**
	 * Determines if a set of denominations adheres to cash register guidelines.
	 * In our case, this cash register must have at least one denomination that 
	 * is equal to 0.
	 * 
	 * @param denominations The array to check.
	 * @return True if the array is cash register-compliant.
	 */
	public static boolean checkDenominations(int[] denominations) {
		if (denominations == null || denominations.length <= 0) {
			throw new IllegalArgumentException("denominations cannot be empty or null");
		}

		//Check each coin.
		//  Is each one >= 1?
		//  Is there at least one that == 1?
		boolean one_found = false;
		for (int i = 0; i < denominations.length; ++i) {
			if (denominations[i] < 1) {
				return false;
			}
			if (denominations[i] == 1) {
				one_found = true;
			}
		}
		return one_found;
	}

	/**
	 * Ensures that the array contains only unique values in ascending order.
	 * 
	 * @param denominations The array to clean.
	 */
	public static int[] cleanDenominations(int[] denominations) {
		if (denominations == null || denominations.length <= 0) {
			throw new IllegalArgumentException("denominations cannot be empty or null");
		}

		//Quickly adds an array into a set so we can then take it back out of the 
		//set and have an array of unique values. Quick and dirty.
		final Set<Integer> set = new HashSet<Integer>(denominations.length, 1.0f);
		for (int i = 0; i < denominations.length; ++i) {
			//Watch the autoboxing.
			set.add(denominations[i]);
		}
		final Integer[] set_denominations = set.toArray(new Integer[set.size()]);
		final int[] ret = new int[set_denominations.length];
		for (int i = 0; i < set_denominations.length; ++i) {
			//Unboxing.
			ret[i] = set_denominations[i];
		}

		//Sorts the array in ascending order.
		Arrays.sort(ret);

		//All done.
		return ret;
	}

	/**
	 * Retrieves the total current inventory amount in the register at the time of the call.
	 * 
	 * @return The total current inventory amount.
	 */
	public long getTotalAmount() {
		return inventory.getTotal();
	}
	
	/**
	 * Creates a string representing the state of the cash register.
	 * 
	 * @return A string representing the state of the cash register.
	 */
	@Override
	public String toString() {
		return inventory.toString();
	}
	
	/**
	 * Creates an exact copy of the provided array.
	 * 
	 * @return A new integer array containing the passed array's values.
	 */
	private int[] copy(int[] array) {
		int[] ret = new int[array.length];
		for(int i = 0; i < ret.length; ++i)
			ret[i] = array[i];
		return ret;
	}
	
	/**
	 * Creates an exact copy of the provided Integer array as a primitive int array.
	 * 
	 * @return The Integer array to convert.
	 */
	private int[] toIntArray(Integer[] array) {
		int[] ret = new int[array.length];
		for(int i = 0; i < ret.length; ++i)
			ret[i] = array[i];
		return ret;
	}
	
	/**
	 * Determines the index of the denomination in both the denominations and 
	 * inventory arrays.
	 * 
	 * @param denomination The coin denomination you're interested in finding the index of.
	 * @return The 0-based index into the array where you can find the denomination.
	 */
	private int indexOfDenomination(int denomination) {
		//This works because cleanDenominations() sorts the array in the constructor before 
		//we perform any kind of operation on it.
		return (Arrays.binarySearch(denominations, denomination));
	}

	/**
	 * Determines if the provided coin is in the set of allowed denominations.
	 * 
	 * @param coin The coin we're validating.
	 * @return True if the provided coin is in the set of allowed denominations.
	 */
	public boolean isValidCoin(int coin) {
		//Check if the coin shows up in our array of valid denominations.
		return (indexOfDenomination(coin) >= 0);
	}
	
	/**
	 * Creates a new {@link CoinBundle}.
	 * @return A new coin bundle.
	 */
	public CoinBundle createCoinBundle() {
		return new CoinBundle();
	}
	
	/**
	 * Be careful calling this ensure that you input all the counts for 
	 * every unique denomination in ascending order. If you don't, 
	 * then your values can be off.
	 * 
	 * @param denomination_counts A list of counts, 1 per possible denomination.
	 */
	public CoinBundle createCoinBundle(int...denomination_counts) {
		return new CoinBundle(denomination_counts);
	}

	/**
	 * Adds coins to the register's inventory.
	 * 
	 * @param denomination The denomination of the coin you wish to add.
	 * @return True if the operation was successful.
	 */
	public boolean addCoin(int denomination) {
		return addCoinRoll(denomination, 1);
	}

	/**
	 * Adds multiple coins of the same denomination to the register's inventory.
	 * 
	 * @param denomination The denomination of the coin you wish to add.
	 * @param count The number of coins you wish to add.
	 * @return True if the operation was successful.
	 */
	public boolean addCoinRoll(int denomination, int count) {
		return inventory.add(denomination, count);
	}
	
	/**
	 * Adds a bundle of coins to the inventory. It does NOT automatically 
	 * remove the passed bundle's coins.
	 * 
	 * @param bundle The bundle whose coins you want added to this inventory.
	 * @return True if the operation was successful.
	 */
	public boolean addCoinBundle(CoinBundle bundle) {
		return inventory.add(bundle);
	}
	
	/**
	 * Adds a bundle of coins to the inventory. It does NOT automatically 
	 * remove the passed bundle's coins.
	 * 
	 * @param denomination_counts The bundle whose coins you want added to this inventory.
	 * @return True if the operation was successful.
	 */
	public boolean addCoinBundle(int...denomination_counts) {
		return addCoinBundle(createCoinBundle(denomination_counts));
	}

	/**
	 * Removes coins from the register's inventory.
	 * @param denomination The denomination of the coin you wish to remove.
	 * @return True if the operation was successful.
	 */
	public boolean removeCoin(int denomination) {
		return removeCoinRoll(denomination, 1);
	}

	/**
	 * Removes multiple coins of the same denomination from the register's inventory.
	 * @param denomination The denomination of the coin you wish to remove.
	 * @param count The number of coins you wish to remove.
	 * @return True if the operation was successful.
	 */
	public boolean removeCoinRoll(int denomination, int count) {
		return inventory.remove(denomination, count);
	}
	
	/**
	 * Removes a bundle of coins from this one.
	 * 
	 * @param bundle The bundle whose coins you want removed from this instance's.
	 * @return True if the operation was successful.
	 */
	public boolean removeCoinBundle(CoinBundle bundle) {
		return inventory.remove(bundle);
	}
	
	/**
	 * Removes a bundle of coins from this one.
	 * 
	 * @param denomination_counts The bundle whose coins you want removed from this instance's.
	 * @return True if the operation was successful.
	 */
	public boolean removeCoinBundle(int...denomination_counts) {
		return removeCoinBundle(createCoinBundle(denomination_counts));
	}
	
	/**
	 * Resets the inventory to a 0-coin state.
	 * 
	 * @return True if the operation was successful.
	 */
	public boolean clearCoins() {
		return inventory.clear();
	}
	
	/**
	 * Simple method to print the inventory to stdout.
	 */
	public void printInventory() {
		System.out.println(inventory.toString());
	}
	
	/**
	 * Make a sale.
	 * 
	 * @param cost How much the sale item cost in base denomination units (e.g. "penny" in the U.S.).
	 * @param amount_tendered The amount the customer offered to pay for the sale item in base denomination units (e.g. "penny" in the U.S.).
	 * @return The change.
	 */
	public CoinBundle sale(long cost, long amount_tendered) {
		if (cost < 0L)
			throw new IllegalArgumentException("Cannot have a negative cost");
		if (amount_tendered < 0L)
			throw new IllegalArgumentException("Cannot have a negative tendered amount");
		if (amount_tendered < cost)
			throw new IllegalArgumentException("The amount tendered must be greater than or equal to the cost");
		
		final long needed_change = amount_tendered - cost;
		if (inventory.getTotal() < needed_change)
			throw new IllegalStateException("There is not enough change in the register for this sale");
		
		final CoinBundle change = determine_change(needed_change);
		if (change == null)
			throw new IllegalStateException("Unable to process this sale because there's not enough coins in the inventory for the change");
		
		//Reduce our inventory by the amount of change given back.
		if (!inventory.remove(change))
			throw new IllegalStateException("Unable to update our inventory with the calculated change");
		
		return change;
	}
	
	/**
	 * See {@link CashRegister#determine_change_rec(CashRegister.CoinBundle, int, long, CashRegister.CoinBundle, CashRegister.CoinBundle[])}
	 */
	private CoinBundle determine_change(long amount) {
		//Return an empty bundle if there's no change.
		if (amount <= 0L)
			return createCoinBundle();
		
		CoinBundle bundle = createCoinBundle();
		CoinBundle[] smallest_solution_ref = new CoinBundle[1];
		determine_change_rec(inventory.duplicate(), 0, amount, bundle, smallest_solution_ref);
		return smallest_solution_ref[0];
	}
	
	/**
	 * The typical dynamic programming coin denomination solution didn't seem sufficient 
	 * for working with an inventory since it assumes an unlimited supply of coins for 
	 * every denomination.
	 * 
	 * The solution here instead calculates every possible solution but prunes branches 
	 * where we would run out of coins. This helps reduce processing time and it's 
	 * still reasonable for most calculations.
	 * 
	 * It could have been sped up if a greedy algorithm were permissible, but that would 
	 * not be correct in all cases. You could probably also remove the recursion.
	 */
	private void determine_change_rec(CoinBundle inventory, int denomination_index_to_start_at, long amount_remaining, CoinBundle bundle, CoinBundle[] smallest_solution) {
		long intermediate_amount_remaining;
		for(int i = denomination_index_to_start_at; i < denominations.length; ++i) {
			if (inventory.coins[i] <= 0)
				continue;
			
			intermediate_amount_remaining = amount_remaining - denominations[i];
			
			if (intermediate_amount_remaining < 0) {
				break;
			} else {
				//For efficiency's sake, we're accessing variables directly 
				//instead of through the standard public methods. It increases 
				//coupling, but I feel it's worth it for this algorithm as 
				//a pragmatic way of reducing unnecessary locking on the 
				//bundle that's already entirely created and handled in the 
				//context of the determine_change* methods anyway.
				if (intermediate_amount_remaining == 0) {
					++bundle.coins[i];
					++bundle.coin_count;
					if (smallest_solution[0] == null || bundle.getCoinCount() < smallest_solution[0].getCoinCount()) {
						bundle.recalculateTotal();
						bundle.recalculateCoinCount();
						smallest_solution[0] = bundle.duplicate();
					}
					--bundle.coin_count;
					--bundle.coins[i];
					break;
				} else {
					++bundle.coins[i];
					++bundle.coin_count;
					--inventory.coins[i];
					determine_change_rec(inventory, i, intermediate_amount_remaining, bundle, smallest_solution);
					++inventory.coins[i];
					--bundle.coin_count;
					--bundle.coins[i];
				}
			}
		}
	}
	
	public class CoinBundle implements Cloneable {
		/**
		 * Reference to the parent cash register.
		 */
		private final CashRegister register;
		/**
		 * How many coins of each denomination we have.
		 */
		private final int[] coins;
		/**
		 * Represents the number of coins in the bundle. It 
		 * could be calculated by iterating through the coins 
		 * array, but we'll track it to speedup change calculations 
		 * at the expense of a bit of memory.
		 */
		private int coin_count;
		/**
		 * Running total of the cash amount in the bundle.
		 */
		private long total;
		/**
		 * Object used for locking this instance for synchronization purposes.
		 */
		private final Object lock;
		
		/**
		 * Creates an instance of an object that represents a collection 
		 * or bundle of coins.
		 */
		public CoinBundle() {
			this.register = CashRegister.this;
			this.lock = new Object();
			this.coins = new int[denominations.length];
			this.total = 0L;
		}
		
		/**
		 * Be careful calling this ensure that you input all the counts for 
		 * every unique denomination in ascending order. If you don't, 
		 * then your values can be off.
		 * 
		 * @param denomination_counts A list of counts, 1 per possible denomination.
		 */
		public CoinBundle(int...denomination_counts) {
			this();
			
			if (denomination_counts == null || denomination_counts.length != denominations.length)
				throw new IllegalArgumentException("denomination_counts must have the same number of entries as the corresponding register's denominations");
			
			for(int i = 0; i < denomination_counts.length; ++i) {
				coins[i] = denomination_counts[i];
				total += (denominations[i] * denomination_counts[i]);
			}
		}
		
		/**
		 * Retrieves the total current amount in the bundle at the time of the call.
		 * 
		 * @return The total current bundle amount.
		 */
		public long getTotal() {
			synchronized (getLock()) {
				return total;
			}
		}
		
		/**
		 * Retrieves the total number of coins in the bundle irregardless of their 
		 * denomination.
		 * 
		 * @return The total current coin count.
		 */
		public int getCoinCount() {
			synchronized (getLock()) {
				return coin_count;
			}
		}
		
		/**
		 * Retrieves an object used for synchronization purposes.
		 * 
		 * @return An object for synchronization purposes.
		 */
		public Object getLock() {
			return lock;
		}
		
		/**
		 * Creates an exact copy of the coins.
		 * 
		 * @return A new integer array containing the bundle's coin spread.
		 */
		public int[] copyCoins() {
			return copy(coins);
		}

		/**
		 * Creates a copy of this object.
		 * 
		 * @return A deep copy of this object.
		 * @throws CloneNotSupportedException 
		 */
		@Override
		protected Object clone() throws CloneNotSupportedException {
			return duplicate();
		}
		
		/**
		 * Creates a copy of this object.
		 * 
		 * @return A deep copy of this object.
		 */
		public CoinBundle duplicate() {
			CoinBundle bundle = register.createCoinBundle(coins);
			bundle.coin_count = coin_count;
			bundle.total = total;
			return bundle;
		}
		
		/**
		 * Creates a string representing the state of the bundle.
		 * 
		 * @return A string representing the state of the bundle.
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(256);
			sb.append("Total: $");
			sb.append(BigDecimal.valueOf(getTotal()).divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP).toPlainString());
			sb.append("; ");
			for(int i = 0; i < denominations.length; ++i) {
				if (i > 0)
					sb.append(", ");
				sb.append('[');
				sb.append(Integer.toString(denominations[i]));
				sb.append("]: ");
				sb.append(Integer.toString(coins[i]));
			}
			return sb.toString();
		}
		
		/**
		 * Forces a recalculation of the total based on the current coin spread.
		 * 
		 * @return The total value based on the current coin spread.
		 */
		public long recalculateTotal() {
			long recalc_total = 0L;
			synchronized (getLock()) {
				for(int i = 0; i < coins.length; ++i) {
					recalc_total += (coins[i] * denominations[i]);
				}
				this.total = recalc_total;
			}
			return recalc_total;
		}
		
		/**
		 * Forces a recalculation of the total number of coins in this bundle.
		 * 
		 * @return The total number of coins in the bundle.
		 */
		public int recalculateCoinCount() {
			int recalc_coin_count = 0;
			synchronized (getLock()) {
				for(int i = 0; i < coins.length; ++i) {
					recalc_coin_count += coins[i];
				}
				this.coin_count = recalc_coin_count;
			}
			return recalc_coin_count;
		}
		
		/**
		 * Adds multiple coins of the same denomination to the bundle.
		 * 
		 * @param denomination The denomination of the coin you wish to add.
		 * @param count The number of coins you wish to add.
		 * @return True if the operation was successful.
		 */
		public boolean add(int denomination, int count) {
			if (count == 0) {
				return true;
			}

			if (count < 0 || !isValidCoin(denomination)) {
				return false;
			}

			int index;
			synchronized (getLock()) {
				if ((index = indexOfDenomination(denomination)) < 0) {
					return false;
				}
				coins[index] += count;
				coin_count += count;
				total += (denomination * count);
			}

			return true;
		}
		
		/**
		 * Adds a bundle of coins to this one. It does NOT automatically 
		 * remove the passed bundle's coins.
		 * 
		 * @param bundle The bundle whose coins you want added to this instance's.
		 * @return True if the operation was successful.
		 */
		public boolean add(CoinBundle bundle) {
			if (bundle == null)
				throw new NullPointerException("bundle is null");
			if (register != bundle.register)
				throw new IllegalArgumentException("bundle must be created by the same cash register as this instance");
			
			//Create a copy of the coins locking the bundle first.
			//Might need further consideration of the ramifications if 
			//multiple registers were to try and access this bundle at 
			//the same time.
			int[] bundle_coins;
			synchronized (bundle.getLock()) {
				bundle_coins = bundle.copyCoins();
			}
			
			//Nothing to do if the bundle is empty.
			if (bundle_coins == null || bundle_coins.length <= 0)
				return true;
			
			//Add the bundle's amounts to our own.
			synchronized (getLock()) {
				for(int i = 0; i < bundle_coins.length; ++i) {
					coins[i] += bundle_coins[i];
					coin_count += bundle_coins[i];
					total += (denominations[i] * bundle_coins[i]);
				}
			}
			
			return true;
		}
		
		/**
		 * Removes multiple coins of the same denomination from this coin bundle.
		 * 
		 * @param denomination The denomination of the coin you wish to remove.
		 * @param count The number of coins you wish to remove.
		 * @return True if the operation was successful.
		 */
		public boolean remove(int denomination, int count) {
			if (count == 0) {
				return true;
			}

			if (count < 0 || !isValidCoin(denomination)) {
				return false;
			}

			int index;
			synchronized (getLock()) {
				if ((index = indexOfDenomination(denomination)) < 0) {
					return false;
				}
				//Don't allow a negative inventory.
				if (coins[index] < count) {
					return false;
				}
				coins[index] -= count;
				coin_count -= count;
				total -= (denomination * count);
			}

			return true;
		}
		
		/**
		 * Removes a bundle of coins from this one.
		 * 
		 * @param bundle The bundle whose coins you want removed from this instance's.
		 * @return True if the operation was successful.
		 */
		public boolean remove(CoinBundle bundle) {
			if (bundle == null)
				throw new NullPointerException("bundle is null");
			if (register != bundle.register)
				throw new IllegalArgumentException("bundle must be created by the same cash register as this instance");
			
			//Create a copy of the coins locking the bundle first.
			//Might need further consideration of the ramifications if 
			//multiple registers were to try and access this bundle at 
			//the same time.
			int[] bundle_coins;
			synchronized (bundle.getLock()) {
				bundle_coins = bundle.copyCoins();
			}
			
			//Nothing to do if the bundle is empty.
			if (bundle_coins == null || bundle_coins.length <= 0)
				return true;
			
			//Remove the bundle's amounts from our own.
			int tmp_coin_count;
			synchronized (getLock()) {
				for(int i = 0; i < bundle_coins.length; ++i) {
					tmp_coin_count = Math.max(0, Math.min(coins[i], bundle_coins[i]));
					coins[i] -= tmp_coin_count;
					coin_count -= tmp_coin_count;
					total -= (denominations[i] * tmp_coin_count);
				}
			}
			
			return true;
		}
		
		/**
		 * Resets this bundle to a 0-coin state.
		 * 
		 * @return True if the operation was successful.
		 */
		public boolean clear() {
			synchronized (getLock()) {
				for(int i = 0; i < coins.length; ++i)
					coins[i] = 0;
				coin_count = 0;
				total = 0;
			}
			return true;
		}
	}
}
