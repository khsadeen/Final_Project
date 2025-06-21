package GUIjavaSwing;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoundedBorder extends AbstractBorder {
    private int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Elements.BEIGE);
        g2.setStroke(new BasicStroke(8));
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius + 1, radius + 1, radius + 1, radius + 1);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(radius + 1, radius + 1, radius + 1, radius + 1);
        return insets;
    }
}

