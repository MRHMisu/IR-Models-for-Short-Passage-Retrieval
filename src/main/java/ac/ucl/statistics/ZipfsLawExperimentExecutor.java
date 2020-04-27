package ac.ucl.statistics;


import ac.ucl.statistics.text_processor.ZipfsLawProof;
import ac.ucl.utility.ConfigurationReader;

/**
 * This is a test class that verify Zipf's Law pattern from a given passage collection.
 */
public class ZipfsLawExperimentExecutor {

    public static void main(String[] args) {
        System.out.println("Experiment Started:...............");
        long start = System.currentTimeMillis();
        final String passes_Collection_Path = ConfigurationReader.getPropertyByName("passage_collection");
        final String zifsLawParameterValueReport = ConfigurationReader.getPropertyByName("Zipfs_Law_Values_Report");
        ZipfsLawProof zp = new ZipfsLawProof(passes_Collection_Path, zifsLawParameterValueReport);
        zp.generateZipfsLawValuesTop100Words();

        long end = System.currentTimeMillis();
        double timeTaken = (end - start) / 1000.00;
        System.out.println("Zipf's Law Pattern Building Completed " + timeTaken + " sec");
        System.out.println("Output CSV file is stored in: " + zifsLawParameterValueReport);
    }


}

