import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SMSSender {

    private static final String ACCOUNT_SID = "###############";
    private static final String AUTH_TOKEN = "################";
    private static final String FROM = "#########";

    public static void sendReminder(String phone, String bookTitle, String dueDate) {

        String body = "Reminder: Please return the book '" + bookTitle + "' by " + dueDate + ".";

        try {

            String urlStr = "https://api.twilio.com/2010-04-01/Accounts/"
                    + ACCOUNT_SID + "/Messages.json";

            URI uri = new URI(urlStr);
            URL url = uri.toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String auth = ACCOUNT_SID + ":" + AUTH_TOKEN;
            String encodedAuth = Base64.getEncoder()
                    .encodeToString(auth.getBytes(StandardCharsets.UTF_8));

            conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String data =
                    "To=" + URLEncoder.encode(phone, "UTF-8") +
                    "&From=" + URLEncoder.encode(FROM, "UTF-8") +
                    "&Body=" + URLEncoder.encode(body, "UTF-8");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();

            if (responseCode == 201 || responseCode == 200) {
                System.out.println("✅ SMS sent successfully to " + phone);
            } else {
                System.out.println("⚠ SMS failed with response code: " + responseCode);
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to send SMS: " + e.getMessage());
        }
    }
}