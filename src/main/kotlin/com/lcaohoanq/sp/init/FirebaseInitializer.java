package com.lcaohoanq.sp.init;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class FirebaseInitializer {

    private static final String CONFIG = """
        {
        }
        """;

    @PostConstruct
    public void init() {
        try {
            InputStream serviceAccount = new ByteArrayInputStream(CONFIG.getBytes());

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
