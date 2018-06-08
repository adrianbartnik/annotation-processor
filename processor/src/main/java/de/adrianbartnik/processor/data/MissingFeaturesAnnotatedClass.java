package de.adrianbartnik.processor.data;

import de.adrianbartnik.annotation.MissingFeature;
import de.adrianbartnik.annotation.MissingFeatureHolder;
import de.adrianbartnik.processor.exception.ProcessingException;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores all classes with at least two or more {@link MissingFeature} annotations.
 */
public class MissingFeaturesAnnotatedClass {

    private final String qualifiedGroupClassName;
    private final List<MissingFeatureHolder> missingFeatureHolder;

    /**
     * @throws ProcessingException if feature description from annotation is null
     */
    public MissingFeaturesAnnotatedClass(TypeElement classElement, MissingFeature[] repeatedAnnotations) throws ProcessingException {

        this.qualifiedGroupClassName = classElement.getQualifiedName().toString();
        this.missingFeatureHolder = new ArrayList<>(repeatedAnnotations.length);

        for (MissingFeature annotation : repeatedAnnotations) {

            if (annotation.featureDescription().equals("")) {
                throw new ProcessingException(
                        classElement.getEnclosingElement(),
                        "feature description in @%s for class %s is null or empty! that's not allowed",
                        MissingFeature.class.getSimpleName(), classElement.getQualifiedName().toString());
            }

            missingFeatureHolder.add(new MissingFeatureHolder(
                    this.qualifiedGroupClassName,
                    annotation.featureDescription(),
                    annotation.severityLevel()));
        }
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

    public List<MissingFeatureHolder> getMissingFeatureHolder() {
        return missingFeatureHolder;
    }
}