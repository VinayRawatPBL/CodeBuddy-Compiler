import java.awt.*;
import java.util.Random;
import java.util.prefs.Preferences;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ThemeManager {
    private static final String THEME_PREF = "compiler_theme";
    private String currentTheme;
    private Preferences prefs;
    private Timer animationTimer;
    private float fadeFactor = 0.0f;
    private Color targetBackground, targetForeground, targetCaret;
    private JTextArea[] areas;
    private List<Point> stars = new ArrayList<>();
    private Random random = new Random();

    public ThemeManager() {
        prefs = Preferences.userNodeForPackage(ThemeManager.class);
        currentTheme = prefs.get(THEME_PREF, "light");
        animationTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fadeFactor < 1.0f) {
                    fadeFactor += 0.1f;
                    applyAnimatedTheme();
                } else {
                    animationTimer.stop();
                }
            }
        });
        initializeStars();
    }

    private void initializeStars() {
        stars.clear();
        for (int i = 0; i < 50; i++) {
            stars.add(new Point(random.nextInt(800), random.nextInt(500)));
        }
    }

    public void toggleTheme(JTextArea... areas) {
        this.areas = areas;
        switch (currentTheme) {
            case "light": currentTheme = "dark"; break;
            case "dark": currentTheme = "blue"; break;
            case "blue": currentTheme = "solarized"; break;
            case "solarized": currentTheme = "monokai"; break;
            case "monokai": currentTheme = "space"; break;
            case "space": currentTheme = "light"; break;
        }
        startAnimation();
        prefs.put(THEME_PREF, currentTheme);
    }

    public void applyTheme(JTextArea... areas) {
        this.areas = areas;
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
                case "space":
                    area.setBackground(new Color(10, 20, 40));
                    area.setForeground(new Color(255, 255, 255));
                    area.setCaretColor(new Color(0, 191, 255)); // Deep Sky Blue
                    break;
                default:
                    area.setBackground(Color.WHITE);
                    area.setForeground(Color.BLACK);
                    area.setCaretColor(Color.BLACK);
                    break;
            }
        }
    }

    private void startAnimation() {
        fadeFactor = 0.0f;
        animationTimer.start();
    }

    private void applyAnimatedTheme() {
        for (JTextArea area : areas) {
            Color baseBackground = area.getBackground();
            Color baseForeground = area.getForeground();
            Color baseCaret = area.getCaretColor();
            switch (currentTheme) {
                case "dark":
                    targetBackground = new Color(30, 30, 30);
                    targetForeground = new Color(0, 255, 0);
                    targetCaret = Color.WHITE;
                    break;
                case "blue":
                    targetBackground = new Color(0, 32, 96);
                    targetForeground = Color.WHITE;
                    targetCaret = Color.YELLOW;
                    break;
                case "solarized":
                    targetBackground = new Color(0, 43, 54);
                    targetForeground = new Color(131, 148, 150);
                    targetCaret = Color.YELLOW;
                    break;
                case "monokai":
                    targetBackground = new Color(39, 40, 34);
                    targetForeground = new Color(248, 248, 242);
                    targetCaret = Color.WHITE;
                    break;
                case "space":
                    targetBackground = new Color(10, 20, 40);
                    targetForeground = new Color(255, 255, 255);
                    targetCaret = new Color(0, 191, 255);
                    break;
                default:
                    targetBackground = Color.WHITE;
                    targetForeground = Color.BLACK;
                    targetCaret = Color.BLACK;
                    break;
            }
            int r = (int) (baseBackground.getRed() + (targetBackground.getRed() - baseBackground.getRed()) * fadeFactor);
            int g = (int) (baseBackground.getGreen() + (targetBackground.getGreen() - baseBackground.getGreen()) * fadeFactor);
            int b = (int) (baseBackground.getBlue() + (targetBackground.getBlue() - baseBackground.getBlue()) * fadeFactor);
            area.setBackground(new Color(r, g, b));
            r = (int) (baseForeground.getRed() + (targetForeground.getRed() - baseForeground.getRed()) * fadeFactor);
            g = (int) (baseForeground.getGreen() + (targetForeground.getGreen() - baseForeground.getGreen()) * fadeFactor);
            b = (int) (baseForeground.getBlue() + (targetForeground.getBlue() - baseForeground.getBlue()) * fadeFactor);
            area.setForeground(new Color(r, g, b));
            area.setCaretColor(fadeFactor == 1.0f ? targetCaret : baseCaret);
        }
    }

    public void applyStarAnimation(JComponent component) {
        if ("space".equals(currentTheme)) {
            component.setBackground(new Color(10, 20, 40));
            component.repaint();
        }
    }

    public void paintStars(Graphics g, JComponent c) {
        if ("space".equals(currentTheme)) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.WHITE);
            for (Point star : stars) {
                int size = random.nextInt(3) + 1;
                g2d.fillOval(star.x, star.y, size, size);
            }
        }
    }
}
