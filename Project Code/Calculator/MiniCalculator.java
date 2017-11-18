import java.util.Arrays;

public class MiniCalculator {

	public static final String[] ZERO = {"zero", "oh"};
	public static final String[] ONES = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
	public static final String[] TEENS = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
	public static final String[] TENS = {"twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
	public static final String[] MAGNITUDES = {"hundred", "thousand", "million", "billion"};

	public static final String[][] ALL = {ZERO, ONES, TEENS, TENS, MAGNITUDES};

	private String input;
	private double[] vals;
	private String[] ops;

	public boolean decode() { // if this returns false, input is not a mathematical equation. if true, then this function turns String input into two arrays: values and operations
		// start by removing all "and"'s from the input since they are redundant, as well as "by" (in the case of "divided by")
		input.replaceAll("and ", "");
		String[] inputSplit = input.split(" ");
		vals = new double[(inputSplit.length + 1) / 2];
	    int	valCount = 0;
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
					}
					if (i + 1 == inputSplit.length) { // meaning next is operation, so copy temp into th vals array. or, if there is no next value i.e. i is on the last iteration, copy.
						temp += tempMag; // if we do not have this, the very last value of inputSplit will be missed. it will be stored in tempMag because of the switch statement, but it is not stored into temp
						vals[valCount] = temp;
						valCount++;
						temp = 0;
						tempMag = 0;
					} else { // if next input is magnitude, then this shouldn't be allowed to add it as tempMag needs to individually be magnified. if not, thten just add them together.
						if (j != 5 && Arrays.asList(MAGNITUDES).indexOf(inputSplit[i + 1]) == -1) {
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
	
	public double getValue() {
		return vals[0]; // values all shifted down to the first index.
	}

	public MiniCalculator(String input) {
		this.input = input;
	}

	public static void main(String[] args) { // if keyword == what is... trouble doing composite brackets
		Calculator calc = new Calculator("three hundred million four hundred thousand nine hundred and forty two");
		if (calc.decode() == true) {
			System.out.println(calc.getValue());
		} else {
			System.out.println("Not a number!"); //note this never triggers because no return false statement (yet) // for the actual thing, this defaults to google search instead
		}
	}	
}


