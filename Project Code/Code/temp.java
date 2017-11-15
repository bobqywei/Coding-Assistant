import java.io.*;
import java.util.ArrayList;

public class Main {

    private static ArrayList<String> file = new ArrayList<String>();
    private static String status;

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

        //cut(1, 4);

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

    private static void undo() {

    }

    private static void redo() {

    }

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

    private static void cut(int x) {
        if (x <= file.size() & x >= 0) {
            copy(x);
            delete(x);
        } else {
            status = "Index out of  bounds";
        }
    }

    private static void cut(int x, int y) {
        if (x <= file.size() & x >= 0 & y <= file.size() & y >= 0) {
            copy(x, y);
            delete(x, y);
        } else {
            status = "Index out of bounds";
        }
    }

    private static void delete(int x) {
        if (x <= file.size() & x >= 0) {
            file.remove(x);
        } else {
            status = "Index out of bounds";
        }
    }

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

    private static void insert(int x, String words) {
        if (x <= file.size() & x >= 0) {
            file.add(x, words);
        } else {
            status = "Index out of bounds";
        }
    }
}