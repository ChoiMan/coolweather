package com.choiman.coolweather.model;

public class Weather {

  private String city;

  private String cityid;

  private String temp1;

  private String temp2;

  private String weatherDesp;

  private String ptime;

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCityid() {
    return cityid;
  }

  public void setCityid(String cityid) {
    this.cityid = cityid;
  }

  public String getTemp1() {
    return temp1;
  }

  public void setTemp1(String temp1) {
    this.temp1 = temp1;
  }

  public String getTemp2() {
    return temp2;
  }

  public void setTemp2(String temp2) {
    this.temp2 = temp2;
  }

  public String getWeatherDesp() {
    return weatherDesp;
  }

  public void setWeatherDesp(String weatherDesp) {
    this.weatherDesp = weatherDesp;
  }

  public String getPtime() {
    return ptime;
  }

  public void setPtime(String ptime) {
    this.ptime = ptime;
  }

}
