package maths;

import data_structures.MyStack;

public final class Function {

    public final static String X_VAR = "x";

    private String expression;
    private String postfix;
    private boolean valid;

    public Function(String expression) {
        this.setExpression(expression);
    }

    private static String parseExpression(String expression) {
        return expression.trim().replaceAll("\\s{2,}", " ");
    }

    @SuppressWarnings("empty-statement")
    private static String calcPostfix(String expression) {
        //2^(6/3*5)

        int n = expression.length();
        MyStack<String> wait = new MyStack(n);
        MyStack<String> stack = new MyStack(n);
        int i = 0;

        while (i < n) {
            String s = expression.substring(i, i + 1);

            if (s.equals(X_VAR)) {
                stack.push(s);
            } else if (isDigit(s) || s.equals(OperatorToken.POINT)) {
                int j = i - 1;
                String s2 = "";

                while (++j < n
                        && (isDigit(s2 = expression.substring(j, j + 1)))
                        || (s2.equals(OperatorToken.POINT)));

                stack.push(expression.substring(i, j));
                i = j - 1;
            } else if (!s.equals(" ")) {
                String s2;

                if (s.equals(OperatorToken.PARENTHESIS_CLOSE)) {
                    while ((s2 = wait.pop()) != null && !s2.equals(OperatorToken.PARENTHESIS_OPEN)) {
                        stack.push(s2);
                    }
                } else {
                    Operator o1 = new Operator(s);

                    while ((s2 = wait.peek()) != null
                            && !s2.equals(OperatorToken.PARENTHESIS_OPEN) && (new Operator(s2)).compareTo(o1) >= 0) {
                        stack.push(wait.pop());
                    }

                    wait.push(s);
                }
            }
            i++;
        }

        String s;
        while ((s = wait.pop()) != null) {
            stack.push(s);
        }
//        TODO: save postfix stack

        return stack.toString();
    }

    @SuppressWarnings("empty-statement")
    public FunctionEval eval(double x) {
        int i = 0;
        int n = this.postfix.length();
        MyStack<Double> stack = new MyStack(n);
        byte success = FunctionEval.SUCCESS;

        try {
            while (i < n) {
                String s = this.postfix.substring(i, i + 1);
                if (isDigit(s) || s.equals(OperatorToken.POINT)) {
                    int j = i - 1;
                    String s2;

                    while (++j < n
                            && (isDigit(s2 = this.postfix.substring(j, j + 1))
                            || s2.equals(OperatorToken.POINT)));
                    stack.push(Double.valueOf(this.postfix.substring(i, j)));
                    i = j;
                } else if (s.equals(X_VAR)) {
                    stack.push(x);
                } else if (!s.equals(" ")) {
                    double b = stack.pop();
                    double a = stack.pop();

                    boolean undefined = false;

                    switch (s) {
                        case OperatorToken.PLUS:
                            stack.push(a + b);
                            break;
                        case OperatorToken.MINUS:
                            stack.push(a - b);
                            break;
                        case OperatorToken.TIMES:
                            stack.push(a * b);
                            break;
                        case OperatorToken.DIVIDE:
                            if (b == 0.0) {
                                undefined = true;
                            } else {
                                stack.push((double) (a / b));
                            }
                            break;
                        case OperatorToken.POW:
                            stack.push(Math.pow(a, b));
                            break;
                    }

                    if (undefined) {
                        success = FunctionEval.UNDEFINED;
                        break;
                    }
                }
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            success = FunctionEval.ERROR;
        }

        return success == FunctionEval.SUCCESS ? new FunctionEval(success, stack.pop()) : new FunctionEval(success);
    }

    private static boolean isDigit(String s) {
        char c = s.charAt(0);
        return c >= 48 && c <= 57;
    }

    private static boolean validatePostfixTokenCount(String postfix) {
        String[] tokens = postfix.split(" ");
        String[] operators = {OperatorToken.PLUS, OperatorToken.MINUS, OperatorToken.TIMES, OperatorToken.DIVIDE, OperatorToken.POW};
        int countNums = 0;
        int countOperators = 0;

        for (String token : tokens) {
            if (token.equals(X_VAR) || token.matches("^\\d+(\\.\\d+)?$")) {
                countNums++;
            } else {
                for (String op : operators) {
                    if (token.equals(op)) {
                        countOperators++;
                        break;
                    }
                }
            }
        }
        
        return countNums == countOperators + 1;
    }

    private static boolean validatePostfixPattern(String postfix) {
        return postfix.matches("^\\s*(\\s*(x|(\\d+(\\.\\d+)?)|[-+*/^])\\s)+\\s*$");
    }

    private static boolean validatePostfix(String postfix) {
        postfix = postfix + " ";
        return validatePostfixPattern(postfix) && validatePostfixTokenCount(postfix);
    }

    public String getExpression() {
        return this.expression;
    }

    public String getPostfix() {
        return this.postfix;
    }

    public void setExpression(String expression) {
        this.expression = parseExpression(expression);
        this.postfix = calcPostfix(this.expression);
        this.valid = validatePostfix(this.postfix);
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    public boolean isValid() {
        return valid;
    }
}
