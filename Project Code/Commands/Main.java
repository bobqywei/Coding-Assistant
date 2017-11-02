import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class Main {

    public static void main(String[] args) {
        String speech = "Hey Arduino how are you";
        String[][] contacts = new String[10000][8];
        StringBuilder builder = new StringBuilder();
        BufferedReader reader;
        String line;
        int count = 0;

        //Stores text files to 2D arrays
        try {
            reader = new BufferedReader(new FileReader("Files/Contacts.txt"));
            while ((line = reader.readLine()) != null) {
                contacts[count] = line.split(",");
                count ++;
            }
            reader.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        //Executes commands
        Commands commands = new Commands(speech, contacts);



        //Writes 2D arrays to text files
        contacts = commands.getContacts();

        for (int x = 0; x < contacts.length; x ++) {
            for (int y = 0; y < contacts[x].length; y ++) {
                builder.append(contacts[x][y] + "");
                if (y < contacts[x].length - 1) {
                    builder.append(",");
                }
            }
            builder.append("\n");
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Files/Contacts.txt"));
            writer.write(builder.toString());//save the string representation of the board
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
