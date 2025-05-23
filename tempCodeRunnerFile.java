import java.awt.*;
import java.io.*;
import javax.swing.*;

public class MainCompiler extends JFrame {
    private JTextArea codeArea, outputArea;
    private Lexer lexer;
    private ThemeManager themeManager;

    public MainCompiler() {
        setTitle("Mini Java Compiler");
        setSize(850, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        codeArea = new JTextArea();
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        lexer = new Lexer();
        themeManager = new ThemeManager();

        JScrollPane codeScroll = new JScrollPane(codeArea);
        JScrollPane outputScroll = new JScrollPane(outputArea);

        JButton openBtn = new JButton("Open");
        JButton scanBtn = new JButton("Scan");
        JButton tokensBtn = new JButton("Tokens");
        JButton symbolsBtn = new JButton("Symbols");
        JButton tacBtn = new JButton("3-Address Code");
        JButton themeBtn = new JButton("Toggle Theme");

        JPanel topPanel = new JPanel();
        topPanel.add(openBtn);
        topPanel.add(scanBtn);
        topPanel.add(tokensBtn);
        topPanel.add(symbolsBtn);
        topPanel.add(tacBtn);
        topPanel.add(themeBtn);

        add(topPanel, BorderLayout.NORTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, codeScroll, outputScroll);
splitPane.setDividerLocation(350); 
splitPane.setResizeWeight(0.7);    
add(splitPane, BorderLayout.CENTER);


        openBtn.addActionListener(e -> openFile());
        scanBtn.addActionListener(e -> scanCode());
        tokensBtn.addActionListener(e -> new TokenTableFrame(lexer.getTokens()).setVisible(true));
        symbolsBtn.addActionListener(e -> new SymbolTableFrame(lexer.getSymbolTable()).setVisible(true));
        tacBtn.addActionListener(e -> showIntermediateCode());
        themeBtn.addActionListener(e -> themeManager.toggleTheme(codeArea, outputArea));

        themeManager.applyLightTheme(codeArea, outputArea);
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
        outputArea.setText("🔹 3-Address Code:\n" + tac);
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
