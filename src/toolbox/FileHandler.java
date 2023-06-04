package toolbox;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class FileHandler {
    public static Document readXML(String path) {
        Document doc;

        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException("couldn't read document at " + path);
        }

        return doc;
    }
}
