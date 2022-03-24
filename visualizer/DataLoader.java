package visualizer;

import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import sol.Dataset;
import src.DecisionTreeTester;
import visualizer.exceptions.BadFileTypeException;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataLoader {

    private BorderPane root;

    /**
     * The constructor for the DataReader. You won't need to call this.
     */
    DataLoader(BorderPane root) {
        this.root = root;
    }

    public Result loadDataset() {
        String fileName = this.getFileName();
        try {
            return new Result(fileName, DecisionTreeTester.makeDataset(fileName, Dataset.class));
        } catch (InstantiationException | InvocationTargetException
            | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException("Could not read file: " + fileName);
        }
    }

    /**
     * This method opens a FileChooser to select a file. The file will be opened
     * only. There is no support currently for saving new files. (This may
     * change if we want to save the produced tree??)
     * <p>
     * This method is called by the loader and the filename returned is used
     * there only.
     * <p>
     * If the file selected is not a csv file, it will error. HOWEVER, if the
     * file selected is somehow invalid, this method WILL NOT ERROR. Instead, it
     * will only return a null filepath, so that an exception can be thrown
     * later in the program.
     * <p>
     * This method also keeps track of the number of lines of the program. I'm
     * not exactly sure why yet, but I have a feeling it will be useful later.
     * ^^ wait that's a lie it doesn't do that yet
     *
     * @return The filepath selected in the filechooser.
     */
    private String getFileName() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select a Data File");

        if (Files.exists(Paths.get("data"))) {
            fc.setInitialDirectory(new File("data"));
        } else {
            fc.setInitialDirectory(new File(System.getProperty("user.dir")));
        }

        File file = fc.showOpenDialog(this.root.getScene().getWindow());
        String path;
        if (file == null) {
            return null;
        } else {
            path = file.getAbsolutePath();
        }
        if (!DataLoader.getExtension(path).equals("csv")) {
            throw new BadFileTypeException();
        }
        return path;
    }

    /**
     * Get the extension of the filepath, if it exists. This method _should_ be
     * proofed against situations where there is a period in the filepath before
     * the extension. It does this by checking to see if the last occurrence of
     * a dot (.) is after the last occurrence of a slash or backslash.
     * <p>
     * If no extension is found, it returns an empty string. Normally I would
     * shudder at doing this, but since this method is only ever used in
     * checking whether the extension is "csv", I think it's probably ok.
     *
     * @param filepath The path to get the extension of
     * @return The string representing the extension (without the dot)
     */
    private static String getExtension(String filepath) {
        String ex = "";
        int dotIndex = filepath.lastIndexOf('.');
        int slashIndex = Math.max(filepath.lastIndexOf('/'),
            filepath.lastIndexOf('\\'));
        // This case occurs if we are really dealing with the extension and not
        // a dot somewhere in the filepath but that isn't a file extension
        if (dotIndex > slashIndex) {
            ex = filepath.substring(dotIndex + 1);
        }
        return ex;
    }

    public class Result {
        public String fileName;
        public Dataset loadedDataset;

        public Result(String fileName, Dataset loadedDataset) {
            this.fileName = fileName;
            this.loadedDataset = loadedDataset;
        }

        public boolean isPresent() {
            return this.loadedDataset != null && this.fileName != null;
        }
    }
}
