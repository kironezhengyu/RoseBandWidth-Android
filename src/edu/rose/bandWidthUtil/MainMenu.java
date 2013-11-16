package edu.rose.bandWidthUtil;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.rose.sprint1.R;

public class MainMenu extends Activity {
	private HashMap<String, String> mData;
	private double mReceived;
	private double mSent;
	private String user = "";
	private Button mUsageReport;

	private ArrayList<BandWidth> mReport;

	private DbAdapter mDbAdapter;
	private int mFontSize = 20;
	private int mLimit = 8000;
	private Context mContext = this;
	private HashMap<String, Integer> mSettings;
	private double mTotal;
	private String mRestriked;

	// private Cursor mScoresCursor;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mDbAdapter = new DbAdapter(this);
		mDbAdapter.open();

		setContentView(R.layout.activity_main_menu);
		Bundle bundel = getIntent().getExtras();
		// initialize handler
		// get data from login
		mData = (HashMap<String, String>) bundel.get("swag");
		// String message = "";
		// message += "Hello User " + mData.get("User") + "Your bandwidth ";
		user = mData.get("USERNAME");
		// String receiStr = mData.get("RECEIVED");
		// receiStr = receiStr.replaceAll(",", "");
		// int receiVal = Float.valueOf(receiStr).intValue();

		// get usage data
		mReceived = Double.valueOf(mData.get("RECEIVED").replaceAll(",", ""));
		mSent = Double.valueOf(mData.get("SENT").replaceAll(",", ""));

		mTotal = mReceived + mSent;
		mRestriked = mData.get("RESTRICTED");
		if (mRestriked.compareTo("Unrestricted") != 0) {
			sendNotification();
		}

		String date = mData.get("DATE");

		mData.put("pRECEIVED",
				"" + Double.valueOf(mData.get("RECEIVED").replaceAll(",", "")));
		mData.put("pSENT",
				"" + Double.valueOf(mData.get("SENT").replaceAll(",", "")));

		Log.d("int", mData.toString());
		// send into DB
		BandWidth bwCurrent = new BandWidth();
		bwCurrent.setReceived(mReceived);
		bwCurrent.setSent(mSent);
		bwCurrent.setName(user);
		bwCurrent.setDate(Integer.parseInt(date));

		mSettings = new HashMap<String, Integer>();
		mSettings.put("FONTSIZE", mFontSize);
		mSettings.put("LIMIT", mLimit);

		System.out.println("what i put into DB" + bwCurrent);

		mDbAdapter.addBandWidth(bwCurrent);

