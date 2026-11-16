package com.example.data

/** Satu-satunya sumber peta kode node lama -> baru. Tambahkan entri di sini saat kode diganti nama. */
object LegacyNodeCodes {
    val MAP: Map<String, String> = mapOf(
        "M03A.T1.1" to "M03.T1.1",
        "M03A.T1.3" to "M03.T1.2",
        "M03B.T1.1" to "M03.T2.1",
        "M03B.T2.1" to "M03.T2.2",
        "M03C.T1.1" to "M03.T3.1",
        "M03C.T1.4" to "M03.T3.2"
    )

    fun normalize(code: String): String = MAP[code] ?: code

    fun normalizeAll(codes: Set<String>): Set<String> =
        codes.mapTo(LinkedHashSet()) { normalize(it) }

    fun moduleIdOf(code: String): String =
        if (code.contains(".")) code.substringBefore(".") else ""
}
