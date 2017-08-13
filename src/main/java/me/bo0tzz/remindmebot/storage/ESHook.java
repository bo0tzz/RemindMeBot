package me.bo0tzz.remindmebot.storage;

import me.bo0tzz.remindmebot.reminder.Reminder;
import me.bo0tzz.remindmebot.util.JSONHelper;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

/**
 * Created by boet on 8/12/17.
 */
public class ESHook {
    private final Client CLIENT;
    private static final String INDEX = "reminders";
    private static final String TYPE = "reminder";


    public ESHook() throws UnknownHostException {
        this.CLIENT = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("elasticsearch"), 9300));

    }

    public void close() {
        CLIENT.close();
    }

    /**
     * Add a reminder to the database.
     *
     * @param r A {@link Reminder} object to add to the database.
     * @return success
     */
    public boolean add(Reminder r) {
        String json = JSONHelper.JSONFromReminder(r);
        if (json == null) return false;
        IndexResponse response = CLIENT.prepareIndex(INDEX, TYPE)
                .setSource(json)
                .get();
        return response.getResult() == DocWriteResponse.Result.CREATED;
    }

    /**
     * Delete a {@link Reminder} from the database.
     *
     * @param r A reminder object with a present UUID field matching the ElasticSearch DB entry field
     * @return success
     */
    public boolean delete(Reminder r) {
        String UUID = r.getUUID();
        if (UUID == null) throw new IllegalStateException("Reminder did not have a UUID. Where did this object come from?");
        DeleteResponse response = CLIENT.prepareDelete(INDEX, TYPE, UUID).get();
        return response.getResult() == DocWriteResponse.Result.DELETED;
    }

    /**
     * Edit a {@link Reminder} in the database
     * @param r The edited reminder object to place in the database. UUID must match old object.
     * @return success
     */
    public boolean edit(Reminder r) {
        String json = JSONHelper.JSONFromReminder(r);
        UpdateResponse updateResponse = CLIENT.prepareUpdate(INDEX, TYPE, r.getUUID())
                .setDoc(json).get();
        return updateResponse.getResult() == DocWriteResponse.Result.UPDATED;
    }

    public Set<Reminder> getAll(String user) {
        return null;
    }
}
