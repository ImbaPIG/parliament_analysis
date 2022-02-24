package interfaces;

import org.apache.uima.jcas.JCas;

public interface Pipeline {
    public JCas pipeline(String s);
}
