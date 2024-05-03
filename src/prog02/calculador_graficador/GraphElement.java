package prog02.calculador_graficador;

import java.awt.Color;
import javax.swing.JTextField;
import maths.Function;

public class GraphElement {

    private Color color;
    private JTextField field;
    private Function function;

    GraphElement(JTextField field, Color color) {
        this.color = color;
        this.field = field;
        this.function = new Function(field.getText());
    }
    
    public void setColor(Color color) {
        this.color = color;
    }

    public void setField(JTextField field) {
        this.field = field;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public Color getColor() {
        return color;
    }

    public JTextField getField() {
        return field;
    }

    public Function getFunction() {
        return function;
    }
    
    
}