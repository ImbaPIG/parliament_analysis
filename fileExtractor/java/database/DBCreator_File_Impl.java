package database;

import bundestag.Protokoll_File_Impl;
import interfaces.DBCreator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Hashtable;
import java.util.Set;

public class DBCreator_File_Impl implements DBCreator {

    public void insert(Hashtable<String, String> protocolLinks){
        /**
         * used to insert all protkolle and jcas into the mongodb
         */
        MongoDBConnectionHandler_File_Impl handler = new MongoDBConnectionHandler_File_Impl();



        // make parser instance
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (Exception e) {e.printStackTrace();}

        // parse through all xml files
        //File[] fs = fileGetter(this.dir);
        for(String protocollID : protocolLinks.keySet()) {
            File f = getFile(protocolLinks.get(protocollID));
            // get sitzungsnr from filename
            String sitzungsnr = protocollID;

            // add protokoll if not already added
            if(handler.protokollExists(sitzungsnr)){
                System.out.println("the protkoll " + sitzungsnr + " already exists, therefore it is skipped");
            } else {
                handler.createPlaceholder(sitzungsnr);
                try{
                    assert builder != null;
                    Protokoll_File_Impl protokoll = new Protokoll_File_Impl(f, builder, handler, sitzungsnr);
                    // String protokollBson = handler.ObjToBson(protokoll);
                    handler.removePlaceholder(sitzungsnr);
                    //handler.uploadBson(protokollBson, "protocol");
                    handler.uploadDoc(protokoll.getDocument(), "protocol");
                } catch (Exception e) {e.printStackTrace();}
           }
        }
    }

    public File getFile(String link){
        // Still needs to be implemented
        File file = new File("../Bundestag_19");
        return file;
    }
    public File[] fileGetter(String dir){
        /**
         * gets all xml files from the given directory
         */
        File folder = new File(dir);
        return folder.listFiles(new FilenameFilter() {
            public boolean accept(File folder, String name) {
                return name.toLowerCase().endsWith(".xml");
            }
        });
    }
}
