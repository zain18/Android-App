package com.example.eversmileproject;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Appointments extends AppCompatActivity {

    private WebView wb;

    //using webview
    //to access appointments, you need to access timetap.com
    //username and password were given to the project sponsors
    public class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        //btn btn-link (javascript class)
        //navbar-inner (javascript class)
        //hide log in button
        //hide sign up button
        @Override
        public void onPageFinished(WebView view, String url)
        {
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('poweredby')[0].style.display='none'; })()");
            view.loadUrl("javascript:(function() { " +
                "document.getElementsByClassName('btn btn-small')[0].style.display='none'; })()");
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('btn btn-link')[0].style.display='none'; })()");
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('navbar-inner')[0].style.display='none'; })()");
            // document.getElementsByClassName('poweredby')
        }
        //hide log in button
        //hide sign up button
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('poweredby')[0].style.display='none'; })()");
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('btn btn-small')[0].style.display='none'; })()");
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('btn btn-link')[0].style.display='none'; })()");
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('navbar-inner')[0].style.display='none'; })()");
        }
        //hide log in button
        //hide sign up button
        @Override
        public void onLoadResource(WebView view, String url){
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('poweredby')[0].style.display='none'; })()");
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('btn btn-small')[0].style.display='none'; })()");
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('btn btn-link')[0].style.display='none'; })()");
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('navbar-inner')[0].style.display='none'; })()");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        wb=findViewById(R.id.web_view);
        WebSettings webSettings=wb.getSettings();
        webSettings.setJavaScriptEnabled(true);// allow javascript to hide unnecessary elements from the site
        wb.getSettings().setBuiltInZoomControls(true);
        wb.getSettings().setBuiltInZoomControls(true);
        wb.setWebViewClient(new WebClient());
        wb.loadUrl("https://bookeversmile.timetap.com/");//timetap website to manage all appointments
    }

}

