package com.choiman.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import com.choiman.coolweather.model.City;
import com.choiman.coolweather.model.County;
import com.choiman.coolweather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {

  /**
   * 数据名
   */
  public static final String DB_NAME = "cool_weather";

  /**
   * 数据库版本
   */
  public static final int VERSION = 1;

  private static CoolWeatherDB coolWeatherDB;

  private SQLiteDatabase db;

  /**
   * 构造方法私有化
   */
  private CoolWeatherDB (Context context) {
    CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
    db = dbHelper.getWritableDatabase();
  }

  /**
   * 获取CoolWeatherDb实例
   */
  public synchronized static CoolWeatherDB getInstance(Context context) {
    if (coolWeatherDB == null) {
      coolWeatherDB = new CoolWeatherDB(context);
    }
    return coolWeatherDB;
  }

  /**
   * 存储Province实例到数据库
   */
  public void saveProvince(Province province) {
    if (province != null) {
      ContentValues contentValues = new ContentValues();
      contentValues.put("province_name", province.getProvinceCode());
      contentValues.put("province_code", province.getProvinceCode());
      db.insert("Province", null, contentValues);
    }
  }

  /**
   * 从数据库读取全国所有的省份信息
   */
  public List<Province> loadProvinces() {
    List<Province> list = new ArrayList<Province>();
    Cursor cursor = db.query("Province", null, null, null, null, null, null);
    if (cursor.moveToFirst()) {
      do {
        Province province = new Province();
        province.setId(cursor.getInt(cursor.getColumnIndex("id")));
        province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
        province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
        list.add(province);
      } while (cursor.moveToNext());
    }
    return list;
  }

  /**
   * 将city实例存储到数据库
   * @param city
   */
  public void saveCity (City city) {
    if (city != null) {
      ContentValues contentValues = new ContentValues();
      contentValues.put("city_name", city.getCityName());
      contentValues.put("city_code", city.getCityCode());
      contentValues.put("province_id", city.getProvinceId());
      db.insert("City", null, contentValues);
    }
  }

  /**
   * 从数据库读取某省下所有的城市信息
   */
  public List<City> loadCities(int provinceId) {
    List<City> list = new ArrayList<City>();
    Cursor cursor = db.query("City", null, "province_id = ?", new String[] {String.valueOf(provinceId)}, null, null, null);
    if (cursor.moveToFirst()) {
      do {
        City city = new City();
        city.setId(cursor.getInt(cursor.getColumnIndex("id")));
        city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
        city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
        city.setProvinceId(provinceId);
        list.add(city);
      } while (cursor.moveToNext());
    }
    return list;
  }

  /**
   * 将County实例存储到数据库
   */
  public void saveCounty(County county) {
    if (county != null) {
      ContentValues contentValues = new ContentValues();
      contentValues.put("county_name", county.getCountyName());
      contentValues.put("county_code", county.getCountyCode());
      contentValues.put("city_id", county.getCityId());
      db.insert("County", null, contentValues);
    }
  }

  /**
   * 从数据库读取某城市下下所有的县信息
   */
  public List<County> loadCounties(int cityId) {
    List<County> list = new ArrayList<County>();
    Cursor cursor = db.query("County", null, "cityId = ?", new String[] {String.valueOf(cityId)}, null, null, null);
    if (cursor.moveToFirst()) {
      do {
        County county = new County();
        county.setId(cursor.getInt(cursor.getColumnIndex("id")));
        county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
        county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
        county.setCityId(cityId);
        list.add(county);
      } while (cursor.moveToNext());
    }
    return list;
  }
}
