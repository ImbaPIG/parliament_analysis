package database;

import org.bson.Document;

/**
 *   
 */
public class JcasDBObj {
    public String _id;
    public String contentXML;
    public String commentXML;

    public JcasDBObj(String redeID, String contentXML, String commentXML) {
        /**
         * object that is used to generate bson to be inserted in the mongodb
         */
        this._id = redeID;
        this.contentXML = contentXML;
        this.commentXML = commentXML;
    }

    /**
     * creator for bson Document
     * @return
     */
    public Document getDocument(){
        org.bson.Document mongoDoc = new org.bson.Document();
        mongoDoc.put("_id", this._id);
        mongoDoc.put("contentXML", this.contentXML);
        mongoDoc.put("commentXML", this.commentXML);
        return mongoDoc;
    }
}
