package database;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import interfaces.MongoDBConnectionHandler;
import org.apache.uima.UIMAException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XCASDeserializer;
import org.apache.uima.cas.impl.XCASSerializer;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Moritz
 */
public class MongoDBConnectionHandler_File_Impl implements MongoDBConnectionHandler {
    private final MongoDatabase db;
    private final Creds_File_Impl cred;


    /**
     * constructor for MongoDBConnectionHandler_File_Impl
     * establishes connection with Database
     */
    public MongoDBConnectionHandler_File_Impl() {
        this.cred = new Creds_File_Impl();

        // set up mongo db client
        MongoCredential credential = MongoCredential.createScramSha1Credential(cred.getUser(), cred.getDatabase(), cred.getPassword());
        ServerAddress seed = new ServerAddress(cred.getHost(), Integer.parseInt(cred.getPort()));
        List<ServerAddress> seeds = new ArrayList(0);
        seeds.add(seed);
        MongoClientOptions options = MongoClientOptions.builder()
                .connectionsPerHost(50)
                .sslEnabled(false)
                .build();
        MongoClient client = new MongoClient(seeds, credential, options);
        this.db = client.getDatabase(cred.getDatabase());
    }

    /**
     * Methode to find out if a Document with given id in given collection exists
     * @param identifier String of Document
     * @param collection String of Document
     * @return boolean whether found or not
     */
    public boolean existsDocument(String identifier, String collection){
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("_id", identifier);
        //checks if first Document found is null
        return this.db.getCollection(collection).find(whereQuery).first() != null;
    }

    /**
     * getter for bson Document
     * @param identifier String of Document
     * @param collection String of Collection
     * @return bson Document that was found or empty Document
     */
    public Document getDocument(String identifier, String collection){
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("_id", identifier);
        try {
            //finds first
            Document document = this.db.getCollection(collection).find(whereQuery).first();
            return document;
        }
        catch (MongoException e){
            e.printStackTrace();
        }
        return new Document();
    }


    /**
     * Methode to update Document
     * @param doc
     * @param collection
     * @return
     * @throws UIMAException
     */
    public boolean updateDocument(Document doc, String collection) throws UIMAException {

        // Create Where-Query
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("_id", doc.get("_id"));
        UpdateResult uResult = null;
        try {
            uResult = this.db.getCollection(collection).replaceOne(whereQuery, doc);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return uResult != null;

    }

    /**
     * uploads a given bson string to a given collection
     * @param bSon String of bson
     * @param collection String of collection name
     */
    public void uploadBson(String bSon, String collection){
        // parse bson to mongo document
        Document dbDoc = null;
        try {
            dbDoc = Document.parse(bSon);
        } catch (MongoException e) {
            e.printStackTrace();
        }
        // upload document
        assert dbDoc != null;
        this.db.getCollection(collection).insertOne(dbDoc);
    }

    /**
     * uploads a bson Document to a given collection
     * @param dbDoc bson Document
     * @param collection String of collection name
     */
    public void uploadDoc(Document dbDoc, String collection){
        // upload document
        assert dbDoc != null;
        //check if a document already exists with same id in collection
        if(existsDocument(dbDoc.get("_id").toString(), collection)){return;}
        this.db.getCollection(collection).insertOne(dbDoc);
    }

    /**
     * uploads a list of bson Documents to a given collection
     * @param dbDocs List of bson Documents to be inserted
     * @param collection String of collection name
     */
    public void uploadDocs(List<Document> dbDocs, String collection){
        assert dbDocs != null;
        this.db.getCollection(collection).insertMany(dbDocs);
    }

    /**
     * Helper to aggregate a mongo pipeline, returns all values from collection if aggregation is wrong
     * @param collection String of collection name
     * @param aggregation List of bson Documents (pipeline to aggregate)
     * @return List of bson Documents from aggregation result
     */
    public List<Document> aggregateMongo (String collection, List<Bson> aggregation){
        try {
            return this.db.getCollection(collection).aggregate(aggregation).allowDiskUse(true).into(new ArrayList<>());
        }catch (MongoException e){
            e.printStackTrace();
            return this.db.getCollection(collection).aggregate(Arrays.asList()).into(new ArrayList<>());
        }
    }

    /**
     * converts JCas to XML String
     * @param jcas Jcas to be converted
     * @return XML String of Jcas
     * @throws IOException
     */
    public String JCasToXML(JCas jcas) {
        /**
         * Serialize JCas Object to a xml String
         */
        String outString = "";
        CAS cas = jcas.getCas();
        //created outputStream and serializes it with XCASSerializer
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            XCASSerializer.serialize(cas, out);
            outString = out.toString();
            out.close();
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }

        return outString;
    }

