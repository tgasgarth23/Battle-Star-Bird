import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Column extends Character {
    public Column(Pair p, Pair v, int h){
        this.height = h;
        try { // https://stackoverflow.com/questions/21970879/java-cant-draw-an-image-from-file
            image = ImageIO.read(getClass().getResourceAsStream("/pictures/Bricks.png")); // importing the image
        }
        catch (IOException e){}
        position = p;
        velocity = v;
        width = 50;
        acceleration = new Pair(0,0);
        color = Color.GREEN;
        isAlive = true;
        rec = new Rectangle((int) this.position.x,(int)this.position.y,width,this.height );
    }
    @Override
    public void draw(Graphics g) {
        //Getting cropped image
        //https://stackoverflow.com/questions/2386064/how-do-i-crop-an-image-in-javaolor
        BufferedImage dest = image.getSubimage(0, 0, width, height);
        g.drawImage(dest, (int)this.position.x, (int)this.position.y, null);
        g.setColor(this.color);
    }
    @Override
    public void checkBorders(World w){
        if (position.x < -width){//if column passes left side of window
            isAlive = false;
        }
    };
}
