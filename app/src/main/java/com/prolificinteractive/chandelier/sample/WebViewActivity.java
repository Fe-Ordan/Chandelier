package com.prolificinteractive.chandelier.sample;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.prolificinteractive.chandelier.widget.Ornament;
import com.prolificinteractive.chandelier.widget.ChandelierLayout;
import java.util.Arrays;

public class WebViewActivity extends AppCompatActivity {

  public static final String GITHUB_URL = "https://www.github.com/";
  public static final String GITHUB_NOTIFICATIONS_URL = GITHUB_URL + "notifications";
  public static final String GITHUB_PULLS_URL = GITHUB_URL + "pulls";
  public static final String GITHUB_SEARCH_URL = GITHUB_URL + "search";

  private ChandelierLayout chandelierLayout;
  private WebView webView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_web_view);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

    webView = (WebView) findViewById(R.id.web_view);
    webView.setWebViewClient(new WebViewClient());
    webView.getSettings().setJavaScriptEnabled(true);
    webView.setWebChromeClient(new WebChromeClient() {
      @Override public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == 100) {
          progressBar.setVisibility(View.INVISIBLE);
        } else {
          progressBar.setVisibility(View.VISIBLE);
        }

        progressBar.setProgress(newProgress);
      }
    });
    webView.loadUrl(GITHUB_URL);

    chandelierLayout = (ChandelierLayout) findViewById(R.id.chandelier_layout);
    chandelierLayout.setOnActionSelectedListener(new ChandelierLayout.OnActionListener() {
      @Override public void onActionSelected(int index, Ornament action) {
        action.execute();
      }
    });
    chandelierLayout.populateActionItems(Arrays.asList(
        new GitHubAction.Builder()
            .setDrawableResId(R.drawable.ic_notifications)
            .setUrl(GITHUB_NOTIFICATIONS_URL)
            .setWebView(webView)
            .build(),
        new GitHubAction.Builder()
            .setDrawableResId(R.drawable.ic_github)
            .setUrl(GITHUB_URL)
            .setWebView(webView)
            .build(),
        new GitHubAction.Builder()
            .setDrawableResId(R.drawable.ic_pull_request)
            .setUrl(GITHUB_PULLS_URL)
            .setWebView(webView)
            .build()
    ));

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        webView.loadUrl(GITHUB_SEARCH_URL);
      }
    });
  }

  static class GitHubAction extends Ornament {
    private String url;
    private WebView webView;

    public GitHubAction(@DrawableRes int drawableResId) {
      super(drawableResId);
    }

    @Override public void execute() {
      if (!TextUtils.isEmpty(url)) {
        webView.loadUrl(url);
      }
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public void setWebView(WebView webView) {
      this.webView = webView;
    }

    static class Builder {
      String url = "";
      int drawableResId;
      WebView webView;

      public Builder setDrawableResId(@DrawableRes int resId) {
        drawableResId = resId;
        return this;
      }

      public Builder setUrl(@NonNull String url) {
        this.url = url;
        return this;
      }

      public Builder setWebView(@NonNull WebView webView) {
        this.webView = webView;
        return this;
      }

      public GitHubAction build() {
        if (url == null || webView == null) {
          throw new IllegalArgumentException("URL and WebView must be set");
        }
        GitHubAction action = new GitHubAction(drawableResId);
        action.setUrl(url);
        action.setWebView(webView);
        return action;
      }
    }
  }
}
