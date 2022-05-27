import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;
import java.awt.Color;
import java.util.TimerTask;
import java.awt.geom.*;

/**
 *
 * @author Kiera Wolfe, Sam Hunsley, Jack Schooley, and Raven Vella
 * @version 11/13/2019
 */
public class GamePanel extends JPanel implements ActionListener
{

    // instance variables
    private static javax.swing.Timer _timer;
    private java.util.Timer _explosionTimer;
    private TimerTask _explode;
    private java.util.Timer _enemyMissileTimer;
    private TimerTask _shoot;
    private Random _generator;
    private PausePanel pausePanel = new PausePanel();
    private SmartRectangle _ground;
    private boolean _paused = false;

    private KeyUpListener _upKey;
    private KeyLeftListener _leftKey;
    private KeyRightListener _rightKey;
    private KeyPListener _pauseKey;
    private MouseListener _cursor;
    private ArrayList<EnemyMissiles> _enemyMissilesArray;
    private ArrayList<SmartEllipse> _explosionsArray;
    private ArrayList<City> _cityArray;
    private ArrayList<MissileLauncher> _launcherArray;
    private MissileLauncher _leftLauncher, _centerLauncher, _rightLauncher;
    private City _city1, _city2, _city3, _city4, _city5, _city6;
    private EnemyMissiles _missile;
    private double _xCursorPosition, _yCursorPosition;
    private int _total = MissileConstants.NUM_MISSILES;
    private int _leftover;
    private int _level = 0;

    /**
     * Constructor for objects of class GamePanel
     */
    public GamePanel()
    {
        // initialise instance variables
        this.setBackground(Color.BLACK);
        this.setSize(MissileConstants.BOARD_WIDTH, MissileConstants.BOARD_HEIGHT);

        _leftover = 0;

        _upKey = new KeyUpListener(this);
        _leftKey = new KeyLeftListener(this);
        _rightKey = new KeyRightListener(this);
        _pauseKey = new KeyPListener(this);
        _cursor = new MouseListener(this);

        _enemyMissilesArray = new ArrayList<EnemyMissiles>();
        _explosionsArray = new ArrayList<SmartEllipse>();
        _cityArray = new ArrayList<City>();
        _launcherArray = new ArrayList<MissileLauncher>();

        _leftLauncher = new MissileLauncher(0);
        _leftLauncher.setLocation(0, 0);
        _launcherArray.add(_leftLauncher);
        _centerLauncher = new MissileLauncher(1);
        _centerLauncher.setLocation(1, 1);
        _launcherArray.add(_centerLauncher);
        _rightLauncher = new MissileLauncher(2);
        _rightLauncher.setLocation(2, 2);
        _launcherArray.add(_rightLauncher);

        _ground = new SmartRectangle(Color.YELLOW);
        _ground.setSize(MissileConstants.BOARD_WIDTH, 50);
        _ground.setLocation(0, (double)MissileConstants.BOARD_HEIGHT - 50);
        _city1 = new City();
        _city1.setLocation((MissileConstants.OBJECT_GAP*2 + MissileConstants.LAUNCHER_WIDTH), (MissileConstants.BOARD_HEIGHT - MissileConstants.CITY_HEIGHT - 25));
        _cityArray.add(_city1);
        _city2 = new City();
        _city2.setLocation((MissileConstants.OBJECT_GAP*3 + MissileConstants.LAUNCHER_WIDTH + MissileConstants.CITY_WIDTH), (MissileConstants.BOARD_HEIGHT - MissileConstants.CITY_HEIGHT - 25));
        _cityArray.add(_city2);
        _city3 = new City();
        _city3.setLocation((MissileConstants.OBJECT_GAP*4 + MissileConstants.LAUNCHER_WIDTH + MissileConstants.CITY_WIDTH*2), (MissileConstants.BOARD_HEIGHT - MissileConstants.CITY_HEIGHT - 25));
        _cityArray.add(_city3);
        _city4 = new City();
        _city4.setLocation((MissileConstants.OBJECT_GAP*6 + MissileConstants.LAUNCHER_WIDTH*2 + MissileConstants.CITY_WIDTH*3), (MissileConstants.BOARD_HEIGHT - MissileConstants.CITY_HEIGHT - 25));
        _cityArray.add(_city4);
        _city5 = new City();
        _city5.setLocation((MissileConstants.OBJECT_GAP*7 + MissileConstants.LAUNCHER_WIDTH*2 + MissileConstants.CITY_WIDTH*4), (MissileConstants.BOARD_HEIGHT - MissileConstants.CITY_HEIGHT - 25));
        _cityArray.add(_city5);
        _city6 = new City();
        _city6.setLocation((MissileConstants.OBJECT_GAP*8 + MissileConstants.LAUNCHER_WIDTH*2 + MissileConstants.CITY_WIDTH*5), (MissileConstants.BOARD_HEIGHT - MissileConstants.CITY_HEIGHT - 25));
        _cityArray.add(_city6);

        _generator = new Random();

        _timer = new javax.swing.Timer(MissileConstants.TIMER_SPEED, this);
        _timer.start();

        //this controls the explosions' time on screen
        _explosionTimer = new java.util.Timer();
        _explode = new ExplosionHelper();
        _explosionTimer.schedule(_explode, 1000, 1200);

        //this controls the missiles' movement on screen
        _enemyMissileTimer = new java.util.Timer();
        _shoot = new EnemyMissileHelper();
        _enemyMissileTimer.schedule(_shoot, 700, 500);

        addMouseMotionListener(_cursor);
    }

