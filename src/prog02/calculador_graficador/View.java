package prog02.calculador_graficador;

import maths.Function;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import maths.FunctionEval;

public class View extends javax.swing.JFrame {

    private static final byte INITIAL_SCALE = 1;
    private static final double MIN_SCALE = 0.125;
    private static final byte MAX_SCALE = 10;

    private boolean darkTheme;
    private Color colorBkg;
    private Color colorMid;
    private Color colorBkgSecond;
    private Color colorFrg;

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
        this.initComponents();

        this.scale = getBoundedScale(scale);
        this.pixelDistance = pixelDistance;
        this.panelGraphWidth = (int) panelGraph.getSize().getWidth();
        this.panelGraphHeight = (int) panelGraph.getSize().getHeight();
        this.origin = new Point(panelGraphWidth / 2, panelGraphHeight / 2);

        this.darkTheme = false;
        this.setColorsTheme();

        this.initPanelGraph();
        this.initGraphElems();

        this.panelGraph.setPreferredSize(new Dimension(this.panelGraphWidth, this.panelGraphHeight));
        this.setResizable(false);

        javax.swing.JOptionPane.showMessageDialog(this, "Enter an algebraic expression in terms of x and press enter to graph.\n\nRoots are not supported.\n\nExample:\n(x^2-5)/(1/2*x^3)+6", "How it works", javax.swing.JOptionPane.INFORMATION_MESSAGE);
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
        
        this.panelGraph.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //System.out.println("DRAG");
                //*
                that.released = e.getPoint().getLocation();
                
