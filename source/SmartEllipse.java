/**
 * SmartEllipse.java
 * Extends Java's Ellipse2D.Double class, adding the capabilities to
 * set color, rotation, location, and size, to move to a specified
 * location, and to display itself on a panel.
 */
import java.awt.*;
public class SmartEllipse extends java.awt.geom.Ellipse2D.Double {
    // Declare instance variables
    private Color _borderColor, _fillColor;    // attributes
    private int _rotation;
    private int _x;
    private int _y;
    private final int STROKE_WIDTH = 2;
    
    /** 
     * Constructor for the SmartEllipse class
     */
    public SmartEllipse(Color aColor){ 
	super();
        // solid color to start
        _borderColor = aColor;
        _fillColor = aColor;        
        // no rotation for now
        _rotation = 0;
	this.setSize(50,50);              
    }
 
    // methods not provided by Java
    public void setBorderColor (java.awt.Color aColor) {
    _borderColor = aColor;
    }
    public Color getBorderColor( ){
        return new Color(_borderColor.getRGB());
    }
    public void setFillColor (java.awt.Color aColor) {
    _fillColor = aColor;
    }
    public Color getFillColor( ){
        return new Color(_fillColor.getRGB());
    }
    public void setRotation (int aRotation)
    {
        _rotation = aRotation;
    }

    // more readable versions of methods provided by Java
    public void setLocation (double x, double y)
    {
        this.setFrame (x, y, this.getWidth(), this.getHeight());
    }
    public void setSize (int aWidth, int aHeight)
    {
        this.setFrame(this.getX(), this.getY(), aWidth, aHeight);
    }
    public void move (int aChangeInX, int aChangeInY)
    {
        this.setFrame((int)this.getX()+aChangeInX,(int)this.getY()+aChangeInY, this.getWidth(), this.getHeight());
    }

    /** 
     * Method fill will fill in the elliptical object with the fill color
     */
    public void fill (Graphics2D aBetterBrush){
        // Save the old color
        Color savedColor = aBetterBrush.getColor();
        // Change the color to the fill color
        aBetterBrush.setColor(_fillColor);
        // Paint a solid ellipse
        aBetterBrush.rotate(_rotation, this.getX(), this.getMinY());
        aBetterBrush.fill(this);
        aBetterBrush.rotate((-_rotation), this.getX(), this.getMinY());

        // Restore the old color
        aBetterBrush.setColor(savedColor);
    }

    /**
     * Method draw will draw an outline of the elliptical object with
     * the draw color
     */
    public void draw (Graphics2D aBrush) {
        // Save the old color
        Color savedColor = aBrush.getColor();
        // Change the color to the draw color
        aBrush.setColor(_borderColor);
        // Save the old stroke parameters
        Stroke savedStroke = aBrush.getStroke();
        // Instantiate a new Stroke with the desired parameters
        aBrush.setStroke(new BasicStroke(STROKE_WIDTH));
        // Draw the outline of the ellipse
        aBrush.rotate(Math.toRadians(_rotation), this.getX(), this.getMinY());
        aBrush.draw(this);
        aBrush.rotate(Math.toRadians(-_rotation), this.getX(), this.getMinY());

        // Restore the former settings for stroke and color
        aBrush.setStroke(savedStroke);
        aBrush.setColor(savedColor);
    }

/*    public void fill (java.awt.Graphics2D aBrush){
        java.awt.Color oldColor = aBrush.getColor();
        aBrush.setColor(_fillColor);
        aBrush.rotate(_rotation, this.getX()+(this.getWidth()/2), this.getY()+(this.getHeight()/2));
        aBrush.fill(this);
        aBrush.rotate(-_rotation, this.getX()+(this.getWidth()/2), this.getY()+(this.getHeight()/2));
        aBrush.setColor(oldColor);
    }
    public void draw (java.awt.Graphics2D aBrush) {
        java.awt.Color oldColor = aBrush.getColor();
        aBrush.setColor(_borderColor);
        java.awt.Stroke oldStroke = aBrush.getStroke();
        aBrush.setStroke(new java.awt.BasicStroke(STROKE_WIDTH));
        aBrush.rotate(_rotation, this.getX()+(this.getWidth()/2), this.getY()+(this.getHeight()/2));
        aBrush.draw(this);
        aBrush.rotate(-_rotation, this.getX()+(this.getWidth()/2), this.getY()+(this.getHeight()/2));
        aBrush.setStroke(oldStroke);
        aBrush.setColor(oldColor);
    }*/
}
