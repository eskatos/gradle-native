package dev.nokee.platform.ios.tasks.fixtures;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import dev.gradleplugins.test.fixtures.file.TestFile;
import dev.gradleplugins.test.fixtures.gradle.executer.ExecutionResult;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.gradle.api.Task;

import java.util.ArrayList;
import java.util.List;

public class WellBehavingTaskPropertyBuilder {
	@NonNull private final String propertyName;
	@NonNull private final Class<? extends Task> taskType;
	private final List<WellBehavingTaskTestCase> upToDateChecks = new ArrayList<>();
	private final List<WellBehavingTaskTestCase> cacheableChecks = new ArrayList<>();
	private final List<WellBehavingTaskTestCase> incrementalChecks = new ArrayList<>();
	private WellBehavingTaskPropertyValue initialValue = null;
	private WellBehavingTaskPropertyMutator mutator = null;

	public WellBehavingTaskPropertyBuilder(String propertyName, Class<? extends Task> taskType) {
		this.propertyName = propertyName;
		this.taskType = taskType;
	}

	public WellBehavingTaskPropertyBuilder withInitialValue(@NonNull WellBehavingTaskPropertyValue value) {
		assert this.initialValue == null : "initial value already configured";
		this.initialValue = value;
		return this;
	}

	//region Initial value
	public WellBehavingTaskPropertyBuilder withInitialValue(@NonNull String initialValue) {
		return withInitialValue(WellBehavingTaskPropertyValue.Quoted.of(initialValue));
	}

	public WellBehavingTaskPropertyBuilder outOfDateWhen(@NonNull WellBehavingTaskTransform transform) {
		upToDateChecks.add(new WellBehavingTaskTestCase(propertyName, transform, WellBehavingTaskSpec.taskUnderTestExecutedAndNotSkipped().and(WellBehavingTaskSpec::assertInitialState)));
		return this;
	}
	//endregion

	//region Configuration policy
	public WellBehavingTaskPropertyBuilder configureAsProperty() {
		mutator = new WellBehavingTaskPropertyMutator.Property(propertyName);
		return this;
	}

	public WellBehavingTaskPropertyBuilder configureAsFileCollection() {
		mutator = new WellBehavingTaskPropertyMutator.FileCollection(propertyName);
		return this;
	}
	//endregion

	public WellBehavingTaskPropertyBuilder restoreFromCacheWhen(@NonNull WellBehavingTaskTransform transform) {
		cacheableChecks.add(new WellBehavingTaskTestCase(propertyName, transform, WellBehavingTaskSpec.taskUnderTestCached().and(WellBehavingTaskSpec::assertInitialState)));
		return this;
	}

	// TODO: Find better way to do this
	public WellBehavingTaskPropertyBuilder incremental(@NonNull WellBehavingTaskTransform transform, WellBehavingTaskAssertion assertion) {
		incrementalChecks.add(new WellBehavingTaskTestCase(propertyName, transform, assertion));
		return this;
	}

	public WellBehavingTaskProperty build() {
		// TODO: assert can find property on tasktype?.. should assert in test
		// TODO: initial state should be aware of the type of property so it does the right thing
		//       Property#set in Kotlin vs Property = in Groovy (also for ListProperty, SetProperty, RegularFileProperty and DirectoryProperty)
		//       ConfigurableFileCollection#from to add vs ConfigurableFileCollection#setFrom to set
		return new WellBehavingTaskProperty(propertyName, new WellBehavingTaskTestSuite.UpToDateTestSuite(upToDateChecks), new WellBehavingTaskTestSuite.CacheableTestSuite(cacheableChecks), new WellBehavingTaskTestSuite.IncrementalTestSuite(incrementalChecks), mutator.configureWith(initialValue));
	}

	public WellBehavingTaskProperty ignore() {
		// TODO: Assert that no test case were configured as they will be ignored
		assert upToDateChecks.isEmpty();
		assert incrementalChecks.isEmpty();
		assert cacheableChecks.isEmpty();
		return WellBehavingTaskProperty.ignored(propertyName, initialValue == null ? "" : mutator.configureWith(initialValue));
	}
}
