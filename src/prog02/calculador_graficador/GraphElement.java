package prog02.calculador_graficador;

import java.awt.Color;
import maths.Function;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JPanel;

public class GraphElement {

    private Color color;
    private JTextField textField;
    private Function function;
    private JScrollPane scrollPane;
    private JPanel panel;

    GraphElement(JTextField textField, Color color, JScrollPane scrollPane, JPanel panel) {
        this.color = color;
        this.textField = textField;
        this.function = new Function(textField.getText());
        this.scrollPane = scrollPane;
        this.panel = panel;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }

    public void setTextField(JTextField textField) {
        this.textField = textField;
        this.function = new Function(this.textField.getText());
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public Color getColor() {
        return color;
    }

    public JTextField getTextField() {
        return textField;
    }

    public Function getFunction() {
        return function;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JPanel getPanel() {
        return panel;
    }
}