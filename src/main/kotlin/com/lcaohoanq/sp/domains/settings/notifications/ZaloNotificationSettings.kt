package com.lcaohoanq.sp.domains.settings.notifications

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class ZaloNotificationSettings(
    @Column(name = "zalo_master_enabled")
    var masterEnabled: Boolean = true,

    @Column(name = "zalo_promo")
    var promo: Boolean = true,
)
