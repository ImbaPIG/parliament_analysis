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
import org.hucompute.textimager.uima.gervader.GerVaderSentiment;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger3;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;


public class Pipeline_File_Impl implements Pipeline {
    private final AggregateBuilder pipeline;

    public Pipeline_File_Impl() {
        this.pipeline = new AggregateBuilder();
    }

    public JCas pipeline(String s){
        /**
         * makes a jcas objcet from a string
         */
        JCas jCas = null;
        try {
            jCas = JCasFactory.createText(s, "de");
        } catch (UIMAException e) {}

        //create pipeline

        try {
            this.pipeline.add(createEngineDescription(SpaCyMultiTagger3.class, SpaCyMultiTagger3.PARAM_REST_ENDPOINT, "http://spacy.prg2021.texttechnologylab.org"
            ));
            this.pipeline.add(createEngineDescription(GerVaderSentiment.class, GerVaderSentiment.PARAM_REST_ENDPOINT, "http://gervader.prg2021.texttechnologylab.org",
                    GerVaderSentiment.PARAM_SELECTION, "text,de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence"
            ));
        } catch (UIMAException ignored) {}
        AnalysisEngine pAE = null;
        try {
            pAE = this.pipeline.createAggregate();
        } catch (ResourceInitializationException ignored) {}
        try {
            SimplePipeline.runPipeline(jCas, pAE);
        } catch (AnalysisEngineProcessException ignored) {}

        return jCas;
    }

}

