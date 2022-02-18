
import BackendHelpers.AggregationBuilder;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import database.MongoDBConnectionHandler_File_Impl;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import java.util.*;

import static BackendHelpers.AggregationHelper.convertDocListToJsonList;
import static spark.Spark.*;


public class server {

    public static void main(String[] args){
        //todo : list.add(new JSONObject(next.toJson()));
        AggregationBuilder aggraBuilder = new AggregationBuilder();
        MongoDBConnectionHandler_File_Impl mongo = new MongoDBConnectionHandler_File_Impl();
        System.out.println("Server listening on Port 4567");
        /*

        get("/hello", (request, response) -> {
                    List<Bson> sampleAggregation= aggraBuilder.createNamedEntitiesAggregation(request.queryMap());
                    List<Document> output2 = mongo.aggregateMongo("analyzedSpeeches", sampleAggregation);
                    return convertDocListToJsonList(output2);
                }
        );

         */

        path("/api", () -> {
            before((q, a) -> System.out.println("Received api call"));
            path("/tokens", () -> {
                get("/",       (request, response) ->{
                    List<Bson> sampleAggregation= aggraBuilder.createTokenAggregation(request.queryMap());
                    List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
                    return convertDocListToJsonList(output);
                });
            });
            path("/speech", () -> {
                get("/",       (request, response) -> {
                    List<Bson> sampleAggregation= aggraBuilder.createSpeechAggregation(request.queryMap());
                    List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
                    return convertDocListToJsonList(output);
                });
            });
            path("/namedEntities", () -> {
                get("/",       (request, response) ->{
                    List<Bson> sampleAggregation= aggraBuilder.createNamedEntitiesAggregation(request.queryMap());
                    List<Document> output = mongo.aggregateMongo("analyzedSpeeches", sampleAggregation);
                    return convertDocListToJsonList(output);
                });
            });
            path("/speakers", () -> {
                get("/",       (request, response) ->{
                    List<Bson> sampleAggregation= aggraBuilder.createSpeakersAggregation(request.queryMap());
                    List<Document> output = mongo.aggregateMongo("speakers", sampleAggregation);
                    return convertDocListToJsonList(output);
                });
            });
            path("/sentiment", () -> {
                get("/",       (request, response) ->{
                    return "not yet implemented /sentiment";
                });
            });
            path("/parties", () -> {
                get("/",       (request, response) ->{
                    return "not yet implemented /parties";
                });
            });
            path("/fractions", () -> {
                get("/",       (request, response) ->{
                    return "not yet implemented /fractions";
                });
            });
            path("/statistic", () -> {
                get("/",       (request, response) ->{
                    return "not yet implemented /statistic";
                });
            });
            path("/pos", () -> {
                get("/",       (request, response) ->{
                    return "not yet implemented /pos";
                });
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










}
