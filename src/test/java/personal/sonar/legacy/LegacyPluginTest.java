package personal.sonar.legacy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.sonar.api.Plugin.Context;

import personal.sonar.legacy.measure.LegacyClassScanner;
import personal.sonar.legacy.measure.NewNonLegacyCodeCoverageComputer;
import personal.sonar.legacy.metrics.LegacyMetrics;

@RunWith(MockitoJUnitRunner.class)
public class LegacyPluginTest
{
    @Mock
    private Context contextMock;

    private LegacyPlugin classUnderTest;

    @Before
    public void init()
    {
        classUnderTest = new LegacyPlugin();
    }

    @Test
    public void define_AllNecessaryClassesAdded() throws Exception
    {
        classUnderTest.define(contextMock);

        Mockito.verify(contextMock).addExtensions(
                LegacyMetrics.class,
                LegacyClassScanner.class,
                NewNonLegacyCodeCoverageComputer.class);
    }
}
