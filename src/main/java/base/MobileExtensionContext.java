package base;

import base.propertyConfig.PropertyConfig;
import base.repository.ReportStepRepository;
import base.repository.ReportTestRepository;
import base.repository.mongo.notReactive.MongoCollectionRepoImpl;

public class MobileExtensionContext {
    public static MongoCollectionRepoImpl mongoInstance;
    public static StepFlowExtensions stepFlowExtensions() { return new StepFlowExtensions(); }
    public static PropertyConfig getProperty() { return new PropertyConfig(); }
    public static ReportTestRepository reportTestRepository() { return new ReportTestRepository(); }
    public static ReportStepRepository reportStepRepository() { return new ReportStepRepository(); }
}
