package Features;

import Management.AccountManager.Account_data;
import GUIjavaSwing.Elements;
import GUIjavaSwing.RoundedBorder;
import Management.CardsManager.Cards;
import Management.CardsManager.CardsLoader;
import Emails.Email;
import Main.MainMenu;
import jakarta.mail.MessagingException;

import javax.swing.*; //for dialogs, buttons .... more general
import java.awt.*;// for colors, components, alignments... for details
import java.io.BufferedReader; // to read the file line by line fast
import java.io.File;// for files
import java.io.FileReader;// to read the file char by char, and it works w buffered reader
import java.io.IOException;//mistakes
import java.time.LocalDate;//to save the date only
import java.util.ArrayList;//oop, from List
import java.util.List;

public class Statements_papersData {
    //===================================================================================================
    public static JDialog dialog;

    public static JButton createStyledButton(String text, Color bg, Color fg,ImageIcon icon) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(fg); //change the font color on the button
        button.setFocusPainted(false);
        button.setFont(Elements.uniFont);
        button.setBorder(BorderFactory.createLineBorder(fg));
        button.setIcon(icon);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.LEFT);
        return button;
    }
    public static void showStatementOptions(Cards authenticatedCard) {
        dialog = new JDialog((Frame) null, "Available Features", true);
        dialog.setSize(460, 640);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(Elements.BEIGE);

        try {
            ImageIcon icon = new ImageIcon("src/assets/logo.png");
            Image scaled = icon.getImage().getScaledInstance(170, 130, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaled));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
            dialog.add(imageLabel, BorderLayout.NORTH);
        } catch (Exception e) {
            System.out.println("X Failed to load image: " + e.getMessage());
        }

        // Title label
        JLabel titleLabel = new JLabel("Select a feature to continue:", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Elements.SEAWEED);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Elements.BEIGE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JButton accountData = createStyledButton(" Account Data",Elements.SEAWEED, Elements.CHAMPAGNE, Elements.resizedAccountInfoIcon);

        JButton operationsSummaryBetweenDates = createStyledButton(" Operations Between Dates",Elements.SEAWEED,Elements.CHAMPAGNE,
                Elements.resizedOperations_2datesIcon);

        JButton statementsButton = createStyledButton(" Last 5 Operations",Elements.SEAWEED,Elements.CHAMPAGNE, Elements.resizedLast5OperationsIcon);


        JButton accountTransferDetailsButton = createStyledButton(" Transfer Details",Elements.SEAWEED,Elements.CHAMPAGNE, Elements.resizedTransInfIcon);

        JButton MonthlyBalanceComparisonButton = createStyledButton(" Monthly Balance Comparison",Elements.SEAWEED,Elements.CHAMPAGNE, Elements.resizedBalanceCompIcon);

        JButton backButton = createStyledButton("Back to Main Menu",Elements.SOFT_GREEN,Elements.CHAMPAGNE, Elements.resizedBackIcon);
        backButton.setFont(new Font("Ariel",Font.PLAIN,14));
        backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        // Set button sizes and padding
        JButton[] buttons = {
                accountData,
                operationsSummaryBetweenDates,
                statementsButton,
                accountTransferDetailsButton,
                MonthlyBalanceComparisonButton,
                backButton
        };
        for (JButton btn : buttons) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(330, 40));
            btn.setBorder(new RoundedBorder(12));
            btn.setFocusPainted(false);
            buttonPanel.add(btn);
            buttonPanel.add(Box.createVerticalStrut(10)); // Space between buttons
        }
    backButton.setPreferredSize(new Dimension(300,37));
        backButton.setFont(new Font("Arial", Font.PLAIN, 10));
        buttonPanel.add(Box.createVerticalGlue());
        dialog.add(titleLabel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        accountData.addActionListener(e -> {
            dialog.dispose();
            Statements_papersData.printAndSendStatement(authenticatedCard);
        });

        operationsSummaryBetweenDates.addActionListener(e -> {
            dialog.dispose();
            Statements_papersData.operationsSummaryBetweenDates(authenticatedCard);
        });

        statementsButton.addActionListener(e -> {
            dialog.dispose();
            Statements_papersData.sendLastFiveOperations(authenticatedCard);
        });

        accountTransferDetailsButton.addActionListener(e -> {
            dialog.dispose();
            Statements_papersData.sendAccountTransferDetails(authenticatedCard);
        });

        MonthlyBalanceComparisonButton.addActionListener(e -> {
            dialog.dispose();
            Statements_papersData.showMonthlyBalanceComparison(authenticatedCard, loadAccountData());
        });

        backButton.addActionListener(e -> {
            dialog.dispose();
            MainMenu.screen();
        });

        dialog.setVisible(true);
    }
    //====================================================================================================
    // Load account transaction data from file
    public static List<Account_data> loadAccountData() { //a loader for file (src/account_data.txt)
        List<Account_data> operations = new ArrayList<>();
        File file = new File("src/DataBase/account_data.txt");

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
            System.out.println("X Error reading transaction file: " + e.getMessage());
        }

        return operations;
    }
    //==================================================================================================================
    public static void showMonthlyBalanceComparison(Cards card, List<Account_data> operations) {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        double startOfMonthBalance = 0;

        for (Account_data op : operations) {
            if (op.accountNumber.equals(card.accountNumber) && op.dateOperation.isBefore(firstDayOfMonth)) {
                if (op.type.equalsIgnoreCase("Deposit")) {
                    startOfMonthBalance += op.amount;
                } else if (op.type.equalsIgnoreCase("Withdraw")) {
                    startOfMonthBalance -= op.amount;
                }
            }
        }

        CardsLoader loader = new CardsLoader();
        List<Cards> cardList = loader.cardsListing();

        double currentBalance = 0;
        String userEmail = null;
        boolean found = false;

        for (Cards c : cardList) {
            if (card.accountNumber.equals(c.accountNumber)) {
                currentBalance = c.balance;
                userEmail = c.email;
                found = true;
                break;
            }
        }
        StringBuilder message = new StringBuilder();
        message.append("📊 Monthly Balance Comparison\n\n");
        message.append("Account Number: ").append(card.accountNumber).append("\n");
        message.append("Start of Month Balance: ₪").append(startOfMonthBalance).append("\n");
        message.append("Current Balance: ₪").append(currentBalance).append("\n\n");

        if (!found) {
            message.append("⚠️ Account not found in cards list.\n");
        } else if (currentBalance > startOfMonthBalance) {
            message.append("✅ Your balance increased this month.\n");
        } else if (currentBalance < startOfMonthBalance) {
            message.append("⚠️ Your balance decreased this month.\n");
        } else {
            message.append("ℹ️ No change in your balance this month.\n");
        }

        // Try to send email
        if (found && userEmail != null) {
            try {
                Email.sendEmail(userEmail, "Monthly Balance Comparison", message.toString());
            } catch (Exception e) {
                message.append("\n❌ Failed to send email: ").append(e.getMessage()).append("\n");
            }
        }

        // Show in dialog
        JDialog dialog = new JDialog((Frame) null, "Monthly Summary", true);
        dialog.setSize(500, 350);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(Elements.BEIGE);

        JTextArea area = new JTextArea(message.toString());
        area.setEditable(false);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setFont(new Font("Monospaced", Font.BOLD, 13));
        area.setForeground(Elements.CHAMPAGNE);
        area.setBackground(Elements.SEAWEED);
        area.setMargin(new Insets(10, 15, 10, 15));

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeBtn = createStyledButton("Close X",Elements.BEIGE,Elements.SOFT_GREEN,null);
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Elements.BEIGE);
        bottomPanel.add(closeBtn);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    public static void operationsSummaryBetweenDates(Cards card) {
        if (card == null) {
            JOptionPane.showMessageDialog(null, "⚠! No user is currently signed in.");
            return;
        }

        JDialog dateDialog = new JDialog((Frame) null, "Select Date Range", true);
        dateDialog.setSize(400, 250);
        dateDialog.setLayout(new BorderLayout());
        dateDialog.setLocationRelativeTo(null);
        dateDialog.getContentPane().setBackground(Elements.BEIGE);

        JLabel titleLabel = new JLabel("Enter date range (YYYY-MM-DD):", SwingConstants.CENTER);
        titleLabel.setFont(Elements.uniFont);
        titleLabel.setForeground(Elements.SEAWEED);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBackground(Elements.BEIGE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JTextField fromField = new JTextField();
        JTextField toField = new JTextField();

        inputPanel.add(new JLabel("From Date:", SwingConstants.RIGHT));
        inputPanel.add(fromField);
        inputPanel.add(new JLabel("To Date:", SwingConstants.RIGHT));
        inputPanel.add(toField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Elements.BEIGE);
        JButton submitButton = createStyledButton("Submit √",Elements.SEAWEED,Elements.CHAMPAGNE,null);
        JButton backButton = createStyledButton("Back",Elements.SEAWEED,Elements.CHAMPAGNE, Elements.resizedBackIcon);
        backButton.setHorizontalTextPosition(SwingConstants.RIGHT);

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
                resultDialog.getContentPane().setBackground(Elements.BEIGE);

                JTextArea textArea = new JTextArea(message.toString());
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospaced", Font.BOLD, 13));
                textArea.setBackground(Elements.SOFT_GREEN);
                textArea.setForeground(Elements.CHAMPAGNE);
                textArea.setMargin(new Insets(10, 10, 10, 10));

                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                resultDialog.add(scrollPane, BorderLayout.CENTER);

                JPanel bottomPanel = new JPanel();
                bottomPanel.setBackground(Elements.BEIGE);
                JButton closeButton = createStyledButton("Close",Elements.SEAWEED,Elements.CHAMPAGNE,null);
                bottomPanel.add(closeButton);
                resultDialog.add(bottomPanel, BorderLayout.SOUTH);
                JButton back1Button = createStyledButton(" back",Elements.SEAWEED,Elements.CHAMPAGNE, Elements.resizedBackIcon);
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
                + "Balance: " + card.balance + "₪\n"
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
        dialog.getContentPane().setBackground(Elements.BEIGE);

        JTextArea textArea = new JTextArea(statement);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.BOLD, 13));
        textArea.setBackground(Elements.SOFT_GREEN);
        textArea.setForeground(Elements.CHAMPAGNE);
        textArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Elements.BEIGE);
        JButton backButton = createStyledButton("Back",Elements.SEAWEED,Elements.CHAMPAGNE, Elements.resizedBackIcon);
        backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        bottomPanel.add(backButton);
        JButton backToMainButton = createStyledButton("Back to Main page",Elements.SEAWEED,Elements.CHAMPAGNE, Elements.resizedBackIcon);
        backToMainButton.setHorizontalTextPosition(SwingConstants.RIGHT);
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
    JDialog dialog = new JDialog((Frame) null, "Last 5 Transactions", true);
    dialog.setSize(500, 300);
    dialog.setLayout(new BorderLayout());
    dialog.setLocationRelativeTo(null);
    dialog.getContentPane().setBackground(Elements.BEIGE);

    JLabel titleLabel = new JLabel("Last 5 Transactions", SwingConstants.CENTER);
    titleLabel.setFont(Elements.uniFont);
    titleLabel.setForeground(Elements.SEAWEED);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
    dialog.add(titleLabel, BorderLayout.NORTH);

    JTextArea textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setFont(new Font("Monospaced", Font.BOLD, 14));
    textArea.setBackground(Elements.BEIGE);
    textArea.setForeground(Color.BLACK);
    textArea.setMargin(new Insets(10, 10, 10, 10));

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    scrollPane.setBackground(Elements.BEIGE);
    scrollPane.setForeground(Color.BLACK);
    scrollPane.setFont(new Font("Monospaced", Font.BOLD, 16));
    dialog.add(scrollPane, BorderLayout.CENTER);
    // Buttons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    buttonPanel.setBackground(Elements.BEIGE);

    JButton backButton = createStyledButton(" Back",Elements.SEAWEED,Elements.CHAMPAGNE, Elements.resizedBackIcon);
    backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
    buttonPanel.add(backButton);
    dialog.add(buttonPanel, BorderLayout.SOUTH);
    JButton backToMainButton = createStyledButton(" Back to Main page",Elements.SEAWEED,Elements.CHAMPAGNE, Elements.resizedBackIcon);
    backToMainButton.setHorizontalTextPosition(SwingConstants.RIGHT);
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
    dialog.setVisible(true);
}
//======================================================================================================================
    public static void sendAccountTransferDetails(Cards card) {
        if (card == null) {
            JOptionPane.showMessageDialog(null, "⚠! No user is currently signed in.");
            return;
        }
        JDialog dialog = new JDialog((Frame) null, "Account Transfer Details", true);
        dialog.setSize(450, 250);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(Elements.BEIGE);

        JLabel titleLabel = new JLabel("📨 Account Transfer Details", SwingConstants.CENTER);
        titleLabel.setFont(Elements.uniFont);
        titleLabel.setForeground(Elements.SEAWEED);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        dialog.add(titleLabel, BorderLayout.NORTH);

        JTextArea messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        messageArea.setBackground(Elements.SOFT_GREEN);
        messageArea.setForeground(Elements.CHAMPAGNE);
        messageArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Elements.BEIGE);
        JButton backButton = createStyledButton(" Back",Elements.SEAWEED,Elements.CHAMPAGNE, Elements.resizedBackIcon);
        backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        backButton.setIcon(Elements.resizedBackIcon);
        backButton.setVerticalTextPosition(SwingConstants.CENTER);
        backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        buttonPanel.add(backButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        JButton backToMainButton = createStyledButton("Back to Main page",Elements.SEAWEED,Elements.CHAMPAGNE, Elements.resizedBackIcon);
        backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        buttonPanel.add(backToMainButton);

        StringBuilder message = new StringBuilder();
        message.append("📨 Account Transfer Details:\n\n")
                .append("Full Name: ").append(card.ownerName).append("\n")
                .append("Account Number: ").append(card.accountNumber).append("\n")
                .append("Bank & Branch: ").append(card.bankBranch).append("\n");

        messageArea.setText(message.toString());
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
}
