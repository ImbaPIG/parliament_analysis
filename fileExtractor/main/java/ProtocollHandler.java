import java.io.IOException;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

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
     * @author Moritz
     *Methode to handle processing and coordinating of protocolls
     * @param args
     * @throws IOException
     * @throws ParseException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws UIMAException
     */

    //used to determine progress
    private Double currentProtocoll = 0.0;
    private Double totalProtocolls = 1.0;
    public void insertAndUpdateProtocolls(Hashtable<String, String> protocolLinks) throws IOException, ParseException, ParserConfigurationException, SAXException, UIMAException {

        //link to finde mdb data
        String zipLink = "https://www.bundestag.de/resource/blob/472878/d5743e6ffabe14af60d0c9ddd9a3a516/MdB-Stammdaten-data.zip";

        DBCreator_File_Impl mongoConnection = new DBCreator_File_Impl();

        try{
            this.totalProtocolls = protocolLinks.size() + 0.0;
            for(String protokollID : protocolLinks.keySet()) {
                mongoConnection.insertProtocolls(protocolLinks.get(protokollID), protokollID);
                //increment progress
                this.currentProtocoll ++;
                TimeUnit.MILLISECONDS.sleep(50);
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
        //update Meta of Speakers (Link and Pictures)
        Document PartyDoc = Webcrawler.fetchDocFromZip(zipLink);
        mongoConnection.updateSpeakerMeta(PartyDoc);
        mongoConnection.insertSpeakersPictures();

        //upload ddc3 Category Encodings
        DBCreator_File_Impl.uploadCategoryEncoding("./resources/ddc3-names-de.csv");

    }

    /**
     * @author Moritz
     * returns progress of current protocoll Parsing
     * @return
     */
    public Double getProgress(){
        //nan check
        return Double.isNaN(this.currentProtocoll / this.totalProtocolls) ? 0 : this.currentProtocoll / this.totalProtocolls;
    }

    /**
     * @author Özlem
     * reset progress of current protocoll parsing
     */
    public void resetProgress(){
            this.currentProtocoll = 0.0;
            this.totalProtocolls = 1.0;
    }
}
