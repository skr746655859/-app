package com.salhe.antibigdata.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.salhe.antibigdata.data.dao.ProductsDao;
import com.salhe.antibigdata.data.pojo.DataState;
import com.salhe.antibigdata.data.pojo.Product;
import com.salhe.antibigdata.utils.SnowFlake;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@SuppressLint("NewApi")
@AndroidEntryPoint
public class CollectProductAccessibilityService extends AccessibilityService {
    private static final String TAG = "CollectDataService";
    boolean matching = false;//保证物品名称和价格配套出现
    String data = "";//打印输出
    String price_now = "";//保存现价
    String product_name = "";
    Date product_time;
    int hold = 1;//格式1是否定位失败标志
    int Hold = 0;//防止在一个页面内重复发送

    @Inject
    public ProductsDao productsDao;
    @Inject
    public SnowFlake snowFlake;

    @Override
    protected void onServiceConnected() {
    }

    @SuppressLint("NewApi")
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo rowNode = getRootInActiveWindow();
        if (rowNode == null) {
            Log.d(TAG, "noteInfo is　null");
            return;
        } else {
            Log.d(TAG, "包名:" + rowNode.getPackageName() + ",当前页面的孩子个数:" + rowNode.getChildCount());
            if (rowNode.getPackageName().toString().equals("com.taobao.taobao")) {//淘宝
                //格式1
                if (rowNode.getChildCount() > 5) {//定位目标页面
                    if (rowNode.getChild(rowNode.getChildCount() - 3).getChildCount() > 1 && rowNode.getChild(rowNode.getChildCount() - 3).getChild(1) != null) {
                        if (rowNode.getChild(rowNode.getChildCount() - 3).getChild(1).getText() != null && rowNode.getChild(rowNode.getChildCount() - 3).getChild(1).getText().toString().equals("收藏")) {
                            if (rowNode.getChild(rowNode.getChildCount() - 4).getChildCount() > 1 && rowNode.getChild(rowNode.getChildCount() - 4).getChild(1) != null) {
                                if (rowNode.getChild(rowNode.getChildCount() - 4).getChild(1).getText() != null && rowNode.getChild(rowNode.getChildCount() - 4).getChild(1).getText().toString().equals("客服")) {
                                    if (rowNode.getChild(rowNode.getChildCount() - 5).getChildCount() > 1 && rowNode.getChild(rowNode.getChildCount() - 5).getChild(1) != null) {
                                        if (rowNode.getChild(rowNode.getChildCount() - 5).getChild(1).getText() != null && rowNode.getChild(rowNode.getChildCount() - 5).getChild(1).getText().toString().equals("店铺")) {
                                            Log.d(TAG, "成功定位到目标界面（格式1）");
                                            hold = 1;//拒绝格式2定位
                                            if (Hold == 0) {//当前页面未发过数据
                                                Log.d(TAG, "hold=" + hold);
                                                recycle(rowNode, rowNode, "");
                                            }
                                        } else hold = 0;//定位失败
                                    } else hold = 0;//定位失败
                                } else hold = 0;//定位失败
                            } else hold = 0;//定位失败
                        } else hold = 0;//定位失败
                    } else hold = 0;//定位失败
                } else hold = 0;//定位失败

                if (hold == 0) {//格式1定位失败了
                    //格式2(淘宝有毒)
                    if (rowNode.getChildCount() > 5) {//定位目标页面
                        if (rowNode.getChild(rowNode.getChildCount() - 3).getContentDescription() != null && rowNode.getChild(rowNode.getChildCount() - 3).getContentDescription().toString().equals("收藏")) {
                            if (rowNode.getChild(rowNode.getChildCount() - 4).getContentDescription() != null && rowNode.getChild(rowNode.getChildCount() - 4).getContentDescription().toString().equals("客服")) {
                                if (rowNode.getChild(rowNode.getChildCount() - 5).getContentDescription() != null && rowNode.getChild(rowNode.getChildCount() - 5).getContentDescription().toString().equals("店铺")) {
                                    Log.d(TAG, "成功定位到目标界面（格式2）");
                                    if (Hold == 0) {//当前页面未发过数据
                                        Log.d(TAG, "hold=" + hold);
                                        recycle(rowNode, rowNode, "");
                                    }
                                } else Hold = 0;//定位失败，重新开始捕捉
                            } else Hold = 0;//定位失败，重新开始捕捉
                        } else Hold = 0;//定位失败，重新开始捕捉
                    } else Hold = 0;//定位失败，重新开始捕捉
                }
                //全局检测（调试代码）
                //recycle(rowNode,rowNode,"");
            } else {
                Hold = 0;//退出淘宝可以重新进行捕获
            }
        }
    }

    public void recycle(AccessibilityNodeInfo info, AccessibilityNodeInfo Rootinfo, String msg) {
        if (info.getChildCount() == 0) {
            if (info.getText() != null) {
                if (info.getText().toString().equals("￥")) {//找到价格
//                    if (judge == 1) {//第二次索引到价格
//                        judge=2;//过滤大于2个￥
//                        Log.d(TAG,"第二次访问编号为:" + msg);
//                        char[] stringArr = msg.toCharArray();
//                        stringArr[stringArr.length - 1] = (char) ((Integer.parseInt(String.valueOf(stringArr[stringArr.length - 1])) + 1) + '0');//向后移一位
//                        Log.d(TAG,"第二次新访问编号为:" + String.valueOf(stringArr));
//                        price_pre=getDate(Rootinfo, String.valueOf(stringArr));
//                        data = data + "\n商品原价格：￥" +price_pre+";";
//                    } else {//第一次索引到价格
//                        judge = 1;//现价已经出现
//                        matching=true;//保证价格名称配套出现
//                        Log.d(TAG,"第一次访问编号为:" + msg);
//                        char[] stringArr = msg.toCharArray();
//                        stringArr[stringArr.length - 1] = (char) ((Integer.parseInt(String.valueOf(stringArr[stringArr.length - 1])) + 1) + '0');//向后移一位
//                        Log.d(TAG,"第一次新访问编号为:" + String.valueOf(stringArr));
//                        price_nex=getDate(Rootinfo, String.valueOf(stringArr));
//                        data = "\n商品现价格：￥" + price_nex+";";
//                    }
                    if (!matching) {//第一次索引到价格
                        matching = true;//保证价格名称配套出现
                        Log.d(TAG, "第一次访问编号为:" + msg);
                        char[] stringArr = msg.toCharArray();
                        stringArr[stringArr.length - 1] = (char) ((Integer.parseInt(String.valueOf(stringArr[stringArr.length - 1])) + 1) + '0');//向后移一位
                        Log.d(TAG, "第一次新访问编号为:" + String.valueOf(stringArr));
                        price_now = getDate(Rootinfo, String.valueOf(stringArr));
                        data = "\n商品价格：￥" + price_now + ";";
                    }
                }
                if (info.isLongClickable() && matching) {//找到物品名称
                    Log.d(TAG, "第三次访问编号为:" + msg);
                    //生成json对象
                    JSONArray array = new JSONArray();
                    JSONObject object = new JSONObject();
                    JSONObject obj = new JSONObject();
                    try {
                        object.put("price_nex", price_now);//现价
                        product_name = info.getText().toString();
                        data = data + "\n商品名称：" + product_name + ";";
                        object.put("name", product_name);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");// HH:mm:ss
                        Date date = new Date(System.currentTimeMillis());
                        product_time = date;
                        data = data + "\n捕获时间:" + simpleDateFormat.format(date) + ";";
                        object.put("time", simpleDateFormat.format(date));

                        String m_szAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        data = data + "\n设备名：" + m_szAndroidID;
                        object.put("equipment", m_szAndroidID);

                        Log.d(TAG, "找到商品 " + product_name + "￥" + price_now + " " + simpleDateFormat.format(date));
                        String[] prices = price_now.split("-");
                        Product product;
                        if (prices.length == 2) {
                            product = new Product(
                                    snowFlake.nextId(),
                                    product_name,
                                    Float.valueOf(prices[0]),
                                    Float.valueOf(prices[1]),
                                    null,
                                    "taobao",
                                    "",
                                    product_time,
                                    "android",
                                    m_szAndroidID,
                                    DataState.wait
                            );
                        } else {
                            product = new Product(
                                    snowFlake.nextId(),
                                    product_name,
                                    Float.valueOf(price_now),
                                    null,
                                    null,
                                    "taobao",
                                    "",
                                    product_time,
                                    "android",
                                    m_szAndroidID,
                                    DataState.wait
                            );
                        }
                        productsDao.insertAll(product);

                        array.put(object);
                        obj.put("Capture", array);//需要上传的json对象
                    } catch (Exception e) {
                        Log.d(TAG, "出错了" + e.getMessage());
                    }

                    Log.d(TAG, obj.toString());
                    Log.d(TAG, data);
                    // writeTxtToFile(data, "/sdcard/DataGet/", "log.txt");

                    matching = false;
                    price_now = "";
                    Hold = 1;//防止在一个页面内重复发送
                }
            } else {
                Log.d(TAG, "无意义控件");
            }

//            全局检测（调试代码）
//            Log.d(TAG,"捕获控件--------------------------------------------");
//            Log.d(TAG,"child widget----------------------------" + info.getClassName());
//            Log.d(TAG,"showDialog:" + info.canOpenPopup());
//            Log.d(TAG,"Text：" + info.getText());
//            Log.d(TAG,"windowId:" + info.getWindowId());
//            Log.d(TAG,"isLongClickable:" + info.isLongClickable());
//            Log.d(TAG,"getClassName:" + info.getClassName());
//            Log.d(TAG,"getClassName:" + info.getClassName());
//            Log.d(TAG,"getViewIdResourceName:" + info.getViewIdResourceName());
//            Log.d(TAG,"getContentDescription:" + info.getContentDescription());
//            Log.d(TAG,"访问编号为:" + msg);
        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    String a = msg + i;
                    recycle(info.getChild(i), Rootinfo, a);
                }
            }
        }
    }

    // 将字符串写入到文本文件中
    public void writeTxtToFile(String strcontent, String filePath, String fileName) {
        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }


    public String getDate(AccessibilityNodeInfo info, String msg) {//根据字符串进行索引
        for (int i = 0; i < msg.length(); i++) {
            info = info.getChild(Integer.parseInt(String.valueOf(msg.charAt(i))));
        }
//        Log.d(TAG,"Text：" + info.getText());
//        Log.d(TAG,"windowId:" + info.getWindowId());
//        Log.d(TAG,"isLongClickable:" + info.isLongClickable());
//        Log.d(TAG,"getClassName:" + info.getClassName());
//        Log.d(TAG,"getClassName:" + info.getClassName());
//        Log.d(TAG,"getViewIdResourceName:" + info.getViewIdResourceName());
//        Log.d(TAG,"getContentDescription:" + info.getContentDescription());
        return info.getText().toString();
    }


    @Override
    public void onInterrupt() {

    }

}