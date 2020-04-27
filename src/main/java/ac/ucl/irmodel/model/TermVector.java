package ac.ucl.irmodel.model;

import java.util.Map;


/**
 * This class represent a term vector that contains the term and TF-IDF score for each term in a map.
 */
public class TermVector {

    private int id;// pid for passage vector and qid for query vector.
    private Map<String, Double> termScoreMap;//(term->score) pair
    public TermVector(int id, Map<String, Double> termScoreMap) {
        this.id = id;
        this.termScoreMap = termScoreMap;
    }
    public int getId() {
        return id;
    }
    public Map<String, Double> getTermScoreMap() {
        return termScoreMap;
    }

    /**
     * Calculate the length of this vector following associated  vector length calculation formula.
     * @return: The length of the vector.
     */
    public double getNormalizedLength() {
        // vector_length=sqrt(i^2+j^2+k^2+........)
        double normalizedLength = 0;
        for (Double value : termScoreMap.values()) {
            normalizedLength += (Math.pow(value, 2));
        }
        return Math.sqrt(normalizedLength);
    }

    @Override
    public String toString() {
        return "PassageVector{" +
                "pid=" + id +
                ", vector=" + termScoreMap +
                '}';
    }
}



