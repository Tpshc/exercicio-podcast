package br.ufpe.cin.if710.podcast.db.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import br.ufpe.cin.if710.podcast.domain.ItemFeed;

/**
 * Created by Arthur on 12/12/2017.
 */
@Dao
public interface ItemFeedDAO {
    @Query("SELECT * FROM episodes")
    List<ItemFeed> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertAll(List<ItemFeed> itens);
}
