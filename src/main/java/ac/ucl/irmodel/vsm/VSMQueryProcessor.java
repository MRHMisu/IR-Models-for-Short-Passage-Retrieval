package ac.ucl.irmodel.vsm;

import ac.ucl.irmodel.model.IRModel;
import ac.ucl.irmodel.model.RankedPassage;
import ac.ucl.utility.ConfigurationReader;
import ac.ucl.utility.FileUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * This class is  main executor class for Vector Space model.
 * It takes files path from the config.properties file and load all the passages and query.
 * Then run Vector Space  model for each query and its associated passages.
 * And stores the output in the given file location in config.properties file.
 */

public class VSMQueryProcessor {

    public static void main(String[] args) {
        long startEx = System.currentTimeMillis();
        String candidate_passages_top1000 = ConfigurationReader.getPropertyByName("candidate_passages_top1000");
        String test_query = ConfigurationReader.getPropertyByName("test_queries");
        String vsm_result_output = ConfigurationReader.getPropertyByName("VSM_Result_Output");

        List<String> linesFromCandidatePassage = FileUtility.readLines(candidate_passages_top1000);
        Map<Integer, String> queryID_query_pair = FileUtility.getTestQueries(test_query);
        List<RankedPassage> allRankedPassage = new ArrayList<>();

        int totalLineProcessed = 0;
        int count = 0;
        for (Integer qid : queryID_query_pair.keySet()) {
            String query = queryID_query_pair.get(qid);
            // create VSM
            //pass query ID  and  linesFromCandidatePassage to VSM so that it can build index;
            long start = System.currentTimeMillis();
            count++;
            System.out.println("Serial No: " + count);
            System.out.println("Query ID:" + qid + " is processing....");
            Map<Integer, String> pidPassagePair = FileUtility.getCandidatePassagesByQueryID(linesFromCandidatePassage, qid);
            System.out.println(pidPassagePair.size() + " Candidate passages are found by Query ID: " + qid);
            totalLineProcessed += pidPassagePair.size();
            IRModel irModel = new IRModel(pidPassagePair, qid, query);
            VectorSpaceModel vsm = new VectorSpaceModel(irModel);
            List<RankedPassage> rankedPassage = vsm.executeVSM();
            allRankedPassage.addAll(rankedPassage);
            long end = System.currentTimeMillis();
            System.out.println("Query id:" + qid + " is processed in " + ((end - start) / 1000.00) + " second");
            System.out.println("-------------------------------------------------------------------");
            System.out.println();
        }

        if (linesFromCandidatePassage.size() == totalLineProcessed) {
            System.out.println("All line processed successfully");
        } else {
            System.out.println((linesFromCandidatePassage.size() - totalLineProcessed) + " lines cannot be processed");
        }
        List<String> result = allRankedPassage.stream().map(x -> x.toString()).collect(Collectors.toList());

        FileUtility.write(result, vsm_result_output);

        long endEx = System.currentTimeMillis();
        System.out.println("Full experiment is completed in " + ((endEx - startEx) / 1000.00) + " second");
    }


}
