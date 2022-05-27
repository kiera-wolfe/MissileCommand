/**
 * Chapter 9: Polygon.java
 * Adds capabilities to the Java2D.Double ellipse.
 * Same as the class defined in Chapter 7.
 */
public class Polygon extends java.awt.Polygon {
    private java.awt.Color _borderColor, _fillColor;
    private double _rotation;
    private final int STROKE_WIDTH = 2;

    public Polygon(java.awt.Color aColor){
        _borderColor = aColor;
        _fillColor = aColor; // solid color to start
        _rotation = 0;
    }

    // methods not provided by Java
    public void setBorderColor (java.awt.Color aColor) {
        _borderColor = aColor;
    }
    public void setFillColor (java.awt.Color aColor) {
        _fillColor = aColor;
    }
    public void setColor (java.awt.Color aColor) {
        _borderColor = aColor;
        _fillColor = aColor;
    }
    public void setLocation(int x, int y){
        this.addPoint(x, MissileConstants.LAUNCHER_HEIGHT+y);
        this.addPoint(MissileConstants.LAUNCHER_WIDTH+x, MissileConstants.LAUNCHER_HEIGHT+y);
        this.addPoint((MissileConstants.LAUNCHER_WIDTH/2)+x, y);
    }

    // not provided by Java
    public void fill (java.awt.Graphics2D aBrush){
        java.awt.Color oldColor = aBrush.getColor();
        aBrush.setColor(_fillColor);
        aBrush.fill(this);
        aBrush.setColor(oldColor);
    }
    public void draw (java.awt.Graphics2D aBrush) {
        java.awt.Color oldColor = aBrush.getColor();
        aBrush.setColor(_borderColor);
        java.awt.Stroke oldStroke = aBrush.getStroke();
        aBrush.setStroke(new java.awt.BasicStroke(STROKE_WIDTH));
        aBrush.draw(this);
        aBrush.setStroke(oldStroke);
        aBrush.setColor(oldColor);
    }
}