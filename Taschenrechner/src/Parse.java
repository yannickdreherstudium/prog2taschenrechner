import java.util.regex.Pattern;

public class Parse {

	/**
	 * 
	 * @param input
	 *            Der Input String, der Analysiert werden soll
	 * @throws FailException
	 */
	public static String start(String start) throws FailException {
		testeeingabe(start);
		testeklammer(start);
		start = "(" + order(start) + ")";
		start = split(new StringBuilder(start)).toString();
		return start.toString();
	}

	private static void testeeingabe(String check) throws FailException {
		if (check.equals("") || removeleere(check).equals("") || removeleere(check).equals(" ")) {
			Main.stop("Du glaubst ernsthaft, damit kommst du druch?", 000, check);
		}
	}

	/**
	 * Berechnet den String rekursiv nach den Klammerregeln
	 * 
	 * @param start
	 *            StringBuilder
	 * @return StringBuilder
	 * @throws FailException
	 */
	private static StringBuilder split(StringBuilder start) throws FailException {
		Object[] clip = new Object[20];
		for (int i = 1; i < start.length(); i++) {
			if (start.charAt(i) == '(') {
				String temp = split(new StringBuilder(start.substring(i))).toString();
				int klammer = 0, place = 0;
				for (int k = i; k < start.length(); k++) {
					if (start.charAt(k) == '(') {
						klammer--;
					} else {
						if (start.charAt(k) == ')') {
							klammer++;
						}
					}
					if (klammer == 0) {
						place = k + 1;
						break;
					}
				}
				String alt = start.substring(i, place).toString();
				start = new StringBuilder(start.toString().replace(alt, temp));
				start = new StringBuilder(order(start.toString()));
			}
			if (start.charAt(i) == ')') {
				String first = start.substring(1, i);
				try {
					Double.parseDouble(first);
					return new StringBuilder(first);
				} catch (java.lang.NumberFormatException e) {
				}
				String[] firstsplit = first.split(" ");
				for (int j = 0; j < firstsplit.length; j++) {
					clip[j] = search(firstsplit[j]);
				}
				return new StringBuilder(resort(clip).toString());
			}
		}
		return new StringBuilder(start);
	}

	/**
	 * Entfernt Systaxfalsche Leerzeichen
	 * 
	 * @param check
	 *            String Input
	 * @return String
	 */
	private static String removeleere(String check) {
		char[] all = check.toCharArray();
		for (int i = 1; i < all.length; i++) {
			if ((all[i - 1] == ' ' && all[i] == ' ') || (all[i] == 'i' && all[i - 1] == 'i')
					|| (all[i - 1] == '(' && all[i] == ' ')) {
				for (int j = i; j < all.length - 1; j++) {
					all[j] = all[j + 1];
					if (j <= (all.length - 2)) {
						all[j + 1] = ' ';
					}
				}
			}
		}
		StringBuilder neu = new StringBuilder("");
		for (int j = 0; j < all.length; j++) {
			if (all[j] == ' ') {
				continue;
			} else {
				for (int k = j; k < all.length; k++) {
					neu.append(all[k]);
					continue;
				}
			}
			break;
		}
		System.out.println(neu.toString() + "_#");
		return neu.toString();
	}

