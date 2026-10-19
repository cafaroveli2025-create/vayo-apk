
package com.ali.cyber;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    LinearLayout layout;
    EditText input;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(28, 35, 28, 35);
        layout.setBackgroundColor(Color.rgb(10, 18, 35));
        scroll.addView(layout);
        setContentView(scroll);

        TextView title = new TextView(this);
        title.setText("ALI CYBER");
        title.setTextSize(30);
        title.setTextColor(Color.rgb(0, 230, 200));
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("IP və domen məlumatlarının yoxlanması");
        subtitle.setTextColor(Color.WHITE);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 12, 0, 25);
        layout.addView(subtitle);

        input = new EditText(this);
        input.setSingleLine(true);
        input.setHint("IP və ya domen yaz");
        input.setHintTextColor(Color.GRAY);
        input.setTextColor(Color.WHITE);
        layout.addView(input);

        Button ipButton = new Button(this);
        ipButton.setText("IP MƏLUMATLARINI YO XLA");
        layout.addView(ipButton);

        Button domainButton = new Button(this);
        domainButton.setText("DOMEN MƏLUMATLARINI YO XLA");
        layout.addView(domainButton);

        result = new TextView(this);
        result.setTextColor(Color.WHITE);
        result.setTextSize(16);
        result.setPadding(0, 25, 0, 10);
        layout.addView(result);

        ipButton.setOnClickListener(v -> {
            String value = input.getText().toString().trim();
            if (value.isEmpty()) {
                input.setError("IP ünvanı yaz");
                return;
            }
            request("https://ipwho.is/" + value, false);
        });

        domainButton.setOnClickListener(v -> {
            String value = input.getText().toString().trim();
            if (value.isEmpty()) {
                input.setError("Domen adı yaz");
                return;
            }
            request("https://rdap.org/domain/" + value, true);
        });
    }

    void request(String address, boolean domain) {
        result.setText("Yoxlanılır...");
        new Thread(() -> {
            String output;
            try {
                HttpURLConnection connection =
                    (HttpURLConnection) new URL(address).openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
                StringBuilder data = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
                reader.close();
                connection.disconnect();

                JSONObject json = new JSONObject(data.toString());
                StringBuilder text = new StringBuilder();

                if (!domain) {
                    if (!json.optBoolean("success", false)) {
                        text.append("IP yoxlanmadı: ")
                            .append(json.optString("message", "Məlumat tapılmadı"));
                    } else {
                        text.append("IP: ").append(json.optString("ip", "-"))
                            .append("\nÖlkə: ").append(json.optString("country", "-"))
                            .append("\nŞəhər: ").append(json.optString("city", "-"))
                            .append("\nProvayder: ").append(json.optString("connection", "-"));
                    }
                } else {
                    text.append("Domen: ").append(json.optString("ldhName", "-"))
                        .append("\nQeydiyyat məlumatları:\n");
                    text.append(json.optJSONArray("events") == null
                        ? "Əlavə məlumat yoxdur"
                        : json.optJSONArray("events").toString(2));
                }
                output = text.toString();

            } catch (Exception e) {
                output = "Sorğu alınmadı.\nİnterneti yoxla və məlumatı yenidən sına.";
            }

            String finalOutput = output;
            runOnUiThread(() -> result.setText(finalOutput));
        }).start();
    }
}
