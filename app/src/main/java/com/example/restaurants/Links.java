package com.example.restaurants;

import org.json.JSONObject;

import java.util.ArrayList;

public   class Links {
  public   static String login = "http://13.235.250.119/v2/login/fetch_result";
  public  static String registration = "http://13.235.250.119/v2/register/fetch_result";
  public static String forgotPassword = " http://13.235.250.119/v2/forgot_password/fetch_result";
  public static String restPassword = "http://13.235.250.119/v2/reset_password/fetch_result";
//  public static String restaurants = "http://13.235.250.119/v2/restaurants/fetch_result/";


  public static String restaurants = "https://run.mocky.io/v3/ee3bc757-cbfd-4994-8a7c-6320f70a90bb";
  public static String menu = "https://run.mocky.io/v3/b3d2892b-e69a-494f-9d10-1feca00f5777";
  public static  String orderHistory = "https://run.mocky.io/v3/45714d4b-2348-4e5f-ac0c-e16f8c58d5ed";

  public static String perfName = "com.example.restaurants";
    public static String perfFav = "com.example.restaurants.favorites";
//    static String login = "";
//    static String login = "";

  public static  ArrayList<JSONObject> cart;
}
