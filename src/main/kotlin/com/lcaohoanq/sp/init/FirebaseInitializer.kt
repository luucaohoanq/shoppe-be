import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FirebaseInitializer {

    private val log = LoggerFactory.getLogger(FirebaseInitializer::class.java)

    @PostConstruct
    fun init() {
        try {
            val serviceAccount = javaClass.classLoader.getResourceAsStream("firebase-service-account.json")
                ?: throw IllegalStateException("firebase-service-account.json not found in resources")

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
                log.info("Firebase has been initialized successfully.")
            } else {
                log.info("Firebase already initialized.")
            }

        } catch (e: Exception) {
            log.error("Failed to initialize Firebase", e)
        }
    }
}
