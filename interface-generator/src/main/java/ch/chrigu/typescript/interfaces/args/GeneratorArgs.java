package ch.chrigu.typescript.interfaces.args;

/**
 * @author christoph.huber
 * @since 14.12.2016
 */
public class GeneratorArgs {
    private String sourceRoot;
    private String targetRoot;

    public GeneratorArgs(String[] args) {
        init(args);
    }

    private void init(String[] args) {
        //TODO
    }

    public String getSourceRoot() {
        return sourceRoot;
    }

    public String getTargetRoot() {
        return targetRoot;
    }
}
