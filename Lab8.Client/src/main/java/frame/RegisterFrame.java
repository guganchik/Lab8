package frame;

import app.Application;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterFrame {

    private final JFrame frame;
    private final JTextField loginField;
    private final JPasswordField passwordField;
    private final JButton registerButton;

    public RegisterFrame() {
        frame = new JFrame("Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(400, 200);
        frame.setResizable(false);

        JPanel pane;
        JLabel label;
        Font font = new Font("Tahoma", 0, 14);
        
        /****** Login & Password inputs ******/
        
        pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        label = new JLabel("Login");
        label.setFont(font);
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20,40,0,0);
        pane.add(label, c);

        loginField = new JTextField();
        loginField.setFont(font);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 10;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(20,0,0,40);
        pane.add(loginField, c);

        label = new JLabel("Password");
        label.setFont(font);
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(20,40,0,0);
        pane.add(label, c);
        
        passwordField = new JPasswordField();
        passwordField.setEchoChar('*');        
        passwordField.setFont(font);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 10;
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(20,0,0,40);
        pane.add(passwordField, c);

        frame.add(pane, BorderLayout.NORTH);
        
        /****** Buttons ******/
        
        pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        registerButton = new JButton("Register");
        registerButton.setFont(font);
        registerButton.setPreferredSize(new Dimension(220,40));
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Application.getInstance().userRegister(loginField.getText(), passwordField.getText());
            }
        });
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,0,0,0);
        pane.add(registerButton, c);
        
        frame.add(pane, BorderLayout.CENTER);
        
        frame.setLocationRelativeTo(null);
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    public void hide() {
        frame.setVisible(false);
    }
    
}
