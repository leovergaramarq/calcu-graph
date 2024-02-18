package math;

import data_structures.MyStack;

public class Function {

    public final static String X_VAR = "x";

    private String expression;
    private String postfix;

    public Function(String expression) {
        this.expression = parseExpression(expression);
        this.postfix = calcPostfix();
    }

    private String parseExpression(String expression) {
        return expression.replaceAll(" ", "");
    }

    @SuppressWarnings("empty-statement")
    private String calcPostfix() {
        //2^(6/3*5)

        int n = this.expression.length();
        MyStack<String> wait = new MyStack(n);
        MyStack<String> stack = new MyStack(n);
        int i = 0;

        while (i < n) {
            String s = this.expression.substring(i, i + 1);

            if (s.equals(X_VAR)) {
                stack.push(s);
            } else if (isDigit(s) || s.equals(OperatorToken.POINT)) {
                int j = i - 1;
                String s2 = "";

                // TODO: include dots
                while (++j < n
                        && (isDigit(s2 = expression.substring(j, j + 1)))
                        || (s2.equals(OperatorToken.POINT)));

                stack.push(expression.substring(i, j));
                i = j - 1;
            } else {
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

        return stack.toString();
    }

    @SuppressWarnings("empty-statement")
    public double eval(double x) {
        int i = 0;
        int n = postfix.length();
        MyStack<Double> stack = new MyStack(n);

        while (i < n) {
            String s = postfix.substring(i, i + 1);

            if (!s.equals("\t")) {
                if (isDigit(s) || s.equals(OperatorToken.POINT)) {
                    int j = i - 1;
                    String s2 = "";

                    while (++j < n
                            && (isDigit(s2 = postfix.substring(j, j + 1))
                            || s2.equals(OperatorToken.POINT)));
                    stack.push(Double.valueOf(postfix.substring(i, j)));
                    i = j;
                } else if (s.equals(X_VAR)) {
                    stack.push(x);
                } else {
                    double b = stack.pop(), a = stack.pop();

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
                            stack.push(a / b);
                            break;
                        case OperatorToken.POW:
                            stack.push(Math.pow(a, b));
                            break;
                    }
                }
            }
            i++;
        }

        return stack.pop();
    }

    private static boolean isDigit(String s) {
        char c = s.charAt(0);
        return c >= 48 && c <= 57;
    }

    private static boolean isDigit(char c) {
        return c >= 48 && c <= 57;
    }

    public String getExpression() {
        return expression;
    }

    public String getPostfix() {
        return this.postfix;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

}
