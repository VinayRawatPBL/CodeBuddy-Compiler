import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;

public class MainCompiler extends JFrame {
    private JTextArea codeArea, outputArea;
    private Lexer lexer;
    private ThemeManager themeManager;
    private SyntaxChecker syntaxChecker;

    public MainCompiler() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Handle exception silently
        }
        setTitle("Mini Java Compiler");
        setSize(850, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        codeArea = new JTextArea();
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        lexer = new Lexer();
        themeManager = new ThemeManager();
        syntaxChecker = new SyntaxChecker();

        JScrollPane codeScroll = new JScrollPane(codeArea);
        JScrollPane outputScroll = new JScrollPane(outputArea);

        JButton openBtn = new JButton("Open");
        JButton scanBtn = new JButton("Scan");
        JButton tokensBtn = new JButton("Tokens");
        JButton symbolsBtn = new JButton("Symbols");
        JButton tacBtn = new JButton("3-Address Code");
        JButton themeBtn = new JButton("Toggle Theme");
        JButton notesBtn = new JButton("Notes");
        JButton syntaxBtn = new JButton("Check Syntax");

        JPanel topPanel = new JPanel();
        topPanel.add(openBtn);
        topPanel.add(scanBtn);
        topPanel.add(tokensBtn);
        topPanel.add(symbolsBtn);
        topPanel.add(tacBtn);
        topPanel.add(themeBtn);
        topPanel.add(notesBtn);
        topPanel.add(syntaxBtn);

        add(topPanel, BorderLayout.NORTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, codeScroll, outputScroll);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.7);
        add(splitPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu helpMenu = new JMenu("Help");
        JMenuItem resourcesItem = new JMenuItem("Online Resources");
        helpMenu.add(resourcesItem);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        openBtn.addActionListener(e -> openFile());
        scanBtn.addActionListener(e -> scanCode());
        tokensBtn.addActionListener(e -> new TokenTableFrame(lexer.getTokens()).setVisible(true));
        symbolsBtn.addActionListener(e -> new SymbolTableFrame(lexer.getSymbolTable()).setVisible(true));
        tacBtn.addActionListener(e -> showIntermediateCode());
        themeBtn.addActionListener(e -> themeManager.toggleTheme(codeArea, outputArea));
        notesBtn.addActionListener(e -> new NotesFrame().setVisible(true));
        syntaxBtn.addActionListener(e -> checkSyntax());
        resourcesItem.addActionListener(e -> showResourcesDialog());

        themeManager.applyTheme(codeArea, outputArea);
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader br = new BufferedReader(new FileReader(chooser.getSelectedFile()))) {
                codeArea.read(br, null);
            } catch (IOException e) {
                showError("Could not open file.");
            }
        }
    }

    private void scanCode() {
        String code = codeArea.getText().trim();
        if (code.isEmpty()) {
            showOutput("No code to scan.");
            return;
        }
        lexer.tokenize(code);
        StringBuilder sb = new StringBuilder();
        sb.append("Scan completed.\n")
          .append("Tokens: ").append(lexer.getTokens().size()).append("\n")
          .append("Symbols: ").append(lexer.getSymbolTable().size()).append("\n\n");
        showOutput(sb.toString());
    }

    private void showIntermediateCode() {
        TACGenerator tacGen = new TACGenerator();
        String tac = tacGen.generate(lexer.getTokens());
        String[] lines = tac.split("\n");
        StringBuilder sb = new StringBuilder("🔹 3-Address Code:\n");
        for (int i = 0; i < lines.length; i++) {
            sb.append(String.format("%2d. %s\n", i + 1, lines[i]));
        }
        outputArea.setText(sb.toString());
    }

    private void checkSyntax() {
        if (syntaxChecker.checkSyntax(lexer.getTokens())) {
            showOutput("Syntax is valid.");
        } else {
            showOutput(syntaxChecker.getErrors());
        }
    }

    private void showResourcesDialog() {
        JDialog dialog = new JDialog(this, "Online Resources", true);
        dialog.setSize(400, 300);
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        editorPane.setText("<html><body><h2>Online Resources</h2>" +
                          "<ul><li><a href='https://www.geeksforgeeks.org/java/'>Java Tutorials</a></li>" +
                          "<li><a href='https://www.geeksforgeeks.org/introduction-of-compiler-design/'>Compiler Design</a></li>" +
                          "<li><a href='https://www.programiz.com/java-programming'>Programiz Java</a></li></ul></body></html>");
        editorPane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED && Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (Exception ex) {
                    showError("Could not open webpage.");
                }
            }
        });
        dialog.add(new JScrollPane(editorPane));
        dialog.setVisible(true);
    }

    private void showOutput(String message) {
        outputArea.setText(message);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainCompiler().setVisible(true));
    }
}
