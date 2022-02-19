package BackendHelpers;

import com.mongodb.AggregationOptions;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UnwindOptions;
import org.bson.BsonNull;
import org.bson.Document;
import org.bson.conversions.Bson;
import se.lth.cs.srl.languages.Language;
import spark.QueryParamsMap;

import static BackendHelpers.AggregationHelper.*;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;

import com.mongodb.AggregationOptions.Builder;

import java.util.*;


import java.util.Arrays;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.conversions.Bson;
import java.util.concurrent.TimeUnit;
import org.bson.Document;

import javax.management.Query;


public class AggregationBuilder {

    public List<Bson> createTokenAggregation(QueryParamsMap queryParams){
        //todo: check if input params are valid
        return Arrays.asList(
                unwindHelper("$tagesordnungspunkte"),
                unwindHelper("$tagesordnungspunkte.reden"),
                lookupHelper("analyzedSpeeches","tagesordnungspunkte.reden.redeID","_id","analyzed"),
                lookupHelper("speakers","tagesordnungspunkte.reden.rednerID","_id","redner"),
                matchHelper("redner._id",queryParams.get("rednerID").value()),
                matchHelper("redner.fraktion",queryParams.get("fraktion").value()),
                matchHelper("redner.party",queryParams.get("party").value()),
                replaceDateStringByDate("date"),
                createMatchByDate("$date",queryParams.get("startDate").value(),queryParams.get("endDate").value()),
                unwindHelper("$analyzed"),
                new Document("$replaceRoot",
                        new Document("newRoot", "$analyzed")),
                unwindHelper("$token"),
                new Document("$group",
                        new Document("_id", "$token")
                                .append("count",
                                        new Document("$sum", 1))),
                new Document("$sort",
                        new Document("count", -1)),
                new Document("$match",
                        new Document("count",
                                new Document("$gte", queryParams.get("minimum").integerValue()))));


    }
    public List<Bson> createNamedEntitiesAggregation(QueryParamsMap queryParams){
        return Arrays.asList(
                unwindHelper("$tagesordnungspunkte"),
                unwindHelper("$tagesordnungspunkte.reden"),
                lookupHelper("analyzedSpeeches","tagesordnungspunkte.reden.redeID","_id","analyzed"),
                lookupHelper("speakers","tagesordnungspunkte.reden.rednerID","_id","redner"),
                matchHelper("redner._id",queryParams.get("rednerID").value()),
                matchHelper("redner.fraktion",queryParams.get("fraktion").value()),
                matchHelper("redner.party",queryParams.get("party").value()),
                replaceDateStringByDate("date"),
                createMatchByDate("$date",queryParams.get("startDate").value(),queryParams.get("endDate").value()),
                unwindHelper("$analyzed"),
                new Document("$replaceRoot",
                        new Document("newRoot", "$analyzed")),
                new Document("$facet",
                        new Document("persons", Arrays.asList(
                                unwindHelper("$persons"),
                                new Document("$group",
                                            new Document("_id", "$persons")
                                            .append("count",
                                                    new Document("$sum",1))),
                                new Document("$project",
                                        new Document("count", 1)
                                                .append("element", "$_id")
                                                .append("_id", 0)),
                                matchHelper("_id", queryParams.get("rednerID").value()),
                                new Document("$sort",
                                        new Document("count", -1)),
                                new Document("$match",
                                        new Document("count",
                                                new Document("$gte", minimumOfZero(queryParams,"minimum"))))))
                        .append("organisations",Arrays.asList(
                                unwindHelper("$organisations"),
                                new Document("$group",
                                        new Document("_id", "$organisations")
                                                .append("count",
                                                        new Document("$sum",1))),
                                new Document("$project",
                                        new Document("count", 1)
                                                .append("element", "$_id")
                                                .append("_id", 0)),
                                new Document("$match",
                                        new Document("count",
                                                new Document("$gte", minimumOfZero(queryParams,"minimum")))),
                                new Document("$sort",
                                        new Document("count", -1))))
                        .append("locations",Arrays.asList(
                                unwindHelper("$locations"),
                                new Document("$group",
                                        new Document("_id", "$locations")
                                                .append("count",
                                                        new Document("$sum",1))),
                                new Document("$project",
                                        new Document("count", 1)
                                                .append("element", "$_id")
                                                .append("_id", 0)),
                                new Document("$match",
                                        new Document("count",
                                                new Document("$gte", minimumOfZero(queryParams,"minimum")))),
                                new Document("$sort",
                                        new Document("count", -1)))))
        );

    }
    public List<Bson> createSpeechAggregation(QueryParamsMap queryParams){
        return Arrays.asList(
                unwindHelper("$tagesordnungspunkte"),
                unwindHelper("$tagesordnungspunkte.reden"),
                matchHelper("tagesordnungspunkte.reden.redeID", queryParams.get("redeID").value()),
                new Document("$project",
                        new Document("date", "$date")
                                .append("speaker", "$tagesordnungspunkte.reden.rednerID")
                                .append("id", "$tagesordnungspunkte.reden.redeID")
                                .append("content", "$tagesordnungspunkte.reden.content")
                                .append("_id",0)
                                .append("perodeID","$_id")),
                new Document("$lookup",
                        new Document("from", "NotSoanalyzedSpeeches")
                                .append("localField", "id")
                                .append("foreignField", "_id")
                                .append("as", "analyzed")),
                unwindHelper("$analyzed"));
    }
    public List<Bson> createSpeakersAggregation(QueryParamsMap queryParams){
        return Arrays.asList(
                partyMatchHelper("party",queryParams),
                matchHelper("fraktion", queryParams.get("fraktion").value()),
                matchHelper("_id", queryParams.get("rednerID").value()));
    }
    public List<Bson> createSentimentAggregation(QueryParamsMap queryParams){
        return Arrays.asList(
                unwindHelper("$tagesordnungspunkte"),
                unwindHelper("$tagesordnungspunkte.reden"),
                replaceDateStringByDate("date"),
                createMatchByDate("$date",queryParams.get("startDate").value(),queryParams.get("endDate").value()),
                lookupHelper("analyzedSpeeches","tagesordnungspunkte.reden.redeID","_id","analyzed"),
                lookupHelper("speakers","tagesordnungspunkte.reden.rednerID","_id","redner"),
                matchHelper("tagesordnungspunkte.reden.rednerID", queryParams.get("rednerID").value()),
                matchHelper("redner.fraktion", queryParams.get("fraktion").value()),
                partyMatchHelper("party",queryParams),
                unwindHelper("$analyzed"),
                new Document("$replaceRoot",
                        new Document("newRoot", "$analyzed")),
                new Document("$group",
                        new Document("_id", "$sentiment")
                                .append("count",
                                        new Document("$sum", 1))),
                new Document("$project",
                        new Document("_id", 0)
                                .append("sentiment", "$_id")
                                .append("count", 1)),
                new Document("$sort",
                        new Document("count", -1)));
    }
    public List<Bson> createPartiesAggregation(QueryParamsMap queryParams){
        return Arrays.asList(
                unwindHelper("$tagesordnungspunkte"),
                unwindHelper("$tagesordnungspunkte.reden"),
                replaceDateStringByDate("date"),
                createMatchByDate("$date",queryParams.get("startDate").value(),queryParams.get("endDate").value()),
                new Document("$lookup",
                        new Document("from", "speakers")
                                .append("localField", "tagesordnungspunkte.reden.rednerID")
                                .append("foreignField", "_id")
                                .append("as", "redner")),
                unwindHelper("$redner"),
                matchHelper("redner.party", null),
                new Document("$group",
                        new Document("_id", "$redner.party")
                                .append("count",
                                        new Document("$sum", 1L))),
                new Document("$sort",
                        new Document("count", -1L)));
    }
    public List<Bson> createFractionsAggregation(QueryParamsMap queryParams){
        return Arrays.asList(
                unwindHelper("$tagesordnungspunkte"),
                unwindHelper("$tagesordnungspunkte.reden"),
                replaceDateStringByDate("date"),
                createMatchByDate("$date",queryParams.get("startDate").value(),queryParams.get("endDate").value()),
                new Document("$lookup",
                        new Document("from", "speakers")
                                .append("localField", "tagesordnungspunkte.reden.rednerID")
                                .append("foreignField", "_id")
                                .append("as", "redner")),
                unwindHelper("$redner"),
                matchHelper("redner.fraktion", null),
                new Document("$group",
                        new Document("_id", "$redner.fraktion")
                                .append("count",
                                        new Document("$sum", 1L))),
                new Document("$sort",
                        new Document("count", -1L)));
    }
    public List<Bson> createStatisticAggregation(QueryParamsMap queryParams){
        return Arrays.asList(
                unwindHelper("$tagesordnungspunkte"),
                unwindHelper("$tagesordnungspunkte.reden"),
                new Document("$facet",
                        new Document("persons", Arrays.asList(
                                new Document("$project",
                                        new Document("content",
                                                new Document("$toString", "$tagesordnungspunkte.reden.content"))
                                                .append("id", "$tagesordnungspunkte.reden.redeID")),
                                new Document("$match",
                                        new Document("content",
                                                new Document("$not",
                                                        new Document("$eq",
                                                                new BsonNull())))),
                                new Document("$project",
                                        new Document("_id", 0L)
                                                .append("id", "$id")
                                                .append("length",
                                                        new Document("$strLenCP", "$content"))),
                                new Document("$sort",
                                        new Document("length", -1L))))
                                .append("speakers",Arrays.asList(
                                        new Document("$project",
                                                new Document("id", "$tagesordnungspunkte.reden.rednerID")),
                                        new Document("$group",
                                                new Document("_id", "$id")
                                                        .append("count",
                                                                new Document("$sum", 1))),
                                        new Document("$match",
                                                new Document("_id",
                                                        new Document("$not",
                                                                new Document("$eq",
                                                                        new BsonNull())))),
                                        new Document("$sort",
                                                new Document("count", -1)))))
        );

    }
    public List<Bson> createPOSAggregation(QueryParamsMap queryParams){
        return Arrays.asList(
                unwindHelper("$tagesordnungspunkte"),
                unwindHelper("$tagesordnungspunkte.reden"),
                lookupHelper("analyzedSpeeches","tagesordnungspunkte.reden.redeID","_id","analyzed"),
                lookupHelper("speakers","tagesordnungspunkte.reden.rednerID","_id","redner"),
                matchHelper("redner._id",queryParams.get("rednerID").value()),
                matchHelper("redner.fraktion",queryParams.get("fraktion").value()),
                matchHelper("redner.party",queryParams.get("party").value()),
                unwindHelper("$analyzed"),
                new Document("$replaceRoot",
                        new Document("newRoot", "$analyzed")),
                unwindHelper("$pos"),
                new Document("$group",
                        new Document("_id", "$pos")
                                .append("count",
                                        new Document("$sum", 1))),
                new Document("$match",
                        new Document("count",
                                new Document("$gte", minimumOfZero(queryParams,"minimum")))),
                new Document("$sort",
                        new Document("count", -1L)));
    }
    public List<Bson> createFullTextSearchAggregation(QueryParamsMap queryParamsMap){
        return Arrays.asList(new Document("$match",
                        new Document("tagesordnungspunkte.reden.content",
                                new Document("$exists", true))));
    }

