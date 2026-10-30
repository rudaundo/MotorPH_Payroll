/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package others;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JLabel;
import javax.swing.Timer;

public class startClock {

    private final JLabel displayLabel;
    private final Timer clockTimer;

    private final DateTimeFormatter dateFormat =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DateTimeFormatter timeFormat =
            DateTimeFormatter.ofPattern("hh:mm:ss a");

    public startClock(JLabel displayLabel) {
        this.displayLabel = displayLabel;

        clockTimer = new Timer(1000, e -> updateClock());

        updateClock(); // Display immediately
        clockTimer.start(); // Update every second
    }

    private void updateClock() {
        LocalDateTime now = LocalDateTime.now();

        String date = now.format(dateFormat);
        String time = now.format(timeFormat);

        displayLabel.setText(
                "<html><center>"
                + date
                + "<br>"
                + time
                + "</center></html>"
        );
    }

    public void stopClock() {
        clockTimer.stop();
    }
}
