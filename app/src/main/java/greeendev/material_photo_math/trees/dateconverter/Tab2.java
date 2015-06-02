package greeendev.material_photo_math.trees.dateconverter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.material_photo_math.trees.dateconverter.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by trees on 5/29/15.
 * <p/>
 * <p/>
 * ALGORITHM
 * <p/>
 * Step 1: Define the least possible English date from your valid range ( 1944/01/01 Saturday)
 * - Since, in our above list, the least possible valid Nepali year is 2000, the corresponding English year would be 1944. So, let's suppose, the least possible valid English year be 1944/01/01.
 * <p/>
 * # Step 2: Define the equivalent Nepali date ( 2000/09/17 Saturday)
 * -  Here we should find out the actual Nepali date equivalent to English date that we picked up in step 1. You can find the equivalent Nepali date from any English to Nepali conversion tool. For my case, I got 2000/09/17.
 * <p/>
 * # Step 3: Pick up an English Date you want to convert.
 * <p/>
 * # Step 4: Count the total number of days in between the above two English Dates.
 * - For this you can use any Java API like Jodatime, build-in java.util.Calender or even you can have your own logic to calculate them.
 * <p/>
 * # Step 5: Add the total number of days (calculated in Step 4.) to the equivalent base Nepali date (calculated in Step 2. ) with the help of the above Nepali Year-Month list.
 * - The addition of the number of days will be in a loop, one day at a time. Withing the loop, you need to increment the date by one day. For example, after one iteration, the base Nepali date would be 2000/09/18 Sunday, then 2000/09/19 Monday, then 2000/09/20 Tuesday and so on.
 * <p/>
 * # Step 6: Congratulation, you have the converted Nepali date on your hand.
 */
public class Tab2 extends Fragment implements View.OnClickListener {

    //FRAGMENT VIEW REFRENCE DECLARTATION
    private TextView convertedDate;
    private Button convertButton;
    private EditText sampleYear, sampleMonth, sampleDay;


    //REFRENCE NEPALI DATE
    public static final int startingNepYear = 2000;
    public static final int startingNepMonth = 1;
    public static final int startingNepDay = 1;
    int dayOfWeek; // 2000/1/1 is Wednesday

    //Equivalent REFRENCE ENGLISH DATE
    public static final int startingEngYear = 1943;
    public static final int startingEngMonth = 4;
    public static final int startingEngDay = 14;


    //MAPPING ARRAY FOR NEPALI ith MONTH DAYS VALUE
    private Map<Integer, int[]> nepaliMap;
    Context context;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //INSTANCE CREATION OF HASH MAAP TYPE AND INITIALIZING IT WITH THE intialize() method
        nepaliMap = new HashMap<Integer, int[]>();
        initialize();
        context=getActivity();

        //INFLATION OF THE tab_1.xml layout file and refreing it with view refrence
        View view = inflater.inflate(R.layout.tab_2, container, false);

        //LINKING OF XML VIEWS INTO THEIER RESPECTIVE JAVA OBJECTS
        convertedDate = (TextView) view.findViewById(R.id.nepDateText);
        sampleYear = (EditText) view.findViewById(R.id.nepYear);
        sampleMonth = (EditText) view.findViewById(R.id.nepMonth);
        sampleDay = (EditText) view.findViewById(R.id.nepDay);
        convertButton = (Button) view.findViewById(R.id.BStoAD);

        //SETTING BUTTON WITH CLICK LISTENER
        convertButton.setOnClickListener(this);