    public Document testAggregation(){
        /*
         * Requires the MongoDB Java Driver.
         * https://mongodb.github.io/mongo-java-driver
         */

        MongoClient mongoClient = new MongoClient(
                new MongoClientURI(
                        "mongodb://PRG_WiSe21_Gruppe_1_3:aNx8P12u@prg2021.texttechnologylab.org:27020/?authSource=PRG_WiSe21_Gruppe_1_3&readPreference=primary&appname=MongoDB+Compass&directConnection=true&ssl=false"
                )
        );
        MongoDatabase database = mongoClient.getDatabase("PRG_WiSe21_Gruppe_1_3");
        MongoCollection<Document> collection = database.getCollection("speeches");

        List<Document> result = collection.aggregate(Arrays.asList(
                unwindHelper("$tagesordnungspunkte"),
                unwindHelper("$tagesordnungspunkte.reden"),
                lookupHelper("analyzedSpeeches","tagesordnungspunkte.reden.redeID","_id","analyzed"),
                lookupHelper("speakers","tagesordnungspunkte.reden.rednerID","_id","redner"),
                matchHelper("redner._id",null),
                matchHelper("redner.fraktion",null),
                matchHelper("redner.party",null),
                replaceDateStringByDate("date"),
                createMatchByDate("$date",null,null),
                unwindHelper("$analyzed"),
                new Document("$replaceRoot",
                        new Document("newRoot", "$analyzed")),
                unwindHelper("$token"),
                new Document("$group",
                        new Document("_id", "$token")
                                .append("count",
                                        new Document("$sum", 1))),
                new Document("$sort",
                        new Document("count", -1)),
                new Document("$match",
                        new Document("count",
                                new Document("$gte", 99))))).into(new ArrayList<>());
        //System.out.println(result.toString());
        return result.get(0);
    }



}

