package toolbox;

import ar.com.hjg.pngj.*;
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
        PngReader decoder = new PngReaderInt(new File(path));
        float[][] data = new float[decoder.imgInfo.rows][decoder.imgInfo.cols];

        for (int i = 0; i < decoder.imgInfo.rows; i++) {
            ImageLineInt line = (ImageLineInt) decoder.readRow();
            int[] scanline = line.getScanline();

            for (int j = 0; j < scanline.length; j++)
                data[i][j] = (float) (scanline[j] / Math.pow(2, decoder.imgInfo.bitspPixel));
        }

        return data;
    }

    public static void writeHeightmap(String path, float[][] heights) {
        ImageInfo info = new ImageInfo(heights[0].length, heights.length, 16, false, true, false);
        PngWriter writer = new PngWriter(new File(path), info);

        float max = (float) (Math.pow(2, 16) - 1);
        for (float[] height : heights) {
            ImageLineInt line = new ImageLineInt(info);
            for (int j = 0; j < height.length; j++) {
                int value = (int) (max * height[j]);
                line.getScanline()[j] = value;
            }
            writer.writeRow(line);
        }

        writer.close();
    }
}