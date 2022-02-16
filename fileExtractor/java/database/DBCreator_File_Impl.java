package database;

import bundestag.Protokoll_File_Impl;
import interfaces.DBCreator;
import org.apache.uima.UIMAException;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.conversions.Bson;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import webscraper.Webcrawler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;


public class DBCreator_File_Impl implements DBCreator {

    public void insertProtocolls(String protocolLink, String protocollID) throws IOException {
        /**
         * used to insert all protkolle and jcas into the mongodb
         */
        MongoDBConnectionHandler_File_Impl handler = new MongoDBConnectionHandler_File_Impl();



        // make parser instance
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (Exception e) {e.printStackTrace();}

        // parse through all xml files
        //File[] fs = fileGetter(this.dir);
        Document doc = Webcrawler.getDocFromLink(protocolLink);
        // get sitzungsnr from filename
        String sitzungsnr = protocollID;

        // add protokoll if not already added
        if(handler.protokollExists(sitzungsnr)){
            System.out.println("the protkoll " + sitzungsnr + " already exists, therefore it is skipped");
        } else {
            handler.createPlaceholder(sitzungsnr);
            try{
                assert builder != null;
                Protokoll_File_Impl protokoll = new Protokoll_File_Impl(doc, builder, handler, sitzungsnr);
                System.out.println("created Protocoll");
                // String protokollBson = handler.ObjToBson(protokoll);
                handler.removePlaceholder(sitzungsnr);
                //handler.uploadBson(protokollBson, "protocol");
                handler.uploadDoc(protokoll.getDocument(), "protocol");

            } catch (Exception e) {e.printStackTrace();}
       }
    }

    public void updateSpeakerMeta(Document mdbXml) throws IOException, UIMAException {
        MongoDBConnectionHandler_File_Impl handler = new MongoDBConnectionHandler_File_Impl();
        NodeList nodeList = mdbXml.getElementsByTagName("MDB");
        System.out.println(nodeList.getLength());
        for(Node node : toIterable(nodeList) ){
            String id = getFirstNodeFromXML(node, "ID") != null ? getFirstNodeFromXML(node, "ID").getTextContent(): "";
            String party = getFirstNodeFromXML(node, "PARTEI_KURZ") != null ? getFirstNodeFromXML(node, "PARTEI_KURZ").getTextContent(): "";


            if(handler.existsDocument(id,"speakers")){
                boolean result = false;
                System.out.println("updating" + id + "picture");
                org.bson.Document speaker = handler.getDocument(id,"speakers");
                speaker.append("party",party);
                speaker.append("picture",Webcrawler.getImageLink(speaker.get("firstname").toString(), speaker.get("lastname").toString()));
                while(!result) {
                    result = handler.updateDocument(speaker, "speakers");
                }
            }

        }
    }


    public File[] fileGetter(String dir){
        /**
         * gets all xml files from the given directory
         */
        File folder = new File(dir);
        return folder.listFiles(new FilenameFilter() {
            public boolean accept(File folder, String name) {
                return name.toLowerCase().endsWith(".xml");
            }
        });
    }

    public static Iterable<Node> toIterable(final NodeList nodeList) {
        return () -> new Iterator<Node>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < nodeList.getLength();
            }
            @Override
            public Node next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return nodeList.item(index++);
            }
        };
    }
    public static Node getFirstNodeFromXML(Node node, String nodeName){

        List<Node> rSet = new ArrayList<>();

        if(node.getNodeName().equals(nodeName)) {
            rSet.add(node);
        }
        else{
            if (node.hasChildNodes()) {
                for(Node child : toIterable(node.getChildNodes())){
                    rSet.add(getFirstNodeFromXML(child, nodeName));
                }
            } else if (node.getNodeName().equals(nodeName)){
                rSet.add(node);
            }
        }

        return rSet.size() > 0 ? rSet.get(0) : null;

    }

    public static List<Node> getNodesFromXML(Node pNode, String sNodeName){

        List<Node> rSet = new ArrayList<>(0);

        if(pNode.getNodeName().equals(sNodeName)) {
            rSet.add(pNode);

        }
        else{

            if (pNode.hasChildNodes()) {
                for (Node child : toIterable(pNode.getChildNodes())) {
                    rSet.addAll(getNodesFromXML(child, sNodeName));
                }
            } else {
                if (pNode.getNodeName().equals(sNodeName)) {
                    rSet.add(pNode);
                }
            }
        }

        return rSet;

    }




}
