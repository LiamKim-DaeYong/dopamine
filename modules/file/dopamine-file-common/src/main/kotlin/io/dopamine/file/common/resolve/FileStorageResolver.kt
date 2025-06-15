package io.dopamine.file.common.resolve

import io.dopamine.file.common.storage.FileStorage

/**
 * Resolves a [FileStorage] by its configured name.
 */
fun interface FileStorageResolver {
    /**
     * Returns the [FileStorage] with the given [name].
     *
     * @throws IllegalArgumentException if no storage is registered with that name
     */
    fun resolve(name: String): FileStorage
}
