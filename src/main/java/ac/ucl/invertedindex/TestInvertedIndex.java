package ac.ucl.invertedindex;


import ac.ucl.utility.ConfigurationReader;
import ac.ucl.utility.FileUtility;

import java.util.List;
import java.util.Map;

/**
 * This is a test class to verify index building for a given passage collection.
 */
public class TestInvertedIndex {

    public static void main(String[] args) {
        final String passesCollectionPath = ConfigurationReader.getPropertyByName("candidate_passages_top1000");
        List<String> list = FileUtility.readLines(passesCollectionPath);
        Map<Integer, String> pidPassagePair = FileUtility.getCandidatePassagesByQueryID(list, 1113437);
        IndexBuilder.buildInvertedIndex(pidPassagePair);
    }
}


