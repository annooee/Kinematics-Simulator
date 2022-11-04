/**
 * This template can be used as reference or a starting point
 * for your final summative project
 * @author Mangat
 **/

//Graphics &GUI imports
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Mouse imports
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class GameFrame2 extends JFrame {

    /****************** CLASS VARIABLES********************/
    /** The variables can be accessed across all methods   ***/
    /**     You will need to add some variables here             ***/
    /********************************************************/
    static double x, y;
    static GameAreaPanel gamePanel;


    /*************************************************************************
     *******  GameFrame - Setups up the Window and Starts displaying it *****
     ************************* DO NOT MODIFY *******************************/
    GameFrame2() {
        super("My Game");
        // Set the frame to full screen
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1024,768);
        gamePanel = new GameAreaPanel();
        this.add(new GameAreaPanel());

        MyKeyListener keyListener = new MyKeyListener();
        this.addKeyListener(keyListener);
        MyMouseListener mouseListener = new MyMouseListener();
        this.addMouseListener(mouseListener);

        this.requestFocusInWindow(); //make sure the frame has focus
        //this.setVisible(true);

        Thread t = new Thread(new Runnable() {
            public void run() { animate(); }}); //start the gameLoop
        t.start();

    } //End of Constructor
/****** End of GameFrame *********************************/

    /**************************** Main Method ************************/
    /** *******************      DO NOT MODIFY                          *********/
    public static void main(String[] args) {
        System.out.println("?>?");

        EventQueue.invokeLater(() -> {
            GameFrame2 x = new GameFrame2();
            x.setVisible(true);
        });
    }
    /****** end of Main *********************************/

    /************************** Animate - Gameloop************************/
    /**      This section is where the games state is updated.                      *****/
    /**     You will need to have most of your game logic in here               **/
    /*********************************************************************/
    public void animate() {

        while(true){
            this.x = (Math.random()*1024);  //update coords
            this.y = (Math.random()*768);
            try{ Thread.sleep(500);} catch (Exception exc){}  //delay
            this.repaint(); //update the screen
        }
    }
    /****** End of Animate *********************************/

    //Inner class - JPanel
    private class GameAreaPanel extends JPanel {
        /************************** PaintComponenet ************************/
        /**             This section is where the screen is drawn                               **/
        /**     You will need to draw all your items in this method                  **/
        /*******************************************************************/
        public void paintComponent(Graphics g) {
            super.paintComponent(g); //required
            setDoubleBuffered(true);
            g.setColor(Color.BLUE); //There are many graphics commands that Java can use
            g.fillRect((int)x, (int)y, 50, 50); //notice the x,y variables that we control from our animate method

        }
    }
/****** End of paintComponent *********************************/


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
            System.out.println("X:"+e.getX() + " y:"+e.getY());
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