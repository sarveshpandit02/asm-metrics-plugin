package edu.utdallas.plugin;

import edu.utdallas.MetricsManager;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @goal metrics
 * @phase process-classes
 * @requiresDependencyResolution runtime
 */
public class AnalysisMojo extends AbstractMojo {

    /**
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    private File targetClassesDirectory;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            processAllClassFiles(targetClassesDirectory);
            MetricsManager.print();
        } catch (IOException e) {
            System.out.println("Error processing class files.");
        }
    }

    private void processAllClassFiles(File classesDir) throws IOException {
        for (File f : classesDir.listFiles()) {
            if (f.isDirectory())
                processAllClassFiles(f);
            else
            if (f.getName().endsWith(".class")) {
                System.out.println("Processing class: "+f.getName());
                try {
                    calculateMetrics(f);
                } catch (IOException e) {
                    throw new IOException("Failed to calculate metrics for file: " + f, e);
                }
            }
        }
    }

    private void calculateMetrics(File f) throws IOException {
        try (InputStream in = new FileInputStream(f)) {

            ClassReader cr = new ClassReader(in);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            CoverageClassVisitor ca = new CoverageClassVisitor(cw);

            cr.accept(ca, 0);
            cw.toByteArray();
        }
    }

}
