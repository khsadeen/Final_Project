package Management.FeaturesManage;

import GUIjavaSwing.Elements;
import Management.CardsManager.Cards;
import Features.LockHandler;
import Features.Statements_papersData;
import Features.WithDraw;
import GUIjavaSwing.RoundedBorder;
import Main.MainMenu;

import javax.swing.*;
import java.awt.*;

public class FeaturesMenu {
    public JDialog dialog;

    public JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setFont(Elements.uniFont);
        return button;
    }

    public FeaturesMenu(Cards authenticatedCard) {
        dialog = new JDialog((Frame) null, "Available Features", true);
        dialog.setSize(450, 640);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(Elements.BEIGE);

        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon("src/assets/logo.png");
            Image image = icon.getImage().getScaledInstance(220, 200, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(image));
            logoLabel.setBorder(BorderFactory.createEmptyBorder(40, 160, 10, 160));
        } catch (Exception e) {
            System.out.println("❌ Could not load logo: " + e.getMessage());
        }

        JLabel titleLabel = new JLabel("Select a feature to continue", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Elements.SEAWEED);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 45, 15, 45));

        Box topPanel = Box.createVerticalBox();        // Top section (logo + title)
        topPanel.setBackground(Elements.BEIGE);
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        topPanel.add(logoLabel);
        topPanel.add(Box.createVerticalStrut(8));
        topPanel.add(titleLabel);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 15, 40));
        buttonPanel.setBackground(Elements.BEIGE);

        JButton withdrawButton = createStyledButton("Withdraw",Elements.SEAWEED,Elements.CHAMPAGNE);
        withdrawButton.setIcon(Elements.resizedWithdrawIcon);
        withdrawButton.setVerticalTextPosition(SwingConstants.CENTER);
        withdrawButton.setHorizontalTextPosition(SwingConstants.LEFT);
        withdrawButton.setBorder(new RoundedBorder(12));
        withdrawButton.addActionListener(e -> {
            dialog.dispose();
            new WithDraw(authenticatedCard);
        });

        JButton lockButton = createStyledButton("Lock Account",Elements.SEAWEED,Elements.CHAMPAGNE);
        lockButton.setIcon(Elements.resizedLockIcon);
        lockButton.setVerticalTextPosition(SwingConstants.CENTER);
        lockButton.setHorizontalTextPosition(SwingConstants.LEFT);
        lockButton.setBorder(new RoundedBorder(12));
        lockButton.addActionListener(e -> {
            dialog.dispose();
            new LockHandler(authenticatedCard);
        });

        JButton statementsButton = createStyledButton("Statements & Transactions",Elements.SEAWEED,Elements.CHAMPAGNE);
        statementsButton.setBorder(new RoundedBorder(12));
        statementsButton.setIcon(Elements.resizedStatementsIcon);
        statementsButton.setVerticalTextPosition(SwingConstants.CENTER);
        statementsButton.setHorizontalTextPosition(SwingConstants.LEFT);
        statementsButton.addActionListener(e -> {
            dialog.dispose();
            Statements_papersData.showStatementOptions(authenticatedCard);
        });

        JButton backButton = createStyledButton(" Back to Main Menu",Elements.SOFT_GREEN,Elements.CHAMPAGNE);
        backButton.setIcon(Elements.resizedBackIcon);
        backButton.setVerticalTextPosition(SwingConstants.CENTER);
        backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        backButton.setBorder(new RoundedBorder(12));
        backButton.addActionListener(e -> {
            dialog.dispose();
            MainMenu.screen();
        });

        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomLeftPanel.setBackground(Elements.BEIGE);
        bottomLeftPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JButton balanceButton = createStyledButton("Show Card Balance ₪",Elements.SOFT_GREEN,Elements.CHAMPAGNE);
        balanceButton.setFont(Elements.FONT_PLAIN_14);
        balanceButton.setBorder(new RoundedBorder(12));
        balanceButton.setFocusable(false);
        balanceButton.addActionListener(e ->
                JOptionPane.showMessageDialog(dialog, "Card Balance: " + authenticatedCard.balance+ "₪")
        );

        bottomLeftPanel.add(backButton);
//------------------------------------------------------
        buttonPanel.add(withdrawButton);
        buttonPanel.add(lockButton);
        buttonPanel.add(statementsButton);
        buttonPanel.add(balanceButton);
        // Add everything to the dialog
        dialog.add(topPanel, BorderLayout.NORTH);
        dialog.add(buttonPanel, BorderLayout.CENTER);
        dialog.add(bottomLeftPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
