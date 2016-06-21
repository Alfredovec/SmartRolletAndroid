package sergii_rud.smartrollet;

import android.os.AsyncTask;

import java.lang.reflect.Type;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button buttonFullOpen;
    Button buttonFullClose;

    Rollet rollet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();

        try {
            rollet = new GetRolletTask().execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageView = (ImageView) findViewById(R.id.imageView);
        if (rollet.getRolletState() == 0) {
            imageView.setImageResource(R.drawable.closedrollet);
        } else {
            imageView.setImageResource(R.drawable.openedrollet);
        }
    }

    public void addListenerOnButton() {

        imageView = (ImageView) findViewById(R.id.imageView);

        buttonFullClose = (Button) findViewById(R.id.FullClose);
        buttonFullOpen = (Button) findViewById(R.id.FullOpen);

        buttonFullClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new UpdateRolletTask().execute(169);
                imageView.setImageResource(R.drawable.closedrollet);
            }
        });

        buttonFullOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new UpdateRolletTask().execute(-169);
                imageView.setImageResource(R.drawable.openedrollet);
            }
        });

    }

    class GetRolletTask extends AsyncTask<Void, Void, Rollet> {

        private Exception exception;

        protected Rollet doInBackground(Void... params) {
            String url = "http://10.23.20.42:4747/rollet?email=rud.sergey.v@gmail.com";

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = null;
            try {
                httpResponse = httpclient.execute(new HttpGet(url));
            } catch (Exception e) {
                e.printStackTrace();
            }

            InputStream inputStream = null;
            String result = "";

            // receive response as inputStream
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Rollet>>() { }.getType();
            ArrayList<Rollet> rollets = null;
            String jsonString = null;
            try {
                jsonString = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            rollets = gson.fromJson(jsonString, type);

            return rollets.get(0);
        }

        protected void onPostExecute(Rollet rollet)
        {
        }
    }

    class UpdateRolletTask extends AsyncTask<Integer, Void, Integer> {

        private Exception exception;

        protected Integer doInBackground(Integer... params) {
            String url = "http://10.23.20.42:4747/rollet/1?change=" + params[0];

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = null;
            HttpPut put = new HttpPut(url);
            try {
                httpResponse = httpclient.execute(put);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return 0;
        }

        protected void onPostExecute(Rollet rollet)
        {
        }
    }
}
