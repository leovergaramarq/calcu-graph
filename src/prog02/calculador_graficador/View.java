package prog02.calculador_graficador;

import maths.Function;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JTextField;
import maths.FunctionEval;

public class View extends javax.swing.JFrame {

    private static final byte INITIAL_SCALE = 1;
    private static final double MIN_SCALE = 0.125;
    private static final byte MAX_SCALE = 10;

    private Point origin;
    private Point pressed;
    private Point released;
    private double scale;
    private final int pixelDistance;
    private final int panelGraphWidth;
    private final int panelGraphHeight;
    private ArrayList<GraphElement> graphElems;

    public View() {
        this(INITIAL_SCALE, 50);
    }

    public View(double scale, int pixelDistance) {
        initComponents();
        this.fieldFunction1.setText("1/x");
        this.scale = getBoundedScale(scale);
        this.pixelDistance = pixelDistance;
        this.panelGraphWidth = (int) panelGraph.getSize().getWidth();
        this.panelGraphHeight = (int) panelGraph.getSize().getHeight();
        this.origin = new Point(panelGraphWidth / 2, panelGraphHeight / 2);

        initPanelGraph();
        initGraphElems();

//        this.panelGraph.setPreferredSize(new Dimension(this.panelWidth, this.panelHeight));
//        setResizable(false);
    }

    private void setScale(boolean increase, boolean add) {
        double newScale;
        if (add) {
            newScale = this.scale + (increase ? 1 : -1);
        } else {
            newScale = this.scale * (increase ? 1.5 : 1 / 1.5);
        }
        
        this.scale = getBoundedScale(newScale);
    }
    
    private double getBoundedScale(double scale) {
        if (scale < MIN_SCALE) {
            return MIN_SCALE;
        }
        if (scale > MAX_SCALE) {
            return MAX_SCALE;
        }
        if (scale == 0) {
            return INITIAL_SCALE;
        }
        
        return scale;
    }

    private void setOrigin(boolean add, boolean repaint) {
        Point p = new Point(this.released.x - this.pressed.x, this.released.y - this.pressed.y);
        this.origin.setLocation(add ? new Point(this.origin.x + p.x, this.origin.y + p.y) : p);
        if (repaint) {
            this.panelGraph.repaint();
        }
        this.released = null;
        this.pressed = null;
    }

