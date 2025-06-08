import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import jakarta.mail.MessagingException;

import javax.swing.*;

public class Statements_papersData {
    //===================================================================================================

    public static JDialog dialog;

    static Color darkBrown = new Color(244, 250, 255);
    static Color gold = new Color(151, 23, 23);
    static Font uniFont = new Font("Arial", Font.BOLD, 14);

    public static JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setFont(uniFont);
        button.setBorder(BorderFactory.createLineBorder(fg));
        return button;
    }

    public static void showStatementOptions(Cards authenticatedCard) {
        dialog = new JDialog((Frame) null, "Available Features", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(darkBrown);

        JLabel titleLabel = new JLabel("Select a feature to continue:", SwingConstants.CENTER);
        titleLabel.setFont(uniFont);
        titleLabel.setForeground(gold);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        dialog.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        buttonPanel.setBackground(darkBrown);

        // Withdraw
        JButton withdrawButton = createStyledButton("Account data", gold, darkBrown);
        withdrawButton.addActionListener(e -> {
            dialog.dispose();
            Statements_papersData.printAndSendStatement(authenticatedCard);
        });

        // Lock Account
        JButton lockButton = createStyledButton("Operations summary between dates", gold, darkBrown);
        lockButton.addActionListener(e -> {
            dialog.dispose();
            Statements_papersData.operationsSummaryBetweenDates(authenticatedCard);
        });

        // Account Statements
        JButton statementsButton = createStyledButton("last 5 operations", gold, darkBrown);
        statementsButton.addActionListener(e -> {
            dialog.dispose();
            Statements_papersData.sendLastFiveOperations(authenticatedCard);
        });

        JButton accountTransferDetailsButton = createStyledButton("Account transfer details", gold, darkBrown);
        accountTransferDetailsButton.addActionListener(e -> {
            dialog.dispose();
            Statements_papersData.sendAccountTransferDetails(authenticatedCard);
        });


        // Back Button
        JButton backButton = createStyledButton("\uD83D\uDD19 Back to Main Menu", gold, darkBrown);
        backButton.addActionListener(e -> {
            dialog.dispose();
            MainMenu.screen();
        });

        buttonPanel.add(withdrawButton);
        buttonPanel.add(lockButton);
        buttonPanel.add(statementsButton);
        buttonPanel.add(accountTransferDetailsButton);
        buttonPanel.add(backButton);
        dialog.add(buttonPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }




    //====================================================================================================
    // Load account transaction data from file
    public static List<Account_data> loadAccountData() { //a loader for file (src/account_data.txt)
        List<Account_data> operations = new ArrayList<>();
        File file = new File("src/account_data.txt");

        if (!file.exists()) {
            System.out.println("⚠️ Transaction file not found: " + file.getAbsolutePath());
            return operations;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    String accNumber = parts[0];
                    String type = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    LocalDate date = LocalDate.parse(parts[3]);
                    operations.add(new Account_data(accNumber, type, amount, date));
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading transaction file: " + e.getMessage());
        }

        return operations;
    }
    //==================================================================================================================

    public static void operationsSummaryBetweenDates(Cards card) {
        if (card == null) {
            JOptionPane.showMessageDialog(null, "⚠️ No user is currently signed in.");
            return;
        }

        JDialog dateDialog = new JDialog((Frame) null, "Select Date Range", true);
        dateDialog.setSize(400, 250);
        dateDialog.setLayout(new BorderLayout());
        dateDialog.setLocationRelativeTo(null);
        dateDialog.getContentPane().setBackground(darkBrown);

        JLabel titleLabel = new JLabel("Enter date range (YYYY-MM-DD):", SwingConstants.CENTER);
        titleLabel.setFont(uniFont);
        titleLabel.setForeground(gold);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBackground(darkBrown);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JTextField fromField = new JTextField();
        JTextField toField = new JTextField();

        inputPanel.add(new JLabel("From Date:", SwingConstants.RIGHT));
        inputPanel.add(fromField);
        inputPanel.add(new JLabel("To Date:", SwingConstants.RIGHT));
        inputPanel.add(toField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(darkBrown);

        JButton submitButton = createStyledButton("Submit", gold, darkBrown);
        JButton backButton = createStyledButton("<- Back", gold, darkBrown);

        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);

        dateDialog.add(titleLabel, BorderLayout.NORTH);
        dateDialog.add(inputPanel, BorderLayout.CENTER);
        dateDialog.add(buttonPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> {
            try {
                LocalDate fromDate = LocalDate.parse(fromField.getText().trim());
                LocalDate toDate = LocalDate.parse(toField.getText().trim());

                List<Account_data> operations = loadAccountData();
                String accountNumber = card.accountNumber;

                StringBuilder message = new StringBuilder();
                message.append("🧾 Operations Summary for Account: ").append(accountNumber).append("\n")
                        .append("Between ").append(fromDate).append(" and ").append(toDate).append("\n")
                        .append("------------------------------------------------\n");

                boolean found = false;
                for (Account_data op : operations) {
                    if (op.accountNumber.equals(accountNumber) &&
                            !op.dateOperation.isBefore(fromDate) &&
                            !op.dateOperation.isAfter(toDate)) {

                        message.append(op.dateOperation).append(" | ")
                                .append(op.type).append(" | ")
                                .append(op.amount).append("\n");
                        found = true;
                    }
                }

                if (!found) {
                    message.append("No operations found in this date range.\n");
                }

                message.append("------------------------------------------------\n");

                // Send the email
                try {
                    Email.sendEmail(card.email, "Operations Summary", message.toString());
                    JOptionPane.showMessageDialog(dateDialog,
                            "📧 Email sent successfully to " + card.email,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dateDialog,
                            "❌ Failed to send email: " + ex.getMessage(),
                            "Email Error", JOptionPane.ERROR_MESSAGE);
                }

                dateDialog.dispose();

                // Show summary in a new dialog
                JDialog resultDialog = new JDialog((Frame) null, "Operations Summary", true);
                resultDialog.setSize(500, 350);
                resultDialog.setLayout(new BorderLayout());
                resultDialog.setLocationRelativeTo(null);
                resultDialog.getContentPane().setBackground(darkBrown);

                JTextArea textArea = new JTextArea(message.toString());
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
                textArea.setBackground(new Color(60, 40, 30));
                textArea.setForeground(Color.WHITE);
                textArea.setMargin(new Insets(10, 10, 10, 10));

                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                resultDialog.add(scrollPane, BorderLayout.CENTER);

                JPanel bottomPanel = new JPanel();
                bottomPanel.setBackground(darkBrown);
                JButton closeButton = createStyledButton("Close", gold, darkBrown);
                bottomPanel.add(closeButton);
                resultDialog.add(bottomPanel, BorderLayout.SOUTH);
                JButton back1Button = createStyledButton("<- back",gold,darkBrown);
                resultDialog.add(back1Button);

                closeButton.addActionListener(ev -> {
                    resultDialog.dispose();
                    MainMenu.screen();
                });
                back1Button.addActionListener(ev -> {
                    resultDialog.dispose();
                    showStatementOptions(card);
                });

                resultDialog.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dateDialog,
                        "❌ Invalid date format. Please enter dates in YYYY-MM-DD format.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            dateDialog.dispose();
            showStatementOptions(card);
        });

        dateDialog.setVisible(true);
    }
//======================================================================================================================

    public static void printAndSendStatement(Cards card) {
        if (card == null) {
            JOptionPane.showMessageDialog(null, "⚠️ No user is currently signed in.");
            return;
        }

        String statement = "📄 Account Statement\n"
                + "------------------------------\n"
                + "Owner Name: " + card.ownerName + "\n"
                + "Account Number: " + card.accountNumber + "\n"
                + "Balance: " + card.balance + "\n"
                + "------------------------------\n"
                + "Thank you for using our service.";

        // Send email
        try {
            Email.sendEmail(card.email, "Account Statement", statement);
            JOptionPane.showMessageDialog(null,
                    "📧 Email sent successfully to " + card.email,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (MessagingException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to send email: " + e.getMessage(),
                    "Email Error", JOptionPane.ERROR_MESSAGE);
        }

        // Show statement in GUI
        JDialog dialog = new JDialog((Frame) null, "Account Statement", true);
        dialog.setSize(500, 300);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(darkBrown);

        JTextArea textArea = new JTextArea(statement);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setBackground(new Color(60, 40, 30));
        textArea.setForeground(Color.WHITE);
        textArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(darkBrown);
        JButton backButton = createStyledButton("<- Back", gold, darkBrown);
        bottomPanel.add(backButton);
        JButton backToMainButton = createStyledButton("<- Back to Main page",gold,darkBrown);
        bottomPanel.add(backToMainButton);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            dialog.dispose();
            showStatementOptions(card);
        });

        backToMainButton.addActionListener(e -> {
            dialog.dispose();
            MainMenu.screen();
        });

        dialog.setVisible(true);
    }

    //======================================================================================================================
public static void sendLastFiveOperations(Cards card) {
    if (card == null) {
        JOptionPane.showMessageDialog(null, "⚠️ No user is currently signed in.");
        return;
    }

    // Create Dialog
    JDialog dialog = new JDialog((Frame) null, "Last 5 Transactions", true);
    dialog.setSize(500, 300);
    dialog.setLayout(new BorderLayout());
    dialog.setLocationRelativeTo(null);
    dialog.getContentPane().setBackground(darkBrown);

    // Title
    JLabel titleLabel = new JLabel("📄 Last 5 Transactions", SwingConstants.CENTER);
    titleLabel.setFont(uniFont);
    titleLabel.setForeground(gold);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
    dialog.add(titleLabel, BorderLayout.NORTH);

    // Text Area
    JTextArea textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
    textArea.setBackground(new Color(60, 40, 30));
    textArea.setForeground(Color.WHITE);
    textArea.setMargin(new Insets(10, 10, 10, 10));

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    dialog.add(scrollPane, BorderLayout.CENTER);

    // Buttons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    buttonPanel.setBackground(darkBrown);

    JButton backButton = createStyledButton("\uD83D\uDD19 Back", gold, darkBrown);
    buttonPanel.add(backButton);
    dialog.add(buttonPanel, BorderLayout.SOUTH);
    JButton backToMainButton = createStyledButton("<- Back to Main page",gold,darkBrown);
    buttonPanel.add(backToMainButton);

    // Load and display data
    List<Account_data> operations = loadAccountData();
    String accountNumber = card.accountNumber;

    List<Account_data> reversed = new ArrayList<>();
    for (int i = operations.size() - 1; i >= 0; i--) {
        reversed.add(operations.get(i));
    }

    StringBuilder message = new StringBuilder();
    message.append("📄 Last 5 Transactions for Account: ").append(accountNumber).append("\n");
    message.append("------------------------------------------------\n");

    int count = 0;
    for (Account_data op : reversed) {
        if (op.accountNumber.equals(accountNumber)) {
            message.append(op.dateOperation).append(" | ")
                    .append(op.type).append(" | ")
                    .append(op.amount).append("\n");
            count++;
            if (count == 5) break;
        }
    }

    if (count == 0) {
        message.append("No recent operations found.\n");
    }

    message.append("------------------------------------------------\n");

    // Display in text area
    textArea.setText(message.toString());

    // Try sending email
    try {
        Email.sendEmail(card.email, "Last 5 Transactions", message.toString());
        JOptionPane.showMessageDialog(dialog,
                "📧 Email sent successfully to " + card.email,
                "Success", JOptionPane.INFORMATION_MESSAGE);
    } catch (MessagingException e) {
        JOptionPane.showMessageDialog(dialog,
                "❌ Failed to send email: " + e.getMessage(),
                "Email Error", JOptionPane.ERROR_MESSAGE);
    }

    // Back button action
    backButton.addActionListener(e -> {
        dialog.dispose();
        showStatementOptions(card);
    });
    backToMainButton.addActionListener(e -> {
        dialog.dispose();
        MainMenu.screen();
    });

    // Show GUI
    dialog.setVisible(true);
}

//======================================================================================================================

    public static void sendAccountTransferDetails(Cards card) {
        if (card == null) {
            JOptionPane.showMessageDialog(null, "⚠️ No user is currently signed in.");
            return;
        }

        // Create Dialog
        JDialog dialog = new JDialog((Frame) null, "Account Transfer Details", true);
        dialog.setSize(450, 250);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(darkBrown);

        // Title Label
        JLabel titleLabel = new JLabel("📨 Account Transfer Details", SwingConstants.CENTER);
        titleLabel.setFont(uniFont);
        titleLabel.setForeground(gold);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        dialog.add(titleLabel, BorderLayout.NORTH);

        // Message Area
        JTextArea messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        messageArea.setBackground(new Color(60, 40, 30));
        messageArea.setForeground(Color.WHITE);
        messageArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(darkBrown);
        JButton backButton = createStyledButton("<- Back", gold, darkBrown);
        buttonPanel.add(backButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        JButton backToMainButton = createStyledButton("<- Back to Main page",gold,darkBrown);
        buttonPanel.add(backToMainButton);

        // Build message
        StringBuilder message = new StringBuilder();
        message.append("📨 Account Transfer Details:\n\n")
                .append("Full Name: ").append(card.ownerName).append("\n")
                .append("Account Number: ").append(card.accountNumber).append("\n")
                .append("Bank & Branch: ").append(card.bankBranch).append("\n");

        messageArea.setText(message.toString());

        // Send email
        try {
            Email.sendEmail(card.email, "Account Transfer Details", message.toString());
            JOptionPane.showMessageDialog(dialog,
                    "📧 Email sent successfully to " + card.email,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog,
                    "❌ Failed to send email: " + e.getMessage(),
                    "Email Error", JOptionPane.ERROR_MESSAGE);
        }

        // Back button action
        backButton.addActionListener(e -> {
            dialog.dispose();
            showStatementOptions(card);
        });
        backToMainButton.addActionListener(e -> {
            dialog.dispose();
            MainMenu.screen();
        });


        // Show dialog
        dialog.setVisible(true);
    }

}
