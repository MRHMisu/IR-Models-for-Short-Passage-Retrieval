package ac.ucl.statistics.model;

/**
 * This class represent Word in the passage collection.
 */

public class Word implements Comparable<Word> {
    private String term;// the word
    private long frequency;// number of times it occurs in the passage collection
    private long rank;// rank in the collection
    private double probability;// the probability of the word of occurrence. .

    public Word(String term, long frequency, double probability) {
        this.term = term;
        this.frequency = frequency;
        this.probability = probability;
    }
    public long getFrequency() {
        return frequency;
    }
    public long getRank() {
        return rank;
    }
    public double getProbability() {
        return probability;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return frequency == word.frequency &&
                term.equals(word.term);
    }
    @Override
    public int hashCode() {
        int hashCode = 31;
        hashCode += 31 * this.term.hashCode();
        hashCode += 31 * this.frequency;
        return hashCode;
    }
    @Override
    public int compareTo(Word o) {

        if (this.frequency > o.frequency)
            return -1;// for descending Order
        else if (this.frequency < o.frequency)
            return 1;// for descending Order
        else if (this.frequency == o.frequency)
            return this.term.compareTo(o.term);

        return 0;
    }
    @Override
    public String toString() {
        // Word, Freq, r, Pr, r*Pr
        return this.term + "," + this.frequency + "," + this.rank + "," + this.probability + "," + (this.rank * this.probability);
    }
}
