package bundestag;

import database.MongoDBConnectionHandler_File_Impl;
import interfaces.Protokoll;
import org.apache.uima.UIMAException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Protokoll_File_Impl implements Protokoll {
    private ArrayList<Tagesordnungspunkt_File_Impl> Tagesordnungspunkte;
    private String date = null;
    private  String periode = null;
    private String protocollNr = null;
    private  String _id = null;
    private  String title = null;
    private ArrayList<String> Sitzungsleiter = null;


    /**
     * creates a Protokoll object and all the theobjects a Protokoll contains (i.e. TagesOrdnungsPunkt, Rede, Redner etc)
     * @param doc Document that is potential protocoll
     * @param builder
     * @param handler MongoConnectionHandler for inserts and updates
     * @throws IOException
     * @throws SAXException
     * @throws ParseException
     * @throws UIMAException
     * @author Erik
     */
    public Protokoll_File_Impl(Document doc, DocumentBuilder builder, MongoDBConnectionHandler_File_Impl handler) throws IOException, SAXException, ParseException, UIMAException {
        //
        this.Tagesordnungspunkte = new ArrayList<Tagesordnungspunkt_File_Impl>();

        // navigate to head
        NodeList headList = doc.getElementsByTagName("kopfdaten");
        Node headNode = headList.item(0);
        Element headElement = (Element) headNode;

        // add infos to object
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String dateString = headElement.getElementsByTagName("datum").item(0).getAttributes().getNamedItem("date").getTextContent();
        this.date  = dateString;
        this.periode = headElement.getElementsByTagName("wahlperiode").item(0).getTextContent();
        this.protocollNr = headElement.getElementsByTagName("sitzungsnr").item(0).getTextContent();
        this._id = this.periode + "-" + this.protocollNr;
        this.title = headElement.getElementsByTagName("sitzungstitel").item(0).getTextContent();
        this.Sitzungsleiter = null; // who is this ??


        // subparse TOPs in Protokoll
        NodeList tOPList = doc.getElementsByTagName("tagesordnungspunkt");
        for (int tOPCount = 0; tOPCount < tOPList.getLength(); tOPCount++) {
            Node tOPNode = tOPList.item(tOPCount);
            Tagesordnungspunkt_File_Impl t = new Tagesordnungspunkt_File_Impl((Element) tOPNode, handler);
            Tagesordnungspunkte.add(t);
        }
    }


    // dummy constructor for instanciating from mongodb
    public Protokoll_File_Impl() {}

    /**
     * getter for Tagesordnungspunkte
     * @return List of Tagesordnungspunkten
     * @author Erik
     */
    public ArrayList<Tagesordnungspunkt_File_Impl> getTagesordnungspunkte() {
        return Tagesordnungspunkte;
    }

    public void setTagesordnungspunkte(ArrayList<Tagesordnungspunkt_File_Impl> tagesordnungspunkte) {
        Tagesordnungspunkte = tagesordnungspunkte;
    }

    /**
     * getter for id
     * @return String id
     * @author Erik
     */
    public String get_id() {
        return _id;
    }

    /**
     * setter for id
     * @param newID String
     * @author Erik
     */
    public void setID(String newID){ this._id = newID;}

    /**
     * getter for date String
     * @return date String
     * @author Erik
     */
    public String getDate() {
        return date;
    }

    /**
     * getter for periode
     * @return periode as String
     * @author Erik
     */
    public String getPeriode() {
        return periode;
    }

    /**
     * getter for title
     * @return title String
     * @author Erik
     */
    public String getTitle() {
        return title;
    }

    /**
     * getter for protocollNr
     * @return protocollNr String
     * @author Erik
     */
    public String getProtocollNr(){ return protocollNr;}

    /**
     * getter for sitzungsleiter
     * @return sitzungsleiter String
     * @author Erik
     */
    public ArrayList<String> getSitzungsleiter() {
        return Sitzungsleiter;
    }

    /**
     * creator for Bson Documnet / creates bson Document
     * @return bson Document
     * @author Moritz
     */
    public org.bson.Document getDocument(){
        org.bson.Document mongoDoc = new org.bson.Document();
        mongoDoc.put("_id", this.get_id());
        mongoDoc.put("date", this.getDate());
        mongoDoc.put("periode", this.getPeriode());
        mongoDoc.put("title", this.getTitle());
        mongoDoc.put("sitzungsleiter", this.getSitzungsleiter());
        mongoDoc.put("protocollNr", this.getProtocollNr());
        List<org.bson.Document> tagesordnungspunkte = new LinkedList<>();

        //gets tagesordnungspunkte Documents as List
        this.getTagesordnungspunkte().forEach(tages -> {
            tagesordnungspunkte.add(tages.getDocument());
        });
        mongoDoc.append("tagesordnungspunkte", tagesordnungspunkte);
        return mongoDoc;
    }
}
