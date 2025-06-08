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
        dialog.setSize(500, 380);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);

        Color darkBrown = new Color(68, 16, 10);
        Color gold = new Color(155, 124, 68);
        dialog.getContentPane().setBackground(darkBrown);

        JLabel titleLabel = new JLabel("⚠️ TEMPORARY ACCOUNT LOCK - CONSEQUENCES:", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(gold);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        dialog.add(titleLabel, BorderLayout.NORTH);

        JTextArea consequencesArea = new JTextArea(
                "1. You will not be able to perform withdrawals, deposits, or transfers.\n" +
                        "2. No one (including authorized users) will be able to access or manage the account.\n" +
                        "3. The lock will stay until you manually unlock it.\n" +
                        "4. You will receive an email confirmation for the lock.\n" +
                        "5. All scheduled operations will be paused."
        );
        consequencesArea.setEditable(false);
        consequencesArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        consequencesArea.setForeground(gold);
        consequencesArea.setBackground(darkBrown);
        consequencesArea.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        dialog.add(consequencesArea, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(darkBrown);

        JButton confirmBtn = new JButton("Confirm and Lock");
        JButton cancelBtn = new JButton("Cancel");
        styleButton(confirmBtn, darkBrown, gold);
        styleButton(cancelBtn, darkBrown, gold);

        buttonPanel.add(confirmBtn);
        buttonPanel.add(cancelBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        confirmBtn.addActionListener(e -> {
            if (card.unLocked.equals("true")){
                card.unLocked ="false";
            CardsUpdater.updateCard(card);
            dialog.dispose();
            showLockedMessage(null);
        }
            else{
                JOptionPane.showMessageDialog(null, "Lock account failed failed, Your account is already locked!");
            }

        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void showLockedMessage(Frame parent) {
        Color darkBrown = new Color(68, 16, 10);
        Color gold = new Color(155, 124, 68);

        JDialog msgDialog = new JDialog(parent, "Account Locked", true);
        msgDialog.setSize(450, 300);
        msgDialog.setLayout(new BorderLayout());
        msgDialog.setLocationRelativeTo(parent);
        msgDialog.getContentPane().setBackground(darkBrown);

        JTextArea messageArea = new JTextArea(
                "🔒 Your account has been temporarily locked.\n\n" +
                        "❌ No operations will be allowed until it is unlocked.\n" +
                        "⏸ All scheduled tasks are on hold.\n" +
                        "🔐 You can unlock your account by logging in and choosing the unlock option."
        );
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageArea.setForeground(gold);
        messageArea.setBackground(darkBrown);
        messageArea.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        msgDialog.add(messageArea, BorderLayout.CENTER);

        JButton okBtn = new JButton("OK");
        styleButton(okBtn, darkBrown, gold);
        okBtn.addActionListener(e -> {
            card.unLocked = "false";                // 1. تأكيد القفل
            CardsUpdater.updateCard(card);        // 2. تحديث الملف بعد OK
            msgDialog.dispose();                  // 3. إغلاق الرسالة
        });

        JPanel panel = new JPanel();
        panel.setBackground(darkBrown);
        panel.add(okBtn);
        msgDialog.add(panel, BorderLayout.SOUTH);

        msgDialog.setResizable(false);
        msgDialog.setVisible(true);
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(fg.darker(), 1));
        button.setPreferredSize(new Dimension(160, 40));
    }
}
