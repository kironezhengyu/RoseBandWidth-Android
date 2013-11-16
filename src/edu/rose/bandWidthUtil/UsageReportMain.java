package edu.rose.bandWidthUtil;

import java.util.ArrayList;
import java.util.HashMap;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
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

public class UsageReportMain extends Activity {

	/** The main dataset that includes all the series that go into a chart. */
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

	/** The main renderer that includes all the renderers customizing a chart. */
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

	/** The most recently added series. */
	private XYSeries mReceivedDataSeries;
	private XYSeries mSentDataSeries;
	private XYSeries mTotalDataSeries;

	/** The most recently created renderer, customizing the current series. */
	private XYSeriesRenderer mReceivedRenderer;
	private XYSeriesRenderer mSentRenderer;
	private XYSeriesRenderer mTotalRenderer;

	/** The chart view that displays the data. */
	private GraphicalView mChartView;
	private ArrayList<BandWidth> mData;
	private String mUser;
	private ArrayList<String> mList;
	private int mFontSize = 20;

	private HashMap<String, String> monthLookup;
	private HashMap<String, Integer> mSettings;

	private void initChart() {
		mReceivedDataSeries = new XYSeries("Data Received");
		mSentDataSeries = new XYSeries("Data Sent");
		mTotalDataSeries = new XYSeries("Policy Total");

		mDataset.addSeries(mReceivedDataSeries);
		mDataset.addSeries(mSentDataSeries);
		mDataset.addSeries(mTotalDataSeries);

		mReceivedRenderer = new XYSeriesRenderer();
		mSentRenderer = new XYSeriesRenderer();
		mTotalRenderer = new XYSeriesRenderer();

		mRenderer.addSeriesRenderer(mReceivedRenderer);
		mRenderer.addSeriesRenderer(mSentRenderer);
		mRenderer.addSeriesRenderer(mTotalRenderer);
	}

	private void configRender() {
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.rgb(0, 0, 0));
		mRenderer.setAxisTitleTextSize(30);
		mRenderer.setChartTitle("Usage Report");
		mRenderer.setGridColor(Color.rgb(255, 255, 255));

		// set up initial zooms
		// mRenderer.setXAxisMin(0);
		// mRenderer.setYAxisMin(0);
		// mRenderer.setXAxisMax(mList.size() + 2);
		// mRenderer.setYAxisMax(mTotalDataSeries.getMaxY() + 10);

		mRenderer.setShowGridX(true);
		mRenderer.setShowGridY(true);
		mRenderer.setXLabels(0);
		// mRenderer.setShowGrid(true);

		for (int i = 0; i < mData.size(); i++) {
			String item = "" + mData.get(i).getDate();
			String m = item.substring(item.length() - 4, item.length() - 2);
			String month = monthLookup.get(m);
			String dis = month + "/"
					+ item.substring(item.length() - 2, item.length());
			mList.add(dis);
			mRenderer.addXTextLabel(i, dis);

		}
		mRenderer.setLabelsTextSize(14);
		mRenderer.setChartTitle("Bandwidth Usage History for User " + mUser);
		// mRenderer.setScale(10);
		mRenderer.setYTitle("MB");
		mRenderer.setChartTitleTextSize(30);
		mRenderer.setLabelsTextSize(15);
		mRenderer.setLegendTextSize(25);
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
		mTotalRenderer.setColor(Color.rgb(117, 255, 105));
		mTotalRenderer.setFillPoints(true);
		mTotalRenderer.setDisplayChartValues(true);
		mTotalRenderer.setDisplayChartValuesDistance(10);
		mTotalRenderer.setChartValuesTextSize(mFontSize);

	}
	//add data to renderer
	private void addSampleData() {
		for (int i = 0; i < mList.size(); i++) {
			mReceivedDataSeries.add(i, mData.get(i).getReceived());

		}

		for (int i = 0; i < mList.size(); i++) {
			mSentDataSeries.add(i, mData.get(i).getSent());

		}

		for (int i = 0; i < mList.size(); i++) {
			mTotalDataSeries.add(i, mData.get(i).getTotal());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usage_report_main);
		mData = new ArrayList<BandWidth>();
		Bundle bundel = getIntent().getExtras();
		// initialize handler
		// get data from login
		mData = (ArrayList<BandWidth>) bundel.get("swag1");
		mUser = mData.get(0).getName();
		mList = new ArrayList<String>();
		monthLookup = createLookup();
		mSettings = (HashMap<String, Integer>) bundel.get("settings");
		try {
			mFontSize = mSettings.get("pFONTSIZE");

		} catch (Exception e) {
			System.out.println("No custome settings passed in");
		}
	}

	//integer to month lookup table
	private HashMap<String, String> createLookup() {
		HashMap<String, String> res = new HashMap<String, String>();
		res.put("01", "Jan");
		res.put("02", "Feb");
		res.put("03", "Mar");
		res.put("04", "Apr");
		res.put("05", "May");
		res.put("06", "June");
		res.put("07", "Jul");
		res.put("08", "Aug");
		res.put("09", "Sept");
		res.put("10", "Oct");
		res.put("11", "Nov");
		res.put("12", "Dec");
		return res;
	}

	@Override
	protected void onResume() {
		super.onResume();
		LinearLayout layout = (LinearLayout) findViewById(R.id.chart1);

		if (mChartView == null) {
			initChart();
			configRender();
			configCurrentRender();
			addSampleData();

			mChartView = ChartFactory.getLineChartView(this, mDataset,
					mRenderer);

			layout.addView(mChartView);

		} else {
			mChartView.repaint();
		}
	}

}
