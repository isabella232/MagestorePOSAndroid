package com.magestore.app.pos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.order.OrderHistoryService;
import com.magestore.app.pos.controller.OrderCommentListController;
import com.magestore.app.pos.controller.OrderHistoryItemsListController;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.pos.controller.OrderPaymentListController;
import com.magestore.app.pos.panel.OrderCommentHistoryListPanel;
import com.magestore.app.pos.panel.OrderDetailPanel;
import com.magestore.app.pos.panel.OrderHistoryItemsListPanel;
import com.magestore.app.pos.panel.OrderListPanel;
import com.magestore.app.pos.panel.OrderPaymentLaterListPanel;
import com.magestore.app.pos.panel.OrderPaymentListPanel;
import com.magestore.app.pos.ui.AbstractActivity;
import com.magestore.app.util.ConfigUtil;

/**
 * An activity representing a list of CartItem. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link OrderDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class OrderActivity extends AbstractActivity {

    private OrderListPanel mOrderListPanel = null;
    private OrderHistoryListController mOrderListController = null;
    private OrderDetailPanel mOrderDetailPanel = null;

    OrderPaymentListPanel mOrderPaymentListPanel;
    // panel hiển thị history comment của 1 order
    OrderCommentHistoryListPanel mOrderCommentHistoryListPanel;
    // panel hiển thị danh sách item/product trong order
    OrderHistoryItemsListPanel mOrderHistoryItemsListPanel;
    OrderPaymentLaterListPanel mOrderPaymentLaterListPanel;
    OrderPaymentListController mOrderPaymentListController;
    OrderCommentListController mOrderCommentHistoryController;
    OrderHistoryItemsListController mOrderHistoryItemsListController;

    LinearLayout ll_payment_later;

    // Toolbar ứng dụng
    private Toolbar mToolbar;

    // xác định loại màn hình 1 pane hay 2 pane
    private boolean mblnTwoPane;

    // Dev license
    TextView dev_license;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_menu);

        initLayout();
        initModel();
        initValue();
        super.setheader();
        super.changeBackgroundSelect();
    }

    @Override
    protected void initLayout() {
        // chuẩn bị tool bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getTitle());
        initToolbarMenu(mToolbar);

        // chuẩn bị panel danh sách đơn hàng
        mOrderListPanel = (OrderListPanel) findViewById(R.id.order_list_panel);
        // chuẩn bị panel thông tin đơn hàng chi tiết
        mOrderDetailPanel = (OrderDetailPanel) findViewById(R.id.order_detail_panel);

        // chuẩn bị panel view danh sách payment
        mOrderPaymentListPanel = (OrderPaymentListPanel) mOrderDetailPanel.findViewById(R.id.order_payment);
        ll_payment_later = (LinearLayout) mOrderDetailPanel.findViewById(R.id.ll_payment_later);
        mOrderPaymentLaterListPanel = (OrderPaymentLaterListPanel)  mOrderDetailPanel.findViewById(R.id.order_payment_later);

        // chuẩn bị panel view danh sách comment
        mOrderCommentHistoryListPanel = (OrderCommentHistoryListPanel) mOrderDetailPanel.findViewById(R.id.order_comment);

        // chuẩn bị panel view danh sách items
        mOrderHistoryItemsListPanel = (OrderHistoryItemsListPanel) mOrderDetailPanel.findViewById(R.id.order_items);

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
        OrderHistoryService service = null;
        try {
            factory = ServiceFactory.getFactory(magestoreContext);
            service = factory.generateOrderHistoryService();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        // Controller Payment
        mOrderPaymentListController = new OrderPaymentListController();
        mOrderPaymentListController.setView(mOrderPaymentListPanel);
        mOrderPaymentListController.setOrderPaymentLaterListPanel(mOrderPaymentLaterListPanel);
        mOrderPaymentListController.setLayoutPaymentLater(ll_payment_later);
        mOrderPaymentListController.setOrderService(service);
        mOrderPaymentListController.setMagestoreContext(magestoreContext);

        // Controller Comment
        mOrderCommentHistoryController = new OrderCommentListController();
        mOrderCommentHistoryController.setView(mOrderCommentHistoryListPanel);
        mOrderCommentHistoryController.setOrderService(service);
        mOrderCommentHistoryController.setMagestoreContext(magestoreContext);

        // Controller CartItem
        mOrderHistoryItemsListController = new OrderHistoryItemsListController();
        mOrderHistoryItemsListController.setView(mOrderHistoryItemsListPanel);
        mOrderHistoryItemsListController.setOrderService(service);
        mOrderHistoryItemsListController.setMagestoreContext(magestoreContext);

        // Tạo list controller
        mOrderListController = new OrderHistoryListController();
        mOrderListController.setMagestoreContext(magestoreContext);
        mOrderListController.setOrderService(service);
        mOrderListController.setListPanel(mOrderListPanel);
        mOrderListController.setDetailPanel(mOrderDetailPanel);
        mOrderListController.setOrderCommentListController(mOrderCommentHistoryController);
        mOrderListController.setOrderHistoryItemsListController(mOrderHistoryItemsListController);
        mOrderListController.setOrderPaymentListController(mOrderPaymentListController);

        // chuẩn bị model cho các panel
        mOrderListPanel.initModel();
        mOrderDetailPanel.initModel();

        IntentFilter filter_change_menu_order = new IntentFilter(CHANGE_PERMISSON_MENU_ORDER);
        registerReceiver(receiver_menu_order, filter_change_menu_order);

        IntentFilter filter_back_to_home = new IntentFilter(BACK_TO_HOME);
        registerReceiver(back_to_home, filter_back_to_home);
    }

    BroadcastReceiver receiver_menu_order = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            changePermissonOrderMenu();
        }
    };

    BroadcastReceiver back_to_home = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void initValue() {
        // load danh sách order
        mOrderListController.doRetrieve();
        // load danh sách payment method
        mOrderListController.doRetrievePaymentMethod();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            mOrderDetailPanel.setVisibility(View.INVISIBLE);
            mOrderListPanel.setVisibility(View.VISIBLE);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            initToolbarMenu(mToolbar);
            return true;
        }
        if (id == R.id.order_action) {
            View rootView = getWindow().getDecorView().getRootView();
            mOrderDetailPanel.showPopupMenu(rootView);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkDevLicense(){
        dev_license.setVisibility(ConfigUtil.isDevLicense() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(receiver_menu_order);
            unregisterReceiver(back_to_home);
        }catch (Exception e){}
    }
}
