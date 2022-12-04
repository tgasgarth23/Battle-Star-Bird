import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

class World {
    int difficulty,height,width;
    private int colRate,colVelo,enemyRate,timePassed,score;
    private BufferedImage background, pauseIcon,exBrick,exEnemy,exFood;
    public Player flappy; //main character
    private ArrayList<Column> columns, scoreAdder; //scoreAdder is an invisible column right behind columns so that when bird hits it, the score increases
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private ArrayList<Food> foods;
    private Random rand;
    private int columnGap = 200; //distance between top and bottom columns
    public boolean isRunning,end,paused,stats,startState;
    public static ScoreBoard scores = new ScoreBoard();
    private int shootTimer = 0;
    private int enemySpeed = 200;
    private int numBullets = 5;
    private int reloadTime = 400;
    public boolean scoreSent = false;  //fileUpdate() is in update so this boolean stops fileUpdate() from constantly sending score on a loop after game is over
    public boolean help;
    public int startCount = 180;
    public int startTimer = 0;

    public World(int initWidth, int initHeight) {
        try { // https://stackoverflow.com/questions/21970879/java-cant-draw-an-image-from-file
            background = ImageIO.read(getClass().getResourceAsStream("/pictures/flappyBackgroundResized2.png"));
            pauseIcon = ImageIO.read(getClass().getResourceAsStream("/pictures/pause.png"));
            exBrick = ImageIO.read(getClass().getResourceAsStream("/pictures/Bricks.png"));
            exEnemy = ImageIO.read(getClass().getResourceAsStream("/pictures/enemybird.png"));
            exFood = ImageIO.read(getClass().getResourceAsStream("/pictures/worm.png"));
        } catch (IOException e) {
        }
        colRate = 150;
        enemyRate = 40;
        width = initWidth;
        height = initHeight;
        flappy = new Player();
        columns = new ArrayList<>();
        scoreAdder = new ArrayList<>();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        foods = new ArrayList<>();
        rand = new Random();
        isRunning = false;
        end = false;
        paused = false;
        stats = false;
        startState = false;
    }

