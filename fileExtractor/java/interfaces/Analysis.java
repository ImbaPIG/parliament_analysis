package interfaces;

import bundestag.Rede_File_Impl;
import bundestag.Redner_File_Impl;
import org.apache.uima.jcas.JCas;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public interface Analysis {



    public void JCasEntityCounter(JCas rede);

    public void getEntity();

    public void printsortedEntitys(List<Map.Entry<String, Double>> EntityList);

    public void JCasPOSCounter(JCas rede);

    public void getPOS();

    public void JCasSentimentAll(JCas rede);

    public void getOverallSentiment();

    public void JCasSentimentRednerCounter(Redner_File_Impl redner, JCas rede);

    public void getRednerSentiments();

    public void JCasSentimentFraktionCounter(Redner_File_Impl redner, JCas rede);

    public void getFraktionSentiments();

    public Double getAverageSentiment(JCas comment);

    public void JCasSentimentAbsolutes(Rede_File_Impl rede, JCas commentJCas);

    public void getAbsolutes();
}
