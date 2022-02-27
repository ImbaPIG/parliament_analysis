package bundestag;

import database.MongoDBConnectionHandler_File_Impl;
import interfaces.Tagesordnungspunkt;
import org.apache.uima.UIMAException;
import org.bson.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Tagesordnungspunkt_File_Impl implements Tagesordnungspunkt {
    private ArrayList<Rede_File_Impl> reden = null;
    private ArrayList<String> kommentare = null;
    private String text = null;
    private String topID = null;

    public Tagesordnungspunkt_File_Impl(Element e, MongoDBConnectionHandler_File_Impl handler) throws IOException, UIMAException {
        /**
         * creates a Tagesordnungspunkt object and all the theobjects a Protokoll contains (i.e. Rede, Redner etc)
         */
        this.reden = new ArrayList<Rede_File_Impl>();
        this.kommentare = new ArrayList<String>();
        this.text = "";
        this.topID = e.getAttribute("top-id");

        // get all kommentare of TOP
        NodeList commentList = e.getElementsByTagName("kommentar");
        for(int i = 0; i < commentList.getLength(); i++){
            Node commentNode = commentList.item(i);
            String comment = commentNode.getTextContent();
            this.kommentare.add(comment);
        }

        //get all reden in TOP
        NodeList redeList = e.getElementsByTagName("rede");
        for(int i = 0; i < redeList.getLength(); i++){
            Element redeElement = (Element) redeList.item(i);
            Rede_File_Impl rede = new Rede_File_Impl(redeElement, handler);
            this.reden.add(rede);
        }

        // get text of TOP
        NodeList textList = e.getElementsByTagName("p");
        for(int i = 0; i < textList.getLength(); i++){
            Node textNode = textList.item(i);
            this.text += textNode.getTextContent();
        }
    }

    // dummy constructor for instanciating from mongodb
    public Tagesordnungspunkt_File_Impl() {}

    /**
     * getter Reden
     * @return
     */
    public ArrayList<Rede_File_Impl> getReden() {
        return reden;
    }

    /**
     * setter Reden
     * @param reden
     */
    public void setReden(ArrayList<Rede_File_Impl> reden) {
        this.reden = reden;
    }

    /**
     * getter Text
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * getter TopID
     * @return
     */
    public String getTopID() {
        return topID;
    }

    /**
     * creates bson Document of Tagesordnungspunkt
     * @return
     */
    public org.bson.Document getDocument(){
        org.bson.Document mongoDoc = new org.bson.Document();
        mongoDoc.put("text",this.getText());
        mongoDoc.put("topID",this.getTopID());

        //iterates over speeches of tagesordnungspunkt and adds bson Docs to List
        List<Document> speeches = new LinkedList<>();
        this.getReden().forEach(speech -> {
            speeches.add(speech.getDocument());
        });
        mongoDoc.append("reden", speeches);
        return mongoDoc;
    }

}