    public void draw(Graphics g) {
        //for the start screen
        g.drawImage(background, 0, 0, Main.WIDTH, Main.HEIGHT, null);
        if (!startState){//!isRunning
            if (!stats) {
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.setColor(Color.GREEN);
                g.drawString("WELCOME TO BATTLE STAR BIRD", 160, 125);
                g.setColor(Color.BLACK);
                g.drawString("CHOOSE YOUR DIFFICULTY: ", 180, 200);
                g.drawString("EASY (PRESS 1)", 280, 250);
                g.drawString("MEDIUM (PRESS 2)", 280, 300);
                g.drawString("HARD (PRESS 3)", 280, 350);
                g.setColor(Color.WHITE);
                g.drawString("PRESS 'L' TO SEE LEADERBOARDS", 150, 450);
                g.drawString("PRESS 'S' FOR PLAYER STATS", 170, 500);
                g.drawString("PRESS 'C' FOR HELP/CONTROLS", 170, 550);
            } else {
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.setColor(Color.BLACK);
                g.drawString("Gamewide Average Scores: ", 180, 100);
                DecimalFormat df = new DecimalFormat("#.##");
                g.drawString("Easy: " + df.format(scores.averageScore(scores.easyScores)), 280, 150);
                g.drawString("Medium: " + df.format(scores.averageScore(scores.medScores)), 280, 200);
                g.drawString("Hard: " + df.format(scores.averageScore(scores.hardScores)), 280, 250);
                g.drawString(Main.user + "'s Average Scores: ", 180, 300);
                g.drawString("Easy: " + df.format(scores.averageScore(Main.user, scores.easyScores)), 280, 350);
                g.drawString("Medium: " + df.format(scores.averageScore(Main.user, scores.medScores)), 280, 400);
                g.drawString("Hard: " + df.format(scores.averageScore(Main.user, scores.hardScores)), 280, 450);
                g.drawString("PRESS 'S' TO RETURN", 215, 500);
            }
            if (help) {
                g.drawImage(background, 0, 0, Main.WIDTH, Main.HEIGHT, null);
                g.drawImage(exBrick, 600, 330, 35, 90, null);
                g.drawImage(exEnemy, 600, 210, 60, 60, null);
                g.drawImage(exFood, 550, 270, 60, 60, null);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.setColor(Color.BLACK);
                g.drawString("CONTROLS:", 275, 140);
                g.drawString("Press 'SPACEBAR' to jump", 100, 200);
                g.drawString("Press 'S' to shoot and kill enemies", 100, 260);
                g.drawString("Gain health by picking up food", 100, 320);
                g.drawString("Avoid hitting enemies or columns", 100, 380);
                g.drawString("Press 'P' to pause gameplay", 100, 440);
                g.drawString("PRESS 'C' TO RETURN", 215, 500);
            }
        }
        else if(!isRunning){//player has chosen difficulty but game hasn't started yet
            //show countdown
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.setColor(Color.WHITE);
            if(startCount%60==0){
                g.drawString("GAME STARTS IN: "+(1+(startCount-startTimer)/60), 100, Main.HEIGHT/2);
            }
        }
        if (ScoreBoard.promptLeaderBoard) {
            g.drawImage(background, 0, 0, Main.WIDTH, Main.HEIGHT, null);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.BLACK);
            g.drawString("PRESS E TO TOGGLE TOP 'EASY' SCORES", 100, 125);
            g.drawString("PRESS M TO TOGGLE TOP 'MEDIUM' SCORES", 100, 200);
            g.drawString("PRESS H TO TOGGLE TOP 'HARD' SCORES", 100, 275);
            g.drawString("PRESS 'L' TO RETURN TO START", 180, 400);
        }
        if (ScoreBoard.viewEasyLeaders) {
            drawScoreboard(g, scores.easyScores);
        } else if (ScoreBoard.viewMediumLeaders) {
            drawScoreboard(g, scores.medScores);
        } else if (ScoreBoard.viewHardLeaders) {
            drawScoreboard(g, scores.hardScores);
        }

