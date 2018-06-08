package de.adrianbartnik.annotation_example;

import de.adrianbartnik.annotation.MissingFeature;

@MissingFeature(
        featureDescription = "Missing Feature Two",
        severityLevel = MissingFeature.SeverityLevel.HIGH)
public class DummyClassTwo {
}
