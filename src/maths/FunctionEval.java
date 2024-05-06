package maths;


public class FunctionEval {
    public static final byte SUCCESS = 1, ERROR = -1, UNDEFINED = 0, IMAGINARY = 2;

    final private byte success;
    final private double result;

    FunctionEval(byte success, double result) {
        this.success = success;
        this.result = result;
    }

    FunctionEval(byte success) {
        this.success = success;
        this.result = 0.0;
    }

    public byte getSuccess() {
        return success;
    }

    public double getResult() {
        return result;
    }
}
