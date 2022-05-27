import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

class PausePanel extends JPanel
{
    private KeyPListener _pauseKey;

    public PausePanel() {
        JLabel pausedLabel = new JLabel("PAUSED");
        pausedLabel.setFont(new Font("Arial", Font.BOLD, 36));
        pausedLabel.setForeground(Color.RED);
        JPanel pausedPanel = new JPanel();
        pausedPanel.setOpaque(false);
        pausedPanel.add(pausedLabel);

        setBackground(Color.BLACK);
        setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.BLACK));
        setLayout(new GridLayout(0, 1, 10, 10));
        add(pausedPanel);
        _pauseKey = new KeyPListener(this);
    }

    // gets rid of pause screen
    private class KeyPListener extends KeyInteractor
    {
        public KeyPListener(JPanel p)
        {
            super(p,KeyEvent.VK_P);
        }

        public void actionPerformed(ActionEvent e)
        {
            Component comp = (Component) e.getSource();
            Window window = SwingUtilities.getWindowAncestor(comp);
            window.dispose();
        }
    }
}
