import java.io.*;
import java.util.ArrayList;

public class Functions {

    private ArrayList<String> file = new ArrayList<String>();
    private String fileName;
    private String status = "Process finished with exit code 0";

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
            reader = new BufferedReader(new FileReader("Files/" + this.fileName + ".txt"));
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
            PrintWriter pr = new PrintWriter("Files/" + this.fileName + ".txt");

            for (int i = 0; i < file.size() ; i ++) {
                pr.println(file.get(i));
            }
            pr.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected void writeStatus() {
        try {
            PrintWriter pr = new PrintWriter("Files/Status/Status.txt");
            pr.println(status);
            pr.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected void newFile(String fileName) {
        try {
            PrintWriter pr = new PrintWriter("Files/" + fileName + ".txt");
            pr.println("public class " + fileName + " {");
            pr.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected void changeFile(String fileName) {
        File file = new File("Files/" + fileName + ".txt");
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
        File file = new File("Files/" + fileName + ".txt");
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

    protected void renameFile(String oldName, String newName) {
        File file = new File("Files/" + oldName + ".txt");
        if (file.isFile()) {
            file.renameTo(new File("Files/" + newName + ".txt"));
            file = new File("Files/" + newName + ".txt");
            if (file.isFile()) {
                status = "File " + fileName + " already exists";
            }
        } else {
            status = "File " + fileName + " does not exist";
        }
    }


}
