public class Main {

    public static void main(String[] args) {
        Functions functions = new Functions("hashtag less than s. t. d. i. o. dot h. greater than space at symbol");

        //Gets file name
        functions.getFile();

        //Stores file in array
        functions.openFile();

        //Executes functions
	functions.findNumber();
        functions.simplify();


        //Writes ArrayList to text file
        functions.writeFile();
    }
}