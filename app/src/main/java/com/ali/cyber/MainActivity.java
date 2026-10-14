package com.ali.cyber;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
        subtitle.setText("OSINT • IP • DOMEN • AÇIQ MƏNBƏLƏR");
        subtitle.setTextColor(Color.WHITE);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 12, 0, 25);
        layout.addView(subtitle);

        input = new EditText(this);
        input.setSingleLine(true);
        input.setHint("IP, domen və ya axtarış sözü");
        input.setHintTextColor(Color.GRAY);
        input.setTextColor(Color.WHITE);
        layout.addView(input);

        addButton("IP MƏLUMATLARINI YOXL A", v -> {
            String value = input.getText().toString().trim();
            if (value.isEmpty()) {
                input.setError("IP ünvanı yaz");
                return;
            }
            request("https://ipwho.is/" + Uri.encode(value), false);
        });

        addButton("DOMEN MƏLUMATLARINI YOXL A", v -> {
            String value = input.getText().toString().trim();
            if (value.isEmpty()) {
                input.setError("Domen adı yaz");
                return;
            }
            request("https://rdap.org/domain/" + Uri.encode(value), true);
        });

        addButton("AÇIQ İNTERNETDƏ AXTAR", v -> {
            String query = input.getText().toString().trim();
            if (query.isEmpty()) {
                input.setError("Axtarış sözü yaz");
                return;
            }
            try {
                String encoded = URLEncoder.encode(
                    query, StandardCharsets.UTF_8.name()
                );
                Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/search?q=" + encoded)
                );
                startActivity(intent);
            } catch (Exception e) {
                result.setText("Axtarış açıla bilmədi.");
            }
        });

        result = new TextView(this);
        result.setTextColor(Color.WHITE);
        result.setTextSize(16);
        result.setPadding(0, 25, 0, 10);
        layout.addView(result);
    }

    void addButton(String label,
                   android.view.View.OnClickListener listener) {
        Button button = new Button(this);
        button.setText(label);
        layout.addView(button);
        button.setOnClickListener(listener);
    }

    void request(String address, boolean domain) {
        result.setText("Məlumat yoxlanılır...");

        new Thread(() -> {
            String output;
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection)
                    new URL(address).openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestMethod("GET");

                int status = connection.getResponseCode();
                if (status < 200 || status >= 300) {
                    throw new Exception("HTTP " + status);
                }

                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                        connection.getInputStream(),
                        StandardCharsets.UTF_8
                    )
                );

                StringBuilder data = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
                reader.close();

                JSONObject json = new JSONObject(data.toString());
                StringBuilder text = new StringBuilder();

                if (!domain) {
                    if (!json.optBoolean("success", false)) {
                        text.append("IP yoxlanmadı: ")
                            .append(json.optString(
                                "message", "Məlumat tapılmadı"
                            ));
                    } else {
                        text.append("IP: ")
                            .append(json.optString("ip", "-"))
                            .append("\nÖlkə: ")
                            .append(json.optString("country", "-"))
                            .append("\nŞəhər: ")
                            .append(json.optString("city", "-"))
                            .append("\nProvayder: ")
                            .append(json.optJSONObject("connection") == null
                                ? "-"
                                : json.optJSONObject("connection")
                                    .optString("isp", "-"));
                    }
                } else {
                    text.append("Domen: ")
                        .append(json.optString("ldhName", "-"))
                        .append("\nQeydiyyat hadisələri:\n");

                    if (json.optJSONArray("events") != null) {
                        text.append(
                            json.optJSONArray("events").toString(2)
                        );
                    } else {
                        text.append("Əlavə məlumat yoxdur.");
                    }
                }

                output = text.toString();

            } catch (Exception e) {
                output = "Sorğu alınmadı.\n"
                    + "İnterneti və daxil etdiyin məlumatı yoxla.";
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            String finalOutput = output;
            runOnUiThread(() -> result.setText(finalOutput));
        }).start();
    }
}
