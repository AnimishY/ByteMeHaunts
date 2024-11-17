import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminLogin extends JFrame {
    private String adminID;

    private JLabel H1;
    private JTextField textField1;
    private JPanel AdminLoginPanel;
    private JLabel enterAdminIDToLabel;
    private JButton enterAsAdminButton;
    private JButton goBackButton;

    public AdminLogin() {
        setContentPane(AdminLoginPanel);
        setTitle("Admin Login");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        enterAsAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminID = textField1.getText();
                if (adminID.matches("admin")) {
                    {
                        JOptionPane.showMessageDialog(null, "Welcome, Admin!");
                        new AdminDashboard();
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid admin ID. Please enter a 10-digit number.");
                }
            }
        });
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Main();
                dispose();
            }
        });
    }
}