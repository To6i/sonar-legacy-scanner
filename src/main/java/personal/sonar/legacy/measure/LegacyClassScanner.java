package personal.sonar.legacy.measure;

import java.util.List;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.Tree;

import personal.sonar.legacy.metrics.LegacyMetrics;

public class LegacyClassScanner extends BaseTreeVisitor implements JavaFileScanner
{
    private static final String LEGACY_CLASS_ANNOTATION = "Legacy";

    private final SensorContext sensorContext;

    private InputFile sonarFile;

    public LegacyClassScanner(SensorContext sensorContext)
    {
        this.sensorContext = sensorContext;
    }

    @Override
    public void scanFile(JavaFileScannerContext context)
    {
        FileSystem fileSystem = sensorContext.fileSystem();
        sonarFile = fileSystem.inputFile(fileSystem.predicates().is(context.getFile()));

        scan(context.getTree());
    }

    @Override
    public void visitClass(ClassTree tree)
    {
        if (isTopLevelClass(tree) && isLegacyClass(tree))
        {
            markAsLegacyClass();
        }
        super.visitClass(tree);
    }

    private boolean isTopLevelClass(ClassTree tree)
    {
        // top level class = not inner class
        if (tree.parent() == null || tree.parent().is(Tree.Kind.COMPILATION_UNIT))
        {
            return true;
        }
        return false;
    }

    private boolean isLegacyClass(ClassTree tree)
    {
        List<AnnotationTree> annotations = tree.modifiers().annotations();
        for (AnnotationTree annotationTree : annotations)
        {
            if (annotationTree.annotationType().is(Tree.Kind.IDENTIFIER))
            {
                IdentifierTree identifierTree = (IdentifierTree) annotationTree.annotationType();
                if (identifierTree.name().equals(LEGACY_CLASS_ANNOTATION))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private void markAsLegacyClass()
    {
        sensorContext.<Boolean> newMeasure()
                .forMetric(LegacyMetrics.LEGACY_CLASS)
                .on(sonarFile)
                .withValue(true)
                .save();
    }
}
