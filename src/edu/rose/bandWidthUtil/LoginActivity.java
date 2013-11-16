package edu.rose.bandWidthUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import edu.rose.sprint1.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

	private String mUrl = "https://netreg.rose-hulman.edu/tools/networkUsage.pl";
	private String mDomain = "rose-hulman.edu"; // May also be referred as
	// realm
	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	// hashMap
	private HashMap<String, String> mData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mData = new HashMap<String, String>();
		setContentView(R.layout.activity_login);

		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.username);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// put dummy data for test use
	// test alwasys test bandwidth
	private void putDummy() {
		Random rd = new Random();
		Integer rs = rd.nextInt(120000);

		Integer rc = rd.nextInt(50000);
		mData.put("USERNAME", "test");
		mData.put("RESTRICTED", "128K");
		mData.put("RECEIVED", "" + rs);
		mData.put("SENT", rc + "");
		mData.put("DATE", "20131111");
		System.out.println(mData.toString());

	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.

			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			if (mEmail.compareTo("test") == 0) {
				Log.d("LLL", "hahaTest" + mEmail);
				putDummy();
				Intent mainMenuIntent = new Intent(getBaseContext(),
						MainMenu.class);

				mainMenuIntent.putExtra("swag", mData);
				startActivity(mainMenuIntent);

			} else {
				mAuthTask = new UserLoginTask();
				mAuthTask.execute((Void) null);
			}

		}

	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// : attempt authentication against a network service.

			try {
				getAuthenticate(mEmail, mPassword, mUrl, mDomain);

			} catch (Exception e) {
				e.printStackTrace();
				Log.d("LLL", "wrong username" + mEmail);
				Log.d("LLL", "Auth Wrong");
				return false;
			}

			Intent mainMenuIntent = new Intent(getBaseContext(), MainMenu.class);

			mainMenuIntent.putExtra("swag", mData);
			startActivity(mainMenuIntent);
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				finish();

			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

	// the method tries to login and throws errors if failed, and return data if
	// succeeded
	@SuppressLint("SimpleDateFormat")
	public Boolean getAuthenticate(final String username,
			final String password, String url, final String domain)
			throws Exception {
		StringBuilder response = new StringBuilder();

		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getAuthSchemes().register("ntlm", new NTLMSchemeFactory());
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(AuthScope.ANY, new NTCredentials(username,
				password, "", mDomain));

		// Make sure the same context is used to execute logically related
		// requests
		HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(credsProvider);

		// Execute a cheap method first. This will trigger NTLM authentication
		HttpGet httpget = new HttpGet(mUrl);
		HttpResponse response1 = httpclient.execute(httpget, context);

		HttpEntity entity1 = response1.getEntity();

		InputStream in = new BufferedInputStream(entity1.getContent());
		Log.d("LLL", "I am getting input");
		BufferedReader stream = new BufferedReader(new InputStreamReader(in));
		String str = "";
		while ((str = stream.readLine()) != null) {
			response.append(str);
		}
		in.close();

		String out = response.toString();

		// System.out.println(out);

		Document doc = Jsoup.parse(out);

		Elements data = doc.getElementsByClass("ms-rteTableOddRow-1");

		String[] result = data.get(0).getElementsByTag("td").html().split("\n");

		String RESTRICTED = result[0];

		String RECEIVED = result[1].split(" ")[0];
		String SENT = result[2].split(" ")[0];

		String timeStamp = new SimpleDateFormat("yyyyMMdd" + "")
				.format(Calendar.getInstance().getTime());
		mData.put("USERNAME", mEmail);
		mData.put("RESTRICTED", RESTRICTED);
		mData.put("RECEIVED", RECEIVED);
		mData.put("SENT", SENT);
		mData.put("DATE", timeStamp);
		System.out.println(mData.toString());
		return true;

	}

}
