package ch.chrigu.typescript.interfaces.spring;

import ch.chrigu.typescript.interfaces.spring.controller.RestControllerTypes;
import ch.chrigu.typescript.interfaces.spring.controller.RestControllerVisitor;
import ch.chrigu.typescript.interfaces.spring.controller.RestControllers;
import ch.chrigu.typescript.interfaces.spring.definitions.TypescriptInterfaces;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author christoph.huber
 * @since 14.12.2016
 */
public class RestControllerDescriptor {
    private final String sourceRoot;
    private String methodAnnotation;
    private String classAnnotation;

    public RestControllerDescriptor(String sourceRoot) {
        this.sourceRoot = sourceRoot;
    }

    public TypescriptInterfaces getTypescriptInterfaces() {
        RestControllers restControllers = findRestControllers();
        RestControllerMethods methods = restControllers.toMethods(methodAnnotation);
        RestControllerTypes types = methods.extractTypes();
        return types.convertToTypescriptInterfaces();
    }

    private RestControllers findRestControllers() {
        ClassLoader classLoader = initClassLoader();
        RestControllers restControllers = new RestControllers();
        FileVisitor fileVisitor = new RestControllerVisitor(restControllers, classAnnotation);
        try {
            Files.walkFileTree(Paths.get(sourceRoot), fileVisitor);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return restControllers;
    }

    private ClassLoader initClassLoader() {
        try {
            return new URLClassLoader(new URL[]{new File(sourceRoot).toURI().toURL()});
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }
}
