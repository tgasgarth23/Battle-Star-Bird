import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Enemy extends Character {
    public boolean inWindow;
    public Enemy(Pair p, Pair v){
        try { // https://stackoverflow.com/questions/21970879/java-cant-draw-an-image-from-file
            image = ImageIO.read(getClass().getResourceAsStream("/pictures/enemybird.png")); // importing the image
        }
        catch (IOException e){}
        Random rand = new Random();
        position = p;
        width = height = 80;
        velocity = v;
        color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        rec = new Rectangle((int) position.x,(int)position.y,width,height);
        inWindow = true;
    }
    @Override
    public void draw(Graphics g) {
        if(isAlive)g.drawImage(this.image, (int) position.x, (int)position.y, (int)(width ), (int)(height), null  );
        if(!isAlive){
            BufferedImage image = null;
            try { // https://stackoverflow.com/questions/21970879/java-cant-draw-an-image-from-file
                image = ImageIO.read(getClass().getResourceAsStream("/pictures/Explosion.png"));
            }
            catch (IOException e){}
            g.drawImage(image, (int)position.x, (int)position.y, height, width, null);
        }
    }

    //when enemies hit top or bottom of screen, they switch directions
    @Override
    public void checkBorders(World w){
        if (position.y < 1) {
            velocity.flipY();
        } else if (position.y + height > w.height) {
            velocity.flipY();
        }
        if(position.x<-width){
            inWindow = false;
        }
    }
    @Override
    public void update(World w, double time){
        super.update(w, time);
        if (!isAlive) {
            acceleration = new Pair(0, 1000);
        }
    }
}
