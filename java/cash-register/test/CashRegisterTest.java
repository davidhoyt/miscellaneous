
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author David Hoyt <dhoyt@hoytsoft.org>
 */
public final class CashRegisterTest {

	public CashRegisterTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testBasicFunctions() {
		assertNotNull(new CashRegister());
		assertNotNull(new CashRegister(USCurrency.INSTANCE));
		assertNotNull(new CashRegister(USCurrency.DENOMINATIONS));
		
		CashRegister register = new CashRegister(USCurrency.DENOMINATIONS);
		assertTrue(register.addCoinRoll(10, 5));
		assertTrue(register.removeCoinRoll(10, 2));
		assertTrue(register.removeCoinRoll(10, 1));
		assertFalse(register.removeCoinRoll(10, 3));
		assertTrue(register.removeCoinRoll(10, 2));
		
		assertTrue(register.addCoinRoll(USCurrency.PENNY, 10));
		assertTrue(register.addCoinRoll(USCurrency.NICKEL, 10));
		assertTrue(register.addCoinRoll(USCurrency.DIME, 100));
		assertTrue(register.addCoinRoll(USCurrency.QUARTER, 100));
		
		assertEquals(3560L, register.getTotalAmount());
		
		CashRegister.CoinBundle change;
		
		//Sell something for $0.60, buy it for $0.90, so we expect $0.30 in change.
		assertNotNull(change = register.sale(60, 90));
		assertEquals(30L, change.getTotal());
		assertEquals(3530L, register.getTotalAmount());
		
		//Reset to $1.10
		assertTrue(register.clearCoins());
		assertEquals(0L, register.getTotalAmount());
		assertTrue(register.addCoinRoll(USCurrency.PENNY, 10));
		assertTrue(register.addCoinRoll(USCurrency.NICKEL, 5));
		assertTrue(register.addCoinRoll(USCurrency.DIME, 5));
		assertTrue(register.addCoinRoll(USCurrency.QUARTER, 1));
		assertEquals(110L, register.getTotalAmount());
		
		//Reduce by $0.05
		assertNotNull(change = register.sale(25, 30));
		assertEquals(1L, change.getCoinCount());       //1 nickel
		assertEquals(5L, change.getTotal());           //$0.05 change
		assertEquals(105L, register.getTotalAmount()); //$1.05 left
		
		//Reduce by $0.00
		assertNotNull(change = register.sale(25, 25));
		assertEquals(0L, change.getCoinCount());       //No change
		assertEquals(0L, change.getTotal());           //$0.00 change
		assertEquals(105L, register.getTotalAmount()); //$1.05 left
		
		//Reduce by $0.25
		assertNotNull(change = register.sale(1, 26));
		assertEquals(1L, change.getCoinCount());       //1 quarter
		assertEquals(25L, change.getTotal());          //$0.25 change
		assertEquals(80L, register.getTotalAmount());  //$0.80 left
		
		//Reduce by $0.25
		//At this point, there's no more quarters left.
		//We can do it in 2 dimes + 1 nickel.
		assertNotNull(change = register.sale(1, 26));
		assertEquals(3L, change.getCoinCount());       //2 dimes, 1 nickel
		assertEquals(25L, change.getTotal());          //$0.25 change
		assertEquals(55L, register.getTotalAmount());  //$0.55 left
		
		//Reduce by $0.37
		//We can do it with 3 dimes + 1 nickel + 2 pennies.
		assertNotNull(change = register.sale(1, 38));
		assertEquals(6L, change.getCoinCount());       //3 dimes, 1 nickel, 2 pennies
		assertEquals(37L, change.getTotal());          //$0.25 change
		assertEquals(18L, register.getTotalAmount());  //$0.18 left
		
		//Attempt to reduce by $0.19.
		//We expect this to fail since there's now not enough 
		//coins in the inventory for it to succeed.
		try {
			register.sale(1, 20);
			assertTrue(false);
		} catch(IllegalStateException ise) {
			assertTrue(true);
		} catch(Throwable t) {
			assertTrue(false);
		}
		assertEquals(18L, register.getTotalAmount());  //$0.18 left
		
		//Change denomination set to something entirely different.
		register = new CashRegister(new int[] { 1, 2, 3 });
		//Reset to $0.12
		register.addCoinBundle(2, 2, 2);
		
		//Reduce by $0.7
		//We can do it with 2x3, 1x1
		assertNotNull(change = register.sale(1, 8));
		assertEquals(3L, change.getCoinCount());       //2x3, 1x1
		assertEquals(7L, change.getTotal());           //$0.07 change
		assertEquals(5L, register.getTotalAmount());   //$0.05 left
		
		//Change denomination set to something entirely different.
		register = new CashRegister(new int[] { 1, 5, 10, 21, 25 });
		//Reset to $1.24
		register.addCoinBundle(2, 2, 2, 2, 2);
		
		//Reduce by $0.64
		//We can do it with 2x21, 2x10, 2x1
		assertNotNull(change = register.sale(1, 65));
		assertEquals(6L, change.getCoinCount());        //2x21, 2x10, 2x1
		assertEquals(64L, change.getTotal());           //$0.07 change
		assertEquals(60L, register.getTotalAmount());   //$0.60 left
		
		//Change denomination set to something entirely different.
		register = new CashRegister(new int[] { 1, 10, 25 });
		//Reset to $0.60
		register.addCoinBundle(5, 3, 1);
		
		//Reduce by $0.30
		//We can do it with 2x21, 2x10, 2x1
		assertNotNull(change = register.sale(0, 30));
		assertEquals(3L, change.getCoinCount());        //3x10
		assertEquals(30L, change.getTotal());           //$0.07 change
		assertEquals(30L, register.getTotalAmount());   //$0.30 left
		
		//Reduce by $0.30
		//At this point, all the dimes are gone.
		//We have to use pennies and quarters.
		//We can do it with 1x25, 5x1
		assertNotNull(change = register.sale(0, 30));
		assertEquals(6L, change.getCoinCount());        //1x25, 5x1
		assertEquals(30L, change.getTotal());           //$0.30 change
		assertEquals(0L, register.getTotalAmount());    //$0.00 left
	}
	
