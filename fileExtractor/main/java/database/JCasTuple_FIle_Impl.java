package database;

import interfaces.JCasTuple;
import org.apache.uima.jcas.JCas;

/**
 *   
 */
public class JCasTuple_FIle_Impl implements JCasTuple {

    private final JCas redeJCas;
    private final JCas commentJCas;

    public JCasTuple_FIle_Impl(JCas redeJCas, JCas commentJCas) {
        /**
         * creates a Tuple containing the two jcas objects saved for every rede (rede_content, rede_comments)
         */
        this.redeJCas = redeJCas;
        this.commentJCas = commentJCas;
    }

    public JCas getRedeJCas() {
        return redeJCas;
    }

    public JCas getCommentJCas() {
        return commentJCas;
    }
}
