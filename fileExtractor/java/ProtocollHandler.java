import database.DBCreator_File_Impl;
import database.MongoDBConnectionHandler_File_Impl;
import nlp.Analysis_File_Impl;

import java.io.IOException;
import java.util.Hashtable;
import webscraper.*;
import static spark.Spark.*;
import static webscraper.webscraper.iterateOffset;

public class ProtocollHandler {
    public static void main(String[] args) throws IOException {
        Hashtable<String, String> protocolLinks = new Hashtable<String, String>();
        iterateOffset(20, "https://www.bundestag.de/ajax/filterlist/de/services/opendata/866354-866354?offset=", protocolLinks);
         // iterateOffset(19, "https://www.bundestag.de/ajax/filterlist/de/services/opendata/543410-543410?offset=", protocolLinks);

        System.out.println("lul about to create");
        // DBCreator_File_Impl mongoConnection = new DBCreator_File_Impl();

        for(String protokollID : protocolLinks.keySet()){
            DBCreator_File_Impl mongoConnection = new DBCreator_File_Impl();
            Analysis_File_Impl anal = new Analysis_File_Impl();
            mongoConnection.insertProtocolls(protocolLinks.get(protokollID), protokollID);
            anal.parseDocs(protocolLinks.get(protokollID), protokollID);
        }
        // mongoConnection.insertProtocolls(protocolLinks.k);
        //insert
        //get protocollLinks somehow
        // Analysis_File_Impl anal = new Analysis_File_Impl();
        // anal.parseDocs(protocolLinks);

        System.out.println("finished");
    }

}
