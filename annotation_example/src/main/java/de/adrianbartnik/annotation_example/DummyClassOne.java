package de.adrianbartnik.annotation_example;

import de.adrianbartnik.annotation.MissingFeature;

@MissingFeature(featureDescription = "Implement Logic",
        severityLevel = MissingFeature.SeverityLevel.HIGH)
@MissingFeature(featureDescription = "Write Blogpost",
        severityLevel = MissingFeature.SeverityLevel.CRITICAL)
@MissingFeature(featureDescription = "Add Authorization")
public class DummyClassOne {
}
