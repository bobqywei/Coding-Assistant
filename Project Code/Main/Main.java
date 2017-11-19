import java.io.*;

public class Main {

    public static void main(String[] args) {
	BufferedReader reader;
	String speech = "";
	String status = "Process finished with exit code 0";

        try {
            reader = new BufferedReader(new FileReader("Files/Speech.txt"));
            speech = reader.readLine();
            reader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        Functions functions = new Functions(speech);

        //Gets working file name
        functions.getFile();

        //Stores file in array
        functions.openFile();

	//Executes commands
	if (speech.contains("raspberry")) {
	    if (speech.contains("calculate") || speech.contains("what is") || speech.contains("what's") || speech.contains("calculate")) {
		speech.replace("equal to", "");
		speech.replace("equal", "");
		Calculator calc = new Calculator(speech);
		if (calc.decode() == true) {
		    System.out.println(calc.getValue());
		} else {
		    status = "Not a mathematical expression";
		}
	    } else if (speech.contains("file") && (speech.contains("create") || speech.contains("build") || speech.contains("make"))) {
		if (speech.contains("called")) {
		    functions.newFile(speech.substring(speech.indexOf("called") + "called".length() + 1));
		} else if (speech.contains("named")) {
		    functions.newFile(speech.substring(speech.indexOf("named") + "named".length() + 1));
		} else if (speech.contains("name")) {
		    functions.newFile(speech.substring(speech.indexOf("name") + "name".length() + 1));
		} else if (speech.contains("file")) {
		    functions.newFile(speech.substring(speech.indexOf("file") + "file".length() + 1));
		}
	    } else if (speech.contains("change") || speech.contains("switch") || speech.contains("tab") || speech.contains("swap")) {
 		if (speech.contains("to")) {
		    functions.changeFile(speech.substring(speech.indexOf("to") + "to".length() + 1));
		} else if (speech.contains("named")) {
		    functions.changeFile(speech.substring(speech.indexOf("named") + "named".length() + 1));
		} if (speech.contains("name")) {
		    functions.changeFile(speech.substring(speech.indexOf("name") + "name".length() + 1));
		} else if (speech.contains("called")) {
		    functions.changeFile(speech.substring(speech.indexOf("called") + "called".length() + 1));
		} else if (speech.contains("file")) {
		    functions.changeFile(speech.substring(speech.indexOf("file") + "file".length() + 1));
		}
	    } else if (speech.contains("file") && (speech.contains("remove") || speech.contains("delete"))) {
		if (speech.contains("called")) {
		    functions.deleteFile(speech.substring(speech.indexOf("called") + "called".length() + 1));
		} else if (speech.contains("named")) {
		    functions.deleteFile(speech.substring(speech.indexOf("named") + "named".length() + 1));
		} else if (speech.contains("name")) {
		    functions.deleteFile(speech.substring(speech.indexOf("name") + "name".length() + 1));
		} else if (speech.contains("file")) {
		    functions.deleteFile(speech.substring(speech.indexOf("file") + "file".length() + 1));
		}
	    } else if (speech.contains("file") && (speech.contains("copy") || speech.contains("clone"))) {
		if (speech.contains("it")) {
		    functions.cloneFile(speech.substring(speech.indexOf("it") + "it".length() + 1));
		} else if (speech.contains("name")) {
		    functions.cloneFile(speech.substring(speech.indexOf("name") + "name".length() + 1));
		} else if (speech.contains("file")) {
		    functions.cloneFile(speech.substring(speech.indexOf("file") + "file".length() + 1));
		}
	    } else if (speech.contains("backspace")) {
		functions.backspace();
	    } else if (speech.contains("delete") && speech.contains("line")) {
		functions.delete(functions.getCurrentLine());
	    }
	} else {
	    functions.findNumber();
	    functions.simplify();
	    functions.addSpeech();
	}
	status = functions.getStatus();

        //Writes ArrayList to text file
        functions.writeFile();
    }
}