package com.intelematics.interview.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.intelematics.interview.SongListActivity;
import com.intelematics.interview.db.DBManager;
import com.intelematics.interview.db.SongManager;
import com.intelematics.interview.models.Song;
import com.intelematics.interview.net.ConnectionManager;
import com.intelematics.interview.util.JsonParser;

import java.util.ArrayList;

/**
 *
 */
public class DownloadSongListTask extends AsyncTask<Void, Void, Void> {
	private DBManager dbManager;
	private SongListActivity activity;
	private ArrayList<Song> songList;
	
	private ConnectionManager connectionManager;
	
	public DownloadSongListTask(SongListActivity activity, DBManager dbManager) {
		this.activity = activity;
		this.dbManager = dbManager;
		songList = new ArrayList<Song>();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		JsonParser parser = new JsonParser();

        // Rock version of the app
        connectionManager = new ConnectionManager(activity, "https://itunes.apple.com/search?term=rock&amp;media=music&amp;entity=song&amp;limit=50");

        // Pop version of the app
        //connectionManager = new ConnectionManager(activity, https://itunes.apple.com/search?term=popk&amp;media=music&amp;entity=song&amp;limit=50);

        // Classic version of the app
        //connectionManager = new ConnectionManager(activity, https://itunes.apple.com/search?term=classick&amp;media=music&amp;entity=song&amp;limit=50);

		Log.d("abc", "doInBackground: " + "1");
		songList = parser.parseSongList(connectionManager.requestJson());
		Log.d("abc", "doInBackground: " + "6");
		connectionManager.closeConnection();
		
		SongManager songManager = new SongManager(activity, dbManager);
		songManager.saveSongsList(songList);
		
		return null;
	}

    protected void onPostExecute(Void result) {
    	activity.updateSongList(songList);
    }



}
