package ch.huber.typescript.definitions.writer;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Writes references to <code>file</code>.
 * If it already exists, the references are added to the top.
 *
 * @author christoph.huber
 * @since 09.12.2015
 */
public class ReferenceWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceWriter.class);
    private static final String PATH_PARAM_SUFFIX = "\"";
    private static final String PATH_PARAM_PREFIX = "path=\"";
    private static final String PATH_PARAM_SUFFIX2 = "'";
    private static final String PATH_PARAM_PREFIX2 = "path='";

    private final Path file;
    private final ArrayList<Path> references;

    public ReferenceWriter(@NotNull Path file) {
        this.file = file;
        this.references = new ArrayList<>();
    }

    public ReferenceWriter addReference(@NotNull Path reference) {
        references.add(reference);
        return this;
    }

    public void write() {
        List<String> lines = getOldFileLines();
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file.toFile());
            for (Path reference : references) {
                if (!referenceExists(reference, lines)) {
                    fileWriter.write(getReferenceString(reference) + "\n");
                }
            }
            for (String line : lines) {
                fileWriter.write(line);
            }
        } catch (IOException e) {
            LOGGER.error("Could not write to {}", file.toString());
        } finally {
            IOUtils.closeQuietly(fileWriter);
        }
    }

    private @NotNull List<String> getOldFileLines() {
        try {
            return Files.readAllLines(file);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private String getReferenceString(Path reference) {
        String relativePath = getRelativePath(reference);
        return "/// <reference " + PATH_PARAM_PREFIX + relativePath + PATH_PARAM_SUFFIX + "/>";
    }

    /**
     * To get the correct relative path, the reference's file parent needs to be taken,
     * which is the directory the source file is located in.
     */
    private String getRelativePath(Path reference) {
        String relativePath = file.getParent().relativize(reference).normalize().toString();
        String unixRelativePath = relativePath.replaceAll("\\" + File.separator, "/");
        return unixRelativePath;
    }

    private boolean referenceExists(Path reference, List<String> oldLines) {
        String relativePath = getRelativePath(reference);
        for (String line : oldLines) {
            if (line.contains(PATH_PARAM_PREFIX + relativePath + PATH_PARAM_SUFFIX) ||
                    line.contains(PATH_PARAM_PREFIX2 + relativePath + PATH_PARAM_SUFFIX2)) {
                return true;
            }
        }
        return false;
    }
}