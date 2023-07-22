import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNIT = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int[] x = new int[GAME_UNIT];
    final int[] y = new int[GAME_UNIT];

    int BodyParts = 6;
    int ApplesEaten;
    int AppleX;
    int AppleY;
    static char Direction = 'R';
    boolean Running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(0,0,0));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        StartGame();
    }

    public void StartGame(){
        NewApple();
        Running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        if(Running) {
            g.setColor(new Color(255, 0, 0));
            g.fillOval(AppleX, AppleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < BodyParts; i++) {
                if (i == 0) {
                    g.setColor(new Color(0, 255, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(33, 180, 0));
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255) , random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(new Color(255,0,0));
            g.setFont(new Font("INK Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score:"+ ApplesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score:"+ApplesEaten)) / 2,
                    g.getFont().getSize());

        }
        else{
            GameOver(g);
        }
    }
    public void NewApple(){
        AppleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        AppleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }
    public void move(){
        for(int i = BodyParts; i > 0; i--){
            x[i] = x[i - 1];
            y[i] = y[i -1];
        }


        switch (Direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }
    public void CheckApple(){
       if((x[0] == AppleX) && (y[0] == AppleY )){
           BodyParts++;
           ApplesEaten++;
           NewApple();
       }
    }
    
    public void CheckCollisions(){
        for(int i = BodyParts; i > 0; i--){
            if ((x[0] == x[i] && y[0] == y[i])) {
                Running = false;
                return;
            }
        }

        if(x[0] < 0 ){
            Running = false;
        }

        if (x[0] > SCREEN_WIDTH){
            Running = false;
        }

        if (y[0] < 0 ){
            Running = false;
        }

        if (y[0] > SCREEN_HEIGHT){
            Running = false;
        }

        if(!Running){
            timer.stop();

        }
    }
    public void GameOver(Graphics g){
        g.setColor(new Color(255,0,0));
        g.setFont(new Font("INK Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score:"+ ApplesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score:"+ApplesEaten)) / 2,
                g.getFont().getSize());


        g.setColor(new Color(255,0,0));
        g.setFont(new Font("INK Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(Running){
            move();
            CheckApple();
            CheckCollisions();
        }

        repaint();

    }


    public static class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if (Direction != 'R') {
                        Direction = 'L';
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if (Direction != 'L') {
                        Direction = 'R';
                    }
                }
                case KeyEvent.VK_UP -> {
                    if (Direction != 'D') {
                        Direction = 'U';
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if (Direction != 'U') {
                        Direction = 'D';
                    }
                }
            }
        }
    }
}
