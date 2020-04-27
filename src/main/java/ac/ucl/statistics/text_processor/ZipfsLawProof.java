package ac.ucl.statistics.text_processor;


import ac.ucl.statistics.model.Word;
import ac.ucl.utility.FileUtility;

import java.util.*;

/**
 * This class helps to produce Zif's Law pattern from the given passage collection.
 */

public class ZipfsLawProof {

    private String collectionPath;
    private String outputPath;

    public ZipfsLawProof(String collectionPath, String outputPath) {
        this.collectionPath = collectionPath;
        this.outputPath = outputPath;
    }

    /**
     * Generate the Zipf's Law values such as frequency, rank, probability for the given passage collections.
     */
    public void generateZipfsLawValuesTop100Words() {
        List<String> terms = getAllTerms();
        List<Word> words = getWords(terms);
        String header = "Word,Freq,r,Pr,r*Pr";
        List<String> parametersValues = new LinkedList<String>();
        parametersValues.add(header);
        for (int i = 0; i < 100; i++) {
            parametersValues.add(words.get(i).toString());
        }
        FileUtility.write(parametersValues, this.outputPath);
        ZipfsPatternChartBuilder zp = new ZipfsPatternChartBuilder(words.subList(0, 100));
        zp.generateChartAndSaveToFile();
    }


    /**
     * Generate all terms from a given passage collection.
     *
     * @return: A list of all terms as tokens.
     */
    private List<String> getAllTerms() {
        List<String> passageList = FileUtility.readLines(this.collectionPath);
        List<String> words = new LinkedList<String>();
        for (String p : passageList) {
            TextPreProcessor tp = new TextPreProcessor(p);
            words.addAll(tp.getNormalizedTokensWithStopWords());
        }
        return words;
    }

    /**
     * Produce Words for a given token list.
     *
     * @return: A list of Words.
     */
    private List<Word> getWords(List<String> terms) {
        int total_terms = terms.size();
        Map<String, Integer> map = countTermFrequency(terms);
        Set<Word> wordSet = new TreeSet<Word>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String term = entry.getKey();
            long frequency = entry.getValue();
            double probability = (frequency / (double) total_terms);
            Word _word = new Word(term, frequency, probability);
            wordSet.add(_word);
        }
        List<Word> words = new ArrayList<Word>(wordSet);
        for (int i = 0; i < words.size(); i++) {
            words.get(i).setRank(i + 1);
        }
        return words;
    }

    /**
     * Count term frequency for a list of tokens generate from a passage.
     *
     * @param tokens: A list of tokens
     * @return: A map contains term and its frequency pairs in form of (term->frequency)
     */
    private Map<String, Integer> countTermFrequency(List<String> tokens) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        tokens.forEach(token -> {
            if (map.containsKey(token)) {
                map.put(token, (map.get(token) + 1));
            } else
                map.put(token, 1);
        });
        return map;
    }

}
