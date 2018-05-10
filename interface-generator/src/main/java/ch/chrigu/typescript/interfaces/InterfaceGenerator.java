package ch.chrigu.typescript.interfaces;

import ch.chrigu.typescript.interfaces.args.GeneratorArgs;
import ch.chrigu.typescript.interfaces.serializer.TypescriptSerializer;
import ch.chrigu.typescript.interfaces.spring.RestControllerDescriptor;

/**
 * @author christoph.huber
 * @since 14.12.2016
 */
public class InterfaceGenerator {
    public static void main(String[] args) {
        GeneratorArgs generatorArgs = new GeneratorArgs(args);
        RestControllerDescriptor restControllerDescriptor = new RestControllerDescriptor(generatorArgs.getSourceRoot());
        TypescriptSerializer typescriptSerializer = new TypescriptSerializer(generatorArgs.getTargetRoot());
        typescriptSerializer.writeFiles(restControllerDescriptor.getTypescriptInterfaces());
    }
}
