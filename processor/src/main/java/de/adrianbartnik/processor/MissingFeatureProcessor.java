package de.adrianbartnik.processor;

import com.google.auto.service.AutoService;
import de.adrianbartnik.annotation.MissingFeature;
import de.adrianbartnik.annotation.MissingFeatures;
import de.adrianbartnik.processor.data.MissingFeatureAnnotatedClass;
import de.adrianbartnik.processor.data.MissingFeaturesAnnotatedClass;
import de.adrianbartnik.processor.exception.ProcessingException;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class MissingFeatureProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private MissingFeatureManagerGenerator missingFeatureManagerGenerator = new MissingFeatureManagerGenerator();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(MissingFeature.class.getCanonicalName());
        set.add(MissingFeatures.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        try {

            processMissingFeatureAnnotations(roundEnv);
            processMissingFeaturesAnnotations(roundEnv);

            missingFeatureManagerGenerator.generateCode(filer);

        } catch (ProcessingException exception) {
            error(exception.getElement(), exception.getMessage());
        } catch (IOException exception) {
            error(null, exception.getMessage());
        }

        return true;
    }

    private void processMissingFeatureAnnotations(RoundEnvironment roundEnv) throws ProcessingException {
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(MissingFeature.class)) {

            // Check if a class has been annotated with @MissingFeature
            checkForClassType(annotatedElement);

            // We can cast it, because we know that it of ElementKind.CLASS
            TypeElement typeElement = (TypeElement) annotatedElement;

            MissingFeatureAnnotatedClass missingFeatureAnnotatedClass = new MissingFeatureAnnotatedClass(typeElement);

            missingFeatureManagerGenerator.add(missingFeatureAnnotatedClass);
        }
    }

    private void processMissingFeaturesAnnotations(RoundEnvironment roundEnv) throws ProcessingException {
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(MissingFeatures.class)) {

            checkForClassType(annotatedElement);

            MissingFeature[] repeatedAnnotations = annotatedElement.getAnnotationsByType(MissingFeature.class);

            if (repeatedAnnotations.length <= 0) {
                continue;
            }

            // We can cast it, because we know that it of ElementKind.CLASS
            TypeElement typeElement = (TypeElement) annotatedElement;

            MissingFeaturesAnnotatedClass missingFeatureAnnotatedClass =
                    new MissingFeaturesAnnotatedClass(typeElement, repeatedAnnotations);

            missingFeatureManagerGenerator.add(missingFeatureAnnotatedClass);
        }
    }

    private void checkForClassType(Element annotatedElement) throws ProcessingException {
        // Check if a class has been annotated with @MissingFeature
        if (annotatedElement.getKind() != ElementKind.CLASS) {
            throw new ProcessingException(annotatedElement, "Only classes can be annotated with @%s",
                    MissingFeature.class.getSimpleName());
        }
    }

    /**
     * Prints an error message
     *
     * @param e   The element which has caused the error. Can be null
     * @param msg The error message
     */
    private void error(Element e, String msg) {
        messager.printMessage(Diagnostic.Kind.ERROR, msg, e);
    }
}