    public void paintComponent (java.awt.Graphics aBrush)
    {
        super.paintComponent(aBrush);
        java.awt.Graphics2D aBetterBrush = (java.awt.Graphics2D)aBrush;
        SmartEllipse nextE;

        Iterator<EnemyMissiles> _iterator = _enemyMissilesArray.iterator();
        while (_iterator.hasNext())
            _iterator.next().fill(aBetterBrush);
            
        _ground.fill(aBetterBrush);
        _leftLauncher.fill(aBetterBrush);
        _centerLauncher.fill(aBetterBrush);
        _rightLauncher.fill(aBetterBrush);
        _city1.fill(aBetterBrush);
        _city2.fill(aBetterBrush);
        _city3.fill(aBetterBrush);
        _city4.fill(aBetterBrush);
        _city5.fill(aBetterBrush);
        _city6.fill(aBetterBrush);

        
        Iterator<SmartEllipse> _iterator2 = _explosionsArray.iterator();
        while (_iterator2.hasNext()){
            nextE = _iterator2.next();
            nextE.fill(aBetterBrush);
            nextE.draw(aBetterBrush);

        }
    }

    /**
     * This method creates an explosion SmartEllipse when a defender missile is launched (an arrow key is pressed).
     * It calls removeMissile on the missile objects that are in the range of the explosion and the original
     * missile that was hit.
     *
     * @param i - x location of mouse location
     * @param j - y location of mouse location
     *
     *
     */
    private void explosion(double i, double j)
    {
        // create explosion around cursor location
        SmartEllipse explosion = new SmartEllipse(Color.RED);
        explosion.setBorderColor(Color.YELLOW);
        explosion.setLocation(i-20, j-15);
        explosion.setSize(40, 30);
        _explosionsArray.add(explosion);

        for (int k=0; k < _enemyMissilesArray.size(); k++)
        {
            EnemyMissiles missile = _enemyMissilesArray.get(k);
            if (checkCollision(missile, explosion))
            {
                removeMissile(missile);
                k--;
            }
        }
    }

    private void onscreen(){
        for (int k=0; k < _enemyMissilesArray.size(); k++){
            EnemyMissiles ourmissile = _enemyMissilesArray.get(k);
            if (!ourmissile.onscreen()){
                _enemyMissilesArray.remove(ourmissile);
            }
        }
    }

    private void attack()
    {
        for (int k=0; k < _enemyMissilesArray.size(); k++)
        {
            EnemyMissiles missile = _enemyMissilesArray.get(k);
            for (int l=0; l < _cityArray.size(); l++){
                City ourcity = _cityArray.get(l);
                if (checkCollision2(ourcity, missile))
                {
                    _cityArray.get(l).hit();
                    _cityArray.remove(_cityArray.get(l));
                    this.explosion(missile.getX(), missile.getY());
                    l--;
                }
            }
            for (int t=0; t < _launcherArray.size(); t++){
                MissileLauncher ourlauncher = _launcherArray.get(t);
                if (checkCollision3(ourlauncher, missile))
                {
                    _launcherArray.get(t).hit();
                    _launcherArray.remove(_launcherArray.get(t));
                    this.explosion(missile.getX(), missile.getY());
                    t--;
                }
            }
        }
    }

    public boolean checkCollision(EnemyMissiles shapeA, Shape shapeB)
    {
        Point2D areaA = shapeA.getArea();
        Area areaB = new Area(shapeB.getBounds());
        return areaB.contains(areaA);
    }

