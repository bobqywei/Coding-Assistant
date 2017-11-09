public class Main {

    public static void main(String[] args) {
        Functions functions = new Functions();

        //Gets file name
        functions.getFile();

        //Stores file in array
        functions.openFile();

        //Executes functions
        functions.changeFile("Main");


        //Writes 2D array to text file
        functions.writeFile();
        functions.writeStatus();
    }
}