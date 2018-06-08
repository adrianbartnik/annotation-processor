package de.adrianbartnik.annotation_example;


import de.adrianbartnik.MissingFeatureManager;
import de.adrianbartnik.annotation.MissingFeature;

public class AnnotationExample {

    private static MissingFeatureManager manager = new MissingFeatureManager();

    public static void main(String[] args) {
        manager.CheckNoBelow(MissingFeature.SeverityLevel.MEDIUM);
    }
}
