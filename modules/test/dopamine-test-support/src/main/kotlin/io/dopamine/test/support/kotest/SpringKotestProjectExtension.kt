package io.dopamine.test.support.kotest

import io.kotest.common.KotestInternal
import io.kotest.core.extensions.ProjectExtension
import io.kotest.core.project.ProjectContext
import io.kotest.extensions.spring.SpringExtension

/**
 * Registers [SpringExtension] for all Kotest tests.
 *
 * Relies on internal Kotest API ([io.kotest.core.config.ProjectConfiguration.registry]).
 * May break on future versions. Review on upgrade.
 */
object SpringKotestProjectExtension : ProjectExtension {
    @OptIn(KotestInternal::class)
    override suspend fun interceptProject(
        context: ProjectContext,
        callback: suspend (ProjectContext) -> Unit,
    ) {
        context.configuration.registry.add(SpringExtension)
        callback(context)
    }
}
