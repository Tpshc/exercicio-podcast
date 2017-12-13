package br.ufpe.cin.if710.podcast.domain;

import android.content.ContentValues;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by tulio on 12/12/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemFeedTest {
    @Mock
    ContentValues values;
    ItemFeed feed1;
    @Test
    public void getFullContentValues() throws Exception {

        String title = "title_value";
        String link = "link_value";
        String pubDate = "pubDate_value";
        String description = "description_value";
        String downloadLink = "downloadLink_value";
        String episodeURI = "episodeUri_value";


        when(values.get(PodcastDBHelper.EPISODE_TITLE)).thenReturn(title);
        when(values.get(PodcastDBHelper.EPISODE_LINK)).thenReturn(link);
        when(values.get(PodcastDBHelper.EPISODE_DATE)).thenReturn(pubDate);
        when(values.get(PodcastDBHelper.EPISODE_DESC)).thenReturn(description);
        when(values.get(PodcastDBHelper.EPISODE_DOWNLOAD_LINK)).thenReturn(downloadLink);
        when(values.get(PodcastDBHelper.EPISODE_FILE_URI)).thenReturn(episodeURI);

        feed1 =new ItemFeed(title,link,pubDate,description,downloadLink,episodeURI);

        feed1.getFullContentValues(values);

        assertEquals(feed1.getTitle(),values.get(PodcastDBHelper.EPISODE_TITLE));
        assertEquals(feed1.getLink(),values.get(PodcastDBHelper.EPISODE_LINK));
        assertEquals(feed1.getPubDate(),values.get(PodcastDBHelper.EPISODE_DATE));
        assertEquals(feed1.getDescription(),values.get(PodcastDBHelper.EPISODE_DESC));
        assertEquals(feed1.getDownloadLink(),values.get(PodcastDBHelper.EPISODE_DOWNLOAD_LINK));
        assertEquals(feed1.getEpisode_uri(),values.get(PodcastDBHelper.EPISODE_FILE_URI));

    }

    @Test
    public void isIn() throws Exception {
        ItemFeed feed1 = new ItemFeed("title_value","link_value","pubDate_value","description_value","downloadLink_value","episodeUri_value");
        ItemFeed feed2 = new ItemFeed("title_value","link_value","pubDate_value","description_value","downloadLink_value","episodeUri_value_DIFFERENT");
        ItemFeed feed3 = new ItemFeed("title_value_DIFFERENT","link_value","pubDate_value","description_value","downloadLink_value","episodeUri_value");

        List<ItemFeed> list = new ArrayList<ItemFeed>();
        list.add(feed2);
        list.add(feed2);

        assertEquals(feed1.isIn(list),true);
        assertEquals(feed2.isIn(list),true);
        assertEquals(feed3.isIn(list),false);

    }

    @Test
    public void equals() throws Exception {
        ItemFeed feed1 = new ItemFeed("title_value","link_value","pubDate_value","description_value","downloadLink_value","episodeUri_value");
        ItemFeed feed2 = new ItemFeed("title_value","link_value","pubDate_value","description_value","downloadLink_value","episodeUri_value_DIFFERENT");
        ItemFeed feed3 = new ItemFeed("title_value_DIFFERENT","link_value","pubDate_value","description_value","downloadLink_value","episodeUri_value");

        assertEquals(feed1.equals(feed2),true);
        assertEquals(feed1.equals(feed3),false);
        assertEquals(feed2.equals(feed3),false);
    }




}