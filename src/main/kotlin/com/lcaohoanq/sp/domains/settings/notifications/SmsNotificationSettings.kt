package com.lcaohoanq.sp.domains.settings.notifications

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class SmsNotificationSettings(
    @Column(name = "sms_master_enabled")
    var masterEnabled: Boolean = true,

    @Column(name = "sms_promo")
    var promo: Boolean = false,
)
