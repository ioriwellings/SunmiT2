package com.sunmi.sunmit2demo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.sunmit2demo.adapter.GvAdapter;
import com.sunmi.sunmit2demo.adapter.MenusAdapter;
import com.sunmi.sunmit2demo.bean.GvBeans;
import com.sunmi.sunmit2demo.bean.MenusBean;
import com.sunmi.sunmit2demo.dialog.AddFruitDialogFragment;
import com.sunmi.sunmit2demo.dialog.PayDialog;
import com.sunmi.sunmit2demo.utils.ResourcesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import sunmi.ds.DSKernel;
import sunmi.ds.callback.IConnectionCallback;
import sunmi.ds.callback.ISendCallback;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static boolean isShowTime;
    private final String TAG = "SUNMI";
    private ListView lvMenus;
    private MenusAdapter menusAdapter;
    private List<MenusBean> menus = new ArrayList<>();
    private GridView gvDrink;
    private GridView gvFruit;
    private GvAdapter drinkAdapter;
    private GvAdapter fruitAdapter;
    private List<GvBeans> mDrinksBean = new ArrayList<>();
    private List<GvBeans> mFruitsBean = new ArrayList<>();
    private TextView tvPrice;
    private TextView btnClear;
    private RelativeLayout rtlEmptyShopcar;
    private LinearLayout llyShopcar;
    private DecimalFormat decimalFormat = new DecimalFormat(".00");
    private Button btnMore;//更多功能
    private Button btnPay;//去付款
    private DSKernel mDSKernel = null;
    private final String DSD_PACKNAME = "sunmi.dsd";
    private long videoId;
    private AddFruitDialogFragment dialogFragment = null;
    private PayDialog payDialog = null;
    private String goods_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menus.clear();
        initView();
        initData();
        initAction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ------------>" + (mDSKernel == null));
        if (mDSKernel != null) {
            mDSKernel.checkConnection();
        } else {
            initSdk();
        }


    }

    private void initSdk() {
        mDSKernel = DSKernel.newInstance();
        mDSKernel.init(this, new IConnectionCallback() {
            @Override
            public void onDisConnect() {

            }

            @Override
            public void onConnected(ConnState state) {
                switch (state) {
                    case AIDL_CONN:
                        break;
                    case VICE_SERVICE_CONN:
                        break;
                    case VICE_APP_CONN:
                        if (menus.size() > 0) {
                            float price = 0.00f;
                            for (MenusBean bean1 : menus) {
                                price = price + Float.parseFloat(bean1.getMoney().substring(1));
                            }
                            buildMenuJson(menus, decimalFormat.format(price));
                        } else {
                            sendVideo();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onPause() { //如果存在activity跳转，需要做清理操作
        super.onPause();
        if (null != mDSKernel) {
            mDSKernel.onDestroy();
            mDSKernel = null;
        }
    }

    private void initView() {
        lvMenus = (ListView) findViewById(R.id.lv_menus);
        gvDrink = (GridView) findViewById(R.id.gv_drinks);
        gvFruit = (GridView) findViewById(R.id.gv_fruits);
        tvPrice = (TextView) findViewById(R.id.main_tv_price);
        btnClear = (TextView) findViewById(R.id.main_btn_clear);
        llyShopcar = (LinearLayout) findViewById(R.id.lly_shopcar);
        rtlEmptyShopcar = (RelativeLayout) findViewById(R.id.rtl_empty_shopcar);
        btnMore = (Button) findViewById(R.id.main_btn_more);
        btnPay = (Button) findViewById(R.id.main_btn_pay);
        gvDrink.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gvFruit.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initAction() {
        gvDrink.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenusBean bean = new MenusBean();
                bean.setId("" + (menus.size() + 1));
                bean.setMoney(mDrinksBean.get(position).getPrice());
                bean.setName(mDrinksBean.get(position).getName());
                menus.add(bean);
                float price = 0.00f;
                for (MenusBean bean1 : menus) {
                    price = price + Float.parseFloat(bean1.getMoney().substring(1));
                }
                buildMenuJson(menus, decimalFormat.format(price));
                tvPrice.setText(ResourcesUtils.getString(MainActivity.this,R.string.units_money_units) + decimalFormat.format(price));
                menusAdapter.update(menus);
                llyShopcar.setVisibility(View.VISIBLE);
                rtlEmptyShopcar.setVisibility(View.GONE);
            }
        });

        gvFruit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("FLAG", position);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(), "");
            }
        });
        btnClear.setOnClickListener(this);
        btnMore.setOnClickListener(this);
        btnPay.setOnClickListener(this);
    }

    private void initData() {
        initDrinks();
        initFruits();
        drinkAdapter = new GvAdapter(this, mDrinksBean, 1);
        gvDrink.setAdapter(drinkAdapter);
        fruitAdapter = new GvAdapter(this, mFruitsBean, 2);
        gvFruit.setAdapter(fruitAdapter);
        menus.clear();
        tvPrice.setText(ResourcesUtils.getString(MainActivity.this,R.string.units_money_units)+"0.00");
        llyShopcar.setVisibility(View.GONE);
        rtlEmptyShopcar.setVisibility(View.VISIBLE);
        menusAdapter = new MenusAdapter(this, menus);
        lvMenus.setAdapter(menusAdapter);
        dialogFragment = new AddFruitDialogFragment();
        dialogFragment.setListener(new AddFruitDialogFragment.AddListener() {
            @Override
            public void onAddResult(String total, String name) {
                MenusBean bean = new MenusBean();
                bean.setId("" + (menus.size() + 1));
                bean.setMoney(ResourcesUtils.getString(MainActivity.this,R.string.units_money_units) + total);
                bean.setName(name);
                menus.add(bean);
                float price = 0.00f;
                for (MenusBean bean1 : menus) {
                    price = price + Float.parseFloat(bean1.getMoney().substring(1));
                }
                buildMenuJson(menus, decimalFormat.format(price));
                tvPrice.setText(ResourcesUtils.getString(MainActivity.this,R.string.units_money_units) + decimalFormat.format(price));
                menusAdapter.update(menus);
                llyShopcar.setVisibility(View.VISIBLE);
                rtlEmptyShopcar.setVisibility(View.GONE);
            }
        });
        payDialog = new PayDialog();
        payDialog.setCompleteListener(new PayDialog.OnCompleteListener() {
            @Override
            public void onCancel() {
                //副屏显示付款金额

            }
            @Override
            public void onSuccess() {

            }
            @Override
            public void onComplete() {
                //副屏显示客观您慢走
            }
        });
    }

    private void initFruits() {
        mFruitsBean.clear();
        GvBeans one = new GvBeans();
        one.setImgId(R.drawable.apple);
        one.setName(ResourcesUtils.getString(this, R.string.goods_apple));
        one.setPrice("8.98" + ResourcesUtils.getString(this, R.string.units_money));

        GvBeans two = new GvBeans();
        two.setImgId(R.drawable.pears);
        two.setName(ResourcesUtils.getString(this, R.string.goods_pear));
        two.setPrice("4.58" + ResourcesUtils.getString(this, R.string.units_money));

        GvBeans three = new GvBeans();
        three.setImgId(R.drawable.banana);
        three.setName(ResourcesUtils.getString(this, R.string.goods_banana));
        three.setPrice("2.98" + ResourcesUtils.getString(this, R.string.units_money));

        GvBeans four = new GvBeans();
        four.setImgId(R.drawable.pitaya);
        four.setName(ResourcesUtils.getString(this, R.string.goods_pitaya));
        four.setPrice("18.59" + ResourcesUtils.getString(this, R.string.units_money));
        mFruitsBean.add(one);
        mFruitsBean.add(two);
        mFruitsBean.add(three);
        mFruitsBean.add(four);

    }

    private void initDrinks() {
        mDrinksBean.clear();
        GvBeans one = new GvBeans();
        one.setImgId(R.drawable.coco);
        one.setName(ResourcesUtils.getString(this, R.string.goods_coke));
        one.setPrice("5.00" + ResourcesUtils.getString(this, R.string.units_money));

        GvBeans two = new GvBeans();
        two.setImgId(R.drawable.sprit);
        two.setName(ResourcesUtils.getString(this, R.string.goods_sprite));
        two.setPrice("4.00" + ResourcesUtils.getString(this, R.string.units_money));

        GvBeans three = new GvBeans();
        three.setImgId(R.drawable.redbull);
        three.setName(ResourcesUtils.getString(this, R.string.goods_red_bull));
        three.setPrice("6.00" + ResourcesUtils.getString(this, R.string.units_money));

        GvBeans four = new GvBeans();
        four.setImgId(R.drawable.oringejuice);
        four.setName(ResourcesUtils.getString(this, R.string.goods_orange_juice));
        four.setPrice("5.50" + ResourcesUtils.getString(this, R.string.units_money));
        mDrinksBean.add(one);
        mDrinksBean.add(two);
        mDrinksBean.add(three);
        mDrinksBean.add(four);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_clear:
                llyShopcar.setVisibility(View.GONE);
                rtlEmptyShopcar.setVisibility(View.VISIBLE);
                menus.clear();
                tvPrice.setText(ResourcesUtils.getString(MainActivity.this,R.string.units_money_units)+"0.00");
                menusAdapter.update(menus);
                sendVideo();
                break;
            case R.id.main_btn_more:
                Intent intent = new Intent(MainActivity.this, MoreActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btn_pay:
                Bundle bundle = new Bundle();
                bundle.putString("MONEY", tvPrice.getText().toString().substring(1));
                bundle.putString("GOODS", goods_data);
                payDialog.setArguments(bundle);
                payDialog.show(getSupportFragmentManager(), "gll");
                break;
            default:
                break;
        }
    }

    private void sendVideo() {
        videoId = mDSKernel.sendFile(DSD_PACKNAME, Environment.getExternalStorageDirectory().getPath() + "/video_01.mp4", new ISendCallback() {
            @Override
            public void onSendSuccess(long l) {
                playvideo(l);
            }

            @Override
            public void onSendFail(int i, String s) {
                Log.d("SUNMI", "发送单个文件视频文件失败 ------------>" + s);
            }

            @Override
            public void onSendProcess(final long l, final long l1) {
            }
        });
    }

    private void playvideo(long taskID) {
        Log.d("SUNMI", "playvideo: ------------>");
        JSONObject json = new JSONObject();
        try {
            json.put("dataModel", "VIDEO");
            json.put("data", "true"); //true代表视频续播
            mDSKernel.sendCMD(DSD_PACKNAME, json.toString(), taskID, new ISendCallback() {
                @Override
                public void onSendSuccess(long taskId) {
                }

                @Override
                public void onSendFail(int errorId, String errorInfo) {
                }

                @Override
                public void onSendProcess(long totle, long sended) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void buildMenuJson(List<MenusBean> menus, String price) {
        try {
            JSONObject data = new JSONObject();
            data.put("title", "Sunmi " + ResourcesUtils.getString(this, R.string.menus_title));
            JSONObject head = new JSONObject();
            head.put("param1", ResourcesUtils.getString(this, R.string.menus_number));
            head.put("param2", ResourcesUtils.getString(this, R.string.menus_goods_name));
            head.put("param3", ResourcesUtils.getString(this, R.string.menus_unit_price));
            data.put("head", head);
            data.put("flag", "true");
            JSONArray list = new JSONArray();
            for (int i = 0; i < menus.size(); i++) {
                JSONObject listItem = new JSONObject();
                listItem.put("param1", "" + (i + 1));
                listItem.put("param2", menus.get(i).getName());
                listItem.put("param3", menus.get(i).getMoney());
                list.put(listItem);
            }
            data.put("list", list);
            JSONArray KVPList = new JSONArray();
            JSONObject KVPListOne = new JSONObject();
            KVPListOne.put("name", ResourcesUtils.getString(this, R.string.shop_car_total)+" ");
            KVPListOne.put("value", price);
            JSONObject KVPListTwo = new JSONObject();
            KVPListTwo.put("name", ResourcesUtils.getString(this, R.string.shop_car_offer)+" ");
            KVPListTwo.put("value", "0.00");
            JSONObject KVPListThree = new JSONObject();
            KVPListThree.put("name", ResourcesUtils.getString(this, R.string.shop_car_number)+" ");
            KVPListThree.put("value", "" + menus.size());
            JSONObject KVPListFour = new JSONObject();
            KVPListFour.put("name", ResourcesUtils.getString(this, R.string.shop_car_receivable)+" ");
            KVPListFour.put("value", price);
            KVPList.put(0, KVPListOne);
            KVPList.put(1, KVPListTwo);
            KVPList.put(2, KVPListThree);
            KVPList.put(3, KVPListFour);
            data.put("KVPList", KVPList);
            goods_data=data.toString();
            Log.d("HHHH", "onClick: ---------->" + data.toString());
            try {
                JSONObject json = new JSONObject();
                json.put("dataModel", "SHOW_VIDEO_LIST");
                json.put("data", data.toString());
                mDSKernel.sendCMD(DSD_PACKNAME, json.toString(), videoId, new ISendCallback() {
                    @Override
                    public void onSendSuccess(long taskId) {
                    }

                    @Override
                    public void onSendFail(int errorId, String errorInfo) {
                    }

                    @Override
                    public void onSendProcess(long totle, long sended) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //退出时的时间
    private long mExitTime;
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(MainActivity.this, ResourcesUtils.getString(this,R.string.tips_exit), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
