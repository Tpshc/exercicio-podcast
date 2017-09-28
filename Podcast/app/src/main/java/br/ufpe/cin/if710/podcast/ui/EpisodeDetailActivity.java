package br.ufpe.cin.if710.podcast.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;

public class EpisodeDetailActivity extends Activity {

    ItemFeed itemFeed;
    TextView item_title;
    TextView item_description;
    TextView item_link;
    TextView item_pubdate;
    TextView item_downloadlink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);

        Intent intent = getIntent();
        itemFeed = (ItemFeed) intent.getSerializableExtra("item-selected");

        item_title = findViewById(R.id.item_title);
        item_pubdate = findViewById(R.id.item_pubdate);
        item_downloadlink = findViewById(R.id.item_downloadlink);
        item_link = findViewById(R.id.item_link);
        item_description = findViewById(R.id.item_description);

        item_title.setText(itemFeed.getTitle());
        item_pubdate.setText(itemFeed.getPubDate());
        item_downloadlink.setText(itemFeed.getDownloadLink());
        item_link.setText(itemFeed.getLink());
        item_description.setText(itemFeed.getDescription());

    }
}
