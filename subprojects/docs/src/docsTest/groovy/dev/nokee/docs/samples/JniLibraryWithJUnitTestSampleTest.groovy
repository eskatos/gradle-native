package dev.nokee.docs.samples

import dev.gradleplugins.spock.lang.CleanupTestDirectory
import dev.gradleplugins.spock.lang.TestNameTestDirectoryProvider
import dev.gradleplugins.test.fixtures.file.TestFile
import dev.gradleplugins.test.fixtures.gradle.GradleExecuterFactory
import dev.gradleplugins.test.fixtures.gradle.executer.GradleExecuter
import dev.gradleplugins.test.fixtures.gradle.executer.LogContent
import dev.gradleplugins.test.fixtures.gradle.executer.OutputScrapingExecutionResult
import dev.gradleplugins.test.fixtures.logging.ConsoleOutput
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.TimeUnit

import static dev.gradleplugins.test.fixtures.gradle.GradleScriptDsl.GROOVY_DSL
import static dev.gradleplugins.test.fixtures.gradle.GradleScriptDsl.KOTLIN_DSL

@CleanupTestDirectory
class JniLibraryWithJUnitTestSampleTest extends Specification {
	@Rule
	final TestNameTestDirectoryProvider temporaryFolder = new TestNameTestDirectoryProvider()

	String getSampleName() {
		return 'jni-library-with-junit-test'
	}

	@Unroll
	def "can run './gradlew #taskName' successfully"(taskName, dsl) {
		def fixture = new SampleContentFixture(sampleName)
		unzipTo(fixture.getDslSample(dsl), temporaryFolder.testDirectory)

		GradleExecuter executer = new GradleExecuterFactory().wrapper(TestFile.of(temporaryFolder.testDirectory))
		expect:
		executer.withTasks(taskName).run()

		where:
		taskName << ['help', 'tasks']
		dsl << [GROOVY_DSL, KOTLIN_DSL]
	}

	def "can execute commands successfully"(dsl) {
		def fixture = new SampleContentFixture(sampleName)
		unzipTo(fixture.getDslSample(dsl), temporaryFolder.testDirectory)

		GradleExecuter executer = new GradleExecuterFactory().wrapper(TestFile.of(temporaryFolder.testDirectory)).withConsole(ConsoleOutput.Rich)
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

	// TODO: Migrate to TestFile
	void unzipTo(TestFile zipFile, File workingDirectory) {
		zipFile.assertIsFile()
		workingDirectory.mkdirs()
		assertSuccessfulExecution(['unzip', zipFile.getCanonicalPath(), '-d', workingDirectory.getCanonicalPath()])
	}

	private void assertSuccessfulExecution(List<String> commandLine, File workingDirectory = null) {
		def process = commandLine.execute(null, workingDirectory)
		def stdoutThread = Thread.start { process.in.eachByte { print(new String(it)) } }
		def stderrThread = Thread.start { process.err.eachByte { print(new String(it)) } }
		assert process.waitFor(30, TimeUnit.SECONDS)
		assert process.exitValue() == 0
		stdoutThread.join(5000)
		stderrThread.join(5000)
	}
}