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
import toolbox.FileHandler;

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

    public static Vector2f getSize(Element component, Vector2f parentSize) {
        String widthVal = component.getAttribute("width");
        float width = switch (widthVal) {
            case "match-parent" -> parentSize.x;
            default -> Float.parseFloat(widthVal);
        };

        String heightVal = component.getAttribute("height");
        float height = switch (heightVal) {
            case "match-parent" -> parentSize.y;
            case "wrap-content" -> -1;
            default -> Float.parseFloat(heightVal);
        };

        return new Vector2f(width, height);
    }

    public static Vector2f getPosition(Element component, Vector2f parentPosition) {
        float x = Float.parseFloat(component.getAttribute("position").split(",")[0]);
        float y = Float.parseFloat(component.getAttribute("position").split(",")[1]);

        return Vector2f.add(parentPosition, new Vector2f(x, y), null);
    }

    private static ArrayList<Element> getChildren(Element element) {
        ArrayList<Element> children = new ArrayList<>();

        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE)
                children.add((Element) nodeList.item(i));
        }

        return children;
    }

    private static boolean hasChildren(Element element) {
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) return true;
        }

        return false;
    }

    public static void addGUIs(Element root, String group, Vector2f parentPosition, Vector2f parentSize) {
        ArrayList<Element> children = getChildren(root);

        for (Element component : children) {
            GUIElement guiElement;

            Vector2f position = getPosition(component, parentPosition);
            Vector2f size = getSize(component, parentSize);
            if (component.getTagName().equals("container")) {
                addGUIs(component, group, position, size);
                continue;
            }

            Vector3f foreground = Colours.WHITE;
            if (!component.getAttribute("foreground").equals(""))
                foreground = Colours.getColour(component.getAttribute("foreground"));


            Vector3f background = Colours.BLACK;
            if (!component.getAttribute("background").equals(""))
                background = Colours.getColour(component.getAttribute("background"));

            float border = 0;
            if (!component.getAttribute("border").equals(""))
                border = Float.parseFloat(component.getAttribute("border"));
            String text = component.getTextContent().strip();
            String id = component.getAttribute("id");

            String type = component.getTagName();
            if (hasChildren(component)) text = "";

            if (type.equals(""))
                throw new RuntimeException("component has no type in " + group);
            if (type.equals("textfield"))
                guiElement = new TextField(background, foreground, position, size, text, border, id);
            else if (type.equals("button"))
                guiElement = new Button(background, foreground, position, size, text, border, id);
            else
                throw new RuntimeException("component type invalid in " + group);


            if (guiElement.getSize().y == -1) // if height set to wrap-content
                guiElement.setSize(new Vector2f(guiElement.getSize().x, guiElement.getText().getHeight() + 2 * (border / Display.getHeight())));

            guiElement.setGroup(group);
            GUIMaster.addElement(guiElement);
            addGUIs(component, group, position, size);
        }
    }

    public static void addFromFile(String path) {
        Document doc = FileHandler.readXML(path);
        Element root = doc.getDocumentElement();

        addGUIs(root, root.getAttribute("groupid"), new Vector2f(), new Vector2f());
    }

    public static void setFont(Loader loader, String font) {
        GUIMaster.font = new FontType(loader.loadTexture(font + ".png"), new File(font + ".fnt"));
    }

    public static void addElement(GUIElement element) {
        elements.add(element);
        guis.add(element.getTexture());
    }

    public static void removeElement(GUIElement element) {
        elements.remove(element);
        guis.remove(element.getTexture());
        TextMaster.removeText(element.getText());
    }

    public static void checkEvents() {
        // since an ActionEvent may alter the contents of elements, events should be serviced after
        // elements has been iterated through
        ArrayList<ActionEvent> events = new ArrayList<>();
        ArrayList<GUIElement> guiElements = new ArrayList<>();

        for (GUIElement element : elements)
            if (element instanceof Button && element.wasClicked()) {
                events.add(((Button) element).getEvent());
                guiElements.add(element);
            }

        for (int i = 0; i < events.size(); i++)
            if (events.get(i) != null)
                events.get(i).onClick(guiElements.get(i));

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
        ArrayList<GUIElement> elementsToRemove = new ArrayList<>();

        for (GUIElement element : elements)
            if (element.getGroup().equals(group)) elementsToRemove.add(element);

        for (GUIElement element : elementsToRemove)
            removeElement(element);
    }

    public static void applyActionEventToGroup(String group, ActionEvent event) {
        for (GUIElement element : elements) {
            if (element.getGroup().equals(group) && element instanceof Button)
                ((Button) element).setEvent(event);
        }
    }
}
