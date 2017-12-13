package br.ufpe.cin.if710.podcast.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProvider;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.db.room.AppDatabase;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.domain.XmlFeedParser;
import br.ufpe.cin.if710.podcast.service.DownloadService;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

public class MainActivity extends Activity {

    //ao fazer envio da resolucao, use este link no seu codigo!
    private final String RSS_FEED = "http://leopoldomt.com/if710/fronteirasdaciencia.xml";
    //TODO teste com outros links de podcast
    private final boolean isUsingRoom = false;
    private ListView items;
    private PodcastProvider podcastProvider;
    private AppDatabase roomDB;
    private View footerView;
    private List<ItemFeed> totalItemFeed;
    private boolean loadingMore = false;
    private int itemsToShow = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        footerView = ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_main, null, false);

        items = (ListView) findViewById(R.id.items);
        podcastProvider = new PodcastProvider();
        roomDB = AppDatabase.getAppDatabase(this.getApplicationContext());//room
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new DownloadXmlTask().execute(RSS_FEED);IntentFilter f=new IntentFilter(DownloadService.DOWNLOAD_COMPLETE);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onDownloadCompleteEvent, f);



        items.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;

                if((lastInScreen == totalItemCount) && !(loadingMore)){
                    Thread thread =  new Thread(null, loadMoreListItems);
                    thread.start();
                }

            }

        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        XmlFeedAdapter adapter = (XmlFeedAdapter) items.getAdapter();
        adapter.clear();
    }



    private class DownloadXmlTask extends AsyncTask<String, Void, List<ItemFeed>> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "iniciando...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<ItemFeed> doInBackground(String... params) {
            List<ItemFeed> itemList = new ArrayList<>();
            List<ItemFeed> dbList = getItemListFromDataBase();
            try {
                itemList = XmlFeedParser.parse(getRssFeed(params[0]));
            } catch (IOException|XmlPullParserException e) {
                e.printStackTrace();
            }
            mergeListsAndInsertNewItensIntoDB(dbList,itemList,podcastProvider);
            return dbList;
        }

        private List<ItemFeed> getItemListFromDataBase(){
            List<ItemFeed> list;
            if(isUsingRoom){

                list = getItemListFromRoomDB();

            }else{

                Cursor cursor = podcastProvider.query(PodcastProviderContract.EPISODE_LIST_URI,PodcastDBHelper.columns, null,null,null);
                list = new ArrayList<ItemFeed>();
                try {
                    while (cursor.moveToNext()) {
                        list.add(new ItemFeed(cursor));
                    }
                } finally {
                    cursor.close();
                }
            }
            return list;
        }

        private List<ItemFeed> getItemListFromRoomDB(){
            return roomDB.itemFeedDAO().getAll();
        }

        private void insertItensIntoRoomDB(List<ItemFeed> items){
            long[] x = roomDB.itemFeedDAO().insertAll(items);
        }

        private void mergeListsAndInsertNewItensIntoDB(List<ItemFeed> dbList,List<ItemFeed> downloadList, PodcastProvider provider){
            List<ItemFeed> itemsToBeInserted = new ArrayList<ItemFeed>();
            for(ItemFeed item : downloadList){
                if(item.isIn(dbList)) continue;

                dbList.add(item);

                itemsToBeInserted.add(item);
            }

            if(isUsingRoom){
                insertItensIntoRoomDB(itemsToBeInserted);
            }else{
                if(itemsToBeInserted.size() > 0){

                    ContentValues[] contentValues = new ContentValues[itemsToBeInserted.size()];
                    for (int i = 0; i < itemsToBeInserted.size(); i++){
                        ContentValues contentValueTemp = new ContentValues();
                        itemsToBeInserted.get(i).getFullContentValues(contentValueTemp);
                        contentValues[i] = contentValueTemp;
                    }
                    provider.bulkInsert(PodcastProviderContract.EPISODE_LIST_URI,contentValues);

                }
            }
        }

        @Override
        protected void onPostExecute(List<ItemFeed> feed) {
            Toast.makeText(getApplicationContext(), "terminando...", Toast.LENGTH_SHORT).show();
            totalItemFeed = feed;
            items.addFooterView(footerView);

            loadingMore = false;
            if(totalItemFeed!= null){
                enableItems(getAdapter());
            }

        }
    }



    //TODO Opcional - pesquise outros meios de obter arquivos da internet
    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onDownloadCompleteEvent);
    }


    private BroadcastReceiver onDownloadCompleteEvent=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent i) {

            int position = Integer.parseInt(i.getStringExtra("selected-item-position"));

            Button action = items.getChildAt(position).findViewById(R.id.item_action);
            Toast.makeText(getApplicationContext(), "Download complete", Toast.LENGTH_SHORT).show();
            action.setEnabled(true);
            action.setText(getString(R.string.action_listen));
        }
    };


    private void enableItems(XmlFeedAdapter adapter){
        items.addFooterView(footerView);


        //atualizar o list view
        items.setAdapter(adapter);
        items.setTextFilterEnabled(true);
        //*
        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                XmlFeedAdapter adapter = (XmlFeedAdapter) parent.getAdapter();
                ItemFeed item = adapter.getItem(position);
                String msg = item.getTitle() + " " + item.getLink();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(),EpisodeDetailActivity.class);
                intent.putExtra("item-selected",item );
                startActivity(intent);
            }
        });
    }

    private XmlFeedAdapter getAdapter(){
        if(totalItemFeed == null){
            return null;
        }
        else if(totalItemFeed.size() > itemsToShow) {
            return new XmlFeedAdapter(getApplicationContext(), R.layout.itemlista, totalItemFeed.subList(0, itemsToShow));
        }
        else {
            return new XmlFeedAdapter(getApplicationContext(), R.layout.itemlista, totalItemFeed.subList(0, totalItemFeed.size()));
        }
    }

    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            if(totalItemFeed == null) return;
            XmlFeedAdapter adapter = getAdapter();
            adapter.notifyDataSetChanged();
            loadingMore = false;
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            enableItems(adapter);
        }
    };

    private Runnable loadMoreListItems = new Runnable() {
        @Override
        public void run() {
            loadingMore = true;
            itemsToShow +=10;
            runOnUiThread(returnRes);
        }

    };


}
