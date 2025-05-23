import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private List<Token> tokens = new ArrayList<>();
    private List<SymbolEntry> symbolTable = new ArrayList<>();
    private List<String> threeAddressCode = new ArrayList<>();
    private int tempVarCount = 1;

    public void tokenize(String code) {
        tokens.clear();
        symbolTable.clear();
        threeAddressCode.clear();

        // Normalize input: replace multiple spaces, handle newlines
        code = code.replaceAll("\\s+", " ").trim();
        if (code.isEmpty()) return;

        // Define token patterns
        String keywordPattern = "\\b(int|float|double|char|boolean|if|while|for)\\b";
        String identifierPattern = "\\b[a-zA-Z_][a-zA-Z0-9_]*\\b";
        String numberPattern = "\\b[0-9]+(\\.[0-9]+)?\\b";
        String operatorPattern = "[+\\-*/=><!]=?|[><]";
        String symbolPattern = "[;(){}]";
        String stringLiteralPattern = "\"[^\"]*\"";

        String combinedPattern = String.join("|", 
            keywordPattern, identifierPattern, numberPattern, 
            operatorPattern, symbolPattern, stringLiteralPattern);
        Pattern pattern = Pattern.compile(combinedPattern);
        Matcher matcher = pattern.matcher(code);

        String currentType = null;
        List<String> expressionTokens = new ArrayList<>();
        boolean inExpression = false;

        while (matcher.find()) {
            String token = matcher.group();

            // Handle keywords
            if (token.matches(keywordPattern)) {
                tokens.add(new Token("KEYWORD", token));
                if (!token.equals("if") && !token.equals("while") && !token.equals("for")) {
                    currentType = token; // Set type for variable declarations
                }
            }
            // Handle identifiers
            else if (token.matches(identifierPattern)) {
                tokens.add(new Token("IDENTIFIER", token));
                expressionTokens.add(token);

                // Check for variable declaration
                if (currentType != null) {
                    symbolTable.add(new SymbolEntry(token, currentType));
                    currentType = null;
                }
            }
            // Handle numbers
            else if (token.matches(numberPattern)) {
                tokens.add(new Token("NUMBER", token));
                expressionTokens.add(token);
            }
            // Handle operators
            else if (token.matches(operatorPattern)) {
                tokens.add(new Token("OPERATOR", token));
                if (token.equals("=")) {
                    inExpression = true;
                } else if (inExpression) {
                    expressionTokens.add(token);
                }
            }
            // Handle symbols
            else if (token.matches(symbolPattern)) {
                tokens.add(new Token("SYMBOL", token));
                if (token.equals(";") && inExpression) {
                    generateTAC(expressionTokens);
                    expressionTokens.clear();
                    inExpression = false;
                }
            }
            // Handle string literals
            else if (token.matches(stringLiteralPattern)) {
                tokens.add(new Token("STRING_LITERAL", token));
            }
            // Unknown tokens
            else {
                tokens.add(new Token("UNKNOWN", token));
            }
        }

        // Handle any remaining expression
        if (!expressionTokens.isEmpty() && inExpression) {
            generateTAC(expressionTokens);
        }
    }

    private void generateTAC(List<String> expressionTokens) {
        if (expressionTokens.size() < 3) return; // Need at least: var = value

        String left = expressionTokens.get(0); // Left-hand side of assignment
        if (!expressionTokens.get(1).equals("=")) return; // Must be assignment

        // Simple assignment: var = value
        if (expressionTokens.size() == 3) {
            threeAddressCode.add(left + " = " + expressionTokens.get(2));
            return;
        }

        // Handle arithmetic expression: var = op1 op op2
        if (expressionTokens.size() >= 5 && expressionTokens.get(3).matches("[+\\-*/]")) {
            String op1 = expressionTokens.get(2);
            String operator = expressionTokens.get(3);
            String op2 = expressionTokens.get(4);
            String temp = "t" + tempVarCount++;
            threeAddressCode.add(temp + " = " + op1 + " " + operator + " " + op2);
            threeAddressCode.add(left + " = " + temp);
        }
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public List<SymbolEntry> getSymbolTable() {
        return symbolTable;
    }

    public List<String> getThreeAddressCode() {
        return threeAddressCode;
    }
}