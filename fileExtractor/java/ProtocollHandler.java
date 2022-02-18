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

import static webscraper.Webcrawler.iterateOffset;

public class ProtocollHandler {
    public static void main(String[] args) throws IOException, ParseException, ParserConfigurationException, SAXException, UIMAException {
        Hashtable<String, String> protocolLinks = new Hashtable<String, String>();
        iterateOffset(20, "https://www.bundestag.de/ajax/filterlist/de/services/opendata/866354-866354?offset=", protocolLinks);
        iterateOffset(19, "https://www.bundestag.de/ajax/filterlist/de/services/opendata/543410-543410?offset=", protocolLinks);


        String zipLink = "https://www.bundestag.de/resource/blob/472878/d5743e6ffabe14af60d0c9ddd9a3a516/MdB-Stammdaten-data.zip";


        DBCreator_File_Impl mongoConnection = new DBCreator_File_Impl();
        Analysis_File_Impl anal = new Analysis_File_Impl();

        for(String protokollID : protocolLinks.keySet()) {
            mongoConnection.insertProtocolls(protocolLinks.get(protokollID), protokollID);
            anal.parseDocs(protocolLinks.get(protokollID), protokollID);
        }

        MongoDBConnectionHandler_File_Impl handler = new MongoDBConnectionHandler_File_Impl();
        while (handler.getExistingPlaceholderIDs().size() > 0){
            for(String placeholder : handler.getExistingPlaceholderIDs()){
                handler.removePlaceholder(placeholder);
                mongoConnection.insertProtocolls(protocolLinks.get(placeholder), placeholder);
                //anal.parseDocs(protocolLinks.get(placeholder), placeholder);
            }
        }


        Document PartyDoc = Webcrawler.fetchDocFromZip(zipLink);
        mongoConnection.updateSpeakerMeta(PartyDoc);

        // mongoConnection.insertProtocolls(protocolLinks.k);
        //insert
        //get protocollLinks somehow
        // Analysis_File_Impl anal = new Analysis_File_Impl();
        // anal.parseDocs(protocolLinks);

        System.out.println("finished g00d Job m8");
    }

}
