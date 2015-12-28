package ch.huber.typescript.definitions.finder.result;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * A module is given by its name, which also can be <code>null</code>.
 * It contains typescript files including <code>module x.y.z {}</code> and
 * one <code>mainDefinitionFile</code> that includes all references to the typescript files.
 *
 * @author christoph.huber
 * @since 08.12.2015
 */
public class TypescriptModule {
    private String moduleName;
    private List<Path> typescriptFiles = new ArrayList<>();
    private Path mainDefinitionFile;
    private Path rootDir;

    public TypescriptModule(@Nullable String moduleName) {
        this.moduleName = moduleName;
    }

    public Path getRootDir() {
        return rootDir;
    }

    public void setMainDefinitionFile(@NotNull Path mainDefinitionFile) {
        if (this.mainDefinitionFile != null) {
            throw new IllegalStateException("Multiple main definition files for " + this + ": " + this.mainDefinitionFile + " and " + mainDefinitionFile);
        }
        this.mainDefinitionFile = mainDefinitionFile;
    }

    public String getName() {
        return moduleName;
    }

    public void addTypescriptFile(Path file) {
        updateRootDir(file.getParent());
        typescriptFiles.add(file);
    }

    public List<Path> getTypescriptFiles() {
        return typescriptFiles;
    }

    public Path getMainDefinitionFile() {
        return mainDefinitionFile;
    }

    private void updateRootDir(@NotNull Path folderPath) {
        if (rootDir == null) {
            rootDir = folderPath;
        } else {
            rootDir = commonParent(rootDir, folderPath);
        }
    }

    private Path commonParent(Path path1, Path path2) {
        Path currentPath1 = path1.toAbsolutePath();
        Path currentPath2 = path2.toAbsolutePath();
        while (!currentPath1.equals(currentPath2)) {
            if (currentPath1.getNameCount() > currentPath2.getNameCount()) {
                currentPath1 = currentPath1.getParent();
            } else {
                currentPath2 = currentPath2.getParent();
            }
        }
        return currentPath1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypescriptModule that = (TypescriptModule) o;

        return !(moduleName != null ? !moduleName.equals(that.moduleName) : that.moduleName != null);

    }

    @Override
    public int hashCode() {
        return moduleName != null ? moduleName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TypescriptModule{" +
                "moduleName='" + moduleName + '\'' +
                '}';
    }
}