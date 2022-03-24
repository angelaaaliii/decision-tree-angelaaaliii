package visualizer.exceptions;

public class BadFileTypeException extends DecisionTreeException {

    public BadFileTypeException() {
        super(
            "The file you selected is not a CSV-formatted file. \nPlease select a file with the .csv extension.");
    }
}