package dev.nokee.docs.samples

import dev.gradleplugins.test.fixtures.file.TestFile
import dev.gradleplugins.test.fixtures.gradle.GradleExecuterFactory
import dev.gradleplugins.test.fixtures.gradle.executer.GradleExecuter
import dev.gradleplugins.test.fixtures.gradle.executer.LogContent
import dev.gradleplugins.test.fixtures.gradle.executer.OutputScrapingExecutionResult
import dev.gradleplugins.test.fixtures.logging.ConsoleOutput

import static dev.gradleplugins.test.fixtures.gradle.GradleScriptDsl.GROOVY_DSL
import static dev.gradleplugins.test.fixtures.gradle.GradleScriptDsl.KOTLIN_DSL

class JvmApplicationWithJniLibrarySampleTest extends WellBehavingSampleTest {
	final String sampleName = 'jvm-application-with-jni-library'

	def "can execute commands successfully"(dsl) {
		def fixture = new SampleContentFixture(sampleName)
		unzipTo(fixture.getDslSample(dsl), temporaryFolder.testDirectory)

		GradleExecuter executer = configureLocalPluginResolution(new GradleExecuterFactory().wrapper(TestFile.of(temporaryFolder.testDirectory)).withConsole(ConsoleOutput.Rich))
		expect:
		fixture.getCommands().size() == 1
		def command = fixture.getCommands()[0]
		command.executable == './gradlew'
		def result = executer.withArguments(command.args).run()
		def expectedResult = OutputScrapingExecutionResult.from(command.expectedOutput.get(), '')

		OutputScrapingExecutionResult.normalize(LogContent.of(result.getPlainTextOutput())).replace(' in 0s', '').startsWith(expectedResult.getOutput())

		where:
		dsl << [GROOVY_DSL, KOTLIN_DSL]
	}
}
