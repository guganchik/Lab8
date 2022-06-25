package frame;

import app.Application;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ParamsDialog extends JDialog {
    
    public ParamsDialog(JFrame frame, String title, String command, String[] params) {
        super(frame, title, true);
        setLayout(new BorderLayout());
        JPanel pane = new JPanel();
        add(pane, BorderLayout.NORTH);
        pane.setLayout(new GridLayout(1,1));
        pane.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        Font font = new Font("Tahoma", 0, 14);
        
        JTextField[] propertyFields = new JTextField[params.length];
        
        JPanel pane2 = new JPanel();
        pane2.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JLabel label;
        
        int i = 0;
        for (String column : params) {
            label = new JLabel(column);
            label.setFont(font);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            constraints.insets = new Insets(5,10,0,0); 
            constraints.gridx = 0;
            constraints.gridy = i;
            constraints.weightx = 1;
            pane2.add(label, constraints);

            propertyFields[i] = new JTextField();
            propertyFields[i].setFont(font);
            constraints.gridx = 1;
            constraints.gridy = i;
            constraints.weightx = 10;
            pane2.add(propertyFields[i], constraints);
            
            i++;
        }
        pane.add(pane2);
        
        JButton button = new JButton(Application.getInstance().getLocalizedString("button.execute"));
        button.setFont(font);
        button.setPreferredSize(new Dimension(220,40));
        button.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String args[] = new String[params.length];
                for (int i=0; i<params.length; i++) {
                    args[i] = propertyFields[i].getText();
                }
                if (Application.getInstance().sendCommand(command, args, true)) {
                    dispose();
                };
            }  
        });
        pane = new JPanel();
        add(pane, BorderLayout.SOUTH);
        pane.setBorder(new EmptyBorder(10, 10, 10, 10));
        pane.add(button);
        
        setSize(500,117 + params.length * 26);    
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - getWidth()) / 2;
        final int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
        
        setVisible(true);  
    }
}
