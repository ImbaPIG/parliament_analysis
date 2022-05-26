package nlp;

import interfaces.Pipeline;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.hucompute.textimager.fasttext.labelannotator.LabelAnnotatorDocker;
import org.hucompute.textimager.uima.gervader.GerVaderSentiment;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger3;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;


/**
 *   
 */
public class Pipeline_File_Impl implements Pipeline {
    private final AggregateBuilder pipeline;
    public Pipeline_File_Impl() {
        this.pipeline = new AggregateBuilder();
    }

    /**
     *Methode to parse string to jcas with pipeline
     * @param s
     * @return
     */
    public JCas pipeline(String s){
        JCas jCas = null;
        try {
            jCas = JCasFactory.createText(s, "de");
        } catch (UIMAException e) {}

        //create pipeline

        try {
            /*
            this.pipeline.add(createEngineDescription(SpaCyMultiTagger3.class, SpaCyMultiTagger3.PARAM_REST_ENDPOINT, "http://spacy.prg2021.texttechnologylab.org"
            ));
            this.pipeline.add(createEngineDescription(GerVaderSentiment.class, GerVaderSentiment.PARAM_REST_ENDPOINT, "http://gervader.prg2021.texttechnologylab.org",
                    GerVaderSentiment.PARAM_SELECTION, "text,de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence"
            ));

             */
            this.pipeline.add(createEngineDescription(SpaCyMultiTagger3.class,
                    SpaCyMultiTagger3.PARAM_REST_ENDPOINT, "http://spacy.prg2021.texttechnologylab.org"

            ));
            String sPOSMapFile = Pipeline_File_Impl.class.getClassLoader().getResource("am_posmap.txt").getPath();
            sPOSMapFile = "./resources/am_posmap.txt";
            //String sPOSMapFile = this.getClass().getResource("am_posmap.txt").getPath();
            //String zz = "./am_posmap.txt";
            //String idk = "C:\\Users\\Moritz\\IdeaProjects\\gruppe_1_3\\fileExtractor\\main\\resources\\am_posmap.txt";
            //System.out.println(sPOSMapFile);

            this.pipeline.add(createEngineDescription(LabelAnnotatorDocker.class,
                    LabelAnnotatorDocker.PARAM_FASTTEXT_K, 100,
                    LabelAnnotatorDocker.PARAM_CUTOFF, false,
                    LabelAnnotatorDocker.PARAM_SELECTION, "text",
                    LabelAnnotatorDocker.PARAM_TAGS, "ddc3",
                    LabelAnnotatorDocker.PARAM_USE_LEMMA, true,
                    LabelAnnotatorDocker.PARAM_ADD_POS, true,
                    LabelAnnotatorDocker.PARAM_POSMAP_LOCATION, sPOSMapFile,
                    LabelAnnotatorDocker.PARAM_REMOVE_FUNCTIONWORDS, true,
                    LabelAnnotatorDocker.PARAM_REMOVE_PUNCT, true,
                    LabelAnnotatorDocker.PARAM_REST_ENDPOINT, "http://ddc.prg2021.texttechnologylab.org"
            ));

            this.pipeline.add(createEngineDescription(GerVaderSentiment.class,
                    GerVaderSentiment.PARAM_REST_ENDPOINT, "http://gervader.prg2021.texttechnologylab.org",
                    GerVaderSentiment.PARAM_SELECTION, "text,de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence"
            ));

        } catch (UIMAException ignored) {}
        AnalysisEngine pAE = null;
        try {
            pAE = this.pipeline.createAggregate();
            //ResourceInitializationException
        } catch (ResourceInitializationException e) {
            e.printStackTrace();
        }
        try {
            SimplePipeline.runPipeline(jCas, pAE);
        } catch (AnalysisEngineProcessException ignored) {}

        return jCas;
    }

}

