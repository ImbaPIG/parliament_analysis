package bundestag;

import org.bson.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Redner_File_Impl {
    private String firstname = null;
    private String lastname = null;
    private String fraktion = null;
    private String rednerID = null;

    public Redner_File_Impl(Element e) {
        /**
         * creates a redner object
         */
        // get firstname
        NodeList firstnameList = e.getElementsByTagName("vorname");
        this.firstname = "";
        try {
            this.firstname = ((Element) firstnameList.item(0)).getTextContent();
        } catch (NullPointerException x) {}
        // get lastname
        NodeList lastnameList = e.getElementsByTagName("nachname");
        this.lastname = "";
        try {
            this.lastname = ((Element) lastnameList.item(0)).getTextContent();
        } catch (NullPointerException x) {}
        // get fraktion
        NodeList fraktionList = e.getElementsByTagName("fraktion");
        this.fraktion = "";
        try {
            this.fraktion = ((Element) fraktionList.item(0)).getTextContent();
        } catch (NullPointerException x) {}
        // get rednerID
        this.rednerID = e.getAttribute("id");

    }

    // dummy constructor for instanciating from mongodb
    public Redner_File_Impl() {}

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFraktion() {
        return fraktion;
    }

    public String getRednerID() {
        return rednerID;
    }

    public Document getDocument(){
        org.bson.Document mongoDoc = new org.bson.Document();
        mongoDoc.put("_id", this.getRednerID());
        mongoDoc.put("firstname", this.getFirstname());
        mongoDoc.put("lastname",this.getLastname());
        mongoDoc.put("fraktion",this.getFraktion());
        return mongoDoc;
    }

}
