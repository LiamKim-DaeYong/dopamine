package io.dopamine.file.common.factory

import io.dopamine.file.common.config.FileProperties
import io.dopamine.file.common.config.StorageType
import io.dopamine.file.common.storage.FileStorage
import io.dopamine.file.common.storage.LocalFileStorage
import java.nio.file.Paths

class LocalFileStorageFactory : FileStorageFactory {
    override fun supports(type: StorageType): Boolean = type == StorageType.LOCAL

    override fun create(config: FileProperties.StorageConfig): FileStorage {
        val baseDir = config.local?.baseDir ?: "./uploads"
        return LocalFileStorage(
            baseDir = Paths.get(baseDir),
            generateUniqueName = true,
            useDateFolder = true,
        )
    }
}
