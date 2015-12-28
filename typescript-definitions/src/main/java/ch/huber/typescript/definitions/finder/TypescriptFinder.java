package ch.huber.typescript.definitions.finder;

import ch.huber.typescript.definitions.finder.result.TypescriptSearchResult;
import ch.huber.typescript.definitions.finder.visitor.TypescriptVisitor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Searches for all *.ts files below a given path. The result is split into definition files *.d.ts and regular
 * typescript files.
 *
 * @author christoph.huber
 * @since 08.12.2015
 */
public class TypescriptFinder {
    private Path rootPath;

    public TypescriptFinder(@NotNull Path rootPath) {
        this.rootPath = rootPath;
    }

    public TypescriptSearchResult find() throws IOException {
        TypescriptVisitor typescriptVisitor = new TypescriptVisitor();
        Files.walkFileTree(rootPath, typescriptVisitor);
        List<Path> typescriptFiles = typescriptVisitor.getFoundFiles();
        return new TypescriptSearchResult(typescriptFiles);
    }
}