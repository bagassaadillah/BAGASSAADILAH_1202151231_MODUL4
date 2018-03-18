package com.example.bagas.myapplication;

/**
 * Created by bagas on 3/18/2018.
 */

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class namaMahasiswa extends AppCompatActivity {

    //deklarasi variabel
    private ListView mListView;
    private ProgressBar mProgressBar;
    private AddItemToListView mAddItemToListView;
    private Button mStartAsyncTask;
    // deklarasi array nama mahasiswa
    private String [] mUsers = {
            "Dina", "Rian", "Gina", "Osas", "Reno", "Lina", "Roi", "Shifa", "Dudel"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nama_mahasiswa);

        //Mendefinisikan listview, progressbar, button pada layout nama mahasiswa
        mListView = (ListView) findViewById(R.id.listView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mStartAsyncTask = (Button) findViewById(R.id.bttnstartAsyncTask);

        //membuat progressbar terlihat ketika aplikasi berjalan
        mListView.setVisibility(View.GONE);

        //setup adapter
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>()));

        //mulai tombol asynctask setelah diklik
        mStartAsyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //process adapter with asyncTask
                mAddItemToListView = new AddItemToListView();
                mAddItemToListView.execute();
            }
        });
    }

    private class AddItemToListView extends AsyncTask<Void, String, Void> {

        private ArrayAdapter<String> mAdapter;
        private int counter=1;
        ProgressDialog mProgressDialog = new ProgressDialog(namaMahasiswa.this);

        @Override
        public void onPreExecute() {
            //casting suggestion
            mAdapter = (ArrayAdapter<String>) mListView.getAdapter();
            //for progress dialog
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setTitle("Loading Data");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgress(0);

            //akan mengcancel asynctask pada saat mengklik tombol cancel
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel Process", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAddItemToListView.cancel(true);
                    mProgressBar.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            });
            mProgressDialog.show();
        }

        //method saat proses asynctask dijalankan
        @Override
        protected Void doInBackground(Void... params) {
            //menyimpan array pada sebuah variabel
            for (String item :  mUsers){
                publishProgress(item);

                // Sleep for the random amount of time
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isCancelled()){
                    mAddItemToListView.cancel(true);
                }
            }
            return null;
        }

        //method sesudah asynctask sudah dijalankan
        @Override
        protected void onProgressUpdate(String... values) {
            mAdapter.add(values[0]);

            Integer current_status = (int) ((counter/(float)mUsers.length)*100);
            mProgressBar.setProgress(current_status);

            //set progress only working for horizontal loading
            mProgressDialog.setProgress(current_status);

            //set message will not working when using horizontal loading
            mProgressDialog.setMessage(String.valueOf(current_status+"%"));
            counter++;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //hide progress bar
            mProgressBar.setVisibility(View.GONE);

            //remove progress dialog
            mProgressDialog.dismiss();
            mListView.setVisibility(View.VISIBLE);
        }

    }
}