/*
unwindHelper("$tagesordnungspunkte"),
                unwindHelper("$tagesordnungspunkte.reden"),
                lookupHelper("analyzedSpeeches","tagesordnungspunkte.reden.redeID","_id","analyzed"),
                lookupHelper("speakers","tagesordnungspunkte.reden.rednerID","_id","redner"),
                matchHelper("redner._id",queryParams.get("rednerID").value()),
                matchHelper("redner.fraktion",queryParams.get("fraktion").value()),
                matchHelper("redner.party",queryParams.get("party").value()),
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

                        public List<Bson> createTokenAggregation(QueryParamsMap queryParams){
        Integer min = 0;
        UnwindOptions unwindOptions = new UnwindOptions();
        unwindOptions.preserveNullAndEmptyArrays(true);
        try {
            min = queryParams.get("minimum") == null ? 0 : Integer.parseInt(queryParams.get("minimum").value());
        }catch (NumberFormatException e){
        }
        return Arrays.asList(
                Aggregates.unwind("$tagesordnungspunkte", unwindOptions),
                Aggregates.unwind("$tagesordnungspunkte.reden", unwindOptions),
                Aggregates.lookup("analyzedSpeeches","tagesordnungspunkte.reden.redeID","_id","analysiert"),
                Aggregates.lookup("speakers","tagesordnungspunkte.reden.rednerID","_id","redner"),
                Aggregates.match(matchHelper("$redner._id",queryParams.get("rednerID").value())),
                Aggregates.match(matchHelper("$redner.fraktion",queryParams.get("fraktion").value())),
                Aggregates.match(matchHelper("$redner.party",queryParams.get("party").value())),
                Aggregates.unwind("$analysiert", unwindOptions),
                Aggregates.replaceRoot("$analysiert"),
                Aggregates.unwind("$token", unwindOptions),
                Aggregates.limit(10000),
                Aggregates.group("$token", sum("count",1)));


    }
    public List<Bson> createGroupAggregation(QueryParamsMap queryParams){
        Integer min = 0;
        UnwindOptions unwindOptions = new UnwindOptions();
        unwindOptions.preserveNullAndEmptyArrays(true);
        return Arrays.asList(
                Aggregates.unwind("$tagesordnungspunkte", unwindOptions),
                Aggregates.unwind("$tagesordnungspunkte.reden", unwindOptions),
                Aggregates.lookup("analyzedSpeeches","tagesordnungspunkte.reden.redeID","_id","analysiert"),
                Aggregates.lookup("speakers","tagesordnungspunkte.reden.rednerID","_id","redner"),
                Aggregates.group("$periode", sum("count",1L)));


    }

    public List<Bson> WAS TOO SLOW, DONT USe (QueryParamsMap queryParams){
        Integer min = 0;
        UnwindOptions unwindOptions = new UnwindOptions();
        unwindOptions.preserveNullAndEmptyArrays(true);
        try {
            min = queryParams.get("minimum") == null ? 0 : Integer.parseInt(queryParams.get("minimum").value());
        }catch (NumberFormatException e){
        }
        return Arrays.asList(
                Aggregates.unwind("$tagesordnungspunkte", unwindOptions),
                Aggregates.unwind("$tagesordnungspunkte.reden", unwindOptions),
                Aggregates.lookup("analyzedSpeeches","tagesordnungspunkte.reden.redeID","_id","analysiert"),
                Aggregates.lookup("speakers","tagesordnungspunkte.reden.rednerID","_id","redner"),
                Aggregates.match(matchHelper("$redner._id",queryParams.get("rednerID").value())),
                Aggregates.match(matchHelper("$redner.fraktion",queryParams.get("fraktion").value())),
                Aggregates.match(matchHelper("$redner.party",queryParams.get("party").value())),
                Aggregates.unwind("$analysiert", unwindOptions),
                Aggregates.replaceRoot("$analysiert"),
                Aggregates.unwind("$token", unwindOptions),
                Aggregates.limit(10000),
                Aggregates.group("$token", sum("count",1)));


    }
 */
