package com.lcaohoanq.sp.entities

import jakarta.persistence.*
import java.security.Timestamp

@Entity
@Table(name = "chat_messages")
class ChatMessage {

    @Id
    @SequenceGenerator(name = "chat_messages_seq", sequenceName = "chat_messages_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_messages_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

    var senderId: Int = 0
    var receiverId: Int = 0
    var message: String? = null
    var sentAt: Timestamp? = null
    var isRead: Boolean = false

}