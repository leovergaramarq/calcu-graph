package prog02.calculador_graficador;

import math.Function;

public class Prog02Calculador_Graficador {

    public static void main(String[] args) {
//        View v = new View((float) 0.5, 50);
//        v.setVisible(true);
//        v.setLocationRelativeTo(null);
        
        Function f = new Function("6.5 * 2 - 4.5 * x ^2");
        
        System.out.println(f.getPostfix());
        System.out.println(f.eval(2));
    }

}
