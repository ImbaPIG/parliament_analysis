package webscraper;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class webscraper {


    public static Hashtable<String, String> getprotocollLinks() throws IOException {
        Hashtable<String, String> protocolLinks = new Hashtable<String, String>();
        iterateOffset(20, "https://www.bundestag.de/ajax/filterlist/de/services/opendata/866354-866354?offset=", protocolLinks);
        iterateOffset(19, "https://www.bundestag.de/ajax/filterlist/de/services/opendata/543410-543410?offset=", protocolLinks);
        return protocolLinks;
    }

    public static String getImageLink(String vorname, String nachname) throws IOException {
        String prefix ="https://bilddatenbank.bundestag.de";
        String urlToParse = "https://bilddatenbank.bundestag.de/search/picture-result?query=" +vorname+ "+" +nachname+ "filterQuery%5Bereignis%5D%5B%5D=Portr%C3%A4t%2FPortrait&sortVal=3";
        Document currentDoc = Jsoup.connect(urlToParse).get();
        Elements images = currentDoc.select("img");
        String dlLink = prefix + images.get(2).attr("src");
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

}

