import java.io.*;

public class Main {

    public static void main(String[] args) {
	BufferedReader reader;
	String speech = "";
	String status = "Success";
	String answer = null;

        try {
            reader = new BufferedReader(new FileReader("Files/Speech.txt"));
            speech = reader.readLine().replace("raspberry ", "");
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
		//if (speech.contains("raspberry")) {

			if (speech.contains("calculate") || speech.contains("what is") || speech.contains("what's")) {
				speech = speech.replace("raspberry", "");
				speech = speech.replace("equal to", "");
				speech = speech.replace("equal", "");
				speech = speech.replace("what is", "");
				speech = speech.replace("what does", "");
				speech = speech.replace("what's", "");
				speech = speech.replace("calculate", "");
				speech = speech.replace("\\s+", "");
				Calculator calc = new Calculator(speech);

				if (calc.decode() == true) {
					answer = Double.toString(calc.getValue());
				} else {
					status = "Not a mathematical expression";
				}
			} else if (speech.contains("file") && (speech.contains("create") || speech.contains("build") || speech.contains("make"))) {
				if (speech.contains("called")) {
					functions.newFile((speech.substring(speech.indexOf("called") + "called".length() + 1)).replaceAll("\\s+", ""));
				} else if (speech.contains("named")) {
					functions.newFile((speech.substring(speech.indexOf("named") + "named".length() + 1)).replaceAll("\\s+", ""));
				} else if (speech.contains("name")) {
					functions.newFile((speech.substring(speech.indexOf("name") + "name".length() + 1)).replaceAll("\\s+", ""));
				} else if (speech.contains("file")) {
					functions.newFile((speech.substring(speech.indexOf("file") + "file".length() + 1)).replaceAll("\\s+", ""));
				}
			} else if (speech.contains("change") || speech.contains("switch") || speech.contains("tab") || speech.contains("swap")) {
				if (speech.contains("to")) {
					functions.changeFile((speech.substring(speech.indexOf("to") + "to".length() + 1)).replaceAll("\\s+", ""));
				} else if (speech.contains("named")) {
					functions.changeFile((speech.substring(speech.indexOf("named") + "named".length() + 1)).replaceAll("\\s+", ""));
				} if (speech.contains("name")) {
					functions.changeFile((speech.substring(speech.indexOf("name") + "name".length() + 1)).replaceAll("\\s+", ""));
				} else if (speech.contains("called")) {
					functions.changeFile((speech.substring(speech.indexOf("called") + "called".length() + 1)).replaceAll("\\s+", ""));
				} else if (speech.contains("file")) {
					functions.changeFile((speech.substring(speech.indexOf("file") + "file".length() + 1)).replaceAll("\\s+", ""));
				}
			} else if (speech.contains("file") && (speech.contains("remove") || speech.contains("delete"))) {
				if (speech.contains("called")) {
					functions.deleteFile((speech.substring(speech.indexOf("called") + "called".length() + 1)).replaceAll("\\s+", ""));
				} else if (speech.contains("named")) {
					functions.deleteFile((speech.substring(speech.indexOf("named") + "named".length() + 1)).replaceAll("\\s+", ""));
				} else if (speech.contains("name")) {
					functions.deleteFile((speech.substring(speech.indexOf("name") + "name".length() + 1)).replaceAll("\\s+", ""));
				} else if (speech.contains("file")) {
					functions.deleteFile((speech.substring(speech.indexOf("file") + "file".length() + 1)).replaceAll("\\s+", ""));
				}
			} else if (speech.contains("file") && (speech.contains("copy") || speech.contains("clone"))) {
				if (speech.contains("it")) {
					functions.cloneFile((speech.substring(speech.indexOf("it") + "it".length() + 1)).replaceAll("\\s+", ""));
				} else if (speech.contains("name")) {
					functions.cloneFile((speech.substring(speech.indexOf("name") + "name".length() + 1)).replaceAll("\\s+", ""));
				} else if (speech.contains("file")) {
					functions.cloneFile((speech.substring(speech.indexOf("file") + "file".length() + 1)).replaceAll("\\s+", ""));
				}
			} else if (speech.contains("backspace")) {
				functions.backspace();
			} else if (speech.contains("copy") && speech.contains("lines")) {
				functions.findNumber();
				speech = functions.getSpeech();
				functions.copy(Integer.parseInt((speech.substring(speech.indexOf("lines") + "lines".length() + 1, speech.indexOf("to") - 1)).replaceAll("\\s+", "")), Integer.parseInt((speech.substring(speech.indexOf("to") + "to".length() + 1)).replaceAll("\\s+", "")));
			} else if (speech.contains("copy") && speech.contains("line")) {
				functions.findNumber();
				speech = functions.getSpeech();
				functions.copy(Integer.parseInt((speech.substring(speech.indexOf("line") + "line".length() + 1)).replaceAll("\\s+", "")));
			} else if (speech.contains("paste")) {
				functions.findNumber();
				speech = functions.getSpeech();
				functions.paste(Integer.parseInt((speech.substring(speech.indexOf("line") + "line".length() + 1)).replaceAll("\\s+", "")));
			} else if (speech.contains("cut") && speech.contains("lines")) {
				functions.findNumber();
				speech = functions.getSpeech();
				functions.cut(Integer.parseInt((speech.substring(speech.indexOf("lines") + "lines".length() + 1, speech.indexOf("to") - 1)).replaceAll("\\s+", "")), Integer.parseInt((speech.substring(speech.indexOf("to") + "to".length() + 1)).replaceAll("\\s+", "")));
			} else if (speech.contains("cut") && speech.contains("line")) {
				functions.findNumber();
				speech = functions.getSpeech();
				functions.cut(Integer.parseInt((speech.substring(speech.indexOf("line") + "line".length() + 1)).replaceAll("\\s+", "")));
			} else if (speech.contains("delete") && speech.contains("lines")) {
				functions.findNumber();
				speech = functions.getSpeech();
				functions.delete(Integer.parseInt((speech.substring(speech.indexOf("lines") + "lines".length() + 1, speech.indexOf("to") - 1)).replaceAll("\\s+", "")), Integer.parseInt((speech.substring(speech.indexOf("to") + "to".length() + 1)).replaceAll("\\s+", "")));
			} else if (speech.contains("delete") && speech.contains("line")) {
				functions.findNumber();
				speech = functions.getSpeech();
				functions.delete(Integer.parseInt((speech.substring(speech.indexOf("line") + "line".length() + 1)).replaceAll("\\s+", "")));
			} else if (speech.contains("insert") && speech.contains("index") && speech.contains("line")) {
				if (speech.contains("at line")) {
					functions.findNumber();
					speech = functions.getSpeech();
					int one = Integer.parseInt((speech.substring(speech.indexOf("line") + "line".length() + 1, speech.indexOf("index") - 1)).replaceAll("\\s+", ""));
					int two = Integer.parseInt((speech.substring(speech.indexOf("index") + "index".length() + 1)).replaceAll("\\s+", ""));
					String speech2 = speech.substring(speech.indexOf("insert") + "insert".length() + 1, speech.indexOf("at line") - 1);
					Functions functions2 = new Functions(speech2);
					functions2.simplify();
					speech2 = functions2.getSpeech();
					functions.insert(one, two, speech2);
				} else if (speech.contains("in line")) {
					functions.findNumber();
					speech = functions.getSpeech();
					int one = Integer.parseInt((speech.substring(speech.indexOf("line") + "line".length() + 1, speech.indexOf("index") - 1)).replaceAll("\\s+", ""));
					int two = Integer.parseInt((speech.substring(speech.indexOf("index") + "index".length() + 1)).replaceAll("\\s+", ""));
					String speech2 = speech.substring(speech.indexOf("insert") + "insert".length() + 1, speech.indexOf("in line") - 1);
					Functions functions2 = new Functions(speech2);
					functions2.simplify();
					speech2 = functions2.getSpeech();
					functions.insert(one, two, speech2);
				}
			} else if (speech.contains("insert") && speech.contains("line")) {
				if (speech.contains("at line")) {
					functions.findNumber();
					speech = functions.getSpeech();
					int one = Integer.parseInt((speech.substring(speech.indexOf("line") + "line".length() + 1)).replaceAll("\\s+", ""));
					String speech2 = speech.substring(speech.indexOf("insert") + "insert".length() + 1, speech.indexOf("at line") - 1);
					Functions functions2 = new Functions(speech2);
					functions2.simplify();
					speech2 = functions2.getSpeech();
					functions.insert(one, speech2);
				} else if (speech.contains("in line")) {
					functions.findNumber();
					speech = functions.getSpeech();
					int one = Integer.parseInt((speech.substring(speech.indexOf("line") + "line".length() + 1)).replaceAll("\\s+", ""));
					String speech2 = speech.substring(speech.indexOf("insert") + "insert".length() + 1, speech.indexOf("in line") - 1);
					Functions functions2 = new Functions(speech2);
					functions2.simplify();
					speech2 = functions2.getSpeech();
					functions.insert(one, speech2);
				}
			} else if (speech.contains("compile")) {
				try {
					Runtime.getRuntime().exec("python Compiler.py");
				} catch (Exception e) {
				}
			} else if (speech.contains("")) {
				functions.findNumber();
				functions.simplify();
				functions.addSpeech();
			}
		//}

        	//Writes ArrayList to text file and prints to console
		functions.writeFile();
		System.out.print("\033[H\033[2J");
		System.out.flush();
		functions.writeConsole();

		//Prints status

		if (answer == null) {
			status = functions.getStatus();
		} else {
			status = answer;
		}

		try {
		    PrintWriter writer = new PrintWriter("Files/Status.txt");
		    writer.print("");
                    writer.close();

            	    PrintWriter pr = new PrintWriter("Files/Status.txt");
		    pr.println(status);
            	    pr.close();
        	}
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}