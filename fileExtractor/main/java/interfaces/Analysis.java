package interfaces;

import bundestag.Rede_File_Impl;
import bundestag.Redner_File_Impl;
import database.JCasTuple_FIle_Impl;
import database.MongoDBConnectionHandler_File_Impl;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.bson.Document;

import java.util.List;
import java.util.Map;

public interface Analysis {

    public Document createAnalysedDoc(JCasTuple_FIle_Impl jCasTuple, MongoDBConnectionHandler_File_Impl handler, Rede_File_Impl rede) throws UIMAException;

    public List<String> getPosList(JCas rede);

    public List<String> getLemma(JCas rede);

    public String getCategoryID(JCas rede);

    public List<String> getTokenList(JCas rede);

    public List<String> getSentenceList(JCas rede);

    public List<Document> getSentenceDocument(JCas rede);

    public List<String> getJCasEntityList(JCas rede, String Entity);

    public Double getSentimentValue(JCas rede);

    public Double getSentimentValue(Sentence sentence);

}
