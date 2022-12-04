import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public abstract class Character {
    Pair position;
    Pair velocity;
    Pair acceleration;
    Color color;
    int width, height;
    boolean isAlive;
    final static Pair gravity = new Pair(0, 800);
    Rectangle rec;
    BufferedImage image;

    public Character() {
        isAlive = true;
        acceleration = new Pair(0, 0);
    }

    public void setVelocity(Pair v) {
        velocity = v;
    }

    public abstract void draw(Graphics g);

    public void update(World w, double time) {
        position = position.add(velocity.times(time));
        velocity = velocity.add(acceleration.times(time));
        rec = new Rectangle((int) position.x, (int) position.y, width, height);
        checkBorders(w);
    }

    protected abstract void checkBorders(World w);
}

class Pair {
    public double x;
    public double y;

    public Pair(double initX, double initY) {
        x = initX;
        y = initY;
    }

    public Pair add(Pair toAdd) {
        return new Pair(x + toAdd.x, y + toAdd.y);
    }

    public Pair divide(double denom) {
        return new Pair(x / denom, y / denom);
    }

    public Pair times(double val) {
        return new Pair(x * val, y * val);
    }

    public void flipX() {
        x = -x;
    }

    public void flipY() {
        y = -y;
    }
}
