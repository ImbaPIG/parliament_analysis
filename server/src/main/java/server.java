
import org.bson.Document;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class server {

    public static void main(String[] args){
        System.out.println("Server listening on Port 4567");
        get("/hello", (request, response) -> {
                    System.out.println(request.queryParams().toString());
                    return "lul" + request.queryParams("idk");
                }
        );
        path("/api", () -> {
            before((q, a) -> System.out.println("Received api call"));
            path("/tokens", () -> {
                get("/",       (request, response) ->"lul tokens" + request.params());
            });
            path("/speech", () -> {
                get("/",       (request, response) ->"lul speech" + request.params());
            });
            path("/namedEntities", () -> {
                get("/",       (request, response) ->"lul namedEntities" + request.params());
            });
            path("/speakers", () -> {
                get("/",       (request, response) ->"lul speakers" + request.params());
            });
            path("/sentiment", () -> {
                get("/",       (request, response) ->"lul sentiment" + request.params());
            });
            path("/parties", () -> {
                get("/",       (request, response) ->"lul parties" + request.params());
            });
            path("/fractions", () -> {
                get("/",       (request, response) ->"lul fractions" + request.params());
            });
            path("/statistic", () -> {
                get("/",       (request, response) ->"lul statistic" + request.params());
            });
            path("/pos", () -> {
                get("/",       (request, response) ->"lul pos" + request.params());
            });
        });
        /*
        Hashtable<String, String> protocolLinks = webscraper.getprotocollLinks();
        //insert
        //get protocollLinks somehow
        System.out.println("started");
        Analysis_File_Impl anal = new Analysis_File_Impl();
        anal.parseDocs(protocolLinks);

         */
    }

    public Document matchHelper(String fieldname, String matchValue){
        Document matchDoc = new Document();
        if(matchValue == null){
            matchDoc = new Document("$match",
                    new Document(fieldname, new Document("$exists", true)));
        }else{
            matchDoc = new Document("$match",
                    new Document(fieldname, matchValue));
        }
        return matchDoc;
    }
    public Document unwindHelper(String unwindPath){
        return new Document("$unwind",
                new Document("path", "unwindPath")
                        .append("preserveNullAndEmptyArrays", true));
    }
    public Document lookupHelper(String fromCollectionName, String localField, String foreignField, String newName){
        return new Document("$lookup",
                new Document("from", fromCollectionName)
                        .append("localField", localField)
                        .append("foreignField", foreignField)
                        .append("as", newName));
    }


    public Document stringToDate(String stringDate){
        return new Document("$dateFromString",
                new Document("dateString", stringDate)
                .append("format","%d.%m.%Y"));
    }

    public Document createMatchByDate(String datePath,String startDate, String endDate){
        //if dates are null
        startDate = startDate == null ? "01.01.2000" : startDate;
        endDate = endDate == null ? "01.01.3000" : endDate;

        Document matchDoc = new Document("$match",
                new Document("$expr",
                        new Document("$and",Arrays.asList(
                                new Document("$gte", Arrays.asList(datePath, stringToDate(startDate))),
                                new Document("$lte", Arrays.asList(datePath, stringToDate(endDate)))
                        )

                )));
        return matchDoc;
    }
    public Document replaceDateStringByDate(String datePath){
        return new Document("$addFields",
                new Document("$date", stringToDate("$date")));
    }

    public List<Document> createTokenAggregation(Map<String,String> queryParams){
        Integer min = 0;
        try {
            min = queryParams.get("minimum") == null ? 0 : Integer.parseInt(queryParams.get("minimum"));
        }catch (NumberFormatException e){
        }
        return Arrays.asList(
                unwindHelper("$tagesordnungspunkte"),
                unwindHelper("$tagesordnungspunkte.reden"),
                lookupHelper("analyzedSpeeches","tagesordnungspunkte.reden.redeID","_id","analyzed"),
                lookupHelper("speakers","tagesordnungspunkte.reden.rednerID","_id","redner"),
                matchHelper("redner._id",queryParams.get("rednerID")),
                matchHelper("redner.fraktion",queryParams.get("fraktion")),
                matchHelper("redner.party",queryParams.get("party")),
                unwindHelper("$analyzed"),
                new Document("$replaceRoot",
                        new Document("newRoot", "$analyzed")),
                unwindHelper("$token"),
                new Document("$group",
                        new Document("_id", "$token")
                                .append("count",
                                        new Document("$sum", 1L))),
                new Document("$project",
                        new Document("_id", 0L)
                                .append("token", "$_id")
                                .append("count",
                                        new Document("$filter",
                                                new Document("input", "$count")
                                                .append("as", "count")
                                                .append("cond",
                                                        new Document("$gte", Arrays.asList("$$count",min)))))),
                new Document("$sort",
                        new Document("count", -1L)));
    }


}
