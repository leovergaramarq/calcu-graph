package math;

class OperatorHierarchy {

    static final byte PLUS = 0, MINUS = 0,
            TIMES = 1, DIVIDE = 1,
            POW = 2, ROOT = 2,
            PARENTHESIS_OPEN = 3, PARENTHESIS_CLOSE = 3,
            UNDEFINED = -1;
}

class OperatorToken {

    static final String PLUS = "+", MINUS = "-",
            TIMES = "*", DIVIDE = "/",
            POW = "^", ROOT = "",
            PARENTHESIS_OPEN = "(", PARENTHESIS_CLOSE = ")",
            POINT = ".";
}

public class Operator implements Comparable {

    private byte hierarchy;
    private String token;

    Operator(String token) {
        this.token = token;

        switch (token) {
            case OperatorToken.PLUS:
                hierarchy = OperatorHierarchy.PLUS;
                break;
            case OperatorToken.MINUS:
                hierarchy = OperatorHierarchy.MINUS;
                break;
            case OperatorToken.TIMES:
                hierarchy = OperatorHierarchy.TIMES;
                break;
            case OperatorToken.DIVIDE:
                hierarchy = OperatorHierarchy.DIVIDE;
                break;
            case OperatorToken.POW:
                hierarchy = OperatorHierarchy.POW;
                break;
            case OperatorToken.PARENTHESIS_OPEN:
                hierarchy = OperatorHierarchy.PARENTHESIS_OPEN;
                break;
            case OperatorToken.PARENTHESIS_CLOSE:
                hierarchy = OperatorHierarchy.PARENTHESIS_CLOSE;
                break;
            default:
                hierarchy = OperatorHierarchy.UNDEFINED;
        }
    }

    public int compareTo(Object o) {
        if (o instanceof Operator) {
            Operator op = (Operator) o;

            return this.hierarchy > op.hierarchy ? 1 : this.hierarchy < op.hierarchy ? -1 : 0;
        }

        return 0;
    }
}
