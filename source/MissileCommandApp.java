import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Kiera Wolfe, Sam Hunsley, Jack Schooley, and Raven Vella
 * @version 11/13/2019
 */
public class MissileCommandApp extends JFrame
{
    private GamePanel _gamePanel;

    public MissileCommandApp(String title){
     super(title);
     this.setResizable(false);
     _gamePanel = new GamePanel();
     this.setSize(MissileConstants.BOARD_WIDTH, MissileConstants.BOARD_HEIGHT + 22);
     this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
     this.add(_gamePanel);
     this.setVisible(true);
    }

    public static void main(String[] args){
        MissileCommandApp app = new MissileCommandApp("Missile Command");
    }
}
