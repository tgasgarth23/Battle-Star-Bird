import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

class Player extends Character{
    BufferedImage imageUp;
    BufferedImage imageDown;
    int radius;
    int health, bulletCount;
    boolean shoot, shootAllowed;//shootAllowed false during reload time, shoot true when player inputs shoot command

    public Player() {
        position = new Pair(200.0, 300.0);
        velocity = new Pair(0,0);
        acceleration = gravity;
        radius = 25;
        width = radius*2;
        height = radius*2;
        color = Color.orange;
        rec = new Rectangle((int) position.x,(int)position.y,width,height);
        health = 5;
        bulletCount = 0;
        shoot = false;
        shootAllowed = true;
        try { // https://stackoverflow.com/questions/21970879/java-cant-draw-an-image-from-file
            image = ImageIO.read(getClass().getResourceAsStream("/pictures/birddown.png"));
            imageDown = ImageIO.read(getClass().getResourceAsStream("/pictures/birddown.png")); // importing the image
            imageUp = ImageIO.read(getClass().getResourceAsStream("/pictures/birdup.png"));
        }
        catch (IOException e){}
    }
    @Override
    public void draw(Graphics g){
        //Color c = g.getColor();
        if(this.velocity.y < 0){
            image = imageUp;
        }
        else if(this.velocity.y >= 0){
            image = imageDown;
        }
        g.drawImage(this.image, (int) position.x, (int)position.y, radius * 2, radius *2, null);
    }

    public void jump(){
        Pair toAdd = new Pair(0, -300);
        setVelocity(toAdd);
    }

    //prevents bird from flying over screen, and if bird hits ground it dies
    @Override
    public void checkBorders(World w) {
        if (position.y < 1) {
            velocity.y = 0;
            position.y = 1;
        } else if (position.y + radius > w.height) {
            velocity.y = 0;
            health = 0;
            isAlive = false;
            position.y = w.height - radius;
        }
    }

}