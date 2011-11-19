package com.guidewire.wordy.impl;

import com.guidewire.wordy.IBoard;
import com.guidewire.wordy.IWordy;
import com.guidewire.wordy.util.FileUtil;
import com.guidewire.wordy.util.LineBlock;
import com.guidewire.wordy.util.StringUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import junit.framework.TestCase;

import java.util.List;

public class WordyImplTest extends TestCase {
	//<editor-fold defaultstate="collapsed" desc="Utilities">
	private static final List<String> COMPLETE_WORD_LIST = createCompleteWordList();
	private static List<String> createCompleteWordList() {
		try {
			final List<String> lst = new ArrayList<String>(114000);

			FileUtil.readLines(new File("CROSSWD.TXT"), new LineBlock() {
				@Override
				public boolean run(String line) {
					lst.add(StringUtil.normalizeWord(line));
					return true;
				}
			});

			return lst;
		} catch(IOException e) {
			return null;
		}
	}
	
	private static WordyImpl createGame(String... known_board) {
		assertNotNull(known_board);

		final IWordy wordy = WordyImpl.createKnownGame(known_board);
		assertNotNull(wordy);
		
		final IBoard board = wordy.generateNewBoard();
		assertNotNull(board);
		
		return (WordyImpl)wordy;
	}
	//</editor-fold>
	
