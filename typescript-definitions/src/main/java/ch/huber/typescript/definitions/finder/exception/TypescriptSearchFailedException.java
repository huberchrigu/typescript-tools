package ch.huber.typescript.definitions.finder.exception;

import java.nio.file.Path;

/**
 * @author christoph.huber
 * @since 08.12.2015
 */
public class TypescriptSearchFailedException extends RuntimeException {
    public TypescriptSearchFailedException(Path failingFile) {
        super("Failed searching for typescript files at " + failingFile);
    }
}
