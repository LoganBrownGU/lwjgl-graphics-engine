package toolbox;

import org.lwjgl.util.vector.Vector3f;

public class Colours {
    public static final Vector3f BLACK = new Vector3f(0, 0, 0);
    public static final Vector3f BLUE = new Vector3f(0, 0, 1);
    public static final Vector3f RED = new Vector3f(1, 0, 0);
    public static final Vector3f GREEN = new Vector3f(0, 1, 0);
    public static final Vector3f YELLOW = new Vector3f(1, 1, 0);
    public static final Vector3f WHITE = new Vector3f(1, 1, 1);
    public static final Vector3f DARK_GREY = new Vector3f(.2f, .2f, .2f);

    public static Vector3f getColour(String colour) {
        switch (colour) {
            case "black":
                return BLACK;
            case "blue":
                return BLUE;
            case "red":
                return RED;
            case "green":
                return GREEN;
            case "yellow":
                return YELLOW;
            case "white":
                return WHITE;
            case "dark-grey":
                return DARK_GREY;
        }

        throw new RuntimeException(colour + " not a vaid colour");
    }
}
