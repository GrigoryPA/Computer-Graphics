import Lab4.MainFrameCS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel {
    private static MainFrame3D frame3d;
    private static MainFrame2D frame2d;
    private static JFrame frameChoice;
    private static JButton button3d;
    private static JButton button2d;
    private static JButton buttonCS;

    public static void main(String[] args) {
        frameChoice = new JFrame();
        frameChoice.setBounds(100, 100, 256, 310);
        frameChoice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        button2d = new JButton(new ImageIcon("src/main/resources/icons/2d.png"));
        button3d = new JButton(new ImageIcon("src/main/resources/icons/3d.png"));
        buttonCS = new JButton("Cohen-Sutherland");

        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(button2d);
        panel.add(button3d);
        panel.add(buttonCS);
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
                frame3d.MakeAndShow();
            }
        });

        buttonCS.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frameChoice.setVisible(false);
                MainFrameCS frameCS = new MainFrameCS();
                frameCS.MakeAndShow();
            }
        });
    }
}
