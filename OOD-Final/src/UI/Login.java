package UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import UI.MainFrame;

import javax.swing.*;

/**
 * @Auther: wangqitong
 * @Date: 04-12-2019 11:48
 * @Description: User needs to inout username and password to enter the system.
 */
public class Login extends JFrame implements ActionListener {
    protected JButton loginButton, resetButton;
    protected JPanel brandPanel, userNamePanel, pswPanel, buttonPanel;
    protected JTextField userNameTextField;
    protected JLabel loginLabel, userNameLabel, pswLabel;
    protected JPasswordField pswTextField;
    protected MainFrame mainFrame;

    public Login() {
        // TODO Auto-generated constructor stub
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);

        brandPanel = new JPanel();
//        ImageIcon img = new ImageIcon("LoginImg.jpg");
//        brandPanel.setLayout(null);
//        brandPanel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
//        JLabel a = new JLabel(img);
//        a.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());

        userNamePanel = new JPanel();
        pswPanel = new JPanel();
        buttonPanel = new JPanel();

        loginLabel = new JLabel("Welcome!");
        loginLabel.setFont(new java.awt.Font("Dialog", 1, 20));
        userNameLabel = new JLabel("Username");
        pswLabel = new JLabel("Psssword");

        userNameTextField = new JTextField(10);
        pswTextField = new JPasswordField(10);

        brandPanel.add(loginLabel);
        userNamePanel.add(userNameLabel);
        userNamePanel.add(userNameTextField);
        pswPanel.add(pswLabel);
        pswPanel.add(pswTextField);
        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);

        brandPanel.setSize(350, 200);
        userNamePanel.setSize(350, 10);
        pswPanel.setSize(350, 10);
        buttonPanel.setSize(350, 30);

        this.add(brandPanel);
        this.add(userNamePanel);
        this.add(pswPanel);
        this.add(buttonPanel);

        this.setTitle("Login");
        this.setLayout(new GridLayout(4, 1));
        this.setSize(350, 250);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setVisible(true);
        this.setResizable(false);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand()=="Login") {
            boolean index = judgeInput();
            if(index) {
                mainFrame = new MainFrame();
                this.dispose();
            }
        }
        else if (e.getActionCommand()=="Reset") {
            userNameTextField.setText("");
            pswTextField.setText("");
        }
    }

    private boolean judgeInput() {
        if(userNameTextField.getText().equals("") && pswTextField.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Please input your username and password!", "Info", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if((userNameTextField.getText().equals("") && !pswTextField.getText().equals(""))){
            JOptionPane.showMessageDialog(null, "Please input your username!", "Info", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if(!userNameTextField.getText().equals("") && pswTextField.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Please input your password!", "Info", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else
            return true;
    }
}
