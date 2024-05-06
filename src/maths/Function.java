package maths;

import data_structures.MyStack;

public final class Function {

    public final static String X_VAR = "x";

    private String expression;
    private String postfix;
    private MyStack<String> postfixStack;
    private boolean valid;

    public Function(String expression) {
        this.setExpression(expression);
    }

    private static String parseExpression(String expression) {
        return expression.trim().replaceAll("\\s{2,}", " ");
    }

    @SuppressWarnings("empty-statement")
    private void calcPostfix(String expression) {
        int n = expression.length();
        MyStack<String> wait = new MyStack(n);
        this.postfixStack = new MyStack(n);
        int i = 0;

        while (i < n) {
            String s = expression.substring(i, i + 1);

            if (s.equals(X_VAR)) {
                this.postfixStack.push(s);
            } else if (isDigit(s) || s.equals(OperatorToken.POINT)) {
                int j = i - 1;
                String s2 = "";

                while (++j < n
                        && (isDigit(s2 = expression.substring(j, j + 1)))
                        || (s2.equals(OperatorToken.POINT)));

                this.postfixStack.push(expression.substring(i, j));
                i = j - 1;
            } else if (!s.equals(" ")) {
                String s2;

                if (s.equals(OperatorToken.PARENTHESIS_CLOSE)) {
                    while ((s2 = wait.pop()) != null && !s2.equals(OperatorToken.PARENTHESIS_OPEN)) {
                        this.postfixStack.push(s2);
                    }
                } else {
                    Operator o1 = new Operator(s);

                    while ((s2 = wait.peek()) != null
                            && !s2.equals(OperatorToken.PARENTHESIS_OPEN) && (new Operator(s2)).compareTo(o1) >= 0) {
                        this.postfixStack.push(wait.pop());
                    }

                    wait.push(s);
                }
            }
            i++;
        }

        String s;
        while ((s = wait.pop()) != null) {
            this.postfixStack.push(s);
        }

        this.postfix = postfixStack.toString();
    }

    @SuppressWarnings("empty-statement")
    public FunctionEval eval(double x) {
        int n = this.postfixStack.getPeek() + 1;
        MyStack<Double> stack = new MyStack(n);
        byte success = FunctionEval.SUCCESS;

        try {
            for (int i = 0; i < n; i++) {
                String s = this.postfixStack.get(i);
                if (isNumber(s)) {
                    stack.push(Double.valueOf(s));
                } else if (s.equals(X_VAR)) {
                    stack.push(x);
                } else if (!s.equals(" ")) {
                    double b = stack.pop();
                    double a = stack.pop();

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
                                success = FunctionEval.UNDEFINED;
                            } else {
                                stack.push((double) (a / b));
                            }
                            break;
                        case OperatorToken.POW:
                            double result;
                            if (a < 0 && b > 0 && b < 1) {
                                result = Math.pow(Math.abs(a), b);
                                if (Math.abs(1.0 / b) % 2 == 1) {
                                    result = -result;
                                } else {
                                    success = FunctionEval.IMAGINARY;
                                }
                            } else {
                                result = Math.pow(a, b);
                            }

                            if (success == FunctionEval.SUCCESS) {
                                stack.push(result);
                            }
                            break;
                        default:
                            this.valid = false;
                            success = FunctionEval.ERROR;
                    }

                    if (success != FunctionEval.SUCCESS) {
                        break;
                    }
                }
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
            if (token.equals(X_VAR) || isNumber(token)) {
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

    private static boolean isNumber(String number) {
        return number.matches("^\\d+(\\.\\d+)?$");
    }

    public String getExpression() {
        return this.expression;
    }

    public String getPostfix() {
        return this.postfix;
    }

    public void setExpression(String expression) {
        this.expression = parseExpression(expression);
        calcPostfix(this.expression);
        this.valid = validatePostfix(this.postfix);
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    public boolean isValid() {
        return valid;
    }
}
