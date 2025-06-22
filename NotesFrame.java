import java.awt.*;
import java.io.*;
import java.net.URI;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class NotesFrame extends JFrame {
    private JEditorPane notesArea;

    public NotesFrame() {
        setTitle("Compiler Design - Detailed Notes by [Your Name]");
        setSize(800, 800); // Increased size for extensive content
        notesArea = new JEditorPane();
        notesArea.setContentType("text/html");
        notesArea.setEditable(true);
        notesArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        // Highly detailed compiler notes structured like a book
        String notesContent = "<html><body style='font-family: Monospaced; font-size: 14px; padding: 10px;'>" +
            "<h1>Compiler Design - Detailed Notes</h1>" +
            "<h2>By [Your Name]</h2>" +
            
            "<h2>Table of Contents</h2>" +
            "<ol>" +
            "<li><b>Introduction to Compilers</b>" +
            "<ul><li>Definition and Need for Compilers</li>" +
            "<li>Phases of Compilation</li>" +
            "<li>Compiler vs Interpreter</li>" +
            "<li>Structure of a Compiler</li></ul></li>" +
            "<li><b>Lexical Analysis (Scanner)</b>" +
            "<ul><li>Role of Lexical Analyzer</li>" +
            "<li>Tokens, Patterns, Lexemes</li>" +
            "<li>Regular Expressions</li>" +
            "<li>Finite Automata (DFA & NFA)</li>" +
            "<li>Lex Tool</li></ul></li>" +
            "<li><b>Syntax Analysis (Parser)</b>" +
            "<ul><li>Role of Parser</li>" +
            "<li>Context-Free Grammars (CFG)</li>" +
            "<li>Parse Trees & Derivations</li>" +
            "<li>Top-Down vs Bottom-Up Parsing</li>" +
            "<li>LL(1), LR(0), SLR(1), LALR(1), CLR Parsers</li>" +
            "<li>YACC Tool</li></ul></li>" +
            "<li><b>Semantic Analysis</b>" +
            "<ul><li>Role of Semantic Analyzer</li>" +
            "<li>Syntax-Directed Translation (SDT)</li>" +
            "<li>Attribute Grammars</li>" +
            "<li>Type Checking</li></ul></li>" +
            "<li><b>Intermediate Code Generation</b>" +
            "<ul><li>Three-Address Code</li>" +
            "<li>Quadruples & Triples</li>" +
            "<li>Postfix Notation</li>" +
            "<li>Syntax Trees & DAGs</li></ul></li>" +
            "<li><b>Code Optimization</b>" +
            "<ul><li>Machine-Independent vs Machine-Dependent Optimization</li>" +
            "<li>Peephole Optimization</li>" +
            "<li>Loop Optimization</li>" +
            "<li>Data Flow Analysis</li></ul></li>" +
            "<li><b>Code Generation</b>" +
            "<ul><li>Target Machine Architecture</li>" +
            "<li>Register Allocation</li>" +
            "<li>Instruction Selection</li></ul></li>" +
            "<li><b>Error Handling & Recovery</b>" +
            "<ul><li>Lexical, Syntactic, Semantic Errors</li>" +
            "<li>Panic Mode, Phrase-Level Recovery</li></ul></li>" +
            "<li><b>Advanced Topics</b>" +
            "<ul><li>Just-In-Time (JIT) Compilation</li>" +
            "<li>Garbage Collection</li>" +
            "<li>Parallel Compilation</li></ul></li>" +
            "</ol>" +
            
            "<h2>1. Introduction to Compilers</h2>" +
            "<h3>Definition and Need for Compilers</h3>" +
            "<p>A <b>compiler</b> is a software tool that translates high-level language (HLL) code (e.g., C, Java) into low-level machine code or assembly while preserving its semantics.</p>" +
            "<ul>" +
            "<li><b>Need</b>: Enhances efficiency (faster execution), portability (compile for multiple architectures), and allows optimization for performance.</li>" +
            "<li><b>Example</b>: GCC compiles C code into x86 machine code.</li>" +
            "</ul>" +
            "<h3>Phases of Compilation</h3>" +
            "<ul>" +
            "<li><b>Front-End</b>: Lexical Analysis, Syntax Analysis, Semantic Analysis.</li>" +
            "<li><b>Middle-End</b>: Intermediate Code Generation, Code Optimization.</li>" +
            "<li><b>Back-End</b>: Code Generation.</li>" +
            "</ul>" +
            "<h3>Compiler vs Interpreter</h3>" +
            "<table border='1'><tr><th>Compiler</th><th>Interpreter</th></tr>" +
            "<tr><td>Translates entire program at once</td><td>Translates and executes line-by-line</td></tr>" +
            "<tr><td>Faster execution post-compilation</td><td>Slower due to runtime translation</td></tr>" +
            "<tr><td>Generates machine code (e.g., GCC)</td><td>Direct execution (e.g., Python)</td></tr></table>" +
            "<h3>Structure of a Compiler</h3>" +
            "<p>Flow: Source Code → [Lexical Analyzer] → [Syntax Analyzer] → [Semantic Analyzer] → [Intermediate Code Generator] → [Optimizer] → [Code Generator] → Target Code</p>" +
            
            "<h2>2. Lexical Analysis (Scanner)</h2>" +
            "<h3>Role of Lexical Analyzer</h3>" +
            "<p>Converts source code into <b>tokens</b>, removing whitespace and comments.</p>" +
            "<h3>Tokens, Patterns, Lexemes</h3>" +
            "<ul>" +
            "<li><b>Token</b>: <code>&lt;token_name, attribute_value&gt;</code> (e.g., <code>&lt;ID, \"x\"&gt;</code>).</li>" +
            "<li><b>Pattern</b>: Rule for token recognition (e.g., <code>[a-zA-Z][a-zA-Z0-9]*</code>).</li>" +
            "<li><b>Lexeme</b>: Matched string (e.g., <code>\"count\"</code>).</li>" +
            "</ul>" +
            "<h3>Regular Expressions (RE)</h3>" +
            "<ul>" +
            "<li>Define token patterns: <code>a|b</code> (a or b), <code>a*</code> (zero or more a’s), <code>[0-9]+</code> (one or more digits).</li>" +
            "<li><b>Example</b>: <code>[a-zA-Z]_*[a-zA-Z0-9]*</code> for identifiers.</li>" +
            "</ul>" +
            "<h3>Finite Automata (DFA & NFA)</h3>" +
            "<ul>" +
            "<li><b>DFA</b>: One transition per input, no ε-moves (e.g., for <code>[0-9]+</code>).</li>" +
            "<li><b>NFA</b>: Multiple transitions, allows ε-moves (e.g., for <code>a|b</code>).</li>" +
            "</ul>" +
            "<h3>Lex Tool</h3>" +
            "<p>Generates lexical analyzers from RE specs (e.g., <code>.l</code> files).</p>" +
            
            "<h2>3. Syntax Analysis (Parser)</h2>" +
            "<h3>Role of Parser</h3>" +
            "<p>Validates token sequence against grammar, builds <b>parse trees</b>.</p>" +
            "<h3>Context-Free Grammars (CFG)</h3>" +
            "<p><code>G = (V, T, P, S)</code>: V (non-terminals), T (terminals), P (productions), S (start symbol).</p>" +
            "<h3>Parse Trees & Derivations</h3>" +
            "<ul>" +
            "<li><b>Derivation</b>: Applying rules (e.g., <code>S → if (E) S</code>).</li>" +
            "<li><b>Leftmost</b>: Expands leftmost non-terminal first.</li>" +
            "<li><b>Rightmost</b>: Expands rightmost first.</li>" +
            "</ul>" +
            "<h3>Top-Down vs Bottom-Up Parsing</h3>" +
            "<table border='1'><tr><th>Top-Down</th><th>Bottom-Up</th></tr>" +
            "<tr><td>Starts from S, LL parsers</td><td>Starts from leaves, LR parsers</td></tr>" +
            "<tr><td>Recursive Descent</td><td>Shift-Reduce</td></tr></table>" +
            "<h3>LL(1), LR(0), SLR(1), LALR(1), CLR Parsers</h3>" +
            "<ul>" +
            "<li><b>LL(1)</b>: Predictive, one-token lookahead.</li>" +
            "<li><b>LR(0)</b>: Basic shift-reduce, no lookahead.</li>" +
            "<li><b>SLR(1)</b>: Simplified LR with one-token lookahead.</li>" +
            "<li><b>LALR(1)</b>: Lookahead LR, efficient for Yacc.</li>" +
            "<li><b>CLR</b>: Canonical LR, handles all LR grammars.</li>" +
            "</ul>" +
            "<h3>YACC Tool</h3>" +
            "<p>Generates LALR(1) parsers from <code>.y</code> files.</p>" +
            
            "<h2>4. Semantic Analysis</h2>" +
            "<h3>Role of Semantic Analyzer</h3>" +
            "<p>Ensures meaning (e.g., type compatibility) using <b>Symbol Tables</b>.</p>" +
            "<h3>Syntax-Directed Translation (SDT)</h3>" +
            "<p>Attaches semantic actions to grammar (e.g., <code>E.val = E1.val + E2.val</code>).</p>" +
            "<h3>Attribute Grammars</h3>" +
            "<p>Extends CFG with synthesized and inherited attributes.</p>" +
            "<h3>Type Checking</h3>" +
            "<p>Verifies operand compatibility (e.g., <code>int + float</code> requires conversion).</p>" +
            
            "<h2>5. Intermediate Code Generation</h2>" +
            "<h3>Three-Address Code (TAC)</h3>" +
            "<p>Format: <code>x = y op z</code>.</p>" +
            "<ul><li>Example: <code>t1 = b * c; t2 = a + t1;</code></li></ul>" +
            "<h3>Quadruples & Triples</h3>" +
            "<ul>" +
            "<li><b>Quadruples</b>: <code>(op, arg1, arg2, result)</code>.</li>" +
            "<li><b>Triples</b>: <code>(op, arg1, arg2)</code> (result implied).</li>" +
            "</ul>" +
            "<h3>Postfix Notation</h3>" +
            "<p>Polish notation: <code>a + b</code> → <code>a b +</code>.</p>" +
            "<h3>Syntax Trees & DAGs</h3>" +
            "<ul>" +
            "<li><b>Syntax Tree</b>: Hierarchical representation.</li>" +
            "<li><b>DAG</b>: Shares common subexpressions (e.g., <code>a + a</code>).</li>" +
            "</ul>" +
            
            "<h2>6. Code Optimization</h2>" +
            "<h3>Machine-Independent vs Machine-Dependent Optimization</h3>" +
            "<ul>" +
            "<li><b>Machine-Independent</b>: Constant folding, dead code elimination.</li>" +
            "<li><b>Machine-Dependent</b>: Register allocation, instruction scheduling.</li>" +
            "</ul>" +
            "<h3>Peephole Optimization</h3>" +
            "<p>Optimizes small code windows (e.g., <code>x = x + 0</code> → <code>x</code>).</p>" +
            "<h3>Loop Optimization</h3>" +
            "<ul>" +
            "<li><b>Unrolling</b>: Expands loop body.</li>" +
            "<li><b>Invariant Motion</b>: Moves loop-invariant code outside.</li>" +
            "</ul>" +
            "<h3>Data Flow Analysis</h3>" +
            "<p>Tracks variable liveness (e.g., <b>live variable analysis</b>).</p>" +
            
            "<h2>7. Code Generation</h2>" +
            "<h3>Target Machine Architecture</h3>" +
            "<p>Includes registers, memory, and instruction sets (e.g., x86, ARM).</p>" +
            "<h3>Register Allocation</h3>" +
            "<p>Uses <b>graph coloring</b> to assign registers without spilling.</p>" +
            "<h3>Instruction Selection</h3>" +
            "<p>Maps intermediate code to target instructions.</p>" +
            
            "<h2>8. Error Handling & Recovery</h2>" +
            "<h3>Lexical, Syntactic, Semantic Errors</h3>" +
            "<ul>" +
            "<li><b>Lexical</b>: Misspelled <code>innt</code> instead of <code>int</code>.</li>" +
            "<li><b>Syntactic</b>: Missing <code>;</code>.</li>" +
            "<li><b>Semantic</b>: <code>x = \"string\"</code> where <code>x</code> is <code>int</code>.</li>" +
            "</ul>" +
            "<h3>Panic Mode, Phrase-Level Recovery</h3>" +
            "<ul>" +
            "<li><b>Panic Mode</b>: Skips tokens until a synchronizing point.</li>" +
            "<li><b>Phrase-Level</b>: Replaces erroneous phrases with valid ones.</li>" +
            "</ul>" +
            
            "<h2>9. Advanced Topics</h2>" +
            "<h3>Just-In-Time (JIT) Compilation</h3>" +
            "<p>Compiles code at runtime (e.g., Java JVM, .NET).</p>" +
            "<h3>Garbage Collection</h3>" +
            "<p>Automates memory management (e.g., mark-and-sweep).</p>" +
            "<h3>Parallel Compilation</h3>" +
            "<p>Uses multi-threading to speed up compilation.</p>" +
            
            "<h2>Conclusion</h2>" +
            "<p>This document covers all fundamental aspects of <b>Compiler Design</b> in detail. Each phase is crucial for transforming high-level code into efficient machine code.</p>" +
            "<p><b>End of Notes.</b> 🚀</p>" +
            
            "<h2>Resources</h2>" +
            "<ul>" +
            "<li><a href='https://www.geeksforgeeks.org/introduction-of-compiler-design/'>GeeksforGeeks: Compiler Design</a></li>" +
            "<li><a href='https://www.antlr.org'>ANTLR Documentation</a></li>" +
            "<li><a href='https://www.geeksforgeeks.org/yacc-program-to-implement-a-calculator/'>Yacc Tutorial</a></li>" +
            "<li><a href='https://www.draw.io'>Draw.io for Diagrams</a></li>" +
            "<li><a href='https://llvm.org'>LLVM Documentation</a></li>" +
            "</ul>" +
            "</body></html>";
        
        notesArea.setText(notesContent);
        add(new JScrollPane(notesArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");
        JButton learnMoreBtn = new JButton("Learn More");
        bottomPanel.add(saveBtn);
        bottomPanel.add(loadBtn);
        bottomPanel.add(learnMoreBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(chooser.getSelectedFile()))) {
                    bw.write(notesArea.getText());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Could not save notes.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loadBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try (BufferedReader br = new BufferedReader(new FileReader(chooser.getSelectedFile()))) {
                    notesArea.read(br, null);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Could not load notes.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        learnMoreBtn.addActionListener(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.geeksforgeeks.org/introduction-of-compiler-design/"));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Could not open webpage.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Enable hyperlink clicking
        notesArea.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED && Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(NotesFrame.this, "Could not open webpage.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
}