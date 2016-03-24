package ch.huber.typescript;

import ch.huber.typescript.definitions.DefinitionReferenceCreator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.nio.file.Path;

/**
 * Goal which {@link DefinitionReferenceCreator creates references to typescript definition files}.
 */
@Mojo(name = "create-references", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class DefinitionReferenceMojo
        extends AbstractMojo {
    /**
     * Location of the typescript sources.
     */
    @Parameter(defaultValue = "${project.build.sourceDirectory}")
    private Path typescriptSource;

    public void execute()
            throws MojoExecutionException {
        DefinitionReferenceCreator creator = new DefinitionReferenceCreator();
        getLog().info("Execute typescript definition references for path " + typescriptSource + "...");
        creator.execute(typescriptSource);
    }
}