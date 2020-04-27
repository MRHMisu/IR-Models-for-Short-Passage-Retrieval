package ac.ucl.statistics.text_processor;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for performing stemming for a given word o word list.
 */

public class WordStemmer {

    private static PorterStemmer stemmer = new PorterStemmer();

    /**
     * Perform Stemming for a single word by using the porter stemmer.
     *
     * @param word: A single word as string.
     * @return: word after performing stemming.
     */
    public static String getStemmedWord(String word) {
        stemmer.reset();
        stemmer.stem(word);
        return stemmer.toString();
    }

    /**
     * Perform Stemming for a list of words.
     *
     * @param words : A list of word
     * @return: A list of stemmed word .
     */
    public static List<String> getStemmedWord(List<String> words) {
        List<String> stemmedWord = new ArrayList<String>();
        for (String w : words) {
            stemmedWord.add(getStemmedWord(w));
        }
        return stemmedWord;
    }
}
