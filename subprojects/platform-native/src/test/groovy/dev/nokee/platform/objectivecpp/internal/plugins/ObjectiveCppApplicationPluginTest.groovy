package dev.nokee.platform.objectivecpp.internal.plugins

import dev.nokee.fixtures.AbstractPluginTest
import dev.nokee.platform.objectivecpp.ObjectiveCppApplicationExtension
import org.gradle.api.Project
import spock.lang.Subject

trait ObjectiveCppApplicationPluginTestFixture {
	abstract Project getProjectUnderTest()

	void applyPluginUnderTest() {
		projectUnderTest.apply plugin: 'dev.nokee.objective-cpp-application'
	}

	void evaluateProject(String because) {
		projectUnderTest.evaluate()
	}

	def getExtensionUnderTest() {
		return projectUnderTest.application
	}

	Class getExtensionType() {
		return ObjectiveCppApplicationExtension
	}
}

@Subject(ObjectiveCppApplicationPlugin)
class ObjectiveCppApplicationPluginTest extends AbstractPluginTest implements ObjectiveCppApplicationPluginTestFixture {
}
