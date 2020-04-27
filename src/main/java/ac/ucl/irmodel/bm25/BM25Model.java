package ac.ucl.irmodel.bm25;

import ac.ucl.invertedindex.model.CandidatePassage;
import ac.ucl.irmodel.model.IRModel;
import ac.ucl.irmodel.model.RankedPassage;
import ac.ucl.statistics.text_processor.TextPreProcessor;

import java.util.*;


/**
 * This class builds the BM25 model for the given passage collection and a query id.
 * And generates a list of top ranked passage.
 */

public class BM25Model {

    //The number of relevant documents containing term i,
    private final double r_i = 0;
    //R is the number of relevant documents the query.
    private final double R = 0;
    //𝑘1 determines how the term frequency weight changes as fi increases
    //𝑘1=1.2 as in text retrieval experiments this is considered to be the typical value
    private final double k1 = 1.2;
    //𝑘2 determines how the query term weight fluctuates when qfi increases.
    // The value for𝑘2typically ranges from 0 to 1000. Here we use the value𝑘1=100
    private final double k2 = 100;
    /**
     * The constant b regulates the impact of the length normalization
     * where b = 0 corresponds to no length normalization, and
     * b = 1 is full normalization. In TREC experiments, a value of b = 0.75 was found
     * to be effective
     */
    private final double b = 0.75;
    //avdl is the average length of a document in the collection
    private double adl;


    private IRModel irModel;

    public BM25Model(IRModel irModel) {
        this.irModel = irModel;
    }

    /**
     * Execute the BM25 model for a given passage collection and query.
     *
     * @return: A list of top ranked passages.
     */
    public List<RankedPassage> executeBM25() {
        Set<CandidatePassage> candidatePassageSet = this.irModel.getCandidatePassages();
        adl = calculateAdl();
        List<RankedPassage> allRankedPassage = createRankedPassages(candidatePassageSet);
        return this.irModel.getTopRankedPassages(allRankedPassage);
    }

    /**
     * Create ranked passages for a given candidate passage set.
     *
     * @param candidatePassageSet: A set of candidate passages.
     * @return: A list of ranked passages.
     */
    private List<RankedPassage> createRankedPassages(Set<CandidatePassage> candidatePassageSet) {
        Set<RankedPassage> rankedPassageSet = new TreeSet<RankedPassage>();
        for (CandidatePassage ind : candidatePassageSet) {
            double score = calculateScoreBM25(ind);
            rankedPassageSet.add(new RankedPassage(this.irModel.queryID, ind.getPid(), score, "BM25"));
        }
        return new ArrayList<>(rankedPassageSet);
    }

    /**
     * Calculate the score the whole query in a given indexed passage.
     *
     * @param cp: candidate passage.
     * @return : The overall score for a given query.
     */
    private double calculateScoreBM25(CandidatePassage cp) {
        double overallScore = 0;
        int dl = cp.getTotalTermsInThisPassage();///dl is the length of the document
        int fi = cp.getFrequencyOfThatTerm();////fi= is the frequency of term i in the document;
        double K = calculateComplexParameter_K(dl);
        for (String queryTerm : this.irModel.queryTermFrequency.keySet()) {
            overallScore += calculateScoreForEachQueryTerm(queryTerm, dl, fi, K);
        }
        return overallScore;
    }

    /**
     * Calculate the score for each query term in a given indexed passage.
     *
     * @param queryTerm: each unique term from the query string.
     * @param dl:        number of terms in a given passage.
     * @param fi:        frequency of the given queryTerm in that passage
     * @param K:         the complex parameter
     * @return: Score for a single term of the query.
     */
    private double calculateScoreForEachQueryTerm(String queryTerm, int dl, int fi, double K) {
        // ni= total number of documents where the term appear;
        int ni = 0;
        if (this.irModel.index.get(queryTerm) != null) ni = this.irModel.index.get(queryTerm).size();
        // N=total number of documents in the collection;
        int N = this.irModel.pidPassagePair.size();
        //qfi= is the frequency of term i in the query
        int qfi = this.irModel.queryTermFrequency.get(queryTerm);
        //dl= is the length of the document
        //K is a more complicated parameter that normalizes the tf component by document length
        double idf_portion = ((r_i + 0.5) / (R - r_i + 0.5)) / ((ni - r_i + 0.5) / (N - ni - R + r_i + 0.5));
        double tf_portion = (((k1 + 1) * fi) / (K + fi)) * (((k2 + 1) * qfi) / (k2 + qfi));
        // use e base natural logarithm
        double score = ((Math.exp(idf_portion)) * tf_portion);
        return score;
    }

    /**
     * Calculate the complex parameters K that normalizes the tf component by document length
     *
     * @param dl: document length;
     * @return complex parameter K
     */
    private double calculateComplexParameter_K(int dl) {
        //dl is the length of the document
        double dl_adl = (dl / adl);
        double K = k1 * ((1 - b) + b * dl_adl);
        return K;
    }

    /**
     * Calculate the average document length (avdl) of a collection
     *
     * @return: adl for a given collection.
     */
    private double calculateAdl() {
        int sumOfAllPassageLength = 0;
        for (String s : this.irModel.pidPassagePair.values()) {
            TextPreProcessor tp = new TextPreProcessor(s);
            sumOfAllPassageLength += tp.getNormalizedTokenPassageLength();
        }
        double adl = (sumOfAllPassageLength / (double) this.irModel.pidPassagePair.size());
        return adl;
    }


}
