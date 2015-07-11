 
import java.awt.Color;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import java.awt.Font;
 
public class Splash extends Thread {
    static JLabel loadingResourcelbl = new JLabel("Loading Resources... Graphical Interface");

    /**
     * @wbp.parser.entryPoint
     */
    public void run() {
    	frame = new JFrame();
    	frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Application.class.getResource("images/twitter47.png")));
        frame.setUndecorated(true);
        frame.setSize(444, 212);
        frame.getContentPane().setLayout(null);
        frame.setBackground(new Color(0, 0, 0, 0));
        label.setIcon(new ImageIcon((getClass().getResource("images/Splash.png"))));
        frame.getContentPane().setLayout(null);
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.CYAN);
        
        loadingResourcelbl = new JLabel();
        loadingResourcelbl.setFont(new Font("HelveticaNeue", Font.PLAIN, 10));
        loadingResourcelbl.setForeground(Color.WHITE);
        loadingResourcelbl.setBounds(149, 70, 211, 28);
        frame.getContentPane().add(loadingResourcelbl);
        progressBar.setBounds(149, 119, 194, 14);
        frame.getContentPane().add(progressBar);
        label.setBounds(0, 0, 428, 212);
        frame. getContentPane().add(label);
        
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        }
    

    private final JLabel label = new JLabel("");
    static JProgressBar progressBar = new JProgressBar();;
	public static JFrame frame;
     

}


