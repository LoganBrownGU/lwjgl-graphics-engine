package toolbox;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

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

    public static void writeXML(String path, Document document) {
        try {
            DOMSource source = new DOMSource(document);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            FileWriter writer = new FileWriter(path);
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
        } catch (IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Element> getChildren(Element element) {
        ArrayList<Element> children = new ArrayList<>();

        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE)
                children.add((Element) nodeList.item(i));
        }

        return children;
    }

    public static float[][] readPixels(String path) {
        PNGDecoder decoder;
        BufferedInputStream stream;

        try {
            stream = new BufferedInputStream(new FileInputStream(path));
            decoder = new PNGDecoder(stream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading " + path + " heightmap");
        }

        int width = decoder.getWidth();
        int height = decoder.getHeight();
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height);
        try {
            decoder.decode(buffer, width, PNGDecoder.Format.ALPHA);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading " + path + " Could be bad format (should be 8bpc RGB)");
        }

        short[][] raw = new short[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                raw[i][j] = (short) (buffer.get(i * width + j) & 0x00FF);// (float) Math.pow(2, 24);

        for (short b : raw[0])
            System.out.printf("%04X ", b);
        System.out.println(height * width + " " + buffer.capacity());

        float[][] data = new float[height][width];
        for (int i = 0; i < raw.length; i++)
            for (int j = 0; j < raw[i].length; j++)
                data[i][j] = (float) raw[i][j] / 0xff;

        System.out.println(Arrays.toString(data[0]));

        return data;
    }
}