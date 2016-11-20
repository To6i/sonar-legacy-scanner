package personal.sonar.legacy.measure;

import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;
import org.sonar.api.measures.CoreMetrics;

import personal.sonar.legacy.metrics.LegacyMetrics;

public class NewNonLegacyCodeCoverageComputer implements MeasureComputer
{
    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext defContext)
    {
        return defContext
                .newDefinitionBuilder()
                .setInputMetrics(
                        CoreMetrics.NEW_COVERAGE_KEY,
                        LegacyMetrics.LEGACY_CLASS_KEY)
                .setOutputMetrics(LegacyMetrics.NEW_NON_LEGACY_CODE_COVERAGE_KEY)
                .build();
    }

    @Override
    public void compute(MeasureComputerContext context)
    {
        Measure newCoverage = context.getMeasure(CoreMetrics.NEW_COVERAGE_KEY);
        if (newCoverage != null && !isLegacyClass(context))
        {
            context.addMeasure(LegacyMetrics.NEW_NON_LEGACY_CODE_COVERAGE_KEY, newCoverage.getDoubleValue());
        }
    }

    private boolean isLegacyClass(MeasureComputerContext context)
    {
        Measure legacyClass = context.getMeasure(LegacyMetrics.LEGACY_CLASS_KEY);
        if (legacyClass != null && legacyClass.getBooleanValue())
        {
            return true;
        }
        return false;
    }
}
