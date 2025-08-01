package com.lcaohoanq.sp.domains.notifications;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcaohoanq.sp.bases.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Notifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity extends BaseEntity {

    public enum NotificationType {
        INFO,
        WARNING,
        SUCCESS,
        ERROR
    }

    public enum NotificationIcon {
        MESSAGE,
        ALERT,
        CHECK,
        ERROR
    }

    @Getter
    @AllArgsConstructor
    public enum NotificationColor {
        BLUE("#2196F3"),
        YELLOW("#FFEB3B"),
        GREEN("#4CAF50"),
        RED("#F44336");

        private final String hex;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "isRead", columnDefinition = "boolean default false")
    @JsonProperty(value = "isRead")
    private boolean read;

    @Enumerated(EnumType.STRING)
    private NotificationIcon iconName;

    @Enumerated(EnumType.STRING)
    private NotificationColor iconColorHex;

    @Column(name = "email", nullable = false)
    private String email;
}