        if (end && !ScoreBoard.promptLeaderBoard) {
            g.drawImage(background, 0, 0, Main.WIDTH, Main.HEIGHT, null);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.RED);
            g.drawString("YOU DIED", 335, 200);
            g.setColor(Color.YELLOW);
            g.drawString("FINAL SCORE:" + "   " + score, 275, 250);
            g.drawString("HIGH SCORE (DIFFICULTY " + difficulty + "): " + scores.getTop(difficulty).score, 275, 295);
            g.setColor(Color.WHITE);
            g.drawString("PRESS 'ENTER' TO RETURN", 200, 400);
        }

        if (flappy.isAlive && isRunning) {
            if (!paused) {
                g.drawImage(pauseIcon, Main.WIDTH - 50, 0, 50, 50, null);
                flappy.draw(g);
                for(int i=0;i<columns.size();i++){
                    columns.get(i).draw(g);
                }
                //Score counter
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.setColor(Color.WHITE);
                g.drawString("Score: " + score, 20, 50);
                //Health bar
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.setColor(Color.WHITE);
                g.drawString("Health: ", 20, 100);
                g.setColor(Color.BLUE);
                g.drawRect(150, 75, 100, 30);
                g.fillRect(150, 75, 20 * flappy.health, 30);

                //Bullet counter
                if (flappy.shootAllowed) {
                    g.setFont(new Font("Arial", Font.BOLD, 30));
                    g.setColor(Color.WHITE);
                    g.drawString("Bullets: " + (numBullets - flappy.bulletCount % numBullets), 20, 150);
                }

                //Reload bar
                if (!flappy.shootAllowed) {
                    g.setFont(new Font("Arial", Font.BOLD, 30));
                    g.setColor(Color.WHITE);
                    g.drawString("Reloading: ", 20, 150);
                    g.setColor(Color.RED);
                    g.drawRect(185, 125, 100, 30);
                    g.fillRect(185, 125, (shootTimer / (reloadTime / 100)), 30);
                }
                for(int i=0;i<bullets.size();i++){
                    if(bullets.get(i).isAlive) bullets.get(i).draw(g);
                }
                for(int i=0;i<enemies.size();i++){
                    enemies.get(i).draw(g);
                }
                for(int i=0;i<foods.size();i++){
                    if(foods.get(i).isAlive) foods.get(i).draw(g);
                }
            } else {
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.setColor(Color.WHITE);
                g.drawString("Press P to resume", 300, 125);
            }
        }
    }

    private void drawScoreboard(Graphics g, ArrayList<Score> scoreLeaders) {
        g.drawImage(background, 0, 0, Main.WIDTH, Main.HEIGHT, null);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.setColor(Color.darkGray);
        g.drawString("TOP 3 SCORERS ARE:", 100, 150);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        if (scoreLeaders.size() == 0) {
            g.drawString("1) -- : --", 160, 225);
            g.drawString("2) -- : --", 160, 275);
            g.drawString("3) -- : --", 160, 325);
        } else if (scoreLeaders.size() == 1) {
            g.drawString("1) " + scoreLeaders.get(0).name + ": " + scoreLeaders.get(0).score, 160, 225);
            g.drawString("2) -- : --", 160, 275);
            g.drawString("3) -- : --", 160, 325);
        } else if (scoreLeaders.size() == 2) {
            g.drawString("1) " + scoreLeaders.get(0).name + ": " + scoreLeaders.get(0).score, 160, 225);
            g.drawString("2) " + scoreLeaders.get(1).name + ": " + scoreLeaders.get(1).score, 160, 275);
            g.drawString("3) -- : --", 160, 325);
        } else {
            g.drawString("1) " + scoreLeaders.get(0).name + ": " + scoreLeaders.get(0).score, 160, 225);
            g.drawString("2) " + scoreLeaders.get(1).name + ": " + scoreLeaders.get(1).score, 160, 275);
            g.drawString("3) " + scoreLeaders.get(2).name + ": " + scoreLeaders.get(2).score, 160, 325);
        }
        g.setColor(Color.BLACK);
        g.drawString("PRESS 'L' TO RETURN TO START", 180, 400);
    }

    private void addBullets() {
        if (isRunning && !end) {//if during game play
            if (flappy.shootAllowed) {
                if (flappy.shoot) {
                    if (flappy.velocity.y > 0) {//if flappy is falling
                        Pair p = new Pair(flappy.position.x + flappy.radius + 20, flappy.position.y + 25);
                        bullets.add(new Bullet(p, (int) 200));//add a bullet at player position with y velocity 200
                    } else if (flappy.velocity.y == 0) {//flappy neither falling nor jumping
                        Pair p = new Pair(flappy.position.x + flappy.radius + 20, flappy.position.y + 15);
                        bullets.add(new Bullet(p, (int) 0));//add a bullet at player position with y velocity 0
                    } else {//flappy jumping
                        Pair p = new Pair(flappy.position.x + flappy.radius + 20, flappy.position.y + 15);
                        bullets.add(new Bullet(p, (int) -300));//add a bullet at player position with y velocity -300
                    }
                    flappy.shoot = false;
                    StdAudio.play("/sounds/ricochet_x.wav");
                    flappy.bulletCount++;
                    if (flappy.bulletCount % numBullets == 0 && flappy.bulletCount > 0) {//if flappy runs out of bullets
                        flappy.shootAllowed = false;
                        flappy.shoot = false;
                    }
                }
            }
            if (shootTimer == reloadTime) {//if reload time elapses
                flappy.shootAllowed = true;
                shootTimer = 0;
            }
        }
    }

    private void setDifficulty() {
        if (difficulty == 1) {   //easy difficulty parameters
            colVelo = -250;
            colRate = 200;
            enemyRate = 80;
            enemySpeed = 50;
            numBullets = 15;
            reloadTime = 400;
        }
        if (difficulty == 2) {  //medium difficulty parameters
            colVelo = -300;
            colRate = 150;
            enemyRate = 40;
            enemySpeed = 100;
            numBullets = 10;
            reloadTime = 200;
        }
        if (difficulty == 3) {  //hard difficulty parameters
            colVelo = -350;
            colRate = 40;
            enemyRate = 20;
            enemySpeed = 200;
            numBullets = 5;
            reloadTime = 100;
        }
    }

    private void addColumnsEnemies() {
        if (timePassed == colRate) {
            timePassed = 0;
            int topHeight = 100 + rand.nextInt(Main.HEIGHT / 2 - 100);//height of top column
            int bottomY = topHeight + columnGap;//y position of bottom column
            int bottomHeight = Main.HEIGHT - bottomY;//height of bottom column
            Pair topPos = new Pair(800, 0);//coordinates of top column
            Pair bottomPos = new Pair(800, bottomY);//coordinates of bottom column
            Pair middle = new Pair(topPos.x + 50, topHeight);//coordinates of invisible rectangle behind columns that add to score when hit
            Pair velocity = new Pair(colVelo, 0);
            if (columnGap > 120) {
                columns.add(new Column(topPos, velocity, topHeight));
                columns.add(new Column(bottomPos, velocity, bottomHeight));
                columnGap = columnGap - 5;  //makes the columns gradually get closer together
            } else {  //column gap stops decreasing at 50 so the bird can always get through
                columns.add(new Column(topPos, velocity, topHeight));
                columns.add(new Column(bottomPos, velocity, bottomHeight));
            }
            scoreAdder.add(new Column(middle, velocity, columnGap));
        }
        //Enemies spawn on screen every 40 time interval
        if (timePassed % enemyRate == 0 && timePassed % colRate != 0) {
            Pair toAdd = new Pair(800, 100 + rand.nextInt(Main.HEIGHT - 200));
            Pair velocity = new Pair(colVelo, 0);
            enemies.add(new Enemy(toAdd, velocity));
            double foodDeterminer = 200 * Math.random() + 201;//random number from 200 to 400
            if (foodDeterminer > Math.abs(colVelo)) {//food less likely to be generated with higher difficulty
                Pair foodPos = new Pair(800, 1 + rand.nextInt(Main.HEIGHT - 50));
                Pair foodVelo = new Pair(colVelo, 0);
                foods.add(new Food(foodPos, foodVelo));
            }
        }
    }

    private void enemyMovement() {
        for (int i = 0; i < enemies.size(); i++) {
            //below gets the enemy to slightly follow the bird's movement
            if (enemies.get(i).position.y < flappy.position.y && enemies.get(i).isAlive) {
                // enemy is above the player
                enemies.get(i).acceleration.y = enemySpeed;
            }
            if (enemies.get(i).position.y > flappy.position.y && enemies.get(i).isAlive) {
                // enemy is above the player
                enemies.get(i).acceleration.y = -enemySpeed;
            }
            if (!enemies.get(i).isAlive && enemies.get(i).position.y > height - enemies.get(i).height) {
                enemies.get(i).velocity.y = 0;
                enemies.get(i).acceleration = new Pair(0, 0);
            }
        }
    }
    //once game ends, organize information and append it to file
    private void fileUpdate() throws IOException {
        if (end && !scoreSent) {
            String name = Main.user;
            fileWriter a = new fileWriter();
            String info = "";
            info = Main.user + " " + score + " " + difficulty;
            a.writer(info);
            scoreSent = true;
            scores.addScore(name, score, difficulty);
        }
    }
    //3 second countdown before start of game
    private void startCountdown(){
        if(!isRunning && startState){
            startTimer++;
            if(startTimer>=startCount){//once 3 second count elapses
                isRunning = true;
            }
        }
    }

    private <T> void removeObjects(ArrayList<T> toCut, int index, boolean eval){
        if(!eval) toCut.remove(index);
    }
    //keeps track of gameplay events and conditions
    public void update(double time) throws IOException {
        startCountdown();
        if (isRunning && !paused) {
            flappy.update(this, time);
            timePassed++;
            setDifficulty();
            addColumnsEnemies();
            enemyMovement();
            addBullets();
            bulletCollisions();
            if (!flappy.shootAllowed) {
                shootTimer += 2;
            }
            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).update(this, time);
                removeObjects(bullets,i,bullets.get(i).isAlive);//remove bullets from list that are dead
            }

            for (int i = 0; i < columns.size(); i++) {
                columns.get(i).update(this, time);
                removeObjects(columns,i,columns.get(i).isAlive);//remove columns from list that are dead
            }
            for (int i = 0; i < scoreAdder.size(); i++) {
                scoreAdder.get(i).update(this, time);
                removeObjects(scoreAdder,i,scoreAdder.get(i).isAlive);
            }
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).update(this, time);
                removeObjects(enemies,i,enemies.get(i).inWindow);//remove enemies from list that are off screen
            }
            for (int i = 0; i < foods.size(); i++) {
                foods.get(i).update(this, time);
                removeObjects(foods,i,foods.get(i).isAlive);
            }
            isBirdAlive();
            scoreChanger();
            fileUpdate();
        }
    }

    private void bulletCollisions() {
        for(int i=0;i<enemies.size();i++){//bullets hitting enemies kills both enemies and bullets
            for(int j=0;j<bullets.size();j++){
                if(bullets.get(j).rec.intersects(enemies.get(i).rec)&&enemies.get(i).isAlive&&bullets.get(j).isAlive){
                    enemies.get(i).isAlive = false;
                    bullets.get(j).isAlive = false;
                    StdAudio.play("/sounds/floop.wav");
                }
            }
        }
        for(int i=0;i<columns.size();i++){//bullets hitting columns kills bullets
            for(int j=0;j<bullets.size();j++){
                if(columns.get(i).rec.intersects(bullets.get(j).rec)){
                    bullets.get(j).isAlive = false;
                }
            }
        }
    }

    private void isBirdAlive() {
        //if bird hits columns, its health becomes 0
        for (Column column : columns) {
            if (flappy.rec.intersects(column.rec) && flappy.isAlive) {
                flappy.health = 0;
            }
        }
        //if bird hits enemies, its health decreases by 1
        for(int i=0;i<enemies.size();i++){
            if(flappy.rec.intersects(enemies.get(i).rec) && flappy.isAlive && enemies.get(i).isAlive) {
                enemies.get(i).isAlive = false;
                flappy.health--;
                StdAudio.play("/sounds/pluck.wav");
            }
        }
        //if bird hits food, its health increases by 1
        for (int i=0;i<foods.size();i++) {
            if (flappy.rec.intersects(foods.get(i).rec) && flappy.isAlive && foods.get(i).isAlive) {
                foods.get(i).isAlive = false;
                if (flappy.health < 5 && flappy.health > 0) {
                    flappy.health++;
                }
            }
        }
        //bird dies when health is 0
        if (flappy.health <= 0 && !end) {
            StdAudio.play("/sounds/whah_whah.wav");
            flappy.isAlive = false;
            end = true;
            //isRunning = false;
        }
    }

    private void scoreChanger() {
        //each time bird hits invisible rectangle behind green columns, score increases by 1
        for (int i=0;i<scoreAdder.size();i++) {
            if (flappy.rec.intersects(scoreAdder.get(i).rec) && flappy.isAlive && scoreAdder.get(i).isAlive) {
                score++;
                StdAudio.play("/sounds/blip.wav");
                scoreAdder.get(i).isAlive = false;
            }
        }
    }
}