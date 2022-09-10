package base.repository;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoBase {

    private final String dbName;
    private final MongoConnection mongoConnection;
    private final MongoCollection<Document> mongoCollection;

    public MongoBase(MongoConnection mongoConnection, String collectionName) {
        this.mongoConnection = mongoConnection;
        this.mongoCollection = mongoConnection.getMongoDatabase().getCollection(collectionName);
        this.dbName = mongoConnection.getMongoDatabase().getName();
    }

    public String getDbName() { return dbName; }
    public MongoConnection getMongoConnection() { return mongoConnection; }
    public MongoCollection<Document> getMongoCollection() { return mongoCollection; }


    public List<Document> getListDatabases() {
        return this.getMongoConnection()
                .getMongoClient()
                .listDatabases()
                .into(new ArrayList<>());
    }

    public List<String> getListDatabasesNames() {
        return this.getMongoConnection()
                .getMongoClient()
                .listDatabaseNames()
                .into(new ArrayList<>());
    }
}
