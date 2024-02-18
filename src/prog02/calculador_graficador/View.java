package prog02.calculador_graficador;

import math.Function;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.event.*;

public class View extends javax.swing.JFrame {

    private Point origin, pressed, released;
    private float scale;
    private int pixelDistance;
    private final int PANEL_WIDTH, PANEL_HEIGHT;
    private ArrayList<GraphElement> graphElems;

    public View() {
        this(1, 50);
    }

    public View(float scale, int pixelDistance) {
        initComponents();
        this.scale = scale;
        this.pixelDistance = pixelDistance;
        PANEL_WIDTH = (int) panelGraph.getSize().getWidth();
        PANEL_HEIGHT = (int) panelGraph.getSize().getHeight();
        origin = new Point(PANEL_WIDTH / 2, PANEL_HEIGHT / 2);

        initGraph();
        initPairs();

        panelGraph.setPreferredSize(new java.awt.Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setResizable(false);
    }

    private void setScale(float plus, boolean add, boolean repaint) {
        if (scale * plus != 0) {
            scale *= plus;
        }
        /*
        if(scale+plus<=0) return;
        scale=(add?scale:0)+plus;
        //*/
        System.out.println("scale: " + scale);
        if (repaint) {
            panelGraph.repaint();
        }
    }

    private void setOrigin(Point p, boolean add, boolean repaint) {
        //System.out.println("old:("+origin.x+", "+origin.y+")");
        origin.setLocation(add ? new Point(origin.x + p.x, origin.y + p.y) : p);
        //System.out.println("new:("+origin.x+", "+origin.y+")");
        if (repaint) {
            panelGraph.repaint();
        }
        //System.out.println("");
    }

    private void setOrigin(boolean add, boolean repaint) {
        Point p = new Point(released.x - pressed.x, released.y - pressed.y);
        origin.setLocation(add ? new Point(origin.x + p.x, origin.y + p.y) : p);
        if (repaint) {
            panelGraph.repaint();
        }
        released = pressed = null;
    }

    private void initGraph() {
        panelGraph.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                setScale(e.getWheelRotation() < 0 ? 2 : (float) 0.5, true, true);
            }
        });
        panelGraph.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pressed = e.getPoint();
                //System.out.println("pressed on ("+e.getPoint().x+", "+e.getPoint().y+")");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                released = e.getPoint();
                setOrigin(true, true);
                //System.out.println("released on ("+e.getPoint().x+", "+e.getPoint().y+")");
            }
        });
    }

    private void initPairs() {
        graphElems = new ArrayList();
        graphElems.add(new GraphElement(fieldF, Color.YELLOW));
        //...

        for (GraphElement p : graphElems) {
            p.field.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_ENTER:
                            p.function = new Function(p.field.getText());
                    }
                }
            });
        }
    }

    private void updatePanel(Graphics g) {
        //final int factorX=100, factorY=100, sizeFont=20;
        final int sizeFont = 20;

        updateAxis(g, Color.WHITE);
        //updateGraphs(g);
        panelGraph.repaint();
    }

    private void updateAxis(Graphics g, Color color) {
        int sizeFont = 10 + (int) (10 * Math.sqrt(scale));

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new java.awt.BasicStroke(2));
        g2.setFont(new java.awt.Font("Candara", java.awt.Font.PLAIN, sizeFont));
        g2.setColor(color);

        g.drawLine(origin.x, 0, origin.x, PANEL_HEIGHT);
        g.drawLine(0, origin.y, PANEL_WIDTH, origin.y);
        //System.out.println("from "+(-origin.x/pixelDistance/scale)+" to "+ ((PANEL_WIDTH-origin.x)/pixelDistance/scale));
        if (scale >= 1) {
            for (int i = -origin.x / pixelDistance / (int) scale; i <= (PANEL_WIDTH - origin.x) / pixelDistance / scale; i += 1 / scale) {
                g.drawString(String.valueOf(i), (int) (origin.x + i * pixelDistance * scale), origin.y + sizeFont);
            }

            for (int i = -origin.y / pixelDistance / (int) scale; i <= (PANEL_HEIGHT - origin.y) / pixelDistance / scale; i += 1 / scale) {
                if (i != 0) {
                    g.drawString(String.valueOf(-i), origin.x, (int) (origin.y + i * pixelDistance * scale));
                }
            }
        } else {
            for (float i = -origin.x / pixelDistance / scale; i <= (PANEL_WIDTH - origin.x) / pixelDistance / scale; i += 1 / scale) {
                g.drawString(String.valueOf(i), origin.x + (int) i * pixelDistance, origin.y + sizeFont);
            }

            for (float i = -origin.y / pixelDistance / scale; i <= (PANEL_HEIGHT - origin.y) / pixelDistance / scale; i += 1 / scale) {
                if (i != 0) {
                    g.drawString(String.valueOf(-i), origin.x, origin.y + (int) i * pixelDistance);
                }
            }
        }

    }

    private void updateGraphs(Graphics g) {
        final int stroke = 2, ox = PANEL_WIDTH / 2, oy = PANEL_HEIGHT / 2;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new java.awt.BasicStroke(stroke));

        final int fromTo = PANEL_WIDTH / 2 / factorX;

        for (GraphElement p : graphElems) {
            Function f = p.function;
            g2.setColor(p.color);

            if (f != null) {
                int oldX = -fromTo, oldY = (int) f.eval(oldX) * factorY;
                oldX *= factorX;

                for (int i = -fromTo + 1; i <= fromTo; i++) {
                    int x = i * factorX, y = (int) f.eval(i) * factorY;
                    g2.drawLine(ox + oldX, oy - oldY, ox + x, oy - y);
                    oldX = x;
                    oldY = y;
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
        fieldF = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        panelGraph = new javax.swing.JPanel(){
            @Override
            public void paint(java.awt.Graphics g){
                super.paint(g);
                updatePanel(g);
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

        fieldF.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        fieldF.setText("2^(6/3*5)");
        fieldF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldFActionPerformed(evt);
            }
        });
        jScrollPane1.setViewportView(fieldF);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 280, 50));

        jLabel2.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        jLabel2.setText("f(x)");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 30, 20));

        jButton1.setBackground(new java.awt.Color(133, 96, 166));
        jButton1.setFont(new java.awt.Font("Candara", 1, 14)); // NOI18N
        jButton1.setText("(0,0)");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

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

    private void fieldFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldFActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        /*
        setScale(1, false, false);
        setOrigin(new Point(PANEL_WIDTH/2, PANEL_HEIGHT/2), false, true);
         */
        scale = 1;
        origin.setLocation(PANEL_WIDTH / 2, PANEL_HEIGHT / 2);
        panelGraph.repaint();
    }//GEN-LAST:event_jButton1ActionPerformed

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

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new View().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField fieldF;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelGraph;
    // End of variables declaration//GEN-END:variables
}
