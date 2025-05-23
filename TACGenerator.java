import java.util.*;

public class TACGenerator {
    private int labelCount = 1;
    private int tempCount = 1;

    private String newLabel() {
        return "L" + labelCount++;
    }

    private String newTemp() {
        return "t" + tempCount++;
    }

    public String generate(List<Token> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            return "No tokens provided.";
        }

        StringBuilder code = new StringBuilder();
        int i = 0;

        while (i < tokens.size()) {
            Token token = tokens.get(i);
            String type = token.getType();
            String value = token.getValue();

            if (type.equals("KEYWORD")) {
                if (value.equals("for")) {
                    i = handleForLoop(tokens, i, code);
                } else if (value.equals("if")) {
                    i = handleIfStatement(tokens, i, code);
                } else if (value.equals("while")) {
                    i = handleWhileLoop(tokens, i, code);
                } else if (value.equals("switch")) {
                    i = handleSwitchStatement(tokens, i, code);
                } else {
                    i++;
                }
            } else if (type.equals("OPERATOR") && value.equals("=")) {
                i = handleAssignment(tokens, i, code);
            } else {
                i++;
            }
        }

        return code.length() == 0 ? "No valid expressions found." : code.toString();
    }

    private int handleAssignment(List<Token> tokens, int i, StringBuilder code) {
        if (i - 1 < 0 || i + 1 >= tokens.size() || !tokens.get(i - 1).getType().equals("IDENTIFIER")) {
            return i + 1;
        }
        String left = tokens.get(i - 1).getValue();
        if (i + 3 < tokens.size() && tokens.get(i + 2).getType().equals("OPERATOR") && "+-*/".contains(tokens.get(i + 2).getValue())) {
            String op1 = tokens.get(i + 1).getValue();
            String op = tokens.get(i + 2).getValue();
            String op2 = tokens.get(i + 3).getValue();
            String temp = newTemp();
            code.append(temp).append(" = ").append(op1).append(" ").append(op).append(" ").append(op2).append("\n");
            code.append(left).append(" = ").append(temp).append("\n");
            return i + 4;
        } else if (tokens.get(i + 1).getType().matches("IDENTIFIER|NUMBER")) {
            String right = tokens.get(i + 1).getValue();
            code.append(left).append(" = ").append(right).append("\n");
            return i + 2;
        }
        return i + 1;
    }

    private int handleForLoop(List<Token> tokens, int i, StringBuilder code) {
        i += 2; // Skip 'for' and '('
        StringBuilder init = new StringBuilder();
        while (i < tokens.size() && !tokens.get(i).getValue().equals(";")) {
            init.append(tokens.get(i).getValue()).append(" ");
            i++;
        }
        i++; // Skip ';'
        StringBuilder cond = new StringBuilder();
        while (i < tokens.size() && !tokens.get(i).getValue().equals(";")) {
            cond.append(tokens.get(i).getValue()).append(" ");
            i++;
        }
        i++; // Skip ';'
        StringBuilder incr = new StringBuilder();
        while (i < tokens.size() && !tokens.get(i).getValue().equals(")")) {
            incr.append(tokens.get(i).getValue()).append(" ");
            i++;
        }
        i++; // Skip ')'
        String labelLoop = newLabel();
        String labelEnd = newLabel();
        if (init.length() > 0) {
            code.append(init.toString().trim()).append("\n");
        }
        code.append(labelLoop).append(":\n");
        if (cond.length() > 0) {
            code.append("ifFalse ").append(cond.toString().trim()).append(" goto ").append(labelEnd).append("\n");
        }
        if (i < tokens.size() && tokens.get(i).getValue().equals("{")) {
            i++;
            while (i < tokens.size() && !tokens.get(i).getValue().equals("}")) {
                i = processStatement(tokens, i, code);
            }
            i++;
        }
        if (incr.length() > 0) {
            code.append(incr.toString().trim()).append("\n");
        }
        code.append("goto ").append(labelLoop).append("\n").append(labelEnd).append(":\n");
        return i;
    }

    private int handleIfStatement(List<Token> tokens, int i, StringBuilder code) {
        i += 2; // Skip 'if' and '('
        StringBuilder cond = new StringBuilder();
        while (i < tokens.size() && !tokens.get(i).getValue().equals(")")) {
            cond.append(tokens.get(i).getValue()).append(" ");
            i++;
        }
        i++; // Skip ')'
        String labelEnd = newLabel();
        String labelElse = newLabel();
        code.append("ifFalse ").append(cond.toString().trim()).append(" goto ").append(labelElse).append("\n");
        if (i < tokens.size() && tokens.get(i).getValue().equals("{")) {
            i++;
            while (i < tokens.size() && !tokens.get(i).getValue().equals("}")) {
                i = processStatement(tokens, i, code);
            }
            i++;
        }
        code.append("goto ").append(labelEnd).append("\n").append(labelElse).append(":\n");
        if (i < tokens.size() && tokens.get(i).getType().equals("KEYWORD") && tokens.get(i).getValue().equals("else")) {
            i++;
            if (i < tokens.size() && tokens.get(i).getValue().equals("{")) {
                i++;
                while (i < tokens.size() && !tokens.get(i).getValue().equals("}")) {
                    i = processStatement(tokens, i, code);
                }
                i++;
            }
        }
        code.append(labelEnd).append(":\n");
        return i;
    }

    private int handleWhileLoop(List<Token> tokens, int i, StringBuilder code) {
        i += 2; // Skip 'while' and '('
        StringBuilder cond = new StringBuilder();
        while (i < tokens.size() && !tokens.get(i).getValue().equals(")")) {
            cond.append(tokens.get(i).getValue()).append(" ");
            i++;
        }
        i++; // Skip ')'
        String labelLoop = newLabel();
        String labelEnd = newLabel();
        code.append(labelLoop).append(":\n");
        code.append("ifFalse ").append(cond.toString().trim()).append(" goto ").append(labelEnd).append("\n");
        if (i < tokens.size() && tokens.get(i).getValue().equals("{")) {
            i++;
            while (i < tokens.size() && !tokens.get(i).getValue().equals("}")) {
                i = processStatement(tokens, i, code);
            }
            i++;
        }
        code.append("goto ").append(labelLoop).append("\n").append(labelEnd).append(":\n");
        return i;
    }

    private int handleSwitchStatement(List<Token> tokens, int i, StringBuilder code) {
        i += 2; // Skip 'switch' and '('
        StringBuilder expr = new StringBuilder();
        while (i < tokens.size() && !tokens.get(i).getValue().equals(")")) {
            expr.append(tokens.get(i).getValue()).append(" ");
            i++;
        }
        i++; // Skip ')'
        String switchVar = expr.toString().trim();
        String labelEnd = newLabel();
        Map<String, String> caseLabels = new LinkedHashMap<>();
        String defaultLabel = null;
        if (i < tokens.size() && tokens.get(i).getValue().equals("{")) {
            i++;
            while (i < tokens.size() && !tokens.get(i).getValue().equals("}")) {
                Token token = tokens.get(i);
                if (token.getType().equals("KEYWORD") && token.getValue().equals("case")) {
                    i++;
                    String caseValue = tokens.get(i).getValue();
                    i += 2; // Skip case value and ':'
                    String caseLabel = newLabel();
                    caseLabels.put(caseValue, caseLabel);
                    code.append(caseLabel).append(":\n");
                    while (i < tokens.size() && !tokens.get(i).getValue().matches("case|default|}") && !tokens.get(i).getType().equals("KEYWORD")) {
                        i = processStatement(tokens, i, code);
                    }
                } else if (token.getType().equals("KEYWORD") && token.getValue().equals("default")) {
                    i += 2; // Skip 'default' and ':'
                    defaultLabel = newLabel();
                    code.append(defaultLabel).append(":\n");
                    while (i < tokens.size() && !tokens.get(i).getValue().matches("case|default|}") && !tokens.get(i).getType().equals("KEYWORD")) {
                        i = processStatement(tokens, i, code);
                    }
                } else {
                    i++;
                }
            }
            i++;
        }
        for (Map.Entry<String, String> entry : caseLabels.entrySet()) {
            code.insert(0, "if " + switchVar + " == " + entry.getKey() + " goto " + entry.getValue() + "\n");
        }
        if (defaultLabel != null) {
            code.insert(0, "goto " + defaultLabel + "\n");
        }
        code.append(labelEnd).append(":\n");
        return i;
    }

    private int processStatement(List<Token> tokens, int i, StringBuilder code) {
        Token token = tokens.get(i);
        String type = token.getType();
        String value = token.getValue();

        if (type.equals("KEYWORD") && value.matches("int|float|double|char|boolean")) {
            i++;
            if (i < tokens.size() && tokens.get(i).getType().equals("IDENTIFIER")) {
                String varName = tokens.get(i).getValue();
                i++;
                if (i < tokens.size() && tokens.get(i).getValue().equals("=")) {
                    i++;
                    if (i + 2 < tokens.size() && tokens.get(i + 1).getType().equals("OPERATOR") && "+-*/".contains(tokens.get(i + 1).getValue())) {
                        String op1 = tokens.get(i).getValue();
                        String op = tokens.get(i + 1).getValue();
                        String op2 = tokens.get(i + 2).getValue();
                        String temp = newTemp();
                        code.append(temp).append(" = ").append(op1).append(" ").append(op).append(" ").append(op2).append("\n");
                        code.append(varName).append(" = ").append(temp).append("\n");
                        i += 3;
                    } else if (i < tokens.size() && tokens.get(i).getType().matches("IDENTIFIER|NUMBER")) {
                        code.append(varName).append(" = ").append(tokens.get(i).getValue()).append("\n");
                        i++;
                    } else {
                        i++;
                    }
                } else {
                    i++;
                }
            }
        } else if (type.equals("IDENTIFIER") && i + 1 < tokens.size() && tokens.get(i + 1).getValue().equals("=")) {
            return handleAssignment(tokens, i + 1, code);
        } else if (type.equals("KEYWORD")) {
            if (value.equals("if")) {
                return handleIfStatement(tokens, i, code);
            } else if (value.equals("while")) {
                return handleWhileLoop(tokens, i, code);
            } else if (value.equals("for")) {
                return handleForLoop(tokens, i, code);
            } else if (value.equals("switch")) {
                return handleSwitchStatement(tokens, i, code);
            }
        }
        return i + 1;
    }
}