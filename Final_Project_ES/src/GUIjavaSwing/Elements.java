package GUIjavaSwing;

import javax.swing.*;
import java.awt.*;

public class Elements {

        // back button icon
        public static ImageIcon backIcon = new ImageIcon("src/assets/back.png");
        public static Image scaledBackImg = backIcon.getImage().getScaledInstance(50, 25, Image.SCALE_SMOOTH);
        public static ImageIcon resizedBackIcon = new ImageIcon(scaledBackImg);

        //account info statement button icon
        public static ImageIcon accountInfoIcon = new ImageIcon("src/assets/AccountInfoIcon.png");
        public static Image accountInfoImg = accountInfoIcon.getImage().getScaledInstance(25, 30, Image.SCALE_SMOOTH);
        public static ImageIcon resizedAccountInfoIcon = new ImageIcon(accountInfoImg);

        //balance comparison statement icon
        public static ImageIcon balanceCompIcon = new ImageIcon("src/assets/balanceCompIcon.png");
        public static Image balanceCompImg = balanceCompIcon.getImage().getScaledInstance(30, 35, Image.SCALE_SMOOTH);
        public static ImageIcon resizedBalanceCompIcon = new ImageIcon(balanceCompImg);

        //operations between two dates statement icon
        public static ImageIcon operations_2datesIcon = new ImageIcon("src/assets/oper_2_DatesIcon.png");
        public static Image operations_2datesImg = operations_2datesIcon.getImage().getScaledInstance(34, 35, Image.SCALE_SMOOTH);
        public static ImageIcon resizedOperations_2datesIcon = new ImageIcon(operations_2datesImg);

        //last 5 operations statement icon
        public static ImageIcon last5OperationsIcon = new ImageIcon("src/assets/last5Oper.png");
        public static Image last5OperationsImg = last5OperationsIcon.getImage().getScaledInstance(25, 30, Image.SCALE_SMOOTH);
        public static ImageIcon resizedLast5OperationsIcon = new ImageIcon(last5OperationsImg);

        //transfer money info statement icon
        public static ImageIcon transInfoIcon = new ImageIcon("src/assets/transDetailsIcon.png");
        public static Image transInfoImg = transInfoIcon.getImage().getScaledInstance(25, 30, Image.SCALE_SMOOTH);
        public static ImageIcon resizedTransInfIcon = new ImageIcon(transInfoImg);

        //statements icon
        public static ImageIcon statementsIcon = new ImageIcon("src/assets/StatementsIcon.png");
        public static Image scaledStatementsImg = statementsIcon.getImage().getScaledInstance(34, 37, Image.SCALE_SMOOTH);
        public static ImageIcon resizedStatementsIcon = new ImageIcon(scaledStatementsImg);

        //withdraw icon
        public static ImageIcon WithDrawIcon = new ImageIcon("src/assets/WithDrawIcon.png");
        public static Image scaledWithdrawImg = WithDrawIcon.getImage().getScaledInstance(42, 29, Image.SCALE_SMOOTH);
        public static ImageIcon resizedWithdrawIcon = new ImageIcon(scaledWithdrawImg);

        //lock account icon
        public static ImageIcon LockIcon = new ImageIcon("src/assets/LockIcon.png");
        public static Image scaledLockImg = LockIcon.getImage().getScaledInstance(36, 30, Image.SCALE_SMOOTH);
        public static ImageIcon resizedLockIcon = new ImageIcon(scaledLockImg);


        public static Color BEIGE = new Color(0xFFF7E8DC, true);//background
        public static Color SEAWEED = new Color(0x1E4D44);
        public static Color CHAMPAGNE = new Color(0xEBD2B8);//font color
        public static Color SOFT_GREEN = new Color(0x4D7C72);
        public static Font uniFont = new Font("Ariel", Font.BOLD, 12);
        public static  Font FONT_BOLD_16 = new Font("Segoe UI", Font.BOLD, 16);
        public static Font FONT_PLAIN_14 = new Font("Segoe UI", Font.PLAIN, 14);
}
