package io.dopamine.file.common.model

/**
 * Value class representing file size in bytes.
 * Provides helper methods for size comparison and unit conversion.
 */
@JvmInline
value class FileSize(
    val bytes: Long,
) {
    fun isOver(limit: Long): Boolean = bytes > limit

    fun isOver(limit: FileSize): Boolean = bytes > limit.bytes

    override fun toString(): String = "${bytes}B"

    companion object {
        const val KB = 1024L
        const val MB = KB * 1024
        const val GB = MB * 1024

        fun ofKb(kb: Long) = FileSize(kb * KB)

        fun ofMb(mb: Long) = FileSize(mb * MB)
    }
}
