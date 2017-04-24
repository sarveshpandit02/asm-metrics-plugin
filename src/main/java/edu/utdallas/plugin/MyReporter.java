package edu.utdallas.plugin;



import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

import java.util.Locale;

/**
 *
 * @goal process
 * @phase test-compatibility-report-generator
 * @requiresDependencyResolution runtime
 */
public class MyReporter extends AbstractMavenReport {

    @Parameter(readonly = true, required = true, defaultValue = "${project.reporting.outputDirectory}")
    private String outputDirectory;

    @Parameter(readonly = true, required = true, defaultValue = "${project}")
    private MavenProject project;

    @Component
    private Renderer siteRenderer;

    @Override protected Renderer getSiteRenderer() {
        return siteRenderer;
    }

    @Override protected String getOutputDirectory() {
        return "getOutputDirectory";
    }

    @Override protected MavenProject getProject() {
        return project;
    }

    @Override protected void executeReport(Locale locale) throws MavenReportException {
        Sink sink = getSink();

        sink.head();
        sink.title();
        sink.text("Schema Compatibility Results");
        sink.title_();
        sink.head_();
        sink.flush();
        sink.close();
    }

    @Override public String getOutputName() {
        return "getOutputName";
    }

    @Override public String getName(Locale locale) {
        return "getName";
    }

    @Override public String getDescription(Locale locale) {
        return "getDescription";
    }
}
