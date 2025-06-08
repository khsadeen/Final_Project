import javax.swing.*;
import java.awt.*;

public class FeaturesMenu {

    public JDialog dialog;

    Color darkBrown = new Color(244, 250, 255);
    Color gold = new Color(151, 23, 23);
    Font uniFont = new Font("Arial", Font.BOLD, 14);

    public JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setFont(uniFont);
        button.setBorder(BorderFactory.createLineBorder(fg));
        return button;
    }

    public FeaturesMenu(Cards authenticatedCard) {
        dialog = new JDialog((Frame) null, "Available Features", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(darkBrown); // خلفية الشاشة

        JLabel titleLabel = new JLabel("Select a feature to continue:", SwingConstants.CENTER);
        titleLabel.setFont(uniFont);
        titleLabel.setForeground(gold); // لون النص العنوان
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        dialog.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        buttonPanel.setBackground(darkBrown); // خلفية لوحة الأزرار

        // Withdraw
        JButton withdrawButton = createStyledButton("Withdraw", gold, darkBrown);
        withdrawButton.addActionListener(e -> {
            dialog.dispose();
            new WithDraw(authenticatedCard);
        });

        // Lock Account
        JButton lockButton = createStyledButton("Lock Account", gold, darkBrown);
        lockButton.addActionListener(e -> {
            dialog.dispose();
            new LockHandler(authenticatedCard);
            JOptionPane.showMessageDialog(null, "Account has been locked.");
        });

        // Account Statements
        JButton statementsButton = createStyledButton("Account Statements & Transactions", gold, darkBrown);
        statementsButton.addActionListener(e -> {
            dialog.dispose();
            Statements_papersData.showStatementOptions(authenticatedCard);
//            JOptionPane.showMessageDialog(null, "Displaying account statements...");
        });


        // Back Button
        JButton backButton = createStyledButton("<- Back to Main Menu", gold, darkBrown);
        backButton.addActionListener(e -> {
            dialog.dispose();
            MainMenu.screen();
        });

        buttonPanel.add(withdrawButton);
        buttonPanel.add(lockButton);
        buttonPanel.add(statementsButton);
        buttonPanel.add(backButton);

        dialog.add(buttonPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}