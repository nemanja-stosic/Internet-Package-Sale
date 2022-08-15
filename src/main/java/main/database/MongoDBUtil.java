package main.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.model.InternetPackage;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDBUtil {

    private static class MongoDBUtilSingleton {
        private static final MongoDBUtil INSTANCE = new MongoDBUtil();
    }

    private MongoDBUtil() {
    }

    public static MongoDBUtil getInstance() {
        return MongoDBUtilSingleton.INSTANCE;
    }

    private void connect() {

    }

    public static void insertInternetPackage(InternetPackage newInternetPackage) {
        MongoClient mongo = new MongoClient("localhost", 27017);

        //opening database
        MongoDatabase database = mongo.getDatabase("internet_package_sales");

        //making collection with name user from InternetPackage class
        MongoCollection<InternetPackage> collection = database.getCollection("sales", InternetPackage.class);

        //configuring CodecProvider so we can use InternetPackage class
        CodecRegistry cr = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        collection = collection.withCodecRegistry(cr);
        collection.insertOne(newInternetPackage);
    }

    public static ObservableList<InternetPackage> getAllInternetPackages() {
        ObservableList<InternetPackage> list = FXCollections.observableArrayList();
        //Creating a MongoDB client
        MongoClient mongo = new MongoClient("localhost", 27017);

        //Connecting to the database
        MongoDatabase database = mongo.getDatabase("internet_package_sales");

        //Creating a collection object
        MongoCollection<InternetPackage> collection = database.getCollection("sales", InternetPackage.class);

        //configuring CodecProvider so we can use InternetPackage class
        CodecRegistry cr = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        collection = collection.withCodecRegistry(cr);

        for (InternetPackage internetPackage : collection.find()) {
            list.add(internetPackage);
        }

        return list;
    }

    public static void delete(String fieldName, String valueToDelete) {
        //Creating a MongoDB client
        MongoClient mongo = new MongoClient("localhost", 27017);

        //Connecting to the database
        MongoDatabase database = mongo.getDatabase("internet_package_sales");

        //Creating a collection object
        MongoCollection<InternetPackage> collection = database.getCollection("sales", InternetPackage.class);

        //configuring CodecProvider so we can use InternetPackage class
        CodecRegistry cr = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        collection = collection.withCodecRegistry(cr);

        collection.deleteOne(Filters.eq(fieldName, valueToDelete));
    }

}
