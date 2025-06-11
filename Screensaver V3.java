import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.time.Period;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.FormatStyle;



class ScreenSaver extends JFrame {

        private Font minecraftFont;

        private void drawCalendar(Graphics g) {
        LocalDate today = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(today.getYear(), today.getMonth());
        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = yearMonth.atDay(1);
        int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1=Monday, 7=Sunday

        int cellWidth = 50;
        int cellHeight = 40;
        int calendarWidth = 7 * cellWidth;
        int calendarHeight = 7 * cellHeight;
        int startX = getWidth() - calendarWidth - 40;
        int startY = 40;

        Font calendarFont = minecraftFont.deriveFont(Font.PLAIN, 18);
        g.setFont(calendarFont);
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();

        // Draw month title
        String monthTitle = today.getMonth().toString() + " " + today.getYear();
        int titleX = startX + (calendarWidth - fm.stringWidth(monthTitle)) / 2;
        g.drawString(monthTitle, titleX, startY);

        // Draw weekdays
        String[] weekdays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < weekdays.length; i++) {
            int x = startX + i * cellWidth + (cellWidth - fm.stringWidth(weekdays[i])) / 2;
            g.drawString(weekdays[i], x, startY + cellHeight);
        }

        // Draw days
        int day = 1;
        int row = 2;
        for (int i = startDayOfWeek - 1; i < 7 * 6 && day <= daysInMonth; i++) {
            int col = i % 7;
            int x = startX + col * cellWidth;
            int y = startY + row * cellHeight;

            String dayStr = String.valueOf(day);
            int strX = x + (cellWidth - fm.stringWidth(dayStr)) / 2;
            int strY = y + fm.getAscent();

            // Highlight today's date
            if (day == today.getDayOfMonth()) {
                g.setColor(Color.RED);
                g.drawRect(x, y - fm.getAscent(), cellWidth, cellHeight);
            }

            g.setColor(Color.WHITE);
            g.drawString(dayStr, strX, strY);
            day++;

            if (col == 6) row++;
        }
    }

    private String[] timePassedLines = {
        "", "", ""
    };

    public ScreenSaver(LocalDateTime birthDateTime) {
    setTitle("Screen Saver");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setUndecorated(true);
    setExtendedState(JFrame.MAXIMIZED_BOTH);

    // Load the font from a file
    try {
        minecraftFont = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("MinecraftSevenv2-Regular.ttf")).deriveFont(24f);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(minecraftFont);
    } catch (Exception e) {
        System.out.println("Could not load custom font. Falling back to default.");
        minecraftFont = new Font("SansSerif", Font.PLAIN, 24);
    }

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
    
        g.setFont(minecraftFont.deriveFont(Font.BOLD, 48));
        g.setColor(Color.white);
        FontMetrics fm = g.getFontMetrics();
        int y = getHeight() / 2 - 60;
        int x = (getWidth() - fm.stringWidth(timePassedLines[0])) / 2;
        g.drawString(timePassedLines[0], x, y);
    
        g.setFont(minecraftFont.deriveFont(Font.BOLD, 96)); //33
        g.setColor(Color.white);
        fm = g.getFontMetrics();
        for (int i = 1; i < timePassedLines.length; i++) {
            int lineX = (getWidth() - fm.stringWidth(timePassedLines[i])) / 2;
            int lineY = y + i * fm.getHeight() + 10;
            g.drawString(timePassedLines[i], lineX, lineY);
        }

         // --- Live Clock (bottom-right) ---
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        Font clockFont = minecraftFont.deriveFont(Font.PLAIN, 36);
        g.setFont(clockFont);
        fm = g.getFontMetrics(clockFont);
        int clockX = getWidth() - fm.stringWidth(currentTime) - 30;
        int clockY = getHeight() - fm.getDescent() - 20;
        g.drawString(currentTime, clockX, clockY);

        // --- Calendar (top-right) ---
        drawCalendar(g);
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