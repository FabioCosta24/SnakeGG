import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;

    static final int SCREEN_HEIGHT = 600;

    static final int CELL_SIZE =25;

    static final int GRID = (SCREEN_WIDTH* SCREEN_HEIGHT)/CELL_SIZE;

    static final int DELAY = 75;

    //getting position on grid
    final int x[] = new int[GRID];

    final int y[] = new int[GRID];

    //snake starting size
    int bodyParts = 6;

    int applesEaten;

    //coordinates where apple will spawn
    int appleX;

    int appleY;

    //direction where snake spawns to U->Up __ D->Down __ R->Right __ L->Left
    char direction = 'R';

    boolean running = false;

    Timer timer;

    Random random;
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }
    //Actions performed for game to start
    public void startGame(){
        createApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw (Graphics g){
        if(running) {
            //Grid for guidance while making the game, can be deleted or hiden
            for (int i = 0; i < SCREEN_HEIGHT / CELL_SIZE; i++) {
                g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * CELL_SIZE, SCREEN_WIDTH, i * CELL_SIZE);
            }
            //Drawing apple process
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, CELL_SIZE, CELL_SIZE);

            //Drawing snake, the if statement separates head from body
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], CELL_SIZE, CELL_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], CELL_SIZE, CELL_SIZE);
                }

            }
            g.setColor(Color.RED);
            g.setFont(new Font("Helvetica", Font.BOLD,20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten , (SCREEN_WIDTH -metrics.stringWidth("Score: " + applesEaten))/2,CELL_SIZE);
        }
        else {
            gameOver(g);
        }
    }
    //Set Position for a new apple to spawn
    public void createApple(){
        appleX = random.nextInt((int) (SCREEN_WIDTH/CELL_SIZE))*CELL_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT /CELL_SIZE))*CELL_SIZE;

    }

    public  void move(){
        //Loop that goes by the snake body parts so we can "map" them
        for (int i = bodyParts; i > 0 ; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        //defining position heading
        switch (direction){
            case 'U':
                y[0] = y[0] - CELL_SIZE;
                break;
            case 'D':
                y[0] = y[0] + CELL_SIZE;
                break;
            case 'L':
                x[0] = x[0] - CELL_SIZE;
                break;
            case 'R':
                x[0] = x[0] + CELL_SIZE;
                break;
        }

    }

// Engine for apples o be eaten
    public void checkApple(){
        if ((x[0] == appleX) && (y[0] == appleY)){
            bodyParts ++;
            applesEaten ++;
            createApple();
        }

    }
    public void checkCollisions(){
        // check if snake head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])){
               running = false;
            }
        }
        //Check if head collides left border
        if (x[0] < 0){
             running = false;
        }
        //Check if head collides right border
        if (x[0] > SCREEN_WIDTH - CELL_SIZE){
            running = false;
        }
        //Check if head collides top boarder
        if (y[0] < 0){
            running = false;
        }
        //Check if head collides bottom boarder
        if (y[0] > SCREEN_HEIGHT- CELL_SIZE){
            running = false;
        }
        if (!running){
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        //Displays Score on Game over screen
        g.setColor(Color.RED);
        g.setFont(new Font("Helvetica", Font.BOLD,20));
        FontMetrics scoreMetrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten , (SCREEN_WIDTH -scoreMetrics.stringWidth("Score: " + applesEaten))/2,CELL_SIZE);
        //Displays Game Over
        g.setColor(Color.RED);
        g.setFont(new Font("Helvetica", Font.BOLD,40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over" , (SCREEN_WIDTH -metrics.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public  class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            //Avoiding 360º turns and making snaking move
            switch (e.getKeyCode()){
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 'R'){
                        direction = 'L';
                        break;
                    }
                case KeyEvent.VK_UP:
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }

        }
    }
}
