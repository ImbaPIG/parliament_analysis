import nlp.Analysis_File_Impl;

import java.util.Hashtable;

public class ProtocollHandler {
    public static void main(String[] args) {
        Hashtable<String, String> protocolLinks = new Hashtable<String, String>();
        //get protocollLinks somehow
        System.out.println("started");
        Analysis_File_Impl anal = new Analysis_File_Impl();
        anal.parseDocs(protocolLinks);
    }

}
