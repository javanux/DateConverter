package com.material_photo_math.trees.dateconverter;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by trees on 5/29/15.
 *
 *
 * ALGORITHM
 *
 *  Step 1: Define the least possible English date from your valid range ( 1944/01/01 Saturday)
 - Since, in our above list, the least possible valid Nepali year is 2000, the corresponding English year would be 1944. So, let's suppose, the least possible valid English year be 1944/01/01.

 # Step 2: Define the equivalent Nepali date ( 2000/09/17 Saturday)
 -  Here we should find out the actual Nepali date equivalent to English date that we picked up in step 1. You can find the equivalent Nepali date from any English to Nepali conversion tool. For my case, I got 2000/09/17.

 # Step 3: Pick up an English Date you want to convert.

 # Step 4: Count the total number of days in between the above two English Dates.
 - For this you can use any Java API like Jodatime, build-in java.util.Calender or even you can have your own logic to calculate them.

 # Step 5: Add the total number of days (calculated in Step 4.) to the equivalent base Nepali date (calculated in Step 2. ) with the help of the above Nepali Year-Month list.
 - The addition of the number of days will be in a loop, one day at a time. Withing the loop, you need to increment the date by one day. For example, after one iteration, the base Nepali date would be 2000/09/18 Sunday, then 2000/09/19 Monday, then 2000/09/20 Tuesday and so on.

 # Step 6: Congratulation, you have the converted Nepali date on your hand.

 *
 */
public class Tab1 extends Fragment implements View.OnClickListener {

    //FRAGMENT VIEW REFRENCE DECLARTATION
    private TextView convertedDate;
    private Button convertButton;
    private EditText sampleYear, sampleMonth,sampleDay;


    //REFRENCE ENGLISH DATE
    public static final int startingEngYear = 1944;
    public static final int startingEngMonth = 0;
    public static final int startingEngDay = 1;
    int dayOfWeek ; // 1944/1/1 is Saturday

    //Equivalent REFRENCE NEPALI DATE
    public static final int startingNepYear = 2000;
    public static final int startingNepMonth = 9;
    public static final int startingNepDay = 17;





    //MAPPING ARRAY FOR NEPALI ith MONTH DAYS VALUE
    private  Map<Integer, int[]> nepaliMap ;
    //private long totalEngDaysCount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //INSTANCE CREATION OF HASH MAAP TYPE AND INITIALIZING IT WITH THE intialize() method
        nepaliMap = new HashMap<Integer, int[]>(); initialize();

        //INFLATION OF THE tab_1.xml layout file and refreing it with view refrence
        View view = inflater.inflate(R.layout.tab_1, container, false);

        //LINKING OF XML VIEWS INTO THEIER RESPECTIVE JAVA OBJECTS
        convertedDate = (TextView) view.findViewById(R.id.engDateText);
        sampleYear=(EditText)view.findViewById(R.id.engYear);
        sampleMonth=(EditText)view.findViewById(R.id.engMonth);
        sampleDay=(EditText)view.findViewById(R.id.engDay);
        convertButton = (Button) view.findViewById(R.id.ADtoBS);

        //SETTING BUTTON WITH CLICK LISTENER
        convertButton.setOnClickListener(this);