                if (that.pressed == null || that.released == null) return;
                if (that.pressed != that.released) {
                    setOrigin(true, true);
                    that.pressed = e.getPoint().getLocation();
                }//*/
            }
        });
    }

    private GraphElement createGraphElem() {
        javax.swing.JTextField fieldFunction = new javax.swing.JTextField();
        fieldFunction.setFont(new java.awt.Font("Candara", 0, 18));

        javax.swing.JScrollPane scrollFunction = new javax.swing.JScrollPane();
        scrollFunction.setViewportView(fieldFunction);

        javax.swing.JButton btnDelFunction = new javax.swing.JButton();
        btnDelFunction.setBackground(new java.awt.Color(255, 51, 51));
        btnDelFunction.setFont(new java.awt.Font("Candara", 1, 14)); // NOI18N
        btnDelFunction.setForeground(new java.awt.Color(255, 255, 255));
        btnDelFunction.setText("X");

        javax.swing.JPanel panelFunction = new javax.swing.JPanel();

        javax.swing.GroupLayout panelFunctionLayout = new javax.swing.GroupLayout(panelFunction);
        panelFunction.setLayout(panelFunctionLayout);

        panelFunctionLayout.setHorizontalGroup(createHorizontalGroupPanelFunction(panelFunction, scrollFunction, btnDelFunction));
        panelFunctionLayout.setVerticalGroup(createVerticalGroupPanelFunction(panelFunction, scrollFunction, btnDelFunction));

//        javax.swing.GroupLayout panelGraphElemsLayout = new javax.swing.GroupLayout(panelGraphElems);
//        panelGraphElems.setLayout(panelGraphElemsLayout);
        GraphElement graphElem = new GraphElement(fieldFunction, generateRandomColor(), scrollFunction, panelFunction);

        fieldFunction.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        graphElem.setColor(generateRandomColor());
                        graphElem.setFunction(new Function(fieldFunction.getText()));
                }
            }
        });
        btnDelFunction.addActionListener((java.awt.event.ActionEvent evt) -> {
            System.out.println("1");
            if (this.graphElems.size() > 1) {
                this.graphElems.remove(graphElem);
                this.panelGraphElems.remove(panelFunction);
                this.positionPanelElems();
                this.panelGraphElems.repaint();
            }
        });

        return graphElem;
    }

    private javax.swing.GroupLayout.Group createHorizontalGroupPanelFunction(javax.swing.JPanel panelFunction, javax.swing.JScrollPane scrollFunction, javax.swing.JButton btnDelFunction) {
        javax.swing.GroupLayout panelFunctionLayout = (javax.swing.GroupLayout) panelFunction.getLayout();

        javax.swing.GroupLayout.Group group = panelFunctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelFunctionLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(scrollFunction, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelFunction)
                        .addGap(12, 12, 12));

        return group;
    }

    private javax.swing.GroupLayout.Group createVerticalGroupPanelFunction(javax.swing.JPanel panelFunction, javax.swing.JScrollPane scrollFunction, javax.swing.JButton btnDelFunction) {
        javax.swing.GroupLayout panelFunctionLayout = (javax.swing.GroupLayout) panelFunction.getLayout();

        javax.swing.GroupLayout.Group group = panelFunctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(scrollFunction, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelFunctionLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnDelFunction));

        return group;
    }

    private javax.swing.GroupLayout.Group createHorizontalGroupPanelGraphElems() {
        javax.swing.GroupLayout panelGraphElemsLayout = (javax.swing.GroupLayout) this.panelGraphElems.getLayout();

//        panelGraphElemsLayout.setHorizontalGroup(
//                panelGraphElemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGroup(panelGraphElemsLayout.createSequentialGroup()
//                                .addContainerGap()
//                                .addGroup(panelGraphElemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                                        .addComponent(panelFunction1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                .addContainerGap(118, Short.MAX_VALUE))
//        );
        javax.swing.GroupLayout.Group subSubGroup = panelGraphElemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        for (GraphElement graphElem : this.graphElems) {
//            subSubGroup = subSubGroup.addComponent(graphElem.getPanel().getComponent(0), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE);
            subSubGroup = subSubGroup.addComponent(graphElem.getPanel(), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE);
        }

        javax.swing.GroupLayout.Group subGroup = panelGraphElemsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subSubGroup)
                .addContainerGap(118, Short.MAX_VALUE);

        javax.swing.GroupLayout.Group group
                = panelGraphElemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(subGroup);

        return group;
    }

    private javax.swing.GroupLayout.Group createVerticalGroupPanelGraphElems() {
//        panelGraphElemsLayout.setVerticalGroup(
//                panelGraphElemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGroup(panelGraphElemsLayout.createSequentialGroup()
//                                .addContainerGap()
//                                .addComponent(panelFunction1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                                .addComponent(panelFunction2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                .addContainerGap(300, Short.MAX_VALUE))
//        );

        javax.swing.GroupLayout panelGraphElemsLayout = (javax.swing.GroupLayout) this.panelGraphElems.getLayout();
        javax.swing.GroupLayout.SequentialGroup seqGroup = panelGraphElemsLayout.createSequentialGroup().addContainerGap();

        for (int i = 0; i < this.graphElems.size(); i++) {
            GraphElement graphElem = this.graphElems.get(i);
//            seqGroup = seqGroup.addComponent(graphElem.getPanel().getComponent(0), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE);
            seqGroup = seqGroup.addComponent(graphElem.getPanel(), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE);
            if (i != this.graphElems.size() - 1) {
                seqGroup = seqGroup.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED);
            }
        }

        seqGroup = seqGroup.addContainerGap(300, Short.MAX_VALUE);

        javax.swing.GroupLayout.Group group = panelGraphElemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(seqGroup.addContainerGap(300, Short.MAX_VALUE));

        return group;
    }

    private void addGraphElem() {
        GraphElement graphElem = this.createGraphElem();
        this.graphElems.add(graphElem);
        this.positionPanelElems();
    }

    private void positionPanelElems() {
        javax.swing.GroupLayout panelGraphElemsLayout = new javax.swing.GroupLayout(this.panelGraphElems);
        this.panelGraphElems.setLayout(panelGraphElemsLayout);

        panelGraphElemsLayout.setHorizontalGroup(this.createHorizontalGroupPanelGraphElems());
        panelGraphElemsLayout.setVerticalGroup(this.createVerticalGroupPanelGraphElems());
    }

    private void initGraphElems() {
        // clear all initial graphElems from GUI
        this.panelGraphElems.removeAll();

        // initialize graphElemes list
        this.graphElems = new ArrayList();

        this.addGraphElem();
    }

    private void updatePanelGraph(Graphics g) {
        //final int factorX=100, factorY=100, sizeFont=20;
//        final int sizeFont = 20;
        this.updateAxis(g);
        this.updateGraphs(g);
        this.panelGraph.repaint();
    }

    private void updateAxis(Graphics g) {
//        int fontSize = 10 + (int) (10 * Math.sqrt(scale));
        int fontSize = 20;

        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("Candara", Font.PLAIN, fontSize));

        g2.setColor(this.colorMid);
        g2.setStroke(new BasicStroke(1));

        for (double i = -this.origin.x / this.pixelDistance / this.scale; i <= (this.panelGraphWidth - this.origin.x) / this.pixelDistance / this.scale; i += 1 / this.scale) {
            int finalX = (int) (this.origin.x + i * this.pixelDistance * this.scale);
            g.drawLine(finalX, 0, finalX, this.panelGraphHeight);
        }

        for (double i = this.origin.y / this.pixelDistance / this.scale; i >= -(this.panelGraphHeight - this.origin.y) / this.pixelDistance / this.scale; i -= 1 / this.scale) {
            if (i != 0.0) {
                int finalY = (int) (this.origin.y - i * this.pixelDistance * this.scale);
                g.drawLine(0, finalY, this.panelGraphWidth, finalY);
            }
        }

        g2.setColor(this.colorFrg);
        g2.setStroke(new BasicStroke(2));

        g.drawLine(this.origin.x, 0, this.origin.x, this.panelGraphHeight);
        g.drawLine(0, this.origin.y, this.panelGraphWidth, this.origin.y);

        for (double i = -this.origin.x / this.pixelDistance / this.scale; i <= (this.panelGraphWidth - this.origin.x) / this.pixelDistance / this.scale; i += 1 / this.scale) {
            String coord;
            if (i % 1 == 0) {
                coord = String.valueOf((int) i);
            } else {
                coord = String.valueOf(Math.round(i * 100.0) / 100.0);
            }
            g.drawString(coord, (int) (this.origin.x + i * this.pixelDistance * this.scale), this.origin.y + fontSize);
        }

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
                    byte success = eval.getSuccess();
                    
                    if (success == FunctionEval.SUCCESS) {
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
                    } else if (success == FunctionEval.UNDEFINED || success == FunctionEval.IMAGINARY) {
                        avoidLine = true;
                    }
                }
            }
        }
    }

    private static Color generateRandomColor() {
        Random random = new Random();
        int minBrightness = 150;

        while (true) {
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);

            int brightness = (int) (0.299 * red + 0.587 * green + 0.114 * blue);

            if (brightness > minBrightness) {
                return new Color(red, green, blue);
            }
        }
    }

    private void setColorsTheme() {
        String btnThemeText;

        if (this.darkTheme) {
            this.colorBkg = new java.awt.Color(40, 40, 40);
            this.colorBkgSecond = new java.awt.Color(60, 60, 60);
            this.colorFrg = java.awt.Color.WHITE;
            btnThemeText = "Light Theme";
        } else {
            this.colorBkg = java.awt.Color.WHITE;
            this.colorBkgSecond = new java.awt.Color(204, 204, 204);
            this.colorFrg = new java.awt.Color(40, 40, 40);
            btnThemeText = "Dark Theme";
        }
        this.colorMid = java.awt.Color.GRAY;

        this.panelGraph.setBackground(this.colorBkg);
        this.panelOptions.setBackground(this.colorBkgSecond);
        this.panelGraphElems.setBackground(this.colorBkgSecond);
        this.btnTheme.setText(btnThemeText);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelWindow = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panelOptions = new javax.swing.JPanel();
        btnTheme = new javax.swing.JButton();
        btnAddGraph = new javax.swing.JButton();
        btnGraph = new javax.swing.JButton();
        scrollGraphElems = new javax.swing.JScrollPane();
        panelGraphElems = new javax.swing.JPanel();
        panelFunction1 = new javax.swing.JPanel();
        scrollFunction1 = new javax.swing.JScrollPane();
        fieldFunction1 = new javax.swing.JTextField();
        btnDelFunction1 = new javax.swing.JButton();
        panelFunction2 = new javax.swing.JPanel();
        scrollFunction2 = new javax.swing.JScrollPane();
        fieldFunction2 = new javax.swing.JTextField();
        btnDelFunction2 = new javax.swing.JButton();
        panelGraph = new javax.swing.JPanel(){
            @Override
            public void paint(java.awt.Graphics g){
                super.paint(g);
                updatePanelGraph(g);
            }
        };

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelWindow.setBackground(new java.awt.Color(197, 205, 211));
        panelWindow.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelHeader.setBackground(new java.awt.Color(102, 153, 255));
        panelHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("OCR A Extended", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 204, 204));
        jLabel1.setText("CalcuGraph");
        panelHeader.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, -1, -1));

        panelWindow.add(panelHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 60));

        panelOptions.setBackground(new java.awt.Color(204, 204, 204));
        panelOptions.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnTheme.setBackground(new java.awt.Color(102, 153, 255));
        btnTheme.setFont(new java.awt.Font("Candara", 1, 14)); // NOI18N
        btnTheme.setForeground(new java.awt.Color(204, 204, 204));
        btnTheme.setText("Dark theme");
        btnTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemeActionPerformed(evt);
            }
        });
        panelOptions.add(btnTheme, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 410, -1, -1));

        btnAddGraph.setBackground(new java.awt.Color(102, 153, 255));
        btnAddGraph.setFont(new java.awt.Font("Candara", 1, 14)); // NOI18N
        btnAddGraph.setForeground(new java.awt.Color(204, 204, 204));
        btnAddGraph.setText("Add graph");
        btnAddGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddGraphActionPerformed(evt);
            }
        });
        panelOptions.add(btnAddGraph, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, -1, -1));

        btnGraph.setBackground(new java.awt.Color(102, 153, 255));
        btnGraph.setFont(new java.awt.Font("Candara", 1, 14)); // NOI18N
        btnGraph.setForeground(new java.awt.Color(204, 204, 204));
        btnGraph.setText("(0,0)");
        btnGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGraphActionPerformed(evt);
            }
        });
        panelOptions.add(btnGraph, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 410, -1, -1));

        scrollGraphElems.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelGraphElems.setBackground(new java.awt.Color(204, 204, 204));

        fieldFunction1.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        fieldFunction1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldFunction1ActionPerformed(evt);
            }
        });
        scrollFunction1.setViewportView(fieldFunction1);

        btnDelFunction1.setBackground(new java.awt.Color(255, 51, 51));
        btnDelFunction1.setFont(new java.awt.Font("Candara", 1, 14)); // NOI18N
        btnDelFunction1.setForeground(new java.awt.Color(255, 255, 255));
        btnDelFunction1.setText("X");
        btnDelFunction1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelFunction1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFunction1Layout = new javax.swing.GroupLayout(panelFunction1);
        panelFunction1.setLayout(panelFunction1Layout);
        panelFunction1Layout.setHorizontalGroup(
            panelFunction1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFunction1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollFunction1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelFunction1)
                .addGap(12, 12, 12))
        );
        panelFunction1Layout.setVerticalGroup(
            panelFunction1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollFunction1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(panelFunction1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDelFunction1))
        );

        fieldFunction2.setFont(new java.awt.Font("Candara", 0, 18)); // NOI18N
        fieldFunction2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldFunction2ActionPerformed(evt);
            }
        });
        scrollFunction2.setViewportView(fieldFunction2);

        btnDelFunction2.setBackground(new java.awt.Color(255, 51, 51));
        btnDelFunction2.setFont(new java.awt.Font("Candara", 1, 14)); // NOI18N
        btnDelFunction2.setForeground(new java.awt.Color(255, 255, 255));
        btnDelFunction2.setText("X");
        btnDelFunction2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelFunction2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFunction2Layout = new javax.swing.GroupLayout(panelFunction2);
        panelFunction2.setLayout(panelFunction2Layout);
        panelFunction2Layout.setHorizontalGroup(
            panelFunction2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFunction2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollFunction2, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelFunction2)
                .addGap(12, 12, 12))
        );
        panelFunction2Layout.setVerticalGroup(
            panelFunction2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollFunction2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(panelFunction2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDelFunction2))
        );

        javax.swing.GroupLayout panelGraphElemsLayout = new javax.swing.GroupLayout(panelGraphElems);
        panelGraphElems.setLayout(panelGraphElemsLayout);
        panelGraphElemsLayout.setHorizontalGroup(
            panelGraphElemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGraphElemsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGraphElemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelFunction1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelFunction2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(118, Short.MAX_VALUE))
        );
        panelGraphElemsLayout.setVerticalGroup(
            panelGraphElemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGraphElemsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFunction1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelFunction2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(300, Short.MAX_VALUE))
        );

        scrollGraphElems.setViewportView(panelGraphElems);

        panelOptions.add(scrollGraphElems, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 400));

        panelWindow.add(panelOptions, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 300, 450));

        panelGraph.setBackground(new java.awt.Color(255, 255, 255));
        panelGraph.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelWindow.add(panelGraph, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 70, 580, 440));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelWindow, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelWindow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void btnAddGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddGraphActionPerformed
        this.addGraphElem();
    }//GEN-LAST:event_btnAddGraphActionPerformed

    private void btnDelFunction1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelFunction1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDelFunction1ActionPerformed

    private void fieldFunction2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldFunction2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldFunction2ActionPerformed

    private void btnDelFunction2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelFunction2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDelFunction2ActionPerformed

    private void btnThemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemeActionPerformed
        this.darkTheme = !this.darkTheme;
        setColorsTheme();
    }//GEN-LAST:event_btnThemeActionPerformed

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
    private javax.swing.JButton btnAddGraph;
    private javax.swing.JButton btnDelFunction1;
    private javax.swing.JButton btnDelFunction2;
    private javax.swing.JButton btnGraph;
    private javax.swing.JButton btnTheme;
    private javax.swing.JTextField fieldFunction1;
    private javax.swing.JTextField fieldFunction2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel panelFunction1;
    private javax.swing.JPanel panelFunction2;
    private javax.swing.JPanel panelGraph;
    private javax.swing.JPanel panelGraphElems;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelOptions;
    private javax.swing.JPanel panelWindow;
    private javax.swing.JScrollPane scrollFunction1;
    private javax.swing.JScrollPane scrollFunction2;
    private javax.swing.JScrollPane scrollGraphElems;
    // End of variables declaration//GEN-END:variables
}
