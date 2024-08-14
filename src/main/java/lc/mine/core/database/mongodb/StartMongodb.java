package lc.mine.core.database.mongodb;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ClusterSettings;

import lc.mine.core.database.Database;

public final class StartMongodb {

    public Database start(final Logger logger, final FileConfiguration config) {
        final ConfigurationSection mongodb = config.getConfigurationSection("mongodb");
        if (mongodb == null) {
            logger.warning("Mongodb section don't found!");
            return null;
        }

        try {
            final String collection = mongodb.getString("collection", "data");
            final MongoCredential credential = MongoCredential.createCredential(mongodb.getString("user"), "lccore", mongodb.getString("pass").toCharArray());

            final Block<ClusterSettings.Builder> localhost = builder -> builder.hosts(List.of(new ServerAddress(mongodb.getString("ip", "localhost"), mongodb.getInt("port", 27017))));
            final MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(localhost)
                .credential(credential)
                .build();

            Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);

            final MongoClient client = MongoClients.create(settings);

            final MongoDatabase mongoDatabase = client.getDatabase("lccore");
            final MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);
            final Document document = mongoCollection.find().limit(1).first();

            if (document == null) {
                mongoDatabase.createCollection(collection);
                mongoCollection.insertOne(new Document());
            }
            return new MongoData(client, mongoCollection);            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Can't connect to database.", e);
            return null;
        }
    }
}