		Button mCheckData = (Button) findViewById(R.id.button_check_bandwidth);
		mCheckData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent bandWidthIntent = new Intent(getBaseContext(),
						BandWidthUsage.class);
				bandWidthIntent.putExtra("swag", mData);
				bandWidthIntent.putExtra("settings", mSettings);
				startActivity(bandWidthIntent);

			}

		});

		mUsageReport = (Button) findViewById(R.id.button_usage_report);
		mUsageReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (user.compareTo("test") == 0) {
					mReport = new ArrayList<BandWidth>();
					putDummy();

				} else {

					mReport = mDbAdapter.getBandWidth(user);
					System.out.println("what i get from DB " + mReport);

				}

				Intent bandWidthIntent = new Intent(getBaseContext(),
						UsageReportMain.class);
				bandWidthIntent.putExtra("swag1", mReport);
				bandWidthIntent.putExtra("settings", mSettings);

				startActivity(bandWidthIntent);

			}
		});

		Button mSetting = (Button) findViewById(R.id.button_settings);
		mSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showUserSetting();
			}
		});

		Button mAbout = (Button) findViewById(R.id.button_about);
		mAbout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog alertDialog = new AlertDialog.Builder(mContext)
						.create();
				alertDialog.setTitle(getString(R.string.about_Title));
				alertDialog.setMessage(getString(R.string.about_content));
				alertDialog.setCanceledOnTouchOutside(true);
				// Set the Icon for the Dialog
				alertDialog.setIcon(R.drawable.about);
				alertDialog.show();

			}
		});

	}

	// create notification
	private void sendNotification() {
		// prepare intent which is triggered if the
		// notification is selected

		Intent intent = new Intent(this, LoginActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		// build notification
		// the addAction re-use the same intent to keep the example short
		Notification n = new Notification.Builder(this)
				.setContentTitle("Your BandWidth is restricted")
				.setSmallIcon(R.drawable.graph)
				.setContentText("Your restriction level is " + mRestriked)
				.setSubText("You have used " + mTotal + " MB data")
				.setContentIntent(pIntent).setAutoCancel(true).build();

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		notificationManager.notify(0, n);

	}

	protected void showUserSetting() {

		final Dialog settingDialog = new Dialog(this);
		settingDialog.setContentView(R.layout.activity_user_setting);
		settingDialog.setTitle(R.string.action_settings);

		Button deleteButton = (Button) settingDialog
				.findViewById(R.id.DeleteProfileButton);
		deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println(user);
				if (user.compareTo("test") == 0) {
					Toast.makeText(mContext, "Your cant delete test account",
							Toast.LENGTH_SHORT).show();
				} else {
					mDbAdapter.removeBandWidth(user);
					Toast.makeText(mContext, "Your data has been removed",
							Toast.LENGTH_SHORT).show();
					Intent returnLogin = new Intent(getBaseContext(),
							LoginActivity.class);
					startActivity(returnLogin);

				}

			}
		});

		final EditText mEdit = (EditText) settingDialog
				.findViewById(R.id.set_bandWidth_limit);
		// get text in EditText:

		final EditText mFontEdit = (EditText) settingDialog
				.findViewById(R.id.set_Font);
		Button saveButton = (Button) settingDialog
				.findViewById(R.id.saveSettingButton);

		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String givenFont = mFontEdit.getText().toString();
				if (givenFont != null) {
					try {
						Integer.parseInt(givenFont);
						if (Integer.parseInt(givenFont) < Integer.valueOf(50)) {

							mFontSize = Integer.parseInt(givenFont);
							System.out.println(givenFont);
							mSettings.put("pFONTSIZE", mFontSize);
						} else {
							Toast.makeText(mContext, "Font is too large!",
									Toast.LENGTH_SHORT).show();
						}
					} catch (NumberFormatException e) {
						Toast.makeText(mContext,
								"You didn't set fontsize, default will apply",
								Toast.LENGTH_SHORT).show();
					}
				}

				String givenLimit = mEdit.getText().toString();
				if (givenLimit != null) {
					try {
						Integer.parseInt(givenLimit);
						mLimit = Integer.parseInt(givenLimit);
						mSettings.put("pLIMIT", mLimit);
					} catch (NumberFormatException e) {
						Toast.makeText(
								mContext,
								"You didn't set BandWidth Limit, default will apply",
								Toast.LENGTH_SHORT).show();
					}

				}

				Toast.makeText(mContext, "Settings Saved", Toast.LENGTH_SHORT)
						.show();
				settingDialog.dismiss();

			}
		});

		// credit button
		Button mCredit = (Button) settingDialog.findViewById(R.id.credit);
		mCredit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, getString(R.string.CreditText),
						Toast.LENGTH_SHORT).show();
			}
		});

		// get text in EditText:

		System.out.println(mFontEdit.getText().toString());

		settingDialog.show();
	}

	private void putDummy() {

		for (int i = 0; i < 15; i++) {
			Double rd = Math.random() * (12000 - 500);
			Double s = Math.random() * (1000 - 200);
			BandWidth bw = new BandWidth();
			bw.setName("test");
			bw.setReceived(rd.intValue());
			bw.setSent(s.intValue());
			bw.setDate(20131100 + i);
			mReport.add(bw);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}

}
