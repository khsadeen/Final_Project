import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class
MainMenu {
    private JFrame frame;
    private static boolean isBackPressed = false;

    private static final Color DARK_BROWN = new Color(244, 250, 255);
    private static final Color GOLD = new Color(151, 23, 23);
    private static final Font FONT_BOLD_16 = new Font("Segoe UI", Font.BOLD, 16);

    public MainMenu() {
        frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 540);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panel.setBackground(DARK_BROWN);

        try {
            ImageIcon icon = new ImageIcon("src/project.Images/Welcome.png");
            Image scaled = icon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaled));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
            panel.add(imageLabel);
        } catch (Exception e) {
            System.out.println("❌ Failed to load image: " + e.getMessage());
        }

        JLabel titleLabel = new JLabel("Select an Operation");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(GOLD);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        panel.add(titleLabel);

        JButton tempVisaButton = createStyledButton("Temporary Visa Card");
        tempVisaButton.addActionListener(e -> {
            frame.dispose();
            showTempVisaOptions();
        });
        panel.add(tempVisaButton);
        panel.add(Box.createRigidArea(new Dimension(0, 18)));

        JButton realVisaButton = createStyledButton("Real Visa Card Operations");
        realVisaButton.addActionListener(e -> {
            frame.dispose();
            showRealVisaLoginOptions();
        });
        panel.add(realVisaButton);

        frame.add(panel);
        frame.getContentPane().setBackground(DARK_BROWN);
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(240, 50));
        button.setMaximumSize(new Dimension(240, 50));
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(GOLD);
        button.setForeground(DARK_BROWN);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color originalBackground = button.getBackground();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(originalBackground.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBackground);
            }
        });

        return button;
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, "❌ " + message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, "✅ " + message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, "⚠️ " + message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    public static int showSourceCardLoginOption(Component parent) {
        JDialog dialog = new JDialog((Frame) null, "Source Card Login", true);
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(DARK_BROWN);

        JLabel label = new JLabel("Login with source card:", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(FONT_BOLD_16);
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));
        dialog.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(DARK_BROWN);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton visaInputBtn = new JButton("Visa Card Input");
        JButton standardSignInBtn = new JButton("Standard Sign In");

        final int[] selection = {-1};

        visaInputBtn.addActionListener(e -> {
            selection[0] = 0;
            dialog.dispose();
        });

        standardSignInBtn.addActionListener(e -> {
            selection[0] = 1;
            dialog.dispose();
        });

        buttonPanel.add(visaInputBtn);
        buttonPanel.add(standardSignInBtn);

        dialog.add(buttonPanel, BorderLayout.CENTER);
        dialog.setVisible(true);

        return selection[0];
    }

    public static void showTempVisaOptions() {
        JDialog dialog = new JDialog((Frame) null, "Temporary Visa Card Options", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);

        JLabel label = new JLabel("Please select an option:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);

        JButton createButton = new JButton("Create New Temporary Visa Card");
        createButton.setBackground(GOLD);
        createButton.setForeground(DARK_BROWN);

        JButton loginButton = new JButton("Sign In to Existing Temporary Visa Card");
        loginButton.setBackground(GOLD);
        loginButton.setForeground(DARK_BROWN);

        JButton backButton = new JButton("🔙 Back");
        backButton.setBackground(DARK_BROWN);
        backButton.setForeground(GOLD);

        createButton.addActionListener(e -> {
            dialog.dispose();
            new TempVisaCardCreator();
        });

        loginButton.addActionListener(e -> {
            dialog.dispose();

            TempVisaCard tempCard = TempVisaLogin.showLogin();
            if (tempCard == null) {
                showError(null, "Temporary Visa login cancelled or failed.");
                return;
            }

            Cards sourceCard = null;
            int option = showSourceCardLoginOption(null);

            if (option == 0) {
                sourceCard = VisaCardInput.showVisaInput();
            } else if (option == 1) {
                sourceCard = StandardLoginGUI.showStandardLogin();
            }

            if (sourceCard == null) {
                showError(null, "Source card login cancelled or failed.");
                return;
            }

            new TransferToTempCard(sourceCard, tempCard.cardNumber);
        });

        backButton.addActionListener(e -> {
            dialog.dispose();
            screen();
        });

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(DARK_BROWN);
        buttonPanel.add(createButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);

        dialog.getContentPane().setBackground(DARK_BROWN);
        dialog.add(label, BorderLayout.NORTH);
        dialog.add(buttonPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    public static void showRealVisaLoginOptions() {
        JDialog dialog = new JDialog((Frame) null, "Real Visa Card Login", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);

        JLabel label = new JLabel("Choose your login method:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.WHITE);

        dialog.add(label, BorderLayout.NORTH);
        dialog.getContentPane().setBackground(DARK_BROWN);

        JButton visaInputButton = new JButton("Visa Card Input");
        visaInputButton.setFont(new Font("Arial", Font.BOLD, 15));
        visaInputButton.setBackground(GOLD);
        visaInputButton.setForeground(DARK_BROWN);

        JButton standardSignInButton = new JButton("Standard Sign In");
        standardSignInButton.setFont(new Font("Arial", Font.BOLD, 15));
        standardSignInButton.setBackground(GOLD);
        standardSignInButton.setForeground(DARK_BROWN);

        JButton backButton = new JButton("🔙 Back to Main page");
        backButton.setBackground(DARK_BROWN);
        backButton.setForeground(GOLD);

        visaInputButton.addActionListener(e -> {
            dialog.dispose();
            Cards authenticated = VisaCardInput.showVisaInput();
            if (authenticated != null) {
                dialog.dispose();
                if(authenticated.unLocked.equals("true")) {
                    new FeaturesMenu(authenticated);
                }
                else {
                    JOptionPane.showMessageDialog(null,"your account is locked, go to bank service!");
                }
            }
        });

        standardSignInButton.addActionListener(e -> {
            dialog.dispose();
            Cards authenticated = StandardLoginGUI.showStandardLogin();
            if (authenticated != null) {
                dialog.dispose();
                if(authenticated.unLocked.equals("true")) {
                    new FeaturesMenu(authenticated);
                }
                else {
                    JOptionPane.showMessageDialog(null,"your account is locked, go to bank service!");
                }
            }
        });

        backButton.addActionListener(e -> {
            dialog.dispose();
            screen();
        });


        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.add(visaInputButton);
        buttonPanel.add(standardSignInButton);
        buttonPanel.add(backButton);
        buttonPanel.setBackground(DARK_BROWN);

        dialog.add(buttonPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    public static void showFeaturesScreen(Cards authenticatedCard) {
        JDialog dialog = new JDialog((Frame) null, "Available Features", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);

        JLabel label = new JLabel("Select a feature to continue:", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        dialog.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton backButton = new JButton("🔙 Back to Main Menu");
        backButton.addActionListener(e -> {
            dialog.dispose();
            MainMenu.screen();
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    public static void screen() {
        SwingUtilities.invokeLater(MainMenu::new);
    }
}