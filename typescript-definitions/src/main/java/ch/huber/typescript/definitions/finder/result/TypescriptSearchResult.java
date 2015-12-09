package ch.huber.typescript.definitions.finder.result;

import ch.huber.typescript.definitions.parser.TypescriptFileParser;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the search result as a list of {@link TypescriptModule}.
 * A module is defined by the <code>module x.y.z {}</code> declarations in the typescript files.
 *
 * @author christoph.huber
 * @since 08.12.2015
 */
public class TypescriptSearchResult {
    private static final Logger LOGGER = LoggerFactory.getLogger(TypescriptSearchResult.class);
    private static final String DEFINITION_FILE_EXTENSION = ".d.ts";
    private final List<TypescriptModule> modules = new ArrayList<>();

    public TypescriptSearchResult(@NotNull List<Path> typescriptFiles) {
        List<Path> definitionFiles = extractDefinitionFiles(typescriptFiles);
        parseTypescriptFileModules(typescriptFiles);
        assignDefinitionFiles(definitionFiles);
    }

    public List<TypescriptModule> getModules() {
        return modules;
    }

    private void assignDefinitionFiles(List<Path> definitionFiles) {
        for (Path definitionFile : definitionFiles) {
            boolean found = false;
            Path dir = definitionFile.getParent();
            for (TypescriptModule module : modules) {
                if (dir.equals(module.getRootDir())) {
                    module.setMainDefinitionFile(definitionFile);
                    found = true;
                    break;
                }
            }
            if (!found) {
                LOGGER.warn("There was a typescript definition file {} that could not be assigned to any module", definitionFile);
            }
        }
    }

    private void parseTypescriptFileModules(@NotNull List<Path> typescriptFiles) {
        for (Path file : typescriptFiles) {
            String moduleName = new TypescriptFileParser(file).getModuleName();
            TypescriptModule module = getOrCreateModule(moduleName);
            module.addTypescriptFile(file);
        }
    }

    private TypescriptModule getOrCreateModule(String moduleName) {
        for (TypescriptModule module : modules) {
            if (StringUtils.equals(module.getName(), moduleName)) {
                return module;
            }
        }
        TypescriptModule module = new TypescriptModule(moduleName);
        modules.add(module);
        return module;
    }


    private List<Path> extractDefinitionFiles(List<Path> typescriptFiles) {
        List<Path> definitionFiles = new ArrayList<>();
        for (int i = typescriptFiles.size() - 1; i >= 0; i--) {
            Path file = typescriptFiles.get(i);
            if (file.toString().endsWith(DEFINITION_FILE_EXTENSION)) {
                definitionFiles.add(file);
                typescriptFiles.remove(i);
            }
        }
        return definitionFiles;
    }
}
