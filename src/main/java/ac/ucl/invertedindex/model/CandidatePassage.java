package ac.ucl.invertedindex.model;

import java.util.Set;

/**
 * This class contains the candidate passage information to support index building.
 */

public class CandidatePassage {
    private int pid;// Passage ID.
    private int frequencyOfThatTerm;// frequency.
    private Set<Integer> termPositions;// positions.
    private int totalTermsInThisPassage;// length of the passage.

    public CandidatePassage(int pid, Set<Integer> termPositions, int frequencyOfThatTerm, int totalTermsInThisPassage) {
        this.pid = pid;
        this.termPositions = termPositions;
        this.frequencyOfThatTerm = frequencyOfThatTerm;
        this.totalTermsInThisPassage = totalTermsInThisPassage;
    }

    public int getPid() {
        return pid;
    }

    public int getFrequencyOfThatTerm() {
        return this.frequencyOfThatTerm;
    }

    public int getTotalTermsInThisPassage() {
        return this.totalTermsInThisPassage;
    }

    public void addNewPositions(int pos) {
        this.termPositions.add(pos);
        this.frequencyOfThatTerm++;
    }

    @Override
    public int hashCode() {
        int hashCode = 31;
        hashCode += 31 * this.pid;
        hashCode += 31 * this.frequencyOfThatTerm;
        hashCode += 31 * this.totalTermsInThisPassage;
        hashCode += 31 * this.termPositions.hashCode();

        return hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CandidatePassage that = (CandidatePassage) o;
        return pid == that.pid;
    }

    @Override
    public String toString() {
        return "IndexedPassage{" +
                "pid=" + pid +
                ", termPositions=" + termPositions +
                ", frequency=" + frequencyOfThatTerm +
                ", totalTermsInThePassage=" + totalTermsInThisPassage +
                '}';
    }
}
