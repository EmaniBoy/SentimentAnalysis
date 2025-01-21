package com.sentiment;
import org.json.JSONArray;
import org.json.JSONObject;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


public class App {
    private static DoccatModel model;
    
    public static void main(String[] args) {
        try {
            // Load pre-trained sentiment analysis model
            model = new DoccatModel(new FileInputStream("opennlp-en-ud-ewt-sentence-1.2-2.5.0.bin"));
            JSONArray reviews = loadReviews("reviews.json");

            for (int i = 0; i < reviews.length(); i++) {
                JSONObject reviewObj = reviews.getJSONObject(i);
                String review = reviewObj.getString("review");
                String sentiment = analyzeSentiment(review);
                System.out.println("Review: " + review + " --> Sentiment: " + sentiment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static JSONArray loadReviews(String filePath) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return new JSONArray(content);
    }

    private static String analyzeSentiment(String review) {
        DocumentCategorizerME categorizer = new DocumentCategorizerME(model);
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        double[] outcomes = categorizer.categorize(tokenizer.tokenize(review));

        return categorizer.getBestCategory(outcomes).equals("1") ? "Positive" : "Negative";
    }
}
