package base;

import base.propertyConfig.PropertyConfig;
import base.repository.ReportStepRepository;
import base.repository.ReportTestRepository;
import base.repository.mongo.notReactive.MongoCollectionRepoImpl;

public class MobileExtensionContext {
    public static MongoCollectionRepoImpl mongoInstance;
    public static PropertyConfig getProperty() { return new PropertyConfig(); }
}
