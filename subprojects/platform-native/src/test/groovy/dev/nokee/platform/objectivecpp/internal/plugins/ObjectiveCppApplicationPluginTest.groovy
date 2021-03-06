package dev.nokee.platform.objectivecpp.internal.plugins

import dev.nokee.fixtures.AbstractPluginTest
import dev.nokee.fixtures.AbstractTargetMachineAwarePluginTest
import dev.nokee.fixtures.AbstractTaskPluginTest
import dev.nokee.platform.objectivecpp.ObjectiveCppApplicationExtension
import org.gradle.api.Project
import spock.lang.Subject

trait ObjectiveCppApplicationPluginTestFixture {
	abstract Project getProjectUnderTest()

	String getPluginId() {
		return 'dev.nokee.objective-cpp-application'
	}

	void applyPluginUnderTest() {
		projectUnderTest.apply plugin: pluginId
	}

	def getExtensionUnderTest() {
		return projectUnderTest.application
	}

	String getExtensionNameUnderTest() {
		return 'application'
	}

	Class getExtensionType() {
		return ObjectiveCppApplicationExtension
	}

	String[] getExpectedVariantAwareTaskNames() {
		return ['objects', 'executable']
	}
}

@Subject(ObjectiveCppApplicationPlugin)
class ObjectiveCppApplicationPluginTest extends AbstractPluginTest implements ObjectiveCppApplicationPluginTestFixture {
	final String pluginIdUnderTest = pluginId
}

@Subject(ObjectiveCppApplicationPlugin)
class ObjectiveCppApplicationTargetMachineAwarePluginTest extends AbstractTargetMachineAwarePluginTest implements ObjectiveCppApplicationPluginTestFixture {
}

@Subject(ObjectiveCppApplicationPlugin)
class ObjectiveCppApplicationTaskPluginTest extends AbstractTaskPluginTest implements ObjectiveCppApplicationPluginTestFixture {
}
