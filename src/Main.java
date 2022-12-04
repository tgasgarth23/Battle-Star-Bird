import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.IOException;

public class Main extends JPanel implements KeyListener{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int FPS = 60;
    World world;
    Cursor dcursor;
    public static String user = "";

    public Main() {
        world = new World(WIDTH, HEIGHT);
        dcursor = new Cursor(Cursor.DEFAULT_CURSOR);
        addKeyListener(this);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        Thread mainThread = new Thread(new Runner());
        mainThread.start();
        Scanner scan = new Scanner(System.in);
        while(user.length() >9 || user.length() == 0) {
            System.out.println("Input your name between 0 and 10 characters to begin:");
            user = scan.nextLine();
        }
    }

    class Runner implements Runnable {
        public void run() {
            while (true) {
                try {
                    world.update(1.0 / (double) FPS);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                repaint();
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                }
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        if (c == 'c') {
            if (!world.startState && !world.stats && !ScoreBoard.promptLeaderBoard)//if game is at start screen
                world.help = !world.help;
        }
        if (c == KeyEvent.VK_SPACE) {
            if (world.isRunning && !world.paused) {//during gameplay and game is not paused
                world.flappy.jump();
            }
        }
        if (c == 'l') {
            if (!world.startState && !world.stats && !world.help) {//if game is at start screen
                ScoreBoard.promptLeaderBoard = !ScoreBoard.promptLeaderBoard;
                ScoreBoard.viewEasyLeaders = false;
                ScoreBoard.viewMediumLeaders = false;
                ScoreBoard.viewHardLeaders = false;
            }
        }
        if (!world.startState && ScoreBoard.promptLeaderBoard) {
            if (c == 'e') {
                if (!ScoreBoard.viewMediumLeaders && !ScoreBoard.viewHardLeaders) {
                    ScoreBoard.viewEasyLeaders = !ScoreBoard.viewEasyLeaders;
                }
            }
            if (c == 'm') {
                if (!ScoreBoard.viewEasyLeaders && !ScoreBoard.viewHardLeaders) {
                    ScoreBoard.viewMediumLeaders = !ScoreBoard.viewMediumLeaders;
                }
            }
            if (c == 'h') {
                if (!ScoreBoard.viewEasyLeaders && !ScoreBoard.viewMediumLeaders) {
                    ScoreBoard.viewHardLeaders = !ScoreBoard.viewHardLeaders;
                }
            }
        }

        if (c == KeyEvent.VK_ENTER) {
            if (world.end) {
                ScoreBoard.promptLeaderBoard = false;
                world = new World(WIDTH, HEIGHT);
            }
        }

        if (!world.startState) {
                if (!ScoreBoard.promptLeaderBoard) {
                    if (!world.help) {
                        if (c == 's') {
                            world.stats = !world.stats;
                        }
                        if (!world.stats) {
                            if (c == '1') {
                                world.difficulty = 1;
                                world.startState = true;
                            }
                            if (c == '2') {
                                world.difficulty = 2;
                                world.startState = true;
                            }
                            if (c == '3') {
                                world.difficulty = 3;
                                world.startState = true;
                            }
                        }
                    }

            }
        } else {
            if(world.isRunning) {
                if (c == 's' && world.flappy.shootAllowed && !world.paused) {
                    world.flappy.shoot = true;
                }
                if (c == 'p') {
                    world.paused = !world.paused;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Battle Star Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        Main mainInstance = new Main();
        frame.setContentPane(mainInstance);
        frame.pack();
        frame.setVisible(true);

        fileReader r = new fileReader();
        r.open();
        r.read();
        r.close();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        world.draw(g);
    }
}