        return view;

    }


    private void calculateNepaliDate(long totalEngDaysCount) {
// decrement totalEngDaysCount until its value becomes 0

        // CONVERTED NEPALI DATE STORING VARAIBALES
         int nepYear ;
         int nepMonth;
         int nepDay ;

        nepYear=startingNepYear;
        nepMonth=startingNepMonth;
        nepDay=startingNepDay;
        dayOfWeek=Calendar.SATURDAY;
        while (totalEngDaysCount != 0) {

            // getting the total number of days in month nepMonth in year nepYear

            int daysInIthMonth;

                daysInIthMonth = nepaliMap.get(nepYear)[nepMonth];



            nepDay++; // incrementing nepali day

            if (nepDay > daysInIthMonth) {
                nepMonth++;
                nepDay = 1;
            }
            if (nepMonth > 12) {
                nepYear++;
                nepMonth = 1;
            }

            dayOfWeek++; // count the days in terms of 7 days
            if (dayOfWeek > 7) {
                dayOfWeek = 1;
            }

            totalEngDaysCount--;
        }

       // convertedDate.setTextSize(13);
        convertedDate.setText("-> " + nepYear + " " + getMonthName(nepMonth) + " " + nepDay + ", " + getDayName(dayOfWeek));


    }

    //FUNCTION THAT RETURNS THE DIFFERENCE BETWEEN THE REFRENCE ENGLISH DATE AND THE GIVEN ENGLISH DATE IN DAYS
    public long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date =(Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }

        return daysBetween;
    }



    @Override
    public void onClick(View v) {

        //USER ENTERED DATE
         int engYear;
         int engMonth;
         int engDay;

        if (v.getId() == R.id.ADtoBS) {


            //PARSING INPUT FROM THE USER AND ASSIGNING IT TO RESPECTIVIE VARAIBALE

            if(dateValidation())     //DO ALL THE NECESSARY STEPS TO CONVERT THE GIVEN DATE
            {

                //Log.i("TRUE","TRUE RETURNED");
                engYear= Integer.parseInt(sampleYear.getText().toString());
                engMonth=Integer.parseInt(sampleMonth.getText().toString())-1;   // -1 because Gregorian indexing is from 0
                engDay=Integer.parseInt(sampleDay.getText().toString());

                //CREATING GREGORIAN CALENDER INSTANCE USING THE USER INPUT DATE
                Calendar currentEngDate = new GregorianCalendar();
                currentEngDate.set(engYear, engMonth, engDay);
                Calendar baseEngDate = new GregorianCalendar();
                baseEngDate.set(startingEngYear, startingEngMonth, startingEngDay);


                //FINDING THE DIFFERRENCE BETWEEN THE REFRENCE ENLISH DATE AND THE USER INPUT DATE IN NUMBER OF DAYS
                long totalEngDaysCount = daysBetween(baseEngDate, currentEngDate);

                //METHOD THAT CALCULATES THE NEPALI DATE WHEN DIFFRENCE OF REFRENCE AND GIVEN ENGLISH DATE IN DAYS IS GIVEN
                calculateNepaliDate(totalEngDaysCount);
            }

            else
            {
                convertedDate.setText("!! Date out of range !!");
            }

        }
    }


    //VALIDATION OF USER ENTERED DATE
    private boolean dateValidation()
    {
        try
        {

            int year= Integer.parseInt(sampleYear.getText().toString());
            int month=Integer.parseInt(sampleMonth.getText().toString());
            int day=Integer.parseInt(sampleDay.getText().toString());

            if((year>=1944 && year<=2033)  && (day>=1 && day<=31 )&& (month>=1 && month<=12))
            {
                return true;
            }

        }catch(Exception e)
        {
            return false;
        }


        return false;



    }


    //METHOD TO RETURN DAYS NAME WHEN POSITION IS GIVEN
    private String getDayName(int pos) {
        if (pos == 1) return "Aaitabar";
        if (pos == 2) return "Sombar";
        if (pos == 3) return "Mangalbar";
        if (pos == 4) return "Budhabar";
        if (pos == 5) return "Bihibar";
        if (pos == 6) return "Sukrabar";
        if (pos == 7) return "Sanibar";

        return "INVALID";


    }





    //METHOD TO RETURN MONTH NAME WHEN POSITION IS GIVEN
    private String getMonthName(int pos)
    {
        if(pos==1) return "Baisakh";
        if(pos==2) return "Jestha";
        if(pos==3) return "Ashar";
        if(pos==4) return "Shrawn";
        if(pos==5) return "Bhadra";
        if(pos==6) return "Asoj";
        if(pos==7) return "Kartik";
        if(pos==8) return "Mangsir";
        if(pos==9) return "Poush";
        if(pos==10) return "Magh";
        if(pos==11) return "Falgun";
        if(pos==12) return "Chaitra";

        return "INVALID";



    }

    //THIS JUST INITIIALIZES THE MONTH AND DAY MAPPING OF NEPALI DATE
    private void initialize(){

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
