package BackendHelpers;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.client.model.Aggregates.*;
import org.json.JSONObject;
import spark.QueryParamsMap;

import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Filters.eq;

public class AggregationHelper {

    public static Bson matchHelper(String fieldname, String matchValue){
        Bson matchDoc = new Document();
        if(matchValue == null){
            matchDoc = new Document("$match",
                    new Document(fieldname,
                            new Document("$exists", true)));
        }else{
            matchDoc = new Document("$match",
                    new Document(fieldname, matchValue));
        }
        return matchDoc;
    }
    public static Bson partyMatchHelper(String fieldname,QueryParamsMap queryParams){
        return queryParams.get("party").value() == null ? matchHelper("_id",null) : matchHelper(fieldname, queryParams.get("party").value());
    }
    public static Document unwindHelper(String unwindPath){
        return new Document("$unwind",
                new Document("path", unwindPath)
                        .append("preserveNullAndEmptyArrays", false));

    }
    public static Document lookupHelper(String fromCollectionName, String localField, String foreignField, String newName){
        return new Document("$lookup",
                new Document("from", fromCollectionName)
                        .append("localField", localField)
                        .append("foreignField", foreignField)
                        .append("as", newName));
    }


    public static Document stringToDate(String stringDate){
        return new Document("$dateFromString",
                new Document("dateString", stringDate)
                        .append("format","%d.%m.%Y"));
    }

    public static Document createMatchByDate(String datePath,String startDate, String endDate){
        //if dates are null
        startDate = startDate == null ? "01.01.2000" : startDate;
        endDate = endDate == null ? "01.01.3000" : endDate;

        Document matchDoc = new Document("$match",
                new Document("$expr",
                        new Document("$and", Arrays.asList(
                                new Document("$gte", Arrays.asList(datePath, stringToDate(startDate))),
                                new Document("$lte", Arrays.asList(datePath, stringToDate(endDate)))
                        )

                        )));
        return matchDoc;
    }
    public static Document replaceDateStringByDate(String datePath){
        return new Document("$addFields",
                new Document(datePath, stringToDate("$"+datePath)));
    }

    public boolean checkAreQueryParamsCorrectFormat(QueryParamsMap queryParams){
        //todo vervollständigen Check Params
        // rednerID startDate endDate fraktion party minAmount
        Pattern dateFormat = Pattern.compile("^\\d{2}.\\d{2}.\\d{4}");
        if(queryParams.get("startDate").value() != null){
            Matcher matcher = dateFormat.matcher(queryParams.get("startDate").value());
            if(!matcher.find()){return false;}
        }
        if(queryParams.get("endDate").value() != null){
            Matcher matcher = dateFormat.matcher(queryParams.get("endDate").value());
            if(!matcher.find()){return false;}
        }
        if(queryParams.get("party").value() != null){

        }
        if(queryParams.get("party").value() != null){

        }
        return true;
    }
    public static JSONObject convertDocListToJsonList(List<Document> docs){
        List<JSONObject> list = new ArrayList<>();
        for(Document doc : docs){
            list.add(new JSONObject(doc.toJson()));
        }
        JSONObject response = new JSONObject();
        response.put("sucess",true);
        response.put("result", list);
        return response;
    }
    public static int minimumOfZero(QueryParamsMap queryParams, String key){
        if(queryParams.get(key).integerValue() == null){
            return 0;
        }
        return queryParams.get(key).integerValue();
    }
}
