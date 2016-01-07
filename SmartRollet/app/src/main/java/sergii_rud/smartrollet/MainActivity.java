package sergii_rud.smartrollet;

import android.os.AsyncTask;

import java.io.UnsupportedEncodingException;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

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
    }

    public void addListenerOnButton() {

        imageView = (ImageView) findViewById(R.id.imageView);

        buttonFullClose = (Button) findViewById(R.id.FullClose);
        buttonFullOpen = (Button) findViewById(R.id.FullOpen);

        buttonFullClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new UpdateRolletTask().execute(0);
                imageView.setImageResource(R.drawable.closedrollet);
            }
        });

        buttonFullOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new UpdateRolletTask().execute(169);
                imageView.setImageResource(R.drawable.openedrollet);
            }
        });

    }

    class GetRolletTask extends AsyncTask<Void, Void, Rollet> {

        private Exception exception;

        protected Rollet doInBackground(Void... params) {
            String url = "http://10.23.15.75:4747/api/rollet/1";

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
            Type type = new TypeToken<Rollet>() { }.getType();
            Rollet rollet = null;
            String jsonString = null;
            try {
                jsonString = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            rollet = gson.fromJson(jsonString, type);

            return rollet;
        }

        protected void onPostExecute(Rollet rollet)
        {
        }
    }

    class UpdateRolletTask extends AsyncTask<Integer, Void, Rollet> {

        private Exception exception;

        protected Rollet doInBackground(Integer... params) {
            String url = "http://10.23.15.75:4747/api/rollet/";

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = null;
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            try {
                post.setEntity(new StringEntity("Id=1&Width=100&Height=100&OpenedPart=" + params[0], "UTF8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                httpResponse = httpclient.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }

            InputStream inputStream = null;
            String result = "";

            // receive response as inputStream
            Gson gson = new Gson();
            Type type = new TypeToken<Rollet>() { }.getType();
            Rollet rollet = null;
            String jsonString = null;
            try {
                jsonString = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            rollet = gson.fromJson(jsonString, type);

            return rollet;
        }

        protected void onPostExecute(Rollet rollet)
        {
        }
    }
}
