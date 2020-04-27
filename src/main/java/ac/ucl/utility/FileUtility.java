package ac.ucl.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;


/**
 * This class FileUtil is used for all generic file operations.
 */

public class FileUtility {


    /**
     * Get all the passage for a given query id.
     *
     * @param candidatePassage: A list of string represents all the passages of the collection.
     * @param qid:              Query id
     * @return: A map with passage id and the passage in the from of (pid->Passage) pair
     */
    public static Map<Integer, String> getCandidatePassagesByQueryID(List<String> candidatePassage, int qid) {
        Map<Integer, String> pidPassagePair = new HashMap<>();
        candidatePassage.forEach(cp -> {
            List<String> line = Arrays.asList(cp.split("\\t"));
            //Each line represent a tuple of qid,pid,query,passage
            if (Integer.parseInt(line.get(0)) == qid) {
                pidPassagePair.put(Integer.parseInt(line.get(1)), line.get(3));
            }
        });
        return pidPassagePair;
    }

    /**
     * Get all the test quires from the given test_query file path.
     *
     * @param filePath : Represents the file path.
     * @return: A map contains the query id and the query in the form of (qid->query) pair.
     */
    public static Map<Integer, String> getTestQueries(String filePath) {
        Map<Integer, String> qid_query_pair = new HashMap<>();
        List<String> candidatePassage = FileUtility.readLines(filePath);
        candidatePassage.forEach(cp -> {
            List<String> line = Arrays.asList(cp.split("\\t"));
            qid_query_pair.put(Integer.parseInt(line.get(0)), line.get(1));
        });
        return qid_query_pair;
    }

    /**
     * Read a file line by line
     *
     * @param filePath: path of the file that is needed to be read
     * @return a list of string containing lines from the file
     */
    public static List<String> readLines(String filePath) {
        File file = new File(filePath);
        List<String> textLines = new ArrayList<String>();
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line = "";
                while ((line = reader.readLine()) != null) {
                    textLines.add(line);
                }
            } catch (IOException e) {
                System.err.format("IOException: %s%n", e);
            }
        }
        return textLines;
    }


    /**
     * Write output to the given file path location.
     *
     * @param text:       A list of string that has to be written in the file.
     * @param outputPath: The file path where the text has to be written.
     * @throws IOException: Throw this exception if the file path is not found any IO bound exception occurs.
     */
    public static void write(List<String> text, String outputPath) {
        StringBuilder sb = new StringBuilder();
        Writer output = null;
        try {
            output = new BufferedWriter(new FileWriter(outputPath, true));
            for (String x : text) {
                sb.append(x + '\n');
            }
            output.append(sb.toString());
            output.close();
        } catch (IOException io) {
            System.out.println(io.getStackTrace());
        } finally {

        }

    }
}
