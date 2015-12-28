package ch.huber.typescript.definitions.parser;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author christoph.huber
 * @since 08.12.2015
 */
public class TypescriptFileParser {
    private static final Pattern MODULE_PATTERN = Pattern.compile("\\s*module\\s+([.\\w0-9-_]+).*");
    private final Path file;

    public TypescriptFileParser(Path file) {
        this.file = file;
    }

    public @Nullable String getModuleName() {
        FileReader fileReader = getFileReader();
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String moduleName = null;
        String line;
        while((line = readLine(bufferedReader)) != null) {
            Matcher matcher = MODULE_PATTERN.matcher(line);
            if (matcher.matches()) {
                moduleName = matcher.group(1);
            }
        }
        IOUtils.closeQuietly(bufferedReader);
        IOUtils.closeQuietly(fileReader);
        return moduleName;
    }

    private String readLine(BufferedReader bufferedReader) {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot parse typescript file", e);
        }
    }

    private FileReader getFileReader() {
        try {
            return new FileReader(file.toFile());
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Typescript file cannot be read", e);
        }
    }
}
