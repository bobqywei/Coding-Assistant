import java.util.Arrays;

public class Calculator {

	public static final String[] ZERO = {"zero", "oh"};
	public static final String[] ONES = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
	public static final String[] TEENS = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
	public static final String[] TENS = {"twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
	public static final String[] MAGNITUDES = {"hundred", "thousand", "million", "billion"};
	public static final String[] OPERATIONS = {"plus", "minus", "times", "divided"}; // add multiplied by?
	public static final String[] BRACES = {"parenthesis", "parentheses", "bracket"};
	public static final String[][] ALL = {ZERO, ONES, TEENS, TENS, MAGNITUDES, OPERATIONS, BRACES};

	private String input;
	private double[] vals;
	private String[] ops;
	private Integer[] brace;

	public boolean decode() { // if this returns false, input is not a mathematical equation. if true, then this function turns String input into two arrays: values and operations
		// start by removing all "and"'s from the input since they are redundant, as well as "by" (in the case of "divided by")
		input.replaceAll("and ", "");
		input.replaceAll("by ", "");
		// INITIALLY IGNORING SINS AND LOGS AND POSSIBLE "OF" AND POSSIBLE "PARAENTHESES/bracket" // handle these separately from vals and ops.
		// also ignore pi, e and decimal (aka point) for now. ALSO "NEGATIVE"
		// bracket with no multiplication sign should default to that?
		// splitting input into individual words for easy comparison
		String[] inputSplit = input.split(" ");
		vals = new double[(inputSplit.length + 1) / 2];
		ops = new String[(inputSplit.length - 1) / 2];
		brace = new Integer[(vals.length)]; // add case where they forget a missing brace... binary array. to give priority to braces, what they essentially do is iterate through the interval between the first and second 1's (i.e. pairs of 1's), and performing the operations within these intervals. the size is the same as vals because the interval is based on the positions of the numbers in vals
		int valCount = 0;
		int opCount = 0;
		double temp = 0; // stores cumulative value until the next operation is found (for switch statement)
		double tempMag = 0; // stores the most recent value that is to be either gaining magnitude or being added
		for (int i = 0; i < inputSplit.length; i++) { // cycling through all inputted words
			for (int j = 0; j < ALL.length; j++) { // cycling through all arrays (but not through the contents of each array)
				if (Arrays.asList(ALL[j]).indexOf(inputSplit[i]) != -1) { // indexing the content of each array for the current word
					int col = Arrays.asList(ALL[j]).indexOf(inputSplit[i]);
					switch (j) {
						case 0: tempMag =  0; // j = 0 is array ZERO (if we are in the context of one oh one (which is the MUCH MORE LIKELY USE FOR 0), this should be multiplying by ten instead)
								break;
						case 1: tempMag = col + 1; // j = 1 is array ONES, where the array index # is always 1 less than the value represented
								break;
						case 2: tempMag = 10 + col; // j = 2 is array TEENS, where the value represented is always 10 + the array index #
								break;
						case 3: tempMag = (col + 2) * 10; // j = 3 is array TENS
								break;
						case 4: if (col == 0) {
									tempMag *= 100;
								} else {
									tempMag *= Math.pow(1000, col); // col corresponds to mag i.e. col = 1: 1000, col = 2: 1000 * 1000 = 1000000 
								}
								break; // no need to increment valCount. magnitude only affects the most recent value and does not add any new ones
						case 5: switch (col) {
									case 0: ops[opCount] = "+";
											break;
									case 1: ops[opCount] = "-";
											break;
									case 2: ops[opCount] = "*";
											break;
									case 3: ops[opCount] = "/";
											break;
								}
								opCount++;
								break;
						case 6: brace[valCount] = 1;
								break;
					}
					if (i + 1 == inputSplit.length || Arrays.asList(OPERATIONS).indexOf(inputSplit[i + 1]) != -1 ) { // meaning next is operation, so copy temp into th vals array. or, if there is no next value i.e. i is on the last iteration, copy.
						temp += tempMag; // if we do not have this, the very last value of inputSplit will be missed. it will be stored in tempMag because of the switch statement, but it is not stored into temp
						vals[valCount] = temp;
						System.out.println("FIX ME" + vals[valCount]);
						valCount++;
						temp = 0;
						tempMag = 0;
					} else { // if next input is magnitude, then this shouldn't be allowed to add it as tempMag needs to individually be magnified. if not, thten just add them together.
						if (j != 5 && Arrays.asList(MAGNITUDES).indexOf(inputSplit[i + 1]) == -1 && Arrays.asList(BRACES).indexOf(inputSplit[i + 1]) == -1) {
							temp += tempMag; // temp stores the cumulative value. note that, if the next input value is a magnitude, this does not run, so tempMag is multiplied by a certain magnitude and then added
						}
					}
					//for (int k = 0; k < ops.length; k++) { 
					//	System.out.println(vals[k] + "next is" + vals[k+1] + " hello " + ops[k]); 
					//}
					break; // start searching for the next word, since this one was already found. also prevents program from returning false even if a word was found
				}
			}
			//return false;// leaving this false gives the program some leeway.. not sure whats the best way to apparoach this //BUGGED HERE, THIS WILL MAKE THE PROGRAM ALWAYS RETURN FALSE FOR SOME REASON // if the for loop is not broken, that must mean no word was matched. so this is not a mathermatical statement.
		}
		return true;
	}

	public void executor(String op1, String op2, boolean priority, int start, int end) {
		int ind;
		do { // complete mult and div first in this while loop. when you remove an operation and combine values, push everything down		
			int ind1 = -1;
			int ind2 = -1;
			// Array Finder
			for (int i = start; i < end; i++) {
				if (op1 == ops[i] && ind1 == -1) {
					ind1 = i; // mult/add has been found at index i in the interval
				}
				if (op2 == ops[i] && ind2 == -1) {
					ind2 = i;
				}
				if (ind1 != -1 && ind2 != -1) {
					break;
				}
			}
			if (ind1 == -1 && ind2 == -1) {
				break;
			}
			// Array Combiner
			if (ind1 < ind2 && ind1 != -1 && ind2  != -1 || ind1 != -1 && ind2 == -1) { // BEDMAS by left associative
				ind = ind1; // use a forloop to find the first occurence istead, with the bounds, and i should be fine.. for loop goes OUTSIDE while loop, as i could use it for my while loop as well..
				if (priority == true) {
					vals[ind] *= vals[ind + 1];
				} else {
					vals[ind] += vals[ind + 1];
				}
			} else { // ind2 must come first in this case. if you use only else, this will crash as ind = -1 is possible
				ind = ind2;
				if (priority == true) {
					vals[ind] /= vals[ind + 1];
				} else {
					vals[ind] -= vals[ind + 1];
				}
			} 
			// Array Shrinker
			for (int i = ind; i < ops.length; i++) { // shifting the values down; last value should now be empty
				if (i != Arrays.asList(ops).indexOf(null) - 1 && i != ops.length - 1) { // first iter stops at length - 1, every other would be stopping at null - 1
					vals[i + 1] = vals[i + 2];
					ops[i] = ops[i + 1];
					brace[i] = brace[i + 1];
				} else {
					vals[i + 1] = Double.NaN;
					ops[i] = null;
					brace[i] = null;
					break; // break is needed as the for loop would countinue past this, where every value would be null
				}
			}
		} while (ind != -1);
	}

	public double getValue() {
		while (Arrays.asList(brace).indexOf(1) != -1) {
			int first = Arrays.asList(brace).indexOf(1); // finding the interval that the brace affects
			brace[Arrays.asList(brace).indexOf(1)] = null;
			int second = Arrays.asList(brace).indexOf(1);
			brace[Arrays.asList(brace).indexOf(1)] = null;
			executor("*","/", true, first, second);  // this sends in the interval with brackets to executor, which then evalutes all expressions in that interval
			executor("+","-", false, first, second);
		}
		executor("*","/", true, 0, ops.length); // priority = true b/c multiplication and division has priority in bedmas
		executor("+","-", false, 0, ops.length);
		return vals[0]; // values all shifted down to the first index.
	}

	public Calculator(String input) {
		this.input = input;
	}

	public static void main(String[] args) { // if keyword == what is... trouble doing composite brackets
		Calculator calc = new Calculator("bracket bracket one plus two bracket plus three bracket");
		if (calc.decode() == true) {
			System.out.println(calc.getValue());
		} else {
			System.out.println("Not a mathematical expression!"); //note this never triggers because no return false statement (yet) // for the actual thing, this defaults to google search instead
		}
	}	
}