	/**
	 * fuegt Systaxrichtige Leerzeichen hinzu
	 * 
	 * @param check
	 *            String Input
	 * @return String
	 */
	private static String order(String check) {
		check = removeleere(check);
		System.out.println(check);
		String[] all = check.split(" ");
		for (int i = 0; i < all.length; i++) {
			check = all[i];

			if (check.matches(Pattern.quote("+")) || check.matches(Pattern.quote("-"))
					|| check.matches(Pattern.quote("*")) || check.matches(Pattern.quote("/"))) {
				continue;
			}
			try {
				Double.parseDouble(check);
				continue;
			} catch (Exception e) {
				if (check.contains("+")) {
					all[i] = check.replace("+", " + ");

					// continue;
				}
				if (check.contains("-")) {
					all[i] = check.replace("-", " - ");
					// continue;
				}
				if (check.contains("*")) {
					try {
						all[i] = check.replaceAll(Pattern.quote("*"), " * ");
						all[i] = check.replace("*", " * ");
						// continue;
					} catch (Exception d) {
						System.out.println(d);

					}
				}
				if (check.contains("/")) {
					try {
						all[i] = check.replaceAll(Pattern.quote("/"), " / ");
						all[i] = check.replace("/", " / ");
					} catch (Exception f) {
						System.out.println(f);
					}
					// continue;
				}

			}

		}
		check = "";
		for (int j = 0; j < all.length; j++) {
			check = check + all[j].toString() + " ";
		}
		return (check.substring(0, check.length() - 1));
	}

	/**
	 * Teste nur, ob es gleich viele '(' wie ')' gibt
	 * 
	 * @param start
	 *            String
	 * @return Boolean
	 * @throws FailException
	 */
	private static boolean testeklammer(String start) throws FailException {
		int auf = 0, zu = 0, wechsel = 0;
		for (int i = 0; i < start.length(); i++) {
			if (start.charAt(i) == '(') {
				auf++;
				wechsel++;
			}
			if (start.charAt(i) == ')') {
				zu++;
				wechsel--;
			}
		}
		if (wechsel < 0) {
			Main.stop("Falsche Klammersetzung", 201, start);
		}
		if (auf == zu) {
			return true;
		} else {
			Main.stop("Falsche Klammersetzung", 202, start);
		}
		return false;
	}

	/**
	 * Teste auf Eigentschaften des Strings, wie z.B. ob es eine Dezimalzahl ist,
	 * ein Bruch etc.
	 * 
	 * @param check
	 *            der zu Analysierende String
	 * @return das gefundene Object
	 * @throws FailException
	 */
	private static Object search(String check) throws FailException {
		if (check == null) {
			return "error";
		}
		try {
			return Double.parseDouble(check);
		} catch (Exception e) {
		}
		if (check.contains("i")) {
			return readimag(check);
		}
		switch (check) {
		case "+":
			return "+";
		case "-":
			return "-";
		case "*":
			return "*";
		case "/":
			return "/";
		case " ":
			return null;
		case "":
			return null;
		default:
			if (check.contains("/")) {
				int zaehler = 0;
				int nenner = 0;
				String[] newbruch = check.split("/");
				if (newbruch.length == 2) {
					try {
						zaehler = Integer.parseInt(newbruch[0]);
						nenner = Integer.parseInt(newbruch[1]);
						return new Bruch(zaehler, nenner);
					} catch (NumberFormatException e) {
						Main.stop("In Bruechen stehen ganze Zahlen", 221, check);
					}
				} else {
					Main.stop("Kein echter Bruch", 222, check);
				}
			}
			Main.stop("kein Gueltiges Zeichen", 223, check);
			return null;
		}

	}

