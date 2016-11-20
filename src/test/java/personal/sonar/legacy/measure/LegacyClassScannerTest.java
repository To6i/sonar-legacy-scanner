package personal.sonar.legacy.measure;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.measure.Measure;
import org.sonar.api.internal.apachecommons.io.FilenameUtils;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

import personal.sonar.legacy.measure.LegacyClassScanner;
import personal.sonar.legacy.metrics.LegacyMetrics;

@RunWith(Parameterized.class)
public class LegacyClassScannerTest
{
    private static final String TEST_MODULE_KEY = "testModuleKey";

    @Parameters(name = "{index}: test-file {0}, expecting legacy measure={1}")
    public static Collection<Object[]> data()
    {
        return Arrays.asList(new Object[][] {
                { "src/test/files/LegacyClass.java", true },
                { "src/test/files/LegacyEnum.java", true },
                { "src/test/files/NonLegacyClass.java", false },
                { "src/test/files/DeprecatedClass.java", false },
                { "src/test/files/AnnotatedClass.java", false },
                { "src/test/files/OuterClassWithInnerLegacyClass.java", false }
        });
    }

    @Parameter(value = 0)
    public String testFile;
    @Parameter(value = 1)
    public boolean assertMeasureSet;

    @Test
    public void scanFile() throws Exception
    {
        // initialization
        SensorContextTester contextFake = createEnvironmentForFile(testFile);
        LegacyClassScanner classUnderTest = createClassUnderTest(contextFake);

        // execution: this will invoke scanFile()
        JavaCheckVerifier.verifyNoIssue(testFile, classUnderTest);

        // verification
        if (assertMeasureSet)
        {
            assertLegacyMeasureIsSet(contextFake, testFile);
        } else
        {
            assertLegacyMeasureNotSet(contextFake, testFile);
        }
    }

    private SensorContextTester createEnvironmentForFile(String testFile) throws IOException
    {
        String baseDir = FilenameUtils.getPath(testFile);
        SensorContextTester sensorContextFake = SensorContextTester.create(new File(baseDir));
        sensorContextFake.fileSystem()
                .add(new DefaultInputFile(TEST_MODULE_KEY, testFile)
                        .setLanguage("java")
                        .initMetadata(readFile(testFile)));
        return sensorContextFake;
    }

    private LegacyClassScanner createClassUnderTest(SensorContext sensorContext)
    {
        return new LegacyClassScanner(sensorContext);
    }

    private String readFile(String path) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    private void assertLegacyMeasureIsSet(SensorContextTester contextFake, String testFile)
    {
        Measure<Boolean> result = getMeasureResult(contextFake, testFile);
        assertNotNull(result);
        assertTrue(result.value());
    }

    private void assertLegacyMeasureNotSet(SensorContextTester contextFake, String testFile)
    {
        Measure<Boolean> result = getMeasureResult(contextFake, testFile);
        assertNull(result);
    }

    private Measure<Boolean> getMeasureResult(SensorContextTester contextFake, String testFile)
    {
        return contextFake.<Boolean> measure(TEST_MODULE_KEY + ":" + testFile, LegacyMetrics.LEGACY_CLASS_KEY);
    }
}
