import java.util.*;

public class SyntaxChecker {
    private List<Token> tokens;
    private Set<String> declaredVariables;
    private StringBuilder errorMessages;

    public SyntaxChecker() {
        declaredVariables = new HashSet<>();
        errorMessages = new StringBuilder();
    }

    public boolean checkSyntax(List<Token> tokens) {
        this.tokens = tokens;
        declaredVariables.clear();
        errorMessages.setLength(0);

        if (tokens == null || tokens.isEmpty()) {
            errorMessages.append("No tokens to analyze.\n");
            return false;
        }

        // Check overall structure
        boolean isValid = checkBraces() && checkParentheses() && checkControlStructures() && checkVariableUsage();
        return isValid;
    }

    public String getErrors() {
        return errorMessages.toString().isEmpty() ? "No syntax errors found." : errorMessages.toString();
    }

    private boolean checkBraces() {
        int braceCount = 0;
        for (Token token : tokens) {
            String value = token.getValue();
            if (value.equals("{")) braceCount++;
            else if (value.equals("}")) braceCount--;
            if (braceCount < 0) {
                errorMessages.append("Mismatched closing brace '}'.\n");
                return false;
            }
        }
        if (braceCount != 0) {
            errorMessages.append("Unmatched opening brace '{'.\n");
            return false;
        }
        return true;
    }

    private boolean checkParentheses() {
        int parenCount = 0;
        for (Token token : tokens) {
            String value = token.getValue();
            if (value.equals("(")) parenCount++;
            else if (value.equals(")")) parenCount--;
            if (parenCount < 0) {
                errorMessages.append("Mismatched closing parenthesis ')'.\n");
                return false;
            }
        }
        if (parenCount != 0) {
            errorMessages.append("Unmatched opening parenthesis '('.\n");
            return false;
        }
        return true;
    }

