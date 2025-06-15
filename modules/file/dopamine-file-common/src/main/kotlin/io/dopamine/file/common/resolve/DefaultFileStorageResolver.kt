package io.dopamine.file.common.resolve

import io.dopamine.file.common.storage.FileStorage

/**
 * Default implementation of [FileStorageResolver] using a map of named [FileStorage] beans.
 */
class DefaultFileStorageResolver(
    private val storages: Map<String, FileStorage>,
) : FileStorageResolver {
    override fun resolve(name: String): FileStorage =
        storages[name]
            ?: throw IllegalArgumentException("No FileStorage found for name: $name")
}
