# IRDM Course Project Part-I


#######################################
#       Completed by:
#       Candidate Number: GCVN7
#       Student Number: 19144504
#######################################

Description: This project is developed as a coursework of Information Retrieval and Data Mining module.
Various subtasks are completed according to the coursework requirements.

#######################################
#       Dataset File Path Configuration.
#######################################
This project contains a configuration file to configure the dataset file paths for this project. The file can be found in:

#-IRDM-Project/src/config.properties

Initially all dataset files are now stored into this location -> IRDM-Project/src/dataset/
If you want to change the location, just change the file path in the config.properties.

***Reminder: Never change the configuration key only change the file path if necessary.

######################################
#   Runtime Environment:
######################################
The project development is performed on Java 11. Therefore, the The the preference is Java 11.
However, it should be on Java 8.
Besides, to have a smooth run, it is suggested that import this project folder IRDM-Project in an IDE such as IntelliJ or Eclipse as a Java Project.
Then also import the following external jar file into the build path.
IRDM-Project-
    |src/ex-lib/jfreechart-1.5.0.jar.

And then run the  instructed files described below.

#######################################
#       Subtask-2: Text Statistics
#######################################
An experiment is develop to show the words from the given passage collection follow Zipf's Law patters.
To run this experiment. Go to the the following location.

IRDM-Project-
    |src/statistics
                --ZipfsLawExperimentExecutor.java

*** Run this file and it will generates the Zipf's patterns for top 100 words in a image file and also
reports the values of those 100 words in a csv file. The output files are stored in the following directory.

IRDM-Project-
    |src/result/
           --ZipfsLawValuesReport.csv
           --Zipfs_Law_Pattern.JPEG
***Only for building the the chart only an external libraby is used called jfreechart.jar.

The implementation of the various text processing can be found in the following location.
IRDM-Project-
    |src/statistics/text_processor/

All processings are implemented by me except the PorterStemmer.java
*** Here publicly availavel Porter Stemmer is used.

######################################
#       Subtask-3: Inverted Index
######################################
Inverted index can be constructed for a given passage collections. To see how the inverted index is implemented,
view the following files.

IRDM-Project-
    |src/invertedindex/
            --IndexBuilder.java
            --TestInvertedIndex.java

IndexBuilder.java, this file is the actual index builder and to test the index builder TestInvertedIndex.java can be run.
It builds index for a given query id (qid) by collecting the passages that are paired with the qid in the
candidate_passages_top1000.tsv file.

######################################
#    Subtask-4: Basic Retrieval Models
######################################

In this subtask two basic IR models are implemented.

######################################
#   Vector Space (VS) Model:
######################################

The implementation of the VS can be found in the following location:

IRDM-Project-
    |src/
     --|irmodel/vsm/
                --VectorSpaceModel.java
                --VSMQueryProcessor.java

The file VectorSpaceModel.java contains the actual implementation of VS model. To the the model for the
passage collections and queries, just run file VSMQueryProcessor.java. An output file will be generated
that can be found in the following location.

IRDM-Project-
    |src/result/
           --VS.txt

######################################
#    BM25 Model
######################################

The implementation of the BM25  can be found in the following location:

IRDM-Project-
     |src/
       --|irmodel/bm25/
                    --BM25Model.java
                    --BM25QueryProcessor.java

The file BM25Model.java contains the actual implementation of BM25 model. To the the model for the
passage collections and queries, just run file BM25QueryProcessor.java. An output file will be generated
that can be found in the following location.

IRDM-Project-
    |src/result/
           --BM25.txt