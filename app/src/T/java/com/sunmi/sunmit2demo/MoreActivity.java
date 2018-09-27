package com.sunmi.sunmit2demo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sunmi.sunmit2demo.databinding.ActivityMoreBinding;
import com.sunmi.sunmit2demo.utils.CommunicateManager;
import com.sunmi.sunmit2demo.utils.ResourcesUtils;
import com.sunmi.sunmit2demo.utils.SharePreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sunmi.ds.DSKernel;
import sunmi.ds.SF;
import sunmi.ds.callback.ICheckFileCallback;
import sunmi.ds.callback.IConnectionCallback;
import sunmi.ds.callback.ISendCallback;
import sunmi.ds.callback.ISendFilesCallback;
import sunmi.ds.callback.QueryCallback;
import sunmi.ds.data.DSData;
import sunmi.ds.data.DataPacket;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class MoreActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "SUNMI";
    private DSKernel mDSKernel = null;
    private ActivityMoreBinding binding;
    private String divide = "------------------------------------------------------------------------ end ------------------------------------------------------------------------";
    private final String DSD_PACKNAME = "sunmi.dsd";

    private IWoyouService woyouService;
    /**
     * 轮播图集合
     * slide collection
     */
    private List<String> imgs = new ArrayList<>();
    private List<String> products = new ArrayList<>();
    private List<String> prices = new ArrayList<>();
    private MyHandler myHandler;
    private CommunicateManager manager = CommunicateManager.getInstance(); //通信文件缓存管理

    private IConnectionCallback mIConnectionCallback = new IConnectionCallback() {
        @Override
        public void onDisConnect() {
//            Message message = new Message();
//            message.what = 1;
//            message.obj = "与远程服务连接中断";
//            myHandler.sendMessage(message);
            append(ResourcesUtils.getString(MoreActivity.this, R.string.service_stop) + "\n\n" + divide + "\n\n");
        }

        @Override
        public void onConnected(ConnState state) {
//            Message message = new Message();
//            message.what = 1;
            switch (state) {
                case AIDL_CONN:
//                    message.obj = "与远程服务绑定成功";
                    append(ResourcesUtils.getString(MoreActivity.this, R.string.service_success) + "\n");
                    break;
                case VICE_SERVICE_CONN:
//                    message.obj = "与副屏服务通讯正常";
                    append(ResourcesUtils.getString(MoreActivity.this, R.string.service_second) + "\n");
                    break;
                case VICE_APP_CONN:
//                    message.obj = "与副屏app通讯正常";
                    append(ResourcesUtils.getString(MoreActivity.this, R.string.service_second_app) + "\n\n" + divide + "\n\n");
                    break;
                default:
                    break;
            }
//            myHandler.sendMessage(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_more);
        myHandler = new MyHandler(this);
        initAction();
        initData();
        connectPrintService();
    }

    private void connectPrintService() {
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

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
        mDSKernel.init(this, mIConnectionCallback);
        manager.init(this, mDSKernel);
    }


    private void initAction() {
        binding.btnShowText.setOnClickListener(this);
        binding.btnShowTextCode.setOnClickListener(this);
        binding.btnShowImg.setOnClickListener(this);
        binding.btnShowPhp.setOnClickListener(this);
        binding.btnShowVideo.setOnClickListener(this);
        binding.btnShowVideos.setOnClickListener(this);
        binding.btnShowMenu.setOnClickListener(this);
        binding.btnShowMenuImg.setOnClickListener(this);
        binding.btnShowMenuVideos.setOnClickListener(this);
        binding.btnShowMenuImgs.setOnClickListener(this);
        binding.btnAcquireSubinfoMain.setOnClickListener(this);
        binding.btnAcquireSubinfoSub.setOnClickListener(this);
        binding.btnClearSubPath.setOnClickListener(this);
        binding.btnClearSubFile.setOnClickListener(this);
        binding.btnAcquireSubPathSize.setOnClickListener(this);
        binding.btnClearSubChche.setOnClickListener(this);
        binding.btnOpenSubSettting.setOnClickListener(this);
        binding.btnShowSubProperty.setOnClickListener(this);
        binding.btnPrint.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }


    private void initData() {
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_01.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_02.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_03.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_04.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_05.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_06.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_01.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_02.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_03.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_04.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_05.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_06.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_01.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_02.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_03.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_04.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_05.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_06.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_01.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_02.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_03.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_04.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_05.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_06.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_01.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_02.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_03.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_04.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_05.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_06.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_01.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_02.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_03.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_04.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_05.png");
        imgs.add(Environment.getExternalStorageDirectory().getPath() + "/img_06.png");

        products.add(ResourcesUtils.getString(this, R.string.tea_goods1));
        products.add(ResourcesUtils.getString(this, R.string.tea_goods2));
        products.add(ResourcesUtils.getString(this, R.string.tea_goods3));
        products.add(ResourcesUtils.getString(this, R.string.tea_goods4));
        products.add(ResourcesUtils.getString(this, R.string.tea_goods5));
        products.add(ResourcesUtils.getString(this, R.string.tea_goods6));
        products.add(ResourcesUtils.getString(this, R.string.tea_goods7));
        products.add(ResourcesUtils.getString(this, R.string.tea_goods8));
        products.add(ResourcesUtils.getString(this, R.string.tea_goods9));
        products.add(ResourcesUtils.getString(this, R.string.tea_goods10));

        prices.add("10.00");
        prices.add("12.00");
        prices.add("13.00");
        prices.add("10.00");
        prices.add("15.00");
        prices.add("16.00");
        prices.add("10.00");
        prices.add("18.00");
        prices.add("18.00");
        prices.add("10.00");
        divide = ResourcesUtils.getString(this, R.string.divide);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_show_text:
                append(ResourcesUtils.getString(this, R.string.more_show_text) + "\n");
                try {
                    JSONObject json = new JSONObject();
                    json.put("dataModel", "SHOW_IMG_WELCOME");
                    json.put("data", "default");
                    manager.sendCMD(json.toString(), new ISendCallback() {
                        @Override
                        public void onSendSuccess(long taskId) {
                            append(ResourcesUtils.getString(MoreActivity.this, R.string.more_show_text_success) + "\n\n" + divide + "\n\n");
                        }

                        @Override
                        public void onSendFail(int errorId, String errorInfo) {
                            append(ResourcesUtils.getString(MoreActivity.this, R.string.more_show_text_fail) + "\n\n" + divide + "\n\n");
                        }

                        @Override
                        public void onSendProcess(long totle, long sended) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    append(ResourcesUtils.getString(MoreActivity.this, R.string.more_show_text_exception) + ":" + e.getMessage() + "\n\n" + divide + "\n\n");
                }
                break;
            case R.id.btn_show_text_code:
                append(ResourcesUtils.getString(this, R.string.more_show_code_text) + "\n");
                JSONObject json = new JSONObject();
                JSONObject json2 = new JSONObject();
                try {
                    json.put("title", ResourcesUtils.getString(this, R.string.wx_pay));
                    json.put("content", "¥ 10.00");

                    json2.put("dataModel", "QRCODE");
                    json2.put("data", "default");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                manager.setMsgSend(json.toString()).setMsgShow(json2.toString()).registereSendAndShowCallBack(new CommunicateManager.SendAndShowCallBack() {
                    @Override
                    public void onShowSuccess(long taskId) {
                        append(ResourcesUtils.getString(MoreActivity.this, R.string.more_show_code_text_success) + "\n\n" + divide + "\n\n");
                    }

                    @Override
                    public void onSendSuccess(long taskId) {
                        append(ResourcesUtils.getString(MoreActivity.this, R.string.more_send_code_success) + "\n");
                    }

                    @Override
                    public void onSendFail(String path, int errorId, String errorInfo) {
                        append(ResourcesUtils.getString(MoreActivity.this, R.string.more_send_code_fail) + "\n\n" + divide + "\n\n");
                    }

                    @Override
                    public void onShowFail(int errorId, String errorInfo) {
                        append(ResourcesUtils.getString(MoreActivity.this, R.string.more_show_code_text_fail) + "\n\n" + divide + "\n\n");
                    }
                }).showCodePay(Environment.getExternalStorageDirectory().getPath() + "/qrcode.png");
                break;
            case R.id.btn_show_img:
                append(ResourcesUtils.getString(this, R.string.more_show_img));
                sendPicture();
                break;
            case R.id.btn_show_php:
                append(ResourcesUtils.getString(this, R.string.more_show_php) + "\n");
                sendImgs();
                break;
            case R.id.btn_show_video:
                append(ResourcesUtils.getString(this, R.string.more_show_video) + "\n");
                sendVideo();
                break;
            case R.id.btn_show_videos:
                append(ResourcesUtils.getString(this, R.string.more_show_videos) + "\n");
                sendVideos();
                break;
            case R.id.btn_show_menu:
                append(ResourcesUtils.getString(this, R.string.more_show_menu) + ResourcesUtils.getString(this, R.string.tips_success) + "\n\n" + divide + "\n\n");
                try {
                    JSONObject menuJson = new JSONObject();
                    JSONObject data = new JSONObject();
                    data.put("title", ResourcesUtils.getString(this, R.string.menus_title2));
                    JSONObject head = new JSONObject();
                    head.put("param1", ResourcesUtils.getString(this, R.string.menus_number));
                    head.put("param2", ResourcesUtils.getString(this, R.string.menus_goods_name));
                    head.put("param3", ResourcesUtils.getString(this, R.string.menus_unit_price));
                    data.put("head", head);
                    JSONArray list = new JSONArray();
                    for (int i = 1; i < 11; i++) {
                        JSONObject listItem = new JSONObject();
                        listItem.put("param1", "" + i);
                        listItem.put("param2", products.get(i - 1));
                        listItem.put("param3", prices.get(i - 1));
                        list.put(listItem);
                    }
                    data.put("list", list);
                    JSONArray KVPList = new JSONArray();
                    JSONObject KVPListOne = new JSONObject();
                    KVPListOne.put("name", ResourcesUtils.getString(this, R.string.shop_car_total) + " ");
                    KVPListOne.put("value", "132.00");
                    JSONObject KVPListTwo = new JSONObject();
                    KVPListTwo.put("name", ResourcesUtils.getString(this, R.string.shop_car_offer) + " ");
                    KVPListTwo.put("value", "12.00");
                    JSONObject KVPListThree = new JSONObject();
                    KVPListThree.put("name", ResourcesUtils.getString(this, R.string.shop_car_number) + " ");
                    KVPListThree.put("value", "10");
                    JSONObject KVPListFour = new JSONObject();
                    KVPListFour.put("name", ResourcesUtils.getString(this, R.string.shop_car_receivable) + " ");
                    KVPListFour.put("value", "120.00");
                    KVPList.put(0, KVPListOne);
                    KVPList.put(1, KVPListTwo);
                    KVPList.put(2, KVPListThree);
                    KVPList.put(3, KVPListFour);
                    data.put("KVPList", KVPList);
                    menuJson.put("data", data.toString());
                    menuJson.put("dataModel", "TEXT");
                    JSONObject j = new JSONObject();
                    j.put("data", menuJson.toString());
                    j.put("dataType", "DATA");
                    manager.setMsgShow(j.toString()).showMenus();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_show_menu_img:
                append(ResourcesUtils.getString(this, R.string.more_show_menu_img) + "\n");
                sendMenuImg();
                break;
            case R.id.btn_show_menu_videos:
                append(ResourcesUtils.getString(this, R.string.more_show_menu_videos) + "\n");
                sendMenuVideos();
                break;
            case R.id.btn_show_menu_imgs:
                append(ResourcesUtils.getString(this, R.string.more_show_menu_php) + "\n");
                sendMenuImgs();
                break;
            case R.id.btn_acquire_subinfo_main:
                append(ResourcesUtils.getString(this, R.string.more_acquire_subinfo_main) + ":" + Settings.Global.getString(getContentResolver(), "sunmi_sub_model") + "\n\n" + divide + "\n\n");
                Toast.makeText(MoreActivity.this, ResourcesUtils.getString(this, R.string.more_acquire_subinfo_model) + Settings.Global.getString(getContentResolver(), "sunmi_sub_model"), Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_acquire_subinfo_sub:
                append(ResourcesUtils.getString(this, R.string.more_acquire_subinfo_sub) + "\n");
                Log.d(TAG, "onClick: ---------->");
                JSONObject jsonObject2 = new JSONObject();
                try {
                    jsonObject2.put("dataModel", "GET_MODEL");
                    jsonObject2.put("data", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DataPacket p2 = new DataPacket.Builder(DSData.DataType.CMD).recPackName(DSD_PACKNAME).data(jsonObject2.toString())
                        .addCallback(new ISendCallback() {
                            @Override
                            public void onSendSuccess(long taskId) {
                                Log.d(TAG, "onSendSuccess: ---------->" + taskId);
                            }

                            @Override
                            public void onSendFail(int errorId, String errorInfo) {

                            }

                            @Override
                            public void onSendProcess(long totle, long sended) {

                            }
                        }).build();
                manager.sendQuery(p2, new QueryCallback() {
                    @Override
                    public void onReceiveData(final DSData data) {
                        Log.d("SUNMI", "onReceiveData: ------------>" + data.data);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MoreActivity.this, ResourcesUtils.getString(MoreActivity.this, R.string.more_acquire_subinfo_sub_model) + data.data, Toast.LENGTH_LONG).show();
                                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_acquire_subinfo_sub_model) + ":" + data.data + "\n\n" + divide + "\n\n");
                            }
                        });
                    }
                });
                break;
            case R.id.btn_clear_sub_path://清空指定缓存目录
                append(ResourcesUtils.getString(this, R.string.more_clear_sub_file) + "\n");
                try {
                    JSONObject clearPath = new JSONObject();
                    clearPath.put("dataModel", "CLEAN_FILES");
                    clearPath.put("data", Environment.getExternalStorageDirectory().getAbsolutePath() + "/HCService/" + getPackageName().replace(".", "_"));
                    manager.sendCMD(clearPath.toString(), new ISendCallback() {
                        @Override
                        public void onSendSuccess(long taskId) {
                            append(ResourcesUtils.getString(MoreActivity.this, R.string.more_clear_sub_file_success) + "\n\n" + divide + "\n\n");
//                            showToast("清除当前应用在副屏缓存文件成功");
                        }

                        @Override
                        public void onSendFail(int errorId, String errorInfo) {
//                            showToast("清除当前应用在副屏缓存文件失败");
                            append(ResourcesUtils.getString(MoreActivity.this, R.string.more_clear_sub_file_fail) + "\n\n" + divide + "\n\n");
                        }

                        @Override
                        public void onSendProcess(long totle, long sended) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_clear_sub_file: //当指定的id在副屏缓存中不存在也认为删除成功
                 //删除指定的文件的前提就是保存了之前发送文件的id
                long deleteTaskId = (long) SharePreferenceUtil.getParam(this,CommunicateManager.CommunicateType.imgsKey.getValue(), 0L);
                manager.clearSubCacheTaskId(deleteTaskId, new ICheckFileCallback() {
                    @Override
                    public void onCheckFail() {
                        Log.d(TAG, "onCheckFail: ----------->");
                        showToast(ResourcesUtils.getString(MoreActivity.this, R.string.more_clear_file_id_fail));
                    }

                    @Override
                    public void onResult(boolean exist) {
                        Log.d(TAG, "onResult: ---------->" + exist);
                        showToast(ResourcesUtils.getString(MoreActivity.this, R.string.more_clear_file_id_result) + "：" + exist);
                    }
                });
                break;
            case R.id.btn_clear_sub_chche:
                append(ResourcesUtils.getString(this, R.string.more_clear_subcache) + "\n");
                try {
                    JSONObject clearPath = new JSONObject();
                    clearPath.put("dataModel", "CLEAN_FILES");
                    clearPath.put("data", Environment.getExternalStorageDirectory().getAbsolutePath() + "/HCService/");
                   manager.registereSendAndShowCallBack(new CommunicateManager.SendAndShowCallBack() {
                       @Override
                       public void onSendSuccess(long taskId) {
                           append(ResourcesUtils.getString(MoreActivity.this, R.string.more_clear_subcache_success) + "\n\n" + divide + "\n\n");
                       }

                       @Override
                       public void onSendFail(String path, int errorId, String errorInfo) {
                           append(ResourcesUtils.getString(MoreActivity.this, R.string.more_clear_subcache_fail) + "\n\n" + divide + "\n\n");

                       }

                       @Override
                       public void onShowSuccess(long taskId) {

                       }

                       @Override
                       public void onShowFail(int errorId, String errorInfo) {

                       }
                   }).clearSubCacheSize();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case R.id.btn_acquire_sub_path_size:
                append(ResourcesUtils.getString(this, R.string.more_acquire_sub_path) + "\n");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("dataModel", "GETVICECACHEFILESIZE");
                    jsonObject.put("data", Environment.getExternalStorageDirectory().getAbsolutePath() + "/HCService/" + getPackageName().replace(".", "_"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DataPacket packet = new DataPacket.Builder(DSData.DataType.CMD).recPackName(SF.SUNMI_DSD_PACKNAME).data(jsonObject.toString())
                        .addCallback(null).build();
                manager.sendQuery(packet, new QueryCallback() {
                    @Override
                    public void onReceiveData(final DSData data) {
                        Log.d("SUNMI", "onReceiveData: ------------>" + data.data);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MoreActivity.this, ResourcesUtils.getString(MoreActivity.this, R.string.more_acquire_sub_path_size_byte) + data.data, Toast.LENGTH_LONG).show();
                                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_acquire_sub_path_size) + ":" + data.data + "\n\n" + divide + "\n\n");
                            }
                        });
                    }
                });
                break;
            case R.id.btn_open_sub_settting:
                append(ResourcesUtils.getString(this, R.string.more_open_sub_setting) + "\n\n" + divide + "\n\n");
               manager.openSubSetting();

                break;
            case R.id.btn_show_sub_property:
                append(ResourcesUtils.getString(this, R.string.more_show_sub_property) + "\n\n" + divide + "\n\n");
                try {
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("dataModel", "SHOW_DATE");
                    jsonObject1.put("data", "");
                    manager.sendCMD(jsonObject1.toString(), null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_print:
                append(ResourcesUtils.getString(this, R.string.more_print) + "\n\n" + divide + "\n\n");
                if (woyouService != null) {
                    try {
                        woyouService.sendRAWData(getBaiduTestBytes(), null);
                        woyouService.lineWrap(3, null);
                        woyouService.cutPaper(null);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    //百度小票
    public byte[] getBaiduTestBytes() {
        return new byte[]{
                0x1b, 0x40, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x11, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x1b, 0x45, 0x01, 0x1b, 0x47, 0x01, (byte) 0xb1, (byte) 0xbe
                , (byte) 0xb5, (byte) 0xea, (byte) 0xc1, (byte) 0xf4, (byte) 0xb4, (byte) 0xe6, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x2a, 0x2a, 0x2a
                , 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a
                , 0x2a, 0x0a
                , 0x1b, 0x40, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x11, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x1b, 0x45, 0x01, 0x1b, 0x47, 0x01, 0x1b, 0x61
                , 0x01, 0x23, 0x31, 0x35, 0x20, (byte) 0xb0, (byte) 0xd9, (byte) 0xb6, (byte) 0xc8, (byte) 0xcd, (byte) 0xe2, (byte) 0xc2, (byte) 0xf4, 0x0a, 0x5b, (byte) 0xbb, (byte) 0xf5, (byte) 0xb5, (byte) 0xbd, (byte) 0xb8, (byte) 0xb6, (byte) 0xbf, (byte) 0xee, 0x5d, 0x0a, 0x1b, 0x4d, 0x00
                , 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a
                , 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x0a
                , 0x1b, 0x40, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x01, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, (byte) 0xc6, (byte) 0xda, (byte) 0xcd, (byte) 0xfb, (byte) 0xcb, (byte) 0xcd, (byte) 0xb4, (byte) 0xef
                , (byte) 0xca, (byte) 0xb1, (byte) 0xbc, (byte) 0xe4, (byte) 0xa3, (byte) 0xba, (byte) 0xc1, (byte) 0xa2, (byte) 0xbc, (byte) 0xb4, (byte) 0xc5, (byte) 0xe4, (byte) 0xcb, (byte) 0xcd, 0x0a, (byte) 0xb6, (byte) 0xa9, (byte) 0xb5, (byte) 0xa5, (byte) 0xb1, (byte) 0xb8, (byte) 0xd7, (byte) 0xa2, (byte) 0xa3, (byte) 0xba, (byte) 0xc7, (byte) 0xeb, (byte) 0xcb
                , (byte) 0xcd, (byte) 0xb5, (byte) 0xbd, (byte) 0xbf, (byte) 0xfc, (byte) 0xbf, (byte) 0xc6, (byte) 0xce, (byte) 0xf7, (byte) 0xc3, (byte) 0xc5, 0x2c, (byte) 0xb2, (byte) 0xbb, (byte) 0xd2, (byte) 0xaa, (byte) 0xc0, (byte) 0xb1, 0x0a, (byte) 0xb7, (byte) 0xa2, (byte) 0xc6, (byte) 0xb1, (byte) 0xd0, (byte) 0xc5, (byte) 0xcf, (byte) 0xa2, (byte) 0xa3
                , (byte) 0xba, (byte) 0xb0, (byte) 0xd9, (byte) 0xb6, (byte) 0xc8, (byte) 0xcd, (byte) 0xe2, (byte) 0xc2, (byte) 0xf4, (byte) 0xb7, (byte) 0xa2, (byte) 0xc6, (byte) 0xb1, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47
                , 0x00, 0x1b, 0x61, 0x00, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a
                , 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x0a
                , 0x1b, 0x40, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, (byte) 0xb6, (byte) 0xa9, (byte) 0xb5, (byte) 0xa5, (byte) 0xb1, (byte) 0xe0, (byte) 0xba, (byte) 0xc5
                , (byte) 0xa3, (byte) 0xba, 0x31, 0x34, 0x31, 0x38, 0x37, 0x31, 0x38, 0x36, 0x39, 0x31, 0x31, 0x36, 0x38, 0x39, 0x0a, (byte) 0xcf, (byte) 0xc2, (byte) 0xb5, (byte) 0xa5, (byte) 0xca, (byte) 0xb1, (byte) 0xbc, (byte) 0xe4, (byte) 0xa3, (byte) 0xba, 0x32
                , 0x30, 0x31, 0x34, 0x2d, 0x31, 0x32, 0x2d, 0x31, 0x36, 0x20, 0x31, 0x36, 0x3a, 0x33, 0x31, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00
                , 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a
                , 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x0a
                , 0x1b, 0x40, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x01, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, (byte) 0xb2, (byte) 0xcb, (byte) 0xc6, (byte) 0xb7, (byte) 0xc3, (byte) 0xfb, (byte) 0xb3, (byte) 0xc6
                , 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, (byte) 0xca, (byte) 0xfd, (byte) 0xc1, (byte) 0xbf, 0x20, 0x20, 0x20, 0x20, 0x20, (byte) 0xbd, (byte) 0xf0, (byte) 0xb6, (byte) 0xee, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61
                , 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
                , 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x01, 0x1b
                , 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, (byte) 0xcf, (byte) 0xe3, (byte) 0xc0, (byte) 0xb1, (byte) 0xc3, (byte) 0xe6, (byte) 0xcc, (byte) 0xd7, (byte) 0xb2, (byte) 0xcd, 0x1b, 0x24, (byte) 0xf2, 0x00, 0x31, 0x1b, 0x24, 0x25, 0x01, (byte) 0xa3
                , (byte) 0xa4, 0x34, 0x30, 0x2e, 0x30, 0x30, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x1b, 0x4d, 0x00
                , 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x01, 0x1b, 0x45, 0x00, 0x1b
                , 0x47, 0x00, 0x1b, 0x61, 0x00, (byte) 0xcb, (byte) 0xd8, (byte) 0xca, (byte) 0xb3, (byte) 0xcc, (byte) 0xec, (byte) 0xcf, (byte) 0xc2, (byte) 0xba, (byte) 0xba, (byte) 0xb1, (byte) 0xa4, 0x1b, 0x24, (byte) 0xf2, 0x00, 0x31, 0x1b, 0x24, 0x25, 0x01, (byte) 0xa3, (byte) 0xa4
                , 0x33, 0x38, 0x2e, 0x30, 0x30, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x1b, 0x4d, 0x00, 0x1b
                , 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00
                , 0x1b, 0x40, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
                , 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a
                , 0x1b, 0x40, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a
                , 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x0a, 0x1b, 0x4d, 0x00
                , 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x01, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, (byte) 0xd0, (byte) 0xd5, (byte) 0xc3, (byte) 0xfb, (byte) 0xa3, (byte) 0xba, (byte) 0xb0, (byte) 0xd9, (byte) 0xb6, (byte) 0xc8, (byte) 0xb2, (byte) 0xe2, (byte) 0xca
                , (byte) 0xd4, 0x0a, (byte) 0xb5, (byte) 0xd8, (byte) 0xd6, (byte) 0xb7, (byte) 0xa3, (byte) 0xba, (byte) 0xbf, (byte) 0xfc, (byte) 0xbf, (byte) 0xc6, (byte) 0xbf, (byte) 0xc6, (byte) 0xbc, (byte) 0xbc, (byte) 0xb4, (byte) 0xf3, (byte) 0xcf, (byte) 0xc3, 0x0a, (byte) 0xb5, (byte) 0xe7, (byte) 0xbb, (byte) 0xb0, (byte) 0xa3, (byte) 0xba, 0x31
                , 0x38, 0x37, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x0a
                , 0x1b, 0x40, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a
                , 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x0a, (byte) 0xb0, (byte) 0xd9, (byte) 0xb6
                , (byte) 0xc8, (byte) 0xb2, (byte) 0xe2, (byte) 0xca, (byte) 0xd4, (byte) 0xc9, (byte) 0xcc, (byte) 0xbb, (byte) 0xa7, 0x0a, 0x31, 0x38, 0x37, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d
                , 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a
                , 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00
                , 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x1b, 0x61, 0x01, 0x23, 0x31, 0x35, 0x20, (byte) 0xb0, (byte) 0xd9, (byte) 0xb6, (byte) 0xc8, (byte) 0xcd, (byte) 0xe2, (byte) 0xc2, (byte) 0xf4, 0x20, 0x20, 0x31, 0x31, (byte) 0xd4, (byte) 0xc2, 0x30
                , 0x39, (byte) 0xc8, (byte) 0xd5, 0x20, 0x31, 0x37, 0x3a, 0x35, 0x30, 0x3a, 0x33, 0x30, 0x0a, 0x0a, 0x0a, 0x0a, 0x0a
        };
    }

    private void sendMenuImgs() {
        JSONObject json = new JSONObject();
        try {
            //轮播图切换时间
            json.put("rotation_time", 2000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject data = new JSONObject();
        try {

            data.put("title", ResourcesUtils.getString(this, R.string.menus_title2));
            JSONObject head = new JSONObject();
            head.put("param1", ResourcesUtils.getString(this, R.string.menus_number));
            head.put("param2", ResourcesUtils.getString(this, R.string.menus_goods_name));
            head.put("param3", ResourcesUtils.getString(this, R.string.menus_unit_price));
            data.put("head", head);
            data.put("alternateTime", 1000);
            JSONArray list = new JSONArray();
            for (int i = 1; i < 11; i++) {
                JSONObject listItem = new JSONObject();
                listItem.put("param1", "" + i);
                listItem.put("param2", products.get(i - 1));
                listItem.put("param3", prices.get(i - 1));
                list.put(listItem);
            }
            data.put("list", list);
            JSONArray KVPList = new JSONArray();
            JSONObject KVPListOne = new JSONObject();
            KVPListOne.put("name", ResourcesUtils.getString(this, R.string.shop_car_total) + " ");
            KVPListOne.put("value", "132.00");
            JSONObject KVPListTwo = new JSONObject();
            KVPListTwo.put("name", ResourcesUtils.getString(this, R.string.shop_car_offer) + " ");
            KVPListTwo.put("value", "12.00");
            JSONObject KVPListThree = new JSONObject();
            KVPListThree.put("name", ResourcesUtils.getString(this, R.string.shop_car_number) + " ");
            KVPListThree.put("value", "10");
            JSONObject KVPListFour = new JSONObject();
            KVPListFour.put("name", ResourcesUtils.getString(this, R.string.shop_car_receivable) + " ");
            KVPListFour.put("value", "120.00");
            KVPList.put(0, KVPListOne);
            KVPList.put(1, KVPListTwo);
            KVPList.put(2, KVPListThree);
            KVPList.put(3, KVPListFour);
            data.put("KVPList", KVPList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject json2 = new JSONObject();
        try {
            json2.put("dataModel", "SHOW_IMGS_LIST");
            json2.put("data", data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        manager.setMsgSend(json.toString()).setMsgShow(json2.toString()).registereSendAndShowCallBack(new CommunicateManager.SendAndShowCallBack() {
            @Override
            public void onSendSuccess(long taskId) {
                Log.d("TAG", "onAllSendSuccess: ---------->");
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_send_allimg_success) + "\n");
            }

            @Override
            public void onSendFail(String path, int errorId, String errorInfo) {
                Log.d(TAG, "onSendFileFaile: --------------->" + path + "  " + errorInfo);
                append(path + errorInfo + "\n");
            }

            @Override
            public void onShowSuccess(long taskId) {
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_send_allimg_success) + "\n\n" + divide + "\n\n");
            }

            @Override
            public void onShowFail(int errorId, String errorInfo) {
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_send_allimg_fail) + "\n\n" + divide + "\n\n");
            }
        }).checkFileExist(CommunicateManager.CommunicateType.PHPMenusKey,imgs);
    }


    private void sendMenuVideos() {
        //请对文件是否存在做判断
        List<String> files = new ArrayList<>();
        files.add(Environment.getExternalStorageDirectory().getPath() + "/video_01.mp4");
        files.add(Environment.getExternalStorageDirectory().getPath() + "/video_02.mp4");
        files.add(Environment.getExternalStorageDirectory().getPath() + "/video_03.mp4");
        final JSONObject data = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            data.put("title", ResourcesUtils.getString(this, R.string.menus_title2));
            JSONObject head = new JSONObject();
            head.put("param1", ResourcesUtils.getString(this, R.string.menus_number));
            head.put("param2", ResourcesUtils.getString(this, R.string.menus_goods_name));
            head.put("param3", ResourcesUtils.getString(this, R.string.menus_unit_price));
            data.put("head", head);
            data.put("alternateTime", 1000);
            JSONArray list = new JSONArray();
            for (int i = 1; i < 11; i++) {
                JSONObject listItem = new JSONObject();
                listItem.put("param1", "" + i);
                listItem.put("param2", products.get(i - 1));
                listItem.put("param3", prices.get(i - 1));
                list.put(listItem);
            }
            data.put("list", list);
            JSONArray KVPList = new JSONArray();
            JSONObject KVPListOne = new JSONObject();
            KVPListOne.put("name", ResourcesUtils.getString(this, R.string.shop_car_total) + " ");
            KVPListOne.put("value", "132.00");
            JSONObject KVPListTwo = new JSONObject();
            KVPListTwo.put("name", ResourcesUtils.getString(this, R.string.shop_car_offer) + " ");
            KVPListTwo.put("value", "12.00");
            JSONObject KVPListThree = new JSONObject();
            KVPListThree.put("name", ResourcesUtils.getString(this, R.string.shop_car_number) + " ");
            KVPListThree.put("value", "10");
            JSONObject KVPListFour = new JSONObject();
            KVPListFour.put("name", ResourcesUtils.getString(this, R.string.shop_car_receivable) + " ");
            KVPListFour.put("value", "120.00");
            KVPList.put(0, KVPListOne);
            KVPList.put(1, KVPListTwo);
            KVPList.put(2, KVPListThree);
            KVPList.put(3, KVPListFour);
            data.put("KVPList", KVPList);
            json.put("dataModel", "MENUVIDEOS");
            json.put("data", data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        manager.setMsgShow(json.toString()).registereSendAndShowCallBack(new CommunicateManager.SendAndShowCallBack() {
            @Override
            public void onSendSuccess(long taskId) {
                Log.d(TAG, "onAllSendSuccess: ----------->" + taskId);
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_send_video_success) + "\n");
            }

            @Override
            public void onSendFail(String path, int errorId, String errorInfo) {
                append(path + errorInfo + "\n\n" + divide + "\n\n");
            }

            @Override
            public void onShowSuccess(long taskId) {
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_show_menu_videos_success) + "\n" + divide + "\n\n");
            }

            @Override
            public void onShowFail(int errorId, String errorInfo) {
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_show_menu_videos_fail) + "\n" + divide + "\n\n");

            }
        }).checkFileExist(CommunicateManager.CommunicateType.videosMenusKey,files);
    }



    private void sendMenuImg() {
        JSONObject data = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            data.put("title", ResourcesUtils.getString(this, R.string.menus_title2));
            JSONObject head = new JSONObject();
            head.put("param1", ResourcesUtils.getString(this, R.string.menus_number));
            head.put("param2", ResourcesUtils.getString(this, R.string.menus_goods_name));
            head.put("param3", ResourcesUtils.getString(this, R.string.menus_unit_price));
            data.put("head", head);
            data.put("alternateTime", 1000);
            JSONArray list = new JSONArray();
            for (int i = 1; i < 11; i++) {
                JSONObject listItem = new JSONObject();
                listItem.put("param1", "" + i);
                listItem.put("param2", products.get(i - 1));
                listItem.put("param3", prices.get(i - 1));
                list.put(listItem);
            }
            data.put("list", list);
            JSONArray KVPList = new JSONArray();
            JSONObject KVPListOne = new JSONObject();
            KVPListOne.put("name", ResourcesUtils.getString(this, R.string.shop_car_total) + " ");
            KVPListOne.put("value", "132.00");
            JSONObject KVPListTwo = new JSONObject();
            KVPListTwo.put("name", ResourcesUtils.getString(this, R.string.shop_car_offer) + " ");
            KVPListTwo.put("value", "12.00");
            JSONObject KVPListThree = new JSONObject();
            KVPListThree.put("name", ResourcesUtils.getString(this, R.string.shop_car_number) + " ");
            KVPListThree.put("value", "10");
            JSONObject KVPListFour = new JSONObject();
            KVPListFour.put("name", ResourcesUtils.getString(this, R.string.shop_car_receivable) + " ");
            KVPListFour.put("value", "120.00");
            KVPList.put(0, KVPListOne);
            KVPList.put(1, KVPListTwo);
            KVPList.put(2, KVPListThree);
            KVPList.put(3, KVPListFour);
            data.put("KVPList", KVPList);

            json.put("dataModel", "SHOW_IMG_LIST");
            json.put("data", data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        manager.setMsgShow(json.toString()).registereSendAndShowCallBack(new CommunicateManager.SendAndShowCallBack() {
            @Override
            public void onSendSuccess(long taskId) {
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_send_img_success) + "\n");
            }

            @Override
            public void onSendFail(String path, int errorId, String errorInfo) {
                Log.d("TAG", "onSendFail: -------------------->" + errorId + "  " + errorInfo);
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_send_img_fail) + "\n\n" + divide + "\n\n");
            }

            @Override
            public void onShowSuccess(long taskId) {
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_show_menu_img_success) + "\n\n" + divide + "\n\n");
            }

            @Override
            public void onShowFail(int errorId, String errorInfo) {
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_show_menu_img_fail) + "\n\n" + divide + "\n\n");
            }
        }).checkFileExist(CommunicateManager.CommunicateType.imgMenuKey, imgs.get(2));

    }


    private void sendPicture() {
        Log.d(TAG, "sendPicture: --------->");
        JSONObject json = new JSONObject();
        try {
            json.put("dataModel", "SHOW_IMG_WELCOME");
            json.put("data", "default");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        manager.setMsgShow(json.toString()).registereSendAndShowCallBack(new CommunicateManager.SendAndShowCallBack() {
            @Override
            public void onShowSuccess(long taskId) {
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_show_single_img_success) + "\n" + divide + "\n\n");
            }

            @Override
            public void onSendSuccess(long taskId) {
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_send_single_img_success) + "\n");
            }

            @Override
            public void onSendFail(String path, int errorId, String errorInfo) {
                Log.d("TAG", "onSendFail: -------------------->" + errorId + "  " + errorInfo);
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_send_single_img_fail) + "\n\n" + divide + "\n\n");
            }

            @Override
            public void onShowFail(int errorId, String errorInfo) {
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_show_single_img_fail) + "\n" + divide + "\n\n");
            }
        }).checkFileExist(CommunicateManager.CommunicateType.imgKey, Environment.getExternalStorageDirectory().getPath() + "/img_01.png");
    }


    /**
     * 发送轮播图片
     */
    private void sendImgs() {
        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        try {
            //轮播图切换时间
            json.put("rotation_time", 2000);
            json2.put("dataModel", "IMAGES");
            json2.put("data", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        manager.setMsgSend(json.toString()).setMsgShow(json2.toString()).registereSendAndShowCallBack(new CommunicateManager.SendAndShowCallBack() {
            @Override
            public void onShowSuccess(long taskId) {

            }

            @Override
            public void onSendSuccess(long taskId) {
                Log.d("TAG", "onAllSendSuccess: ---------->");
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_send_allphp_success) + "\n\n" + divide + "\n\n");
            }

            @Override
            public void onSendFail(String path, int errorId, String errorInfo) {
                Log.d(TAG, "onSendFileFaile: --------------->" + errorId + "  " + errorInfo);
                append(errorId + errorInfo + "\n\n" + divide + "\n\n");
            }

            @Override
            public void onShowFail(int errorId, String errorInfo) {

            }
        }).checkFileExist(CommunicateManager.CommunicateType.imgsKey, imgs);
    }






    private void sendVideo() {
        JSONObject json = new JSONObject();
        try {
            json.put("dataModel", "VIDEO");
            json.put("data", "true"); //true代表视频续播
        } catch (JSONException e) {
            e.printStackTrace();
        }
        manager.setMsgShow(json.toString()).registereSendAndShowCallBack(new CommunicateManager.SendAndShowCallBack() {
            @Override
            public void onShowSuccess(long taskId) {
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_show_single_video_success) + "\n\n" + divide + "\n\n");
            }

            @Override
            public void onSendSuccess(long taskId) {
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_send_single_video_success) + "\n");
            }

            @Override
            public void onSendFail(String path, int errorId, String errorInfo) {
                Log.d("SUNMI", "发送单个文件视频文件失败 ------------>" + errorInfo);
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_send_single_video_fail) + "\n\n" + divide + "\n\n");
            }

            @Override
            public void onShowFail(int errorId, String errorInfo) {
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_show_single_video_fail) + "\n\n" + divide + "\n\n");
            }
        }).checkFileExist(CommunicateManager.CommunicateType.videoKey, Environment.getExternalStorageDirectory().getPath() + "/video_01.mp4");
    }
