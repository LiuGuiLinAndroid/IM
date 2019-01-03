package com.liuguilin.im.manager;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.liuguilin.im.utils.IMLog;

/**
 * FileName: MapManager
 * Founder: LiuGuiLin
 * Create Date: 2019/1/3 11:12
 * Email: lgl@szokl.com.cn
 * Profile:
 */
public class MapManager {

    private static MapManager mInstance = null;

    private LocationClient mLocationClient = null;
    private LocationListener mLocationListener = new LocationListener();

    private OnLocationResultListener resultListener;

    private MapManager() {

    }

    public static MapManager getInstance() {
        if (mInstance == null) {
            synchronized (MapManager.class) {
                if (mInstance == null) {
                    mInstance = new MapManager();
                }
            }
        }
        return mInstance;
    }

    public void initLocation(Context mContext, OnLocationResultListener listener) {
        resultListener = listener;
        mLocationClient = new LocationClient(mContext.getApplicationContext());
        mLocationClient.registerLocationListener(mLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(0);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setWifiCacheTimeOut(5 * 60 * 1000);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }

    private class LocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            int errorCode = location.getLocType();

            switch (errorCode) {
                case BDLocation.TypeGpsLocation:
                case BDLocation.TypeNetWorkLocation:
                    resultListener.OnLocationSucceess(location.getAddrStr(), latitude, longitude);
                    stopLocation();
                    break;
                default:
                    resultListener.OnLocationFail();
                    break;
            }

        }
    }

    public void startLocation() {
        if (mLocationClient != null) {
            mLocationClient.start();
        }
    }

    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }

    public interface OnLocationResultListener {

        void OnLocationSucceess(String address, double lat, double lon);

        void OnLocationFail();
    }
}
