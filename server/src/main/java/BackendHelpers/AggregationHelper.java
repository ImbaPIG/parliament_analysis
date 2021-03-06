package BackendHelpers;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import spark.QueryParamsMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *    & Moritz
 */
public class AggregationHelper {

    /**
     *helper methode to create match bson document
     * @param fieldname to match
     * @param matchValue value to match
     * @return bson Document
     */
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

    /**
     *helper methode to create match bson document for party
     * @param fieldname to party
     * @param queryParams params to extract party
     * @return bson Document
     */
    public static Bson partyMatchHelper(String fieldname,QueryParamsMap queryParams){
        return queryParams.get("party").value() == null ? matchHelper("_id",null) : matchHelper(fieldname, queryParams.get("party").value());
    }

    /**
     *helper methode to create unwind bson document
     * @param unwindPath path to unwind
     * @return bson Document
     */
    public static Document unwindHelper(String unwindPath){
        return new Document("$unwind",
                new Document("path", unwindPath)
                        .append("preserveNullAndEmptyArrays", false));

    }

    /**
     *helper methode to create lookup bson document
     * @param fromCollectionName collectionName from to lookup
     * @param localField localField of lookup
     * @param foreignField foreignField of lookup
     * @param newName newName of lookup
     * @return bson Document
     */
    public static Document lookupHelper(String fromCollectionName, String localField, String foreignField, String newName){
        return new Document("$lookup",
                new Document("from", fromCollectionName)
                        .append("localField", localField)
                        .append("foreignField", foreignField)
                        .append("as", newName));
    }

    /**
     *helper methode to create date bson document from string
     * @param stringDate String to be converted to Date
     * @return bson Document
     */
    public static Document stringToDate(String stringDate){
        return new Document("$dateFromString",
                new Document("dateString", stringDate)
                        .append("format","%d.%m.%Y"));
    }

    /**
     *helper methode to create a match that date is inbetween dates
     * @param datePath path of document date
     * @param startDate earliest date to be matched
     * @param endDate latest date to be matched
     * @return bson Document
     */
    public static Document createMatchByDate(String datePath,String startDate, String endDate){
        //null check on dates
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

    /**
     *helper methode to turn string field into date field
     * @param datePath path to date in Document
     * @return bson Document
     */
    public static Document replaceDateStringByDate(String datePath){
        return new Document("$addFields",
                new Document(datePath, stringToDate("$"+datePath)));
    }

    /**
     * helper function to determine if query params match the required values
     * @param queryParams params to extract values from
     * @return boolean false => wrong format
     */
    public static boolean checkAreQueryParamsCorrectFormat(QueryParamsMap queryParams){
        Pattern dateFormat = Pattern.compile("^\\d{2}.\\d{2}.\\d{4}");
        if(queryParams.get("startDate").value() != null){
            Matcher matcher = dateFormat.matcher(queryParams.get("startDate").value());
            if(!matcher.find()){return false;}
        }
        if(queryParams.get("endDate").value() != null){
            Matcher matcher = dateFormat.matcher(queryParams.get("endDate").value());
            if(!matcher.find()){return false;}
        }
        if(queryParams.get("minimum").value() != null){
            try{
                int min = Integer.parseInt(queryParams.get("minimum").value());
            }catch( NumberFormatException e){
                return false;
            }
        }
        return true;
    }

    /**
     *helper function to wrap list of bson documents to json and add sucess parameter
     * @param docs
     * @return JSOnObject
     */
    public static JSONObject convertDocListToJsonList(List<Document> docs){
        List<JSONObject> list = new ArrayList<>();
        for(Document doc : docs){
            list.add(new JSONObject(doc.toJson()));
        }
        JSONObject response = new JSONObject();
        if(list.size() > 0) {
            response.put("sucess", true);
            response.put("result", list);
        }else {
            response.put("sucess", false);
            response.put("error", "No data could be fetched with the provided query, check if QueryParams are set properly");
        }
        return response;
    }

    /**
     *helper function to makes sure that value of queryParams is at least zero
     * @param queryParams
     * @param key
     * @return int >= 0
     */
    public static int minimumOfZero(QueryParamsMap queryParams, String key){
        if(queryParams.get(key).integerValue() == null){
            return 0;
        }
        return queryParams.get(key).integerValue();
    }
}
