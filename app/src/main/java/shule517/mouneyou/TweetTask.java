package shule517.mouneyou;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by shule517 on 2015/09/27.
 */
public
// Image取得用スレッドクラス
class TweetTask extends AsyncTask<String, Void, Boolean> {

    String text;

    TweetTask(String text) {
        this.text = text;
    }

    private void tweet() {
        try {

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("wgrDtnR6zi3sv2d6m6k3C9olS")
                    .setOAuthConsumerSecret("mZGlg1tOgElsoX4Ge7ymZzGlMaj2IRG8l3pWsxGQYVU0ECYKAZ")
                    .setOAuthAccessToken("67637197-6Em5LcQ197vANf1UQ1PpDvoKpIF83CImN2lozKDDz")
                    .setOAuthAccessTokenSecret("IT27ijIQNk7Rzz2kWfvmfxTQBSZ5QtidnqDBBxhkxQqy4");
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();

            // Twitter twitter = new TwitterFactory().getInstance();

            try {
                // get request token.
                // this will throw IllegalStateException if access token is already available
                RequestToken requestToken = twitter.getOAuthRequestToken();
                System.out.println("Got request token.");
                System.out.println("Request token: " + requestToken.getToken());
                System.out.println("Request token secret: " + requestToken.getTokenSecret());
                AccessToken accessToken = null;

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                while (null == accessToken) {
                    System.out.println("Open the following URL and grant access to your account:");
                    System.out.println(requestToken.getAuthorizationURL());
                    System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                    String pin = br.readLine();
                    try {
                        if (pin.length() > 0) {
                            accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                        } else {
                            accessToken = twitter.getOAuthAccessToken(requestToken);
                        }
                    } catch (TwitterException te) {
                        if (401 == te.getStatusCode()) {
                            System.out.println("Unable to get the access token.");
                        } else {
                            te.printStackTrace();
                        }
                    }
                }
                System.out.println("Got access token.");
                System.out.println("Access token: " + accessToken.getToken());
                System.out.println("Access token secret: " + accessToken.getTokenSecret());
            } catch (IllegalStateException ie) {
                // access token is already available, or consumer key/secret is not set.
                if (!twitter.getAuthorization().isEnabled()) {
                    System.out.println("OAuth consumer key/secret is not set.");
                    //System.exit(-1);
                }
            }
            twitter4j.Status status = twitter.updateStatus(text);
            System.out.println("Successfully updated the status to [" + status.getText() + "].");
            //System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            //System.exit(-1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to read the system input.");
            //System.exit(-1);
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        tweet();
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
    }
}
