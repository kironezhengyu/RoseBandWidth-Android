package edu.rose.sprint1;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		showRequirements();
		
		//final Intent loginIntent = new Intent(this, LoginActivity.class);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void showRequirements(){
		DialogFragment sReq = new DialogFragment(){
			public Dialog onCreateDialog(Bundle b){
				AlertDialog.Builder aboutBuilder = new AlertDialog.Builder(getActivity());
				aboutBuilder.setMessage(R.string.message_Requirements);
				aboutBuilder.setTitle(R.string.message_Req_Title);
				aboutBuilder.setIcon(R.drawable.ic_pound_sign);
				aboutBuilder.setPositiveButton(R.string.ok, 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								Intent loginIntent = new Intent(getBaseContext(), LoginActivity.class);
								startActivity(loginIntent);
							}
						});
				aboutBuilder.setNegativeButton(R.string.cancel, 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
				return aboutBuilder.create();
			}
		};
		sReq.show(getFragmentManager(), "");
	}
}
