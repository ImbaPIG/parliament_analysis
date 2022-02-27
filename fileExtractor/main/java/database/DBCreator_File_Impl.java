package database;

import bundestag.Protokoll_File_Impl;
import interfaces.DBCreator;
import org.apache.uima.UIMAException;
import org.bson.conversions.Bson;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import webscraper.Webcrawler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DBCreator_File_Impl implements DBCreator {

    public void insertProtocolls(String protocolLink, String protocollID) throws IOException, ParseException, UIMAException, SAXException {
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
        Document doc = Webcrawler.getDocFromLink(protocolLink);
        // get sitzungsnr from filename
        String sitzungsnr = protocollID;



        // add protokoll if not already added
        if(handler.protokollExists(protocollID)){
            System.out.println("the protkoll " + protocollID + " already exists, therefore it is skipped");
        } else {
            // handler.createPlaceholder(sitzungsnr);
            try{
                assert builder != null;
                Protokoll_File_Impl protokoll = new Protokoll_File_Impl(doc, builder, handler);
                System.out.println("created Protocoll");
                // String protokollBson = handler.ObjToBson(protokoll);
                handler.removePlaceholder(sitzungsnr);
                //handler.uploadBson(protokollBson, "protocol");
                handler.uploadDoc(protokoll.getDocument(), "protocol");

            } catch (Exception e) {e.printStackTrace();}
       }
    }

    /**
     * @author Moritz&Özlem
     * updates Speaker Meta Data of all Speakers that can be found in the passed XML
     * @param mdbXml mdb XML to be parsed
     * @throws IOException
     * @throws UIMAException
     */
    public void updateSpeakerMeta(Document mdbXml) throws IOException, UIMAException {
        MongoDBConnectionHandler_File_Impl handler = new MongoDBConnectionHandler_File_Impl();

        //searches for all MDB´s and gets their id and party
        NodeList nodeList = mdbXml.getElementsByTagName("MDB");
        for(Node node : toIterable(nodeList) ){
            String id = getFirstNodeFromXML(node, "ID") != null ? getFirstNodeFromXML(node, "ID").getTextContent(): "";
            String party = getFirstNodeFromXML(node, "PARTEI_KURZ") != null ? getFirstNodeFromXML(node, "PARTEI_KURZ").getTextContent(): "";


            //checks if speaker is in database
            if(handler.existsDocument(id,"speakers")){
                boolean result = false;
                org.bson.Document speaker = handler.getDocument(id,"speakers");
                speaker.append("party",party);
                while(!result) {
                    result = handler.updateDocument(speaker, "speakers");
                }
            }

        }
    }

    /**
     * @author Moritz&Özlem
     * updates all speakers Pictures from the DB
     */
    public void insertSpeakersPictures() {
        try {
            //established DB connection and gets all speakers
            MongoDBConnectionHandler_File_Impl handler = new MongoDBConnectionHandler_File_Impl();
            List<org.bson.Document> speakers = handler.aggregateMongo("speakers", new ArrayList<Bson>());

            //iterates over speakers and sets the bson Document with the found picture
            for (org.bson.Document speaker : speakers) {
                boolean result = false;
                speaker.append("picture", Webcrawler.getImageLink(speaker.get("firstname").toString(), speaker.get("lastname").toString()));
                while(!result) {
                    result = handler.updateDocument(speaker, "speakers");
                }
            }
        }catch (IOException | UIMAException e){
            e.printStackTrace();
        }
    }


    /**
     * @author Moritz
     * helper function to convert NodeList to Iterable of <Node>>
     * @param nodeList to be converted to Iterable
     * @return Iterable of NodeList
     */
    public static Iterable<Node> toIterable(final NodeList nodeList) {
        return () -> new Iterator<Node>() {
            int index = 0;
            //override hasNext to check if new Iterable has next
            @Override
            public boolean hasNext() {
                return index < nodeList.getLength();
            }
            //override next to get next Item of Iterable
            @Override
            public Node next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return nodeList.item(index++);
            }
        };
    }

    /**
     * helper function to get the first Node that matches the passed nodeName in the xml
     * @param node parentNode to be searched
     * @param nodeName nodeName of Node to be searched
     * @return
     */
    public static Node getFirstNodeFromXML(Node node, String nodeName){

        List<Node> rSet = new ArrayList<>();
        //checks if current node matches nodeName, adds them to set if true
        if(node.getNodeName().equals(nodeName)) {
            rSet.add(node);
        }
        //checks if node has childs and recursivly look for nodeNames of child Nodes
        else if (node.hasChildNodes()){
            for(Node child : toIterable(node.getChildNodes())){
                rSet.add(getFirstNodeFromXML(child, nodeName));
            }
        }
        //return first Node in set or null
        return rSet.size() > 0 ? rSet.get(0) : null;

    }

    /**
     * @author Moritz
     * uploades CategoryEncodings of the passed path to the mongoDB
     * @param path to CategoryEncodings
     */
    public static void uploadCategoryEncoding(String path) {
        MongoDBConnectionHandler_File_Impl handler = new MongoDBConnectionHandler_File_Impl();
        LinkedList<org.bson.Document> docsToBeInserted = new LinkedList<>();
        //until there is no new line in file to be read read in line by line
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                //add id and text of encoding to bson Document and push Document to list if not already in Database
                org.bson.Document docToInsert = new org.bson.Document();
                String[] values = line.split("\t");
                //empty line check
                if(values.length < 2){continue;}
                docToInsert.put("_id", values[0]);
                docToInsert.put("text",values[1]);
                if(!handler.existsDocument(docToInsert.getString("_id"),"categoryEncodings" )){
                    docsToBeInserted.push(docToInsert);
                    TimeUnit.MILLISECONDS.sleep(10);
                }
            }
            //insert list to collection
            handler.uploadDocs(docsToBeInserted, "categoryEncodings");
        } catch (Exception e){
            e.printStackTrace();
        }
    }




}
