package br.ufpe.cin.if710.podcast.ui.adapter;

/**
 * Created by leopoldomt on 9/19/17.
 */

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.service.DownloadService;

public class XmlFeedAdapter extends ArrayAdapter<ItemFeed> {

    int linkResource;

    public XmlFeedAdapter(Context context, int resource, List<ItemFeed> objects) {
        super(context, resource, objects);
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
                    b.setEnabled(false);
                    b.setText(R.string.action_downloading);
                    Intent downloadService = new Intent(v.getContext(),DownloadService.class);
                    downloadService.putExtra("selected-item",getItem(position));
                    downloadService.putExtra("selected-item-position",position + "");
                    getContext().startService(downloadService);
                }

            }
        });



                return convertView;
    }




/*



    private class DownloadFile extends AsyncTask<Void,Integer,Void>{
        private ItemFeed itemFeed;

        public void modifyItemFeed(ItemFeed item){
            itemFeed = item;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            holder.item_progressBar.setProgress(values[0]);
        }



        @Override
        protected Void doInBackground(Void... params) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(itemFeed.getDownloadLink());
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                String filename = itemFeed.getDownloadLink().substring(itemFeed.getDownloadLink().lastIndexOf('/') + 1);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),filename);
                output = new FileOutputStream(file);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            throw new Exception("FIM");
            return null;
        }











        /*
        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.e("Value", "onProgressUpdate - " + values[0] + "%");
        }

        @Override
        protected Void doInBackground(Void... parms){

            String file_uri;
            try {

                URL url = new URL(itemFeed.getDownloadLink());

                String path = url.getFile();
                String file_name = path.substring(path.lastIndexOf('/') + 1);
                File file = new Fil
                String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                URLConnection conexion = url.openConnection();
                conexion.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conexion.getContentLength();

                // downlod the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filess_uri);

                byte data[] = new byte[1024];

                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int)(total*100/lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

                PodcastProvider podcastProvider = new PodcastProvider();
                ContentValues values = new ContentValues();
                values.put(PodcastDBHelper.EPISODE_FILE_URI, file_uri);

                String[] selectionArgs = {itemFeed.getDownloadLink(), itemFeed.getTitle(), itemFeed.getPubDate(), itemFeed.getDescription()};
                String selection = PodcastDBHelper.EPISODE_DOWNLOAD_LINK + "=? AND " +
                        PodcastDBHelper.EPISODE_TITLE + "=? AND " +
                        PodcastDBHelper.EPISODE_DATE + "=? AND " +
                        PodcastDBHelper.EPISODE_DESC + "=?";

                podcastProvider.update(PodcastProviderContract.EPISODE_LIST_URI, values, selection, selectionArgs);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
*/
}
