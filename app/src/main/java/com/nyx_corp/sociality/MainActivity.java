package com.nyx_corp.sociality;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.verify.domain.DomainVerificationManager;
import android.content.pm.verify.domain.DomainVerificationUserState;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.window.SplashScreen;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private WebView mywebview;
    private View splashScreen;
    private ValueCallback<Uri[]> uploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;

    private String lastLoadUrl;
    private String desktop = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // This removes the gray overlay/scrim
            getWindow().setNavigationBarContrastEnforced(false);
        }

        // Optional: Ensure the bar is actually transparent
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Get the intent that started the activity
        Intent intent = getIntent();
        Uri data = intent.getData();



        View menubtn = findViewById(R.id.menubtn);
        View reddit = findViewById(R.id.reddit);
        View facebook = findViewById(R.id.facebook);
        View viewbtn = findViewById(R.id.viewbtn);
        View reload = findViewById(R.id.reload);
        View bluesky = findViewById(R.id.bluesky);
        View instagram = findViewById(R.id.instagram);
        View linkedin = findViewById(R.id.linkedin);
        View x = findViewById(R.id.xbtn);
        View threads = findViewById(R.id.threads);
        View xkipedia = findViewById(R.id.xkipedia);
        View github = findViewById(R.id.github);


        View instructText = findViewById(R.id.instructionText);

        splashScreen = findViewById(R.id.splash_screen);

        splashScreen.setVisibility(View.INVISIBLE);


        menubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.animate()
                        .scaleX(0.9f) // Shrink to 90%
                        .scaleY(0.9f)
                        .setDuration(100)
                        .withEndAction(() -> {
                            // 2. Bounce back to original size
                            v.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setInterpolator(new android.view.animation.OvershootInterpolator())
                                    .setDuration(200)
                                    .start();
                        }).start();
                if (reddit.getVisibility() == View.GONE) {
                    // --- OPENING ANIMATION ---
                    showView(reddit);
                    showView(facebook);
                    showView(viewbtn);
                    showView(reload);
                    showView(instagram);
                    showView(linkedin);
                    showView(bluesky);
                    showView(instructText);
                    showView(x);
                    showView(github);
                    showView(xkipedia);
                    showView(threads);
                } else {
                    // --- CLOSING ANIMATION ---
                    hideView(reddit);
                    hideView(facebook);
                    hideView(viewbtn);
                    hideView(reload);
                    hideView(instagram);
                    hideView(linkedin);
                    hideView(bluesky);
                    hideView(instructText);
                    hideView(x);
                    hideView(xkipedia);
                    hideView(threads);
                    hideView(github);
                }
            }

            private void showView(View view) {
                view.setVisibility(View.VISIBLE);
                view.setAlpha(0f);
                view.setScaleX(0f);
                view.setScaleY(0f);

                view.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .setListener(null) // Clear any previous listeners
                        .start();
            }

            // Helper method to animate out and then hide
            private void hideView(final View view) {
                view.animate()
                        .alpha(0f)
                        .scaleX(0f)
                        .scaleY(0f)
                        .setDuration(300)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                view.setVisibility(View.GONE);
                            }
                        })
                        .start();
            }
        });


        viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastUrl = mywebview.getUrl();
                if (Objects.equals(desktop, mywebview.getSettings().getUserAgentString())) {
                    mywebview.getSettings().setUserAgentString("");
                    splashScreen.setAlpha(1f);
                    splashScreen.setVisibility(View.VISIBLE);
                    mywebview.loadUrl(lastUrl);
                } else {
                    mywebview.getSettings().setUserAgentString(desktop);
                    splashScreen.setAlpha(1f);
                    splashScreen.setVisibility(View.VISIBLE);
                    mywebview.loadUrl(lastUrl);
                }

                menubtn.performClick();
            }
        });

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mywebview.reload();
                animate_btn(v);
                menubtn.performClick();
                }
            });

        reddit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mywebview.getSettings().setUserAgentString("");
                mywebview.loadUrl("https://sh.reddit.com");
                animate_btn(v);
                splashScreen.setVisibility(View.VISIBLE);
                splashScreen.setAlpha(1f);

                menubtn.performClick();

                mywebview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mywebview.clearHistory();
                    }
                }, 3000);
            };
        });

        facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animate_btn(v);

                    splashScreen.setVisibility(View.VISIBLE);
                    splashScreen.setAlpha(1f);

                    mywebview.getSettings().setUserAgentString("");
                    mywebview.loadUrl("https://www.facebook.com");

                    mywebview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mywebview.clearHistory();
                        }
                    }, 3000);

                    menubtn.performClick();
                };

            });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate_btn(v);

                splashScreen.setVisibility(View.VISIBLE);
                splashScreen.setAlpha(1f);

                mywebview.getSettings().setUserAgentString("");
                mywebview.loadUrl("https://www.instagram.com/");

                mywebview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mywebview.clearHistory();
                    }
                }, 3000);

                menubtn.performClick();
            }

        });

        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate_btn(v);

                splashScreen.setVisibility(View.VISIBLE);
                splashScreen.setAlpha(1f);

                mywebview.getSettings().setUserAgentString("");
                mywebview.loadUrl("https://www.linkedin.com/");

                mywebview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mywebview.clearHistory();
                    }
                }, 3000);

                menubtn.performClick();
            }

        });

        bluesky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate_btn(v);

                splashScreen.setVisibility(View.VISIBLE);
                splashScreen.setAlpha(1f);

                mywebview.getSettings().setUserAgentString("");
                mywebview.loadUrl("https://bsky.app/");

                mywebview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mywebview.clearHistory();
                    }
                }, 3000);

                menubtn.performClick();
            }

        });

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate_btn(v);

                splashScreen.setVisibility(View.VISIBLE);
                splashScreen.setAlpha(1f);

                mywebview.getSettings().setUserAgentString("");
                mywebview.loadUrl("https://www.x.com/");

                mywebview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mywebview.clearHistory();
                    }
                }, 3000);

                menubtn.performClick();
            }

        });

        threads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate_btn(v);

                splashScreen.setVisibility(View.VISIBLE);
                splashScreen.setAlpha(1f);

                mywebview.getSettings().setUserAgentString("");
                mywebview.loadUrl("https://www.threads.com/");

                mywebview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mywebview.clearHistory();
                    }
                }, 3000);

                menubtn.performClick();
            }

        });

        xkipedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate_btn(v);

                splashScreen.setVisibility(View.VISIBLE);
                splashScreen.setAlpha(1f);

                mywebview.getSettings().setUserAgentString("");
                mywebview.loadUrl("https://xikipedia.org/");

                mywebview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mywebview.clearHistory();
                    }
                }, 3000);

                menubtn.performClick();
            }

        });

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate_btn(v);

                splashScreen.setVisibility(View.VISIBLE);
                splashScreen.setAlpha(1f);

                mywebview.getSettings().setUserAgentString("");
                mywebview.loadUrl("https://github.com/");

                mywebview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mywebview.clearHistory();
                    }
                }, 3000);

                menubtn.performClick();
            }

        });





        mywebview = findViewById(R.id.webview);
        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.getSettings().setDomStorageEnabled(true);
        mywebview.getSettings().setLoadsImagesAutomatically(true);
        mywebview.getSettings().setDatabaseEnabled(true);
        mywebview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mywebview.clearCache(false);
        mywebview.setRendererPriorityPolicy(WebView.RENDERER_PRIORITY_IMPORTANT, true);
        mywebview.getSettings().setLoadsImagesAutomatically(true);
        mywebview.getSettings().setBlockNetworkImage(false);
        mywebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mywebview.getSettings().setOffscreenPreRaster(true);

        mywebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mywebview.getSettings().setSupportMultipleWindows(true);

        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(mywebview, true);


        // Disable the scrollbars
        mywebview.setVerticalScrollBarEnabled(false);
        mywebview.setHorizontalScrollBarEnabled(false);
        mywebview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        mywebview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        mywebview.setBackgroundColor(Color.TRANSPARENT);
        mywebview.setFocusable(true);
        mywebview.setLongClickable(true);

        if (Intent.ACTION_VIEW.equals(intent.getAction()) && data != null) {
            // If opened via a link, load that link!
            mywebview.loadUrl(data.toString());
            splashScreen.setAlpha(1f);
            splashScreen.setVisibility(View.VISIBLE);
            menubtn.performClick();
        }
