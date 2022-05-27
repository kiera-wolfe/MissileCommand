import javafx.scene.shape.Line;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Random;

public class EnemyMissiles {
    private Random _generator;
    private int _start;
    private int _endX;
    private int _endY;
    private int _offsetX;
    private int _shift;
    private int _increase = 5;
    private Line2D.Double _missile;

    public EnemyMissiles(int x, int y)
    {
        _generator = new Random();
        _start = (_generator.nextInt(MissileConstants.BOARD_WIDTH));
        _missile = new Line2D.Double(_start, 0, _start, 0);
        _endX = x;
        _endY = y;
        _offsetX = _start - _endX;
        _shift = _generator.nextInt(5);
        if(_shift == 0) _shift ++;
        int temp = _generator.nextInt(2);
        if(temp == 0) _shift *= -1;
    }
    
    public boolean onscreen(){
        if (_missile.getY2() < (MissileConstants.BOARD_HEIGHT - 50) && _missile.getX2() > 0 && _missile.getX2() < MissileConstants.BOARD_WIDTH){
            return true;
        } else {
            return false;
        }
    }

    public void fill(java.awt.Graphics2D brush)
    {
        brush.setColor(Color.GREEN);
        brush.setStroke(new BasicStroke(3));
        brush.draw(_missile);
    }

    public void draw(java.awt.Graphics2D brush)
    {
        brush.setColor(Color.RED);
        brush.setStroke(new BasicStroke(3));
        brush.draw(_missile);
    }

    public void move()
    {
        if(_missile.getY2() <= MissileConstants.BOARD_HEIGHT + 1)
        {
            double x = _missile.getX2() + _shift;
            _missile.setLine(_start, 0, x, _missile.getY2() + _increase);
        }
    }

    public Point2D getArea()
    {
        return _missile.getP2();
    }

    public double getX(){
        return _missile.getX2();
    }
    
    public double getY(){
        return _missile.getY2();
    }
}
