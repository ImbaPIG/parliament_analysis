package database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import interfaces.MongoDBConnectionHandler;
import org.apache.uima.UIMAException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XCASDeserializer;
import org.apache.uima.cas.impl.XCASSerializer;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.bson.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MongoDBConnectionHandler_File_Impl implements MongoDBConnectionHandler {
    public final MongoDatabase db;
    private final Creds_File_Impl cred;


    public MongoDBConnectionHandler_File_Impl() {
        /**
         * establishes a connection with the mongodb
         */
        this.cred = new Creds_File_Impl();

        // set up mongo db client
        MongoCredential credential = MongoCredential.createScramSha1Credential(cred.getUser(), cred.getDatabase(), cred.getPassword());
        ServerAddress seed = new ServerAddress(cred.getHost(), Integer.parseInt(cred.getPort()));
        List<ServerAddress> seeds = new ArrayList(0);
        seeds.add(seed);
        MongoClientOptions options = MongoClientOptions.builder()
                .connectionsPerHost(10)
                .socketTimeout(5000)
                .maxWaitTime(1000)
                .connectTimeout(1000)
                .sslEnabled(false)
                .build();
        MongoClient client = new MongoClient(seeds, credential, options);
        this.db = client.getDatabase(cred.getDatabase());
    }

    public void uploadBson(String bSon, String collection){
        /**
         * uploads a given bson string to a given collection
          */
        // validate collection name
        if(collection == "protocol"){
            collection = this.cred.getProtocollCollection();
        } else if(collection == "jcas"){
            collection = this.cred.getJcasCollection();
        } else {
            System.out.println("ERROR - " + collection + " is not a valid collection");}
        // parse bson to mongo document
        Document dbDoc = null;
        try {
            dbDoc = Document.parse(bSon);
        } catch (Exception e) {
            System.out.println("The given Bson string is not parseable");
            e.printStackTrace();
        }
        // upload document
        assert dbDoc != null;
        this.db.getCollection(collection).insertOne(dbDoc);
    }
    public void uploadDoc(Document dbDoc, String collection){
        /**
         * uploads a given dbDoc to a given collection
         */
        // validate collection name
        if(collection == "protocol"){
            collection = this.cred.getProtocollCollection();
        } else if(collection == "jcas"){
            collection = this.cred.getJcasCollection();
        } else {
            System.out.println("ERROR - " + collection + " is not a valid collection");}
        // upload document
        assert dbDoc != null;
        this.db.getCollection(collection).insertOne(dbDoc);
    }

    /*
    kann weg i guess
    public String ObjToBson(Object a){
        /**
         * creates from a Object (should be Protokoll or JCasDBObj) a Bson String
        assert(a instanceof Protokoll_File_Impl | a instanceof JcasDBObj);
        ObjectMapper mapper = new ObjectMapper();
        String objectString = "";
        try {
            objectString = mapper.writeValueAsString(a);
        }catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objectString;

    }
    */



    public String JCasToXML(JCas jcas) throws IOException {
        /**
         * Serialize JCas Object to a xml String
         */
        String outString = "";
        CAS cas = jcas.getCas();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            XCASSerializer.serialize(cas, out);
            outString = out.toString();
        } catch (IOException | SAXException ignored) {
        }
        out.close();
        return outString;
    }

    public JCas XMLToJcas(String xml) throws UIMAException {
        /**
         * Deserialize xml String to JCas object
         */
        JCas emptyJcas = JCasFactory.createText("", "de");
        ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes());
        CAS cas = emptyJcas.getCas();
        try {
            XCASDeserializer.deserialize(in, cas);
        } catch (IOException | SAXException e) {
        }
        return cas.getJCas();
    }

    public void resetDB(){
        /**
         * resets all collections
         */
        db.getCollection(cred.getProtocollCollection()).deleteMany(new Document());
        db.getCollection(cred.getJcasCollection()).deleteMany(new Document());
        System.out.println("The database was cleaned");
    }

    public boolean jCasExists(String redeID) {
        /**
         * checks if jcas obj exists in database
         */
        Document doc = new Document("_id", redeID);
        Document d = db.getCollection(cred.getJcasCollection()).find(doc).first();
        return d != null;
    }

    public boolean protokollExists(String sitzungsnr){
        /**
         * checks if protkoll obj exists in database
         */
        Document doc = new Document("_id", sitzungsnr);
        Document d = db.getCollection(cred.getProtocollCollection()).find(doc).first();
        return d != null;
    }

    public void createPlaceholder(String sitzungsnr){
        /**
         * creates a placeholder for a protkoll in the db so no other instance of the uploader trys to upload it (so no doubling)
         */
        Document doc = new Document("_id", sitzungsnr);
        doc.append("placeholder", true);
        db.getCollection(cred.getProtocollCollection()).insertOne(doc);
        System.out.println("The Placeholder for protkoll " + sitzungsnr + " was created");
    }

    public void removePlaceholder(String sitzungsnr){
        /**
         * removes previously created placeholder
         */
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

    public void deleteAllPlaceholder(){
        /**
         * deletes all placeholder to clean up failed uploads that didnt reach the removePlaceholder method
         */
        Document doc = new Document("placeholder", true);
        DeleteResult result = db.getCollection(cred.getProtocollCollection()).deleteMany(doc);
    }

    public long countProtokolle(){
        /**
         * counts the amount of protkolls in the db
         */
        return db.getCollection(cred.getProtocollCollection()).countDocuments();
    }

    public void intelligentPlaceholderDeleter() throws InterruptedException {
        /**
         * cleans all the placeholder smartly when the db gets clogged with placeholders
         */
        int i = 0;
        while (i < 4){
            if(this.countProtokolle() > 220){
                if (i>0){i++;}
                this.deleteAllPlaceholder();
                System.out.println("deleted placeholder");
            } else {
                System.out.println("there is still work to do, nothing is clogged");
                i = 0;
            }
            TimeUnit.MINUTES.sleep(2);
        }
    }

    public JCasTuple_FIle_Impl getRedeJcas(String redeID) throws UIMAException {
        /**
         * creates a JCasTuple (including the reden and comments) from the mongodb
         */
        Document doc = new Document("_id", redeID);
        Document result = db.getCollection(cred.getJcasCollection()).find(doc).first();
        assert result != null;
        String contentXMl = result.getString("contentXML");
        String commentXML = result.getString("commentXML");
        return new JCasTuple_FIle_Impl(XMLToJcas(contentXMl), XMLToJcas(commentXML));
    }
}
