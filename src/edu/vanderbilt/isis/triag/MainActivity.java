package edu.vanderbilt.isis.triag;

/*
 * To all those brave enough to enter, ye be warned
 * 
 * 				Here there be hacking.
 */

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments representing
     * each object in a collection. We use a {@link android.support.v4.app.FragmentStatePagerAdapter}
     * derivative, which will destroy and re-create fragments as needed, saving and restoring their
     * state in the process. This is important to conserve memory and is a best practice when
     * allowing navigation between objects in a potentially large collection.
     */
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_activity);

        // Create an adapter that when requested, will return a fragment representing an object in
        // the collection.
        // 
        // ViewPager and its adapters use support library fragments, so we must use
        // getSupportFragmentManager.
        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
    }
    
    //Vital gathering functions
    //OnClickListener getTemp = new OnClickListener() {
    	public void getTemp(View view){
    		String response = null;
    		int color = Color.BLACK;

    		//Get the temp 
    		double temp = 0;
    		//TODO

    		//Get the appropriate response
    		if(temp < 80){
    			response = "Please point the sensor toward your mouth and try again";
    		}else if(temp < 95){
    			response = "Hypothermia";
    			color = Color.BLUE;
    		}else if(temp < 97){
    			response = "Below Normal";
    			color = Color.CYAN;
    		}else if(temp < 99){
    			response = "Normal";
    			color = Color.GREEN;
    		}else if(temp < 100){
    			response = "Low Fever";
    			color = Color.YELLOW;
    		}else if(temp < 103){
    			response = "Fever";
    			color = Color.MAGENTA;
    		}else if(temp < 107){
    			response = "High Fever";
    			color = Color.RED;
    		}else{
    			response = "Cooked";
    			color = Color.RED;
    		}

    		//Remove the button
    		LinearLayout tempBox = (LinearLayout)findViewById(R.id.tempBox);
    		tempBox.removeAllViews();

    		//Display the temp
    		TextView tempLabel = new TextView(view.getContext());
    		tempLabel.setText(response);
    		tempLabel.setTextColor(color);
    		tempLabel.setTextSize(40);

    		TextView tempView = new TextView(view.getContext());
    		tempView.setText(temp + "\u00b0F");
    		tempView.setTextSize(90);

    		tempView.setGravity(Gravity.CENTER);
    		tempLabel.setGravity(Gravity.CENTER);

    		//Add "Retake" button
    		Button retake = new Button(view.getContext());
    		retake.setText("Measure Again");
    		retake.setTextSize(30);
    		retake.setGravity(Gravity.CENTER);
    		retake.setOnClickListener(cancelTemp);

    		//Create layout rule for this button and add to container
    		RelativeLayout cont = (RelativeLayout)findViewById(R.id.retakeBox);
    		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); // or wrap_content
    		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    		cont.addView(retake , layoutParams);

    		tempBox.addView(tempLabel);
    		tempBox.addView(tempView);
    	}
    //};

    OnClickListener cancelTemp = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//Clear the tempBox and remove retake button
			LinearLayout tempBox = (LinearLayout)findViewById(R.id.tempBox);
			RelativeLayout cont = (RelativeLayout)findViewById(R.id.retakeBox);

			tempBox.removeAllViews();
			cont.removeAllViews();

			//Add the button to measure temp
			Button temp = new Button(v.getContext());
			temp.setText(R.string.takeTemp);
			temp.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v){
					getTemp(v);
				}
			});
			//}getTemp);
			temp.setGravity(Gravity.CENTER);
    		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); // or wrap_content
			tempBox.addView(temp, layoutParams);
		}
	};

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, MainActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
     * representing an object in the collection.
     */
    public static class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
    	String[] titles = {"General Information", 
    			           "Vitals: Temperature", 
                           "Vitals: Blood Oxidation",
                           "Vitals: EKG"};

        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            Bundle args = new Bundle();

        	switch(i){

        	case 0:
        	    //General info sheet
                fragment = new GeneralInfoSheet();
                //args.putInt(GeneralInfoSheet.ARG_OBJECT, i + 1); // Our object is just an integer :-P
                fragment.setArguments(args);
                break;
            
        	case 1:
                //Temp Measuring Sheet
                fragment = new TempSheet();
                //args.putInt(GeneralInfoSheet.ARG_OBJECT, i + 1); // Our object is just an integer :-P
                fragment.setArguments(args);
                break;

       	    case 2:
                //Temp Measuring Sheet
                fragment = new PulseOxSheet();
                //args.putInt(GeneralInfoSheet.ARG_OBJECT, i + 1); // Our object is just an integer :-P
                fragment.setArguments(args);
                break;

         	case 3:
                //Temp Measuring Sheet
                fragment = new EKGSheet();
                //args.putInt(GeneralInfoSheet.ARG_OBJECT, i + 1); // Our object is just an integer :-P
                fragment.setArguments(args);
                break;
                
        	}
        	return fragment;
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 100-object collection.
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class GeneralInfoSheet extends Fragment {

        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	//Create General Form
            View rootView = inflater.inflate(R.layout.form_general, container, false);
            Bundle args = getArguments();
            //((TextView) rootView.findViewById(android.R.id.text1)).setText(
             //       Integer.toString(args.getInt(ARG_OBJECT)));
            return rootView;
        }
    }

    public static class TempSheet extends Fragment {//Temp = temperature not temporary

        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	//Create General Form
            View rootView = inflater.inflate(R.layout.form_temp, container, false);
            Bundle args = getArguments();
            //((TextView) rootView.findViewById(android.R.id.text1)).setText(
             //       Integer.toString(args.getInt(ARG_OBJECT)));
            return rootView;
        }
    }

    public static class EKGSheet extends Fragment {

        public static final String ARG_OBJECT = "object";
        private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
        private XYSeries mCurrentSeries;
        private XYSeriesRenderer mCurrentRenderer;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            mCurrentSeries = new XYSeries("Sample Data");
            mDataset.addSeries(mCurrentSeries);
            mCurrentRenderer = new XYSeriesRenderer();
            mRenderer.addSeriesRenderer(mCurrentRenderer);

            addTestData();
            //LineGraphView graph = new LineGraphView(this.getActivity(), "Electrocardiogram");
            //GraphViewSeries series = new GraphViewSeries(new GraphViewData[] {
            		//new GraphViewData(1, 2.0d)
            		//, new GraphViewData(2, 1.5d)
            		//, new GraphViewData(3, 2.5d)
            		//, new GraphViewData(4, 1.0d)
            //});
            //graph.addSeries(series);
            mRenderer.setShowLegend(false);
            GraphicalView graph = ChartFactory.getCubeLineChartView(this.getActivity(), mDataset, mRenderer, 0.3f);
            
            View rootView = inflater.inflate(R.layout.form_ekg, container, false);
            Bundle args = getArguments();

            FrameLayout layout = (FrameLayout) rootView.findViewById(R.id.graphSpot);
            layout.addView(graph);

            return rootView;
        }
        
        private void addTestData(){
        	for(int i = 0; i < 10; i++){
        		mCurrentSeries.add(i,Math.random()*10);
        	}
        }
    }

    public static class PulseOxSheet extends Fragment {

        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.form_po, container, false);
            Bundle args = getArguments();
            //TODO

            return rootView;
        }
    }
}
