package de.adrianbartnik.processor;

import com.squareup.javapoet.*;
import de.adrianbartnik.annotation.MissingFeature;
import de.adrianbartnik.annotation.MissingFeatureHolder;
import de.adrianbartnik.processor.data.MissingFeatureAnnotatedClass;
import de.adrianbartnik.processor.data.MissingFeaturesAnnotatedClass;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class holds all {@link MissingFeature}s for each class and each
 * {@link de.adrianbartnik.annotation.MissingFeature.SeverityLevel} and generates
 * a new java file with convenience features for those classes.
 */
public class MissingFeatureManagerGenerator {

    private static final String FILE_NAME = "MissingFeatureManager";
    private static final String PACKAGE = "de.adrianbartnik";

    private List<MissingFeatureHolder> missingFeatures = new ArrayList<>();

    /**
     * Adds an annotated class to this factory.
     */
    void add(MissingFeatureAnnotatedClass annotatedClass) {
        missingFeatures.add(annotatedClass.getMissingFeatureHolder());
    }

    void add(MissingFeaturesAnnotatedClass annotatedClass) {
        missingFeatures.addAll(annotatedClass.getMissingFeatureHolder());
    }

    private boolean generated = false;

    void generateCode(Filer filer) throws IOException {

        if (generated) {
            return;
        } else {
            generated = true;
        }

        ClassName missingFeature = ClassName.get(MissingFeatureHolder.class);
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        TypeName listOfFeatures = ParameterizedTypeName.get(list, missingFeature);

        FieldSpec missingFeatures = FieldSpec.builder(listOfFeatures, "missingFeatures")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T<>()", arrayList)
                .build();

        MethodSpec.Builder getAllMethod = MethodSpec.methodBuilder("getAllMissingFeatures")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return " + missingFeatures.name)
                .returns(listOfFeatures);


        MethodSpec.Builder checkSeverity = MethodSpec.methodBuilder("CheckNoBelow")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(MissingFeature.SeverityLevel.class, "severityLevel")
                .beginControlFlow("for ($L tmp : $L)", missingFeature, missingFeatures.name)
                .beginControlFlow("if ($L.getSeverityLevel().isMoreSevereThan(severityLevel))", "tmp")
                .addStatement("throw new IllegalStateException(\"There are more severe open features!\")")
                .endControlFlow()
                .endControlFlow();

        CodeBlock.Builder codeBlock = CodeBlock.builder();
        for (MissingFeatureHolder feature : this.missingFeatures) {
            codeBlock.addStatement(missingFeatures.name + ".add(new $T($S, $S, $L))",
                    MissingFeatureHolder.class,
                    feature.getClazz(),
                    feature.getMissingFeature(),
                    feature.getSeverityLevel());
        }

        TypeSpec typeSpec = TypeSpec.classBuilder(FILE_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(missingFeatures)
                .addStaticBlock(codeBlock.build())
                .addMethod(getAllMethod.build())
                .addMethod(checkSeverity.build())
                .build();

        // Write file
        JavaFile.builder(PACKAGE, typeSpec)
                .addStaticImport(MissingFeature.SeverityLevel.NONE)
                .addStaticImport(MissingFeature.SeverityLevel.LOW)
                .addStaticImport(MissingFeature.SeverityLevel.MEDIUM)
                .addStaticImport(MissingFeature.SeverityLevel.HIGH)
                .addStaticImport(MissingFeature.SeverityLevel.CRITICAL)
                .build()
                .writeTo(filer);
    }
}