	public void testScoreWordsCountsWordsThatAreBothValidAndInBoard() {
		// 0
		// Test a game that does nothing.
		assertEquals(
			0, createGame(
				"----", 
				"----",
				"----", 
				"---A"
			).scoreWords(
				"",
				"A"
			)
		);
		
		// 2: "" and "ow" are both worth 0 points. "how" and "show" are each worth 1 point.
		// A game with very few valid words.
		assertEquals(
			2, createGame(
				"S---", 
				"-H--",
				"--O-", 
				"---W"
			).scoreWords(
				"",
				"ow",
				"how", 
				"show"
			)
		);
		
		// 1: "dear" is a 4-letter word worth 1 point.
		// A possible random board, but the player hasn't found all possible words.
		assertEquals(
			1, createGame(
				"abcd", 
				"deae",
				"zzrf", 
				"zzzz"
			).scoreWords(
				"dear"
			)
		);
		
		// 11: "unceremoniously" is a 15-letter word worth 11 points.
		// A single word snaking around that almost fills the grid.
		assertEquals(
			11, createGame(
				"unce", 
				"omer",
				"niou", 
				"-yls"
			).scoreWords(
				"unceremoniously"
			)
		);
		
		// 0: "unceremoniously" is a 15-letter word worth 11 points, but one letter is out of place.
		assertEquals(
			0, createGame(
				"unce", 
				"omer",
				"niou", 
				"-lys"
			).scoreWords(
				"unceremoniously"
			)
		);
		
		// 11: "unceremoniously" is a 15-letter word worth 11 points.
		// A single word that is very difficult to find.
		assertEquals(
			11, createGame(
				"erem", 
				"ycno",
				"-lni", 
				"suou"
			).scoreWords(
				"unceremoniously"
			)
		);
		
		// 3373: There's a lot of matches.
		// A board that has many, many matches.
		assertEquals(
			3373, createGame(
				"SERS", 
				"PATG",
				"LINE", 
				"SERS"
			).scoreWords(
				"AIL", "AILS", "AIN", "AINE", "AINS", "AIR", "AIRN", "AIRNS", "AIRS", "AIS", "AISLE", "AIT", "AITS", "ALE", 
				"ALES", "ALIEN", "ALIENER", "ALIENERS", "ALIENS", "ALINE", "ALINER", "ALINERS", "ALINES", "ALIT", "ALP", "ALPINE", 
				"ALPINES", "ALPS", "ANE", "ANES", "ANGER", "ANGERS", "ANGST", "ANI", "ANIL", "ANILE", "ANILS", "ANIS", "ANISE", 
				"ANT", "ANTE", "ANTES", "ANTI", "ANTIS", "ANTRE", "ANTRES", "ANTS", "APE", "APER", "APERS", "APES", "APING", "APLITE", 
				"APLITES", "APSE", "ARE", "ARES", "ARETE", "ARETES", "ARGENT", "ARGENTS", "ARS", "ART", "ARTERIES", "ARTIER", "ARTS", 
				"ASP", "ASPER", "ASPERGES", "ASPERS", "ASPIRE", "ASPIRES", "ASPIS", "ATE", "ATES", "EAR", "EARS", "EAT", "EATEN", 
				"EATER", "EATERIES", "EATERS", "EATING", "EATINGS", "EATS", "EGRET", "EGRETS", "ELAIN", "ELAINS", "ELAN", "ELANS", 
				"ELAPINE", "ELAPSE", "ELATE", "ELATER", "ELATERIN", "ELATERINS", "ELATERS", "ELATES", "ELATING", "ELITE", "ELITES", 
				"ELS", "ENATE", "ENATES", "ENG", "ENGRAIL", "ENGRAILS", "ENGS", "ENISLE", "ENS", "ENTAIL", "ENTAILER", "ENTAILERS", 
				"ENTAILS", "ENTER", "ENTERA", "ENTERAL", "ENTERS", "ENTIA", "ENTIRE", "ENTIRES", "ENTRAILS", "ENTRAP", "ENTRAPS", 
				"ERA", "ERAS", "ERE", "ERG", "ERGS", "ERN", "ERNE", "ERNES", "ERNS", "ERS", "ERST", "ESPALIER", "ESPALIERS", "ESPIAL", 
				"ESPIALS", "ESPIES", "ETA", "ETAPE", "ETAPES", "ETAS", "ETERNAL", "ETERNALS", "ETERNE", "ETERNISE", "ETNA", "ETNAS", 
				"GENE", "GENES", "GENIAL", "GENIE", "GENIES", "GENIP", "GENIPS", "GENITAL", "GENITALS", "GENRE", "GENRES", "GENS", 
				"GENT", "GENTES", "GENTIL", "GENTILE", "GENTILES", "GENTS", "GERENT", "GERENTS", "GET", "GETS", "GNAR", "GNARS", 
				"GNAT", "GNATS", "GRAIL", "GRAILS", "GRAIN", "GRAINER", "GRAINERS", "GRAINS", "GRANITE", "GRANITES", "GRANT", "GRANTER", 
				"GRANTERS", "GRANTS", "GRAPE", "GRAPES", "GRAPIER", "GRAPLIN", "GRAPLINE", "GRAPLINES", "GRAPLINS", "GRASP", "GRAT", 
				"GRATE", "GRATER", "GRATERS", "GRATES", "GRATIN", "GRATINS", "GRATIS", "GREAT", "GREATEN", "GREATENS", "GREATER", "GREATS", 
				"INGRATE", "INGRATES", "INS", "INSET", "INSETS", "INTEGRAL", "INTEGRALS", "INTER", "INTERS", "IRE", "IRES", "ISLE", "ITS", 
				"LAIN", "LAIR", "LAIRS", "LANE", "LANES", "LANG", "LAP", "LAPIN", "LAPINS", "LAPIS", "LAPS", "LAPSE", "LAPSER", "LAPSERS", 
				"LAR", "LARES", "LARGE", "LARGER", "LARGES", "LARS", "LAS", "LASE", "LASER", "LASERS", "LAT", "LATE", "LATEN", "LATENS", 
				"LATER", "LATI", "LATS", "LEI", "LEIS", "LENES", "LENIS", "LENS", "LENSE", "LENT", "LIANE", "LIANES", "LIANG", "LIANGS", 
				"LIAR", "LIARS", "LIE", "LIEN", "LIENS", "LIER", "LIERNE", "LIERNES", "LIERS", "LIES", "LIN", "LINE", "LINER", "LINERS", 
				"LINES", "LING", "LINGER", "LINGERS", "LINGS", "LINS", "LINT", "LINTER", "LINTERS", "LINTS", "LIP", "LIPASE", "LIPS", 
				"LIRE", "LIS", "LIT", "LITAS", "LITER", "LITERS", "LITRE", "LITRES", "LITS", "NAE", "NAIL", "NAILER", "NAILERS", "NAILS", 
				"NAP", "NAPE", "NAPES", "NAPS", "NARES", "NATES", "NEREIS", "NET", "NETS", "NIL", "NILS", "NIP", "NIPA", "NIPAS", "NIPS", 
				"NIT", "NITER", "NITERS", "NITRE", "NITRES", "NITS", "PAIL", "PAILS", "PAIN", "PAINS", "PAINT", "PAINTER", "PAINTERS", 
				"PAINTS", "PAIR", "PAIRS", "PAISE", "PAL", "PALE", "PALER", "PALES", "PALIER", "PALING", "PALINGS", "PALS", "PAN", 
				"PANE", "PANEL", "PANELS", "PANES", "PANG", "PANGS", "PANIER", "PANIERS", "PANS", "PANT", "PANTIE", "PANTIES", "PANTILE", 
				"PANTILES", "PANTS", "PAR", "PARE", "PARES", "PARGE", "PARGES", "PARGET", "PARGETS", "PARS", "PART", "PARTIES", "PARTING", 
				"PARTINGS", "PARTNER", "PARTNERS", "PARTS", "PAS", "PASE", "PAT", "PATE", "PATEN", "PATENS", "PATER", "PATERS", "PATES", 
				"PATIN", "PATINE", "PATINES", "PATINS", "PATS", "PEA", "PEAL", "PEALING", "PEALS", "PEAN", "PEANS", "PEAR", "PEARS", "PEART", 
				"PEARTER", "PEAS", "PEAT", "PEATIER", "PEATS", "PER", "PERT", "PERTAIN", "PERTAINS", "PERTER", "PES", "PET", "PETAL", 
				"PETALINE", "PETALS", "PETER", "PETERING", "PETERS", "PETS", "PIA", "PIAL", "PIAN", "PIANS", "PIAS", "PIE", "PIER", "PIERS", 
				"PIES", "PILAR", "PILE", "PILES", "PILSENER", "PILSENERS", "PIN", "PINA", "PINAS", "PINE", "PINES", "PINETA", "PING", 
				"PINGER", "PINGERS", "PINGS", "PINS", "PINT", "PINTA", "PINTAS", "PINTS", "PIRN", "PIRNS", "PIS", "PIT", "PITA", "PITAS", 
				"PITS", "PLAIN", "PLAINER", "PLAINS", "PLAINT", "PLAINTS", "PLAIT", "PLAITER", "PLAITERS", "PLAITS", "PLAN", "PLANE", 
				"PLANER", "PLANERS", "PLANES", "PLANET", "PLANETS", "PLANS", "PLANT", "PLANTER", "PLANTERS", "PLANTS", "PLAT", "PLATE", 
				"PLATEN", "PLATENS", "PLATER", "PLATERS", "PLATES", "PLATIER", "PLATIES", "PLATING", "PLATINGS", "PLATS", "PLENA", 
				"PLIANT", "PLIE", "PLIER", "PLIERS", "PLIES", "RAIL", "RAILER", "RAILERS", "RAILS", "RAIN", "RAINS", "RAISE", "RAISER", 
				"RAISERS", "RALE", "RALES", "RAN", "RANG", "RANGE", "RANGER", "RANGERS", "RANGES", "RANI", "RANIS", "RANT", "RANTER", 
				"RANTERS", "RANTS", "RAP", "RAPE", "RAPES", "RAPIER", "RAPIERS", "RAPINE", "RAPINES", "RAPING", "RAPS", "RAS", "RASE", 
				"RASP", "RASPIER", "RASPING", "RAT", "RATE", "RATER", "RATERS", "RATES", "RATINE", "RATINES", "RATING", "RATINGS", "RATS", 
				"REAL", "REALER", "REALES", "REALISE", "REALISER", "REALISERS", "REALS", "REAP", "REAPING", "REAPS", "REGNA", "REGNAL", 
				"REGRANT", "REGRANTS", "REGRATE", "REGRATES", "REGRET", "REGRETS", "REI", "REIN", "REINS", "REINTER", "REINTERS", "REIS", 
				"RELAPSE", "RELAPSER", "RELAPSERS", "RELATE", "RELATER", "RELATERS", "RELATES", "RELATING", "RELIANT", "RELINE", "RELINES", 
				"RELIT", "RENAL", "RENT", "RENTAL", "RENTALS", "RENTE", "RENTER", "RENTERS", "RENTES", "RENTS", "REP", "REPAINT", "REPAINTS", 
				"REPAIR", "REPAIRS", "REPIN", "REPINE", "REPINER", "REPINERS", "REPINES", "REPINS", "REPLAN", "REPLANS", "REPLANT", "REPLANTS", 
				"REPLATE", "REPLATES", "REPLATING", "REPLIER", "REPLIERS", "REPLIES", "REPS", "RES", "RESAIL", "RESAILS", "RESALE", "RESALES", 
				"RESIN", "RESINATE", "RESINATES", "RESINS", "RESPIRE", "RESPIRES", "RESPITE", "RESPITES", "RET", "RETAIL", "RETAILER", 
				"RETAILERS", "RETAILS", "RETAIN", "RETAINER", "RETAINERS", "RETAINS", "RETE", "RETENE", "RETENES", "RETIA", "RETIAL", 
				"RETIE", "RETIES", "RETINA", "RETINAE", "RETINAL", "RETINALS", "RETINAS", "RETIRE", "RETIRES", "RETRAIN", "RETRAINS", "RETRAL", 
				"RETS", "RIAL", "RIALS", "RIANT", "RIEL", "RIELS", "RILE", "RILES", "RIN", "RING", "RINGS", "RINS", "RINSE", "RIP", "RIPE", 
				"RIPER", "RIPES", "RIPS", "RISE", "RISEN", "RITE", "RITES", "SAE", "SAIL", "SAILER", "SAILERS", "SAILS", "SAIN", "SAINS", 
				"SAINT", "SAINTS", "SAL", "SALE", "SALES", "SALIENT", "SALIENTS", "SALINE", "SALINES", "SALP", "SALPINGES", "SALS", "SANE", 
				"SANER", "SANES", "SANG", "SANGER", "SANGERS", "SANIES", "SANS", "SANTIR", "SANTIRS", "SAP", "SAPIENS", "SAPIENT", "SAPLING", 
				"SAPLINGS", "SARGE", "SARGES", "SAT", "SATE", "SATES", "SATI", "SATIN", "SATING", "SATINS", "SATIRE", "SATIRES", "SATIS", 
				"SEA", "SEAL", "SEALER", "SEALERS", "SEALING", "SEALS", "SEAR", "SEARS", "SEAT", "SEATER", "SEATERS", "SEATING", "SEATINGS", 
				"SEATS", "SEGNI", "SEI", "SEINE", "SEINER", "SEINERS", "SEINES", "SEL", "SEN", "SENATE", "SENATES", "SENILE", "SENILES", 
				"SENSE", "SENT", "SENTI", "SEPAL", "SEPALINE", "SEPALS", "SEPIA", "SER", "SERA", "SERAI", "SERAIL", "SERAILS", "SERAIS", "SERAL", 
				"SERE", "SEREIN", "SERENATE", "SERES", "SERGE", "SERGES", "SERIAL", "SERIALS", "SERIATE", "SERIATES", "SERIES", "SERIN", "SERINE", 
				"SERINES", "SERING", "SERINS", "SERS", "SET", "SETA", "SETAE", "SETAL", "SETS", "SIAL", "SILANE", "SILANES", "SILENT", 
				"SILENTER", "SILENTS", "SIN", "SINE", "SINES", "SING", "SINGE", "SINGER", "SINGERS", "SINGES", "SINGS", "SINS", "SINTER", 
				"SINTERS", "SIP", "SIPE", "SIPES", "SIPS", "SIR", "SIRE", "SIREN", "SIRENS", "SIRES", "SIRS", "SIT", "SITAR", "SITARS", 
				"SITE", "SITES", "SITS", "SLAIN", "SLANG", "SLANGS", "SLANT", "SLANTS", "SLAP", "SLAPS", "SLAT", "SLATE", "SLATER", "SLATERS", 
				"SLATES", "SLATIER", "SLATING", "SLATINGS", "SLATS", "SLIER", "SLING", "SLINGER", "SLINGERS", "SLINGS", "SLIP", "SLIPE", 
				"SLIPES", "SLIPS", "SLIT", "SLITS", "SNAIL", "SNAILS", "SNAP", "SNAPS", "SNARE", "SNARES", "SNIP", "SNIPE", "SNIPER", "SNIPERS", 
				"SNIPES", "SNIPS", "SNIT", "SNITS", "SPA", "SPAE", "SPAIL", "SPAILS", "SPAIT", "SPAITS", "SPALE", "SPALES", "SPAN", "SPANG", 
				"SPANIEL", "SPANIELS", "SPANS", "SPAR", "SPARE", "SPARGE", "SPARGER", "SPARGERS", "SPARGES", "SPARS", "SPAT", "SPATE", 
				"SPATES", "SPATS", "SPEAN", "SPEANS", "SPEAR", "SPEARS", "SPIEL", "SPIELS", "SPIER", "SPIERS", "SPIES", "SPILE", "SPILES", 
				"SPIN", "SPINAL", "SPINALS", "SPINATE", "SPINE", "SPINEL", "SPINELS", "SPINES", "SPINET", "SPINETS", "SPINS", "SPIRE", "SPIRES", 
				"SPIT", "SPITAL", "SPITALS", "SPITE", "SPITES", "SPITS", "SPLAT", "SPLATS", "SPLENIA", "SPLENT", "SPLENTS", "SPLINE", "SPLINES", 
				"SPLINT", "SPLINTER", "SPLINTERS", "SPLINTS", "SPLIT", "SPLITS", "SRI", "SRIS", "STAIN", "STAINER", "STAINERS", "STAINS", 
				"STAIR", "STAIRS", "STALE", "STALER", "STALES", "STALING", "STANE", "STANES", "STANG", "STAPES", "STAPLE", "STAPLER", 
				"STAPLERS", "STAPLES", "STAPLING", "STAR", "STARE", "STARES", "STEAL", "STEALER", "STEALERS", "STEALING", "STEALS", "STEP", 
				"STEPS", "STERE", "STERES", "STERILE", "STERN", "STERNA", "STERNAL", "STERNS", "STIES", "STILE", "STILES", "STING", "STINGER", 
				"STINGERS", "STIPE", "STIPES", "STIR", "STIRS", "STRAIN", "STRAINER", "STRAINERS", "STRAINS", "STRANG", "STRANGE", "STRANGER", 
				"STRANGERS", "STRAP", "STRAPS", "STREP", "STREPS", "TAE", "TAIL", "TAILER", "TAILERS", "TAILS", "TAIN", "TAINS", "TALE", 
				"TALER", "TALERS", "TALES", "TALI", "TALIPES", "TAN", "TANG", "TANGS", "TANS", "TAP", "TAPE", "TAPER", "TAPERS", "TAPES", 
				"TAPING", "TAPIR", "TAPIRS", "TAPIS", "TAPS", "TAR", "TARE", "TARES", "TARGE", "TARGES", "TARS", "TAS", "TEA", "TEAL", "TEALS", 
				"TEAR", "TEARS", "TEAS", "TEG", "TEGS", "TEN", "TENAIL", "TENAILS", "TENIA", "TENIAE", "TENIAS", "TENS", "TEPA", "TEPAS", 
				"TERAI", "TERAIS", "TERN", "TERNE", "TERNES", "TERNS", "TIE", "TIER", "TIERS", "TIES", "TIL", "TILE", "TILER", "TILERS", 
				"TILES", "TILS", "TIN", "TINE", "TINES", "TING", "TINGE", "TINGES", "TINGS", "TINS", "TIP", "TIPS", "TIRE", "TIRES", 
				"TIS", "TRAIL", "TRAILER", "TRAILERS", "TRAILS", "TRAIN", "TRAINER", "TRAINERS", "TRAINS", "TRAIPSE", "TRANS", "TRAP", 
				"TRAPES", "TRAPS", "TREPAN", "TREPANG", "TREPANGS", "TREPANS"
			)
		);
	}

