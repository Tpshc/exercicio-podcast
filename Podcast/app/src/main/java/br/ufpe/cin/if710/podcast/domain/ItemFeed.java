package br.ufpe.cin.if710.podcast.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;

@Entity(tableName="episodes")
public class ItemFeed implements Serializable{
    @PrimaryKey(autoGenerate = true)
    private final int _id;
    private final String title;
    private final String link;
    private final String pubDate;
    private final String description;
    private final String downloadLink;
    @ColumnInfo(name = "downloadUri")
    private String episode_uri = "";

    @Ignore
    public ItemFeed(String title, String link, String pubDate, String description, String downloadLink, String episode_uri) {
        this._id =  0;//evitando exception por causa do room
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.downloadLink = downloadLink;
        this.episode_uri = episode_uri;
    }

    public ItemFeed(int id, String title, String link, String pubDate, String description, String downloadLink, String episode_uri) {
        this._id = id;
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.downloadLink = downloadLink;
        this.episode_uri = episode_uri;
    }

    @Ignore
    public ItemFeed(Cursor cursor){
        this._id =  cursor.getColumnIndexOrThrow(PodcastDBHelper._ID);//evitando exception por causa do room
        int title_index = cursor.getColumnIndexOrThrow(PodcastDBHelper.EPISODE_TITLE);
        int date_index = cursor.getColumnIndexOrThrow(PodcastDBHelper.EPISODE_DATE);
        int desc_index = cursor.getColumnIndexOrThrow(PodcastDBHelper.EPISODE_DESC);
        int downloadlink_index = cursor.getColumnIndexOrThrow(PodcastDBHelper.EPISODE_DOWNLOAD_LINK);
        int link_index = cursor.getColumnIndexOrThrow(PodcastDBHelper.EPISODE_LINK);
        int episode_uri_index = cursor.getColumnIndexOrThrow(PodcastDBHelper.EPISODE_FILE_URI);


        this.title = cursor.getString(title_index);
        this.link = cursor.getString(link_index);
        this.pubDate = cursor.getString(date_index);
        this.description = cursor.getString(desc_index);
        this.downloadLink = cursor.getString(downloadlink_index);
        this.episode_uri = cursor.getString(episode_uri_index);
    }

    public void getFullContentValues(ContentValues values){
        values.put(PodcastDBHelper.EPISODE_TITLE, getTitle());
        values.put(PodcastDBHelper.EPISODE_DATE, getPubDate());
        values.put(PodcastDBHelper.EPISODE_DESC, getDescription());
        values.put(PodcastDBHelper.EPISODE_DOWNLOAD_LINK, getDownloadLink());
        values.put(PodcastDBHelper.EPISODE_LINK, getLink());
        values.put(PodcastDBHelper.EPISODE_FILE_URI, getEpisode_uri());

    }


    public boolean isIn(List<ItemFeed> list){
        for (ItemFeed item : list) {
            if(this.equals(item)){
                return true;
            }
        }
        return false;
    }

    public boolean equals(ItemFeed item){
        return (getTitle().equals(item.getTitle())) &&
                getLink().equals(item.getLink()) &&
                getDescription().equals(item.getDescription()) &&
                getPubDate().equals(item.getPubDate()) &&
                getDownloadLink().equals(item.getDownloadLink());

    }

    public int get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public String getEpisode_uri() {
        return episode_uri;
    }

    public void setEpisode_uri(String uri){
        episode_uri = uri;
    }
    @Override
    public String toString() {
        return title;
    }
}