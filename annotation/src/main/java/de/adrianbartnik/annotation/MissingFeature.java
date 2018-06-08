package de.adrianbartnik.annotation;

import java.lang.annotation.*;

/**
 * Represents a missing feature for a class. A class may be annotated with multiple
 * {@link MissingFeature}s annotations. Internally, this is realized by the helper
 * annotation {@link MissingFeatures}.
 */
@Repeatable(MissingFeatures.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface MissingFeature {

    /**
     * Description of the missing feature
     */
    String featureDescription();

    /**
     * The severity of the missing feature
     */
    SeverityLevel severityLevel() default SeverityLevel.MEDIUM;

    /**
     * Represents the severity of a feature
     */
    enum SeverityLevel {
        CRITICAL(4),
        HIGH(3),
        MEDIUM(2),
        LOW(1),
        NONE(0);

        private final int severity;

        SeverityLevel(int severity) {
            this.severity = severity;
        }

        public boolean isMoreSevereThan(SeverityLevel other) {
            return this.severity > other.severity;
        }
    }
}
