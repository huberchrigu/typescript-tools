package ch.huber.typescript.definitions.finder.visitor;

import ch.huber.typescript.definitions.finder.exception.TypescriptSearchFailedException;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Collects all found *.ts files.
 *
 * @author christoph.huber
 * @since 08.12.2015
 */
public class TypescriptVisitor implements FileVisitor<Path> {
    private static final String TYPESCRIPT_EXTENSION = ".ts";
    List<Path> typescriptFiles = new ArrayList<>();

    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (file.toString().endsWith(TYPESCRIPT_EXTENSION)) {
            typescriptFiles.add(file);
        }
        return FileVisitResult.CONTINUE;
    }

    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        throw new TypescriptSearchFailedException(file);
    }

    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    public List<Path> getFoundFiles() {
        return typescriptFiles;
    }
}
