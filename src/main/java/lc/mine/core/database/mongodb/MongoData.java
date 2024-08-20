package lc.mine.core.database.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.Set;
import java.util.UUID;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import lc.mine.core.database.Database;
import lc.mine.core.database.PlayerData;

public final class MongoData implements Database {

    private final Map<UUID, PlayerData> cache = new HashMap<>();
    private final MongoClient client;
    private final MongoCollection<Document> collection;

    private static final String
        LCOINS = "coins",
        VIP_POINTS = "vip";

    MongoData(MongoClient client, MongoCollection<Document> collection) {
        this.client = client;
        this.collection = collection;
    }
    
    @Override
    public PlayerData getCached(UUID uuid) {
        return cache.get(uuid);
    }

    @Override
    public void save(final Player player) {
        final PlayerData data = cache.remove(player.getUniqueId());
        if (data == null) {
            return;
        }
        if (data instanceof PlayerData.New) {
            final Document document = new Document();
            document.put("_id", player.getName());
            document.put(LCOINS, data.getLcoins());
            document.put(VIP_POINTS, data.getVipPoins());
            CompletableFuture.runAsync(()->collection.insertOne(document));
            return;
        }
        final Bson query = createUpdateQuery(data);

        if (query != null) {
            CompletableFuture.runAsync(()->collection.updateOne(Filters.eq("_id", player.getName()), query));
        }
    }

    private Bson createUpdateQuery(final PlayerData data) {
        final List<Bson> update = new ArrayList<>();
        if (data.getJoinVipPoints() != data.getVipPoins()) {
            update.add(Updates.set(VIP_POINTS, data.getVipPoins()));
        }
        if (data.getJoinLcoins() != data.getLcoins()) {
            update.add(Updates.set(LCOINS, data.getLcoins()));
        }
        if (update.isEmpty()) {
            return null;
        } 
        return Updates.combine(update);
    }

    @Override
    public void load(final UUID uuid, final String playerName) {
        final Document document = collection.find(Filters.eq("_id", playerName)).limit(1).first();
        if (document == null) {
            return;
        }
        final Double lcoins = document.getDouble(LCOINS);
        final Double vipPoints = document.getDouble(VIP_POINTS);
        final double parsedLcoins = (lcoins == null) ? 0 : lcoins;
        final double parsedvipPoints = (vipPoints == null) ? 0 : vipPoints;

        cache.put(uuid, new PlayerData(playerName, parsedLcoins, parsedvipPoints));
    }
    
    @Override
    public void create(Player player, PlayerData data) {
        cache.put(player.getUniqueId(), data);
    }

    @Override
    public void close() {
        if (cache.isEmpty()) {
            client.close();
            return;
        }
        final Set<Entry<UUID, PlayerData>> entries = cache.entrySet();
        final List<Document> toInsert = new ArrayList<>();

        for (final Entry<UUID, PlayerData> entry : entries) {
            if (entry.getValue() instanceof PlayerData.New) {
                final Document document = new Document();
                document.put("_id", entry.getValue().getPlayerName());
                document.put(LCOINS, entry.getValue().getLcoins());
                document.put(VIP_POINTS, entry.getValue().getVipPoins());
                toInsert.add(document);
                return;
            }
            final Bson query = createUpdateQuery(entry.getValue());
            if (query != null) {
                collection.updateOne(Filters.eq("_id", entry.getValue().getPlayerName()), query);
            }
        }
        if (!toInsert.isEmpty()) {
            collection.insertMany(toInsert);
        }
        client.close();
    }
}