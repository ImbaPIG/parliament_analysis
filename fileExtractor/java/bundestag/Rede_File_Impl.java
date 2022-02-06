package bundestag;


import database.JcasDBObj;
import database.MongoDBConnectionHandler_File_Impl;
import interfaces.Rede;
import nlp.Pipeline_File_Impl;
import org.apache.uima.jcas.JCas;
import org.bson.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;

public class Rede_File_Impl implements Rede {
    private MongoDBConnectionHandler_File_Impl handler = null;
    private Redner_File_Impl redner = null;
    private String content = null;
    private String comments = "";
    private String redeID = null;

    public Rede_File_Impl(Element e, MongoDBConnectionHandler_File_Impl handler) throws IOException {
        /**
         * creates a Rede object and all the theobjects a Protokoll contains (i.e. Redner etc)
         */
        this.handler = handler;

        // get redner of rede
        NodeList rednerList = e.getElementsByTagName("redner");
        Element rednerElement = (Element) rednerList.item(0);
        this.redner = new Redner_File_Impl(rednerElement);

        // get redeID
        this.redeID = e.getAttribute("id");

        // get content of rede
        this.content = "";
        NodeList sentenceList = e.getElementsByTagName("p");
        for(int i = 0; i < sentenceList.getLength(); i++){
            Node sentenceNode = sentenceList.item(i);
            String sentence = sentenceNode.getTextContent();
            this.content += (sentence.trim() + " ");
        }

        //get all kommentare from rede
        ArrayList<String> comments = new ArrayList<String>();
        NodeList commentList = e.getElementsByTagName("kommentar");
        for(int i = 0; i < commentList.getLength(); i++){
            String comment = "";
            try {
                comment = ((Element) commentList.item(i)).getTextContent();
                this.comments += (comment.trim()) + " ";
            } catch (NullPointerException x) {}

        }
        // upload rede and comments as jcas if not already uploaded
        System.out.println("redeID ---> " + this.redeID);
        if(this.handler.jCasExists(this.redeID)) {
            System.out.println("Rede already inserted, skipping uploading rede as jcas");
        } else {
            Pipeline_File_Impl pipe = new Pipeline_File_Impl();
            // create jcas
            JCas contentJcas = pipe.pipeline(this.content);
            String contentXMl = this.handler.JCasToXML(contentJcas);
            JCas commentJcas = pipe.pipeline(this.comments);
            String commentXML = this.handler.JCasToXML(commentJcas);
            // created object for jcas collection
            JcasDBObj dbObj = new JcasDBObj(this.redeID, contentXMl, commentXML);
            // upload jcas obj\
            //String jCasBson = this.handler.ObjToBson(dbObj);
            //this.handler.uploadBson(jCasBson, "jcas");
            this.handler.uploadDoc(dbObj.getDocument(), "jcas");
        }
    }
    // dummy constructor for instanciating from mongodb
    public Rede_File_Impl() {}

    public Redner_File_Impl getRedner() {
        return redner;
    }

    public String getContent() {
        return content;
    }

    public String getComments() {
        return comments;
    }

    public String getRedeID() {
        return redeID;
    }

    public Document getDocument(){
        org.bson.Document mongoDoc = new org.bson.Document();
        mongoDoc.put("content", this.getContent());
        mongoDoc.put("comments", this.getComments());
        mongoDoc.put("redeID", this.getRedeID());
        mongoDoc.put("redner", this.getRedner().getDocument());
        return mongoDoc;
    }
}
