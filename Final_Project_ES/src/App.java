import GUIjavaSwing.Elements;
import GUIjavaSwing.RoundedBorder;
import Main.MainMenu;

import javafx.application.Platform; //thread
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import javax.swing.*;
import java.awt.*;

public class App {

    public App() {
        JFrame frame = new JFrame("Welcome to Flexi ATM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(480, 520);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Elements.BEIGE);

        JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            try {
                Image fxImage = new Image("file:src/assets/WelcomeLogo.gif");
                ImageView imageView = new ImageView(fxImage);
                imageView.setFitWidth(480);
                imageView.setFitHeight(450);

                StackPane root = new StackPane(imageView);
                Scene scene = new Scene(root);
                fxPanel.setScene(scene);
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JLabel errorLabel = new JLabel("⚠️ Failed to load intro video", SwingConstants.CENTER);
                    errorLabel.setForeground(Color.RED);
                    frame.add(errorLabel, BorderLayout.CENTER);
                    frame.revalidate();
                    frame.repaint();
                });
            }
        });

        // "Enter" button
        JButton enterBtn = new JButton("Enter");
        enterBtn.setBorder(new RoundedBorder(13));
        enterBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        enterBtn.setBackground(Elements.SEAWEED);
        enterBtn.setForeground(Elements.CHAMPAGNE);
        enterBtn.setFocusPainted(false);
        enterBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        enterBtn.setPreferredSize(new Dimension(140, 50));

        enterBtn.addActionListener(e -> {
            frame.dispose();
            MainMenu.screen();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Elements.BEIGE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        bottomPanel.add(enterBtn);

        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { //EDT , RUN IN SPECIFIC TIME
            // Initialize JavaFX once
            Platform.startup(() -> {
                new App();
            });
        });

    }
}