//        } else {
//            // Otherwise, load your default home page
//           // mywebview.loadUrl("https://www.facebook.com");
//        }

// Trigger the settings prompt after 3 seconds so the user isn't annoyed immediately
        mywebview.postDelayed(this::checkAndShowDeepLinkPrompt, 3000);



//        mywebview.setOnLongClickListener(v -> {
//            WebView.HitTestResult result = mywebview.getHitTestResult();
//            String url = result.getExtra();
//
//            // This checks for links, image links, and even data/phone/email links
//            if (url != null) {
//                // Copy to clipboard
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clip = ClipData.newPlainText("Copied URL", url);
//                clipboard.setPrimaryClip(clip);
//
//                Toast.makeText(getApplicationContext(), "Link Copied!", Toast.LENGTH_SHORT).show();
//                return true; // "true" means we handled the click, so no other menu pops up
//            }
//
//            return false; // "false" means we didn't find a link, let the system handle it
//        });


        mywebview.setOnLongClickListener(v -> {
            WebView.HitTestResult result = mywebview.getHitTestResult();
            int type = result.getType();
            String url = result.getExtra();

            if (url == null) return false;

            // 1. Identify Content Type
            String extension = MimeTypeMap.getFileExtensionFromUrl(url);
            boolean isVideo = extension != null && (
                    extension.equalsIgnoreCase("mp4") ||
                            extension.equalsIgnoreCase("webm") ||
                            extension.equalsIgnoreCase("ogg"));

            boolean isImage = (type == WebView.HitTestResult.IMAGE_TYPE ||
                    type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE);

            // 2. Build Material Alert Dialog
            // Use v.getContext() to ensure the Material theme is applied correctly
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(v.getContext());
            builder.setTitle("Link Options");

            // Optional: Add an icon to the title for extra polish
            builder.setIcon(isImage || isVideo ? android.R.drawable.ic_menu_save : android.R.drawable.ic_menu_share);

            String[] options = (isImage || isVideo) ?
                    new String[]{"Copy Link", "Download Media"} :
                    new String[]{"Copy Link"};

            builder.setItems(options, (dialog, which) -> {
                if (which == 0) {
                    copyToClipboard(url);
                } else if (which == 1) {
                    // Pass the WebView's UA. ContentDisposition and MimeType are null here,
                    // but our updated downloadMedia will handle the 'guess' logic.
                    downloadMedia(url, mywebview.getSettings().getUserAgentString(), null, null);
                }
            });

            builder.show();
            return true;
        });

        mywebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                if (mywebview.getUrl() != null && mywebview.getUrl().contains("www.linkedin.com")) {

                    mywebview.setBackgroundColor(Color.WHITE);

                };
                if (mywebview.getUrl() != null && mywebview.getUrl().contains("www.facebook.com")) {

                    mywebview.setBackgroundColor(Color.BLACK);

                };
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String currentUA = mywebview.getSettings().getUserAgentString();


                view.evaluateJavascript(
                        "(function() {" +
                                "   var css = '* { -webkit-tap-highlight-color: transparent !important; } ' + " +
                                "              'button, a { outline: none !important; }' + " +
                                "             'html { touch-action: manipulation !important; } '; " +
                                "" +
                                "   var styleId = 'global-web-style';" +
                                "   var style = document.getElementById(styleId);" +
                                "" +
                                "   /* Create the style element only if it doesn't exist */" +
                                "   if (!style) {" +
                                "       style = document.createElement('style');" +
                                "       style.id = styleId;" +
                                "       style.type = 'text/css';" +
                                "       document.head.appendChild(style);" +
                                "   }" +
                                "" +
                                "   /* Only update innerHTML if the content is different to save CPU */" +
                                "   if (style.innerHTML !== css) {" +
                                "       style.innerHTML = css;" +
                                "       console.log('Global styles injected/updated');" +
                                "   }" +
                                "})();", null);




                if (mywebview.getUrl() != null && mywebview.getUrl().contains("x.com")) {

                    view.evaluateJavascript(
                            "(function() {" +
                                    "   var css = '* { -webkit-tap-highlight-color: transparent !important; } ' +" +
                                    "             'div[data-testid=\"TopNavBar\"] {transition-duration: 200ms !important; transform: translateY(0px) !important; opacity: 1 !important;}' + " +
                                    "             'a[data-testid=\"FloatingActionButtons_Tweet_Button\"] { position: fixed !important; bottom: 100px !important; right: 30px !important;  opacity: 1 !important;}' + " +
                                    "             'div[data-testid=\"BottomBar\"] {transform: none !important ; transition-duration: 10ms !important; width: 100vw !important; position: fixed !important; bottom: -50px !important; z-index: 0 !important;  opacity: 1 !important;}' + " +
                                    "             'a[href=\"/i/premium_sign_up\"], a[href*=\"premium_sign_up\"] { display:none !important; }';" +
                                    "" +
                                    "   function inject() {" +
                                    "       var style = document.getElementById('custom-web-style');" +
                                    "       if (!style) {" +
                                    "           style = document.createElement('style');" +
                                    "           style.id = 'custom-web-style';" +
                                    "           document.head.appendChild(style);" +
                                    "       }" +
                                    "       if (style.innerHTML !== css) {" +
                                    "           style.innerHTML = css;" +
                                    "       }" +
                                    "   }" +
                                    "" +
                                    "   /* 1. Initial Injection */" +
                                    "   inject();" +
                                    "" +
                                    "   /* 2. Check if observer already exists on the window object */" +
                                    "   if (!window.myAppObserver) {" +
                                    "       window.myAppObserver = new MutationObserver(function(mutations) {" +
                                    "           /* Only re-inject if our style tag was wiped out by the SPA navigation */" +
                                    "           if (!document.getElementById('custom-web-style')) {" +
                                    "               inject();" +
                                    "           }" +
                                    "       });" +
                                    "" +
                                    "       window.myAppObserver.observe(document.documentElement, {" +
                                    "           childList: true," +
                                    "           subtree: true" +
                                    "       });" +
                                    "   }" +
                                    "})();", null);
                };

                if (mywebview.getUrl() != null && mywebview.getUrl().contains("www.facebook.com")) {
                    view.evaluateJavascript(
                            "(function() {" +
                                    "   var css = '* { -webkit-tap-highlight-color: transparent !important; } ' + " +
                                    "             'div.m[data-comp-id=\"30000\"] { position:fixed !important; bottom:0 !important; z-index:999 !important; }' + "+
                                    "             'div.m[style*=\"height:1px\"] { height:0 !important; border:.5px solid black !important; }' + " +
                                    "             'h2.m[style*=\"height:33px\"] { display:none !important; }' + " +
                                    "             'div.m[aria-label=\"Navigate to your Reels profile\"] { display:none !important; }' + "+
                                    "             'div.m[aria-label=\"Open app\"] { display:none !important; }' + "+
                                    "             'div.m[aria-label=\"Search\"] { opacity: 0 !important; }' + "+
                                    "             'div.m[aria-label=\"Search\"][data-action-id=\"32684\"] { opacity: 1 !important; }' + "+
                                    "             'div[data-mcomponent=\"MContainer\"][role=\"tablist\"] {height: 50px !important}' + " +
                                    "             'div.m.vertically-snappable[role=\"tablist\"] { ' + " +
                                    "             '    position: fixed !important; ' + " +
                                    "             '    bottom: 0 !important; ' + " +
                                    "             '    left: 0 !important; ' + " +
                                    "             '    width: 100% !important; ' + " +
                                    "             '    z-index: 9999 !important; ' + " +
                                    "             '    background: black !important; ' + " +
                                    "             '    height: 50px !important; ' + " +
                                    "             '}'; " +
                                    "" +
                                    "   function inject() {" +
                                    "       var style = document.getElementById('fb-custom-style');" +
                                    "       if (!style) {" +
                                    "           style = document.createElement('style');" +
                                    "           style.id = 'fb-custom-style';" +
                                    "           document.head.appendChild(style);" +
                                    "       }" +
                                    "       if (style.innerHTML !== css) {" +
                                    "           style.innerHTML = css;" +
                                    "       }" +
                                    "   }" +
                                    "" +
                                    "   inject();" +
                                    "" +
                                    "   /* Singleton Observer to handle Facebook SPA navigation */" +
                                    "   if (!window.fbAppObserver) {" +
                                    "       window.fbAppObserver = new MutationObserver(function(mutations) {" +
                                    "           if (!document.getElementById('fb-custom-style')) {" +
                                    "               inject();" +
                                    "           }" +
                                    "       });" +
                                    "       window.fbAppObserver.observe(document.documentElement, {" +
                                    "           childList: true," +
                                    "           subtree: true" +
                                    "       });" +
                                    "   }" +
                                    "})();", null);

                        }

                if (mywebview.getUrl() != null && mywebview.getUrl().contains("www.instagram.com")) {
                    view.evaluateJavascript(
                            "(function() {" +
                                    "   var css = '* { -webkit-tap-highlight-color: transparent !important; } ' + " +
                                    "             'div._acc8 { display:none !important; }' + " +
                                    "             'div.x1uvtmcs { position: fixed !important; bottom: -50 !important; z-index: 9999 !important; }' ; " +
                                    "" +
                                    "   function inject() {" +
                                    "       var style = document.getElementById('fb-custom-style');" +
                                    "       if (!style) {" +
                                    "           style = document.createElement('style');" +
                                    "           style.id = 'fb-custom-style';" +
                                    "           document.head.appendChild(style);" +
                                    "       }" +
                                    "       if (style.innerHTML !== css) {" +
                                    "           style.innerHTML = css;" +
                                    "       }" +
                                    "   }" +
                                    "" +
                                    "   inject();" +
                                    "" +
                                    "   /* Singleton Observer to handle Facebook SPA navigation */" +
                                    "   if (!window.fbAppObserver) {" +
                                    "       window.fbAppObserver = new MutationObserver(function(mutations) {" +
                                    "           if (!document.getElementById('fb-custom-style')) {" +
                                    "               inject();" +
                                    "           }" +
                                    "       });" +
                                    "       window.fbAppObserver.observe(document.documentElement, {" +
                                    "           childList: true," +
                                    "           subtree: true" +
                                    "       });" +
                                    "   }" +
                                    "})();", null);

                }


                if (mywebview.getUrl() != null && mywebview.getUrl().contains("threads")) {
                    view.evaluateJavascript(
                            "(function() {" +
                                    "   var css = '* { -webkit-tap-highlight-color: transparent !important; } ' + " +
                                    "             'div.x1mk1bxn { display:none !important; }' + " +
                                    "             'nav { display: fixed !important; bottom: -50px !important; z-index: 9999 !important; }' ; " +
                                    "" +
                                    "   function inject() {" +
                                    "       var style = document.getElementById('threads-custom-style');" +
                                    "       if (!style) {" +
                                    "           style = document.createElement('style');" +
                                    "           style.id = 'threads-custom-style';" +
                                    "           (document.head || document.documentElement).appendChild(style);" +
                                    "       }" +
                                    "       /* Force update the CSS content every time inject is called */" +
                                    "       if (style.textContent !== css) {" +
                                    "           style.textContent = css;" +
                                    "       }" +
                                    "   }" +
                                    "" +
                                    "   inject();" +
                                    "" +
                                    "   if (!window.threadsObserver) {" +
                                    "       window.threadsObserver = new MutationObserver(function() {" +
                                    "           /* Run inject every time to ensure our style wins over the SPA changes */" +
                                    "           inject();" +
                                    "       });" +
                                    "       window.threadsObserver.observe(document.documentElement, {" +
                                    "           childList: true," +
                                    "           subtree: true" +
                                    "       });" +
                                    "   }" +
                                    "})();", null);
                }


            }

        });

        mywebview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                // 1. Properly guess the filename using the metadata provided by the site
                String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                // 2. Add Cookies and User Agent (Crucial for LinkedIn/X/Facebook)
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("Cookie", cookies);
                request.addRequestHeader("User-Agent", userAgent);

                request.setMimeType(mimetype);
                request.setTitle(fileName); // Shows the real filename in the notification
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                // 3. Use the dynamic fileName instead of a hardcoded string
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                if (dm != null) {
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Downloading: " + fileName, Toast.LENGTH_LONG).show();
                }
            }
        });


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // 1. We tell JS to go back.
                // We check the history length before and after, or just try window.history.back()
                mywebview.evaluateJavascript(
                        "(function() {" +
                                "   if (window.history.length > 1) {" +
                                "       window.history.back();" +
                                "       return true;" +
                                "   }" +
                                "   return false;" +
                                "})();",
                        value -> {
                            // 'value' comes back as "true" or "false" (as a string)
                            if ("false".equals(value)) {
                                // JavaScript says there's nowhere to go back to
                                showExitConfirmation();
                            }
                            // If true, JS already triggered the back navigation
                        }
                );
            }

            private void showExitConfirmation() {
                new com.google.android.material.dialog.MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle("Exit App")
                        .setMessage("Are you sure you want to close the app?")
                        .setPositiveButton("Yes", (dialog, which) -> finish())
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });



        mywebview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if (newProgress > 93) {
                    splashScreen.animate()
                            .alpha(0f)
                            .setDuration(500)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    splashScreen.setVisibility(View.GONE);
                                }
                            });


                    if (mywebview.getUrl() != null && mywebview.getUrl().contains("reddit.com")) {

                        view.evaluateJavascript(
                                "(function() {" +
                                        "   var css = 'body {position: static !important; overflow: auto !important; height: auto !important; pointer-events: auto !important; }' + " +
                                        "             'div[style*=\"backdrop-filter: blur(4px)\"] { display: none !important; }' + " +
                                        "             '#blocking-modal { display: none !important; }';" +
                                        "   var shadowHostTag = 'xpromo-nsfw-blocking-container';" +
                                        "   var shadowTargetSelector = '.prompt';" +
                                        "" +
                                        "   function inject() {" +
                                        "       /* 1. Standard CSS Injection */" +
                                        "       var style = document.getElementById('custom-web-style');" +
                                        "       if (!style) {" +
                                        "           style = document.createElement('style');" +
                                        "           style.id = 'custom-web-style';" +
                                        "           (document.head || document.documentElement).appendChild(style);" +
                                        "       }" +
                                        "       if (style.innerHTML !== css) { style.innerHTML = css; }" +
                                        "" +
                                        "       /* 2. Shadow DOM Piercing */" +
                                        "       var host = document.querySelector(shadowHostTag);" +
                                        "       if (host) {" +
                                        "           /* If it has a shadowRoot, hide the internal prompt too */" +
                                        "           if (host.shadowRoot) {" +
                                        "               var target = host.shadowRoot.querySelector(shadowTargetSelector);" +
                                        "               if (target) { target.style.setProperty('display', 'none', 'important'); }" +
                                        "           }" +
                                        "       }" +
                                        "   }" +
                                        "" +
                                        "   inject();" + // Initial run
                                        "" +
                                        "   if (!window.myAppObserver) {" +
                                        "       window.myAppObserver = new MutationObserver(function() {" +
                                        "           inject();" + // Run every time the page changes
                                        "       });" +
                                        "       window.myAppObserver.observe(document.documentElement, { childList: true, subtree: true });" +
                                        "   }" +
                                        "})();", null);

//                        view.evaluateJavascript(
//                                "(function() {" +
//                                        "   var css = 'body {position: static !important; overflow: auto !important; height: auto !important; pointer-events: auto !important; }' + " +
//                                        "               'div[style*=\"backdrop-filter: blur(4px)\"] { display: none !important; }' + " +
//                                        "               '#blocking-modal { display: none !important; }' ;" +
//                                        "" +
//                                        "   function inject() {" +
//                                        "       var style = document.getElementById('custom-web-style');" +
//                                        "       if (!style) {" +
//                                        "           style = document.createElement('style');" +
//                                        "           style.id = 'custom-web-style';" +
//                                        "           document.head.appendChild(style);" +
//                                        "       }" +
//                                        "       if (style.innerHTML !== css) {" +
//                                        "           style.innerHTML = css;" +
//                                        "       }" +
//                                        "   }" +
//                                        "" +
//                                        "   /* 1. Initial Injection */" +
//                                        "   inject();" +
//                                        "" +
//                                        "   /* 2. Check if observer already exists on the window object */" +
//                                        "   if (!window.myAppObserver) {" +
//                                        "       window.myAppObserver = new MutationObserver(function(mutations) {" +
//                                        "           /* Only re-inject if our style tag was wiped out by the SPA navigation */" +
//                                        "           if (!document.getElementById('custom-web-style')) {" +
//                                        "               inject();" +
//                                        "           }" +
//                                        "       });" +
//                                        "" +
//                                        "       window.myAppObserver.observe(document.documentElement, {" +
//                                        "           childList: true," +
//                                        "           subtree: true" +
//                                        "       });" +
//                                        "   }" +
//                                        "})();", null);

                    }

                }


                if (mywebview.getUrl() != null && mywebview.getUrl().contains("linkedin.com") && newProgress > 20) {

                    view.evaluateJavascript(
                            "(function() {" +
                                    "   var css = '.upsell-bottom { display: none !important; }' + " +
                                    "             'body { overflow: auto !important; }' + " +
                                    "             '.feed-item { margin:0 !important; }' + " +
                                    "             '#comment-box-wrapper { transform: translateY(50px) !important; }' + " +
                                    "             '.bottom-sheet { display:none; !important; }' + " +
                                    "             'p[data-test-id=\"main-feed-card__header\"] { display:none; !important; }' + " +
                                    "             '#primary-nav {width: 100vw !important; position: fixed !important; bottom: -50px !important;}' ;" +
                                    "" +
                                    "   function inject() {" +
                                    "       var style = document.getElementById('custom-web-style');" +
                                    "       if (!style) {" +
                                    "           style = document.createElement('style');" +
                                    "           style.id = 'custom-web-style';" +
                                    "           document.head.appendChild(style);" +
                                    "       }" +
                                    "       if (style.innerHTML !== css) {" +
                                    "           style.innerHTML = css;" +
                                    "       }" +
                                    "   }" +
                                    "" +
                                    "   /* 1. Initial Injection */" +
                                    "   inject();" +
                                    "" +
                                    "   /* 2. Check if observer already exists on the window object */" +
                                    "   if (!window.myAppObserver) {" +
                                    "       window.myAppObserver = new MutationObserver(function(mutations) {" +
                                    "           /* Only re-inject if our style tag was wiped out by the SPA navigation */" +
                                    "           if (!document.getElementById('custom-web-style')) {" +
                                    "               inject();" +
                                    "           }" +
                                    "       });" +
                                    "" +
                                    "       window.myAppObserver.observe(document.documentElement, {" +
                                    "           childList: true," +
                                    "           subtree: true" +
                                    "       });" +
                                    "   }" +
                                    "})();", null);

                }

            };
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                // 1. Cancel any existing message
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try {
                    // Use your existing result code
                    startActivityForResult(intent, FILECHOOSER_RESULTCODE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage.onReceiveValue(null); // Critical: notify WebView of failure
                    uploadMessage = null;
                    return false;
                }
                return true;
            }


        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // Call super first

        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (uploadMessage == null) return;

            Uri[] results = null;
            if (resultCode == RESULT_OK && data != null) {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        results[i] = clipData.getItemAt(i).getUri();
                    }
                } else if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }

            // This notifies the WebView with either the result OR null
            // If you don't call this on cancel, the upload button stops working
            uploadMessage.onReceiveValue(results);
            uploadMessage = null;
        }
    }
    public void animate_btn (View v) {
        v.animate()
                .scaleX(0.9f) // Shrink to 90%
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction(() -> {
                    // 2. Bounce back to original size
                    v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setInterpolator(new android.view.animation.OvershootInterpolator())
                            .setDuration(200)
                            .start();
                }).start();
    }
    private void checkAndShowDeepLinkPrompt() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            DomainVerificationManager manager = getSystemService(DomainVerificationManager.class);
            try {
                DomainVerificationUserState userState = manager.getDomainVerificationUserState(getPackageName());

                // Check if the global "Open supported links" toggle is on
                // This is required for deep links that aren't auto-verified
                if (userState.isLinkHandlingAllowed()) {
                    // The user has enabled "Open supported links"
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // If not allowed or older Android version, show the prompt
        showDeepLinkPrompt();
    }
    private void showDeepLinkPrompt() {
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                .setTitle("Enable App Links")
                .setMessage("To open social links directly in Sociality, you need to enable 'Open supported links' in the next screen.")
                .setPositiveButton("Go to Settings", (dialog, which) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
                        Intent intent = new Intent(android.provider.Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS,
                                Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    } else {
                        // Older Androids
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Not Now", null)
                .show();
    }


    private void copyToClipboard(String url) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("URL", url);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Link Copied!", Toast.LENGTH_SHORT).show();
        }
    }
    private void downloadMedia(String url, String userAgent, String contentDisposition, String mimeType) {
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

            // 1. Use the actual headers to get the filename
            // This is much more accurate than guessing from the URL string
            String fileName = URLUtil.guessFileName(url, contentDisposition, mimeType);

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setMimeType(mimeType);

            // Add cookies and UserAgent so the server doesn't block the DownloadManager
            String cookie = CookieManager.getInstance().getCookie(url);
            request.addRequestHeader("Cookie", cookie);
            request.addRequestHeader("User-Agent", userAgent);

            request.setTitle(fileName);
            request.setDescription("Downloading file...");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            if (manager != null) {
                manager.enqueue(request);
                Toast.makeText(this, "Download Started...", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Download failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

}