	/**
	 * testet alle Kombinationen, wie eine Imaginaere Zahl aufgabut ist
	 * 
	 * @param check
	 *            die zu ueberpruefende Zeichenkette
	 * @return die fertige Imaginaere Zahl
	 * @throws FailException
	 *             falls eine Konvetion nicht eingehalten wurde
	 */
	private static Komplex readimag(String check) throws FailException {
		double reell, imag;
		try {
			if (check.contains("+")) {
				String[] newimag = check.split("\\+");
				if (newimag.length == 2) {
					if (newimag[1].contains("i")) {
						reell = Double.parseDouble(newimag[0]);
						imag = Double.parseDouble(newimag[1].replaceAll("i", ""));
						return new Komplex(reell, imag);
					} else {
						reell = Double.parseDouble(newimag[1]);
						imag = Double.parseDouble(newimag[0].replaceAll("i", ""));
						return new Komplex(reell, imag);
					}
				} else {
					Main.stop("keine gueltige Imaginaere Zahl", 231, check);
				}
			} else {
				if (check.contains("-")) {
					String[] newimag = check.split("-");
					if (newimag.length == 2) {
						if (newimag[1].contains("i")) {
							reell = Double.parseDouble(newimag[0]);
							imag = Double.parseDouble(newimag[1].replaceAll("i", ""));
							return new Komplex(reell, imag * (-1));
						} else {
							reell = Double.parseDouble(newimag[1]);
							imag = Double.parseDouble(newimag[0].replaceAll("i", ""));
							return new Komplex(reell, imag * (-1));
						}
					} else {
						Main.stop("keine gueltige Imaginaere Zahl Zahl", 232, check);
					}
				} else {
					if (check.contains("i")) {
						String[] newimag = check.split("i");
						for (String string : newimag) {
							if (string == null) {
								continue;
							} else {
								try {
									imag = Double.parseDouble(string);
									return new Komplex(0, imag);
								} catch (Exception e) {
								}
							}
						}
					} else {
						Main.stop("keine gueltige Imaginaere Zahl Zahl", 233, check);
					}
				}
			}
			Main.stop("keine gueltige Imaginaere Zahl", 234, check);
		} catch (Exception e) {
			Main.stop("keine gueltige Imaginaere Zahl", 235, check);
		}
		return null;
	}

	/**
	 * Zaehlt die Elemente abzueglich 'null'
	 * 
	 * @param count
	 *            Object-Array
	 * @return Int-Anzahl der Elemente abzueglich derer, die 'null' sind
	 */
	private static int nullcounter(Object[] count) {
		int real = 0;
		for (int k = 0; count.length > k; k++) {
			if (count[k] != null) {
				real += 1;
			}
		}
		return real;
	}

