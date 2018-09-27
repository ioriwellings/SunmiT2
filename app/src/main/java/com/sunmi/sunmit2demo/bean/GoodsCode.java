package com.sunmi.sunmit2demo.bean;

import com.sunmi.sunmit2demo.MyApplication;
import com.sunmi.sunmit2demo.R;
import com.sunmi.sunmit2demo.utils.ResourcesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsCode {

    private  Map<String, GvBeans> Goods = new HashMap<>();
    List<GvBeans> drinks=new ArrayList<>();
    List<GvBeans> snacks=new ArrayList<>();
    List<GvBeans> vegetables=new ArrayList<>();
    List<GvBeans> fruits=new ArrayList<>();
    private static GoodsCode instance=null;
    public static GoodsCode getInstance() {
        if(instance==null){
            instance=new GoodsCode();
        }
        return instance;
    }



    private  GoodsCode(){
        add("6901939621257", R.drawable.goods_1, R.string.goods_1, 3.00f);
        add("6928804014686", R.drawable.goods_2, R.string.goods_2, 3.00f);
        add("6925303721367", R.drawable.goods_3, R.string.goods_3, 3.50f);
        add("6921581596048", R.drawable.goods_4, R.string.goods_4, 4.50f);

        add("6948939635686", R.drawable.goods_5, R.string.goods_5, 6.80f);
        add("6948939611543", R.drawable.goods_6, R.string.goods_6, 6.80f);
        add("4895058313549", R.drawable.goods_7, R.string.goods_7, 6.60f);
        add("4895058313532", R.drawable.goods_8, R.string.goods_8, 6.60f);

        add("6928804011142", R.drawable.coco, R.string.goods_coke, 5.00f);
        add("6902827110013", R.drawable.sprit, R.string.goods_sprite, 4.00f);
        add("6920202888883", R.drawable.redbull, R.string.goods_red_bull, 6.00f);
//        add("1111111111111", 0, R.string.goods_orange_juice, 5.50f);

        add("1", R.drawable.apple, R.string.goods_apple, 9.90f);
        add("2", R.drawable.pears, R.string.goods_pear, 7.00f);
        add("3", R.drawable.banana, R.string.goods_banana, 12.0f);
        add("4", R.drawable.pitaya, R.string.goods_pitaya, 16.0f);
        add("5", R.drawable.goods_sc_1, R.string.goods_sc_1, 13.0f);
        add("6", R.drawable.goods_sc_2, R.string.goods_sc_2, 20.0f);
        add("7", R.drawable.goods_sc_3, R.string.goods_sc_3, 12.0f);
        add("8", R.drawable.goods_sc_4, R.string.goods_sc_4, 8.00f);

        add("9", R.drawable.goods_scs_1, R.string.goods_scs_1, 5.50f);
        add("10", R.drawable.goods_scs_2, R.string.goods_scs_2, 3.50f);
        add("11", R.drawable.goods_scs_3, R.string.goods_scs_3, 4.70f);
        add("12", R.drawable.goods_scs_4, R.string.goods_scs_4, 9.90f);


        drinks.add(Goods.get("6901939621257"));
        drinks.add(Goods.get("6928804014686"));
        drinks.add(Goods.get("6925303721367"));
        drinks.add(Goods.get("6921581596048"));

        snacks.add(Goods.get("6948939635686"));
        snacks.add(Goods.get("6948939611543"));
        snacks.add(Goods.get("4895058313549"));
        snacks.add(Goods.get("4895058313532"));

        fruits.add(Goods.get("1").setLogo( R.drawable.apple_dialog));
        fruits.add(Goods.get("2").setLogo( R.drawable.pears_dialog));
        fruits.add(Goods.get("3").setLogo( R.drawable.banana_dialog));
        fruits.add(Goods.get("4").setLogo( R.drawable.pitaya_dialog));
        fruits.add(Goods.get("5").setLogo( R.drawable.goods_sc_icon_1));
        fruits.add(Goods.get("6").setLogo( R.drawable.goods_sc_icon_2));
        fruits.add(Goods.get("7").setLogo( R.drawable.goods_sc_icon_3));
        fruits.add(Goods.get("8").setLogo( R.drawable.goods_sc_icon_4));

        vegetables.add(Goods.get("9").setLogo( R.drawable.goods_scs_icon_1));
        vegetables.add(Goods.get("10").setLogo( R.drawable.goods_scs_icon_2));
        vegetables.add(Goods.get("11").setLogo( R.drawable.goods_scs_icon_3));
        vegetables.add(Goods.get("12").setLogo( R.drawable.goods_scs_icon_4));

    }
    public List<GvBeans> getVegetables() {
        return vegetables;
    }

    public List<GvBeans> getFruits() {
        return fruits;
    }

    public Map<String, GvBeans> getGood(){
        return Goods;
    }

    public List<GvBeans> getSnacks(){

        return snacks;
    }
    public List<GvBeans> getDrinks() {
        return drinks;
    }

     void add(String code, int imageId, int resString, float price) {
        Goods.put(code, new GvBeans(imageId, ResourcesUtils.getString(MyApplication.getInstance(), resString), ResourcesUtils.getString(MyApplication.getInstance(), R.string.units_money) + price));
    }
}
