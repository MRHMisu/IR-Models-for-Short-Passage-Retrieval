package ac.ucl.invertedindex;

import ac.ucl.invertedindex.model.CandidatePassage;
import ac.ucl.statistics.text_processor.TextPreProcessor;

import java.util.*;

/**
 * This class construct inverted index for an given passage collection.
 */

public class IndexBuilder {

    /**
     * @param passageCollection: Passage Collection in form of (pid->Passage) pair.
     * @return A Map as inverted index contains (term->List<CandidatePassage>) pair.
     * This method is used as a static method to build basic IR model VS and BM25.
     */
    public static Map<String, List<CandidatePassage>> buildInvertedIndex(Map<Integer, String> passageCollection) {
        long start = System.currentTimeMillis();
        List<Map<String, CandidatePassage>> indexedPidPassagePairs = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : passageCollection.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            TextPreProcessor tp = new TextPreProcessor(value);
            List<String> tokens = tp.getNormalizedTokens();
            indexedPidPassagePairs.add(getTermPassageMap(tokens, key));
        }
        System.out.println("Processed " + passageCollection.size() + " passages for indexing");
        Map<String, List<CandidatePassage>> invertedIndex = new HashMap<>();
        for (Map<String, CandidatePassage> m : indexedPidPassagePairs) {
            for (Map.Entry<String, CandidatePassage> entry : m.entrySet()) {
                String key = entry.getKey();
                CandidatePassage V = entry.getValue();

                if (invertedIndex.containsKey(key)) {
                    invertedIndex.get(key).add(V);
                } else {
                    List<CandidatePassage> cp = new ArrayList<CandidatePassage>();
                    cp.add(V);
                    invertedIndex.put(key, cp);
                }
            }
        }
        long end = System.currentTimeMillis();
        double timeTaken = (end - start) / 1000.00;
        System.out.println("Index building is completed in:" + timeTaken + " second");
        return invertedIndex;
    }

    /**
     * @param tokens :Generated tokens from the passage text
     * @param pid    :Passage identifier
     * @return A map of term->Candidate Passage pair.
     */
    private static Map<String, CandidatePassage> getTermPassageMap(List<String> tokens, int pid) {
        Map<String, CandidatePassage> map = new HashMap<>();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (map.containsKey(token)) {
                map.get(token).addNewPositions(i);
            } else {
                Set<Integer> positions = new TreeSet<Integer>();
                positions.add(i);
                map.put(token, new CandidatePassage(pid, positions, 1, tokens.size()));
            }
        }
        return map;
    }
}
