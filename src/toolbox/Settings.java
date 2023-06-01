package toolbox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Settings {
    public static int FPS_CAP, MSAA_LEVEL, RES_X, RES_Y;
    public static boolean VSYNC_ENABLED, FULLSCREEN_ENABLED;

    private static HashMap<String, String> readSettings(String settingsPath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(settingsPath));
            HashMap<String, String> hash = new HashMap<>();

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("=");
                hash.put(parts[0], parts[1]);
            }

            br.close();
            return hash;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void updateSettings(String settingsPath) {
        HashMap<String, String> hash = readSettings(settingsPath);
        if (hash == null) throw new RuntimeException("Settings couldn't be read");

        FPS_CAP = Integer.parseInt(hash.get("fps"));
        MSAA_LEVEL = Integer.parseInt(hash.get("msaa"));
        RES_X = Integer.parseInt(hash.get("resolution").split("x")[0]);
        RES_Y = Integer.parseInt(hash.get("resolution").split("x")[1]);
        VSYNC_ENABLED = Boolean.parseBoolean(hash.get("vsync"));
        FULLSCREEN_ENABLED = Boolean.parseBoolean(hash.get("fullscreen"));
    }
}
