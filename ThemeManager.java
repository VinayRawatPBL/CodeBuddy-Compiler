import java.awt.*;
import java.util.prefs.Preferences;
import javax.swing.*;

// Vinay: Fixed for JTextArea compatibility, added themes
public class ThemeManager {
    private static final String THEME_PREF = "compiler_theme";
    private String currentTheme;
    private Preferences prefs;

    public ThemeManager() {
        prefs = Preferences.userNodeForPackage(ThemeManager.class);
        currentTheme = prefs.get(THEME_PREF, "light");
    }

    public void toggleTheme(JTextArea... areas) {
        switch (currentTheme) {
            case "light": currentTheme = "dark"; break;
            case "dark": currentTheme = "blue"; break;
            case "blue": currentTheme = "solarized"; break;
            case "solarized": currentTheme = "monokai"; break;
            default: currentTheme = "light"; break;
        }
        applyTheme(areas);
        prefs.put(THEME_PREF, currentTheme);
    }

    public void applyLightTheme(JTextArea... areas) {
        currentTheme = "light";
        applyTheme(areas);
        prefs.put(THEME_PREF, currentTheme);
    }

    private void applyTheme(JTextArea... areas) {
        for (JTextArea area : areas) {
            switch (currentTheme) {
                case "dark":
                    area.setBackground(new Color(30, 30, 30));
                    area.setForeground(new Color(0, 255, 0));
                    area.setCaretColor(Color.WHITE);
                    break;
                case "blue":
                    area.setBackground(new Color(0, 32, 96));
                    area.setForeground(Color.WHITE);
                    area.setCaretColor(Color.YELLOW);
                    break;
                case "solarized":
                    area.setBackground(new Color(0, 43, 54));
                    area.setForeground(new Color(131, 148, 150));
                    area.setCaretColor(Color.YELLOW);
                    break;
                case "monokai":
                    area.setBackground(new Color(39, 40, 34));
                    area.setForeground(new Color(248, 248, 242));
                    area.setCaretColor(Color.WHITE);
                    break;
                default:
                    area.setBackground(Color.WHITE);
                    area.setForeground(Color.BLACK);
                    area.setCaretColor(Color.BLACK);
                    break;
            }
        }
    }
}