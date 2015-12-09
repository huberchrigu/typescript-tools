package ch.huber.typescript.definitions.finder.result;

import org.junit.Test;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author christoph.huber
 * @since 08.12.2015
 */
public class TypescriptModuleTest {
    private static final String NAME = "name";
    private static final Path ROOT_PATH = FileSystems.getDefault().getPath("/root");
    private static final Path PATH1 = ROOT_PATH.resolve("some/other/path/script.ts");
    private static final Path PATH2 = ROOT_PATH.resolve("other/other.ts");

    private TypescriptModule testee = new TypescriptModule(NAME);

    @Test
    public void testRootDir() {
        testee.addTypescriptFile(PATH1);
        testee.addTypescriptFile(PATH2);
        assertThat(testee.getRootDir().toString()).isEqualTo(ROOT_PATH.toString());
    }

}