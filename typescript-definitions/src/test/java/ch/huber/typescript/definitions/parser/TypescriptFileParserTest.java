package ch.huber.typescript.definitions.parser;

import ch.huber.typescript.definitions.test.TestConstants;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author christoph.huber
 * @since 08.12.2015
 */
public class TypescriptFileParserTest {
    private static final String MODULE_NAME = "Chart";

    private TypescriptFileParser testee;

    @Test
    public void testNoModuleName() throws URISyntaxException {
        withFile(TestConstants.TYPESCRIPT_WITHOUT_MODULE);
        assertThat(testee.getModuleName()).isNull();
    }
    @Test
     public void testModuleName() throws URISyntaxException {
        withFile(TestConstants.TYPESCRIPT_WITH_MODULE);
        assertThat(testee.getModuleName()).isEqualTo(MODULE_NAME);
    }

    private void withFile(String fileName) throws URISyntaxException {
        URI uri = getClass().getResource("/" + fileName).toURI();
        testee = new TypescriptFileParser(Paths.get(uri));
    }
}