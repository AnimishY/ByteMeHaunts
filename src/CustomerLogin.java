import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerLogin extends JFrame {
    private String customerId;
    private String name;

    private JLabel H1;
    private JButton loginSignUpButton;
    private JTextField textField1;
    private JPanel CustomerLoginPanel;
    private JButton goBackButton;
    private JTextField textField2;

    public CustomerLogin() {
        setContentPane(CustomerLoginPanel);
        setTitle("Customer Login");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);


        loginSignUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerId = textField1.getText();
                name = textField2.getText();
                if (customerId.matches("\\d{10}")) {
                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Name field cannot be empty.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Welcome, " + customerId + "!");
                        Customer customer = Main.customers.get(customerId);
                        if (customer == null) {
                            customer = new Customer(customerId, name);
                            Main.customers.put(customerId, customer);
                            System.out.println("New customer created: " + customer.getCustomerId());
                        }
                        new CustomerDashboard(customer);
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid customer ID. Please enter a 10-digit number.");
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