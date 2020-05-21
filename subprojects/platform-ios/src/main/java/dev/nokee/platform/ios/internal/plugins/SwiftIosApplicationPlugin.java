package dev.nokee.platform.ios.internal.plugins;

import dev.nokee.platform.ios.SwiftIosApplicationExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;

import javax.inject.Inject;

public abstract class SwiftIosApplicationPlugin implements Plugin<Project> {
	@Inject
	protected abstract ObjectFactory getObjects();

	@Override
	public void apply(Project project) {
		project.getExtensions().add(SwiftIosApplicationExtension.class, "application", getObjects().newInstance(SwiftIosApplicationExtension.class));
	}
}
