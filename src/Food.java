import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class Food extends Character {
    public Food(Pair p, Pair v){
        try { // https://stackoverflow.com/questions/21970879/java-cant-draw-an-image-from-file
            image = ImageIO.read(getClass().getResourceAsStream("/pictures/worm.png")); // importing the image
        }
        catch (IOException e){}
        position = p;
        width = height = 50;
        velocity = v;
        color = Color.white;
        rec = new Rectangle((int) position.x,(int)position.y,width,height);
    }
    @Override
    public void draw(Graphics g) {
        g.drawImage(this.image, (int) position.x, (int)position.y, (int)(width ), (int)(height), null  );
    }

    //when food hit top or bottom of screen, they switch directions
    @Override
    public void checkBorders(World w){
        if (position.y < 1) {
            velocity.flipY();
        } else if (position.y + height > w.height-1) {
            velocity.flipY();
        }
        if(position.x<-width){//if food passes left side of window
            isAlive = false;
        }
    }
}
