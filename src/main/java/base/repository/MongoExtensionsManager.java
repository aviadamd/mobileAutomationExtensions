package base.repository;

import base.repository.mongo.notReactive.MongoCollectionRepoImpl;

public class MongoExtensionsManager {
    private static final ThreadLocal<MongoCollectionRepoImpl> mongoInstance = new ThreadLocal<>();

    public static void setMongoInstance(MongoCollectionRepoImpl mongo) { mongoInstance.set(mongo); }

    public static MongoCollectionRepoImpl getMongoInstance() { return mongoInstance.get(); }

    public static void aVoid() { mongoInstance.remove(); }
}
