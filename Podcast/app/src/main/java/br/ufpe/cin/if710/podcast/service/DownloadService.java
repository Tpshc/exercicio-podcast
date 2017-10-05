package br.ufpe.cin.if710.podcast.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;

/**
 * Created by tulio on 9/30/17.
 */
public class DownloadService extends IntentService {

    public static final String DOWNLOAD_COMPLETE = "br.ufpe.cin.if710.services.action.DOWNLOAD_COMPLETE";
    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onHandleIntent(Intent i) {
        try {
            ItemFeed item = (ItemFeed) i.getSerializableExtra("selected-item");
            String position = i.getStringExtra("selected-item-position");

            //checar se tem permissao... Android 6.0+
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            root.mkdirs();
            String filename = item.getDownloadLink().substring(item.getDownloadLink().lastIndexOf('/') + 1);
            File output = new File(root, filename);
            if (output.exists()) {
                output.delete();
            }
            URL url = new URL(item.getDownloadLink());
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            FileOutputStream fos = new FileOutputStream(output.getPath());
            BufferedOutputStream out = new BufferedOutputStream(fos);
            try {
                InputStream in = c.getInputStream();
                byte[] buffer = new byte[8192];
                int len = 0;
                while ((len = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, len);
                }
                out.flush();
            }
            finally {
                fos.getFD().sync();
                out.close();
                c.disconnect();
            }

            updateDBWithFileURI(output.toURI().toString(),item);

            Intent downloadFinished = new Intent(DOWNLOAD_COMPLETE);
            downloadFinished.putExtra("selected-item-position",position);
            item.setEpisode_uri(output.toURI().toString());
            LocalBroadcastManager.getInstance(this).sendBroadcast(downloadFinished);



        } catch (IOException e2) {
            Log.e(getClass().getName(), "Exception durante download", e2);
        }
    }
    private int updateDBWithFileURI(String fileURI, ItemFeed item){
        ContentResolver resolver = getContentResolver();
        ContentValues values = new ContentValues();

        values.put(PodcastDBHelper.EPISODE_FILE_URI, fileURI);

        String selection = PodcastDBHelper.EPISODE_DESC + " =? AND "+
                PodcastDBHelper.EPISODE_DATE  + " =? AND "+
                PodcastDBHelper.EPISODE_TITLE  + " =? ";
        String[] selectionArgs = new String[]{item.getDescription(), item.getPubDate(), item.getTitle()};
        return resolver.update(PodcastProviderContract.EPISODE_LIST_URI,
                values,
                selection,
                selectionArgs);

    }

}
