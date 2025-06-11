import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.time.Period;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;

class ScreenSaver extends JFrame {

    private String[] timePassedLines = {
        "", "", ""
    };

    public ScreenSaver(LocalDateTime birthDateTime) {
        setTitle("Screen Saver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        Timer timer = new Timer(1000, e -> {
            updateTimePassed(birthDateTime);
            repaint();
        });
        timer.start();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                System.exit(0);
            }
        });

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                System.exit(0);
            }
        });

        setFocusable(true);
        setVisible(true);
    }

    private void updateTimePassed(LocalDateTime birthDateTime) {
        LocalDateTime now = LocalDateTime.now();
        Period period = Period.between(birthDateTime.toLocalDate(), now.toLocalDate());
        Duration duration = Duration.between(birthDateTime, now);
    
        long totalSeconds = duration.getSeconds();
        long hours = (totalSeconds % (24 * 3600)) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
    
        timePassedLines[0] = String.format("Time passed since your birth:");
        timePassedLines[1] = String.format("%d Years  %d Months  %d Days", period.getYears(), period.getMonths(), period.getDays());
        timePassedLines[2] = String.format("%dh  %dm  %ds", hours, minutes, seconds);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
    
        g.setFont(new Font("MinecraftSevenv2-Regular", Font.BOLD, 48));
        g.setColor(Color.white);
        FontMetrics fm = g.getFontMetrics();
        int y = getHeight() / 2 - 60;
        int x = (getWidth() - fm.stringWidth(timePassedLines[0])) / 2;
        g.drawString(timePassedLines[0], x, y);
    
        g.setFont(new Font("MinecraftSevenv2-Regular", Font.BOLD, 96)); //33
        g.setColor(Color.white);
        fm = g.getFontMetrics();
        for (int i = 1; i < timePassedLines.length; i++) {
            int lineX = (getWidth() - fm.stringWidth(timePassedLines[i])) / 2;
            int lineY = y + i * fm.getHeight() + 10;
            g.drawString(timePassedLines[i], lineX, lineY);
        }
    }

    public static void main(String[] args) {
        //Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter your birth date in the format YYYY-MM-DD: ");
        //String birthDate = scanner.nextLine();
        String birthDate = "2001-08-31";

        System.out.println("Please enter the hours and minutes you were born in the format HH:mm: ");
        //String birthTime = scanner.nextLine();
        String birthTime = "06:31";

        try {
            String birthDateTimeString = birthDate + "T" + birthTime;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime birthDateTime = LocalDateTime.parse(birthDateTimeString, formatter);

            SwingUtilities.invokeLater(() -> new ScreenSaver(birthDateTime));
        } catch (Exception e) {
            System.out.println("Invalid input. Please ensure the date is in YYYY-MM-DD and time is in HH:mm format.");
        }
    }
}