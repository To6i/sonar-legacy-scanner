package personal.sonar.legacy.metrics;

import java.util.List;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import com.google.common.collect.ImmutableList;

public class LegacyMetrics implements Metrics
{
    public static final String LEGACY_CLASS_KEY = "legacy_class";
    public static final Metric<Boolean> LEGACY_CLASS = new Metric.Builder(
            LEGACY_CLASS_KEY,
            "Legacy Class",
            Metric.ValueType.BOOL)
                    .setDescription("Legacy classes")
                    .setQualitative(false)
                    .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
                    .setDeleteHistoricalData(true)
                    .create();

    public static final String NEW_NON_LEGACY_CODE_COVERAGE_KEY = "new_non_legacy_code_coverage";
    public static final Metric<Double> NEW_NON_LEGACY_CODE_COVERAGE = new Metric.Builder(
            NEW_NON_LEGACY_CODE_COVERAGE_KEY,
            "Coverage on New, non Legacy Code",
            Metric.ValueType.PERCENT)
                    .setDescription("Coverage of new/changed code excluding legacy code")
                    .setDirection(Metric.DIRECTION_BETTER)
                    .setQualitative(true)
                    .setDomain(CoreMetrics.DOMAIN_COVERAGE)
                    .setWorstValue(0.0)
                    .setBestValue(100.0)
                    .setDeleteHistoricalData(true)
                    .create();

    @SuppressWarnings("rawtypes")
    @Override
    public List<Metric> getMetrics()
    {
        return ImmutableList.of(LEGACY_CLASS, NEW_NON_LEGACY_CODE_COVERAGE);
    }
}
