package br.ufpe.cin.if710.podcast.domain;

import android.database.Cursor;

import java.io.Serializable;

import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;

public class ItemFeed implements Serializable{
    private final String title;
    private final String link;
    private final String pubDate;
    private final String description;
    private final String downloadLink;


    public ItemFeed(String title, String link, String pubDate, String description, String downloadLink) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.downloadLink = downloadLink;
    }

    public ItemFeed(Cursor cursor){
        int title_index = cursor.getColumnIndexOrThrow(PodcastDBHelper.EPISODE_TITLE);
        int date_index = cursor.getColumnIndexOrThrow(PodcastDBHelper.EPISODE_DATE);
        int desc_index = cursor.getColumnIndexOrThrow(PodcastDBHelper.EPISODE_DESC);
        int downloadlink_index = cursor.getColumnIndexOrThrow(PodcastDBHelper.EPISODE_DOWNLOAD_LINK);
        int link_index = cursor.getColumnIndexOrThrow(PodcastDBHelper.EPISODE_LINK);


        this.title = cursor.getString(title_index);
        this.link = cursor.getString(link_index);
        this.pubDate = cursor.getString(date_index);
        this.description = cursor.getString(desc_index);
        this.downloadLink = cursor.getString(downloadlink_index);
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

    @Override
    public String toString() {
        return title;
    }
}