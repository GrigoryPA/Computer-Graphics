import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel {
    private static MainFrame3D frame3d;
    private static MainFrame2D frame2d;
    private static MainFrameRT frameRT;
    private static JFrame frameChoice;
    private static JButton button3d;
    private static JButton button2d;
    private static JButton buttonRT;

    public static void main(String[] args) {
        frameChoice =new JFrame();
        frameChoice.setBounds(100,100, 256, 450);
        frameChoice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        button2d = new JButton(new ImageIcon("src/main/resources/icons/2d.png"));
        button3d = new JButton(new ImageIcon("src/main/resources/icons/3d.png"));
        buttonRT = new JButton(new ImageIcon("src/main/resources/icons/2d.png"));
        frameChoice.add(button2d,BorderLayout.NORTH);
        frameChoice.add(button3d,BorderLayout.CENTER);
        frameChoice.add(buttonRT,BorderLayout.SOUTH);
        frameChoice.setVisible(true);

        button2d.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                frameChoice.setVisible(false);
                frame2d = new MainFrame2D();
                frame2d.MakeAndShow();
            }
        });

        button3d.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                frameChoice.setVisible(false);
                frame3d = new MainFrame3D();
                frame3d.MakeAndShow();
            }
        });

        buttonRT.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                frameChoice.setVisible(false);
                frameRT = new MainFrameRT();
                frameRT.MakeAndShow();
            }
        });
    }
}
