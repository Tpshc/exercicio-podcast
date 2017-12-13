package br.ufpe.cin.if710.podcast.db.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.ufpe.cin.if710.podcast.domain.ItemFeed;

/**
 * Created by Arthur on 12/12/2017.
 */

@Database(entities = {ItemFeed.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract ItemFeedDAO itemFeedDAO();

    public static AppDatabase getAppDatabase(Context context) { //singleton pattern
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "itemfeed-database")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
