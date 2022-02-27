import java.io.IOException;
import java.text.ParseException;
import java.util.Hashtable;

import database.DBCreator_File_Impl;
import database.MongoDBConnectionHandler_File_Impl;
import nlp.Analysis_File_Impl;
import org.apache.uima.UIMAException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import webscraper.*;

import javax.xml.parsers.ParserConfigurationException;

public class ProtocollHandler {
    /**
     *Methode to handle processing and coordinating of protocolls
     * @param args
     * @throws IOException
     * @throws ParseException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws UIMAException
     */

    private Double currentProtocoll = 0.0;
    private Double totalProtocolls = 1.0;
    public void insertAndUpdateProtocolls(Hashtable<String, String> protocolLinks) throws IOException, ParseException, ParserConfigurationException, SAXException, UIMAException {

        String zipLink = "https://www.bundestag.de/resource/blob/472878/d5743e6ffabe14af60d0c9ddd9a3a516/MdB-Stammdaten-data.zip";

        DBCreator_File_Impl mongoConnection = new DBCreator_File_Impl();
        Analysis_File_Impl anal = new Analysis_File_Impl();


        try{
            this.totalProtocolls = protocolLinks.size() + 0.0;
            for(String protokollID : protocolLinks.keySet()) {
                mongoConnection.insertProtocolls(protocolLinks.get(protokollID), protokollID);
                this.currentProtocoll ++;
            }

            MongoDBConnectionHandler_File_Impl handler = new MongoDBConnectionHandler_File_Impl();
            while (handler.getExistingPlaceholderIDs().size() > 0){
                for(String placeholder : handler.getExistingPlaceholderIDs()){
                    handler.removePlaceholder(placeholder);
                    mongoConnection.insertProtocolls(protocolLinks.get(placeholder), placeholder);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }



        Document PartyDoc = Webcrawler.fetchDocFromZip(zipLink);
        mongoConnection.updateSpeakerMeta(PartyDoc);
        mongoConnection.insertSpeakersPictures();

        DBCreator_File_Impl.uploadCategoryEncoding("./resources/ddc3-names-de.csv");

        System.out.println("finished inserting");
    }
    public Double getProgress(){
        return this.currentProtocoll / this.totalProtocolls;
    }
    public void resetProgress(){

            this.currentProtocoll = 0.0;
            this.totalProtocolls = 1.0;
    }
}
