package com.example.finalproj;

import android.util.Log;

import java.util.Objects;

public class DateHandler {

    public static String[] getDate(String direction, String date){

        int day = Integer.parseInt(date.substring(0,2));
        int month = Integer.parseInt(date.substring(2,4));
        int year = Integer.parseInt(date.substring(4,8));

        boolean addMonth = false;

        if(Objects.equals(direction, "next")){
            day += 1;
            if(addMonth == false){
                if(month == 1 || month == 3 ||month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
                    addMonth = true;
                    if(day == 32){
                        month+= 1;
                        day = 1;
                    }
                }
            }
            if(addMonth == false){
                if(month == 4 || month == 6 || month == 9 || month == 11){
                    addMonth = true;
                    if(day == 31){
                        month +=1;
                        day = 1;
                    }
                }
            }
            if(addMonth == false){
                if(month == 2){
                    addMonth = true;
                    if(!isLeapYear(year)){
                        if(day == 29){
                            month +=1;
                            day = 1;
                        }
                    }
                    if(isLeapYear(year)){
                        if(day == 30){
                            month +=1;
                            day = 1;
                        }
                    }

                }
            }

            if(month == 13){
                year +=1;
                day = 1;
                month = 1;
            }
        }
        if(Objects.equals(direction, "back")){
            day -= 1;
            if(addMonth == false){
                if(month == 1 || month == 2 || month == 4 || month == 6 || month == 8 || month == 9 || month == 11){
                    addMonth = true;
                    if(day == 0){
                        month -= 1;
                        day = 31;
                    }
                }
            }
            if(addMonth == false){
                if(month == 5 || month == 7 || month == 10 || month == 12){
                    addMonth = true;
                    if(day == 0){
                        month -= 1;
                        day = 30;
                    }
                }
            }
            if(addMonth == false){
                addMonth = true;
                if(month == 3){
                    if(day == 0){
                        if(!isLeapYear(year)){
                            month -= 1;
                            day = 28;
                        }
                        if(isLeapYear(year)){
                            month -= 1;
                            day = 29;
                        }

                    }
                }
            }

            if(month == 0){
                year -=1;
                day = 31;
                month = 12;
            }
        }


        String dayS = String.valueOf(day);
        String monthS = String.valueOf(month);
        if(String.valueOf(day).length() == 1){
            dayS = String.valueOf(0) + dayS;
        }
        if(String.valueOf(month).length() == 1){
            monthS = String.valueOf(0) + monthS;
        }

        date = dayS + monthS + String.valueOf(year);
        String dateText = (dayS + "/" + monthS + "/" + String.valueOf(year));

        Log.d("date", String.valueOf(day) + String.valueOf(month) + String.valueOf(year));



        return new String[]{date, dateText};
    }

    public static boolean isLeapYear(int testYear){
        boolean isTrue = false;
        int leap = 2008;

        while(leap < testYear){
            leap += 4;
            if(leap == testYear){
                isTrue = true;
                break;
            }
            if(leap > testYear){
                isTrue = false;
                break;
            }
        }

        while(leap > testYear){
            leap -= 4;
            if(leap == testYear){
                isTrue = true;
                break;
            }
            if(leap < testYear){
                isTrue = false;
                break;
            }
        }

        if(leap == testYear){
            isTrue = true;
        }

        return isTrue;
    }
}
