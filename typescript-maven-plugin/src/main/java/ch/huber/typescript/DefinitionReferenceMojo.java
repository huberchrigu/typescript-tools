package ch.huber.typescript;

import ch.huber.typescript.definitions.DefinitionReferenceCreator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.nio.file.Path;

/**
 * Goal which {@link DefinitionReferenceCreator creates references to typescript definition files}.
 *
 * @goal create-references
 * @phase process-sources
 */
public class DefinitionReferenceMojo
        extends AbstractMojo {
    /**
     * Location of the typescript sources.
     *
     * @parameter property="${project.build.sourceDirectory}"
     * @required
     */
    private Path typescriptSource;

    public void execute()
            throws MojoExecutionException {
        DefinitionReferenceCreator creator = new DefinitionReferenceCreator();
        getLog().info("Execute typescript definition references for path " + typescriptSource + "...");
        creator.execute(typescriptSource);
    }
}