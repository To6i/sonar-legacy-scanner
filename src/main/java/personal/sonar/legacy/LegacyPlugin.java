/*
 * Copyright (C) 2009-2014 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */
package personal.sonar.legacy;

import org.sonar.api.Plugin;

import personal.sonar.legacy.measure.LegacyClassScanner;
import personal.sonar.legacy.measure.NewNonLegacyCodeCoverageComputer;
import personal.sonar.legacy.metrics.LegacyMetrics;

/**
 * Entry point of this sonarqube plugin
 */
public class LegacyPlugin implements Plugin
{
    @Override
    public void define(Context context)
    {
        context.addExtensions(
                LegacyMetrics.class,
                LegacyClassScanner.class,
                NewNonLegacyCodeCoverageComputer.class);
    }
}
