package ac.ucl.irmodel.model;

/**
 * This class is model of Raked passage that contain the rank, score and other information to produce the result.
 */
public class RankedPassage implements Comparable<RankedPassage> {

    private int qid;// Query ID
    private int pid;// Passage ID
    private int rank;// Rank of the passage after scoring
    private double score;// Calculated score for is passage.
    private String algorithmName;// Executed Algorithm.
    private final String ASSIGNMENT_NAME = "A1";

    public RankedPassage(int qid, int pid, double score, String algorithmName) {
        this.qid = qid;
        this.pid = pid;
        this.score = score;
        this.algorithmName = algorithmName;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    @Override
    public String toString() {
        return this.qid +" "+ this.ASSIGNMENT_NAME +" "+ this.pid +" "+ this.rank +" "+ this.score +" "+ this.algorithmName;
    }
    @Override
    public int hashCode() {
        int hashCode = 31;
        hashCode += 31 * this.qid;
        hashCode += 31 * this.pid;
        hashCode += 31 * this.score;
        return hashCode;
    }
    @Override
    public int compareTo(RankedPassage rp) {
        if (this.score > rp.score)
            return -1;// for descending Order
        else if (this.score < rp.score)
            return 1;// for descending Order
        return 0;
    }
}
