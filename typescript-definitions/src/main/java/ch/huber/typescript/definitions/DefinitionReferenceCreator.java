package ch.huber.typescript.definitions;

import ch.huber.typescript.definitions.finder.TypescriptFinder;
import ch.huber.typescript.definitions.finder.result.TypescriptModule;
import ch.huber.typescript.definitions.finder.result.TypescriptSearchResult;
import ch.huber.typescript.definitions.writer.MainDefinitionFileCreator;
import ch.huber.typescript.definitions.writer.ReferenceWriter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * <ol>
 *     <li>{@link TypescriptFinder Search all typescript and typescript definitions files}</li>
 *     <li>Per module, if there is no definition file in the module's root path:
 *     <ol>
 *         <li>Create a module definition file that links the module typescript files</li>
 *         <li>Vice versa, the typescript files are extended by a link to the module definition file</li>
 *     </ol>
 *     </li>
 * </ol>
 * @author christoph.huber
 * @since 09.12.2015
 */
public class DefinitionReferenceCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefinitionReferenceCreator.class);

    public void execute(@NotNull Path rootPath) {
        TypescriptFinder finder = new TypescriptFinder(rootPath);
        TypescriptSearchResult result;
        try {
            result = finder.find();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        result.getModules().stream().forEach(module -> {
            LOGGER.info("Create new main definition file for module {} at {}", module.getName(), module.getRootDir());
            handleModule(module);
        });
    }

    private void handleModule(TypescriptModule module) {
        if (module.getMainDefinitionFile() == null) {
            new MainDefinitionFileCreator(module).create();
        } else {
            new MainDefinitionFileCreator(module).update();
        }
        Path mainDefinitionFile = module.getMainDefinitionFile();
        for (Path typescriptFile : module.getTypescriptFiles()) {
            new ReferenceWriter(typescriptFile).addReference(mainDefinitionFile).write();
        }
    }
}