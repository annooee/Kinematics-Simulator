
/**
 * [KinematicsSimulator.java]
 * This program simulates movement in the kinematics unit. User enters velocity, acceleration, and scale and the program will output
 * the animation, graphs, and motion diagram accordingly.
 *
 * @author Annie Yu
 * @version 1.0 June 11, 2021
 **/

//Graphics &GUI imports

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

//Keyboard imports
import java.awt.event.*;

public class KinematicsSimulator extends JFrame {

    /****************** CLASS VARIABLES********************/
    /** The variables can be accessed across all methods   ***/
    /**     You will need to add some variables here             ***/
    /********************************************************/
    static double x, y; //instant position used for animation
    static double tempScale = 1; //temporary scale because scale is constant therefore an array is not needed however it changes immediately when user puts in new input
    static double scale = 1; //default scale value subjective to change
    static double v0 = 0; //default initial velocity value subjective to change
    static double v; //instant velocity used for animation
    static double a = 1; //default acceleration value subjective to change
    static double tempA = 1; //temporary acceleration because acceleration is constant therefore an array is not needed however it changes immediately when user puts in new input
    static int t = 0; //time
    static int initialX = 500; //starting x coordinate of car
    static int initialY = 690; //starting y coordinate of car
    static double[] velocity = new double[101]; //from 0-100, there are 10 seconds, velocity every 0.1 intervals
    static double[] displacement = new double[101]; //from 0-100, there are 10 seconds, displacement every 0.1 intervals
    static int[] location = new int[101]; //from 0-100, there are 10 seconds, position on screen every 0.1 intervals

    static GameAreaPanel gamePanel;
    static JPanel inputPanel; //panel embedded in the game panel used for user input
    static Boolean start = false;
    static Boolean showMotion = true;
    static Boolean validVelocity = true;
    static Boolean validAcceleration = true;
    static Boolean validScale = true;

    //images
    BufferedImage orangeCar;
    BufferedImage orangeCarReverse;
    BufferedImage kinematicsEquations;

    /*************************************************************************
     *******  GameFrame - Setups up the Window and Starts displaying it *****
     ************************* DO NOT MODIFY *******************************/
    KinematicsSimulator() {
        super("Kinematics Simulator");
        // Set the frame to full screen
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1024, 768);
        gamePanel = new GameAreaPanel();
        this.add(gamePanel);

        MyKeyListener keyListener = new MyKeyListener();
        this.addKeyListener(keyListener);
        MyMouseListener mouseListener = new MyMouseListener();
        this.addMouseListener(mouseListener);

        this.requestFocusInWindow(); //make sure the frame has focus
        //this.setVisible(true);

