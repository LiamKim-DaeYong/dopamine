package io.dopamine.file.common.factory

import io.dopamine.file.common.config.FileProperties
import io.dopamine.file.common.config.StorageType
import io.dopamine.file.common.storage.FileStorage

interface FileStorageFactory {
    fun supports(type: StorageType): Boolean

    fun create(config: FileProperties.StorageConfig): FileStorage
}