    public boolean checkCollision2(City shapeA, EnemyMissiles shapeB)
    {
        Area areaA = new Area(shapeA.getArea());
        Point2D point = shapeB.getArea();
        return areaA.contains(point);
    }

    public boolean checkCollision3(MissileLauncher shapeA, EnemyMissiles shapeB)
    {
        Point2D point = shapeB.getArea();
        return shapeA.contains(point);
    }

    /**
     * This method removes a missile from the missile array when it is hit or caught in an explosion zone.
     *
     * @param missile - a missile to be removed
     *
     */
    public void removeMissile(EnemyMissiles missile)
    {
        _enemyMissilesArray.remove(missile);
    }

    /**
     * This method checks to see if a level has ended.
     *
     * @return boolean This function returns whether the level is over or not.
     */
    private boolean checkEndOfLevel()
    {
        if(_total == 0 && _enemyMissilesArray.size() == 0 && _leftover != 0)
        {
            return true;
        }
        else if (_cityArray.size() == 0 || _launcherArray.size() == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void GameOver(int points){
        _timer.stop();
        int x = JOptionPane.showConfirmDialog(this, 
                "Game Over! Your points: " + points, "Comfirm!",
                JOptionPane.YES_NO_OPTION);
        if(x == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /**
     * This method sets up the next level.
     *
     * @return void
     */
    private void BeginNextLevel(int points)
    {
        _timer.stop();
        int x = JOptionPane.showConfirmDialog(this, 
                "Next Level! Your points: " + points, "Comfirm!",
                JOptionPane.YES_NO_OPTION);
        if(x == JOptionPane.NO_OPTION) {
            System.exit(0);
        } else {
            MissileConstants.TIMER_SPEED = MissileConstants.TIMER_SPEED*2;
            _launcherArray.clear();
            if (! _leftLauncher.GotHit())
            {
                _leftLauncher = new MissileLauncher(0);
                _leftLauncher.setLocation(0, 0);
                _launcherArray.add(_leftLauncher);
            }
            if (! _centerLauncher.GotHit())
            {
                _centerLauncher = new MissileLauncher(1);
                _centerLauncher.setLocation(1, 1);
                _launcherArray.add(_centerLauncher);
            }
            if (! _rightLauncher.GotHit())
            {
                _rightLauncher = new MissileLauncher(2);
                _rightLauncher.setLocation(2,2);
                _launcherArray.add(_rightLauncher);
            }
           /* for (int k=0; k < _launcherArray.size();k++){
                _launcherArray.get(k).ClearMissiles();
                _launcherArray.get(k).createMissiles();
            }*/
            _generator = new Random();
            _level ++;
            _total = MissileConstants.NUM_MISSILES + _level;
            _timer = new javax.swing.Timer(MissileConstants.TIMER_SPEED, this);
            _timer.start();
            generateEnemy();
        }

    }

    /**
     * This method tallies the points from the current state of the game.
     *
     * @return int
     */
    private int tallyPoints()
    {
        for (int k=0; k < _launcherArray.size(); k++){
            _leftover = _leftover + _launcherArray.get(k).getMissileCount();
        }
        if (_leftover == 1){
            return MissileConstants.NUM_MISSILES - 27 + _leftover - 1 + _cityArray.size()*10;
        } else {
            return MissileConstants.NUM_MISSILES - 27 + _leftover + _cityArray.size()*10;
        }

    }

    /**
     * This method generates enemy missiles.
     *
     * @return void
     */
    private void generateEnemy()
    {
        int locationX = 0, locationY = 0;
        switch (_generator.nextInt(9)) {
            case 0:
            locationX = _leftLauncher.getLocationX();
            locationY = _leftLauncher.getLocationY();
            case 1:
            locationX = _city1.getLocationX();
            locationY = _city1.getLocationY();
            case 2:
            locationX = _city2.getLocationX();
            locationY = _city2.getLocationY();
            case 3:
            locationX = _city3.getLocationX();
            locationY = _city3.getLocationY();
            case 4:
            locationX = _centerLauncher.getLocationX();
            locationY = _centerLauncher.getLocationY();
            case 5:
            locationX = _city4.getLocationX();
            locationY = _city4.getLocationY();
            case 6:
            locationX = _city5.getLocationX();
            locationY = _city5.getLocationY();
            case 7:
            locationX = _city6.getLocationX();
            locationY = _city6.getLocationY();
            case 8:
            locationX = _rightLauncher.getLocationX();
            locationY = _rightLauncher.getLocationY();

            _missile = new EnemyMissiles(locationX, locationY);
            _enemyMissilesArray.add(_missile);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        repaint();
    }

    private class KeyUpListener extends KeyInteractor
    {
        public KeyUpListener(JPanel p)
        {
            super(p,KeyEvent.VK_UP);
        }

        public  void actionPerformed (ActionEvent e) {
            if(_timer.isRunning() && !checkEndOfLevel())
            {
                if (_centerLauncher.getMissileCount() > 0){
                    explosion(_xCursorPosition, _yCursorPosition);
                    _centerLauncher.decrementMissileCount();
                    repaint();
                }
            }
        }
    }
    private class KeyLeftListener extends KeyInteractor
    {
        public KeyLeftListener(JPanel p)
        {
            super(p,KeyEvent.VK_LEFT);
        }

        public void actionPerformed (ActionEvent e)
        {
            if(_timer.isRunning() && !checkEndOfLevel())
            {
                if (_leftLauncher.getMissileCount() > 0){
                    explosion(_xCursorPosition, _yCursorPosition);
                    _leftLauncher.decrementMissileCount();
                    repaint();
                }
            }
        }
    }
    private class KeyRightListener extends KeyInteractor
    {
        public KeyRightListener(JPanel p)
        {
            super(p,KeyEvent.VK_RIGHT);
        }

        public void actionPerformed (ActionEvent e) {
            if(_timer.isRunning() && !checkEndOfLevel())
            {
                if (_rightLauncher.getMissileCount() > 0){
                    explosion(_xCursorPosition, _yCursorPosition);
                    _rightLauncher.decrementMissileCount();
                    repaint();
                }
            }
        }
    }
    private class KeyPListener extends KeyInteractor
    {
        public KeyPListener(JPanel p)
        {
            super(p,KeyEvent.VK_P);
        }

        public void actionPerformed (ActionEvent e) {

            Component panel = (Component) e.getSource();
            Color overlay = new Color(0, 0, 0, 175);
            JPanel pausePane = new JPanel()
                {
                    protected void paintComponent(Graphics g) {
                        g.setColor(getBackground());
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                };
            pausePane.setOpaque(false);
            pausePane.setBackground(overlay);

            if(_timer.isRunning())
            {
                _timer.stop();
                _paused = true;
                RootPaneContainer window = (RootPaneContainer) SwingUtilities.getWindowAncestor(panel);
                window.setGlassPane(pausePane);
                pausePane.setVisible(true);
                JDialog pauseDialog = new JDialog((Window)window, "Pause", Dialog.ModalityType.DOCUMENT_MODAL);
                pauseDialog.getContentPane().add(pausePanel);
                pauseDialog.setUndecorated(true);
                pauseDialog.pack();
                pauseDialog.setLocationRelativeTo((Window) window);
                pauseDialog.setVisible(true);
                pausePane.setVisible(false);
                _timer.start();
                _paused = false;
            }
            repaint();
        }
    }

    private class MouseListener extends MouseAdapter
    {
        public MouseListener(JPanel p)
        {
            Cursor c = new Cursor(Cursor.CROSSHAIR_CURSOR);
            p.setCursor(c);
        }

        public void mouseEntered(MouseEvent e)
        {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }

        public void mouseMoved(MouseEvent e)
        {
            _xCursorPosition = e.getPoint().getX();
            _yCursorPosition = e.getPoint().getY();
        }
    }

    private class ExplosionHelper extends TimerTask
    {
        public void run()
        {
            if(_timer.isRunning())
            {
                Iterator<SmartEllipse> _iterator2 = _explosionsArray.iterator();
                if (_iterator2.hasNext())
                {
                    SmartEllipse explosion = _iterator2.next();
                    explosion = null;
                    repaint();
                    _iterator2.remove();
                }
            }
        }
    }

    private class EnemyMissileHelper extends TimerTask
    {
        private int _count = 0;
        public void run()
        {
            if(_timer.isRunning()  && !checkEndOfLevel())
            {
                if (_leftover == 0){
                    _leftover = 1;
                }
                if(_count == 0 && _total >= 1)
                {
                    generateEnemy();
                    _count++;
                    _total--;
                }
                else
                {
                    _count++;
                    if(_count == 3) _count = 0;
                }

                Iterator<EnemyMissiles> iterator = _enemyMissilesArray.iterator();
                while(iterator.hasNext())
                {
                    EnemyMissiles missile = iterator.next();
                    missile.move();
                    repaint();
                }
                attack();
                onscreen();
            } else if (! _paused){
                if (_cityArray.size() == 0 || _launcherArray.size() == 0){
                    GameOver(tallyPoints());
                }
                else {
                    BeginNextLevel(tallyPoints());
                }

            }
        }
    }
}