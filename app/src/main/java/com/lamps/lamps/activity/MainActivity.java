package com.lamps.lamps.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lamps.lamps.adapter.MyPagerAdapter;
import com.lamps.lamps.R;
import com.lamps.lamps.fragment.BrightFragment;
import com.lamps.lamps.fragment.LightFragment;
import com.lamps.lamps.fragment.ColorFragment;
import com.lamps.lamps.fragment.GradientFragment;
import com.lamps.lamps.fragment.TwinkleFragment;
import com.lamps.lamps.util.CommandUtils;
import com.lamps.lamps.util.ControlLight;
import com.lamps.lamps.util.CustomViewpager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.SimpleViewPagerDelegate;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ImageView lightOff;
    private ImageView lightOn;
    private String currentOrder = CommandUtils.BRIGHT_1;
    private String currentSpeed = CommandUtils.SPEED_1;
    private boolean shouldSendSpeed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTab();
        initView();
    }

    private void initView() {
        lightOff = (ImageView) findViewById(R.id.light_off);
        lightOn = (ImageView) findViewById(R.id.light_on);
        ImageView setImg = (ImageView) findViewById(R.id.main_setting);
        ImageView backWifiList = (ImageView) findViewById(R.id.back_wifi_list);
        ImageView setTimeImg = (ImageView) findViewById(R.id.main_set_time);

        if (lightOff != null && lightOn != null && setImg != null && setTimeImg != null && backWifiList != null) {
            lightOff.setOnClickListener(this);
            lightOn.setOnClickListener(this);
            setImg.setOnClickListener(this);
            backWifiList.setOnClickListener(this);
            setTimeImg.setOnClickListener(this);
        }

    }

    private void initTab() {
        final List<String> tabNames = new ArrayList<>();
        final List<Fragment> fragments = new ArrayList<>();

        tabNames.add(getString(R.string.tab_bright));
        tabNames.add(getString(R.string.tab_twinkle));
        tabNames.add(getString(R.string.tab_gradient));
        tabNames.add(getString(R.string.tab_color));
        tabNames.add(getString(R.string.tab_light));

        fragments.add(new BrightFragment());
        fragments.add(new TwinkleFragment());
        fragments.add(new GradientFragment());
        fragments.add(new ColorFragment());
        fragments.add(new LightFragment());

        final CustomViewpager viewPager = (CustomViewpager) findViewById(R.id.viewpager);

        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.indicator);

        if (viewPager == null || magicIndicator == null) {
            return;
        }

        /**
         * 禁止viewpager 滑动
         */
        viewPager.setPagingEnabled(false);


        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabNames.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(context.getResources().getColor(R.color.tabUnSelected));
                colorTransitionPagerTitleView.setSelectedColor(context.getResources().getColor(R.color.white));
                colorTransitionPagerTitleView.setText(tabNames.get(index));
                colorTransitionPagerTitleView.setTextSize(18);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (index == 3 || index == 4) {
                            return;
                        }

                        viewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineWidth(context.getResources().getDimension(R.dimen.indicator_line_width));
                indicator.setLineHeight(context.getResources().getDimension(R.dimen.indicator_line_height));
                indicator.setColors(context.getResources().getColor(R.color.white));
                return indicator;
            }
        });
        commonNavigator.setAdjustMode(true);
        magicIndicator.setNavigator(commonNavigator);
        SimpleViewPagerDelegate.with(magicIndicator, viewPager).delegate();

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), tabNames, fragments);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (fragments.get(position) instanceof BrightFragment) {

                    shouldSendSpeed = true;

                    currentOrder = ((BrightFragment) fragments.get(position)).getOrder();
                } else if (fragments.get(position) instanceof TwinkleFragment) {

                    shouldSendSpeed = false;

                    currentOrder = ((TwinkleFragment) fragments.get(position)).getOrder();
                    currentSpeed = ((TwinkleFragment) fragments.get(position)).getSpeed();
                    ControlLight.sendOrder(currentSpeed);
                } else if (fragments.get(position) instanceof GradientFragment) {

                    shouldSendSpeed = false;

                    currentOrder = ((GradientFragment) fragments.get(position)).getOrder();
                    currentSpeed = ((GradientFragment) fragments.get(position)).getSpeed();
                    ControlLight.sendOrder(currentSpeed);
                } else if (fragments.get(position) instanceof ColorFragment) {

                    shouldSendSpeed = true;

                    currentOrder = ((ColorFragment) fragments.get(position)).getOrder();
                } else if (fragments.get(position) instanceof LightFragment) {

                    shouldSendSpeed = false;

                    currentOrder = ((LightFragment) fragments.get(position)).getOrder();
                    currentSpeed = ((LightFragment) fragments.get(position)).getSpeed();
                    ControlLight.sendOrder(currentSpeed);
                }


                lightOff.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ControlLight.sendOrder(currentOrder);
                    }
                }, 1000);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.light_off:
                /**
                 * 灯总开关：关闭
                 */
                lightOff.setVisibility(View.INVISIBLE);
                lightOn.setVisibility(View.VISIBLE);

                ControlLight.sendOrder(CommandUtils.OFF);

                break;
            case R.id.light_on:
                /**
                 * 灯总开关：开启
                 */
                lightOff.setVisibility(View.VISIBLE);
                lightOn.setVisibility(View.INVISIBLE);

                /**
                 * 先开启灯，再获取当前的模式
                 */

                ControlLight.sendOrder(CommandUtils.ON);
                lightOff.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ControlLight.sendOrder(currentOrder);

                        lightOff.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!shouldSendSpeed) {
                                    ControlLight.sendOrder(currentSpeed);
                                }
                            }
                        }, 1000);


                    }
                }, 1000);

                break;
            case R.id.main_setting: {
                /**
                 * 跳转至设置界面
                 */
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }

            break;
            case R.id.main_set_time: {
                /**
                 * 跳转定时界面
                 */
                Intent intent = new Intent(MainActivity.this, TimingActivity.class);
                startActivity(intent);
            }

            break;

            case R.id.back_wifi_list:
                /**
                 * 跳转到wifi列表
                 */

                finish();

                break;
        }

    }

    public void sendOrder(String order) {
        currentOrder = order;
        ControlLight.sendOrder(order);
    }

    public void sendSpeed(String speed) {
        currentSpeed = speed;
        ControlLight.sendOrder(speed);
    }

}
