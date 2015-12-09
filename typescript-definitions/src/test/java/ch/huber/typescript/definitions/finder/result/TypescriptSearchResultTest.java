package ch.huber.typescript.definitions.finder.result;

import ch.huber.typescript.definitions.test.TestConstants;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author christoph.huber
 * @since 08.12.2015
 */
public class TypescriptSearchResultTest {
    private static final URL CLASS_LOADER_ROOT = TypescriptSearchResultTest.class.getResource("/");

    private TypescriptSearchResult testee;

    @Before
    public void setUp() throws URISyntaxException {
        withFiles(TestConstants.TYPESCRIPT_WITH_MODULE, TestConstants.TYPESCRIPT_WITHOUT_MODULE,
                TestConstants.TYPESCRIPT_TEST_MODULE_DEFINITION, TestConstants.TYPESCRIPT_OTHER_DEFINITION,
                TestConstants.TYPESCRIPT_TEST_FILE1, TestConstants.TYPESCRIPT_TEST_FILE2);
    }

    @Test
    public void testModules() throws URISyntaxException {
        List<TypescriptModule> modules = testee.getModules();
        assertThat(modules).hasSize(3);

        assertModule(modules, null, null, "", 1);
        assertModule(modules, "Chart", null, "chart", 1);
        assertModule(modules, "test", TestConstants.TYPESCRIPT_TEST_MODULE_DEFINITION, "testModule", 2);
    }

    private void assertModule(List<TypescriptModule> modules, String expectedName, String expectedMainDefinition, String expectedRootPath, int expectedNumberOfFiles) throws URISyntaxException {
        Path classLoaderRoot = Paths.get(CLASS_LOADER_ROOT.toURI());
        TypescriptModule module = findByName(modules, expectedName);
        assertThat(module).isNotNull();
        if (expectedMainDefinition == null) {
            assertThat(module.getMainDefinitionFile()).isNull();
        } else {
            assertThat(module.getMainDefinitionFile().toString()).isEqualTo(classLoaderRoot.resolve(expectedMainDefinition).toString());
        }
        assertThat(module.getRootDir().toString()).isEqualTo(classLoaderRoot.resolve(expectedRootPath).toString());
        assertThat(module.getTypescriptFiles()).hasSize(expectedNumberOfFiles);
    }

    private TypescriptModule findByName(List<TypescriptModule> modules, String name) {
        for (TypescriptModule module : modules) {
            if (StringUtils.equals(name, module.getName())) {
                return module;
            }
        }
        return null;
    }

    private void withFiles(String... scriptPath) throws URISyntaxException {
        URI classLoaderUri = CLASS_LOADER_ROOT.toURI();
        List<Path> paths = Arrays.stream(scriptPath).map(stringPath -> Paths.get(classLoaderUri).resolve(stringPath)).collect(Collectors.toList());
        testee = new TypescriptSearchResult(paths);
    }
}