        Thread t = new Thread(new Runnable() {
            public void run() {
                animate();
            }
        }); //start the gameLoop
        t.start();

    } //End of Constructor
    /****** End of GameFrame *********************************/

    /**************************** Main Method ************************/
    /**
     * ******************      DO NOT MODIFY                 *********/
    public static void main(String[] args) {
        System.out.println("?>?");

        EventQueue.invokeLater(() -> {
            KinematicsSimulator kinematicsSimulator = new KinematicsSimulator();
            kinematicsSimulator.setVisible(true);
        });
    }
    /****** end of Main *********************************/

    /************************** Animate - Gameloop************************/
    /**      This section is where the games state is updated.       *****/
    /**     You will need to have most of your game logic in here       **/
    /*********************************************************************/
    public void animate() {
        try {
            //image read
            orangeCar = ImageIO.read(new File("orangeCar.png"));
            orangeCarReverse = ImageIO.read(new File("orangeCarReverse.png"));
            kinematicsEquations = ImageIO.read(new File("kinematicsEquations.png"));
        } catch (Exception e) {
            System.out.println("error loading image files.");
        }
        for (int i = 0; i <= 100; i++) { //initialize calculations into arrays to be accessed later
            velocity[i] = v0 + a * i / 10;
            displacement[i] = v0 * i / 10 + a * i * i / 100 / 2;
            location[i] = (int) (10 * displacement[i] + initialX);
        }
        //prepare the location information for the animation
        x = initialX;
        y = initialY;
        v = v0;

        gamePanel.setLayout(new GridLayout(3, 1));

        //input panel and labels
        inputPanel = new JPanel();
        inputPanel.setLayout(null);
        gamePanel.add(inputPanel); //this is where you add the input panel as part of game panel

        JLabel velocityLabel = new JLabel("Initial Velocity (m/s):");
        JLabel accelerationLabel = new JLabel("Acceleration (m/s^2):");
        JLabel scaleLabel = new JLabel("Scale:");
        JLabel motionChoiceLabel = new JLabel("Show Motion Diagram:");

        //messages
        JLabel velocityMessage = new JLabel("If you make a change, click ENTER.");
        velocityMessage.setForeground(Color.GREEN);
        JLabel accelerationMessage = new JLabel("If you make a change, click ENTER.");
        accelerationMessage.setForeground(Color.GREEN);
        JLabel scaleMessage = new JLabel("If you make a change, click ENTER.");
        scaleMessage.setForeground(Color.GREEN);

        //Input Field
        JTextField initialVelocityTextField = new JTextField(); //velocity
        initialVelocityTextField.setText(Double.toString(v0));
        JTextField accelerationTextField = new JTextField(); //acceleration
        accelerationTextField.setText(Double.toString(a));
        JTextField scaleTextField = new JTextField(); //scale
        scaleTextField.setText(Double.toString(scale));

        //button
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");

        //checkbox
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(showMotion);

        //add to inputPanel
        inputPanel.add(velocityLabel);
        inputPanel.add(initialVelocityTextField);
        inputPanel.add(accelerationLabel);
        inputPanel.add(accelerationTextField);
        inputPanel.add(scaleLabel);
        inputPanel.add(scaleTextField);
        inputPanel.add(motionChoiceLabel);
        inputPanel.add(startButton);
        inputPanel.add(stopButton);
        inputPanel.add(checkBox);
        inputPanel.add(velocityMessage);
        inputPanel.add(accelerationMessage);
        inputPanel.add(scaleMessage);

        //equation display
        JLabel equations = new JLabel(new ImageIcon(kinematicsEquations));
        equations.setBounds(600, 10, 340, 150);
        inputPanel.add(equations);

        //set bounds
        velocityLabel.setBounds(10, 20, 150, 30);
        initialVelocityTextField.setBounds(160, 20, 100, 30);
        accelerationLabel.setBounds(10, 60, 150, 30);
        accelerationTextField.setBounds(160, 60, 100, 30);
        scaleLabel.setBounds(10, 100, 150, 30);
        scaleTextField.setBounds(160, 100, 100, 30);
        motionChoiceLabel.setBounds(10, 150, 200, 30);
        checkBox.setBounds(180, 150, 30, 30);
        startButton.setBounds(10, 185, 100, 20);
        stopButton.setBounds(150, 185, 100, 20);
        velocityMessage.setBounds(280, 20, 200, 30);
        accelerationMessage.setBounds(280, 60, 200, 30);
        scaleMessage.setBounds(280, 100, 300, 30);

        //reminder about program functions
        JTextArea textArea = new JTextArea(
                "This program can only create a 10 seconds animation\n" +
                        "and it will keep replaying until user exists."
        );
        textArea.setForeground(Color.BLUE);
        textArea.setBounds(600, 170, 340, 50);
        inputPanel.add(textArea);

        //initial velocity action listener for input
        initialVelocityTextField.addActionListener(new ActionListener() { //velocity input listener
            @Override
            /**
             * actionPerformed
             * when user hits the ENTER button for velocity input, this method validates input, displays messages accordingly
             * if valid, input will be saved under v0 to be used later on in calculations
             * @param e Action Event
             */
            public void actionPerformed(ActionEvent e) {
                try {
                    v0 = Double.parseDouble(initialVelocityTextField.getText());
                    velocityMessage.setText("");
                    validVelocity = true;
                    velocityMessage.setForeground(Color.BLUE);
                    velocityMessage.setText("Please click Start to apply change.");
                    if (validAcceleration && validScale) {
                        startButton.setEnabled(true); //ensure other inputs are valid for start button enabling
                    }
                } catch (NumberFormatException initialVelocityException) {
                    velocityMessage.setForeground(Color.RED);
                    velocityMessage.setText("Invalid input for v0."); //message for invalid input
                    startButton.setEnabled(false);
                    validVelocity = false;
                }
            }
        });

        accelerationTextField.addActionListener(new ActionListener() { //acceleration input listener
            @Override
            /**
             * actionPerformed
             * when user hits the ENTER button for acceleration input, this method validates input, displays messages accordingly
             * if valid, input will be saved under tempA to be used later on in calculations
             * @param e Action Event
             */
            public void actionPerformed(ActionEvent e) {
                try {
                    tempA = Double.parseDouble(accelerationTextField.getText());
                    accelerationMessage.setText("");
                    validAcceleration = true;
                    accelerationMessage.setForeground(Color.BLUE);
                    accelerationMessage.setText("Please click Start to apply change.");
                    if (validVelocity && validScale) {
                        startButton.setEnabled(true); //ensure other inputs are valid for start button enabling
                    }
                } catch (NumberFormatException accelerationException) {
                    accelerationMessage.setForeground(Color.RED);
                    accelerationMessage.setText("Invalid input for acceleration."); //message for invalid input
                    startButton.setEnabled(false);
                    validAcceleration = false;
                }
            }
        });

        scaleTextField.addActionListener(new ActionListener() {
            @Override
            /**
             * actionPerformed
             * when user hits the ENTER button for scale input, this method validates input, displays messages accordingly
             * if valid, input will be saved under tempA to be used later on in calculations
             * @param e Action Event
             */
            public void actionPerformed(ActionEvent e) {
                try {
                    tempScale = Double.parseDouble(scaleTextField.getText());
                    if (tempScale <= 0) { //has to be greater than 0
                        validScale = false; //false in this case
                        startButton.setEnabled(false); //restrict start
                        scaleMessage.setForeground(Color.RED);
                        scaleMessage.setText("Invalid input for scale. Input positive number.");
                    } else {
                        scaleMessage.setForeground(Color.BLUE);
                        scaleMessage.setText("Please click Start to apply change.");
                        validScale = true;
                        if (validVelocity && validAcceleration) { //ensure other inputs are valid
                            startButton.setEnabled(true);
                        }
                    }
                } catch (NumberFormatException scaleException) { //invalid input for scale
                    scaleMessage.setForeground(Color.RED);
                    scaleMessage.setText("Invalid input for scale.");
                    startButton.setEnabled(false);
                    validScale = false;
                }
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            /**
             * actionPerformed
             * method invokes messages when Start button is pressed as well checking whether check box has been
             * selected or not to perform events
             * @param e Action Event
             */
            public void actionPerformed(ActionEvent e) {
                start = true; //start true for default inputs
                showMotion = checkBox.isSelected(); //checkbox
                a = tempA;
                scale = tempScale;
                velocityMessage.setForeground(Color.GREEN);
                velocityMessage.setText("If you make a change, click ENTER."); //remind user to click enter
                accelerationMessage.setForeground(Color.GREEN);
                accelerationMessage.setText("If you make a change, click ENTER.");
                scaleMessage.setForeground(Color.GREEN);
                scaleMessage.setText("If you make a change, click ENTER.");

                for (int i = 0; i <= 100; i++) { //calculating user input with kinematics equations for output
                    velocity[i] = v0 + a * i / 10;
                    displacement[i] = v0 * i / 10 + a * i * i / 100 / 2;
                    location[i] = (int) (10 * displacement[i] * scale + initialX);
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            /**
             * actionPerformed
             * method stops animation once Stop button is pressed
             * @param e Action Event
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                start = false;
            }
        });
        //while start is true, allow animation to keep running
        while (true) {
            this.x = location[t]; //store amount of time it takes to go through array values
            this.y = initialY;
            this.v = velocity[t];
            if (start) {
                t++;
                if (t == 101) t = 0; //restart animation after 10 seconds
            }
            //time between every repaint
            try {
                Thread.sleep(100); //animate every 0.1 second
            } catch (Exception exc) {
            }  //delay

            this.repaint(); //update the screen, repaint graphics
        }

    }

    /****** End of Animate *********************************/

//Inner class - JPanel
    private class GameAreaPanel extends JPanel {
        /************************** PaintComponenet ************************/
        /**             This section is where the screen is drawn         **/
        /**     You will need to draw all your items in this method       **/
        /*******************************************************************/
        public void paintComponent(Graphics g) {
            super.paintComponent(g); //required
            setDoubleBuffered(true);


            g.setColor(Color.WHITE); //There are many graphics commands that Java can use
            //the canvas start from coordinate (0,240) as the space above 240 is for the input panel
            g.fillRect(0, 240, 1010, 510); //notice the x,y variables that we control from our animate method
            g.setColor(Color.BLACK);
            g.drawLine(5, initialY+10, 1005, initialY+10);

            //draw car platform line
            for (int i = 0; i <= 10; i++) {
                g.drawLine(5 + 100 * i, initialY+10 - 5, 5 + 100 * i, initialY+10 + 5);
                double k = (-1000 / 10 / 2 + 10 * i) / scale;
                if (k == 0) g.drawString(Double.toString(Math.round(k * 100) / 100.0), 2 + 100 * i, initialY+10 + 15);
                else g.drawString(Double.toString(Math.round(k * 100) / 100.0), 100 * i, initialY+10 + 15);
            }
            g.drawString("(m)",980, initialY+30);

            if (v >= 0) { //if positive velocity, car faces the right
                g.drawImage(orangeCar, (int) x, (int) y - 15, this);
            } else { //if negative velocity, care faces the left
                g.drawImage(orangeCarReverse, (int) x, (int) y - 15, this);
            }

            //call draw methods
            drawMotionDiagram(g);
            drawDTGraph(g);
            drawVTGraph(g);
            drawATGraph(g);
        }

        /**
         * drawMotionDiagram
         * This methods draws the motion diagram
         * @param g Graphics
         */
        public void drawMotionDiagram(Graphics g) {
            //draw motion diagram
            Font numberFont = new Font("SansSerif", Font.PLAIN, 10);
            g.setFont(numberFont);

            if (showMotion) { //checkbox determines whether true or not
                double turnPoint = (-velocity[0]) / a; //turning point change of velocity
                int s = 0;
                if (turnPoint > 0 || turnPoint < 10) {
                    s = (int) Math.floor(turnPoint) + 1;
                }
                for (int i = 0; i <= 10; i++) {
                    int x1 = location[i * 10]; //draw the element at index at each second of the 10 seconds
                    // the animation runs for because every 0.1 second the animation runs but the motion diagram only cares
                    // the per second interval.
                    int y1 = 600; //draw points before turn point at the bottom
                    if ((i >= s) && (turnPoint > 0)) {
                        y1 = 550; //draw points after turn point higher
                    }
                    //draw points
                    g.fillRect(x1, y1, 3, 3);
                    g.drawString(Integer.toString(i + 1), x1, y1 - 10);
                }
            }

        }

        /**
         * drawXY
         * This method is for drawing the graphs
         * @param g       For graphics
         * @param originX the x coordinate centre dot for te graph where the lines extend from
         * @param originY the x coordinate centre dot for te graph where the lines extend from
         * @param width   width of the line
         * @param height  height of the line
         * @param labelX  label of x axis
         * @param labelY  label of y axis
         */
        public void drawXY(Graphics g, int originX, int originY, int width, int height, String labelX, String labelY) {
            g.drawLine(originX, originY - height, originX, originY + height); //y-axis
            g.drawLine(originX, originY, originX + width + 10, originY); //x-axis
            g.drawLine(originX, originY - height, originX - 4, originY - height + 4); //y arrow
            g.drawLine(originX, originY - height, originX + 4, originY - height + 4); //y arrow
            g.drawLine(originX + width + 10, originY, originX + width + 10 - 4, originY + 4); //x arrow
            g.drawLine(originX + width + 10, originY, originX + width + 10 - 4, originY - 4); //x arrow
            g.drawString(labelY, originX - 25, originY - height - 5);
            g.drawString(labelX, originX + width + 20, originY + 3);

            for (int i = 1; i <= 10; i++) {
                int x = originX + 20 * i;
                g.drawLine(x, originY - 3, x, originY + 3);
                g.drawString(Integer.toString(i), x - 3, originY + 15);
            }
            g.drawString("0", originX - 15, 420 + 5);
        }

        /**
         * drawDTGraph
         * This method is for drawing the displacement time graph
         * @param g For graphics
         */
        public void drawDTGraph(Graphics g) {
            //draw graphs
            //dt
            drawXY(g, 100, 420, 200, 110, "t(s)", "d(m)"); //call method drawXY to draw graph

            //sorting method to find max and min
            double min = displacement[0];
            double max = displacement[0];

            //sort through array
            for (int i = 0; i <= 100; i++) {
                g.fillRect(100 + 2 * i, (int) (420 - displacement[i] * scale), 2, 2);
                if (displacement[i] < min) {
                    min = displacement[i];
                } else if (displacement[i] > max) {
                    max = displacement[i];
                }
            }
            //keep to one decimal
            max = Math.round(max * 100) / 100.0;
            min = Math.round(min * 100) / 100.0;

            if (min != 0) {
                g.drawLine(100 - 3, (int) (420 - min * scale), 100 + 3, (int) (420 - min * scale));
                g.drawString(Double.toString(min), 100 - 30, (int) (420 - min * scale + 5));
            }
            if (max != 0) {
                g.drawLine(100 - 3, (int) (420 - max * scale), 100 + 3, (int) (420 - max * scale));
                g.drawString(Double.toString(max), 100 - 30, (int) (420 - max * scale + 5));
            }


            //draw indicator
            int topX = 100 + 2 * t;
            int topY = (int) (420 - displacement[t] * scale);
            Polygon triangle = new Polygon(new int[]{topX, topX - 5, topX + 5}, new int[]{topY, topY + 5, topY + 5}, 3);

            g.setColor(Color.RED);
            g.fillPolygon(triangle);
            g.setColor(Color.BLACK);

        }
        /**
         * drawVTGraph
         * This method is for drawing the displacement time graph
         * @param g For graphics
         */
        public void drawVTGraph(Graphics g) {
            //vt
            drawXY(g, 400, 420, 200, 110, "t(s)", "v(m/s)"); //call method drawXY to draw graph

            for (int i = 0; i <= 100; i++) {
                g.fillRect(400 + 2 * i, (int) (420 - velocity[i] * 2 * scale), 2, 2);
            }
            if (velocity[0] != 0) {
                g.drawLine(400 - 3, (int) (420 - velocity[0] * 2 * scale), 400 + 3, (int) (420 - velocity[0] * 2 * scale));
                g.drawString(Double.toString(velocity[0]), 400 - 30, (int) (420 - velocity[0] * 2 * scale + 5));
            }
            if (velocity[100] != 0) {
                g.drawLine(400 - 3, (int) (420 - velocity[100] * 2 * scale), 400 + 3, (int) (420 - velocity[100] * 2 * scale));
                g.drawString(Double.toString(velocity[100]), 400 - 30, (int) (420 - velocity[100] * 2 * scale + 5));
            }

            //draw indicator
            int topX = 400 + 2 * t;
            int topY = (int) (420 - velocity[t] * 2 * scale);
            Polygon triangle = new Polygon(new int[]{topX, topX - 5, topX + 5}, new int[]{topY, topY + 5, topY + 5}, 3);
            g.setColor(Color.RED);
            g.fillPolygon(triangle);
            g.setColor(Color.BLACK);

        }
        /**
         * drawATGraph
         * This method is for drawing the displacement time graph
         * @param g For graphics
         */
        public void drawATGraph(Graphics g) {
            //at
            drawXY(g, 700, 420, 200, 110, "t(s)", "a(m/s^2)"); //call method drawXY to draw graph

            //drawing the graph line
            for (int i = 0; i <= 100; i++) {
                g.fillRect(700 + 2 * i, (int) (420 - a * 10 * scale), 2, 2);
            }
            if (a != 0) {
                g.drawString(Double.toString(a), 700 - 20, (int) (420 - a * 10 * scale + 5));
            }

            //draw indicator
            int topX = 700 + 2 * t;
            int topY = (int) (420 - a * 10 * scale);
            Polygon triangle = new Polygon(new int[]{topX, topX - 5, topX + 5}, new int[]{topY, topY + 5, topY + 5}, 3);
            g.setColor(Color.RED);
            g.fillPolygon(triangle);
            g.setColor(Color.BLACK);

            //new acceleration so when user changes a, it doesn't change the immediate graph until they click start

        }
    }

/**
 * End of paintComponent  Key Listener        This section is where keyboard input is handled                           You will add code to respond to key presses
 * Key Listener        This section is where keyboard input is handled                           You will add code to respond to key presses
 **/


/***************************** Key Listener ************************/
/**       This section is where keyboard input is handled                    **/
/**       You will add code to respond to key presses                          **/

    /*******************************************************************/
    private class MyKeyListener implements KeyListener {

        public void keyPressed(KeyEvent e) {
            //System.out.println("keyPressed="+KeyEvent.getKeyText(e.getKeyCode()));

            if (KeyEvent.getKeyText(e.getKeyCode()).equals("D")) {  //If 'D' is pressed
                System.out.println("YIKES D KEY!");
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {  //If ESC is pressed
                System.out.println("YIKES ESCAPE KEY!"); //close frame & quit
                System.exit(0);
            }
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }
    }
/****** Key Listener *********************************/

/**************************** Mouse Listener ************************/
/**       This section is where mouse input is handled                           **/
/**       You may have to add code to respond to mouse clicks          **/

    /********************************************************************/
    private class MyMouseListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            System.out.println("Mouse Clicked");
            System.out.println("X:" + e.getX() + " y:" + e.getY());
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

/****** Mouse Listener *********************************/
}