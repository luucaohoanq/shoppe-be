import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.DateSerializer
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
import java.time.LocalDateTime

@MappedSuperclass
class BaseEntity(
    @Column(name = "created_at", nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    @JsonSerialize(using = DateSerializer::class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Ho_Chi_Minh")
    var createdAt: Timestamp? = null,

    @Column(name = "created_by", nullable = false)
    var createdBy: String? = null,

    @Column(name = "last_modified_by", nullable = false)
    var lastModifiedBy: String? = null,

    @Column(name = "last_modified_on", nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @UpdateTimestamp
    @JsonSerialize(using = DateSerializer::class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Ho_Chi_Minh")
    var lastModifiedOn: Timestamp? = null
) {
    @PrePersist
    fun onCreate() {
        val now = Timestamp.valueOf(LocalDateTime.now())
        if (createdAt == null) {
            createdAt = now
        }
        lastModifiedOn = now
    }

    @PreUpdate
    fun onUpdate() {
        lastModifiedOn = Timestamp.valueOf(LocalDateTime.now())
    }
}
