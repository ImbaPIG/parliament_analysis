package nlp;

import bundestag.Protokoll_File_Impl;
import bundestag.Rede_File_Impl;
import bundestag.Redner_File_Impl;
import bundestag.Tagesordnungspunkt_File_Impl;
import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonFormat;
import com.mongodb.MongoSocketOpenException;
import database.Creds_File_Impl;
import database.JCasTuple_FIle_Impl;
import database.MongoDBConnectionHandler_File_Impl;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import interfaces.Analysis;
import org.apache.uima.UIMAException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.codehaus.jackson.map.ObjectMapper;
import org.hucompute.textimager.uima.type.Sentiment;
import org.bson.Document;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Analysis_File_Impl implements Analysis {

    // JCasTokenCounter
    private HashMap<String, Double> tokenCounter;

    // JCasEntityCounter
    private final HashMap<String, Double> perCounter;
    private final HashMap<String, Double> locCounter;
    private final HashMap<String, Double> orgCounter;

    // JCasPOSCounter
    private HashMap<String, Double> posCounter;

    // JCasSentimentAll
    private Double sentimentSum;
    private Double sentenceCount;

    // JCasSentimentRednerCounter
    private HashMap<String, Double> sentenceRednerCounter;
    private HashMap<String, Double> sentimentRednerSum;

    // JCasSentimentFraktionCounter
    private HashMap<String, Double> sentimentFraktionCounter;
    private HashMap<String, Double> sentenceFraktionCounter;

    // JCasSentimentAbsolutes
    private Double mostPosSent;
    private Rede_File_Impl mostPosRede;
    private Double mostNegSent;
    private Rede_File_Impl mostNegRede;
    private Double mostNeutralSent;
    private Rede_File_Impl mostNeutralRede;




    public Analysis_File_Impl() {
        this.tokenCounter = new HashMap<String, Double>();
        //createTXTFile("Antworten/Uebung2_Frage1");
        this.perCounter = new HashMap<String, Double>();
        this.locCounter = new HashMap<String, Double>();
        this.orgCounter = new HashMap<String, Double>();
        //createTXTFile("Antworten/Uebung2_Frage2");
        this.posCounter = new HashMap<String, Double>();
        //createTXTFile("Antworten/Uebung2_Frage3");
        this.sentenceRednerCounter = new HashMap<String, Double>();
        this.sentimentRednerSum = new HashMap<String, Double>();
        //createTXTFile("Antworten/Uebung2_Frage4");
        this.sentimentFraktionCounter = new HashMap<String, Double>();
        this.sentenceFraktionCounter = new HashMap<String, Double>();
        this.sentimentSum = 0D;
        this.sentenceCount = 0D;
        this.mostPosSent = Double.NEGATIVE_INFINITY;
        this.mostPosRede = null;
        this.mostNegSent = Double.POSITIVE_INFINITY;
        this.mostNegRede = null;
        this.mostNeutralSent = Double.NEGATIVE_INFINITY;
        this.mostNeutralRede = null;
        //createTXTFile("Antworten/Uebung2_Frage5");
    }

    public void parseDocs(String protocolLink, String protocollID){
        /**
         * go through all protkolle/tagesorsdnungspunkte/reden and add them to the analysing methods that are collecting data from all the reden
         */
        MongoDBConnectionHandler_File_Impl handler = new MongoDBConnectionHandler_File_Impl();
        Creds_File_Impl cred = new Creds_File_Impl();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
//        for(Integer i = 1; i < 2; i++){
        System.out.println("Currently handeling document number ---> " + protocollID);
        try {
            String document = handler.db.getCollection(cred.getProtocollCollection()).find(eq("_id", protocollID)).first().toJson();
            Protokoll_File_Impl protokoll = mapper.readValue(document, Protokoll_File_Impl.class);
            for(Tagesordnungspunkt_File_Impl top : protokoll.getTagesordnungspunkte()){
                if (top.getReden().size() > 0) {
                    for (Rede_File_Impl rede : top.getReden()) {
                        System.out.println("Here it is --> " + rede.getRedeID());
                        //Document analysedDoc = createAnalysedDoc(rede, handler);
                        //handler.uploadDoc(analysedDoc, "analyzedSpeeches");
                        /*
                        JCasTuple_FIle_Impl jCasTuple = handler.getRedeJcas(rede.getRedeID());
                        JCas redeJCas = jCasTuple.getRedeJCas();
                        this.JCasTokenCounter(redeJCas);
                        this.JCasEntityCounter(redeJCas);
                        this.JCasEntityCounter(jCasTuple.getCommentJCas());
                        this.JCasPOSCounter(redeJCas);
                        this.JCasSentimentAll(redeJCas);
                        this.JCasSentimentRednerCounter(rede.getRedner(), redeJCas);
                        this.JCasSentimentFraktionCounter(rede.getRedner(), redeJCas);
                        this.JCasSentimentAbsolutes(rede, jCasTuple.getCommentJCas());

                         */
                    }
                }
            }
        } catch (NullPointerException | IOException e) {
            System.out.println("couldnt find protokoll nr " + protocollID);
            e.printStackTrace();
        } catch (MongoSocketOpenException e){
            System.out.println("Mongo not reached");
        }
    }



    public void JCasTokenCounter(JCas rede){
        /**
         * if token found inc token value by one in hashmap
         * assumption: capitalization does not change a word from antoher, i.e. there is no difference between "TeSt" and "tEsT"
         */
        Collection<Sentence> sentences = JCasUtil.select(rede, Sentence.class);
        for (Sentence sentence : sentences) {
            Collection<Token> tokens = JCasUtil.selectCovered(Token.class, sentence);
            for (Token token : tokens) {
                String tokenText = token.getText().toLowerCase();
                Double count = this.tokenCounter.containsKey(tokenText) ? this.tokenCounter.get(tokenText) : this.tokenCounter.put(tokenText, 0D);
                if (count == null){count = 0D;}
                this.tokenCounter.put(tokenText, count + 1);
            }
        }
    }


    public Document createAnalysedDoc(JCasTuple_FIle_Impl jCasTuple,MongoDBConnectionHandler_File_Impl handler,Rede_File_Impl rede) throws UIMAException {

        JCas redeJCas = jCasTuple.getRedeJCas();
        org.bson.Document mongoDoc = new org.bson.Document();
        mongoDoc.put("_id", rede.getRedeID());
        mongoDoc.put("persons",getJCasEntityList(redeJCas, "PER"));
        mongoDoc.put("organisations",getJCasEntityList(redeJCas, "ORG"));
        mongoDoc.put("locations",getJCasEntityList(redeJCas, "LOC"));
        mongoDoc.put("token", getTokenList(redeJCas));
        //mongoDoc.put("sentences", getSentenceList(redeJCas));
        //mongoDoc.put("sentencesSentiment",getSentenceSentimentValue(redeJCas));
        mongoDoc.put("pos", getPosList(redeJCas));
        mongoDoc.put("lemma",getLemma(redeJCas));
        mongoDoc.put("sentiment",getSentimentValue(redeJCas));
        //org.bson.Document sentences = new org.bson.Document();
        mongoDoc.put("sentences", getSentenceDocument(redeJCas));
        //sentences.put("sentimentValue",getSentenceSentimentValue(redeJCas));
        //mongoDoc.put("sentences", sentences);

        /*
        List<Document> speeches = new LinkedList<>();
        this.getReden().forEach(speach -> {
            speeches.add(speach.getDocument());
        });

         */

        return mongoDoc;
    }
    public void getToken(){
        /**
         * sort tokens and add them to text file
         */
        List<Map.Entry<String, Double>> EntityList = new LinkedList<Map.Entry<String, Double>>(this.tokenCounter.entrySet());
        Collections.sort(EntityList, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        System.out.println("getting Token");
        for (Map.Entry<String, Double> entry : EntityList){
            appendToTXT("Antworten/Uebung2_Frage1", entry.getKey() + " - " + entry.getValue());
        }
    }

    public List<String> getPosList(JCas rede){
        List<String> posList = JCasUtil.select(rede, POS.class).stream().map(pos -> pos.getPosValue()).collect(Collectors.toList());
        return posList;
    }
    public List<String> getLemma(JCas rede){
        List<String> lemmaList = JCasUtil.select(rede, Lemma.class).stream().map(pos -> pos.getCoveredText()).collect(Collectors.toList());
        return lemmaList;
    }

    public List<String> getTokenList(JCas rede){
        LinkedList<String> tokenList = new LinkedList<>();
        Collection<Sentence> sentences = JCasUtil.select(rede, Sentence.class);
        for (Sentence sentence : sentences){
            Collection<Token> tokens = JCasUtil.selectCovered(Token.class, sentence);
            for (Token token : tokens) {
                String tokenText = token.getText().toLowerCase();
                tokenList.push(tokenText);
            }
        }
        return tokenList;
    }

    public List<String> getSentenceList(JCas rede){
        //to be continued
        LinkedList<String> sentenceList = new LinkedList<>();
        Collection<Sentence> sentences = JCasUtil.select(rede, Sentence.class);
        for (Sentence sentence : sentences){
            sentenceList.push((sentence.getCoveredText()));
        }
        return sentenceList;
    }
    public List<Double> getSentenceSentimentValue(JCas rede){
        //to be continued
        LinkedList<Double> sentenceList = new LinkedList<>();
        Collection<Sentence> sentences = JCasUtil.select(rede, Sentence.class);
        for (Sentence sentence : sentences){
            sentenceList.push(getSentimentValue(sentence));
        }
        return sentenceList;
    }
    public List<Document> getSentenceDocument(JCas rede){
        //to be continued
        LinkedList<Document> sentenceList = new LinkedList<>();
        Collection<Sentence> sentences = JCasUtil.select(rede, Sentence.class);
        for (Sentence sentence : sentences){
            org.bson.Document sentenceSentiment = new org.bson.Document();
            sentenceSentiment.put("sentenceSentimentValue", getSentimentValue(sentence));
            sentenceSentiment.put("sentenceText", sentence.getCoveredText());
            sentenceList.push(sentenceSentiment);
        }
        return sentenceList;
    }
    public List<String> getJCasEntityList(JCas rede, String Entity){
        LinkedList<String> entityList = new LinkedList<>();
        Collection<NamedEntity> entitys = JCasUtil.select(rede, NamedEntity.class);
        for(NamedEntity entity : entitys){
            if (entity.getValue().equals(Entity)){
                entityList.push(entity.getCoveredText());
            }
        }
        return entityList;
    }
    public Double getSentimentValue(JCas rede){
        /**
         * Partei and Fraktion is the same thing
         */
        Double overallSentiment = 0.0;
        int amountSentences = JCasUtil.select(rede, Sentence.class).size();
        for(Sentence sentence : JCasUtil.select(rede, Sentence.class)){
            for(Sentiment sentiment : JCasUtil.selectCovered(Sentiment.class, sentence)) {
                Double currentSentiment = sentiment.getSentiment();
                overallSentiment = overallSentiment + currentSentiment;
            }
        }
        return overallSentiment / amountSentences;

    }
    public Double getSentimentValue(Sentence sentence){
        /**
         * Partei and Fraktion is the same thing
         */
        Double overallSentiment = 0.0;
        int amountSentences = 1;
        for(Sentiment sentiment : JCasUtil.selectCovered(Sentiment.class, sentence)) {
            Double currentSentiment = sentiment.getSentiment();
            overallSentiment = overallSentiment + currentSentiment;
        }
        return overallSentiment / amountSentences;

    }
    public void JCasEntityCounter(JCas rede){
        /**
         * if entity found inc token value by one in hashmap
         * 3 different hashmaps for three different entity kinds
         */
        Collection<NamedEntity> entitys = JCasUtil.select(rede, NamedEntity.class);
        for(NamedEntity entity : entitys){
            String entityString = entity.getCoveredText();
            if (entity.getValue().equals("PER")){
                Double count = this.perCounter.containsKey(entityString) ? this.perCounter.get(entityString) : this.perCounter.put(entityString, 0D);
                if (count == null){count = 0D;}
                this.perCounter.put(entityString, count + 1);
            } else if (entity.getValue().equals("ORG")) {
                Double count = this.orgCounter.containsKey(entityString) ? this.orgCounter.get(entityString) : this.orgCounter.put(entityString, 0D);
                if (count == null){count = 0D;}
                this.orgCounter.put(entityString, count + 1);
            } else if (entity.getValue().equals("LOC")) {
                Double count = this.locCounter.containsKey(entityString) ? this.locCounter.get(entityString) : this.locCounter.put(entityString, 0D);
                if (count == null){count = 0D;}
                this.locCounter.put(entityString, count + 1);
            }
        }
    }
    public void getEntity(){
        /**
         * format entitys clean for printing in file
         */
        appendToTXT("Antworten/Uebung2_Frage2","-----");
        appendToTXT("Antworten/Uebung2_Frage2","Location Entitys: ");
        appendToTXT("Antworten/Uebung2_Frage2","-----");
        printsortedEntitys(new LinkedList<Map.Entry<String, Double>>(this.locCounter.entrySet()));
        appendToTXT("Antworten/Uebung2_Frage2","");
        appendToTXT("Antworten/Uebung2_Frage2","-----");
        appendToTXT("Antworten/Uebung2_Frage2","Person Entitys: ");
        appendToTXT("Antworten/Uebung2_Frage2","-----");
        printsortedEntitys(new LinkedList<Map.Entry<String, Double>>(this.perCounter.entrySet()));
        appendToTXT("Antworten/Uebung2_Frage2","");
        appendToTXT("Antworten/Uebung2_Frage2","-----");
        appendToTXT("Antworten/Uebung2_Frage2","Organisation Entitys: ");
        appendToTXT("Antworten/Uebung2_Frage2","-----");
        printsortedEntitys(new LinkedList<Map.Entry<String, Double>>(this.orgCounter.entrySet()));

    }

    public void printsortedEntitys(List<Map.Entry<String, Double>> EntityList){
        /**
         * sort entitys and add them to text file
         */
        Collections.sort(EntityList, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }});
        for (Map.Entry<String, Double> entry : EntityList){
            appendToTXT("Antworten/Uebung2_Frage2", entry.getKey() + " - " + entry.getValue());
        }
    }

    public void JCasPOSCounter(JCas rede){
        /**
         * if POS found inc token value by one in hashmap
         * only get from speech and not comments because it is called POS (Parts of SPEECH)
         */
        Collection<Token> tokens = JCasUtil.select(rede, Token.class);
        for(Token token : tokens){
            String posString = token.getPos().getCoarseValue();
            Double count = this.posCounter.containsKey(posString) ? this.posCounter.get(posString) : this.posCounter.put(posString, 0D);
            if (count == null){count = 0D;}
            this.posCounter.put(posString, count + 1);
        }
    }
    public void getPOS(){
        /**
         * sort POS and add them to text file
         */
        List<Map.Entry<String, Double>> EntityList = new LinkedList<Map.Entry<String, Double>>(this.posCounter.entrySet());
        Collections.sort(EntityList, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        for (Map.Entry<String, Double> entry : EntityList){
            appendToTXT("Antworten/Uebung2_Frage3", entry.getKey() + " - " + entry.getValue());
        }
    }

    public void JCasSentimentAll(JCas rede) {
        /**
         * add upp sentiments of all reden
         */
        for (Sentence sentence : JCasUtil.select(rede, Sentence.class)) {
            for (Sentiment sentiment : JCasUtil.selectCovered(Sentiment.class, sentence)) {
                this.sentenceCount++;
                this.sentimentSum +=  sentiment.getSentiment();
            }
        }
    }

    public void getOverallSentiment(){
        /**
         * print overrall sentiment into text file
         */
        appendToTXT("Antworten/Uebung2_Frage4", "Overall Sentiment: " + this.sentimentSum/max(this.sentenceCount, 1));
    }

    public void JCasSentimentRednerCounter(Redner_File_Impl redner, JCas rede){
            /**
             * sentiment sum saved with key rednerID
             * sentence count saved with key rednerID+"C"
             * average sentiment/sentence = rednerID/rendnerID+"C"
             */
            for (Sentence sentence : JCasUtil.select(rede, Sentence.class)) {
                for (Sentiment sentiment : JCasUtil.selectCovered(Sentiment.class, sentence)) {
                    Double sentDouble = sentiment.getSentiment();
                    String rednerID = redner.getRednerID();
                    Double count = this.sentenceRednerCounter.containsKey(rednerID) ? this.sentenceRednerCounter.get(rednerID) : this.sentenceRednerCounter.put(rednerID, 0D);
                    if (count == null){count = 0D;}
                    this.sentenceRednerCounter.put(rednerID, count + 1);
                    Double sentimentSum = this.sentimentRednerSum.containsKey(rednerID) ? this.sentimentRednerSum.get(rednerID) : this.sentimentRednerSum.put(rednerID, 0D);
                    if (sentimentSum == null){sentimentSum = 0D;}
                    this.sentimentRednerSum.put(rednerID, sentimentSum + sentDouble);
                }
            }
        }

    public void getRednerSentiments(){
        /**
         *  add up Redner sentiment to their individual hashmap entry
          */
        appendToTXT("Antworten/Uebung2_Frage4", "");
        appendToTXT("Antworten/Uebung2_Frage4", "-----");
        appendToTXT("Antworten/Uebung2_Frage4", "RednerID - Average Sentiment");
        appendToTXT("Antworten/Uebung2_Frage4", "-----");
        for (Map.Entry<String, Double> entry : this.sentenceRednerCounter.entrySet()) {
            Double numerator = sentimentRednerSum.get(entry.getKey());
            appendToTXT("Antworten/Uebung2_Frage4", entry.getKey() + " - " + (numerator/entry.getValue()));

        }
    }

    public void JCasSentimentFraktionCounter(Redner_File_Impl redner, JCas rede){
        /**
         * Partei and Fraktion is the same thing
         */
        for(Sentence sentence : JCasUtil.select(rede, Sentence.class)){
            for(Sentiment sentiment : JCasUtil.selectCovered(Sentiment.class, sentence)) {
                Double sentDouble = sentiment.getSentiment();
                String fraktion = redner.getFraktion().toLowerCase().replaceAll("\\s", "").replaceAll("[^\\x00-\\x7F]", "");
                Double sentencesCount = this.sentenceFraktionCounter.containsKey(fraktion) ? this.sentenceFraktionCounter.get(fraktion) : this.sentenceFraktionCounter.put(fraktion, 0D);
                if(sentencesCount == null) {sentencesCount = 0D;}
                this.sentenceFraktionCounter.put(fraktion, sentencesCount + 1);
                Double sentimentSum = this.sentimentFraktionCounter.containsKey(fraktion) ? this.sentimentFraktionCounter.get(fraktion) : this.sentimentFraktionCounter.put(fraktion, 0D);
                if(sentimentSum == null) {sentimentSum = 0D;}
                this.sentimentFraktionCounter.put(fraktion, sentimentSum + sentDouble);
            }
        }
    }
    public void getFraktionSentiments(){
        /**
         * add up Fraktion sentiment to their individual hashmap entry
         */
        appendToTXT("Antworten/Uebung2_Frage4", "");
        appendToTXT("Antworten/Uebung2_Frage4", "-----");
        appendToTXT("Antworten/Uebung2_Frage4", "Fraktion - Average Sentiment");
        appendToTXT("Antworten/Uebung2_Frage4", "-----");
        for (Map.Entry<String, Double> entry : this.sentenceFraktionCounter.entrySet()) {
            Double numerator = sentimentFraktionCounter.get(entry.getKey());
            appendToTXT("Antworten/Uebung2_Frage4", entry.getKey() + " - " + (numerator / entry.getValue()));
        }
    }

    public Double getAverageSentiment(JCas comment){
        /**
         * Given a String of multiple sentences: averages out the sentiment per sentence
         */
        Double sentenceCount = 0D;
        Double sentimenSum = 0D;
        for(Sentence sentence : JCasUtil.select(comment, Sentence.class)){
            for(Sentiment sentiment : JCasUtil.selectCovered(Sentiment.class, sentence)){
                sentenceCount++;
                sentimenSum += sentiment.getSentiment();
            }
        }
        return sentimenSum/sentenceCount;
    }

    public void JCasSentimentAbsolutes(Rede_File_Impl rede, JCas commentJCas){
        /**
         * gets the extrema sentiments from all the reden
         */
        Double redeSentiment = getAverageSentiment(commentJCas);
        if(redeSentiment > this.mostPosSent){
            this.mostPosSent = redeSentiment;
            this.mostPosRede = rede;
        }
        if (redeSentiment < this.mostNegSent){
            this.mostNegSent = redeSentiment;
            this.mostNegRede = rede;
        }
        if (abs(redeSentiment) < abs(this.mostNeutralSent)){
            this.mostNeutralSent = redeSentiment;
            this.mostNeutralRede = rede;
        }

    }
    public void getAbsolutes(){
        /**
         * print the extrema sentiments to txt
         */
        String pos = this.mostPosRede.getRedeID() + " is the most positive rede, it was held by " + this.mostPosRede.getRedner().getFirstname() + " " + this.mostPosRede.getRedner().getLastname() + "  (" + this.mostPosRede.getRedner().getFraktion() + ")";
        appendToTXT("Antworten/Uebung2_Frage5", pos);
        String neg = this.mostNegRede.getRedeID() + " is the most negative rede, it was held by " + this.mostNegRede.getRedner().getFirstname() + " " + this.mostNegRede.getRedner().getLastname() + "  (" + this.mostNegRede.getRedner().getFraktion() + ")";
        appendToTXT("Antworten/Uebung2_Frage5", neg);
        String neutral = this.mostNeutralRede.getRedeID() + " is the most neutral rede, it was held by " + this.mostNeutralRede.getRedner().getFirstname() + " " + this.mostNeutralRede.getRedner().getLastname() + "  (" + this.mostNeutralRede.getRedner().getFraktion() + ")";
        appendToTXT("Antworten/Uebung2_Frage5", neutral);
    }

    private void createTXTFile(String filename){
        try {
            /**
             * creates a text file with a given name
             */
            File myFile = new File(filename + ".txt");
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the text file");
            e.printStackTrace();
        }
    }

    private void appendToTXT(String filename, String context){
        /**
         * appends text to a given file
         */
        try {
            FileWriter myWriter = new FileWriter(filename + ".txt", true);
            myWriter.write(context + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred while appending line to "+ filename +" file");
            e.printStackTrace();
        }

    }

}
