package ch.huber.typescript.definitions.writer;

import ch.huber.typescript.definitions.finder.result.TypescriptModule;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Writes all module's typescript files into a reference file.
 * <p>If the file already exists, its references can be updated.</p>
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
        assert module.getMainDefinitionFile() == null;

        String moduleName = module.getName() == null ? DEFAULT_MODULE_NAME : module.getName();
        Path mainDefinitionPath = Paths.get(module.getRootDir() + "/_" + moduleName + ".d.ts");
        writeReferences(mainDefinitionPath);
        module.setMainDefinitionFile(mainDefinitionPath);
        return mainDefinitionPath;
    }

    public void update() {
        assert module.getMainDefinitionFile() != null;

        writeReferences(module.getMainDefinitionFile());
    }

    private void writeReferences(Path mainDefinitionPath) {
        ReferenceWriter referenceWriter = new ReferenceWriter(mainDefinitionPath);
        module.getTypescriptFiles().forEach(referenceWriter::addReference);
        referenceWriter.write();
    }
}
