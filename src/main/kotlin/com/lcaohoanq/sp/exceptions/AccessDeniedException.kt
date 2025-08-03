package com.lcaohoanq.sp.exceptions

import java.lang.RuntimeException

class AccessDeniedException : RuntimeException(
    "Access denied. You do not have permission to perform this action."
) {
}