	@Test
	public void testCoinBundle() {
		final CashRegister register1 = new CashRegister();
		final CashRegister register2 = new CashRegister();
		final CashRegister.CoinBundle bundle1 = register1.createCoinBundle();
		final CashRegister.CoinBundle bundle2 = register2.createCoinBundle();
		final CashRegister.CoinBundle bundle3 = register1.createCoinBundle(10, 10, 10, 10, 10);
		long bundle1_expected_total = 0L;
		long bundle2_expected_total = 0L;
		long bundle3_expected_total = (1L * 10L) + (5L * 10L) + (10L * 10L) + (25L * 10L) + (50L * 10L);
		
		assertEquals(bundle1_expected_total, bundle1.getTotal());
		assertEquals(bundle2_expected_total, bundle2.getTotal());
		assertEquals(bundle3_expected_total, bundle3.getTotal());
		
		try {
			//Shouldn't work b/c we can only add bundles created by the same register.
			bundle1.add(bundle2);
			assertTrue(false);
		} catch(IllegalArgumentException iae) {
		}
		
		//Add bundle3's coins to bundle1. bundle3's coins are not automatically removed!
		assertTrue(bundle1.add(bundle3));
		assertEquals(bundle1_expected_total = bundle3_expected_total, bundle1.getTotal());
		
		//Remove bundle3's coins by the same amount we just added to bundle1.
		//We expect bundle3 to have nothing left.
		assertTrue(bundle3.remove(bundle1));
		assertEquals(bundle3_expected_total = 0L, bundle3.getTotal());
		
		//Adding nothing -- should be same as before.
		bundle1.add(bundle3);
		assertEquals(bundle1_expected_total, bundle1.getTotal());
		
		assertTrue(bundle1.add(USCurrency.PENNY, 3));
		assertEquals(bundle1_expected_total += 3, bundle1.getTotal());
		
		assertTrue(bundle1.add(USCurrency.NICKEL, 3));
		assertEquals(bundle1_expected_total += USCurrency.NICKEL * 3, bundle1.getTotal());
	}
}
