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
    private static JButton buttonSL;

    public static void main(String[] args) {
        frameChoice = new JFrame();
        frameChoice.setBounds(100, 100, 256, 450);
        frameChoice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        button2d = new JButton("2d");
        button3d = new JButton("3d");
        buttonRT = new JButton("Raytracing");
        buttonSL = new JButton("Scan Line");

        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.add(button2d);
        panel.add(button3d);
        panel.add(buttonRT);
        panel.add(buttonSL);
        frameChoice.add(panel);
        frameChoice.setVisible(true);

        button2d.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frameChoice.setVisible(false);
                frame2d = new MainFrame2D();
                frame2d.MakeAndShow();
            }
        });

        button3d.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frameChoice.setVisible(false);
                frame3d = new MainFrame3D();
                //frame3d.MakeAndShow();
            }
        });

        buttonSL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frameChoice.setVisible(false);
                MainFrameSL frameSL = new MainFrameSL();
                frameSL.MakeAndShow();
            }
        });

        buttonRT.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frameChoice.setVisible(false);
                frameRT = new MainFrameRT();
                //frameRT.MakeAndShow();
            }
        });


    }
}
