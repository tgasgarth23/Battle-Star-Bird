import java.awt.*;

public class Bullet extends Character {
    int radius;
    public Bullet(Pair p, int velo) {
        position = p;
        velocity = new Pair(400,velo);
        radius = 5;
        width = radius*2;
        height = radius*2;
        color = Color.red;
        rec = new Rectangle((int) position.x,(int)position.y,width,height);
    }
    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval((int) (this.position.x), (int) this.position.y, this.width, this.height);
    }

    @Override
    public void checkBorders(World w) {
        if(position.x>w.width){//if bullet goes past right side of window
            isAlive = false;
        }
        if(position.y>w.height || position.y<0){//if bullet goes past top or bottom of window
            isAlive = false;
        }
    }
}
