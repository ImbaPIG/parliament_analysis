package interfaces;

/**
 * @author Erik
 */
public interface Creds {
    public String getHost();

    public String getDatabase();

    public String getUser();

    public char[] getPassword();

    public String getPort();

    public String getProtocollCollection();

    public String getJcasCollection();
}
