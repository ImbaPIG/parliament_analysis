package database;

import java.io.*;
import java.util.Properties;

/**
 *   
 */
public class Creds_File_Impl {
    private Properties prop;
    public Creds_File_Impl() {
        /**
         * gets the mongodb login data and makes it easily available
         */
        InputStream propStream = null;
        try {
            propStream = new FileInputStream(new File("./mongoDB.properties"));
        } catch (FileNotFoundException e) {
            System.out.println("DB Login File not found");
        }
        this.prop = new Properties();
        try {
            prop.load(propStream);
        } catch (IOException e) {
            System.out.println("There is a Problem with your db login data");
        }
    }

    // Getters for all Properties
    public String getHost(){return prop.getProperty("host");}
    public String getDatabase(){return prop.getProperty("database");}
    public String getUser(){return prop.getProperty("user");}
    public char[] getPassword(){return prop.getProperty("password").toCharArray();}
    public String getPort(){return prop.getProperty("port");}
    public String getProtocollCollection(){return prop.getProperty("protocoll_collection");}
    public String getJcasCollection(){return prop.getProperty("jcas_collection");}

}
