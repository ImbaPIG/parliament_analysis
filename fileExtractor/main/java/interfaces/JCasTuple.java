package interfaces;

import org.apache.uima.jcas.JCas;

/**
 * @author Erik
 */
public interface JCasTuple {
    public JCas getRedeJCas();
    public JCas getCommentJCas();
}
