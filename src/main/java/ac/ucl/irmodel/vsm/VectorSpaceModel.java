package ac.ucl.irmodel.vsm;


import ac.ucl.invertedindex.model.CandidatePassage;
import ac.ucl.irmodel.model.IRModel;
import ac.ucl.irmodel.model.RankedPassage;
import ac.ucl.irmodel.model.TermVector;
import ac.ucl.statistics.text_processor.TextPreProcessor;

import java.util.*;

public class VectorSpaceModel {

    private IRModel irModel;

    public VectorSpaceModel(IRModel irModel) {
        this.irModel = irModel;
    }

    /**
     * Execute the VS model for a given passage collection and query.
     *
     * @return: A list of top ranked passages.
     */
    public List<RankedPassage> executeVSM() {
        Set<CandidatePassage> candidatePassageSet = this.irModel.getCandidatePassages();
        Set<TermVector> passageTermVectorSet = buildPassageVectors(candidatePassageSet);
        TermVector queryTermVector = buildQueryVector();
        List<RankedPassage> allRankedPassage = createRankedPassages(passageTermVectorSet, queryTermVector);
        return this.irModel.getTopRankedPassages(allRankedPassage);
    }

    /**
     * Create ranked passages for a given set of passage vector and query vector.
     *
     * @param passageTermVectorSet : A set of passage term vectors.
     * @param queryTermVector;     Query term vectors.
     * @return A list of ranked passages.
     */
    private List<RankedPassage> createRankedPassages(Set<TermVector> passageTermVectorSet, TermVector queryTermVector) {
        Set<RankedPassage> rankedPassageSet = new TreeSet<>();
        for (TermVector pv : passageTermVectorSet) {
            double score = calculateCosineSimilarity(queryTermVector, pv);
            rankedPassageSet.add(new RankedPassage(queryTermVector.getId(), pv.getId(), score, "VS"));
        }
        return new ArrayList<>(rankedPassageSet);
    }

    /**
     * Construct query vectors for a given query.
     *
     * @return: A Query term vector containing the term and associated score.
     */
    private TermVector buildQueryVector() {
        Map<String, Double> vector = new HashMap<>();
        for (Map.Entry<String, Integer> entry : this.irModel.queryTermFrequency.entrySet()) {
            String term = entry.getKey();
            Integer frequency = entry.getValue();
            double tf = (frequency / (double) this.irModel.totalTermInTheQuery);
            double idf = calculateIDF(term);
            vector.put(term, (tf * idf));
        }
        return new TermVector(this.irModel.queryID, vector);
    }

    /**
     * Construct passage vectors for a given candidate passage set.
     *
     * @param candidatePassage: A set of candidate passage derived from index.
     * @return: A set of term vectors containing the term and associated score.
     */
    private Set<TermVector> buildPassageVectors(Set<CandidatePassage> candidatePassage) {
        Set<TermVector> termVectors = new HashSet<TermVector>();
        for (CandidatePassage idp : candidatePassage) {
            Map<String, Double> vector = new HashMap<>();
            TextPreProcessor _textPreProcessor = new TextPreProcessor(this.irModel.pidPassagePair.get(idp.getPid()));
            Set<String> terms = _textPreProcessor.getNormalizedUniqueTokens();
            terms.forEach(term -> {
                double tf_Idf_Score = calculateTFIDFScore(term, idp.getPid());
                vector.put(term, tf_Idf_Score);
            });
            termVectors.add(new TermVector(idp.getPid(), vector));
        }
        return termVectors;
    }

    /**
     * Calculate Tf-Idf score for the given term and passage id.
     *
     * @param term: A term in the passage.
     * @param pid:  Passage id where the Tf-Idf is to be counted.
     * @return: Tf-Idf score for that term and given passage.
     */
    private double calculateTFIDFScore(String term, int pid) {
        return (calculateTF(term, pid)) * calculateIDF(term);
    }

    /**
     * Calculate term frequency for the given term and passage id.
     *
     * @param term: A term in the passage.
     * @param pid:  Passage id where the term frequency is to be counted.
     * @return: TF score for that term and given passage.
     */
    private double calculateTF(String term, int pid) {
        List<CandidatePassage> passage = this.irModel.index.get(term);
        double tf = 0;
        for (CandidatePassage candidatePassage : passage) {
            if (candidatePassage.getPid() == pid) {
                // 1+log(t_frequency)
                tf = 1 + Math.log10(candidatePassage.getFrequencyOfThatTerm());
                return tf;
            }
        }
        return tf;
    }

    /**
     * Calculate IDF for a given term
     *
     * @param term: Represent a term from the given passage collection.
     * @return : The IDF score for this given term.
     */
    private double calculateIDF(String term) {
        //df(t)=number of documents containing t
        if (this.irModel.index.get(term) != null) {
            double df = this.irModel.index.get(term).size();
            //|C|=number of documents in the collection
            double C = this.irModel.totalNumberOfPassage;
            // IDF=log10(|C|/df)
            double idf = Math.log10(C / df);
            return idf;
        }
        return 0;

    }

    /**
     * Calculate cosine-similarity between passage vector and query vector
     *
     * @param query:   Represents a query term vector.
     * @param passage: Represents a passage term vector.
     * @return: A similarity score based on cosine-similarity function.
     */
    private double calculateCosineSimilarity(TermVector query, TermVector passage) {
        double totalScore = 0;
        for (String queryTerm : query.getTermScoreMap().keySet()) {
            double termScoreInPassage = 0;
            if (passage.getTermScoreMap().containsKey(queryTerm)) {
                termScoreInPassage = passage.getTermScoreMap().get(queryTerm);
            }
            double termScoreInQuery = query.getTermScoreMap().get(queryTerm);
            totalScore += (termScoreInPassage * termScoreInQuery);
        }
        double queryLength = query.getNormalizedLength();
        double passageLength = passage.getNormalizedLength();
        double denominator = (queryLength * passageLength);
        double cosineScore = 0;
        if (denominator != 0) cosineScore = (totalScore / (queryLength * passageLength));
        return cosineScore;
    }

}
