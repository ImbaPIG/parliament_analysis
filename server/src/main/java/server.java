
import static spark.Spark.*;

public class server {

    public static void main(String[] args){
        System.out.println("Server listening on Port 4567");
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
