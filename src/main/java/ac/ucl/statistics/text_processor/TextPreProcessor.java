package ac.ucl.statistics.text_processor;

import java.util.*;

public class TextPreProcessor {

    private String passage;

    public TextPreProcessor(String passage) {
        this.passage = passage;
    }


    /**
     * Generate a stream of tokes from a passage that also includes the stop words.
     *
     * @return: A list of tokens
     */
    public List<String> getNormalizedTokensWithStopWords() {
        List<String> tokens = tokenizePassage(this.passage);
        tokens = removeSpecialCharacters(tokens);
        tokens = stemWord(tokens);
        return tokens;
    }

    /**
     * Generate a stream of tokes from a passage that also includes the stop words.
     *
     * @return: A list of tokens.
     */
    public List<String> getNormalizedTokens() {
        List<String> tokens = tokenizePassage(this.passage);
        tokens = removeSpecialCharacters(tokens);
        tokens = removeStopWords(tokens);
        tokens = stemWord(tokens);
        return tokens;
    }

    /**
     * Calculate the token length/ Passage length
     *
     * @return: Number of tokens contained in  passage.
     */
    public int getNormalizedTokenPassageLength() {
        return getNormalizedTokens().size();
    }

    /**
     * Generate unique tokes from a given passage.
     *
     * @return: A Set of unique tokens.
     */
    public Set<String> getNormalizedUniqueTokens() {
        return new TreeSet<>(getNormalizedTokens());
    }

    /**
     * Generate token term->frequency as a Map for a given passage.
     *
     * @return: A map that contains unique tokens with its frequency as (term->frequency) form
     */
    public Map<String, Integer> getTokenTermFrequency() {
        List<String> tokens = getNormalizedTokens();
        return countTermFrequency(tokens);
    }

    /**
     * Tokenize a passage split by with space
     *
     * @param passage: A given passage as string.
     * @return: A list of string as tokens.
     */
    private List<String> tokenizePassage(String passage) {
        String[] words = passage.split("\\s+");
        List<String> tokens = new ArrayList<String>();
        for (String word : words) {
            if (!(word.equals("") || word.equals(" ")))
                tokens.add(word.toLowerCase().trim());
        }
        return tokens;
    }

    /**
     * Remove all special characters from a list of tokens.
     *
     * @param tokens: A list of tokens generated from a passage.
     * @return: A list of tokens containing no special characters.
     */
    private List<String> removeSpecialCharacters(List<String> tokens) {
        List<String> cleanTokens = new ArrayList<String>();
        tokens.forEach(token -> {
            cleanTokens.add(token.replaceAll("[^a-zA-Z0-9]", "").trim());
        });
        String[] space = {"", " "};
        cleanTokens.removeAll(Arrays.asList(space));
        return cleanTokens;
    }

    /**
     * Remove all stop words from a list of tokens.
     *
     * @param tokens: A list of tokens generated from a passage.
     * @return: A list of tokens containing no stop words.
     */
    private List<String> removeStopWords(List<String> tokens) {
        return StopWordRemover.removeStopWord(tokens);
    }

    /**
     * Stem a wrord by reducing redundant suffix.
     *
     * @param tokens: A list of tokens generated from a passage.
     * @return: A list of stemmed tokens.
     */
    private List<String> stemWord(List<String> tokens) {
        return WordStemmer.getStemmedWord(tokens);
    }

    /**
     * Count term frequency for a list of tokens generate from a passage.
     *
     * @param tokens: A list of tokens
     * @return: A map contains term and its frequency pairs in form of (term->frequency)
     */
    public Map<String, Integer> countTermFrequency(List<String> tokens) {
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
