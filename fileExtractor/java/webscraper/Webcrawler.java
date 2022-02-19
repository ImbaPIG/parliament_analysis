package webscraper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import EDU.oswego.cs.dl.util.concurrent.FJTask;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import org.apache.commons.io.IOUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;


public class Webcrawler {



    public static String getImageLink(String vorname, String nachname) throws IOException {
        String prefix ="https://bilddatenbank.bundestag.de";
        String urlToParse = "https://bilddatenbank.bundestag.de/search/picture-result?query="+vorname+ "+"+nachname+ "&filterQuery%5Bereignis%5D%5B%5D=Portr%C3%A4t%2FPortrait&sortVal=3";
        Document currentDoc = Jsoup.connect(urlToParse).get();
        Elements images = currentDoc.select("img");
        if(images.size() < 3){return "";}
        String dlLink = prefix + images.get(2).attr("src");
        System.out.println(dlLink);
        return dlLink;
    }
    public static void iterateOffset(Integer plenarPeriode, String link, Hashtable<String,String> protocollLinks) throws IOException {
        Integer offset = 0;
        Document currentDoc = Jsoup.connect(link + offset.toString()).get();
        while(currentDoc.toString().contains("Plenarprotokoll ")){
            List<Node> table = currentDoc.select("tbody").first().childNodes();
            for(Node listNode: table){
                if(!(listNode instanceof Element)){
                    continue;
                }
                Element itemElem = (Element) listNode;
                String itemText = itemElem.getElementsContainingText("Plenarprotokoll").first().text();
                String protoID = createProtocollID(plenarPeriode,itemText);
                String dlLink = itemElem.getElementsByAttribute("href").first().attr("href");
                String prefix = "https://www.bundestag.de";
                protocollLinks.put(protoID,prefix + dlLink);
            }
            offset ++;
            currentDoc = Jsoup.connect(link + offset.toString()).get();
        }
    }
    public static String createProtocollID(Integer PlenarPeriode, String listText){
        Matcher m = Pattern.compile("[^0-9]*([0-9]+).*").matcher(listText);
        if (m.matches()) {
            System.out.println(m.group(1));
            String result =  String.valueOf(PlenarPeriode) + '-' + m.group(1);
            return result;
        }
        return new String();
    }

    public static Document fetchDocument(String documentLink) throws IOException {

        Document doc = Jsoup.connect(documentLink).get();
        return doc;
    }

    public static org.w3c.dom.Document fetchDocFromZip(String zipLink) throws IOException{

        //sadly DocumentBuilder wants the Bytestream to be stored on the Harddrive D: really sad to convert Byte Array to String
        //Just to convert it to a Document ;) but Jsoup works fine
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        dbFactory.setValidating(false);
        dbFactory.setIgnoringComments(false);
        dbFactory.setIgnoringElementContentWhitespace(true);
        W3CDom w3cDom = new W3CDom();


        InputStream is = new URL(zipLink).openConnection().getInputStream();
        File file = stream2file(is);
        ZipFile zipFile = new ZipFile(file);
        ZipEntry entry = zipFile.getEntry("MDB_STAMMDATEN.XML");
        InputStream zipIn = zipFile.getInputStream(entry);

        List<String> strings = IOUtils.readLines(zipIn);
        String xmlRawString = StringUtils.join(strings, ", ");

        Document document = Jsoup.parse(xmlRawString, "", Parser.xmlParser());
        return w3cDom.fromJsoup(document);
    }
    public static File stream2file (InputStream in) throws IOException {
        final String PREFIX = "filler";
        final String SUFFIX = ".xml";
        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }
    public static org.w3c.dom.Document getDocFromLink(String link) throws IOException {
        W3CDom w3cDom = new W3CDom();
        return w3cDom.fromJsoup(fetchDocument(link));
    }

}