	/**
	 * Testet, ob noch '*' oder '/' im Object-Array enthalten sind
	 * 
	 * @param count
	 *            Object-Array
	 * @return Boolean
	 */
	private static boolean punktcounter(Object[] count) {
		for (int h = 0; count.length > h; h++) {
			if (count[h].equals("*") || count[h].equals("/")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Testet, ob noch '+' oder '-' im Object-Array enthalten sind
	 * 
	 * @param count
	 *            Object-Array
	 * @return Boolean
	 */
	private static boolean strichcounter(Object[] count) {
		for (int h = 0; count.length > h; h++) {
			if (count[h].equals("+") || count[h].equals("-")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Entfernt alle 'null' -Objecte und "sortiert" das Array neu und kuerzt es auf
	 * die minimale Groesse
	 * 
	 * @param all
	 * @return Das verkleinerte Object-Array
	 */
	private static Object[] removenull(Object[] all) {
		Object[] allmini = new Object[nullcounter(all)];
		for (int l = 0; allmini.length > l; l++) {
			try {
				allmini[l] = all[l];
			} catch (Exception e) {
				break;
			}
		}
		return allmini;
	}

	static Object resort(Object[] all) throws FailException {
		// wenn das Array nur noch ein Element hat, muss es die L�sung sein
		if (all.length == 1) {
			return all[0];
		}
		// wenn nicht wird das Array genommen, kopiert und alle leeren eintraege
		// entfernt
		Object[] allmini = all;
		// das gekuerzte Array wird jetzt im sinne von punkt vor strich durchgegangen
		// und
		// berechnet
		Object result;
		while (punktcounter(removenull(allmini))) {
			allmini = removenull(allmini);
			for (int i = 0; allmini.length > i; i++) {
				result = decidepunkt(allmini, i);
				if (result != null) {
					allmini[i - 1] = result;
					for (int j = i; allmini.length > j; j++) {
						try {
							allmini[j] = allmini[j + 2];
						} catch (Exception e) {
							allmini[j] = null;
						}
					}
					break;
				} else {
					continue;
				}

			}
		}
		while (strichcounter(removenull(allmini))) {
			allmini = removenull(allmini);
			for (int i = 0; allmini.length > i; i++) {
				result = decidestrich(allmini, i);
				if (result != null) {
					allmini[i - 1] = result;
					for (int j = i; allmini.length > j; j++) {
						try {
							allmini[j] = allmini[j + 2];
						} catch (Exception e) {
							allmini[j] = null;
						}
					}
					break;
				} else {
					continue;
				}

			}
		}
		// das nun verkuerze Arry durchlauuft die berechnung nochmal
		return resort(removenull(allmini));
	}

	private static Object decidepunkt(Object[] all, int i) throws FailException {
		try {
			if (all[i] == null) {
				return null;
			}
			if (all[i].equals("*")) {
				switch (all[i - 1].getClass().getName()) {
				case "Bruch":
					switch (all[i + 1].getClass().getName()) {
					case "Komplex":
						return Calculate.mult((Bruch) all[i - 1], (Komplex) all[i + 1]);
					case "Bruch":
						return Calculate.mult((Bruch) all[i - 1], (Bruch) all[i + 1]);
					case "java.lang.Double":
						return Calculate.mult((Bruch) all[i - 1], (Double) all[i + 1]);
					default:
						Main.stop("Dicide Error", 251, all[i + 1]);
						break;
					}
					break;
				case "Komplex":
					switch (all[i + 1].getClass().getName()) {
					case "Komplex":
						return Calculate.mult((Komplex) all[i - 1], (Komplex) all[i + 1]);
					case "Bruch":
						return Calculate.mult((Komplex) all[i - 1], (Bruch) all[i + 1]);
					case "java.lang.Double":
						return Calculate.mult((Komplex) all[i - 1], (Double) all[i + 1]);
					default:
						Main.stop("Dicide Error", 252, all[i + 1]);
						break;
					}
				case "java.lang.Double":
					switch (all[i + 1].getClass().getName()) {
					case "Komplex":
						return Calculate.mult((Double) all[i - 1], (Komplex) all[i + 1]);
					case "Bruch":
						return Calculate.mult((Double) all[i - 1], (Bruch) all[i + 1]);
					case "java.lang.Double":
						return Calculate.mult((Double) all[i - 1], (Double) all[i + 1]);
					default:
						Main.stop("Dicide Error", 253, all[i + 1]);
						break;
					}
				default:
					Main.stop("Fataler Dicide Error", 254, "Error");
					break;
				}
			}
			if (all[i].equals("/")) {
				switch (all[i - 1].getClass().getName()) {
				case "Bruch":
					switch (all[i + 1].getClass().getName()) {
					case "Komplex":
						return Calculate.div((Bruch) all[i - 1], (Komplex) all[i + 1]);
					case "Bruch":
						return Calculate.div((Bruch) all[i - 1], (Bruch) all[i + 1]);
					case "java.lang.Double":
						return Calculate.div((Bruch) all[i - 1], (Double) all[i + 1]);
					default:
						Main.stop("Dicide Error", 255, all[i + 1]);
						break;
					}
					break;
				case "Komplex":
					switch (all[i + 1].getClass().getName()) {
					case "Komplex":
						return Calculate.div((Komplex) all[i - 1], (Komplex) all[i + 1]);
					case "Bruch":
						return Calculate.div((Komplex) all[i - 1], (Bruch) all[i + 1]);
					case "java.lang.Double":
						return Calculate.div((Komplex) all[i - 1], (Double) all[i + 1]);
					default:
						Main.stop("Dicide Error", 256, all[i + 1]);
						break;
					}
				case "java.lang.Double":
					switch (all[i + 1].getClass().getName()) {
					case "Komplex":
						return Calculate.div((Double) all[i - 1], (Komplex) all[i + 1]);
					case "Bruch":
						return Calculate.div((Double) all[i - 1], (Bruch) all[i + 1]);
					case "java.lang.Double":
						return Calculate.div((Double) all[i - 1], (Double) all[i + 1]);
					default:
						Main.stop("Dicide Error", 257, all[i + 1]);
						break;
					}
				default:
					Main.stop("Fataler Dicide Error", 258, "Error");
					break;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			Main.stop("Falsche Eingabe", 259, all[i]);
		}
		return null;
	}

	private static Object decidestrich(Object[] all, int i) throws FailException {
		try {
			if (all[i].equals("+")) {
				switch (all[i - 1].getClass().getName()) {
				case "Bruch":
					switch (all[i + 1].getClass().getName()) {
					case "Komplex":
						return Calculate.add((Bruch) all[i - 1], (Komplex) all[i + 1]);
					case "Bruch":
						return Calculate.add((Bruch) all[i - 1], (Bruch) all[i + 1]);
					case "java.lang.Double":
						return Calculate.add((Bruch) all[i - 1], (Double) all[i + 1]);
					default:
						Main.stop("Dicide Error", 261, all[i + 1]);
						break;
					}
					break;
				case "Komplex":
					switch (all[i + 1].getClass().getName()) {
					case "Komplex":
						return Calculate.add((Komplex) all[i - 1], (Komplex) all[i + 1]);
					case "Bruch":
						return Calculate.add((Komplex) all[i - 1], (Bruch) all[i + 1]);
					case "java.lang.Double":
						return Calculate.add((Komplex) all[i - 1], (Double) all[i + 1]);
					default:
						Main.stop("Dicide Error", 262, all[i + 1]);
						break;
					}
				case "java.lang.Double":
					switch (all[i + 1].getClass().getName()) {
					case "Komplex":
						return Calculate.add((Double) all[i - 1], (Komplex) all[i + 1]);
					case "Bruch":
						return Calculate.add((Double) all[i - 1], (Bruch) all[i + 1]);
					case "java.lang.Double":
						return Calculate.add((Double) all[i - 1], (Double) all[i + 1]);
					default:
						Main.stop("Dicide Error", 263, all[i + 1]);
						break;
					}
				default:
					Main.stop("Fataler Dicide Error", 264, "Error");
					break;
				}
			}
			if (all[i].equals("-")) {
				switch (all[i - 1].getClass().getName()) {
				case "Bruch":
					switch (all[i + 1].getClass().getName()) {
					case "Komplex":
						return Calculate.sub((Bruch) all[i - 1], (Komplex) all[i + 1]);
					case "Bruch":
						return Calculate.sub((Bruch) all[i - 1], (Bruch) all[i + 1]);
					case "java.lang.Double":
						return Calculate.sub((Bruch) all[i - 1], (Double) all[i + 1]);
					default:
						Main.stop("Dicide Error", 265, all[i + 1]);
						break;
					}
					break;
				case "Komplex":
					switch (all[i + 1].getClass().getName()) {
					case "Komplex":
						return Calculate.sub((Komplex) all[i - 1], (Komplex) all[i + 1]);
					case "Bruch":
						return Calculate.sub((Komplex) all[i - 1], (Bruch) all[i + 1]);
					case "java.lang.Double":
						return Calculate.sub((Komplex) all[i - 1], (Double) all[i + 1]);
					default:
						Main.stop("Dicide Error", 266, all[i + 1]);
						break;
					}
				case "java.lang.Double":
					switch (all[i + 1].getClass().getName()) {
					case "Komplex":
						return Calculate.sub((Double) all[i - 1], (Komplex) all[i + 1]);
					case "Bruch":
						return Calculate.sub((Double) all[i - 1], (Bruch) all[i + 1]);
					case "java.lang.Double":
						return Calculate.sub((Double) all[i - 1], (Double) all[i + 1]);
					default:
						Main.stop("Dicide Error", 267, all[i + 1]);
						break;
					}
				default:
					Main.stop("Fataler Dicide Error", 268, "Error");
					break;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			Main.stop("Falsche Eingabe", 270, all[i]);
		}
		return null;
	}

}