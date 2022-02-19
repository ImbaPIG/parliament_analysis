package interfaces;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

public interface DBCreator {
    public void insertProtocolls(String protocolLink, String protocollID) throws IOException;
}
