package com.magestore.app.pos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.registershift.RegisterShiftService;
import com.magestore.app.lib.service.user.UserService;
import com.magestore.app.pos.controller.RegisterShiftListController;
import com.magestore.app.pos.panel.RegisterShiftDetailPanel;
import com.magestore.app.pos.panel.RegisterShiftListPanel;
import com.magestore.app.pos.ui.AbstractActivity;
import com.magestore.app.util.ConfigUtil;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftActivity extends AbstractActivity {
    RegisterShiftListPanel mRegisterShiftListPanel;
    RegisterShiftDetailPanel mRegisterShiftDetailPanel;
    RegisterShiftListController mRegisterShiftListController;
    boolean isEnableAction = true;

    // Toolbar ứng dụng
    private Toolbar toolbar_order;

    // xác định loại màn hình 1 pane hay 2 pane
    private boolean mblnTwoPane;

    // Dev license
    TextView dev_license;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_shift_menu);

        initLayout();
        initModel();
        initValue();
        super.setheader();
        super.changeBackgroundSelect();
    }

    @Override
    protected void initLayout() {
        toolbar_order = (Toolbar) findViewById(R.id.toolbar_order);
        initToolbarMenu(toolbar_order);

        // chuẩn bị panel danh sách register shift
        mRegisterShiftListPanel = (RegisterShiftListPanel) findViewById(R.id.register_shift_list_panel);
        // chuẩn bị panel thông tin đơn hàng chi tiết
        mRegisterShiftDetailPanel = (RegisterShiftDetailPanel) findViewById(R.id.register_shift_detail_panel);

        mRegisterShiftListPanel.setToolbar(toolbar_order);
        // xem giao diện 2 pane hay 1 pane
        mblnTwoPane = findViewById(R.id.two_pane) != null;

        // Dev license
        dev_license = (TextView) findViewById(R.id.dev_license);
        checkDevLicense();
    }

    @Override
    protected void initModel() {
        // Context sử dụng xuyên suốt hệ thống
        MagestoreContext magestoreContext = new MagestoreContext();
        magestoreContext.setActivity(this);

        // chuẩn bị service
        ServiceFactory factory;
        RegisterShiftService service = null;
        UserService userService = null;
        try {
            factory = ServiceFactory.getFactory(magestoreContext);
            service = factory.generateRegisterShiftService();
            userService = factory.generateUserService();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        mRegisterShiftListController = new RegisterShiftListController();
        mRegisterShiftListController.setMagestoreContext(magestoreContext);
        mRegisterShiftListController.setRegisterShiftService(service);
        mRegisterShiftListController.setUserService(userService);
        mRegisterShiftListController.setListPanel(mRegisterShiftListPanel);
        mRegisterShiftListController.setDetailPanel(mRegisterShiftDetailPanel);

        mRegisterShiftListPanel.initModel();
        mRegisterShiftDetailPanel.initModel();

        IntentFilter filter = new IntentFilter(RegisterShiftListController.SEND_NOTI_TO_REGISTER_ACTIVITY);
        registerReceiver(receiver_data, filter);
        IntentFilter filter_change_menu_order = new IntentFilter(CHANGE_PERMISSON_MENU_ORDER);
        registerReceiver(receiver_menu_order, filter_change_menu_order);
        IntentFilter filter_back_to_home = new IntentFilter(BACK_TO_HOME);
        registerReceiver(back_to_home, filter_back_to_home);
    }

    @Override
    protected void initValue() {
        // load danh sách register shift
        mRegisterShiftListController.doRetrieve();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            mRegisterShiftDetailPanel.setVisibility(View.INVISIBLE);
            mRegisterShiftListPanel.setVisibility(View.VISIBLE);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    BroadcastReceiver receiver_data = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isEnableAction = intent.getBooleanExtra("is_show", false);
            if (isEnableAction) {
                isEnableAction(true);
            } else {
                isEnableAction(false);
            }
        }
    };

    BroadcastReceiver receiver_menu_order = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            changePermissonOrderMenu();
            mRegisterShiftDetailPanel.bindItem(mRegisterShiftListController.getSelectedItem());
        }
    };

    BroadcastReceiver back_to_home = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(receiver_data);
            unregisterReceiver(receiver_menu_order);
            unregisterReceiver(back_to_home);
        }catch (Exception e){}
    }

    @Override
    public void onBackPressed() {
        if(isEnableAction){
            super.onBackPressed();
        }
    }

    private void checkDevLicense(){
        dev_license.setVisibility(ConfigUtil.isDevLicense() ? View.VISIBLE : View.GONE);
    }
}
