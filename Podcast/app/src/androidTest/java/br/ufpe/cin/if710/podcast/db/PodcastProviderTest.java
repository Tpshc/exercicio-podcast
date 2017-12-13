package br.ufpe.cin.if710.podcast.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.ufpe.cin.if710.podcast.domain.ItemFeed;

import static org.junit.Assert.assertNotEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PodcastProviderTest extends ProviderTestCase2<PodcastProvider>{

    ContentResolver contentResolver;
    Context context;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        setContext(InstrumentationRegistry.getTargetContext());
        context = InstrumentationRegistry.getTargetContext();
        contentResolver = context.getContentResolver();

    }

    public PodcastProviderTest() {
        super(PodcastProvider.class, PodcastProvider.class.getName());
    }


    @Test
    public void insert() throws  Exception {
        ContentValues values = new ContentValues();

        values.put(PodcastProviderContract.DATE, "date");
        values.put(PodcastProviderContract.DESCRIPTION, "desc");
        values.put(PodcastProviderContract.DOWNLOAD_LINK, "down_link");
        values.put(PodcastProviderContract.EPISODE_URI, "uri");
        values.put(PodcastProviderContract.EPISODE_LINK, "ep_link");
        values.put(PodcastProviderContract.TITLE, "tit");

        Uri uri  = contentResolver.insert(PodcastProviderContract.EPISODE_LIST_URI,values);
        assertNotNull( uri );
    }

    @Test
    public void delete() throws  Exception {
        ContentValues values = new ContentValues();

        String[] selections = {PodcastProviderContract.DATE,PodcastProviderContract.DESCRIPTION,PodcastProviderContract.DOWNLOAD_LINK,PodcastProviderContract.EPISODE_URI,PodcastProviderContract.EPISODE_LINK,PodcastProviderContract.TITLE};
        String[] args = {"date","description","download_link","uri","episode_link","title"};

        for(int i = 0; i < selections.length; i++){
            values.put(selections[i],args[i]);

        }
        String selection = "";
        for(String s : selections){
            selection+= s + " =? AND ";
        }
        selection = selection.substring(0,selection.lastIndexOf("AND"));

        Uri uri  = contentResolver.insert(PodcastProviderContract.EPISODE_LIST_URI,values);
        assertNotNull( uri );
        int res = contentResolver.delete(PodcastProviderContract.EPISODE_LIST_URI,selection,args);
        assertNotEquals(0,res);


    }

    @Test
    public void query() throws  Exception {
        ContentValues values = new ContentValues();
        ItemFeed item = new ItemFeed("a","b","c","d","e","f");
        item.getFullContentValues(values);

        Uri uri  = contentResolver.insert(PodcastProviderContract.EPISODE_LIST_URI,values);

        String[] projections = {PodcastProviderContract.DATE,
                PodcastProviderContract.DESCRIPTION,
                PodcastProviderContract.DOWNLOAD_LINK,
                PodcastProviderContract.EPISODE_URI,
                PodcastProviderContract.EPISODE_LINK,
                PodcastProviderContract.TITLE};

        String selection = PodcastProviderContract.DESCRIPTION + " =?";
        String[] args = {item.getDescription()};

        Cursor c = contentResolver.query(PodcastProviderContract.EPISODE_LIST_URI,projections,selection,args,"");
        c.moveToFirst();
        ItemFeed item2 = new ItemFeed(c);

        assertTrue( item.equals(item2) );
    }

    @Test
    public void update() throws Exception {
        ContentValues values = new ContentValues();
        values.put( PodcastProviderContract.TITLE, "title_test" );
        values.put( PodcastProviderContract.EPISODE_LINK, "link" );
        values.put( PodcastProviderContract.DATE, "date" );
        values.put( PodcastProviderContract.DESCRIPTION, "desc" );
        values.put( PodcastProviderContract.EPISODE_URI, "uri" );
        values.put( PodcastProviderContract.DOWNLOAD_LINK, "download" );
        contentResolver.insert( PodcastProviderContract.EPISODE_LIST_URI, values );

        ContentValues values2 = new ContentValues();
        values2.put( PodcastProviderContract.DOWNLOAD_LINK, "new-download" );
        String selection = PodcastProviderContract.TITLE + " =?";

        String[] args = {(String)values.get(PodcastProviderContract.TITLE)};
        int count = contentResolver.update( PodcastProviderContract.EPISODE_LIST_URI, values2, selection, args );

        assertNotEquals( count, 0);
    }
}