    private boolean checkControlStructures() {
        boolean valid = true;
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType().equals("KEYWORD")) {
                String value = token.getValue();
                if (value.equals("if")) {
                    valid &= checkIfStatement(i);
                } else if (value.equals("while")) {
                    valid &= checkWhileStatement(i);
                } else if (value.equals("for")) {
                    valid &= checkForStatement(i);
                } else if (value.equals("switch")) {
                    valid &= checkSwitchStatement(i);
                } else if (value.matches("int|float|double|char|boolean")) {
                    valid &= checkVariableDeclaration(i);
                }
            }
        }
        return valid;
    }

    private boolean checkIfStatement(int index) {
        if (index + 1 >= tokens.size() || !tokens.get(index + 1).getValue().equals("(")) {
            errorMessages.append("Expected '(' after 'if' at token " + index + ".\n");
            return false;
        }
        int parenCount = 1;
        index += 2;
        while (index < tokens.size() && parenCount > 0) {
            String value = tokens.get(index).getValue();
            if (value.equals("(")) parenCount++;
            else if (value.equals(")")) parenCount--;
            index++;
        }
        if (parenCount > 0) {
            errorMessages.append("Unclosed parenthesis in 'if' condition.\n");
            return false;
        }
        if (index >= tokens.size() || !tokens.get(index).getValue().equals("{")) {
            errorMessages.append("Expected '{' after 'if' condition.\n");
            return false;
        }
        return true;
    }

    private boolean checkWhileStatement(int index) {
        if (index + 1 >= tokens.size() || !tokens.get(index + 1).getValue().equals("(")) {
            errorMessages.append("Expected '(' after 'while' at token " + index + ".\n");
            return false;
        }
        int parenCount = 1;
        index += 2;
        while (index < tokens.size() && parenCount > 0) {
            String value = tokens.get(index).getValue();
            if (value.equals("(")) parenCount++;
            else if (value.equals(")")) parenCount--;
            index++;
        }
        if (parenCount > 0) {
            errorMessages.append("Unclosed parenthesis in 'while' condition.\n");
            return false;
        }
        if (index >= tokens.size() || !tokens.get(index).getValue().equals("{")) {
            errorMessages.append("Expected '{' after 'while' condition.\n");
            return false;
        }
        return true;
    }

    private boolean checkForStatement(int index) {
        if (index + 1 >= tokens.size() || !tokens.get(index + 1).getValue().equals("(")) {
            errorMessages.append("Expected '(' after 'for' at token " + index + ".\n");
            return false;
        }
        int parenCount = 1;
        int semicolons = 0;
        index += 2;
        while (index < tokens.size() && parenCount > 0) {
            String value = tokens.get(index).getValue();
            if (value.equals("(")) parenCount++;
            else if (value.equals(")")) parenCount--;
            else if (value.equals(";")) semicolons++;
            index++;
        }
        if (parenCount > 0) {
            errorMessages.append("Unclosed parenthesis in 'for' loop.\n");
            return false;
        }
        if (semicolons != 2) {
            errorMessages.append("Expected exactly two semicolons in 'for' loop declaration.\n");
            return false;
        }
        if (index >= tokens.size() || !tokens.get(index).getValue().equals("{")) {
            errorMessages.append("Expected '{' after 'for' loop.\n");
            return false;
        }
        return true;
    }

    private boolean checkSwitchStatement(int index) {
        if (index + 1 >= tokens.size() || !tokens.get(index + 1).getValue().equals("(")) {
            errorMessages.append("Expected '(' after 'switch' at token " + index + ".\n");
            return false;
        }
        int parenCount = 1;
        index += 2;
        while (index < tokens.size() && parenCount > 0) {
            String value = tokens.get(index).getValue();
            if (value.equals("(")) parenCount++;
            else if (value.equals(")")) parenCount--;
            index++;
        }
        if (parenCount > 0) {
            errorMessages.append("Unclosed parenthesis in 'switch' expression.\n");
            return false;
        }
        if (index >= tokens.size() || !tokens.get(index).getValue().equals("{")) {
            errorMessages.append("Expected '{' after 'switch' expression.\n");
            return false;
        }
        // Check for case/default statements
        boolean hasCase = false;
        while (index < tokens.size() && !tokens.get(index).getValue().equals("}")) {
            if (tokens.get(index).getValue().equals("case") || tokens.get(index).getValue().equals("default")) {
                hasCase = true;
                if (tokens.get(index).getValue().equals("case")) {
                    index++;
                    if (index >= tokens.size() || (!tokens.get(index).getType().equals("NUMBER") && !tokens.get(index).getType().equals("IDENTIFIER"))) {
                        errorMessages.append("Expected constant or identifier after 'case'.\n");
                        return false;
                    }
                    index++;
                    if (index >= tokens.size() || !tokens.get(index).getValue().equals(":")) {
                        errorMessages.append("Expected ':' after 'case' value.\n");
                        return false;
                    }
                }
            }
            index++;
        }
        if (!hasCase) {
            errorMessages.append("'switch' statement must contain at least one 'case' or 'default'.\n");
            return false;
        }
        return true;
    }

    private boolean checkVariableDeclaration(int index) {
        if (index + 1 >= tokens.size() || !tokens.get(index + 1).getType().equals("IDENTIFIER")) {
            errorMessages.append("Expected identifier after type at token " + index + ".\n");
            return false;
        }
        String varName = tokens.get(index + 1).getValue();
        declaredVariables.add(varName);
        return true;
    }

    private boolean checkVariableUsage() {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType().equals("IDENTIFIER")) {
                String varName = token.getValue();
                if (!declaredVariables.contains(varName)) {
                    // Allow identifiers in for loop conditions or switch expressions
                    boolean isInControlStructure = false;
                    for (int j = i - 1; j >= 0; j--) {
                        if (tokens.get(j).getValue().matches("for|switch|if|while")) {
                            isInControlStructure = true;
                            break;
                        }
                        if (tokens.get(j).getValue().equals(";") || tokens.get(j).getValue().equals("{")) {
                            break;
                        }
                    }
                    if (!isInControlStructure) {
                        errorMessages.append("Undeclared variable '" + varName + "' used.\n");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}