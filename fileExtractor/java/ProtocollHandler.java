import nlp.Analysis_File_Impl;

import java.io.IOException;
import java.util.Hashtable;
import webscraper.*;
import static spark.Spark.*;

public class ProtocollHandler {
    public static void main(String[] args) throws IOException {
        get("/hello", (request, response) ->
                "Hello, world"
        );
        get("/users/:id", (request, response) ->
            "Hello, "+ request.params(":id")
        );
        /*
        Hashtable<String, String> protocolLinks = webscraper.getprotocollLinks();
        //insert
        //get protocollLinks somehow
        System.out.println("started");
        Analysis_File_Impl anal = new Analysis_File_Impl();
        anal.parseDocs(protocolLinks);

         */
    }

}