    /**
     * converts XML String to Jcas
     * @param xml String to be converted into Jcas
     * @return Jcas Object
     * @throws UIMAException
     */
    public JCas XMLToJcas(String xml) throws UIMAException {
        JCas emptyJcas = JCasFactory.createText("", "de");
        //created inputStream and deserializes it with XCASDeserializer
        ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes());
        CAS cas = emptyJcas.getCas();
        try {
            XCASDeserializer.deserialize(in, cas);
        } catch (IOException | SAXException e) {
        }
        return cas.getJCas();
    }


    /**
     * checks if jcas obj exists in database
     * @param redeID id of Document that is inspected
     * @return true if Document is found
     */
    public boolean jCasExists(String redeID) {
        Document doc = new Document("_id", redeID);
        Document d = db.getCollection(cred.getJcasCollection()).find(doc).first();
        return d != null;
    }

    /**
     * checks if protkoll obj exists in database
     * @param sitzungsnr id of Protocoll
     * @return true if Protocoll bson is found
     */
    public boolean protokollExists(String sitzungsnr){
        try{
            //create new bson Document with sitzungsnr as ID
            Document doc = new Document("_id", sitzungsnr);
            Document d = db.getCollection(cred.getProtocollCollection()).find(doc).first();
            return d != null;
        }catch (MongoSocketOpenException e){
            System.out.println("couldnt reach mongo");
        }
        return true;
    }

    /**
     * creates a placeholder for a protkoll in the db so no other instance of the uploader trys to upload it (so no doubling)
     * @param sitzungsnr id of protocoll
     */
    public void createPlaceholder(String sitzungsnr){
        Document doc = new Document("_id", sitzungsnr);
        doc.append("placeholder", true);
        db.getCollection(cred.getProtocollCollection()).insertOne(doc);
        System.out.println("The Placeholder for protkoll " + sitzungsnr + " was created");
    }

    /**
     * removes previously created placeholder
     * @param sitzungsnr id of protocoll
     * */
    public void removePlaceholder(String sitzungsnr){
        Document doc = new Document("_id", sitzungsnr);
        doc.append("placeholder", true);
        Document filter = db.getCollection(cred.getProtocollCollection()).find(doc).first();
        if(filter != null) {
            DeleteResult result = db.getCollection(cred.getProtocollCollection()).deleteOne(filter);
            System.out.println("The Placeholder for protkoll " + sitzungsnr + " was deleted");
        } else {
            System.out.println("No Placeholder for protkoll " + sitzungsnr + " was found, therefore it couldn't be deleted");
        }
    }


    /**
     * finds all document ids of placeholders
     * @return List of document ids
     */
    public List<String> getExistingPlaceholderIDs(){
        LinkedList<String> pIDs = new LinkedList<>();
        Document doc = new Document("placeholder", true);
        //search for all Documents with placeholder:true
        FindIterable<Document> placeholders = this.db.getCollection(cred.getProtocollCollection()).find(doc);

        //iterate over result and append each id to pIDs list
        for(Document placeholder : placeholders){
            pIDs.push(placeholder.getString("_id"));
        }
        return pIDs;
    }

    public long countProtokolle(){
        /**
         * counts the amount of protkolls in the db
         */
        return db.getCollection(cred.getProtocollCollection()).countDocuments();
    }


    /**
     * creates a JCasTuple (including the reden and comments) from the mongodb
     * @param redeID
     * @return Jcas Tuple with reden and comments
     * @throws UIMAException
     */
    public JCasTuple_FIle_Impl getRedeJcas(String redeID) throws UIMAException {
        Document doc = new Document("_id", redeID);
        Document result = db.getCollection(cred.getJcasCollection()).find(doc).first();
        assert result != null;
        String contentXMl = result.getString("contentXML");
        String commentXML = result.getString("commentXML");
        return new JCasTuple_FIle_Impl(XMLToJcas(contentXMl), XMLToJcas(commentXML));
    }

}
