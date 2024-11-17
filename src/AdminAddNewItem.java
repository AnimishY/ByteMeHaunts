import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminAddNewItem extends JFrame {
    private JLabel H1;
    private JButton logOutButton;
    private JTextField itemprice;
    private JTextField itemname;
    private JTextField itemcategory;
    private JButton CancelAdd;
    private JButton AddItemtoMenu;
    private JPanel AdminAddNewItemPanel;

    public AdminAddNewItem() {
        setContentPane(AdminAddNewItemPanel);
        setTitle("Admin Add New Item");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        CancelAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new AdminDashboard();
                dispose();
            }
        });

        AddItemtoMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String name = itemname.getText();
                String price = itemprice.getText();
                String category = itemcategory.getText();
                FoodItem item = new FoodItem(name, Double.parseDouble(price), category, true);
                Main.menu.addItem(item);
                JOptionPane.showMessageDialog(null, "Item added successfully.");
                new AdminDashboard();
                dispose();
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Main();
                dispose();
            }
        });
    }
}
