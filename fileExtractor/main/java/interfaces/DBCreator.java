package interfaces;

import org.apache.uima.UIMAException;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Hashtable;

/**
 * @author Moritz
 */
public interface DBCreator {
    public void insertProtocolls(String protocolLink, String protocollID) throws IOException, ParseException, UIMAException, SAXException;
}
