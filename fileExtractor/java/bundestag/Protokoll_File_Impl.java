package bundestag;

import com.mongodb.BasicDBObject;
import database.MongoDBConnectionHandler_File_Impl;
import interfaces.Protokoll;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static java.util.Arrays.asList;

public class Protokoll_File_Impl implements Protokoll {
    private ArrayList<Tagesordnungspunkt_File_Impl> Tagesordnungspunkte;
    private  String date = null;
    private  String periode = null;
    private String protocollNr = null;
    private  String _id = null;
    private  String title = null;
    private ArrayList<String> Sitzungsleiter = null;


    public Protokoll_File_Impl(Document doc, DocumentBuilder builder, MongoDBConnectionHandler_File_Impl handler, String protocollID) throws IOException, SAXException {
        /**
         * creates a Protokoll object and all the theobjects a Protokoll contains (i.e. TagesOrdnungsPunkt, Rede, Redner etc)
         */
        this.Tagesordnungspunkte = new ArrayList<Tagesordnungspunkt_File_Impl>();

        // navigate to head
        NodeList headList = doc.getElementsByTagName("kopfdaten");
        Node headNode = headList.item(0);
        Element headElement = (Element) headNode;

        // add infos to object
        this.date = headElement.getElementsByTagName("datum").item(0).getTextContent();
        this.periode = headElement.getElementsByTagName("wahlperiode").item(0).getTextContent();
        this.protocollNr = headElement.getElementsByTagName("sitzungsnr").item(0).getTextContent();
        this._id = protocollID;
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

    public ArrayList<Tagesordnungspunkt_File_Impl> getTagesordnungspunkte() {
        return Tagesordnungspunkte;
    }

    public void setTagesordnungspunkte(ArrayList<Tagesordnungspunkt_File_Impl> tagesordnungspunkte) {
        Tagesordnungspunkte = tagesordnungspunkte;
    }

    public String get_id() {
        return _id;
    }

    public void setID(String newID){ this._id = newID;}

    public String getDate() {
        return date;
    }

    public String getPeriode() {
        return periode;
    }

    public String getTitle() {
        return title;
    }

    public String getProtocollNr(){ return protocollNr;}

    public ArrayList<String> getSitzungsleiter() {
        return Sitzungsleiter;
    }

    public org.bson.Document getDocument(){
        org.bson.Document mongoDoc = new org.bson.Document();
        mongoDoc.put("_id", this.get_id());
        mongoDoc.put("date", this.getDate());
        mongoDoc.put("periode", this.getPeriode());
        mongoDoc.put("title", this.getTitle());
        mongoDoc.put("sitzungsleiter", this.getSitzungsleiter());
        mongoDoc.put("protocollNr", this.getProtocollNr());
        List<org.bson.Document> tagesordnungspunkte = new LinkedList<>();
        this.getTagesordnungspunkte().forEach(tages -> {
            tagesordnungspunkte.add(tages.getDocument());
        });
        mongoDoc.append("tagesordnungspunkte", tagesordnungspunkte);
        return mongoDoc;
    }
}
