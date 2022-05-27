import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.geom.Point2D;
import java.lang.Math;

/**
 *
 * @author Kiera Wolfe, Sam Hunsley, Jack Schooley, and Raven Vella
 * @version 11/13/2019
 */
public class MissileLauncher implements Hitable
{
    private int _missileCount = 9;
    private Polygon _body;
    private final double X = 57;
    private final double Y = MissileConstants.BOARD_HEIGHT - 95;
    private ArrayList<SmartEllipse> _missilesArray;
    private double _offsetX = X;
    private double _offsetY = Y;
    private boolean _hit = false;

    public MissileLauncher(int k)
    {
        _body = new Polygon(Color.YELLOW);
        _missilesArray = new ArrayList<SmartEllipse>();
    }
    
    public Rectangle getArea()
    {
        return _body.getBounds();
    }
    
    public void ClearMissiles(){
        _missileCount =9;
        _missilesArray.clear();
    }

    public void hit()
    {
        _missileCount = 0;
        _missilesArray.clear();
        Disappear();
        _hit = true;
    }

    public boolean GotHit()
    {
        // returns if launcher has been hit
        return _hit;
    }
    
    public boolean contains(Point2D point){
        return _body.contains(point);
    }

    public void Disappear()
    {
        _body.setColor(Color.RED);
    }

    public void fill(java.awt.Graphics2D brush)
    {
        _body.fill(brush);
        Iterator<SmartEllipse> _iterator = _missilesArray.iterator();
        while (_iterator.hasNext())
            _iterator.next().fill(brush);
    }

    public void draw(java.awt.Graphics2D brush){
        _body.draw(brush);
        Iterator<SmartEllipse> _iterator = _missilesArray.iterator();
        while (_iterator.hasNext())
            _iterator.next().draw(brush);
    }

    public void setLocation(int x, int y)
    {
        int a = (MissileConstants.OBJECT_GAP * (x * 4 + 1)) + (x * MissileConstants.LAUNCHER_WIDTH) + (x * 3 * MissileConstants.CITY_WIDTH);
        int b = MissileConstants.BOARD_HEIGHT - MissileConstants.LAUNCHER_HEIGHT - 25;
        _offsetX += a - MissileConstants.OBJECT_GAP;

        _body.setLocation(a, b);
        createMissiles();
    }

    public int getLocationX()
    {
        return (int)_body.getBounds().getX();
    }

    public int getLocationY()
    {
        return (int)_body.getBounds().getY();
    }

    public void createMissiles()
    {
        for(int i = 0; i < _missileCount; i++)
        {
            SmartEllipse missile = new SmartEllipse(Color.RED);
            missile.setLocation(_offsetX, _offsetY);
            missile.setSize(MissileConstants.MISSILE_DIAMETER, MissileConstants.MISSILE_DIAMETER);

            _missilesArray.add(missile);

            if(i == 0) _offsetX -=  MissileConstants.MISSILE_OFFSET_X;
            else if(i == 3) _offsetX -= MissileConstants.MISSILE_OFFSET_X * i;
            else _offsetX += MissileConstants.MISSILE_OFFSET_X;

            if(i == 0 || i == 3)
                _offsetY += MissileConstants.MISSIEL_OFFSET_Y;
            if (i == _missileCount - 1)
            {
                _offsetX = X;
                _offsetY = Y;
            }
            
        }
    }

    public int getMissileCount(){
        return _missileCount;
    }

    public void decrementMissileCount(){
        Iterator<SmartEllipse> _iterator = _missilesArray.iterator();
        if (_iterator.hasNext() && _missileCount > 0)
        {
            SmartEllipse temp = _iterator.next();
            temp = null;
            _iterator.remove();
            _missileCount -= 1;
        }
    }
}