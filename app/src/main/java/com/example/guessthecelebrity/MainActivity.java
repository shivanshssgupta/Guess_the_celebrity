package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    String content=null;
    String[] imageUrls= new String[11];
    String[] names= new String[11];
    int i;
    int correctTag;
    int rand;
    Button op1;
    Button op2;
    Button op3;
    Button op4;
    public void setAll(){
        ImageDownload task= new ImageDownload();
        rand=(int)(Math.random()*11);
        correctTag=(int)(Math.random()*4);
        setOptions(rand);
        Bitmap bitmap;
        try{
            bitmap=task.execute(imageUrls[rand]).get();
            ImageView imageView=findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void setOptions(int sol)
    {
        int no1,no2,no3;
        no1=(int)(Math.random()*11);
        while (no1==sol)
        {
            no1=(int)(Math.random()*11);
        }
        no2=(int)(Math.random()*11);
        while(no2==sol || no2==no1)
        {
            no2=(int)(Math.random()*11);
        }
        no3=(int)(Math.random()*11);
        while(no3==sol || no3==no1 || no3==no2)
        {
            no3=(int)(Math.random()*11);
        }
        switch (correctTag)
        {
            case 0: op1.setText(names[sol]);
                op2.setText(names[no1]);
                op3.setText(names[no2]);
                op4.setText(names[no3]);
                break;
            case 1: op1.setText(names[no1]);
                op2.setText(names[sol]);
                op3.setText(names[no2]);
                op4.setText(names[no3]);
                break;
            case 2: op1.setText(names[no1]);
                op2.setText(names[no2]);
                op3.setText(names[sol]);
                op4.setText(names[no3]);
                break;
            case 3: op1.setText(names[no1]);
                op2.setText(names[no2]);
                op3.setText(names[no3]);
                op4.setText(names[sol]);
                break;
        }
    }
    public void answer(View view)
    {
        Button chosen=(Button)view;
        if(String.valueOf(chosen.getTag()).equals(Integer.toString(correctTag)))
        {
            Toast.makeText(this, "You are correct!", Toast.LENGTH_SHORT).show();
            setAll();
        }
        else
        {
            Toast.makeText(this, "Wrong! Right answer is "+names[rand], Toast.LENGTH_SHORT).show();
            setAll();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadTask task= new DownloadTask();
        op1=findViewById(R.id.op1);
        op2=findViewById(R.id.op2);
        op3=findViewById(R.id.op3);
        op4=findViewById(R.id.op4);
        try {
            content = task.execute("http://www.posh24.se/kandisar").get();
            Pattern p1= Pattern.compile("<img src=\"(.*?)\"");
            Matcher m1= p1.matcher(content);
            i=0;
            while (m1.find() && i<11)
            {
                //Log.i("urls",m1.group(1));
                imageUrls[i]=m1.group(1);
                i++;
            }
            i=0;
            Pattern p2= Pattern.compile("alt=\"(.*?)\"/>");
            Matcher m2= p2.matcher(content);
            while (m2.find())
            {
                //Log.i("names",m2.group(1));
                names[i]=m2.group(1);
                i++;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        setAll();
    }
    public class DownloadTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection;
            String result="";
            int data;
            try {
                url= new URL(urls[0]);
                urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in= urlConnection.getInputStream();
                InputStreamReader reader= new InputStreamReader(in);
                data=reader.read();
                while(data!=-1)
                {
                    char charac=(char) data;
                    result+=charac;
                    data=reader.read();

                }
                return result;

            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }
    public class ImageDownload extends  AsyncTask<String, Void, Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection;
            String result = "";
            int data;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
