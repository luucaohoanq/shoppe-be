package com.lcaohoanq.sp.entities

import jakarta.persistence.*
import java.security.Timestamp

@Entity
@Table(name = "chat_messages")
class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null

    var senderId: Int = 0
    var receiverId: Int = 0
    var message: String? = null
    var sentAt: Timestamp? = null
    var isRead: Boolean = false

}