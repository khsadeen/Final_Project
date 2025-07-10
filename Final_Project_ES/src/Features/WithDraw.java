package Features;

import GUIjavaSwing.Elements;
import Management.CardsManager.Cards;
import Management.CardsManager.CardsUpdater;
import Emails.Email;
import Management.FeaturesManage.FeaturesMenu;
import GUIjavaSwing.RoundedBorder;
import Main.MainMenu;

import javax.swing.*;
import java.awt.*;

public class WithDraw {

    public interface AmountSelectionListener {
        void onAmountSelected(double amount);
    }

    // Constructor: Withdraw from a real card
    public WithDraw(Cards card) {
        JFrame frame = new JFrame("💵 Choose Amount 💵");
        frame.setSize(400, 570);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Elements.BEIGE);

        JLabel logoLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("src/assets/logo.png");
            Image image = icon.getImage().getScaledInstance(200, 180, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(image));
            logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 80, 10, 80));
        } catch (Exception e) {
            logoLabel.setText("⚠️ Logo not found");
            logoLabel.setForeground(Elements.SEAWEED);
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        frame.add(logoLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Elements.BEIGE);

        JLabel label = new JLabel("Select amount to withdraw:", SwingConstants.CENTER);
        label.setForeground(Elements.SEAWEED);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        centerPanel.add(label, BorderLayout.NORTH);

        JPanel amountPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        amountPanel.setBackground(Elements.BEIGE);
        amountPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton btn100 = styledButton("100₪", Elements.SEAWEED,Elements.CHAMPAGNE);
        btn100.setBorder(new RoundedBorder(12));
        JButton btn200 = styledButton("200₪",Elements.SEAWEED,Elements.CHAMPAGNE);
        btn200.setBorder(new RoundedBorder(12));
        JButton btn500 = styledButton("500₪",Elements.SEAWEED,Elements.CHAMPAGNE);
        btn500.setBorder(new RoundedBorder(12));
        JButton btn1000 = styledButton("1000₪",Elements.SEAWEED,Elements.CHAMPAGNE);
        btn1000.setBorder(new RoundedBorder(12));

        btn100.addActionListener(e -> handleWithdraw(frame, card, 100));
        btn200.addActionListener(e -> handleWithdraw(frame, card, 200));
        btn500.addActionListener(e -> handleWithdraw(frame, card, 500));
        btn1000.addActionListener(e -> handleWithdraw(frame, card, 1000));

        amountPanel.add(btn100);
        amountPanel.add(btn200);
        amountPanel.add(btn500);
        amountPanel.add(btn1000);
        centerPanel.add(amountPanel, BorderLayout.CENTER);

        // ======= Custom + Cancel =======
        JPanel otherPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        otherPanel.setBackground(Elements.BEIGE);
        otherPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        JButton btnOther = styledButton("Custom Amount",Elements.SOFT_GREEN,Elements.CHAMPAGNE);
        btnOther.setBorder(new RoundedBorder(10));
        btnOther.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JButton btnCancel = styledButton("Cancel X",Elements.SOFT_GREEN ,Elements.CHAMPAGNE);
        btnCancel.setBorder(new RoundedBorder(10));
        btnOther.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Enter custom amount:");
            try {
                if (input != null) {
                    double amount = Double.parseDouble(input);
                    handleWithdraw(frame, card, amount);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "❌ Invalid input.");
            }
        });

        btnCancel.addActionListener(e -> {frame.dispose();
        MainMenu.screen();});
        otherPanel.add(btnOther);
        otherPanel.add(btnCancel);

        centerPanel.add(otherPanel, BorderLayout.SOUTH);
        frame.add(centerPanel, BorderLayout.CENTER);

        JButton backButton = styledButton(" Back",Elements.SOFT_GREEN,Elements.CHAMPAGNE);
        backButton.setIcon(Elements.resizedBackIcon);
        backButton.setVerticalTextPosition(SwingConstants.CENTER);
        backButton.setHorizontalTextPosition(SwingConstants.LEFT);
        backButton.setBorder(new RoundedBorder(10));

        JButton curBalanceBtn = styledButton("show balance",Elements.SOFT_GREEN,Elements.CHAMPAGNE);
        curBalanceBtn.setBorder(new RoundedBorder(10));
        curBalanceBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JPanel backPanel = new JPanel(new GridLayout(1,2,20,0));
        backPanel.setBackground(Elements.BEIGE);
        backPanel.setBorder(BorderFactory.createEmptyBorder(7, 20, 7, 20));
        backPanel.add(backButton, BorderLayout.WEST);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.addActionListener(e -> {
            frame.dispose();
            new FeaturesMenu(card);
        });
        curBalanceBtn.addActionListener(e -> {
            JPanel panel = new JPanel();
            panel.setBackground(Elements.BEIGE);
            panel.setForeground(Elements.SEAWEED);
            JOptionPane.showMessageDialog(panel,"Your current balance is: "+card.balance+"₪");
            JButton okBtn = styledButton("OK",Elements.SEAWEED,Elements.CHAMPAGNE);
            okBtn.addActionListener(e1 -> frame.dispose());
            panel.add(okBtn);
        });
    backPanel.add(curBalanceBtn);
        frame.add(backPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    // Constructor: Select amount only (no real card)
    public WithDraw(JFrame parent, AmountSelectionListener listener) {
        JDialog dialog = new JDialog(parent, "Select Transfer Amount", true);
        dialog.setSize(350, 400);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Elements.BEIGE);

        JLabel label = new JLabel("Choose am\n" +
                "\n" +
                "Process finished with exit code 0\nount to transfer:", SwingConstants.CENTER);
        label.setForeground(Elements.SEAWEED);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        dialog.add(label, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        buttonsPanel.setBackground(Elements.BEIGE);

        JButton btn100 = styledButton("100₪",Elements.SEAWEED,Elements.CHAMPAGNE);
        JButton btn200 = styledButton("200₪",Elements.SEAWEED,Elements.CHAMPAGNE);
        JButton btn500 = styledButton("500₪",Elements.SEAWEED,Elements.CHAMPAGNE);
        JButton btn1000 = styledButton("1000₪",Elements.SEAWEED,Elements.CHAMPAGNE);
        JButton btnOther = styledButton("Custom Amount",Elements.SOFT_GREEN,Elements.CHAMPAGNE);
        JButton btnCancel = styledButton("Cancel X",Elements.SOFT_GREEN,Elements.CHAMPAGNE);

        btn100.addActionListener(e -> {
            if (confirmAmount(100)) {
                listener.onAmountSelected(100);
                dialog.dispose();
            }
        });
        btn200.addActionListener(e -> {
            if (confirmAmount(200)) {
                listener.onAmountSelected(200);
                dialog.dispose();
            }
        });
        btn500.addActionListener(e -> {
            if (confirmAmount(500)) {
                listener.onAmountSelected(500);
                dialog.dispose();
            }
        });
        btn1000.addActionListener(e -> {
            if (confirmAmount(1000)) {
                listener.onAmountSelected(1000);
                dialog.dispose();
            }
        });
        btnOther.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(dialog, "Custom Amount:");
            try {
                if (input != null) {
                    double amount = Double.parseDouble(input);
                    if (confirmAmount(amount)) {
                        listener.onAmountSelected(amount);
                        dialog.dispose();
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "❌ Invalid input.");
            }
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        buttonsPanel.add(btn100);
        buttonsPanel.add(btn200);
        buttonsPanel.add(btn500);
        buttonsPanel.add(btn1000);
        buttonsPanel.add(btnOther);
        buttonsPanel.add(btnCancel);
        dialog.add(buttonsPanel, BorderLayout.CENTER);

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    // Shared confirmation logic
    private boolean confirmAmount(double amount) {
        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to proceed with ₪" + amount + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return false;

        if (amount > 7000) {
            int high = JOptionPane.showConfirmDialog(null,
                    "⚠️ High amount. Are you sure?", "High Amount", JOptionPane.YES_NO_OPTION);
            if (high != JOptionPane.YES_OPTION) return false;
        }
        return true;
    }

    // Actual withdrawal logic
    private void handleWithdraw(JFrame parent, Cards card, double amount) {
        if (!confirmAmount(amount)) return;

        double available = card.balance + card.overDraftLimit;
        if (amount > available) {
            int option = JOptionPane.showConfirmDialog(null,
                    "⚠️ Amount exceeds available balance.\nContinue anyway?", "Warning⚠️", JOptionPane.YES_NO_OPTION);
            if (option != JOptionPane.YES_OPTION) return;
        }

        if (card.balance - amount >= -card.overDraftLimit) {
            card.balance -= amount;
            CardsUpdater.updateCard(card);
            showConfirmationScreen(parent, amount, card.balance);
            try {
                Email.sendEmail(
                        card.email,
                        "✅ Withdrawal Successful",
                        "Hello " + card.ownerName + ",\n\n" +
                                "You have successfully withdrawn " + card.balance + "₪ from your card.\n\n" +
                                "Thank you for using Flexi ATM.\n\n" +
                                "Best regards,\n" +
                                "Flexi ATM Support Team"
                );
                JOptionPane.showMessageDialog(null, "Email sent successfully to your inbox.");
            } catch (Exception e) {
                System.out.println("❌ Error sending email to: " + card.email + ". Exception: " + e);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Withdrawal failed. Overdraft limit exceeded.X");
        }
    }

    // Confirmation screen after successful withdrawal
    private void showConfirmationScreen(JFrame parent, double amount, double balance) {
        JDialog dialog = new JDialog(parent, "✅ Withdrawal Confirmation", true);
        dialog.setSize(400, 520);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Elements.BEIGE);

        JLabel logoLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("src/assets/logo.png");
            Image image = icon.getImage().getScaledInstance(360, 250, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            logoLabel.setText("✅ Success");
            logoLabel.setForeground(Elements.SEAWEED);
        }
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        dialog.add(logoLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 8, 8));
        infoPanel.setBackground(Elements.BEIGE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30)); // padding حول المحتوى

        JLabel successLabel = new JLabel("√ Withdrawal Successful!", SwingConstants.CENTER);
        successLabel.setForeground(Elements.SEAWEED);
        successLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel amountLabel = new JLabel("Withdrawn: ₪" + amount, SwingConstants.CENTER);
        amountLabel.setForeground(Elements.SEAWEED);
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel balanceLabel = new JLabel("Current Balance: ₪" + balance, SwingConstants.CENTER);
        balanceLabel.setForeground(Elements.SEAWEED
        );
        balanceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        infoPanel.add(successLabel);
        infoPanel.add(amountLabel);
        infoPanel.add(balanceLabel);

        dialog.add(infoPanel, BorderLayout.CENTER);

        JButton okButton = styledButton("OK",Elements.SEAWEED,Elements.CHAMPAGNE);
        okButton.addActionListener(e -> {
            dialog.dispose();
            MainMenu.screen();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Elements.BEIGE);
        bottomPanel.add(okButton);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(parent);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    // Style buttons
    private JButton styledButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(foreground.darker(), 1));
        button.setPreferredSize(new Dimension(100, 40));
        return button;
    }
}