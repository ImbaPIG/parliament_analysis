package interfaces;

import database.JCasTuple_FIle_Impl;
import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;

import java.io.IOException;

public interface MongoDBConnectionHandler {
    public void uploadBson(String bSon, String collection);

    public String JCasToXML(JCas jcas) throws IOException;

    public JCas XMLToJcas(String xml) throws UIMAException;

    public void resetDB();

    public boolean jCasExists(String redeID);

    public boolean protokollExists(String sitzungsnr);

    public void createPlaceholder(String sitzungsnr);

    public void removePlaceholder(String sitzungsnr);

    public void deleteAllPlaceholder();

    public long countProtokolle();

    public void intelligentPlaceholderDeleter()throws InterruptedException;

    public JCasTuple_FIle_Impl getRedeJcas(String redeID) throws UIMAException;
}
