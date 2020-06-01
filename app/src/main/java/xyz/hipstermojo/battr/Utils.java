package xyz.hipstermojo.battr;

public class Utils {
    public static String formatDuration(int durationMinutes) {
        String format;
        if (durationMinutes / 60 == 0) {
            format = String.format("%dmin", durationMinutes % 60);
        } else {
            format = String.format("%dh %dmin", durationMinutes / 60, durationMinutes % 60);
        }
        return format;
    }
    public static String formatAmount(double amount) {
            String format = String.format("%.2f",amount);
            if (format.endsWith("00")){
                return String.format("%.0f",amount);
            }
            return format;
    }
}
