package de.adrianbartnik.annotation;

public class MissingFeatureHolder {

    private final String clazz;
    private final String missingFeature;
    private final MissingFeature.SeverityLevel severityLevel;

    @Override
    public String toString() {
        return "MissingFeatureHolder{" +
                "class=" + clazz +
                ", missingFeature='" + missingFeature + '\'' +
                ", severityLevel=" + severityLevel +
                '}';
    }

    public MissingFeatureHolder(String clazz, String missingFeature, MissingFeature.SeverityLevel severityLevel) {
        this.clazz = clazz;
        this.missingFeature = missingFeature;
        this.severityLevel = severityLevel;
    }

    public MissingFeature.SeverityLevel getSeverityLevel() {
        return severityLevel;
    }

    public String getMissingFeature() {
        return missingFeature;
    }

    public String getClazz() {
        return clazz;
    }
}
