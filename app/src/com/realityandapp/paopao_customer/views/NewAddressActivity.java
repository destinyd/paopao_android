package com.realityandapp.paopao_customer.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.*;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.mindpin.android.loadingview.LoadingView;
import com.realityandapp.paopao_customer.Constants;
import com.realityandapp.paopao_customer.PaopaoCustomerApplication;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.http.Address;
import com.realityandapp.paopao_customer.views.adapter.SuggestionResultsAdapter;
import com.realityandapp.paopao_customer.views.base.PaopaoBaseActivity;
import com.realityandapp.paopao_customer.widget.FontAwesomeButton;
import roboguice.inject.InjectView;

/**
 * Created by dd on 14-9-24.
 */
public class NewAddressActivity extends PaopaoBaseActivity implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener, AdapterView.OnItemClickListener, TextWatcher {
    @InjectView(R.id.fa_btn_ok)
    FontAwesomeButton fa_btn_ok;
    @InjectView(R.id.actv_address)
    AutoCompleteTextView actv_address;
    @InjectView(R.id.bmapView)
    MapView bmapView;
    @InjectView(R.id.loading_view)
    LoadingView loading_view;

    private Address address;
    private BaiduMap mBaiduMap;
    private PoiSearch mPoiSearch;
    //定义Maker坐标点 柳州市 109.422402, 24.329053
    private static LatLng point_liuzhou = new LatLng(24.329053, 109.422402);
    private BitmapDescriptor mCurrentMarker;
    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;
    private double latitude = point_liuzhou.latitude;
    private double longitude = point_liuzhou.longitude;
    private final static String city = "柳州市";
    private SuggestionSearch mSuggestionSearch;
    private SuggestionResultsAdapter sugAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // todo change to application if use baidu map other place
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.new_address);
        init();
    }

    private void init() {
        setTitle("新建地址-第一步");
        init_view();
        bind_views();
    }

    private void init_view() {
        mBaiduMap = bmapView.getMap();

        init_poi();
        build_and_add_default_marker();
        map_center_to_default();
        location_user();
    }

    private void location_user() {
        init_location();
    }

    @Override
    protected void onStop() {
        mLocationClient.stop();
        super.onStop();
    }

    private void init_location() {
        // todo 不知为何没有用
        mLocationClient = ((PaopaoCustomerApplication) getApplication()).getLocationClient();
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//一次定位,使用高精度
        option.setCoorType("bd09ll"); //直接获取百度坐标
//        option.setCoorType("gcj02"); //
        option.setScanSpan(1000);// 定位时间间隔
//        option.setIsNeedAddress(true); // 反向获取地址？
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private void map_center_to_default() {
        map_center_to(point_liuzhou);
    }

    private void init_poi() {
        // 初始化搜索模块，注册事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        sugAdapter = new SuggestionResultsAdapter(this,
                R.layout.baidu_suggestion_dropdown_item);
        actv_address.addTextChangedListener(this);
        // remove it dropdown item click will not work
        actv_address.setOnItemClickListener(this);

        actv_address.setAdapter(sugAdapter);
    }

    private void map_center_to(LatLng lat_lng) {
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(lat_lng)
                .zoom(15)
                .build();

        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    private void build_and_add_default_marker() {
        //构建Marker图标
        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point_liuzhou)
                .icon(mCurrentMarker);
//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    private void bind_views() {
        fa_btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fa_btn_ok:
                create_address_and_go_to_fill_contact();
                break;
            default:
                super.onClick(view);
        }
    }

    private void create_address_and_go_to_fill_contact() {
        create_address();
        go_to_fill_contact();
    }

    private void create_address() {
        address = new Address();
        address.set_address(actv_address.getText().toString());
        address.set_latitude(latitude);
        address.set_longitude(longitude);
//        address.set_coordinates(latitude, longitude);
    }

    private void go_to_fill_contact() {
        Intent intent = new Intent(this, FillContactActivity.class);
        intent.putExtra(Constants.Extra.ADDRESS, address);
        startActivityForResult(intent, Constants.Request.ADDRESS);
    }

    private void return_address(Address address) {
        Intent intent = new Intent();
        intent.putExtra(Constants.Extra.ADDRESS, address);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        bmapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        bmapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        bmapView.onDestroy();
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        super.onDestroy();
    }

    // 获取地址下拉建议
    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            System.out.println("maybe getAllSuggestions null");
            return;
        }
        reset_adapter(res);
    }

    private void reset_adapter(SuggestionResult res) {
        sugAdapter.clear();
        // key wrong? or something others
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            // 有district 也就是分区的可能是车站环线
            if (city.equals(info.city) && !TextUtils.isEmpty(info.district)) {
                sugAdapter.add(info);
            }
        }
        sugAdapter.notifyDataSetChanged();
    }

    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            to(result);
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(NewAddressActivity.this, strInfo, Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void to(PoiResult result) {
        if (result.getAllPoi().isEmpty()) {
            System.out.println("result.getAllPoi().isEmpty()");
            return;
        }
        // todo 选择更加合适的项？
        LatLng location = result.getAllPoi().get(0).location;
        latitude = location.latitude;
        longitude = location.longitude;
        marker_to(location);
        map_center_to(location);
    }

    private void marker_to(LatLng location) {
        mBaiduMap.clear();
        OverlayOptions option = new MarkerOptions()
                .position(location)
                .icon(mCurrentMarker);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    // 点击选项获取详细信息？
    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {
        System.out.println("onGetPoiDetailResult:" + result);
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(NewAddressActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
            map_center_to(result.getLocation());
            Toast.makeText(NewAddressActivity.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                        .city(city)
                        .keyword(actv_address.getText().toString())
//                .pageNum(load_Index)
        );
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence cs, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable cs) {
        /**
         * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
         */
        if (cs.length() <= 0) {
            return;
        }
        mSuggestionSearch
                .requestSuggestion((new SuggestionSearchOption())
                        .keyword(cs.toString()).city(city));
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            switch (location.getLocType()) {
                case 61: // GPS定位结果
                    map_center_to(location);
                    break;
                case 65: // 定位缓存的结果。
                    map_center_to(location);
                    break;
                case 66:// 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
                    map_center_to(location);
                    break;
                case 68:// 网络连接失败时，查找本地离线定位时对应的返回结果
                    map_center_to(location);
                    break;
                case 161:// 表示网络定位结果
                    map_center_to(location);
                    break;
            }
        }
    }

    private void map_center_to(BDLocation location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        System.out.println("addr:" + location.getAddrStr());
        map_center_to(new LatLng(latitude, longitude));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == Constants.Request.ADDRESS && null != data){
            address = (Address) data.getSerializableExtra(Constants.Extra.ADDRESS);
            return_address(address);
        }
    }
}