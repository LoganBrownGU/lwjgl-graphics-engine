package gui;

import fontMeshCreator.FontType;
import fontRendering.TextMaster;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import renderEngine.Loader;
import toolbox.Colours;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GUIMaster {

    private static final ArrayList<GUIElement> elements = new ArrayList<>();
    private static final ArrayList<GUITexture> guis = new ArrayList<>();
    protected static FontType font;

    private static Document readDocument(String path) {
        Document doc;

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.parse(path);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            throw new RuntimeException(path + " not found");
        }

        return doc;
    }

    private static ArrayList<Element> readComponents(String path) {
        Document doc = readDocument(path);
        Element root = doc.getDocumentElement();
        root.normalize();

        ArrayList<Element> componentElements = new ArrayList<>();

        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE && ((Element) node).getTagName().equals("component"))
                componentElements.add((Element) node);
        }

        return componentElements;
    }

    private static String getGroupID(String path) {
        Document doc = readDocument(path);
        return doc.getDocumentElement().getAttribute("groupid");
    }

    public static Vector2f getSize(Element component) {
        String widthVal = component.getAttribute("width");
        float width = Float.parseFloat(widthVal);
        String heightVal = component.getAttribute("height");
        float height = heightVal.equals("wrap-content") ? -1 : Float.parseFloat(heightVal);

        return new Vector2f(width * Display.getWidth(), height * Display.getHeight());
    }

    public static void addGUI(String path) {
        ArrayList<Element> components = readComponents(path);

        for (Element component : components) {
            GUIElement guiElement = null;
            Vector3f foreground = Colours.getColour(component.getAttribute("foreground"));
            Vector3f background = Colours.getColour(component.getAttribute("background"));

            float x = Float.parseFloat(component.getAttribute("position").split(",")[0]) * Display.getWidth();
            float y = Float.parseFloat(component.getAttribute("position").split(",")[1]) * Display.getHeight();
            Vector2f position = new Vector2f(x, y);
            Vector2f size = getSize(component);
            float border = 0;
            if (!component.getAttribute("border").equals(""))
                border = Float.parseFloat(component.getAttribute("border"));
            String text = component.getTextContent().strip();
            String id = component.getAttribute("id");

            if (component.getAttribute("type").equals(""))
                throw new RuntimeException("component has no type in " + path);

            if (component.getAttribute("type").equals("textfield"))
                guiElement = new TextField(background, foreground, position, size, text, border, id);
            else
                throw new RuntimeException("component type invalid in " + path);


            if (guiElement.getSize().y == -1) // if height set to wrap-content
                guiElement.setSize(new Vector2f(guiElement.getSize().x, guiElement.getText().getHeight() + 2 * (border / Display.getHeight())));

            guiElement.setGroup(getGroupID(path));
            GUIMaster.addElement(guiElement);
        }
    }

    public static void setFont(Loader loader, String font) {
        GUIMaster.font = new FontType(loader.loadTexture(font + ".png"), new File(font + ".fnt"));
    }

    public static void addElement(GUIElement element) {
        // todo make all positioning relative
        elements.add(element);
        guis.add(element.getTexture());
    }

    public static void removeElement(GUIElement element) {
        elements.remove(element);
        guis.remove(element.getTexture());
    }

    public static void checkEvents() {
        for (GUIElement element : elements) {
            if (element instanceof Button && element.wasClicked()) ((Button) element).getEvent().onClick();
        }
    }

    public static void clear() {
        for (GUIElement element : elements)
            TextMaster.removeText(element.getText());

        elements.clear();
        guis.clear();
    }

    public static void render(GUIRenderer renderer) {
        renderer.render(guis);
    }

    public static GUIElement getElementByID(String id) {
        for (GUIElement element : elements)
            if (element.getId().equals(id)) return element;

        throw new RuntimeException("element with id " + id + " does not exist");
    }

    public static void removeGroup(String group) {
        elements.removeIf(element -> element.getGroup().equals(group));
    }
}
