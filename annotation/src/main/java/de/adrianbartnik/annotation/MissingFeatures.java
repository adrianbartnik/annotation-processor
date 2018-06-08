package de.adrianbartnik.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface MissingFeatures {

    /**
     * Contains all repeated {@link MissingFeature} annotations for one class
     */
    MissingFeature[] value();
}