//
//    private void playvideo(long taskID) {
//        Log.d("SUNMI", "playvideo: ------------>");
//        JSONObject json = new JSONObject();
//        try {
//            json.put("dataModel", "VIDEO");
//            json.put("data", "true"); //true代表视频续播
//            mDSKernel.sendCMD(DSD_PACKNAME, json.toString(), taskID, new ISendCallback() {
//                @Override
//                public void onSendSuccess(long taskId) {
//
//                }
//
//                @Override
//                public void onSendFail(int errorId, String errorInfo) {
//
//                }
//
//                @Override
//                public void onSendProcess(long totle, long sended) {
//
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }


    /**
     * 发送多视频
     */
    private void sendVideos() {
        //请对文件是否存在做判断
        List<String> files = new ArrayList<>();
        files.add(Environment.getExternalStorageDirectory().getPath() + "/video_01.mp4");
        files.add(Environment.getExternalStorageDirectory().getPath() + "/video_02.mp4");
        files.add(Environment.getExternalStorageDirectory().getPath() + "/video_03.mp4");
        JSONObject json = new JSONObject();
        try {
            json.put("dataModel", "VIDEOS");
            json.put("data", "true"); //true代表视频续播
        } catch (JSONException e) {
            e.printStackTrace();
        }
        manager.setMsgShow(json.toString()).registereSendAndShowCallBack(new CommunicateManager.SendAndShowCallBack() {
            @Override
            public void onShowSuccess(long taskId) {
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_show_videos_success) + "\n\n" + divide + "\n\n");
            }

            @Override
            public void onSendSuccess(long taskId) {
                Log.d(TAG, "onAllSendSuccess: ----------->" + taskId);
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_send_videos_success) + "\n");
            }

            @Override
            public void onSendFail(String path, int errorId, String errorInfo) {
                append(path + errorInfo + "\n\n" + divide + "\n\n");
            }

            @Override
            public void onShowFail(int errorId, String errorInfo) {
                append(ResourcesUtils.getString(MoreActivity.this, R.string.more_show_videos_fail) + "\n\n" + divide + "\n\n");
            }
        }).checkFileExist(CommunicateManager.CommunicateType.videosKey, files);

    }


    private static class MyHandler extends Handler {
        private WeakReference<Activity> mActivity;

        public MyHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() != null && !mActivity.get().isFinishing()) {
                switch (msg.what) {
                    case 1://消息提示用途
                        Toast.makeText(mActivity.get(), msg.obj + "", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }

    }


    @Override
    protected void onPause() { //如果存在activity跳转，需要做清理操作
        super.onPause();
        mDSKernel.onDestroy();
        mDSKernel = null;
    }

    public void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MoreActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void append(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.tvNote.append(message);
            }
        });
    }
}
