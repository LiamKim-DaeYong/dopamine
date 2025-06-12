package io.dopamine.file.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Configuration properties for the Dopamine file module.
 * Bound to the "dopamine.file" prefix in application settings.
 */
@ConfigurationProperties(prefix = FilePropertyKeys.PREFIX)
data class FileProperties(
    /**
     * Whether to enable the Dopamine file module.
     * If false, no file-related beans will be registered.
     */
    val enabled: Boolean = true,
    /**
     * A list of file storage configurations.
     * Each storage must have a unique [name] and define its [type] and corresponding settings.
     */
    val storages: List<StorageConfig> = listOf(StorageConfig()),
) {
    /**
     * Represents a single file storage configuration entry.
     */
    data class StorageConfig(
        /**
         * Unique name used to identify and refer to this storage.
         */
        val name: String = "default",
        /**
         * Type of storage to use.
         * Determines which configuration block is required.
         */
        val type: StorageType = StorageType.LOCAL,
        /**
         * Maximum allowed upload size in bytes.
         * If null, there is no size limit.
         * Default: 5MB (5 * 1024 * 1024)
         */
        val maxSize: Long? = 5 * 1024 * 1024,
        /**
         * Extension filtering policy.
         * Defines whether to allow or deny certain file extensions.
         * If null, all extensions are accepted.
         */
        val extensionPolicy: ExtensionPolicy? = null,
        /**
         * Configuration for local file system storage.
         * Required when [type] is LOCAL.
         */
        val local: LocalStorageProperties? = LocalStorageProperties(),
        /**
         * Configuration for AWS S3 storage.
         * Required when [type] is S3.
         */
        val s3: S3StorageProperties? = null,
    )

    /**
     * Extension filtering policy definition.
     * Determines which extensions are either allowed or denied.
     */
    data class ExtensionPolicy(
        /**
         * Extension policy mode.
         * ALLOW: only specified extensions are accepted.
         * DENY: all extensions are accepted except the ones listed.
         */
        val mode: ExtensionMode,
        /**
         * A set of file extensions used in this policy.
         * Should be lowercase and exclude the dot (e.g., ["jpg", "png"]).
         */
        val patterns: Set<String>,
    )

    /**
     * Mode for extension filtering.
     */
    enum class ExtensionMode {
        /** Only explicitly listed extensions will be accepted. */
        ALLOW,

        /** All extensions are accepted except those listed. */
        DENY,
    }

    /**
     * Properties for local disk storage.
     */
    data class LocalStorageProperties(
        /**
         * Base directory where uploaded files will be stored.
         * This path can be absolute or relative to the project root.
         */
        val baseDir: String = "./uploads",
    )

    /**
     * Properties for AWS S3 storage.
     */
    data class S3StorageProperties(
        /**
         * Name of the target S3 bucket.
         */
        val bucket: String,
        /**
         * Optional path prefix inside the bucket.
         * All uploaded files will be stored under this prefix if set.
         */
        val prefix: String? = null,
    )
}
