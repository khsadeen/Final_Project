package Features;

import GUIjavaSwing.Elements;
import Management.CardsManager.Cards;
import Management.CardsManager.CardsUpdater;
import Emails.Email;
import jakarta.mail.MessagingException;

import javax.swing.*;
import java.awt.*;

public class LockHandler {

    private Cards card;

    public LockHandler(Cards card) {
        this.card = card;
        showLockDialog();
    }

    private void showLockDialog() {
        JDialog dialog = new JDialog((Frame) null, "Temporary Account Lock", true);
        dialog.setSize(550, 420);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(Elements.BEIGE);

        // Title
        JLabel titleLabel = new JLabel("!#! TEMPORARY ACCOUNT LOCK - CONSEQUENCES:", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Elements.SEAWEED);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        dialog.add(titleLabel, BorderLayout.NORTH);

        JTextArea consequencesArea = new JTextArea(
                "1. You will not be able to perform withdrawals, deposits, or transfers.\n\n" +
                        "2. No one (including authorized users) will be able to access or manage the account.\n\n" +
                        "3. The lock will stay until you manually unlock it.\n\n" +
                        "4. You will receive an email confirmation for the lock.\n\n" +
                        "5. All scheduled operations will be paused."
        );
        consequencesArea.setWrapStyleWord(true);
        consequencesArea.setLineWrap(true);
        consequencesArea.setEditable(false);
        consequencesArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        consequencesArea.setForeground(Elements.SEAWEED);
        consequencesArea.setBackground(Elements.BEIGE);
        consequencesArea.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JScrollPane scrollPane = new JScrollPane(consequencesArea);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Elements.BEIGE);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Elements.BEIGE);

        JButton confirmBtn = new JButton("Confirm and Lock");
        JButton cancelBtn = new JButton("Cancel X");
        styleButton(confirmBtn, Elements.SEAWEED, Elements.CHAMPAGNE);
        styleButton(cancelBtn,Elements.SOFT_GREEN,Elements.CHAMPAGNE);

        buttonPanel.add(confirmBtn);
        buttonPanel.add(cancelBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        confirmBtn.addActionListener(e -> {
            if ("true".equals(card.unLocked)) {
                card.unLocked = "false";
                CardsUpdater.updateCard(card);
                dialog.dispose();
                showLockedMessage(null);
            } else {
                JOptionPane.showMessageDialog(null, "⚠️ Your account is already locked!");
            }
        });

        cancelBtn.addActionListener(e -> {
            dialog.dispose();
            JOptionPane.showMessageDialog(null,"Lock account has been cancelled");
        });
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void showLockedMessage(Frame parent) {
        JDialog msgDialog = new JDialog(parent, "Account Locked", true);
        msgDialog.setSize(500, 300);
        msgDialog.setLayout(new BorderLayout());
        msgDialog.setLocationRelativeTo(parent);
        msgDialog.getContentPane().setBackground(Elements.BEIGE);

        JTextArea messageArea = new JTextArea(
                "!#! Your account has been temporarily locked.\n\n" +
                        "√ No operations will be allowed until it is unlocked.\n\n" +
                        "√ All scheduled tasks are on hold.\n\n" +
                        "√ You can unlock your account by logging in and choosing the unlock option."
        );
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Segoe UI", Font.BOLD, 15));
        messageArea.setForeground(Elements.SEAWEED);
        messageArea.setBackground(Elements.BEIGE);
        messageArea.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(null);
        msgDialog.add(scrollPane, BorderLayout.CENTER);

        JButton okBtn = new JButton("OK");
        styleButton(okBtn,Elements.SOFT_GREEN,Elements.CHAMPAGNE);
        okBtn.addActionListener(e -> {
            card.unLocked = "false";
            CardsUpdater.updateCard(card);
            try {
                Email.sendEmail(card.email,"process done successfully!",
                        "Hello " + card.ownerName + ",\n\n" +
                                "Account Number: " + card.accountNumber + "\n" +
                                "Card Number: " + card.cardNumber + "\n\n" +
                                "Your account has been successfully locked.\n\n" +
                                "🔒 You will not be able to perform any transactions until it is unlocked.\n" +
                                "📌 For more details, please contact your bank.\n\n" +
                                "Best regards,\nFlexi ATM Support Team"
                );

            } catch (MessagingException ex) {
                throw new RuntimeException(ex);
            }
            msgDialog.dispose();
            JOptionPane.showMessageDialog(null,"⚠ Your account has been locked"+"\n\n"+"email has been sent to your Inbox");
        });

        JPanel panel = new JPanel();
        panel.setBackground(Elements.BEIGE);
        panel.add(okBtn);
        msgDialog.add(panel, BorderLayout.SOUTH);

        msgDialog.setResizable(false);
        msgDialog.setVisible(true);
    }

    public void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(fg.darker(), 1));
        button.setPreferredSize(new Dimension(180, 42));
    }
}
