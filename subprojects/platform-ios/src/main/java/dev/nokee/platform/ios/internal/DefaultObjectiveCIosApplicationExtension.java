package dev.nokee.platform.ios.internal;

import com.google.common.collect.ImmutableList;
import dev.nokee.language.base.internal.DefaultSourceSet;
import dev.nokee.language.objectivec.internal.UTTypeObjectiveCSource;
import dev.nokee.platform.base.internal.BuildVariant;
import dev.nokee.platform.base.internal.DefaultBuildVariant;
import dev.nokee.platform.base.internal.NamingScheme;
import dev.nokee.platform.ios.ObjectiveCIosApplicationExtension;
import dev.nokee.platform.nativebase.internal.BaseNativeApplicationComponent;
import dev.nokee.platform.nativebase.internal.DefaultNativeComponentDependencies;
import dev.nokee.runtime.nativebase.internal.DefaultMachineArchitecture;
import dev.nokee.runtime.nativebase.internal.DefaultOperatingSystemFamily;

import javax.inject.Inject;

public abstract class DefaultObjectiveCIosApplicationExtension extends BaseNativeApplicationComponent implements ObjectiveCIosApplicationExtension {
	@Inject
	public DefaultObjectiveCIosApplicationExtension(DefaultNativeComponentDependencies dependencies, NamingScheme names) {
		super(dependencies, names);
		getSourceCollection().add(getObjects().newInstance(DefaultSourceSet.class, new UTTypeObjectiveCSource()).srcDir("src/main/objc"));
	}

	@Override
	protected Iterable<BuildVariant> createBuildVariants() {
		return ImmutableList.of(DefaultBuildVariant.of(DefaultOperatingSystemFamily.forName("ios"), DefaultMachineArchitecture.X86_64));
	}
}
