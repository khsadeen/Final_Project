 package Management.SignInManager;

 import GUIjavaSwing.Elements;
 import GUIjavaSwing.RoundedBorder;
 import Main.MainMenu;
 import Management.CardsManager.Cards;
 import Management.CardsManager.CardsLoader;

 import javax.swing.*;
 import java.awt.*;
 import java.util.List;

 public class StandardLoginGUI {

     private static Cards authenticatedCard = null;

     public static Cards showStandardLogin() {
         CardsLoader loader = new CardsLoader();
         List<Cards> cards = loader.cardsListing(); //ArrayList

         JDialog dialog = new JDialog((Frame) null, "Standard Login", true);
         dialog.setSize(400, 570);
         dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

         JLabel logoLabel = new JLabel();
         ImageIcon icon = new ImageIcon("src/assets/logo.png");
         Image image = icon.getImage().getScaledInstance(200, 160, Image.SCALE_SMOOTH);
         logoLabel.setIcon(new ImageIcon(image));
         JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
         logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
         logoPanel.setBackground(Elements.BEIGE);
         logoPanel.add(logoLabel);

         JTextField cardField = new JTextField();
         JPasswordField passField = new JPasswordField();

         Dimension fieldSize = new Dimension(200, 35);
         cardField.setPreferredSize(fieldSize);
         passField.setPreferredSize(fieldSize);

         JLabel status = new JLabel("Enter credit details", SwingConstants.CENTER);
         status.setForeground(Elements.SEAWEED);
         status.setBackground(Elements.BEIGE);

         JButton login = createStyledButton("Login");
         login.setPreferredSize(new Dimension(200, 50));
         JButton backButton = createStyledButton("Back to main menu");
         backButton.setIcon(Elements.resizedBackIcon);
         backButton.setVerticalTextPosition(SwingConstants.CENTER);
         backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
         backButton.setBorder(new RoundedBorder(12));
         backButton.setBackground(Elements.SOFT_GREEN);
         JButton switchVisaInputButton = createStyledButton("Switch to Visa input");
         switchVisaInputButton.setBorder(new RoundedBorder(12));

         switchVisaInputButton.addActionListener(e -> {
             dialog.dispose();
             authenticatedCard = VisaCardInput.showVisaInput();
         });

         login.addActionListener(e -> {
             String cardNum = cardField.getText();
             String pass = new String(passField.getPassword());

             for (Cards c : cards) { //here we make sure that the inputs right
                 if (c.cardNumber.equals(cardNum) && c.password.equals(pass)) {
                     authenticatedCard = c;
                     JOptionPane.showMessageDialog(dialog, "✅ Welcome, " + c.ownerName);
                     dialog.dispose();
                 }
             }
             status.setText("X Invalid. Try again. X");
         });

         backButton.addActionListener(e -> {
             dialog.dispose();
             MainMenu.screen();
         });

         // Input panel
         JPanel inputPanel = new JPanel(new GridLayout(5, 1, 10, 8));
         inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 5, 30));
         inputPanel.setBackground(Elements.BEIGE);

         JLabel cardNum = new JLabel("Card Number:");
         cardNum.setFont(Elements.FONT_BOLD_16);
         cardNum.setForeground(Color.BLACK);
         inputPanel.add(cardNum);
         inputPanel.add(cardField);

         JLabel password = new JLabel("Password:");
         password.setFont(Elements.FONT_BOLD_16);
         password.setForeground(Color.BLACK);
         inputPanel.add(password);
         inputPanel.add(passField);
         inputPanel.setBackground(Elements.BEIGE);

         inputPanel.add(login);

         // Button arrangement panel
         JPanel buttonsContainer = new JPanel(new GridLayout(2, 1, 10, 10));
         buttonsContainer.setBackground(Elements.BEIGE);
         buttonsContainer.setBorder(BorderFactory.createEmptyBorder(0, 30, 5, 30));
         buttonsContainer.add(switchVisaInputButton);
         buttonsContainer.add(backButton);

         // Combine input + buttons
         JPanel centerPanel = new JPanel(new BorderLayout());
         centerPanel.setBackground(Elements.BEIGE);
         centerPanel.add(inputPanel, BorderLayout.CENTER);
         centerPanel.add(buttonsContainer, BorderLayout.SOUTH);

         // Status formatting
         status.setBackground(Elements.BEIGE);
         status.setForeground(Color.BLACK);
         cardField.setBackground(Color.WHITE);
         cardField.setForeground(Color.BLACK);
         passField.setBackground(Color.WHITE);
         passField.setForeground(Color.BLACK);
         // Dialog Layout
         dialog.setLayout(new BorderLayout());
         dialog.add(logoPanel, BorderLayout.NORTH);
         dialog.add(centerPanel, BorderLayout.CENTER);
         dialog.add(status, BorderLayout.SOUTH);

         dialog.getContentPane().setBackground(Elements.BEIGE);
         dialog.setLocationRelativeTo(null);
         dialog.setVisible(true);
         return authenticatedCard;
     }

     public static JButton createStyledButton(String text) {
         JButton button = new JButton(text);
         button.setFont(Elements.FONT_BOLD_16);
         button.setBackground(Elements.SEAWEED);
         button.setForeground(Elements.CHAMPAGNE);
         button.setFocusPainted(false);
         button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
         return button;
     }
 }
