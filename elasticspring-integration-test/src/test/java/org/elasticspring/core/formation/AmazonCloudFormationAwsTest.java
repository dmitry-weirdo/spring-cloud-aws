package org.elasticspring.core.formation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

// TODO discuss where which types of tests should live (e.g. tests requiring amazon environment)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("AmazonCloudFormationAwsTest-context.xml")
public class AmazonCloudFormationAwsTest {

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	@Autowired
	@Qualifier("staticStackNameProviderBasedStackResourceRegistry")
	private AmazonStackResourceRegistry staticStackNameProviderBasedStackResourceRegistry;

	@Test
	public void stackResourceRegistry_staticStackNameProvider_stackResourceRegistryBeanExposed() {
		// Assert
		assertThat(this.staticStackNameProviderBasedStackResourceRegistry, is(not(nullValue())));
	}

	@Test
	public void lookupPhysicalResourceId_staticStackNameProviderAndLogicalResourceIdOfExistingResourceProvided_returnsPhysicalResourceId() {
		// Act
		String physicalResourceId = this.staticStackNameProviderBasedStackResourceRegistry.lookupPhysicalResourceId("RdsSingleMicroInstance");

		// Assert
		assertThat(physicalResourceId, is(not(nullValue())));
	}

	@Test
	public void lookupPhysicalResourceId_logicalResourceIdOfNonExistingResourceProvided_throwsException() {
		// Assert
		// TODO decide on whether to use custom exception instead of generic one
		this.expectedException.expect(IllegalArgumentException.class);
		this.expectedException.expectMessage("No resource found with logical id 'nonExistingLogicalResourceId'");

		// Act
		this.staticStackNameProviderBasedStackResourceRegistry.lookupPhysicalResourceId("nonExistingLogicalResourceId");
	}

}
