package Main;
//importing classes from other packages
import Features.TempVisaWithDraw;
import GUIjavaSwing.Elements;
import Management.CardsManager.Cards;
import Management.FeaturesManage.FeaturesMenu;
import GUIjavaSwing.RoundedBorder;
import Management.SignInManager.StandardLoginGUI;
import Management.SignInManager.VisaCardInput;
import Management.TempVisaManager.TempVisaCard;
import Management.TempVisaManager.TempVisaCardCreator;
import Management.TempVisaManager.TempVisaLogin;

//libraries used in this class
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu {
    private JFrame frame;
    public static Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);

    public MainMenu() {
        frame = new JFrame("Flexi ATM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 580);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setBackground(Elements.BEIGE);

        // Logo
        try {
            ImageIcon icon = new ImageIcon("src/assets/logo.png");
            Image scaled = icon.getImage().getScaledInstance(270, 220, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaled));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
            panel.add(imageLabel);
        } catch (Exception e) {
            System.out.println("❌ Failed to load image: " + e.getMessage());
        }

        // Title
        JLabel titleLabel = new JLabel("Select an Operation");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(Elements.SEAWEED);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(titleLabel);

        // Buttons
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton tempCardBtn = createStyledButton("Temporary Visa Card",Elements.SEAWEED,Elements.CHAMPAGNE);
        ImageIcon tempIcon = new ImageIcon("src/assets/tempCardIcon.png");
        Image scaledTempImg = tempIcon.getImage().getScaledInstance(90, 50, Image.SCALE_SMOOTH);
        ImageIcon resizedTempIcon = new ImageIcon(scaledTempImg);
        tempCardBtn.setIcon(resizedTempIcon);
        tempCardBtn.setVerticalTextPosition(SwingConstants.CENTER);
        tempCardBtn.setHorizontalTextPosition(SwingConstants.LEFT);
        tempCardBtn.setBorder(BorderFactory.createEmptyBorder(20,10,20,10));
        tempCardBtn.setBorder(new RoundedBorder(12));
        panel.add(tempCardBtn);
        tempCardBtn.setBorder(BorderFactory.createEmptyBorder(20,10,20,10));
        tempCardBtn.setBorder(new RoundedBorder(12));
        tempCardBtn.addActionListener(e -> {
            frame.dispose();
            showTempVisaOptions();
        });

        panel.add(Box.createRigidArea(new Dimension(0, 18)));
        JButton realCardOperations = createStyledButton("Real Visa Card Operations",Elements.SEAWEED,Elements.CHAMPAGNE);
        ImageIcon realIcon = new ImageIcon("src/assets/realCard.png");
        Image scaledRealImg = realIcon.getImage().getScaledInstance(50, 40, Image.SCALE_SMOOTH);
        ImageIcon resizedRealIcon = new ImageIcon(scaledRealImg);
        realCardOperations.setIcon(resizedRealIcon);
        realCardOperations.setVerticalTextPosition(SwingConstants.CENTER);
        realCardOperations.setHorizontalTextPosition(SwingConstants.LEFT);
        realCardOperations.setBorder(BorderFactory.createEmptyBorder(20,10,20,10));
        realCardOperations.setBorder(new RoundedBorder(12));
        panel.add(realCardOperations);
        realCardOperations.addActionListener(e -> {
            frame.dispose();
            showRealVisaLoginOptions();
        });
        panel.add(Box.createRigidArea(new Dimension(0, 18)));

        frame.setContentPane(panel);
        frame.getContentPane().setBackground(Elements.BEIGE);
        frame.setVisible(true);
    }

    private static JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(180, 50));
        button.setMaximumSize(new Dimension(310, 60));
        button.setFont(Elements.FONT_BOLD_16);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        Color originalBg = button.getBackground();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Elements.SOFT_GREEN);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBg);
            }
        });

        return button;
    }

    public static void showTempVisaOptions() {
        JDialog dialog = new JDialog((Frame) null, "Temporary Visa Card Options", true);
        dialog.setSize(400, 240);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(Elements.BEIGE);

        JLabel label = new JLabel("Please select an option:", SwingConstants.CENTER);
        label.setFont(Elements.FONT_BOLD_16);
        label.setForeground(Elements.SEAWEED);
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        dialog.add(label, BorderLayout.NORTH);

        JButton createBtn = createStyledButton("Create New Temporary Visa Card",Elements.SEAWEED,Elements.CHAMPAGNE);
        createBtn.setBorder(new RoundedBorder(20));
        JButton loginBtn  = new JButton("WithDraw From Temp Card");
        loginBtn.setBorder(new RoundedBorder(20));
        JButton backBtn   = new JButton("Back to Main Menu",Elements.resizedBackIcon);
        backBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
        backBtn.setVerticalTextPosition(SwingConstants.CENTER);

        styleDialogButton(createBtn);
        styleDialogButton(loginBtn);
        styleDialogButton(backBtn,Elements.SOFT_GREEN, Color.WHITE);

        createBtn.addActionListener(e -> {
            dialog.dispose();
            new TempVisaCardCreator();
        });
        loginBtn.addActionListener(e -> {
            dialog.dispose();
            TempVisaCard temp = TempVisaLogin.showLogin();
            try {
                if (temp != null) {
                    dialog.dispose();
                    if(temp.balance>0) {
                        TempVisaWithDraw.show(temp);
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"your visa card isn't active, your balance = 0");
                    }
                }
            }catch (Exception exception){
                System.out.println("error reading card information!");
            }
        });
        backBtn.addActionListener(e -> {
            dialog.dispose();
            screen();
        });

        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        btnPanel.setBackground(Elements.BEIGE);
        btnPanel.add(createBtn); btnPanel.add(loginBtn); btnPanel.add(backBtn);
        dialog.add(btnPanel, BorderLayout.CENTER);

        dialog.setVisible(true);
    }

    public static void showRealVisaLoginOptions() {
        JDialog dialog = new JDialog((Frame) null, "Real Visa Card Login", true);
        dialog.setSize(400, 240);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setBackground(Elements.BEIGE);

        JLabel label = new JLabel("Choose your login method:", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(Elements.SEAWEED);
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        dialog.add(label, BorderLayout.NORTH);

        JButton visaBtn = new JButton("Visa Card Input");
        visaBtn.setBorder(new RoundedBorder(20));
        visaBtn.setPreferredSize(new Dimension(40,45));
        JButton stdBtn  = new JButton("Standard Sign In");
        stdBtn.setBorder(new RoundedBorder(20));
        stdBtn.setPreferredSize(new Dimension(40,45));
        JButton backBtn = new JButton("Back to Main Menu",Elements.resizedBackIcon);
        backBtn.setVerticalTextPosition(SwingConstants.CENTER);
        backBtn.setHorizontalTextPosition(SwingConstants.RIGHT);

        styleDialogButton(visaBtn);
        styleDialogButton(stdBtn);
        styleDialogButton(backBtn,Elements.SOFT_GREEN, Color.WHITE);

        visaBtn.addActionListener(e -> {
            dialog.dispose();
            Cards auth = VisaCardInput.showVisaInput();
            if (auth != null) {
                if ("true".equals(auth.unLocked)) {
                    new FeaturesMenu(auth);
                } else {
                    showWarning(null, "Your account is locked, contact the bank.");
                }
            }
        });
        stdBtn.addActionListener(e -> {
            dialog.dispose();
            Cards auth = StandardLoginGUI.showStandardLogin();
            if (auth != null) {
                if ("true".equals(auth.unLocked)) {
                    new FeaturesMenu(auth);
                } else {
                    showWarning(null, "Your account is locked, contact the bank.");
                }
            }
        });
        backBtn.addActionListener(e -> {
            dialog.dispose();
            screen();
        });

        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        btnPanel.setBackground(Elements.BEIGE);
        btnPanel.add(visaBtn); btnPanel.add(stdBtn); btnPanel.add(backBtn);
        dialog.add(btnPanel, BorderLayout.CENTER);

        dialog.setVisible(true);
    }

    private static void styleDialogButton(JButton button) {
        styleDialogButton(button,Elements.SEAWEED,Elements.BEIGE);
    }
    private static void styleDialogButton(JButton button, Color bg, Color fg) {
        button.setFont(Elements.FONT_BOLD_16);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, "⚠️ " + message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    public static void screen() {
        SwingUtilities.invokeLater(MainMenu::new);
    }
}