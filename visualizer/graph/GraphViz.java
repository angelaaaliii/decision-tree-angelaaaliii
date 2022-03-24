package visualizer.graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The Graphviz Class. Can use this class to generate image byte array. Created
 * by frank on 2014/11/17. Adapted for Decision Trees by skaragou 2018 Updated
 * for Windows by jkennan 3/2018
 */
public class GraphViz {

    private final String DOT;
    private final String TMP_PATH;

    /**
     * Constructor. Sets the temporary path and dot execution file based on the
     * user's OS.
     */
    public GraphViz() {
        TMP_PATH = System.getProperty("java.io.tmpdir");
        String os = System.getProperty("os.name");

        if (os.contains("Windows")) {
            if (System.getProperty("os.arch").contains("64")) {
                DOT = "C:\\Program Files\\Graphviz\\bin\\dot.exe";
            } else {
                DOT = "C:\\Program Files (x86)\\Graphviz\\bin\\dot.exe";
            }
        } else {
            DOT = "dot"; // THIS MAY NEED TO CHANGE
        }
    }

    /**
     * Generate Graph's image byte array.
     *
     * @param graph the graph you want to generate.
     * @param type  the file type.
     * @param dpi   dpi
     * @return the byte array of the image file
     */
    public byte[] getGraphByteArray(Graph graph, String type, String dpi) {
        String dotSource = this.genDotStringByGraph(graph);
        byte[] imgStream = null;
        File dotStringFile = this.writeDotSourceToFile(dotSource);
        if (dotStringFile != null) {
            imgStream = this.getImgStream(dotStringFile, type, "dot", dpi);
            try {
                dotStringFile.delete();
            } catch (Exception ex) {
                System.err.println(
                    "ERROR: Something went wrong deleting the temporary dot string file. \n"
                        +
                        "The program will continue to function normally, but be aware that phantom files are"
                        + "persisting on your filesystem.");
            }
        }
        return imgStream;
    }

    private String genDotStringByGraph(Graph graph) {

        String dotString = "digraph " +
            graph.getId() +
            graph.genDotString();
        return dotString;
    }

    /**
     * Writes a dot-formatted string to file. Returns the File object if successful.
     * Returns null otherwise. The file is written to the user's OS-specific
     * temporary directory.
     *
     * @param dotStr the dot source string to write
     * @return the file object if writing was successful, null otherwise.
     */
    private File writeDotSourceToFile(String dotStr) {
        File temp;
        try {
            temp = File.createTempFile("graph_", ".dot.tmp", new File(TMP_PATH));
            FileWriter fout = new FileWriter(temp);
            fout.write(dotStr);
            fout.close();
            return temp;
        } catch (IOException e) {
            System.err.println(
                "ERROR: Something went wrong printing the graph visualization string to file. \n"
                    + "This is likely a problem with your Graphviz installation.");
            return null;
        }
    }

    private byte[] getImgStream(File dotSourceFile, String type, String representationType,
                                String dpi) {
        File imgFile = null;
        byte[] imageStream = null;

        try {
            imgFile = File.createTempFile("graphviz_", "." + type, new File(TMP_PATH));
            Runtime rt = Runtime.getRuntime();
            Process p;

            String[] args = {DOT, "-T", type, "-K", representationType, "-Gdpi=" + dpi,
                dotSourceFile.getAbsolutePath(), "-o",
                imgFile.getAbsolutePath()};
            p = rt.exec(args);

            p.waitFor();

            FileInputStream finput = new FileInputStream(imgFile.getAbsolutePath());
            imageStream = new byte[finput.available()];
            finput.read(imageStream);
            finput.close();
            imgFile.delete();

        } catch (IOException e) {
            System.err.println(
                "ERROR: An error occurred either creating, closing, or deleting the temporary image file created by "
                    +
                    "Graphviz. The program will continue to run normally, but be aware that phantom files may"
                    + "exist in your temporary folder.");
        } catch (InterruptedException ie) {
            System.err.println(
                "ERROR: The dot process was interrupted while creating graph image files.");
        } finally {
            try {
                if (dotSourceFile != null) {
                    dotSourceFile.delete();
                }
                if (imgFile != null) {
                    imgFile.delete();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return imageStream;
    }
}

