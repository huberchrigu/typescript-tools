package ch.huber.typescript.definitions;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Copies /src/test/resources to /copy and executes the integration test there.
 * Finally the directory is deleted again.
 *
 * @author christoph.huber
 * @since 09.12.2015
 */
public class DefinitionReferenceCreatorIntTest {

    private Path copiedPath;

    @Before
    public void setUp() throws URISyntaxException, IOException {
        Path testResourcePath = Paths.get(getClass().getResource("/").toURI());
        copiedPath = testResourcePath.resolve("../../target/copy").toAbsolutePath().normalize();
        FileUtils.copyDirectory(testResourcePath.toFile(), copiedPath.toFile());
    }

    @Test
    public void testTestResources() throws IOException {
        new DefinitionReferenceCreator().execute(copiedPath);
        assertModuleDefinition("_root.d.ts", "greeter.ts");
        assertModuleDefinition("chart/_Chart.d.ts", "data.ts");
        assertModuleDefinition("testModule/test.d.ts", "dir/file.ts", "dir2/file.ts");
        assertExternalReference("testModule/test.d.ts", "some-external-reference.d.ts");
    }

    private void assertExternalReference(String relDefinitionFilePath, String referencePath) throws IOException {
        Path definitionFilePath = copiedPath.resolve(relDefinitionFilePath);
        File definitionFile = definitionFilePath.toFile();
        assertThat(definitionFile).exists();
        FileReader fileReader = new FileReader(definitionFile);
        String content = IOUtils.toString(fileReader);
        fileReader.close();
        assertReference(content, referencePath);
    }

    /**
     *
     * @param relativeDefinitionFilePath The module's definition file.
     * @param typescriptFiles Typescript files belonging to the module
     */
    private void assertModuleDefinition(String relativeDefinitionFilePath, String ...typescriptFiles) throws IOException {
        Path definitionFilePath = copiedPath.resolve(relativeDefinitionFilePath);
        File definitionFile = definitionFilePath.toFile();
        assertThat(definitionFile).exists();
        FileReader reader = new FileReader(definitionFile);
        String definitionFileContent = IOUtils.toString(reader);
        reader.close();
        for (String typescriptFileRelPath : typescriptFiles) {
            assertReference(definitionFileContent, typescriptFileRelPath);
            Path referencedFilePath = definitionFilePath.getParent().resolve(typescriptFileRelPath);
            assertTypescriptFileContainsReferenceToDefinition(referencedFilePath.toFile(), definitionFilePath);
        }
    }

    private void assertTypescriptFileContainsReferenceToDefinition(File typescriptFile, Path definitionFilePath) throws IOException {
        assertThat(typescriptFile).exists();
        FileReader reader = new FileReader(typescriptFile);
        String typescriptContent = IOUtils.toString(reader);
        reader.close();
        Path relativePathToDefinition = typescriptFile.toPath().getParent().relativize(definitionFilePath);
        assertReference(typescriptContent, relativePathToDefinition.toString().replaceAll("\\\\", "/"));
    }

    /**
     * Asserts that the reference exists exactly once.
     */
    private void assertReference(String references, String referencedFile) {
        String pathAttribute = "path=\"" + referencedFile + "\"";
        assertThat(references).contains(pathAttribute);
        int indexOfPathAttribute = references.indexOf(pathAttribute);
        String referencesAfterFirstMatch = references.substring(indexOfPathAttribute + pathAttribute.length());
        assertThat(referencesAfterFirstMatch).doesNotContain(pathAttribute);
    }

    @After
    public void cleanUp() throws IOException {
        FileUtils.deleteDirectory(copiedPath.toFile());
    }
}