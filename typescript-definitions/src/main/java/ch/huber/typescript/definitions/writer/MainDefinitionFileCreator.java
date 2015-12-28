package ch.huber.typescript.definitions.writer;

import ch.huber.typescript.definitions.finder.result.TypescriptModule;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Writes all module's typescript files into a reference file.
 *
 * @author christoph.huber
 * @since 09.12.2015
 */
public class MainDefinitionFileCreator {
    private static final String DEFAULT_MODULE_NAME = "root";
    private final TypescriptModule module;

    public MainDefinitionFileCreator(TypescriptModule module) {
        this.module = module;
    }

    public Path create() {
        String moduleName = module.getName() == null ? DEFAULT_MODULE_NAME : module.getName();
        Path mainDefinitionPath = Paths.get(module.getRootDir() + "/_" + moduleName + ".d.ts");
        ReferenceWriter referenceWriter = new ReferenceWriter(mainDefinitionPath);
        module.getTypescriptFiles().forEach(referenceWriter::addReference);
        referenceWriter.write();
        module.setMainDefinitionFile(mainDefinitionPath);
        return mainDefinitionPath;
    }
}
