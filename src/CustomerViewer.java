import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomerViewer {
    private JFrame frame;

    public CustomerViewer(List<Customer> customers) {
        frame = new JFrame("Booked Customers");
        frame.setSize(450, 400);
        frame.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        StringBuilder sb = new StringBuilder();
        if (customers.isEmpty()) {
            sb.append("No customers currently booked.");
        } else {
            for (Customer c : customers) {
                sb.append("Name: ").append(c.getName())
                        .append(", Contact: ").append(c.getContactNumber())
                        .append(", Room No.: ").append(c.getRoomNumber())
                        .append(", Room Type: ").append(c.getRoomType())
                        .append(", Price: ₹").append(c.getRoomPrice())
                        .append("\n");
            }
        }

        textArea.setText(sb.toString());

        frame.add(new JScrollPane(textArea));
        frame.setVisible(true);
    }
}
