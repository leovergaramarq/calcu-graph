package prog02.calculador_graficador;

import java.awt.Color;
import javax.swing.JTextField;
import math.Function;

public class GraphElement {

    Color color;
    JTextField field;
    Function function;

    GraphElement(JTextField field, Color color, String fnExpr) {
        this.color = color;
        this.field = field;
        this.function = new Function(fnExpr);
    }
}