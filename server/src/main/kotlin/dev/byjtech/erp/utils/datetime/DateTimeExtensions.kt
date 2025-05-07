package dev.byjtech.erp.utils.datetime

import kotlinx.datetime.LocalDateTime as KxLocalDateTime
import java.time.LocalDateTime as JvmLocalDateTime

fun JvmLocalDateTime.toKotlinx(): KxLocalDateTime =
    KxLocalDateTime(year, monthValue, dayOfMonth, hour, minute, second, nano)

fun KxLocalDateTime.toJava(): JvmLocalDateTime =
    JvmLocalDateTime.of(year, monthNumber, dayOfMonth, hour, minute, second, nanosecond)