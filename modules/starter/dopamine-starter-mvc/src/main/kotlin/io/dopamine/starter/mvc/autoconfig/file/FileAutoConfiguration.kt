package io.dopamine.starter.mvc.autoconfig.file

import io.dopamine.file.common.config.FileProperties
import io.dopamine.file.common.config.FilePropertyKeys
import io.dopamine.file.common.factory.FileStorageFactory
import io.dopamine.file.common.factory.LocalFileStorageFactory
import io.dopamine.file.common.resolve.DefaultFileStorageResolver
import io.dopamine.file.common.resolve.FileStorageResolver
import io.dopamine.file.common.storage.FileStorage
import io.dopamine.file.mvc.controller.FileController
import io.dopamine.file.mvc.mapper.MultipartFileUploadRequestMapper
import io.dopamine.file.mvc.service.DefaultFileUploadService
import io.dopamine.file.mvc.service.FileUploadService
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.support.GenericApplicationContext
import java.util.function.Supplier

@AutoConfiguration
@Import(FileController::class)
@ConditionalOnProperty(
    name = [FilePropertyKeys.ENABLED],
    havingValue = "true",
    matchIfMissing = true,
)
@EnableConfigurationProperties(FileProperties::class)
class FileAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun multipartFileUploadRequestMapper() = MultipartFileUploadRequestMapper()

    @Bean
    @ConditionalOnMissingBean
    fun fileUploadService(
        storageResolver: FileStorageResolver,
        mapper: MultipartFileUploadRequestMapper,
    ): FileUploadService =
        DefaultFileUploadService(
            storageResolver = storageResolver,
            mapper = mapper,
        )

    @Bean
    @ConditionalOnMissingBean
    fun storageResolver(storageMap: Map<String, FileStorage>): FileStorageResolver =
        DefaultFileStorageResolver(storageMap)

    @Bean
    fun fileStorageMap(
        properties: FileProperties,
        factories: List<FileStorageFactory>,
        context: GenericApplicationContext,
    ): Map<String, FileStorage> {
        val result = mutableMapOf<String, FileStorage>()

        for (config in properties.storages) {
            val name = config.name

            if (context.containsBean(name)) continue

            val factory =
                factories.find { it.supports(config.type) }
                    ?: throw IllegalArgumentException("No FileStorageFactory found for type ${config.type}")

            val storage = factory.create(config)
            context.registerBean(name, FileStorage::class.java, Supplier { storage })
            result[name] = storage
        }

        return result
    }

    @Bean
    fun localFileStorageFactory() = LocalFileStorageFactory()
}
