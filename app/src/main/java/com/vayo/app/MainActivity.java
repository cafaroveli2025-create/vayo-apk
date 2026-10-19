package com.vayo.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
    LinearLayout root, content, navigation;
    int green = Color.rgb(0, 143, 114);
    int dark = Color.rgb(30, 40, 42);
    int gray = Color.rgb(110, 120, 120);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(green);
        getWindow().setNavigationBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        );

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);
        setContentView(root);

        TextView header = new TextView(this);
        header.setText("VAYO");
        header.setTextSize(29);
        header.setTypeface(null, Typeface.BOLD);
        header.setTextColor(Color.WHITE);
        header.setBackgroundColor(green);
        header.setPadding(22, 22, 18, 4);
        root.addView(header);

        TextView subtitle = new TextView(this);
        subtitle.setText("Əlaqədə qal, birlikdə irəli get");
        subtitle.setTextSize(14);
        subtitle.setTextColor(Color.WHITE);
        subtitle.setBackgroundColor(green);
        subtitle.setPadding(24, 0, 18, 18);
        root.addView(subtitle);

        navigation = new LinearLayout(this);
        navigation.setBackgroundColor(Color.WHITE);
        navigation.setOrientation(LinearLayout.HORIZONTAL);
        root.addView(navigation);

        ScrollView scroll = new ScrollView(this);
        root.addView(scroll, new LinearLayout.LayoutParams(
            -1, 0, 1
        ));

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(18, 12, 18, 18);
        scroll.addView(content);

        addTab("Çatlar", "chat");
        addTab("Status", "status");
        addTab("Qruplar", "groups");
        addTab("Profil", "profile");

        showPage("chat");
    }

    void addTab(String label, String page) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(12);
        b.setAllCaps(false);
        b.setTextColor(green);
        b.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(Color.WHITE)
        );
        navigation.addView(b, new LinearLayout.LayoutParams(
            0, 55, 1
        ));
        b.setOnClickListener(v -> showPage(page));
    }

    void heading(String text) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(23);
        t.setTypeface(null, Typeface.BOLD);
        t.setTextColor(dark);
        t.setPadding(0, 12, 0, 18);
        content.addView(t);
    }

    void item(String symbol, String title, String detail) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(4, 13, 4, 13);

        TextView avatar = new TextView(this);
        avatar.setText(symbol);
        avatar.setTextSize(23);
        avatar.setGravity(Gravity.CENTER);
        avatar.setTextColor(Color.WHITE);
        avatar.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(green)
        );
        avatar.setBackground(getDrawable(android.R.drawable
            .btn_default));
        avatar.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(green)
        );
        row.addView(avatar, new LinearLayout.LayoutParams(52, 52));

        LinearLayout texts = new LinearLayout(this);
        texts.setOrientation(LinearLayout.VERTICAL);
        texts.setPadding(14, 0, 0, 0);

        TextView name = new TextView(this);
        name.setText(title);
        name.setTextSize(17);
        name.setTypeface(null, Typeface.BOLD);
        name.setTextColor(dark);
        texts.addView(name);

        TextView desc = new TextView(this);
        desc.setText(detail);
        desc.setTextSize(13);
        desc.setTextColor(gray);
        desc.setPadding(0, 5, 0, 0);
        texts.addView(desc);

        row.addView(texts, new LinearLayout.LayoutParams(
            0, -2, 1
        ));
        content.addView(row);

        View line = new View(this);
        line.setBackgroundColor(Color.rgb(235, 239, 238));
        content.addView(line, new LinearLayout.LayoutParams(
            -1, 1
        ));
    }

    void showPage(String page) {
        content.removeAllViews();

        if (page.equals("chat")) {
            heading("Mesajlar");
            item("＋", "Yeni söhbət", "İnsanlarla əlaqə qur");
            item("👥", "VAYO dostları", "Qrup söhbətləri burada");
            item("💬", "Şəxsi mesajlar", "Söhbətlər burada görünəcək");
            item("🔒", "Təhlükəsizlik", "Hesabını qoru");
        } else if (page.equals("status")) {
            heading("Statuslar");
            item("＋", "Status əlavə et", "Şəkil və fikirlərini paylaş");
            item("◉", "Dostların statusları", "Yeni paylaşımlar burada");
        } else if (page.equals("groups")) {
            heading("Qruplar");
            item("＋", "Yeni qrup", "Birlikdə söhbətə başla");
            item("👥", "Qruplarım", "Üzv olduğun qruplar burada");
        } else {
            heading("Mənim profilim");
            item("👤", "İstifadəçi", "Profil məlumatların");
            item("🏪", "Biznes mağazam", "Gələcəkdə mağazanı burada aç");
            item("⚙", "Parametrlər", "Hesab və tətbiq ayarları");
        }

        TextView footer = new TextView(this);
        footer.setText(
            "\nVAYO • İlk versiya\n\n" +
            "Bu ekran ilkin dizayndır. Real mesajlaşma, " +
            "qeydiyyat və onlayn bağlantı növbəti mərhələdə qoşulacaq."
        );
        footer.setTextColor(gray);
        footer.setTextSize(12);
        footer.setPadding(4, 20, 4, 20);
        content.addView(footer);
    }
}
