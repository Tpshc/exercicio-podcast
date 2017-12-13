package br.ufpe.cin.if710.podcast.ui.adapter;

/**
 * Created by leopoldomt on 9/19/17.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.service.DownloadService;
import br.ufpe.cin.if710.podcast.ui.Util;

public class XmlFeedAdapter extends ArrayAdapter<ItemFeed> {

    int linkResource;
    Context activityContext;

    public XmlFeedAdapter(Context context, int resource, List<ItemFeed> objects, Context activityContext) {
        super(context, resource, objects);
        this.activityContext = activityContext;
        linkResource = resource;
    }

    /**
     * public abstract View getView (int position, View convertView, ViewGroup parent)
     * <p>
     * Added in API level 1
     * Get a View that displays the data at the specified position in the data set. You can either create a View manually or inflate it from an XML layout file. When the View is inflated, the parent View (GridView, ListView...) will apply default layout parameters unless you use inflate(int, android.view.ViewGroup, boolean) to specify a root view and to prevent attachment to the root.
     * <p>
     * Parameters
     * position	The position of the item within the adapter's data set of the item whose view we want.
     * convertView	The old view to reuse, if possible. Note: You should check that this view is non-null and of an appropriate type before using. If it is not possible to convert this view to display the correct data, this method can create a new view. Heterogeneous lists can specify their number of view types, so that this View is always of the right type (see getViewTypeCount() and getItemViewType(int)).
     * parent	The parent that this view will eventually be attached to
     * Returns
     * A View corresponding to the data at the specified position.
     */


	/*
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.itemlista, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.item_title);
		textView.setText(items.get(position).getTitle());
	    return rowView;
	}
	/**/

    //http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
    static class ViewHolder {
        TextView item_title;
        TextView item_date;
        Button item_action;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = View.inflate(getContext(), linkResource, null);
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_date = (TextView) convertView.findViewById(R.id.item_date);
            holder.item_action = (Button) convertView.findViewById(R.id.item_action);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_title.setText(getItem(position).getTitle());
        holder.item_date.setText(getItem(position).getPubDate());

        if(getItem(position).getEpisode_uri() != null && !getItem(position).getEpisode_uri().equals("")){
            holder.item_action.setText(R.string.action_listen);
        }


        holder.item_action.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v.findViewById(R.id.item_action);
                if(b.getText().toString().equals(v.getResources().getString(R.string.action_listen))){
                    // PLAY SONG
                    try {
                        MediaPlayer player = new MediaPlayer();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource(getItem(position).getEpisode_uri());
                        player.prepare();
                        player.start();
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
                else if(b.getText().toString().equals(v.getResources().getString(R.string.action_download))){
                    String networkStatus = Util.checkNetworkStatus(getContext());

                    if(networkStatus.equals(Util.MOBILEDATA)){
                        AlertDialog alertDialog = new AlertDialog.Builder(activityContext)
                                .setTitle("Alerta")
                                .setMessage("Você deseja realizar o download através da rede móvel?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("Sim", new YesOnClickListener(b,v,position))
                                .setNegativeButton("Não", null)
                                .create();
                        alertDialog.show();

                    }else if(networkStatus.equals(Util.WIFI)){
                        DownloadPodcast(b,v,position);
                    }else {
                        //no connection
                        Toast.makeText(getContext(), "No connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return convertView;
    }

    private void DownloadPodcast(Button b, View v, int position){
        b.setEnabled(false);
        b.setText(R.string.action_downloading);
        Intent downloadService = new Intent(v.getContext(),DownloadService.class);
        downloadService.putExtra("selected-item",getItem(position));
        downloadService.putExtra("selected-item-position",position + "");
        getContext().startService(downloadService);
    }

    class YesOnClickListener implements DialogInterface.OnClickListener
    {
        Button b;
        View v;
        int position;

        public YesOnClickListener(Button b, View v, int position) {
            this.b = b;
            this.v = v;
            this.position = position;
        }

        public void onClick(DialogInterface dialog, int whichButton)
        {
            DownloadPodcast(b,v,position);
        }
    };
}
