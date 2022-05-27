import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Kiera Wolfe, Sam Hunsley, Jack Schooley, and Raven Vella
 * @version 11/13/2019
 */
public class City implements Hitable
{
    private boolean _hit = false;
    SmartRectangle _city;

    public City()
    {
        // initialise instance variables
        // create shape objects that make up city object
        _city = new SmartRectangle(Color.BLUE);
    }
    
    public boolean GotHit(){
        return _hit;
    }

    public void hit()
    {
        Disappear();
        _hit = true;
    }

     public void Disappear()
    {
        // city goes out of commission when hit (set its color to ground color)
        _city.setColor(Color.RED);
    }
    
    public void fill(java.awt.Graphics2D brush){
        // fill in color for the shapes that make up the city object
        _city.fill(brush);
        draw(brush);
    }
    
    public void draw(java.awt.Graphics2D brush){
        // draw the shape objects to the panel
        _city.draw(brush);
    }
    
    public void setLocation(int x, int y){
        // set the location of the shapes that make up the city (where location is based off top left corner of object)
        _city.setLocation(x, y);
        _city.setSize((int)MissileConstants.CITY_WIDTH, (int)MissileConstants.CITY_HEIGHT);
    }

    public int getLocationX()
    {
        return (int)_city.getX();
    }

    public int getLocationY()
    {
        return (int)_city.getY();
    }
    
    public Rectangle getArea()
    {
        return _city.getBounds();
    }
}
