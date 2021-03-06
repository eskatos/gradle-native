package dev.nokee.ide.xcode

import org.apache.commons.lang3.SystemUtils
import spock.lang.Requires

class XcodeIdeWorkspaceFunctionalTest extends AbstractXcodeIdeFunctionalSpec {
	def "generates empty workspace on non-configured Xcode IDE plugin"() {
		given:
		settingsFile << "rootProject.name = 'app'"
		buildFile << applyXcodeIdePlugin()

		when:
		succeeds('xcode')

		then:
		result.assertTasksExecutedAndNotSkipped(':xcodeWorkspace', ':xcode')

		and:
		xcodeWorkspace('app').assertHasProjects()
	}

	@Requires({ SystemUtils.IS_OS_MAC })
	def "xcodebuild sees no schemes inside empty workspace"() {
		useXcodebuildTool()
		given:
		settingsFile << "rootProject.name = 'app'"
		buildFile << applyXcodeIdePlugin()

		when:
		succeeds('xcode')

		then:
		def result = xcodebuild.withWorkspace(xcodeWorkspace('app')).withArgument('-list').execute()
		result.out.contains('There are no schemes in workspace "app".')
	}

	def "uses root project name as Xcode workspace filename"() {
		given:
		settingsFile << "rootProject.name = 'app'"
		buildFile << applyXcodeIdePlugin()

		when:
		succeeds('xcode')

		then:
		testDirectory.listFiles(exceptHiddenFiles())*.name as Set == ['build.gradle', 'settings.gradle', 'app.xcworkspace'] as Set
	}

	// TODO: can remap projects included in the workspace (exclude projects)

	private static FilenameFilter exceptHiddenFiles() {
		return new FilenameFilter() {
			@Override
			boolean accept(File dir, String name) {
				return !name.startsWith('.')
			}
		}
	}
}
