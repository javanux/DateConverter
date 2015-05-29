package com.material_photo_math.trees.dateconverter;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolBar;                     // The tool bar object is the java object equivalent to the app bar of xml
    private ViewPager pager;
    ViewPagerAdapter pagerAdapter;
    SlidingTabLayout tabs;
    CharSequence titles[]={"AD -> BS","BS -> AD"};
    int noOfTabs=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*1)TOOL BAAR
          #Linking the app bar layout file with the tool bar object
          #Tool bar is set as action bar for an activity using setSupportActionBar(ToolBar Instance)
        */
        toolBar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolBar);



        /*
        2)
        #creating the ViewPagerAdapter Instance and passing it a supportFragmentMnager, names of all tabs, and no of tabs
         */
        pagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),titles,noOfTabs );


        /*
        3)VIEW Pager ADAPTER
                #Creating a view pager adapter and it instantiated with it constructor that takes
                    supportFragmentManger as a parameter
                #Assignning the view for ViewPager object and setting the adaper

         */

        pager=(ViewPager)findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);




        /*
        4) SLIDING TAB LAYOUT
            #Assignning the java refrence a view from xml using findViewById()
            #setting the tabs to be fixed and evenly span along the blank spaces
            #Setting Custom Color for the Scroll bar indicator of the Tab View
            #settting the view pager for sliding tab layout

         */

        tabs=(SlidingTabLayout)findViewById(R.id.slidingTabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        tabs.setViewPager(pager);


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
