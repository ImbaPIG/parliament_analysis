package nlp;

import bundestag.Rede_File_Impl;
import bundestag.Redner_File_Impl;
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
import org.hucompute.textimager.uima.type.category.CategoryCoveredTagged;
import org.hucompute.textimager.uima.type.Sentiment;
import org.bson.Document;


import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Analysis_File_Impl implements Analysis {

    /**
     *
     */
    public Analysis_File_Impl() {
    }

    /**
     *
     * @param jCasTuple
     * @param handler
     * @param rede
     * @return
     * @throws UIMAException
     */
    public Document createAnalysedDoc(JCasTuple_FIle_Impl jCasTuple,MongoDBConnectionHandler_File_Impl handler,Rede_File_Impl rede) throws UIMAException {

        JCas redeJCas = jCasTuple.getRedeJCas();

        org.bson.Document mongoDoc = new org.bson.Document();
        mongoDoc.put("_id", rede.getRedeID());
        mongoDoc.put("persons",getJCasEntityList(redeJCas, "PER"));
        mongoDoc.put("organisations",getJCasEntityList(redeJCas, "ORG"));
        mongoDoc.put("locations",getJCasEntityList(redeJCas, "LOC"));
        mongoDoc.put("token", getTokenList(redeJCas));
        mongoDoc.put("pos", getPosList(redeJCas));
        mongoDoc.put("lemma",getLemma(redeJCas));
        mongoDoc.put("sentiment",getSentimentValue(redeJCas));
        mongoDoc.put("sentences", getSentenceDocument(redeJCas));
        mongoDoc.put("category", getCategoryID(redeJCas));

        return mongoDoc;
    }

    /**
     *
     * @param rede
     * @return
     */
    public List<String> getPosList(JCas rede){
        List<String> posList = JCasUtil.select(rede, POS.class).stream().map(pos -> pos.getPosValue()).collect(Collectors.toList());
        return posList;
    }

    /**
     *
     * @param rede
     * @return
     */
    public List<String> getLemma(JCas rede){
        List<String> lemmaList = JCasUtil.select(rede, Lemma.class).stream().map(pos -> pos.getCoveredText()).collect(Collectors.toList());
        return lemmaList;
    }

    /**
     *
     * @param rede
     * @return
     */
    public String getCategoryID(JCas rede){
        Collection<CategoryCoveredTagged> categories = JCasUtil.select(rede, CategoryCoveredTagged.class );
        return categories.stream().findFirst().get().getValue().split("__")[2];
    }

    /**
     *
     * @param rede
     * @return
     */
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

    /**
     *
     * @param rede
     * @return
     */
    public List<String> getSentenceList(JCas rede){
        //to be continued
        LinkedList<String> sentenceList = new LinkedList<>();
        Collection<Sentence> sentences = JCasUtil.select(rede, Sentence.class);
        for (Sentence sentence : sentences){
            sentenceList.push((sentence.getCoveredText()));
        }
        return sentenceList;
    }

    /**
     *
     * @param rede
     * @return
     */
    public List<Double> getSentenceSentimentValue(JCas rede){
        //to be continued
        LinkedList<Double> sentenceList = new LinkedList<>();
        Collection<Sentence> sentences = JCasUtil.select(rede, Sentence.class);
        for (Sentence sentence : sentences){
            sentenceList.push(getSentimentValue(sentence));
        }
        return sentenceList;
    }

    /**
     *
     * @param rede
     * @return
     */
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

    /**
     *
     * @param rede
     * @param Entity
     * @return
     */
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

    /**
     *
     * @param rede
     * @return
     */
    public Double getSentimentValue(JCas rede){
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

    /**
     *
     * @param sentence
     * @return
     */
    public Double getSentimentValue(Sentence sentence){
        Double overallSentiment = 0.0;
        int amountSentences = 1;
        for(Sentiment sentiment : JCasUtil.selectCovered(Sentiment.class, sentence)) {
            Double currentSentiment = sentiment.getSentiment();
            overallSentiment = overallSentiment + currentSentiment;
        }
        return overallSentiment / amountSentences;

    }


}
