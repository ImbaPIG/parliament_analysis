
import BackendHelpers.AggregationBuilder;
import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonFactory;
import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonParser;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.DeserializationFeature;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.node.ObjectNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.node.ArrayNode;
import database.MongoDBConnectionHandler_File_Impl;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import spark.Service;

import javax.json.Json;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
            response.status(200);
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
            return convertDocListToJsonList(protocollContentContains(output, request.queryMap().get("contains").value()));
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
    public static List<Document> protocollContentContains(List<Document> protocolls, String toFind) throws IOException {
        List<JSONObject> convertedProtocolls = protocolls.stream().map(protocoll -> new JSONObject(protocoll)).collect(Collectors.toList());
        List<JSONObject> matchedProtocolls = new LinkedList<JSONObject>();
        for(JSONObject protocoll : convertedProtocolls){
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            JsonFactory factory = mapper.getFactory();
            JsonParser createParser = factory.createParser(String.valueOf(protocoll));
            JsonNode jNode = mapper.readTree(createParser);
            ObjectNode oNode = jNode.deepCopy();

            ArrayNode tagespunkte = (ArrayNode) mapper.createArrayNode();
            for (int itpunkte = 0; itpunkte < oNode.get("tagesordnungspunkte").size(); itpunkte++) {
                JsonNode tpunkt = oNode.get("tagesordnungspunkte").get(itpunkte);
                ArrayNode speeches = (ArrayNode) mapper.createArrayNode();
                for (int irede = 0; irede < tpunkt.get("reden").size(); irede++) {
                    ArrayNode rede = (ArrayNode) oNode.get("tagesordnungspunkte").get(itpunkte).get("reden");
                    if (rede.get(irede).get("content").toString().contains(toFind)) {
                        speeches.add(rede.get(irede));
                    }
                }

                if (speeches.size() > 0) {
                    tagespunkte.add(tpunkt);
                }

            }
            if (tagespunkte.size() > 0) {
                try {
                    matchedProtocolls.add(new JSONObject(tagespunkte.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return matchedProtocolls.stream().map(mprotocoll -> Document.parse(mprotocoll.toString()) ).collect(Collectors.toList());
    }










}
