package personal.sonar.legacy.metrics;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.measures.Metric;

public class LegacyMetricsTest
{
    private LegacyMetrics classUnderTest;

    @Before
    public void init()
    {
        classUnderTest = new LegacyMetrics();
    }

    @Test
    public void getMetrics_LegacyClassAndNewCoverageProvided() throws Exception
    {
        List<Metric> providedMetrics = classUnderTest.getMetrics();

        assertThat(providedMetrics, hasItem(LegacyMetrics.LEGACY_CLASS));
        assertThat(providedMetrics, hasItem(LegacyMetrics.NEW_NON_LEGACY_CODE_COVERAGE));
    }
}