        return view;

    }


    @Override
    public void onClick(View v) {

        //USER ENTERED DATE WILL BE STORED IN THESE VARIABLES
        int nepYear;
        int nepMonth;
        int nepDay;

        if (v.getId() == R.id.BStoAD) {


            //PARSING INPUT FROM THE USER AND ASSIGNING IT TO RESPECTIVIE VARAIBALE

            if (dateValidation())     //DO ALL THE NECESSARY STEPS TO CONVERT THE GIVEN DATE
            {

                //Log.i("TRUE","TRUE RETURNED");
                nepYear = Integer.parseInt(sampleYear.getText().toString());
                nepMonth = Integer.parseInt(sampleMonth.getText().toString()) ;
                nepDay = Integer.parseInt(sampleDay.getText().toString());

                //FINDING THE DIFFERRENCE BETWEEN THE REFRENCE ENLISH DATE AND THE USER INPUT DATE IN NUMBER OF DAYS
                long totalNepDaysCount = daysBetween(nepYear, nepMonth, nepDay);

                //METHOD THAT CALCULATES THE NEPALI DATE WHEN DIFFRENCE OF REFRENCE AND GIVEN ENGLISH DATE IN DAYS IS GIVEN
                calculateEnglishDate(totalNepDaysCount);
            } else {
                convertedDate.setText("-> !! वर्ष बि.सं. २०००  देखि  २०९०  सम्म राख्नुहोस!!");
                ///THIS DRAGS THE KEYBOARD DOWN AFTER OKAY IS CLICKED
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(sampleDay.getWindowToken(), 0);
            }

        }
    }



    private void calculateEnglishDate(long totalNepDaysCount) {
// decrement totalEngDaysCount until its value becomes 0

        int[] daysInMonth = new int[]{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int[] daysInMonthOfLeapYear = new int[]{0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};


// calculation of equivalent english date...
        int engYear = startingEngYear;
        int engMonth = startingEngMonth;
        int engDay = startingEngDay;
        dayOfWeek=Calendar.WEDNESDAY;
        int endDayOfMonth = 0;

        while (totalNepDaysCount != 0) {
            if (isLeapYear(engYear)) {
                endDayOfMonth = daysInMonthOfLeapYear[engMonth];
            } else {
                endDayOfMonth = daysInMonth[engMonth];
            }
            engDay++;
            dayOfWeek++;
            if (engDay > endDayOfMonth) {
                engMonth++;
                engDay = 1;
                if (engMonth > 12) {
                    engYear++;
                    engMonth = 1;
                }
            }
            if (dayOfWeek > 7) {
                dayOfWeek = 1;
            }
            totalNepDaysCount--;
        }


        //convertedDate.setTextSize(13);
        convertedDate.setText("-> " + engYear + " " + getMonthName(engMonth) + " " + engDay + ", " + getDayName(dayOfWeek));


        ///THIS DRAGS THE KEYBOARD DOWN AFTER OKAY IS CLICKED
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(sampleDay.getWindowToken(), 0);


    }

    //FUNCTION THAT RETURNS THE DIFFERENCE BETWEEN THE REFRENCE ENGLISH DATE AND THE GIVEN ENGLISH DATE IN DAYS
    public long daysBetween(int nepYear, int nepMonth, int nepDay) {

        long totalNepDaysCount = 0;

// count total days in-terms of year
        for (int i = startingNepYear; i < nepYear; i++) {
            for (int j = 1; j <= 12; j++) {
                totalNepDaysCount += nepaliMap.get(i)[j];
            }
        }

// count total days in-terms of month
        for (int j = startingNepMonth; j < nepMonth; j++) {
            totalNepDaysCount += nepaliMap.get(nepYear)[j];
        }

// count total days in-terms of date
        totalNepDaysCount += nepDay - startingNepDay;

        return totalNepDaysCount;
    }



    //VALIDATION OF USER ENTERED DATE
    private boolean dateValidation() {
        try {

            int year = Integer.parseInt(sampleYear.getText().toString());
            int month = Integer.parseInt(sampleMonth.getText().toString());
            int day = Integer.parseInt(sampleDay.getText().toString());

            if ((year >=2000 && year <= 2090) && (day >= 1 && day <= 32) && (month >= 1 && month <= 12)) {
                return true;
            }

        } catch (Exception e) {
            return false;
        }


        return false;


    }



    public static boolean isLeapYear(int year) {
        if (year % 100 == 0) {
            return year % 400 == 0;
        } else {
            return year % 4 == 0;
        }
    }



    //METHOD TO RETURN DAYS NAME WHEN POSITION IS GIVEN
    private String getDayName(int pos)
    {
        if(pos==1) return "Sunday";
        if(pos==2) return "Monday";
        if(pos==3) return "Tuesday";
        if(pos==4) return "Wednesday";
        if(pos==5) return "Thursday";
        if(pos==6) return "Friday";
        if(pos==7) return "Saturday";

        return "INVALID";



    }

    //METHOD TO RETURN MONTH NAME WHEN POSITION IS GIVEN
    private String getMonthName(int pos) {
        if (pos == 1) return "January";
        if (pos == 2) return "February";
        if (pos == 3) return "March";
        if (pos == 4) return "April";
        if (pos == 5) return "May";
        if (pos == 6) return "June";
        if (pos == 7) return "July";
        if (pos == 8) return "August";
        if (pos == 9) return "September";
        if (pos == 10) return "October";
        if (pos == 11) return "November";
        if (pos == 12) return "December";

        return "INVALID";


    }

    //THIS JUST INITIIALIZES THE MONTH AND DAY MAPPING OF NEPALI DATE
    private void initialize() {

        Log.i("Initialize", "Initialize called");

        nepaliMap.put(2000, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        nepaliMap.put(2001, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2002, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2003, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        nepaliMap.put(2004, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        nepaliMap.put(2005, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2006, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2007, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        nepaliMap.put(2008, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31});
        nepaliMap.put(2009, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2010, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2011, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        nepaliMap.put(2012, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30});
        nepaliMap.put(2013, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2014, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2015, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        nepaliMap.put(2016, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30});
        nepaliMap.put(2017, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2018, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2019, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        nepaliMap.put(2020, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2021, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2022, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30});
        nepaliMap.put(2023, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        nepaliMap.put(2024, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2025, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2026, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        nepaliMap.put(2027, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        nepaliMap.put(2028, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2029, new int[]{0, 31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2030, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        nepaliMap.put(2031, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        nepaliMap.put(2032, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2033, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2034, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        nepaliMap.put(2035, new int[]{0, 30, 32, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31});
        nepaliMap.put(2036, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2037, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2038, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        nepaliMap.put(2039, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30});
        nepaliMap.put(2040, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2041, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2042, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        nepaliMap.put(2043, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30});
        nepaliMap.put(2044, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2045, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2046, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        nepaliMap.put(2047, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2048, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2049, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30});
        nepaliMap.put(2050, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        nepaliMap.put(2051, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2052, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2053, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30});
        nepaliMap.put(2054, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        nepaliMap.put(2055, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2056, new int[]{0, 31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2057, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        nepaliMap.put(2058, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        nepaliMap.put(2059, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2060, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2061, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        nepaliMap.put(2062, new int[]{0, 30, 32, 31, 32, 31, 31, 29, 30, 29, 30, 29, 31});
        nepaliMap.put(2063, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2064, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2065, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        nepaliMap.put(2066, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31});
        nepaliMap.put(2067, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2068, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2069, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        nepaliMap.put(2070, new int[]{0, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30});
        nepaliMap.put(2071, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2072, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2073, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31});
        nepaliMap.put(2074, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2075, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2076, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30});
        nepaliMap.put(2077, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31});
        nepaliMap.put(2078, new int[]{0, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2079, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30});
        nepaliMap.put(2080, new int[]{0, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30});
        nepaliMap.put(2081, new int[]{0, 31, 31, 32, 32, 31, 30, 30, 30, 29, 30, 30, 30});
        nepaliMap.put(2082, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30});
        nepaliMap.put(2083, new int[]{0, 31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30});
        nepaliMap.put(2084, new int[]{0, 31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30});
        nepaliMap.put(2085, new int[]{0, 31, 32, 31, 32, 30, 31, 30, 30, 29, 30, 30, 30});
        nepaliMap.put(2086, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30});
        nepaliMap.put(2087, new int[]{0, 31, 31, 32, 31, 31, 31, 30, 30, 29, 30, 30, 30});
        nepaliMap.put(2088, new int[]{0, 30, 31, 32, 32, 30, 31, 30, 30, 29, 30, 30, 30});
        nepaliMap.put(2089, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30});
        nepaliMap.put(2090, new int[]{0, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30});


    }

}
