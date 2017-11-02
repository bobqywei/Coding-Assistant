package edu.cmu.sphinx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import edu.cmu.sphinx.api.*;

public class exp {
    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        config.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        config.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        config.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        LiveSpeechRecognizer rec = new LiveSpeechRecognizer(config);
        rec.startRecognition(true);

        SpeechResult result;
        String words = "";
        PrintWriter writer = new PrintWriter(new FileOutputStream("src/main/java/edu/cmu/sphinx/output.txt", false));
        while ((result = rec.getResult()) != null) {
        		words += result.getHypothesis() + " ";
            System.out.println(result.getHypothesis());
            if (result.getHypothesis().equals("stop")) {
            		break;
            }
        }
        writer.println(words);
        writer.close();
        rec.stopRecognition();
    }
}