	public void testScoreWordsCountsDuplicatesOnlyOnce() {
		// 2: "" and "ow" are both worth 0 points. "how" and "show" are each worth 1 point.
		// A game with very few valid words and multiple duplicates.
		assertEquals(
			2, createGame(
				"S---", 
				"-H--",
				"--O-", 
				"---W"
			).scoreWords(
				"",
				"ow",
				"how", 
				"show", 
				"show", 
				"how", 
				"how", 
				"how", 
				"show"
			)
		);
	}

	public void testScoreWordsWithLeadingAndTrailingSpaces() {
		// 1
		assertEquals(
			1, createGame(
				"T---", 
				"-E--",
				"--S-", 
				"---T"
			).scoreWords(
				"  test"
			)
		);
		
		// 1
		assertEquals(
			1, createGame(
				"T---", 
				"-E--",
				"--S-", 
				"---T"
			).scoreWords(
				"test  "
			)
		);
		
		// 1
		assertEquals(
			1, createGame(
				"T---", 
				"-E--",
				"--S-", 
				"---T"
			).scoreWords(
				"  test  "
			)
		);
		
		// 1
		assertEquals(
			1, createGame(
				"T---", 
				"-E--",
				"--S-", 
				"---T"
			).scoreWords(
				"  test",
				"test  ", 
				"  test  "
			)
		);
	}

	public void testScoreWordsDoesNotCountWordsThatAreValidButNotInBoard() {
		// 1: "test"
		assertEquals(
			1, createGame(
				"T---", 
				"-E--",
				"--S-", 
				"---T"
			).scoreWords(
				"what",
				"a", 
				"lovely", 
				"test",
				"this",
				"is"
			)
		);
		
		// 4: "test", "lovely"
		assertEquals(
			4, createGame(
				"TYEV", 
				"-ELO",
				"--SL", 
				"IS-T"
			).scoreWords(
				"what",
				"a", 
				"lovely", 
				"test",
				"this",
				"is"
			)
		);
	}
	
	// Add your own test cases here
}