    private void initPanelGraph() {
        View that = this;
        this.panelGraph.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                setScale(e.getWheelRotation() < 0, false);
            }
        });
        this.panelGraph.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                that.pressed = e.getPoint();
                //System.out.println("pressed on ("+e.getPoint().x+", "+e.getPoint().y+")");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                that.released = e.getPoint();
                if (that.pressed != that.released) {
                    setOrigin(true, true);
                }
                //System.out.println("released on ("+e.getPoint().x+", "+e.getPoint().y+")");
            }
        });
    }

    private void initGraphElems() {
        GraphElement graphElem = new GraphElement(this.fieldFunction1, Color.GREEN);
        this.graphElems = new ArrayList();
        this.graphElems.add(graphElem);
        JTextField field = graphElem.getField();

        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        graphElem.setFunction(new Function(field.getText()));
//                        updateGraphs();
                }
            }
        });
    }

    private void updatePanelGraph(Graphics g) {
        //final int factorX=100, factorY=100, sizeFont=20;
//        final int sizeFont = 20;
        updateAxis(g);
        updateGraphs(g);
        this.panelGraph.repaint();
    }

    private void updateAxis(Graphics g) {
//        int fontSize = 10 + (int) (10 * Math.sqrt(scale));
        int fontSize = 20;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setFont(new Font("Candara", Font.PLAIN, fontSize));
        g2.setColor(Color.WHITE);

        g.drawLine(this.origin.x, 0, this.origin.x, this.panelGraphHeight);
        g.drawLine(0, this.origin.y, this.panelGraphWidth, this.origin.y);
        //System.out.println("from "+(-origin.x/pixelDistance/scale)+" to "+ ((PANEL_WIDTH-origin.x)/pixelDistance/scale));

        for (double i = -this.origin.x / this.pixelDistance / this.scale; i <= (this.panelGraphWidth - this.origin.x) / this.pixelDistance / this.scale; i += 1 / this.scale) {
            String coord;
            if (i % 1 == 0) {
                coord = String.valueOf((int) i);
            } else {
                coord = String.valueOf(Math.round(i * 100.0) / 100.0);
            }
            g.drawString(coord, (int) (this.origin.x + i * this.pixelDistance * this.scale), this.origin.y + fontSize);
        }

//        for (double i = this.origin.y / this.pixelDistance / this.scale; i >= -(this.panelHeight - this.origin.y) / this.pixelDistance / this.scale; i -= 1 / this.scale) {
//            System.out.print(i + " ");
//        }
//        System.out.println("");
        for (double i = this.origin.y / this.pixelDistance / this.scale; i >= -(this.panelGraphHeight - this.origin.y) / this.pixelDistance / this.scale; i -= 1 / this.scale) {
            if (i != 0.0) {
                String coord;
                if (i % 1 == 0) {
                    coord = String.valueOf((int) i);
                } else {
                    coord = String.valueOf(Math.round(i * 100.0) / 100.0);
                }
                g.drawString(coord, this.origin.x, (int) (this.origin.y - i * this.pixelDistance * this.scale));
            }
        }
    }

    private void updateGraphs(final Graphics g) {
        final int stroke = 2;
        final int step = 10; // 10 drawn points for each unit

        final Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(stroke));

        for (GraphElement graphElem : this.graphElems) {
            g2.setColor(graphElem.getColor());
            Function f = graphElem.getFunction();

            if (f.isValid()) {
//                for (double x = -this.origin.x / this.pixelDistance / this.scale; x <= (this.panelWidth - this.origin.x) / this.pixelDistance / this.scale; x += 1 / this.scale / step) {
//                    System.out.print("x: " + x + ", ");
//                }
//                System.out.println("");

                boolean avoidLine = true;

                int oldX = 0;
                int oldY = 0;

                for (double x = -this.origin.x / this.pixelDistance / this.scale; x <= (this.panelGraphWidth - this.origin.x) / this.pixelDistance / this.scale; x += 1 / this.scale / step) {
                    FunctionEval eval = f.eval(x);
                    if (eval.getSuccess() == FunctionEval.SUCCESS) {
                        double y = eval.getResult();
                        int finalX = (int) (x * this.pixelDistance * this.scale);
                        int finalY = (int) (y * this.pixelDistance * this.scale);

                        if (avoidLine) {
                            avoidLine = false;
                        } else if (Math.abs(finalY - oldY) < this.panelGraphHeight) {
                            g.drawLine(this.origin.x + oldX, this.origin.y - oldY, this.origin.x + finalX, this.origin.y - finalY);
                        }

                        oldX = finalX;
                        oldY = finalY;
                    } else if (eval.getSuccess() == FunctionEval.UNDEFINED) {
                        avoidLine = true;
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        fieldFunction1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnGraph = new javax.swing.JButton();
        panelGraph = new javax.swing.JPanel(){
            @Override
            public void paint(java.awt.Graphics g){
                super.paint(g);
                updatePanelGraph(g);
            }
        };

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(197, 205, 211));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(133, 96, 166));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Nirmala UI Semilight", 1, 36)); // NOI18N
        jLabel1.setText("CalcuGraph");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 0, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 60));

        jPanel3.setBackground(new java.awt.Color(154, 123, 181));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fieldFunction1.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        fieldFunction1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldFunction1ActionPerformed(evt);
            }
        });
        jScrollPane1.setViewportView(fieldFunction1);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 280, 50));

        jLabel2.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        jLabel2.setText("f(x)");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 30, 20));

        btnGraph.setBackground(new java.awt.Color(133, 96, 166));
        btnGraph.setFont(new java.awt.Font("Candara", 1, 14)); // NOI18N
        btnGraph.setText("(0,0)");
        btnGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGraphActionPerformed(evt);
            }
        });
        jPanel3.add(btnGraph, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 300, 450));

        panelGraph.setBackground(new java.awt.Color(40, 40, 40));
        panelGraph.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(panelGraph, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 70, 580, 440));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fieldFunction1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldFunction1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldFunction1ActionPerformed

    private void btnGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraphActionPerformed
        this.scale = INITIAL_SCALE;
        this.origin.setLocation(this.panelGraphWidth / 2, this.panelGraphHeight / 2);
    }//GEN-LAST:event_btnGraphActionPerformed

    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        java.awt.EventQueue.invokeLater(() -> {
            new View().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGraph;
    private javax.swing.JTextField fieldFunction1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelGraph;
    // End of variables declaration//GEN-END:variables
}
