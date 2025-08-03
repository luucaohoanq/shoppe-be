package com.lcaohoanq.sp.domains.user

import com.lcaohoanq.sp.bases.BaseEntity
import com.lcaohoanq.sp.domains.loginhistory.LoginHistory
import com.lcaohoanq.sp.domains.settings.UserSettings
import com.lcaohoanq.sp.entities.NotificationEntity
import com.lcaohoanq.sp.entities.UserDeviceToken
import com.lcaohoanq.sp.enums.UserEnum
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    val id: Long? = null,

    @Column(name = "email", unique = true)
    val email: String,

    @Column(name = "username")
    val userName: String = "",

    @Column(name = "password")
    var hashedPassword: String,

    @Enumerated(EnumType.ORDINAL)
    val role: UserEnum.Role? = UserEnum.Role.CUSTOMER,
    val phone: String = "",
    val name: String = "New User",

    var totpSecret: String = "", // null if 2FA not enabled yet

    @Enumerated(EnumType.ORDINAL)
    val gender: UserEnum.Gender? = UserEnum.Gender.FEMALE,

    @Enumerated(EnumType.ORDINAL)
    var status: UserEnum.Status? = UserEnum.Status.UNVERIFIED,

    val dateOfBirth: String? = "",

    val avatar: String = "",

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var address: MutableList<Address> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var notificationEntities: MutableList<NotificationEntity> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var deviceTokens: MutableSet<UserDeviceToken> = mutableSetOf(),

    val cartId: String,

    val walletId: String,

    @Embedded
    var preference: UserPreference = UserPreference(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val loginHistory: MutableList<LoginHistory> = mutableListOf(),

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_settings_id", referencedColumnName = "id")
    var userSettings: UserSettings? = null,

    ) : BaseEntity(), UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_${role?.name}"))
    }

    override fun getPassword(): String = hashedPassword

    override fun getUsername(): String = email

    // Implement other UserDetails methods
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}
