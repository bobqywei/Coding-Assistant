import java.io.*;
import java.util.ArrayList;

public class Main {

    private static ArrayList<String> file = new ArrayList<String>();
    private static String status;
    private static String speech = "HASHtag include less than s t d i o dot h greater than space at";

    public static void main(String[] args) {
        BufferedReader reader;
        String line;
        try {
            reader = new BufferedReader(new FileReader("Test.txt"));
            while ((line = reader.readLine()) != null) {
                file.add(line);
            }
            reader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        simplify();
        System.out.println(speech);

        try {
            PrintWriter pr = new PrintWriter("Test.txt");
            for (String aFile : file) {
                pr.println(aFile);
            }
            pr.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    //Line x
    private static void copy(int x) {
        if (x <= file.size() & x >= 0) {

            FileWriter fwOb;
            try {
                fwOb = new FileWriter("Clipboard.txt", false);
                PrintWriter pwOb = new PrintWriter(fwOb, false);
                pwOb.flush();
                pwOb.close();
                fwOb.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                PrintWriter pr = new PrintWriter("Clipboard.txt");
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
    private static void copy(int x, int y) {
        if (x <= file.size() & x >= 0 & y <= file.size() & y >= 0) {

            FileWriter fwOb;
            try {
                fwOb = new FileWriter("Clipboard.txt", false);
                PrintWriter pwOb = new PrintWriter(fwOb, false);
                pwOb.flush();
                pwOb.close();
                fwOb.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                PrintWriter pr = new PrintWriter("Clipboard.txt");
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
    private static void paste(int x) {
        ArrayList<String> temp = new ArrayList<String>();
        if (x <= file.size() & x >= 0) {
            BufferedReader reader;
            String line;
            try {
                reader = new BufferedReader(new FileReader("Clipboard.txt"));
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
    private static void cut(int x) {
        if (x <= file.size() & x >= 0) {
            copy(x);
            delete(x);
        } else {
            status = "Index out of  bounds";
        }
    }

    //Lines x to y inclusive
    private static void cut(int x, int y) {
        if (x <= file.size() & x >= 0 & y <= file.size() & y >= 0) {
            copy(x, y);
            delete(x, y);
        } else {
            status = "Index out of bounds";
        }
    }

    //Line x
    private static void delete(int x) {
        if (x <= file.size() & x >= 0) {
            file.remove(x);
        } else {
            status = "Index out of bounds";
        }
    }

    //Lines x to y inclusive
    private static void delete(int x, int y) {
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
    private static void insert(int x, String words) {
        if (x <= file.size() & x >= 0) {
            file.add(x, words);
        } else {
            status = "Index out of bounds";
        }
    }

    //Line x, index y
    private static void insert(int x, int y, String words) {
        if (x <= file.size() & x >= 0 & y <= file.get(x).length() & y >= 0) {
            StringBuilder sb = new StringBuilder(file.get(x));
            sb.insert(y, words);
            file.set(x, sb.toString());
        } else {
            status = "Index out of bounds";
        }
    }

    private static void backspace() {
        StringBuilder sb = new StringBuilder(file.get(file.size() - 1));
        if (sb.length() == 0) {
            file.remove(file.size() - 1);
        } else {
            sb.setLength(sb.length() - 1);
            file.set(file.size() - 1, sb.toString());
        }
    }

    private static void simplify() {
        speech = speech.toLowerCase();

        speech = speech.replaceAll("\\btilde\\b", "~");
        speech = speech.replaceAll("\\bacute\\b", "`");
        speech = speech.replaceAll("\\bback quote\\b", "`");
        speech = speech.replaceAll("\\bgrave\\b", "`");
        speech = speech.replaceAll("\\bexclamation mark\\b", "!");
        speech = speech.replaceAll("\\bexclamation point\\b", "!");
        speech = speech.replaceAll("\\bampersat\\b", "@");
        speech = speech.replaceAll("\\bat\\b", "@");
        speech = speech.replaceAll("\\bat symbol\\b", "@");
        speech = speech.replaceAll("\\bnumber sign\\b", "#");
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
        speech = speech.replaceAll("\\band\\b", "&");
        speech = speech.replaceAll("\\band symbol\\b", "&");
        speech = speech.replaceAll("\\basterisk\\b", "*");
        speech = speech.replaceAll("\\bstar\\b", "*");
        speech = speech.replaceAll("\\bstar symbol\\b", "*");
        speech = speech.replaceAll("\\bopen parenthesis\\b", "(");
        speech = speech.replaceAll("\\bclose parenthesis\\b", ")");
        speech = speech.replaceAll("\\bhyphen\\b", "-");
        speech = speech.replaceAll("\\bminus\\b", "-");
        speech = speech.replaceAll("\\bdash\\b", "-");
        speech = speech.replaceAll("\\bminus sign\\b", "-");
        speech = speech.replaceAll("\\bunderscore\\b", "_");
        speech = speech.replaceAll("\\bplus\\b", "+");
        speech = speech.replaceAll("\\bplus sign\\b", "+");
        speech = speech.replaceAll("\\bequals\\b", "=");
        speech = speech.replaceAll("\\bequal\\b", "=");
        speech = speech.replaceAll("\\bopen brace\\b", "{");
        speech = speech.replaceAll("\\bclose brace\\b", "}");
        speech = speech.replaceAll("\\bopen bracket\\b", "[");
        speech = speech.replaceAll("\\bclose bracket\\b", "]");
        speech = speech.replaceAll("\\bpipe\\b", "|");
        speech = speech.replaceAll("\\bor\\b", "|");
        speech = speech.replaceAll("\\bor symbol\\b", "|");
        speech = speech.replaceAll("\\bbackslash\\b", "\\");
        speech = speech.replaceAll("\\breverse solidus\\b", "\\");
        speech = speech.replaceAll("\\bforward slash\\b", "/");
        speech = speech.replaceAll("\\bsolidus\\b", "/");
        speech = speech.replaceAll("\\bcolon\\b", ":");
        speech = speech.replaceAll("\\bsemicolon\\b", ";");
        speech = speech.replaceAll("\\bquote\\b", "\"");
        speech = speech.replaceAll("\\bquotation mark\\b", "\"");
        speech = speech.replaceAll("\\bapostrophe\\b", "'");
        speech = speech.replaceAll("\\bsingle quote\\b", "'");
        speech = speech.replaceAll("\\bless than\\b", "<");
        speech = speech.replaceAll("\\bless than symbol\\b", "<");
        speech = speech.replaceAll("\\bless then\\b", "<");
        speech = speech.replaceAll("\\bless then symbol\\b", "<");
        speech = speech.replaceAll("\\bgreater than\\b", ">");
        speech = speech.replaceAll("\\bgreater than symbol\\b", ">");
        speech = speech.replaceAll("\\bgreater then\\b", ">");
        speech = speech.replaceAll("\\bgreater then symbol\\b", ">");
        speech = speech.replaceAll("\\bcomma\\b", ",");
        speech = speech.replaceAll("\\bperiod\\b", ".");
        speech = speech.replaceAll("\\bdot\\b", ".");
        speech = speech.replaceAll("\\bquestion mark\\b", "?");
        speech = speech.replaceAll("\\bnew line\\b", "\n");

        speech = speech.replaceAll(" ", "");
        speech = speech.replaceAll("\\bspace\\b", " ");
    }
}