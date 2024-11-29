import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerLogin extends JFrame {
    String customerId;
    private String name;

    private JLabel H1;
    JButton loginSignUpButton;
    JTextField textField1;
    private JPanel CustomerLoginPanel;
    private JButton goBackButton;
    JTextField textField2;

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

// tests

class CustomerLoginTest {
    private CustomerLogin loginFrame;

    @BeforeEach
    void setUp() {
        // Initialize the login frame before each test
        loginFrame = new CustomerLogin();
    }

    @Test
    @DisplayName("Test valid customer login with new customer")
    void testValidNewCustomerLogin() {
        // Set valid input values
        loginFrame.textField1.setText("1234567890");
        loginFrame.textField2.setText("John Doe");

        // Simulate button click
        loginFrame.loginSignUpButton.doClick();

        // Verify customer was added to the system
        Customer customer = Main.customers.get("1234567890");
        assertNotNull(customer, "Customer should be added to the system");
        assertEquals("1234567890", customer.getCustomerId(), "Customer ID should match input");
        assertEquals("John Doe", customer.getName(), "Customer name should match input");
    }

    @Test
    @DisplayName("Test valid customer login with existing customer")
    void testValidExistingCustomerLogin() {
        // Add an existing customer
        Customer existingCustomer = new Customer("9876543210", "Jane Smith");
        Main.customers.put("9876543210", existingCustomer);

        // Set valid input values
        loginFrame.textField1.setText("9876543210");
        loginFrame.textField2.setText("Jane Smith");

        // Simulate button click
        loginFrame.loginSignUpButton.doClick();

        // Verify customer exists in the system
        Customer customer = Main.customers.get("9876543210");
        assertNotNull(customer, "Customer should exist in the system");
        assertEquals("9876543210", customer.getCustomerId(), "Customer ID should match existing customer");
    }

    @Test
    @DisplayName("Test invalid customer ID format")
    void testInvalidCustomerId() {
        // Test cases for invalid customer IDs
        String[] invalidIds = {
                "123", // Too short
                "12345678901", // Too long
                "abcd123456", // Contains letters
                "123-456-789", // Contains special characters
                "" // Empty
        };

        for (String invalidId : invalidIds) {
            loginFrame.textField1.setText(invalidId);
            loginFrame.textField2.setText("Test Name");

            // Simulate button click
            loginFrame.loginSignUpButton.doClick();

            // Verify customer was not added to the system
            assertNull(Main.customers.get(invalidId),
                    "Customer should not be added with invalid ID: " + invalidId);
        }
    }

    @Test
    @DisplayName("Test empty name field")
    void testEmptyName() {
        // Set valid customer ID but empty name
        loginFrame.textField1.setText("1234567890");
        loginFrame.textField2.setText("");

        // Simulate button click
        loginFrame.loginSignUpButton.doClick();

        // Verify customer was not added to the system
        assertNull(Main.customers.get("1234567890"),
                "Customer should not be added with empty name");
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        loginFrame.dispose();
        Main.customers.clear();
    }
}