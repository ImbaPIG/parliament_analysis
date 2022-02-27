import BackendHelpers.AggregationBuilder;
import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonFactory;
import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonParser;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.DeserializationFeature;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.node.ArrayNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.node.ObjectNode;
import database.MongoDBConnectionHandler_File_Impl;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import spark.Filter;

import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static BackendHelpers.AggregationHelper.convertDocListToJsonList;
import static spark.Spark.after;
import static spark.Spark.get;
import static webscraper.Webcrawler.iterateOffset;

/**
 * @author Özlem & Moritz
 */
public class server {

    public static void main(String[] args) throws IOException {
        //init basic variables
        AggregationBuilder aggraBuilder = new AggregationBuilder();
        MongoDBConnectionHandler_File_Impl mongo = new MongoDBConnectionHandler_File_Impl();

        Hashtable<String, String> basicProtocollLinks = new Hashtable<String, String>();
        iterateOffset(20, "https://www.bundestag.de/ajax/filterlist/de/services/opendata/866354-866354?offset=", basicProtocollLinks);
        iterateOffset(19, "https://www.bundestag.de/ajax/filterlist/de/services/opendata/543410-543410?offset=", basicProtocollLinks);

        ProtocollHandler protoHandler = new ProtocollHandler();

        System.out.println("Server listening on Port 4567");

        //setup headers
        after((Filter) (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET");
        });

        //registration of routes
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
            List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
            return convertDocListToJsonList(output);
        });
        get("/api/speakers",       (request, response) ->{
            List<Bson> sampleAggregation= aggraBuilder.createSpeakersAggregation(request.queryMap());
            List<Document> output = mongo.aggregateMongo("speakers", sampleAggregation);
            return convertDocListToJsonList(output);
        });
        get("/api/sentiment",       (request, response) ->{
            List<Bson> sampleAggregation= aggraBuilder.createSentimentAggregation(request.queryMap());
            List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
            return convertDocListToJsonList(output);
        });
        get("/api/parties",       (request, response) ->{
            List<Bson> sampleAggregation= aggraBuilder.createPartiesAggregation(request.queryMap());
            List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
            return convertDocListToJsonList(output);
        });
        get("/api/fractions",       (request, response) ->{
            List<Bson> sampleAggregation= aggraBuilder.createFractionsAggregation(request.queryMap());
            List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
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
        get("/api/fetchProtocolls",       (request, response) ->{
            Hashtable<String, String> protocollLinksToFetch = new Hashtable<String, String>();
            protoHandler.resetProgress();
            if (request.queryMap().get("link").value() != null) {
                protocollLinksToFetch.clear();
                protocollLinksToFetch.put("new", request.queryMap().get("link").value());
            } else {
                protocollLinksToFetch = basicProtocollLinks;
            }
            protoHandler.insertAndUpdateProtocolls(protocollLinksToFetch);
            return "finished";
        });
        get("/api/fetchProtocollsProgress",       (request, response) ->{
                return protoHandler.getProgress();
        });
        get("/api/speakerByCategory",       (request, response) ->{
            List<Bson> sampleAggregation= aggraBuilder.createSpeakersByCategoryAggregation(request.queryMap());
            List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
            return convertDocListToJsonList(output);
        });
        get("/api/speechesByCategory",       (request, response) ->{
            List<Bson> sampleAggregation= aggraBuilder.createSpeechesByCategoryAggregation(request.queryMap());
            List<Document> output = mongo.aggregateMongo("speeches", sampleAggregation);
            return convertDocListToJsonList(output);
        });





    }

    /**
     * @author Moritz
     * Methode to return List of Documents with speechText that contains substring
     * @param protocolls list of bson protocolls to be filtered
     * @param toFind string to find in speech
     * @return List of matched Documents
     * @throws IOException
     */
    public static List<Document> protocollContentContains(List<Document> protocolls, String toFind) throws IOException {
        List<JSONObject> convertedProtocolls = protocolls.stream().map(JSONObject::new).collect(Collectors.toList());
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
