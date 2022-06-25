package frame;

import app.Application;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
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

public class MessageDialog extends JDialog {
    
    public MessageDialog(JFrame frame, String title, String text) {
        super(frame, title, true);
        setLayout(new BorderLayout());
        JPanel pane = new JPanel();
        add(pane, BorderLayout.NORTH);
        pane.setLayout(new GridLayout(1,1));
        pane.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        Font font = new Font("Tahoma", 0, 14);
        
        JTextArea textArea = new JTextArea();               
        textArea.setFont(font);
        textArea.setLineWrap(true);
        
        JScrollPane scroll= new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setSize(500, 250);
        scroll.setBorder(BorderFactory.createLineBorder(Color.black));
        pane.add(scroll);        
        
        textArea.setText(text);

        JButton button = new JButton(Application.getInstance().getLocalizedString("button.close"));
        button.setFont(font);
        button.setPreferredSize(new Dimension(220,40));
        button.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }  
        });
        pane = new JPanel();
        add(pane, BorderLayout.SOUTH);
        pane.setBorder(new EmptyBorder(10, 10, 10, 10));
        pane.add(button);
        
        //pack();
        //setLocationRelativeTo(null);
        setSize(500,300);    
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - getWidth()) / 2;
        final int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
        
        setVisible(true);  
    }
}
