package de.adrianbartnik.processor.data;

import de.adrianbartnik.annotation.MissingFeature;
import de.adrianbartnik.annotation.MissingFeatureHolder;
import de.adrianbartnik.processor.exception.ProcessingException;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

/**
 * Stores all classes with at exactly one {@link MissingFeature} annotation.
 */
public class MissingFeatureAnnotatedClass {

    private final String qualifiedGroupClassName;
    private final MissingFeatureHolder missingFeatureHolder;

    /**
     * @throws ProcessingException if feature description from annotation is null
     */
    public MissingFeatureAnnotatedClass(TypeElement classElement) throws ProcessingException {
        MissingFeature annotation = classElement.getAnnotation(MissingFeature.class);

        if (annotation.featureDescription().equals("")) {
            throw new ProcessingException(
                    classElement.getEnclosingElement(),
                    "feature description in @%s for class %s is null or empty! that's not allowed",
                    MissingFeature.class.getSimpleName(), classElement.getQualifiedName().toString());
        }

        this.qualifiedGroupClassName = classElement.getQualifiedName().toString();
        this.missingFeatureHolder = new MissingFeatureHolder(
                this.qualifiedGroupClassName,
                annotation.featureDescription(),
                annotation.severityLevel());
    }

    @Override
    public String toString() {
        return "MissingFeatureAnnotatedClass{" +
                "qualifiedGroupClassName='" + qualifiedGroupClassName + '\'' +
                ", missingFeatureHolder=" + missingFeatureHolder +
                '}';
    }

    /**
     * Get the full qualified name of the type specified in  {@link MissingFeature#severityLevel()} ()}.
     *
     * @return qualified name
     */
    public String getQualifiedFactoryGroupName() {
        return qualifiedGroupClassName;
    }

    public MissingFeatureHolder getMissingFeatureHolder() {
        return missingFeatureHolder;
    }
}