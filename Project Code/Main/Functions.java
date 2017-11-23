import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Functions {

    private static ArrayList<String> file = new ArrayList<String>();
    private static String fileName;
    private static String speech;
    private static String status = "Success";
    private static final String[] ZERO = {"zero", "oh"};
    private static final String[] ONES = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    private static final String[] TEENS = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
    private static final String[] TENS = {"twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
    private static final String[] MAGNITUDES = {"hundred", "thousand", "million", "billion"};
    private static final String[] NUMBERS = {"zero", "oh", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety", "hundred", "thousand", "million", "billion"};
    private static final String[][] ALL = {ZERO, ONES, TEENS, TENS, MAGNITUDES};

    private static int[] vals;
    private static String subSpeech;

    Functions(String speech) {
	this.speech = speech;
    }

    protected String getSpeech() {
	return this.speech;
    }

    protected String getStatus() {
	return this.status;
    }

    protected void addSpeech() {
	file.add(speech);
    }

    protected int getCurrentLine() {
	return file.size() - 1;
    }

    protected void getFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Files/WorkingFile/FileName.txt"));
            fileName = reader.readLine();
            reader.close();
        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }

    protected void openFile() {
        BufferedReader reader;
        String line;

        try {
            reader = new BufferedReader(new FileReader("Files/" + this.fileName + ".c"));
            while ((line = reader.readLine()) != null) {
                file.add(line);
            }
            reader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected void writeFile() {
        try {
		PrintWriter writer = new PrintWriter("Files/" + this.fileName + ".c");
		writer.print("");
                writer.close();

            PrintWriter pr = new PrintWriter("Files/" + this.fileName + ".c");

            for (int i = 0; i < file.size() ; i ++) {
                pr.println(file.get(i));
            }
            pr.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected void writeConsole() {
	BufferedReader reader;
        String line;

        try {
            reader = new BufferedReader(new FileReader("Files/" + this.fileName + ".c"));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected void newFile(String fileName) {
        try {
            PrintWriter pr = new PrintWriter("Files/" + fileName + ".c");
	    pr.println("#include <stdio.h>\n");
            pr.println("int main() {");
            pr.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected void changeFile(String fileName) {
        File file = new File("Files/" + fileName + ".c");
        if (file.isFile()) {
            try {
                PrintWriter pr = new PrintWriter("Files/WorkingFile/FileName.txt");
                pr.println(fileName);
                pr.close();

                if (fileName.equals(this.fileName)) {
                    status = "Already in file " + fileName;
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            status = "File " + fileName + " does not exist";
        }

    }

    protected void deleteFile(String fileName) {
        File file = new File("Files/" + fileName + ".c");
        if (file.isFile()) {
            try {
                file.delete();

                if (fileName.equals(this.fileName)) {
                    status = "Cannot delete an open file";
                }
            } catch(Exception exception){
                exception.printStackTrace();
            }
        } else {
            status = "File " + fileName + " does not exist";

        }
    }

    protected void cloneFile(String newName) {
        File file = new File("Files/" + fileName + ".c");
	File file2 = new File("Files/" + newName + ".c");
        if (file.isFile()) {
            if (file2.isFile()) {
                status = "File " + newName + " already exists";
            } else {
		file.renameTo(new File("Files/" + newName + ".c"));
	    }
        } else {
            status = "File " + fileName + " does not exist";
        }
    }

    protected void renameFile(String oldName, String newName) {
        File file = new File("Files/" + oldName + ".c");
	File file2 = new File("Files/" + newName + ".c");
        if (file.isFile()) {
	    if (file2.isFile()) {
                status = "File " + newName + " already exists";
            } else {
		if (oldName.equals(fileName)) {
		    status = "Switch working file to change file name";
		} else {
                    file.renameTo(new File("Files/" + newName + ".c"));
		}
	    }
        } else {
            status = "File " + fileName + " does not exist";
        }
    }

    protected static boolean numberExists(String inputStr, String[] items) {
        for (String item : items) {
            if (inputStr.contains(item)) {
                return true;

            }
        }
        return false;
    }

    protected static void findNumber() {
	speech = speech.replace(".", "");
        while (numberExists(speech, NUMBERS)) {
            speech = speech.replaceAll("\\s+", " ");
            int smallest = 0;
            String[] temp1 = speech.split(" ");
            for (String aTemp1 : temp1) {
                if (numberExists(aTemp1, NUMBERS)) {
                    break;
                }
                smallest++;
                smallest += aTemp1.length();
            }

            int largest = speech.substring(0, smallest).length();
            String[] temp = speech.substring(smallest).split(" ");
            for (String aTemp : temp) {
                if (!numberExists(aTemp, NUMBERS) & !aTemp.equals("and")) {
                    break;
                }
                largest += aTemp.length() + 1;


            }

            subSpeech = speech.substring(smallest, largest - 1);
            if (changeNumber()) {
                speech = speech.substring(0, smallest) + vals[0] + " " + speech.substring(largest - 1);
            }
        }
    }


    protected static boolean changeNumber() {
        subSpeech.replaceAll("and ", "");
        String[] inputSplit = subSpeech.split(" ");
        vals = new int[(inputSplit.length + 1) / 2];
        int	valCount = 0;
        int temp = 0; // stores cumulative value until the next operation is found (for switch statement)
        int tempMag = 0; // stores the most recent value that is to be either gaining magnitude or being added
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

    //Line x
    protected static void copy(int x) {
        if (x <= file.size() & x >= 0) {
            FileWriter fwOb;
            try {
                fwOb = new FileWriter("Files/Clipboard.txt", false);
                PrintWriter pwOb = new PrintWriter(fwOb, false);
                pwOb.flush();
                pwOb.close();
                fwOb.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                PrintWriter pr = new PrintWriter("Files/Clipboard.txt");
                pr.println(file.get(x));
                pr.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            status = "Index out of bounds";
        }
    }

    //Lines x to y inclusive
    protected static void copy(int x, int y) {
        if (x <= file.size() & x >= 0 & y <= file.size() & y >= 0) {
            FileWriter fwOb;
            try {
                fwOb = new FileWriter("Files/Clipboard.txt", false);

                PrintWriter pwOb = new PrintWriter(fwOb, false);
                pwOb.flush();
                pwOb.close();
                fwOb.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                PrintWriter pr = new PrintWriter("Files/Clipboard.txt");
                for (int z = 0; z <= Math.abs(y - x); z ++) {
                    if (x < y) {
                        pr.println(file.get(x + z));
                    } else {
                        pr.println(file.get(y + z));
                    }
                }
                pr.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            status = "Index out of bounds";
        }
    }

    //Line x
    protected static void paste(int x) {
        ArrayList<String> temp = new ArrayList<String>();
        if (x <= file.size() & x >= 0) {
            BufferedReader reader;
            String line;
            try {

                reader = new BufferedReader(new FileReader("Files/Clipboard.txt"));
                while ((line = reader.readLine()) != null) {
                    temp.add(0, line);
                }
                reader.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            for (String aTemp : temp) {
                file.add(x, aTemp);
            }
        } else {
            status = "Index out of bounds";
        }
    }

    //Line x
    protected static void cut(int x) {
        if (x <= file.size() & x >= 0) {
            copy(x);
            delete(x);
        } else {
            status = "Index out of  bounds";
        }
    }

    //Lines x to y inclusive
    protected static void cut(int x, int y) {
        if (x <= file.size() & x >= 0 & y <= file.size() & y >= 0) {
            copy(x, y);
            delete(x, y);
        } else {
            status = "Index out of bounds";
        }
    }

    //Line x
    protected static void delete(int x) {
        if (x <= file.size() & x >= 0) {
            file.remove(x);
        } else {
            status = "Index out of bounds";
        }
    }

    //Lines x to y inclusive
    protected static void delete(int x, int y) {
        if (x <= file.size() & x >= 0 & y <= file.size() & y >= 0) {
            for (int z = 0; z <= Math.abs(y - x); z ++) {
                if (x < y) {

                    file.remove(x);
                } else {
                    file.remove(y);
                }
            }
        } else {
            status = "Index out of bounds";
        }
    }

    //Line x
    protected static void insert(int x, String words) {
        if (x <= file.size() & x >= 0) {
            file.add(x, words);
        } else {
            status = "Index out of bounds";
        }
    }

    //Line x, index y
    protected static void insert(int x, int y, String words) {
        if (x <= file.size() & x >= 0 & y <= file.get(x).length() & y >= 0) {
            StringBuilder sb = new StringBuilder(file.get(x));
            sb.insert(y, words);
            file.set(x, sb.toString());
        } else {
            status = "Index out of bounds";
        }
    }

    protected static void backspace() {
	if (file.size() != 0) {
            StringBuilder sb = new StringBuilder(file.get(file.size() - 1));
	    if (sb.length() == 0) {
    	        file.remove(file.size() - 1);
       	    } else {
                sb.setLength(sb.length() - 1);
                file.set(file.size() - 1, sb.toString());
            }
	} else {
	    status = "Index out of bounds";
	}
    }


    protected static void simplify() {
        speech = speech.toLowerCase();

	speech = speech.replaceAll("\\bopen parentheses\\b", "(");
	speech = speech.replaceAll("\\bclose parentheses\\b", ")");
        speech = speech.replaceAll("\\btilde\\b", "~");
        speech = speech.replaceAll("\\bacute\\b", "`");
        speech = speech.replaceAll("\\bback quote\\b", "`");
        speech = speech.replaceAll("\\bgrave\\b", "`");
        speech = speech.replaceAll("\\bexclamation mark\\b", "!");
        speech = speech.replaceAll("\\bexclamation point\\b", "!");
        speech = speech.replaceAll("\\bampersat\\b", "@");
        speech = speech.replaceAll("\\bat symbol\\b", "@");
        speech = speech.replaceAll("\\bat\\b", "@");
        speech = speech.replaceAll("\\bnumber sign\\b", "#");
	speech = speech.replaceAll("\\bnumber\\b", "#");
        speech = speech.replaceAll("\\bpound\\b", "#");
        speech = speech.replaceAll("\\bsharp\\b", "#");
        speech = speech.replaceAll("\\bhashtag\\b", "#");
        speech = speech.replaceAll("\\boctothorpe\\b", "#");
        speech = speech.replaceAll("\\bdollar sign\\b", "$");
        speech = speech.replaceAll("\\bpercent sign\\b", "%");
        speech = speech.replaceAll("\\bpercent\\b", "%");
        speech = speech.replaceAll("\\bcaret\\b", "^");
        speech = speech.replaceAll("\\bcircumflex\\b", "^");
        speech = speech.replaceAll("\\bampersand\\b", "&");
        speech = speech.replaceAll("\\band symbol\\b", "&");
        speech = speech.replaceAll("\\band\\b", "&");
        speech = speech.replaceAll("\\basterisk\\b", "*");
        speech = speech.replaceAll("\\bstar symbol\\b", "*");
        speech = speech.replaceAll("\\bstar\\b", "*");
	speech = speech.replaceAll("\\btimes\\b", "*");
	speech = speech.replaceAll("\\bmultiplied by\\b", "*");
        speech = speech.replaceAll("\\bhyphen\\b", "-");
        speech = speech.replaceAll("\\bminus\\b", "-");
        speech = speech.replaceAll("\\bdash\\b", "-");
        speech = speech.replaceAll("\\bminus sign\\b", "-");
        speech = speech.replaceAll("\\bunderscore\\b", "_");
        speech = speech.replaceAll("\\bplus\\b", "+");
        speech = speech.replaceAll("\\bplus sign\\b", "+");
        speech = speech.replaceAll("\\bequals to\\b", "=");
        speech = speech.replaceAll("\\bequals\\b", "=");
        speech = speech.replaceAll("\\bequal\\b", "=");
        speech = speech.replaceAll("\\bopen brace\\b", "{");
        speech = speech.replaceAll("\\bclose brace\\b", "}");
        speech = speech.replaceAll("\\bopen bracket\\b", "[");
        speech = speech.replaceAll("\\bclose bracket\\b", "]");
        speech = speech.replaceAll("\\bpipe\\b", "|");
        speech = speech.replaceAll("\\bor symbol\\b", "|");
        speech = speech.replaceAll("\\bor\\b", "|");
        speech = speech.replaceAll("\\bbackslash\\b", "\\");
        speech = speech.replaceAll("\\breverse solidus\\b", "\\");
        speech = speech.replaceAll("\\bforward slash\\b", "/");
        speech = speech.replaceAll("\\bsolidus\\b", "/");
	speech = speech.replaceAll("\\bdivided by\\b", "/");
        speech = speech.replaceAll("\\bcolon\\b", ":");
        speech = speech.replaceAll("\\bsemicolon\\b", ";");
        speech = speech.replaceAll("\\bquote\\b", "\"");
        speech = speech.replaceAll("\\bquotation mark\\b", "\"");
        speech = speech.replaceAll("\\bapostrophe\\b", "'");
        speech = speech.replaceAll("\\bsingle quote\\b", "'");
        speech = speech.replaceAll("\\bless than symbol\\b", "<");
        speech = speech.replaceAll("\\bless than\\b", "<");
        speech = speech.replaceAll("\\bless then symbol\\b", "<");
        speech = speech.replaceAll("\\bless then\\b", "<");
        speech = speech.replaceAll("\\bgreater than symbol\\b", ">");
        speech = speech.replaceAll("\\bgreater than\\b", ">");
        speech = speech.replaceAll("\\bgreater then symbol\\b", ">");
        speech = speech.replaceAll("\\bgreater then\\b", ">");
        speech = speech.replaceAll("\\bcomma\\b", ",");
        speech = speech.replaceAll("\\bperiod\\b", ".");
        speech = speech.replaceAll("\\bdot\\b", ".");
        speech = speech.replaceAll("\\bpoint\\b", ".");
        speech = speech.replaceAll("\\bquestion mark\\b", "?");
        speech = speech.replaceAll("\\bnew line\\b", "\n");
        speech = speech.replaceAll("\\btab\\b", "\t");

	speech = speech.replaceAll("\\bspace\\b", "@@@");
        speech = speech.replaceAll(" ", "");
        speech = speech.replace("@@@", " ");
	System.out.println(speech);
    }
}
