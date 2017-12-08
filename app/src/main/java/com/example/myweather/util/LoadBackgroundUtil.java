package com.example.myweather.util;

import android.text.TextUtils;

import com.example.myweather.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 背景图片加载，根据省份名称拿到对应的Drawable的id
 */

public class LoadBackgroundUtil {

    private static Map<String, Integer> drawableMap;
    // 初始化map
    private static void initMap() {
        drawableMap = new HashMap<>();
        drawableMap.put("北京", R.drawable.beijing);
        drawableMap.put("安徽", R.drawable.anhui);
        drawableMap.put("澳门", R.drawable.aomen);
        drawableMap.put("重庆", R.drawable.chongqing);
        drawableMap.put("福建", R.drawable.fujian);
        drawableMap.put("甘肃", R.drawable.gansu);
        drawableMap.put("广东", R.drawable.guangdong);
        drawableMap.put("广西", R.drawable.guangxi);
        drawableMap.put("贵州", R.drawable.guizhou);
        drawableMap.put("海南", R.drawable.hainan);
        drawableMap.put("河北", R.drawable.hebei);
        drawableMap.put("黑龙江", R.drawable.heilongjiang);
        drawableMap.put("河南", R.drawable.henan);
        drawableMap.put("湖北", R.drawable.hubei);
        drawableMap.put("湖南", R.drawable.hunan);
        drawableMap.put("江苏", R.drawable.jiangsu);
        drawableMap.put("江西", R.drawable.jiangxi);
        drawableMap.put("吉林", R.drawable.jilin);
        drawableMap.put("内蒙古", R.drawable.neimenggu);
        drawableMap.put("宁夏", R.drawable.ningxia);
        drawableMap.put("青海", R.drawable.qinghai);
        drawableMap.put("山东", R.drawable.shandong);
        drawableMap.put("上海", R.drawable.shanghai);
        drawableMap.put("山西", R.drawable.shanxi);
        drawableMap.put("沈阳", R.drawable.shenyang);
        drawableMap.put("四川", R.drawable.sichuan);
        drawableMap.put("台湾", R.drawable.taiwan);
        drawableMap.put("天津", R.drawable.tianjin);
        drawableMap.put("西安", R.drawable.xian);
        drawableMap.put("香港", R.drawable.xianggang);
        drawableMap.put("新疆", R.drawable.xinjiang);
        drawableMap.put("西藏", R.drawable.xizang);
        drawableMap.put("云南", R.drawable.yunnan);
        drawableMap.put("浙江", R.drawable.zhejiang);
        drawableMap.put("default", R.drawable.bg_src_tianjin);
    }
    // 根据省份名称找到对应的drawable的id
    public static int findDrawableId(String province) {
        initMap();
        int id = 0;
        if (!TextUtils.isEmpty(province)) {
            id = drawableMap.get(province);
            // 找不到，使用默认的背景
            if (id == 0) {
                id = drawableMap.get("default");
            }
        } else {
            id = drawableMap.get("default");
        }
        return id;
    }
}
