package interfaces;

import org.apache.uima.jcas.JCas;

/**
 * @author Erik
 */
public interface Pipeline {
    public JCas pipeline(String s);
}
