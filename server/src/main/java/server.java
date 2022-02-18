
import BackendHelpers.AggregationBuilder;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import database.MongoDBConnectionHandler_File_Impl;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import spark.Service;

import java.util.*;

import static BackendHelpers.AggregationHelper.convertDocListToJsonList;
import static spark.Spark.*;


public class server {

    public static void main(String[] args){
        //todo : list.add(new JSONObject(next.toJson()));
        AggregationBuilder aggraBuilder = new AggregationBuilder();
        MongoDBConnectionHandler_File_Impl mongo = new MongoDBConnectionHandler_File_Impl();
        System.out.println("Server listening on Port 4567");
        Service service = Service.ignite();
        /*

        get("/hello", (request, response) -> {
                    List<Bson> sampleAggregation= aggraBuilder.createNamedEntitiesAggregation(request.queryMap());
                    List<Document> output2 = mongo.aggregateMongo("analyzedSpeeches", sampleAggregation);
                    return convertDocListToJsonList(output2);
                }
        );

         */

        get("/api/tokens",       (request, response) ->{
            List<Bson> sampleAggregation= aggraBuilder.createTokenAggregation(request.queryMap());
            List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
            return convertDocListToJsonList(output);
        });
        get("/api/speech",       (request, response) -> {
            List<Bson> sampleAggregation= aggraBuilder.createSpeechAggregation(request.queryMap());
            List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
            return convertDocListToJsonList(output);
        });
        get("/api/namedEntities",       (request, response) ->{
            List<Bson> sampleAggregation= aggraBuilder.createNamedEntitiesAggregation(request.queryMap());
            List<Document> output = mongo.aggregateMongo("analyzedSpeeches", sampleAggregation);
            return convertDocListToJsonList(output);
        });
        get("/api/speakers",       (request, response) ->{
            List<Bson> sampleAggregation= aggraBuilder.createSpeakersAggregation(request.queryMap());
            List<Document> output = mongo.aggregateMongo("speakers", sampleAggregation);
            return convertDocListToJsonList(output);
        });
        get("/api/sentiment",       (request, response) ->{
            List<Bson> sampleAggregation= aggraBuilder.createSentimentAggregation(request.queryMap());
            List<Document> output = mongo.aggregateMongo("speakers", sampleAggregation);
            return convertDocListToJsonList(output);
        });
        get("/api/parties",       (request, response) ->{
            List<Bson> sampleAggregation= aggraBuilder.createPartiesAggregation(request.queryMap());
            List<Document> output = mongo.aggregateMongo("speakers", sampleAggregation);
            return convertDocListToJsonList(output);
        });
        get("/api/fractions",       (request, response) ->{
            List<Bson> sampleAggregation= aggraBuilder.createFractionsAggregation(request.queryMap());
            List<Document> output = mongo.aggregateMongo("speakers", sampleAggregation);
            return convertDocListToJsonList(output);
        });
        get("/api/statistic",       (request, response) ->{
            List<Bson> sampleAggregation= aggraBuilder.createStatisticAggregation(request.queryMap());
            List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
            return convertDocListToJsonList(output);
        });
        get("/api/pos",       (request, response) ->{
            List<Bson> sampleAggregation= aggraBuilder.createPOSAggregation(request.queryMap());
            List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
            return convertDocListToJsonList(output);
        });
        get("/api/fullTextSearch",       (request, response) ->{
            System.out.println("searching");
            List<Bson> sampleAggregation= aggraBuilder.createFullTextSearchAggregation(request.queryMap());
            List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
            return convertDocListToJsonList(matchProtocollsRegex(output));
        });


        /*
        service.path("/api", () -> {
            before((q, a) -> System.out.println("Received api call"));
            path("/tokens", () -> {
                get("/api/tokens",       (request, response) ->{
                    List<Bson> sampleAggregation= aggraBuilder.createTokenAggregation(request.queryMap());
                    List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
                    return convertDocListToJsonList(output);
                });
            });
            path("/api/speech", () -> {
                get("/api/speech",       (request, response) -> {
                    List<Bson> sampleAggregation= aggraBuilder.createSpeechAggregation(request.queryMap());
                    List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
                    return convertDocListToJsonList(output);
                });
            });
            path("/api/namedEntities", () -> {
                get("/api/namedEntities",       (request, response) ->{
                    List<Bson> sampleAggregation= aggraBuilder.createNamedEntitiesAggregation(request.queryMap());
                    List<Document> output = mongo.aggregateMongo("analyzedSpeeches", sampleAggregation);
                    return convertDocListToJsonList(output);
                });
            });
            path("/api/speakers", () -> {
                get("/api/speakers",       (request, response) ->{
                    List<Bson> sampleAggregation= aggraBuilder.createSpeakersAggregation(request.queryMap());
                    List<Document> output = mongo.aggregateMongo("speakers", sampleAggregation);
                    return convertDocListToJsonList(output);
                });
            });
            path("/api/sentiment", () -> {
                get("/api/sentiment",       (request, response) ->{
                    List<Bson> sampleAggregation= aggraBuilder.createSentimentAggregation(request.queryMap());
                    List<Document> output = mongo.aggregateMongo("speakers", sampleAggregation);
                    return convertDocListToJsonList(output);
                });
            });
            path("/api/parties", () -> {
                get("/api/parties",       (request, response) ->{
                    List<Bson> sampleAggregation= aggraBuilder.createPartiesAggregation(request.queryMap());
                    List<Document> output = mongo.aggregateMongo("speakers", sampleAggregation);
                    return convertDocListToJsonList(output);
                });
            });
            path("/api/fractions", () -> {
                get("/api/fractions",       (request, response) ->{
                    List<Bson> sampleAggregation= aggraBuilder.createFractionsAggregation(request.queryMap());
                    List<Document> output = mongo.aggregateMongo("speakers", sampleAggregation);
                    return convertDocListToJsonList(output);
                });
            });
            path("/api/statistic", () -> {
                get("/api/statistic",       (request, response) ->{
                    List<Bson> sampleAggregation= aggraBuilder.createStatisticAggregation(request.queryMap());
                    List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
                    return convertDocListToJsonList(output);
                });
            });
            path("/api/pos", () -> {
                get("/api/pos",       (request, response) ->{
                    List<Bson> sampleAggregation= aggraBuilder.createPOSAggregation(request.queryMap());
                    List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
                    return convertDocListToJsonList(output);
                });
            });
            path("/api/fullTextSearch", () -> {
                get("/api/fullTextSearch",       (request, response) ->{
                    System.out.println("searching");
                    List<Bson> sampleAggregation= aggraBuilder.createFullTextSearchAggregation(request.queryMap());
                    List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
                    return convertDocListToJsonList(output);
                });
            });
            //createFullTextSearchAggregation
        });
        Hashtable<String, String> protocolLinks = webscraper.getprotocollLinks();
        //insert
        //get protocollLinks somehow
        System.out.println("started");
        Analysis_File_Impl anal = new Analysis_File_Impl();
        anal.parseDocs(protocolLinks);

         */
    }
    public static List<Document> matchProtocollsRegex(List<Document> protocolls){
        for(int indexProtocolls = 0;indexProtocolls<protocolls.size();indexProtocolls++){
            Object protocoll = protocolls.get(indexProtocolls);
            for(int indexTagespunkte = 0;indexTagespunkte<protocolls.get(indexProtocolls).size();indexTagespunkte++){
                //Object
                //for(int indexReden = 0;indexReden<pro)
            }
        }

        System.out.println("reached");
        Object first = protocolls.get(0).get("tagesordnungspunkte");
        System.out.println(protocolls.get(0).get("tagesordnungspunkte"));
        System.out.println("lul");
        return protocolls;
    }










}
