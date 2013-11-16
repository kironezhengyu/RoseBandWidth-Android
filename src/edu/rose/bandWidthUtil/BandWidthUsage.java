package edu.rose.bandWidthUtil;

import java.util.HashMap;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import edu.rose.sprint1.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

public class BandWidthUsage extends Activity {

	/** The main dataset that includes all the series that go into a chart. */
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

	/** The main renderer that includes all the renderers customizing a chart. */
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

	/** The most recently added series. */
	private XYSeries mReceivedDataSeries;
	private XYSeries mSentDataSeries;
	private XYSeries mTotalDataSeries;
	private XYSeries mTotalDataLeftSeries;

	/** The most recently created renderer, customizing the current series. */
	private XYSeriesRenderer mReceivedRenderer;
	private XYSeriesRenderer mSentRenderer;
	private XYSeriesRenderer mTotalRenderer;
	private XYSeriesRenderer mTotalLeftRenderer;
	private HashMap<String, String> mData;
	private String mResLevel;
	private String mRecevied;
	private String mSent;

	/** The chart view that displays the data. */
	private GraphicalView mChartView;
	//preconfigured parameters
	private String mUser;
	private int mLimit = 8000;
	private int mFontSize = 20;

	private HashMap<String, Integer> mSettings;

	private void initChart() {
		mReceivedDataSeries = new XYSeries("Data Received");
		mSentDataSeries = new XYSeries("Data Sent");
		mTotalDataSeries = new XYSeries("Policy Data Limit");
		mTotalDataLeftSeries = new XYSeries("Policy Data Left");

		mDataset.addSeries(mReceivedDataSeries);
		mDataset.addSeries(mSentDataSeries);
		mDataset.addSeries(mTotalDataLeftSeries);
		mDataset.addSeries(mTotalDataSeries);

		mReceivedRenderer = new XYSeriesRenderer();
		mSentRenderer = new XYSeriesRenderer();
		mTotalRenderer = new XYSeriesRenderer();
		mTotalLeftRenderer = new XYSeriesRenderer();

		mRenderer.addSeriesRenderer(mReceivedRenderer);
		mRenderer.addSeriesRenderer(mSentRenderer);
		mRenderer.addSeriesRenderer(mTotalLeftRenderer);
		mRenderer.addSeriesRenderer(mTotalRenderer);
	}

	private void configRender() {
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.rgb(0, 0, 0));
		mRenderer.setAxisTitleTextSize(30);
		mRenderer.setChartTitle("Bandwidth Usage Chart for User " + mUser
				+ " \n" + mResLevel);
		mRenderer.setGridColor(Color.rgb(255, 255, 255));
		mRenderer.setShowGridX(true);

		mRenderer.addXTextLabel(1, "Data Received");
		mRenderer.setXLabels(0);

		mRenderer.addXTextLabel(3, "Data Sent");
		mRenderer.setXLabels(0);

		mRenderer.addXTextLabel(5, "Policy Total");

		mRenderer.setXAxisMin(0);
		mRenderer.setYAxisMin(0);
		mRenderer.setXAxisMax(7);
		
		mRenderer.setYTitle("MB");
		mRenderer.setChartTitleTextSize(30);
		mRenderer.setLabelsTextSize(20);
		mRenderer.setLegendTextSize(20);
		mRenderer.setZoomButtonsVisible(true);
	}

	private void configCurrentRender() {
		mReceivedRenderer.setPointStyle(PointStyle.DIAMOND);
		mReceivedRenderer.setColor(Color.rgb(133, 255, 226));
		mReceivedRenderer.setFillPoints(true);
		mReceivedRenderer.setDisplayChartValues(true);
		mReceivedRenderer.setDisplayChartValuesDistance(1);
		mReceivedRenderer.setChartValuesTextSize(mFontSize);

		mSentRenderer.setPointStyle(PointStyle.CIRCLE);
		mSentRenderer.setColor(Color.rgb(255, 252, 105));
		mSentRenderer.setFillPoints(true);
		mSentRenderer.setDisplayChartValues(true);
		mSentRenderer.setDisplayChartValuesDistance(1);
		mSentRenderer.setChartValuesTextSize(mFontSize);

		mTotalRenderer.setPointStyle(PointStyle.SQUARE);
		mTotalRenderer.setColor(Color.rgb(255, 0, 0));
		mTotalRenderer.setFillPoints(true);
		mTotalRenderer.setDisplayChartValues(true);
		mTotalRenderer.setDisplayChartValuesDistance(10);
		mTotalRenderer.setChartValuesTextSize(mFontSize);

		mTotalLeftRenderer.setPointStyle(PointStyle.SQUARE);
		mTotalLeftRenderer.setColor(Color.rgb(117, 255, 105));
		mTotalLeftRenderer.setFillPoints(true);
		mTotalLeftRenderer.setDisplayChartValues(true);
		mTotalLeftRenderer.setDisplayChartValuesDistance(10);
		mTotalLeftRenderer.setChartValuesTextSize(mFontSize);

	}

	private void addSampleData() {
		mReceivedDataSeries.add(1, Double.parseDouble(mRecevied));
		mSentDataSeries.add(3, Double.parseDouble(mSent));
		mTotalDataLeftSeries.add(5, mLimit);
		mTotalDataSeries.add(5,
				Double.parseDouble(mRecevied) + Double.parseDouble(mSent));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_band_width_usage);
		mData = new HashMap<String, String>();
		Bundle bundel = getIntent().getExtras();

		// initialize handler
		// get data from login
		mData = (HashMap<String, String>) bundel.get("swag");
		mSettings = (HashMap<String, Integer>) bundel.get("settings");
		System.out.println(mSettings);
		// String message = "";
		// message += "Hello User " + mData.get("User") + "Your bandwidth ";
		mResLevel = "Restriction Level: " + mData.get("RESTRICTED");
		mRecevied = mData.get("pRECEIVED");
		mSent = mData.get("pSENT");
		mUser = mData.get("USERNAME");
		System.out.println(mSettings);

		try {
			mFontSize = mSettings.get("pFONTSIZE");

			mLimit = mSettings.get("pLIMIT");
		} catch (Exception e) {
			System.out.println("No custome settings passed in");
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		LinearLayout layout = (LinearLayout) findViewById(R.id.chart);

		if (mChartView == null) {
			initChart();
			configRender();
			configCurrentRender();
			addSampleData();

			mChartView = ChartFactory.getBarChartView(this, mDataset,
					mRenderer, Type.STACKED);

			layout.addView(mChartView);

		} else {
			mChartView.repaint();
		}
	}

}
