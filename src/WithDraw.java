import jakarta.mail.MessagingException;

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
        Color darkBrown = new Color(68, 16, 10);
        Color gold = new Color(155, 124, 68);
        frame.getContentPane().setBackground(darkBrown);

        JLabel logoLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("src/project.Images/Welcome.png");
            Image image = icon.getImage().getScaledInstance(360, 240, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            logoLabel.setText("⚠️ Logo not found");
            logoLabel.setForeground(gold);
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        frame.add(logoLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(darkBrown);

        JLabel label = new JLabel("Select amount to withdraw:", SwingConstants.CENTER);
        label.setForeground(gold);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        centerPanel.add(label, BorderLayout.NORTH);

        // ======= مبلغ ثابت buttons =======
        JPanel amountPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        amountPanel.setBackground(darkBrown);
        amountPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton btn100 = styledButton("100₪", darkBrown, gold);
        JButton btn200 = styledButton("200₪", darkBrown, gold);
        JButton btn500 = styledButton("500₪", darkBrown, gold);
        JButton btn1000 = styledButton("1000₪", darkBrown, gold);

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
        otherPanel.setBackground(darkBrown);
        otherPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton btnOther = styledButton("Custom Amount", gold, darkBrown);
        btnOther.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JButton btnCancel = styledButton("Cancel", gold, darkBrown);

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

        btnCancel.addActionListener(e -> frame.dispose());
        otherPanel.add(btnOther);
        otherPanel.add(btnCancel);

        centerPanel.add(otherPanel, BorderLayout.SOUTH);
        frame.add(centerPanel, BorderLayout.CENTER);

        JButton backButton = styledButton("Back", gold, darkBrown);
        JPanel backPanel = new JPanel(new BorderLayout());
        backPanel.setBackground(darkBrown);
        backPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backPanel.add(backButton, BorderLayout.WEST);

        backButton.addActionListener(e -> {
            frame.dispose();
            new FeaturesMenu(card);
        });

        frame.add(backPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    // Constructor: Select amount only (no real card)
    public WithDraw(JFrame parent, AmountSelectionListener listener) {
        JDialog dialog = new JDialog(parent, "Select Transfer Amount", true);
        dialog.setSize(350, 400);
        dialog.setLayout(new BorderLayout());
        Color babyBlue = new Color(244, 250, 255);
        Color richRed = new Color(151, 23, 23);
        Color gold = new Color(155, 124, 68);
        dialog.getContentPane().setBackground(babyBlue);

        JLabel label = new JLabel("Choose amount to transfer:", SwingConstants.CENTER);
        label.setForeground(richRed);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        dialog.add(label, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        buttonsPanel.setBackground(babyBlue);

        JButton btn100 = styledButton("100₪", richRed, gold);
        JButton btn200 = styledButton("200₪", richRed, gold);
        JButton btn500 = styledButton("500₪", richRed, gold);
        JButton btn1000 = styledButton("1000₪", richRed, gold);
        JButton btnOther = styledButton("Custom Amount", gold, richRed);
        JButton btnCancel = styledButton("Cancel", gold, richRed);

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
            try {
                Email.SendEmail(card, "Withdrawal Confirmation..." +"\n\n"+
                        "Dear " + card.ownerName + ",\n\n" +
                        "You have successfully withdrawn: " + amount + "₪.\n" +
                        "Your current balance is: " + card.balance + "₪.\n\n" +
                        "Thank you for using our ATM service!.");
            }
            catch (MessagingException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(parent, "❌ Failed to send confirmation process email.");
            }
            showConfirmationScreen(parent, amount, card.balance);
        } else {
            JOptionPane.showMessageDialog(null, "❌ Withdrawal failed. Overdraft limit exceeded.");
        }
    }

    // Confirmation screen after successful withdrawal
    private void showConfirmationScreen(JFrame parent, double amount, double balance) {
        JDialog dialog = new JDialog(parent, "✅ Withdrawal Confirmation", true);
        dialog.setSize(400, 520);
        dialog.setLayout(new BorderLayout());

        Color darkBrown = new Color(244, 250, 255);
        Color gold = new Color(151, 23, 23);
        dialog.getContentPane().setBackground(darkBrown);

        JLabel logoLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("src/project.Images/Welcome.png");
            Image image = icon.getImage().getScaledInstance(360, 250, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            logoLabel.setText("✅ Success");
            logoLabel.setForeground(gold);
        }
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        dialog.add(logoLabel, BorderLayout.NORTH);

        // غيرت من 3 الى 4 صفوف، وزدت padding للهوامش
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 8, 8)); // 4 صفوف و 8 بكسل فراغات بين العناصر
        infoPanel.setBackground(darkBrown);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30)); // padding حول المحتوى

        JLabel successLabel = new JLabel("√ Withdrawal Successful!", SwingConstants.CENTER);
        successLabel.setForeground(gold);
        successLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel amountLabel = new JLabel("Withdrawn: ₪" + amount, SwingConstants.CENTER);
        amountLabel.setForeground(gold);
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel balanceLabel = new JLabel("Current Balance: ₪" + balance, SwingConstants.CENTER);
        balanceLabel.setForeground(gold);
        balanceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel emailSentLabel = new JLabel("A confirmation email has been sent to your inbox √", SwingConstants.CENTER);
        emailSentLabel.setForeground(gold);

        infoPanel.add(successLabel);
        infoPanel.add(amountLabel);
        infoPanel.add(balanceLabel);
        infoPanel.add(emailSentLabel);

        dialog.add(infoPanel, BorderLayout.CENTER);

        JButton okButton = styledButton("OK", gold, darkBrown);
        okButton.addActionListener(e -> {
            MainMenu.screen();
            dialog.dispose();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(darkBrown);
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