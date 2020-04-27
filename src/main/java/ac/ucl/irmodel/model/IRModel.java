package ac.ucl.irmodel.model;


import ac.ucl.invertedindex.IndexBuilder;
import ac.ucl.invertedindex.model.CandidatePassage;
import ac.ucl.statistics.text_processor.TextPreProcessor;

import java.util.*;

public class IRModel {

    public int queryID;
    public String query;
    public Map<String, List<CandidatePassage>> index;
    public Map<Integer, String> pidPassagePair;
    public int totalTermInTheQuery;
    public int totalNumberOfPassage;
    public Map<String, Integer> queryTermFrequency;

    public IRModel(Map<Integer, String> pidPassagePair, int queryID, String query) {
        this.pidPassagePair = pidPassagePair;
        this.queryID = queryID;
        this.query = query;
        initialize();
    }

    public void initialize() {
        this.index = IndexBuilder.buildInvertedIndex(this.pidPassagePair);
        this.totalNumberOfPassage = this.pidPassagePair.size();
        TextPreProcessor textPreProcessor = new TextPreProcessor(this.query);
        this.totalTermInTheQuery = textPreProcessor.getNormalizedTokens().size();
        this.queryTermFrequency = textPreProcessor.getTokenTermFrequency();
    }

    public Set<CandidatePassage> getCandidatePassages() {
        List<CandidatePassage> relevantCandidatePassages = new ArrayList<>();
        Map<Integer, CandidatePassage> relevantPassage = new HashMap<>();
        for (String term : this.queryTermFrequency.keySet()) {
            if (this.index.get(term) != null)
                relevantCandidatePassages.addAll(this.index.get(term));
        }
        relevantCandidatePassages.addAll(relevantPassage.values());
        return new HashSet<>(relevantCandidatePassages);
    }

    public List<RankedPassage> getTopRankedPassages(List<RankedPassage> rankedPassages) {
        for (int i = 0; i < rankedPassages.size(); i++)
            rankedPassages.get(i).setRank(i + 1);
        if (rankedPassages.size() < 100) return rankedPassages;
        return rankedPassages.subList(0, 100);
    }


}
