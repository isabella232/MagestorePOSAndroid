package com.magestore.app.pos.controller;

import android.content.Intent;
import android.os.Parcelable;
import android.widget.RelativeLayout;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderInvoiceParams;
import com.magestore.app.lib.model.sales.OrderItemUpdateQtyParam;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderShipmentTrackParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.model.sales.OrderUpdateQtyParam;
import com.magestore.app.lib.service.order.OrderHistoryService;
import com.magestore.app.pos.SalesActivity;
import com.magestore.app.pos.panel.OrderAddCommentPanel;
import com.magestore.app.pos.panel.OrderCancelPanel;
import com.magestore.app.pos.panel.OrderDetailPanel;
import com.magestore.app.pos.panel.OrderInvoicePanel;
import com.magestore.app.pos.panel.OrderAddPaymentPanel;
import com.magestore.app.pos.panel.OrderListChoosePaymentPanel;
import com.magestore.app.pos.panel.OrderListPanel;
import com.magestore.app.pos.panel.OrderRefundPanel;
import com.magestore.app.pos.panel.OrderSendEmailPanel;
import com.magestore.app.pos.panel.OrderShipmentPanel;
import com.magestore.app.pos.panel.OrderTakePaymentPanel;
import com.magestore.app.pos.ui.AbstractActivity;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 1/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderHistoryListController extends AbstractListController<Order> {
    String mSearchStatus;
    OrderSendEmailPanel mOrderSendEmailPanel;

    OrderAddCommentPanel mOrderAddCommentPanel;
    OrderCommentListController mOrderCommentListController;
    OrderHistoryItemsListController mOrderHistoryItemsListController;
    OrderPaymentListController mOrderPaymentListController;

    OrderShipmentPanel mOrderShipmentPanel;
    OrderRefundPanel mOrderRefundPanel;
    OrderInvoicePanel mOrderInvoicePanel;
    OrderCancelPanel mOrderCancelPanel;
    OrderTakePaymentPanel mOrderTakePaymentPanel;
    OrderAddPaymentPanel mOrderAddPaymentPanel;
    OrderListChoosePaymentPanel mOrderListChoosePaymentPanel;

    public static int SENT_EMAIL_TYPE = 1;
    public static String SENT_EMAIL_CODE = "send_email";
    public static int INSERT_STATUS_TYPE = 2;
    public static String INSERT_STATUS_CODE = "insert_status";
    public static int CREATE_SHIPMENT_TYPE = 3;
    public static String CREATE_SHIPMENT_CODE = "create_shipment";
    public static int ORDER_REFUND_BY_CREDIT_TYPE = 4;
    public static String ORDER_REFUND_BY_CREDIT_CODE = "order_refund_by_credit";
    public static int ORDER_REFUND_BY_GIFTCARD_TYPE = 5;
    public static String ORDER_REFUND_BY_GIFTCARD_CODE = "order_refund_by_giftcard";
    public static int ORDER_REFUND_TYPE = 6;
    public static String ORDER_REFUND_CODE = "order_refund";
    public static int ORDER_INVOICE_UPDATE_QTY_TYPE = 7;
    public static String ORDER_INVOICE_UPDATE_QTY_CODE = "order_invoice_update_qty";
    public static int ORDER_INVOICE_TYPE = 8;
    public static String ORDER_INVOICE_CODE = "order_invoice";
    public static int ORDER_CANCEL_TYPE = 9;
    public static String ORDER_CANCEL_CODE = "order_cancel";
    public static int ORDER_REORDER_TYPE = 10;
    public static String ORDER_REORDER_CODE = "order_reorder";
    public static int RETRIEVE_PAYMENT_METHOD_TYPE = 11;
    public static String RETRIEVE_PAYMENT_METHOD_CODE = "retrieve_payment_method";
    public static int ORDER_TAKE_PAYMENT_TYPE = 12;
    public static String ORDER_TAKE_PAYMENT_CODE = "order_take_payment";

    public static String SEND_ORDER_TO_SALE_ACTIVITY = "com.magestore.app.pos.controller.orderhistory.reorder";

    Map<String, Object> wraper;
    String time_create = "";
    boolean isFirst = true;

    /**
     * Service xử lý các vấn đề liên quan đến order
     */
    OrderHistoryService mOrderService;
    RelativeLayout mLayoutOrderCommentLoading;

    /**
     * Thiết lập service
     *
     * @param mOrderService
     */
    public void setOrderService(OrderHistoryService mOrderService) {
        this.mOrderService = mOrderService;
        setListService(mOrderService);
    }

    /**
     * Trả lại order service
     *
     * @return
     */
    public OrderHistoryService getOrderService() {
        return mOrderService;
    }

    public void setOrderSendEmailPanel(OrderSendEmailPanel mOrderSendEmailPanel) {
        this.mOrderSendEmailPanel = mOrderSendEmailPanel;
    }

    public void setOrderAddCommentPanel(OrderAddCommentPanel mOrderAddCommentPanel) {
        this.mOrderAddCommentPanel = mOrderAddCommentPanel;
    }

    public void setOrderCommentListController(OrderCommentListController mOrderCommentListController) {
        this.mOrderCommentListController = mOrderCommentListController;
    }

    public OrderCommentListController getOrderCommentListController() {
        return mOrderCommentListController;
    }

    public OrderHistoryItemsListController getOrderHistoryItemsListController() {
        return mOrderHistoryItemsListController;
    }

    public void setOrderHistoryItemsListController(OrderHistoryItemsListController mOrderHistoryItemsListController) {
        this.mOrderHistoryItemsListController = mOrderHistoryItemsListController;
    }

    public void setOrderPaymentListController(OrderPaymentListController mOrderPaymentListController) {
        this.mOrderPaymentListController = mOrderPaymentListController;
    }

    public OrderPaymentListController getOrderPaymentListController() {
        return mOrderPaymentListController;
    }

    public void setOrderShipmentPanel(OrderShipmentPanel mOrderShipmentPanel) {
        this.mOrderShipmentPanel = mOrderShipmentPanel;
    }

    public void setOrderRefundPanel(OrderRefundPanel mOrderRefundPanel) {
        this.mOrderRefundPanel = mOrderRefundPanel;
    }

    public void setOrderInvoicePanel(OrderInvoicePanel mOrderInvoicePanel) {
        this.mOrderInvoicePanel = mOrderInvoicePanel;
    }

    public void setOrderCancelPanel(OrderCancelPanel mOrderCancelPanel) {
        this.mOrderCancelPanel = mOrderCancelPanel;
    }

    public void setOrderTakePaymentPanel(OrderTakePaymentPanel mOrderTakePaymentPanel) {
        this.mOrderTakePaymentPanel = mOrderTakePaymentPanel;
    }

    public void setOrderAddPaymentPanel(OrderAddPaymentPanel mOrderAddPaymentPanel) {
        this.mOrderAddPaymentPanel = mOrderAddPaymentPanel;
    }

    public void setOrderListChoosePaymentPanel(OrderListChoosePaymentPanel mOrderListChoosePaymentPanel) {
        this.mOrderListChoosePaymentPanel = mOrderListChoosePaymentPanel;
    }

    @Override
    public void onRetrievePostExecute(List<Order> list) {
        List<Order> listOrder = new ArrayList<>();
        for (Order order : list) {
            if (!ConfigUtil.formatDate(order.getCreatedAt()).equals(time_create)) {
                Order nOrder = mOrderService.create();
                nOrder.setIsCreateAtView(true);
                nOrder.setCreateAt(order.getCreatedAt());
                listOrder.add(nOrder);
                listOrder.add(order);
                time_create = ConfigUtil.formatDate(order.getCreatedAt());
            } else {
                listOrder.add(order);
            }
        }
        if (isFirst) {
            if (list != null && list.size() > 0) {
                setSelectedItem(list.get(0));
                bindItem(list.get(0));
                ((OrderListPanel) mView).setSelectPosition();
            }
            isFirst = false;
        }
        super.onRetrievePostExecute(listOrder);

        if (wraper == null) {
            wraper = new HashMap<>();
        }
    }

    @Override
    public void bindItem(Order item) {
        if (!item.IsCreateAtView())
            super.bindItem(item);
    }

    public void doInputSendEmail(Map<String, Object> paramSendEmail) {
        showDetailOrderLoading(true);
        doAction(SENT_EMAIL_TYPE, SENT_EMAIL_CODE, paramSendEmail, null);
    }

    public void doInputCreateShipment(Order order) {
        showDetailOrderLoading(true);
        doAction(CREATE_SHIPMENT_TYPE, CREATE_SHIPMENT_CODE, wraper, order);
    }

    public void doInputInsertStatus(Order order) {
        showDetailOrderLoading(true);
        doAction(INSERT_STATUS_TYPE, INSERT_STATUS_CODE, wraper, order);
    }

    public void doInputRefundByGiftCard(Order order) {
        showDetailOrderLoading(true);
        doAction(ORDER_REFUND_BY_GIFTCARD_TYPE, ORDER_REFUND_BY_GIFTCARD_CODE, wraper, order);
    }

    public void doInputRefundByCredit(Order order) {
        showDetailOrderLoading(true);
        doAction(ORDER_REFUND_BY_CREDIT_TYPE, ORDER_REFUND_BY_CREDIT_CODE, wraper, order);
    }

    public void doInputRefund(Order order) {
        showDetailOrderLoading(true);
        doAction(ORDER_REFUND_TYPE, ORDER_REFUND_CODE, wraper, order);
    }

    public void doInputInvoiceUpdateQty(OrderUpdateQtyParam orderUpdateQtyParam) {
        doAction(ORDER_INVOICE_UPDATE_QTY_TYPE, ORDER_INVOICE_UPDATE_QTY_CODE, wraper, orderUpdateQtyParam);
    }

    public void doInputInvoice(Order order) {
        showDetailOrderLoading(true);
        doAction(ORDER_INVOICE_TYPE, ORDER_INVOICE_CODE, wraper, order);
    }

    public void doInputCancel(Order order) {
        showDetailOrderLoading(true);
        doAction(ORDER_CANCEL_TYPE, ORDER_CANCEL_CODE, wraper, order);
    }

    public void doInputReorder(Order order) {
        showDetailOrderLoading(true);
//        doAction(ORDER_REORDER_TYPE, ORDER_REORDER_CODE, wraper, order);
        // chuyển order sang cho sales activity để re-order
        SalesActivity.mOrder = order;
        Intent intent = new Intent();
        intent.setAction(SEND_ORDER_TO_SALE_ACTIVITY);
        getMagestoreContext().getActivity().sendBroadcast(intent);
        getMagestoreContext().getActivity().finish();

        Intent i = new Intent();
        i.setAction(AbstractActivity.BACK_TO_HOME);
        getMagestoreContext().getActivity().sendBroadcast(i);
    }

    public void doRetrievePaymentMethod() {
        if (wraper == null) {
            wraper = new HashMap<>();
        }
        doAction(RETRIEVE_PAYMENT_METHOD_TYPE, RETRIEVE_PAYMENT_METHOD_CODE, wraper, null);
    }

    public void doInputTakePayment(Order order) {
        // Check payment khác null hay ko
        List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_choose_payment");
        if (listCheckoutPayment != null && listCheckoutPayment.size() > 0) {
            doAction(ORDER_TAKE_PAYMENT_TYPE, ORDER_TAKE_PAYMENT_CODE, wraper, order);
            showDetailOrderLoading(true);
        } else {
            // show notifi
            mOrderTakePaymentPanel.showNotifiSelectPayment();
        }
    }

    @Override
    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wraper, Model... models) throws Exception {
        if (actionType == SENT_EMAIL_TYPE) {
            String email = (String) wraper.get("email");
            String orderId = (String) wraper.get("order_id");
            String email_respone = mOrderService.sendEmail(email, orderId);
            if (StringUtil.isNullOrEmpty(email_respone)) {
                wraper.put("email_respone", email_respone);
                return true;
            } else {
                if (email_respone.equals("false")) {
                    return false;
                }
            }
            return true;
        } else if (actionType == CREATE_SHIPMENT_TYPE) {
            wraper.put("shipment_respone", mOrderService.createShipment((Order) models[0]));
            return true;
        } else if (actionType == INSERT_STATUS_TYPE) {
            wraper.put("status_respone", mOrderService.insertOrderStatus((Order) models[0]));
            return true;
        } else if (actionType == ORDER_REFUND_BY_GIFTCARD_TYPE) {
            wraper.put("refund_by_giftcard_respone", mOrderService.orderRefundByGiftCard((Order) models[0]));
            return true;
        } else if (actionType == ORDER_REFUND_BY_CREDIT_TYPE) {
            wraper.put("refund_by_credit_respone", mOrderService.orderRefundByCredit((Order) models[0]));
            return true;
        } else if (actionType == ORDER_REFUND_TYPE) {
            wraper.put("refund_respone", mOrderService.orderRefund((Order) models[0]));
            return true;
        } else if (actionType == ORDER_INVOICE_UPDATE_QTY_TYPE) {
            wraper.put("invoice_update_qty_respone", mOrderService.orderInvoiceUpdateQty((OrderUpdateQtyParam) models[0]));
            return true;
        } else if (actionType == ORDER_INVOICE_TYPE) {
            wraper.put("invoice_respone", mOrderService.orderInvoice((Order) models[0]));
            return true;
        } else if (actionType == ORDER_CANCEL_TYPE) {
            wraper.put("cancel_respone", mOrderService.orderCancel((Order) models[0]));
            return true;
        } else if (actionType == ORDER_REORDER_TYPE) {
            Order order = (Order) models[0];
            String Ids = getIdsItemInfoBuy(order);
            wraper.put("list_product", mOrderService.retrieveOrderItem(Ids));
            return true;
        } else if (actionType == RETRIEVE_PAYMENT_METHOD_TYPE) {
            wraper.put("list_payment", mOrderService.retrievePaymentMethod());
            return true;
        } else if (actionType == ORDER_TAKE_PAYMENT_TYPE) {
            List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_choose_payment");
            wraper.put("take_payment_respone", mOrderService.orderTakePayment(((Order) models[0]), listCheckoutPayment));
            return true;
        }
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        super.onActionPostExecute(success, actionType, actionCode, wraper, models);
        if (actionType == SENT_EMAIL_TYPE) {
            if (success) {
                if (wraper.get("email_respone") != null) {
                    String email_respone = (String) wraper.get("email_respone");
                    if (!StringUtil.isNullOrEmpty(email_respone)) {
                        mOrderSendEmailPanel.showAlertRespone(success, email_respone);
                    } else {
                        mOrderSendEmailPanel.showAlertRespone(success, "");
                    }
                } else {
                    mOrderSendEmailPanel.showAlertRespone(success, "");
                }
            } else {
                if (wraper.get("email_respone") != null) {
                    String email_respone = (String) wraper.get("email_respone");
                    if (!StringUtil.isNullOrEmpty(email_respone)) {
                        mOrderSendEmailPanel.showAlertRespone(success, email_respone);
                    } else {
                        mOrderSendEmailPanel.showAlertRespone(success, "");
                    }
                } else {
                    mOrderSendEmailPanel.showAlertRespone(success, "");
                }
            }
            showDetailOrderLoading(false);
        } else if (success && actionType == CREATE_SHIPMENT_TYPE) {
            Order order = (Order) wraper.get("shipment_respone");
            mOrderShipmentPanel.showAlertRespone(true);
            mOrderHistoryItemsListController.doSelectOrder(order);
            mOrderCommentListController.doSelectOrder(order);
            mOrderHistoryItemsListController.notifyDataSetChanged();
            mOrderCommentListController.notifyDataSetChanged();
            setNewOrderToList(((Order) models[0]), order);
            mView.notifyDataSetChanged();
            ((OrderDetailPanel) mDetailView).changeColorStatusOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).changeStatusTopOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).bindDataRespone(order);
            ((OrderDetailPanel) mDetailView).setOrder(order);
            showDetailOrderLoading(false);
        } else if (success && actionType == INSERT_STATUS_TYPE) {
            Order order = (Order) wraper.get("status_respone");
            mOrderAddCommentPanel.showAlertRespone(true);
            mOrderCommentListController.doSelectOrder(order);
            mOrderCommentListController.notifyDataSetChanged();
            ((OrderDetailPanel) mDetailView).bindDataRespone(order);
            ((OrderDetailPanel) mDetailView).setOrder(order);
            showDetailOrderLoading(false);
        } else if (success && actionType == ORDER_REFUND_BY_GIFTCARD_TYPE) {
            boolean refund_by_giftcard_respone = (boolean) wraper.get("refund_by_giftcard_respone");
            if (!refund_by_giftcard_respone) {
                ((OrderDetailPanel) mDetailView).showErrorRefund(0);
            }
        } else if (success && actionType == ORDER_REFUND_BY_CREDIT_TYPE) {
            boolean refund_by_credit_respone = (boolean) wraper.get("refund_by_credit_respone");
            if (!refund_by_credit_respone) {
                ((OrderDetailPanel) mDetailView).showErrorRefund(1);
            }
        } else if (success && actionType == ORDER_REFUND_TYPE) {
            Order order = (Order) wraper.get("refund_respone");
            mOrderRefundPanel.showAlertRespone(true);
            mOrderHistoryItemsListController.doSelectOrder(order);
            mOrderCommentListController.doSelectOrder(order);
            mOrderHistoryItemsListController.notifyDataSetChanged();
            mOrderCommentListController.notifyDataSetChanged();
            setNewOrderToList(((Order) models[0]), order);
            mView.notifyDataSetChanged();
            ((OrderDetailPanel) mDetailView).changeColorStatusOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).changeStatusTopOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).bindDataRespone(order);
            ((OrderDetailPanel) mDetailView).setOrder(order);
            showDetailOrderLoading(false);
        } else if (success && actionType == ORDER_INVOICE_UPDATE_QTY_TYPE) {
            Order order = (Order) wraper.get("invoice_update_qty_respone");
            mOrderInvoicePanel.bindTotal(order);
        } else if (success && actionType == ORDER_INVOICE_TYPE) {
            Order order = (Order) wraper.get("invoice_respone");
            mOrderInvoicePanel.showAlertRespone(true);
            mOrderHistoryItemsListController.doSelectOrder(order);
            mOrderCommentListController.doSelectOrder(order);
            mOrderHistoryItemsListController.notifyDataSetChanged();
            mOrderCommentListController.notifyDataSetChanged();
            setNewOrderToList(((Order) models[0]), order);
            ((OrderDetailPanel) mDetailView).changeColorStatusOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).changeStatusTopOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).bindDataRespone(order);
            ((OrderDetailPanel) mDetailView).setOrder(order);
            showDetailOrderLoading(false);
        } else if (success && actionType == ORDER_CANCEL_TYPE) {
            Order order = (Order) wraper.get("cancel_respone");
            mOrderCancelPanel.showAlertRespone(true);
            mOrderHistoryItemsListController.doSelectOrder(order);
            mOrderCommentListController.doSelectOrder(order);
            mOrderHistoryItemsListController.notifyDataSetChanged();
            mOrderCommentListController.notifyDataSetChanged();
            setNewOrderToList(((Order) models[0]), order);
            ((OrderListPanel) mView).notifyDataSetChanged();
            ((OrderDetailPanel) mDetailView).changeColorStatusOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).changeStatusTopOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).bindDataRespone(order);
            ((OrderDetailPanel) mDetailView).setOrder(order);
            showDetailOrderLoading(false);
        } else if (success && actionType == ORDER_REORDER_TYPE) {
            List<Product> listProduct = (List<Product>) wraper.get("list_product");
            Order order = (Order) models[0];
            order.setListProductReorder(listProduct);
            // chuyển order sang cho sales activity để re-order
            SalesActivity.mOrder = order;
            Intent intent = new Intent();
            intent.setAction(SEND_ORDER_TO_SALE_ACTIVITY);
            getMagestoreContext().getActivity().sendBroadcast(intent);
            getMagestoreContext().getActivity().finish();
        } else if (success && actionType == ORDER_TAKE_PAYMENT_TYPE) {
            Order order = (Order) wraper.get("take_payment_respone");
            mOrderHistoryItemsListController.doSelectOrder(order);
            mOrderCommentListController.doSelectOrder(order);
            mOrderPaymentListController.doSelectOrder(order);
            mOrderHistoryItemsListController.notifyDataSetChanged();
            mOrderCommentListController.notifyDataSetChanged();
            mOrderPaymentListController.notifyDataSetChanged();
            setNewOrderToList(((Order) models[0]), order);
            mView.notifyDataSetChanged();
            ((OrderDetailPanel) mDetailView).changeColorStatusOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).changeStatusTopOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).bindDataRespone(order);
            ((OrderDetailPanel) mDetailView).setOrder(order);
            showDetailOrderLoading(false);
        }
    }

    @Override
    public void onCancelledBackground(Exception exp, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        if (actionType == CREATE_SHIPMENT_TYPE) {
            mOrderShipmentPanel.showAlertRespone(false);
            showDetailOrderLoading(false);
        } else if (actionType == ORDER_CANCEL_TYPE) {
            mOrderCancelPanel.showAlertRespone(false);
            showDetailOrderLoading(false);
        } else if (actionType == ORDER_REFUND_TYPE) {
            mOrderRefundPanel.showAlertRespone(false);
            showDetailOrderLoading(false);
        } else if (actionType == ORDER_INVOICE_TYPE) {
            mOrderInvoicePanel.showAlertRespone(false);
            showDetailOrderLoading(false);
        } else if (actionType == INSERT_STATUS_TYPE) {
            mOrderAddCommentPanel.showAlertRespone(false);
            showDetailOrderLoading(false);
        } else if (actionType == SENT_EMAIL_TYPE) {
            if (wraper.get("email_respone") != null) {
                String email_respone = (String) wraper.get("email_respone");
                if (!StringUtil.isNullOrEmpty(email_respone)) {
                    mOrderSendEmailPanel.showAlertRespone(false, email_respone);
                } else {
                    mOrderSendEmailPanel.showAlertRespone(false, "");
                }
            } else {
                mOrderSendEmailPanel.showAlertRespone(false, "");
            }
            showDetailOrderLoading(false);
        } else if (actionType == ORDER_REFUND_BY_GIFTCARD_TYPE) {
            ((OrderDetailPanel) mDetailView).showErrorRefund(0);
        } else if (actionType == ORDER_REFUND_BY_CREDIT_TYPE) {
            ((OrderDetailPanel) mDetailView).showErrorRefund(1);
        } else {
            super.onCancelledBackground(exp, actionType, actionCode, wraper, models);
            showDetailOrderLoading(false);
        }
    }

    /**
     * cập nhật lại order trong list
     *
     * @param oldOrder
     * @param newOrder
     */
    private void setNewOrderToList(Order oldOrder, Order newOrder) {
        int index = mList.indexOf(oldOrder);
        if (index != -1) {
            mList.remove(index);
            mList.add(index, newOrder);
            bindList(mList);
        }
    }

    /**
     * set data cho list choose payment
     */
    public void bindDataListChoosePayment() {
        List<CheckoutPayment> list_payment = (List<CheckoutPayment>) wraper.get("list_payment");
        mOrderAddPaymentPanel.bindList(checkListPayment(list_payment));
    }

    private List<CheckoutPayment> checkListPayment(List<CheckoutPayment> listPayment) {
        List<CheckoutPayment> listChoosePayment = (List<CheckoutPayment>) wraper.get("list_choose_payment");
        List<CheckoutPayment> listPaymentActive = new ArrayList<>();
        listPaymentActive.addAll(listPayment);
        for (CheckoutPayment payment : listPayment) {
            if (listChoosePayment != null && listChoosePayment.size() > 0) {
                for (CheckoutPayment paymentChoose : listChoosePayment) {
                    if (paymentChoose.getCode().equals(payment.getCode())) {
                        listPaymentActive.remove(payment);
                    }
                }
            }
        }
        return listPaymentActive;
    }

    // khi thay đổi value từng payment update giá trị money
    public void updateMoneyTotal(boolean type, float totalPrice) {
        mOrderTakePaymentPanel.updateMoneyTotal(type, totalPrice);
    }

    /**
     * add thêm payment trong vào checkout
     *
     * @param method
     */
    public void onAddPaymentMethod(CheckoutPayment method) {
        Order mOrder = ((OrderDetailPanel) mDetailView).getOrder();
        List<CheckoutPayment> listPayment = (List<CheckoutPayment>) wraper.get("list_choose_payment");
//        checkIsPayLater(method, listPayment);
        float total = 0;
//        if (method.isPaylater().equals("1")) {
//            if (mOrder.getRemainMoney() > 0) {
//                isEnableButtonAddPayment(true);
//            } else {
//                isEnableButtonAddPayment(false);
//            }
//        } else {
        if (mOrder.getRemainMoney() > 0) {
            total = mOrder.getRemainMoney();
            isEnableButtonAddPayment(true);
        } else {
            total = ConfigUtil.convertToPrice(mOrder.getBaseTotalDue());
            isEnableButtonAddPayment(false);
        }
//        }

        method.setAmount(total);
        method.setBaseAmount(total);
        method.setRealAmount(total);
        method.setBaseRealAmount(total);

        if (listPayment == null) {
            listPayment = new ArrayList<>();
        }
        listPayment.add(method);
        wraper.put("list_choose_payment", listPayment);
        mOrderListChoosePaymentPanel.bindList(listPayment);
        mOrderListChoosePaymentPanel.updateTotal(listPayment);
        mOrderTakePaymentPanel.showPanelListChoosePayment();
        mOrderTakePaymentPanel.hideCheckPaymenrRequired();
    }

    /**
     * xóa 1 payment method  checkout
     */
    public void onRemovePaymentMethod() {
        List<CheckoutPayment> listPayment = (List<CheckoutPayment>) wraper.get("list_choose_payment");
        Order mOrder = ((OrderDetailPanel) mDetailView).getOrder();
        if (listPayment.size() == 0) {
            mOrderTakePaymentPanel.showPanelAddPaymentMethod();
            mOrderTakePaymentPanel.bindTotalPrice(mOrder.getTotalDue());
            isEnableButtonAddPayment(false);
            bindDataListChoosePayment();
        }
    }

    /**
     * kiểm tra nếu payment truyền vào ko phải pay later thì remove all payment is_pay_later
     *
     * @param checkoutPayment
     * @param listPayment
     */
    public void checkIsPayLater(CheckoutPayment checkoutPayment, List<CheckoutPayment> listPayment) {
        if (!checkoutPayment.isPaylater().equals("1")) {
            if (listPayment != null && listPayment.size() > 0) {
                for (int i = 0; i < listPayment.size(); i++) {
                    CheckoutPayment payment = listPayment.get(i);
                    if (payment.isPaylater().equals("1")) {
                        listPayment.remove(payment);
                        i--;
                    }
                }
            }
        }
    }

    // lấy toàn bị id item để lấy full thông tin product
    private String getIdsItemInfoBuy(Order order) {
//        List<CartItem> listItems = order.getItemsInfoBuy().getListItems();
        String Ids = "";
//        for (CartItem item : listItems) {
//            if (!item.getID().equals("custom_item")) {
//                Ids = item.getID() + ",";
//            }
//        }
        return Ids;
    }

    /* Felix 3/4/2017 Start*/
    public boolean checkDimissDialogTakePayment(Order order) {
        List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_choose_payment");
        if (listCheckoutPayment != null && listCheckoutPayment.size() > 0) {
            return true;
        }

        return false;
    }
    /* Felix 3/4/2017 End*/

    /**
     * ẩn hoặc hiện button add payment
     *
     * @param enable
     */
    public void isEnableButtonAddPayment(boolean enable) {
        mOrderTakePaymentPanel.isEnableButtonAddPayment(enable);
    }

    public OrderStatus createOrderStatus() {
        return mOrderService.createOrderStatus();
    }

    public OrderShipmentParams createOrderShipmentParams() {
        return mOrderService.createOrderShipmentParams();
    }

    public OrderShipmentTrackParams createOrderShipmentTrackParams() {
        return mOrderService.createOrderShipmentTrackParams();
    }

    public List<OrderShipmentTrackParams> createListTrack() {
        return mOrderService.createListTrack();
    }

    public OrderCommentParams createCommentParams() {
        return mOrderService.createCommentParams();
    }

    public List<OrderCommentParams> createListComment() {
        return mOrderService.createListComment();
    }

    public OrderRefundParams createOrderRefundParams() {
        return mOrderService.createOrderRefundParams();
    }

    public OrderInvoiceParams createOrderInvoiceParams() {
        return mOrderService.createOrderInvoiceParams();
    }

    public OrderUpdateQtyParam createOrderUpdateQtyParam() {
        return mOrderService.createOrderUpdateQtyParam();
    }

    public OrderItemUpdateQtyParam creaOrderItemUpdateQtyParam() {
        return mOrderService.createOrderItemUpdateQtyParam();
    }

    /*Felix 3/4/2017 Start*/
    public void showDetailOrderLoading(boolean visible) {
        ((OrderDetailPanel) mDetailView).showDetailOrderLoading(visible);
    }
    /*Felix 3/4/2017 End*/

    public boolean checkCanInvoice(Order order) {
        return mOrderService.checkCanInvoice(order);
    }

    public boolean checkCanTakePayment(Order order) {
        return mOrderService.checkCanTakePayment(order);
    }

    public boolean checkCanCancel(Order order) {
        return mOrderService.checkCanCancel(order);
    }

    public boolean checkCanRefund(Order order) {
        return mOrderService.checkCanRefund(order);
    }

    public boolean checkCanShip(Order order) {
        return mOrderService.checkCanShip(order);
    }

    public float checkShippingRefund(Order order) {
        return mOrderService.checkShippingRefund(order);
    }

    public boolean checkCanRefundGiftcard(Order order) {
        return mOrderService.checkCanRefundGiftcard(order);
    }

    public boolean checkCanStoreCredit(Order order) {
        return mOrderService.checkCanStoreCredit(order);
    }

    /**
     * Thực hiện search theo status
     *
     * @param searchStatus
     */
    public void doSearchStatus(String searchStatus) {
        mSearchStatus = searchStatus;
        reload();
    }

    /**
     * Hướng tìm kiếm theo status
     *
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    protected List<Order> callRetrieveService(int page, int pageSize) throws Exception {
        if (StringUtil.isNullOrEmpty(mSearchStatus) || !(getListService() instanceof OrderHistoryService))
            return super.callRetrieveService(page, pageSize);
        else {
            OrderHistoryService service = (OrderHistoryService) getListService();
            if (StringUtil.isNullOrEmpty(getSearchString()))
                return service.retrieve(page, pageSize, mSearchStatus);
            else
                return service.retrieve(getSearchString(), page, pageSize, mSearchStatus);
        }
    }

    public void getTotalItem() {
        Order order = ((OrderDetailPanel) mDetailView).getOrder();
        float total_item_price = 0;
        float total_giftcard = 0;
        float ratioGiftVoucher = (0 - order.getBaseGiftVoucherDiscount()) / order.getBaseSubtotal();
        for (CartItem cart : checkParentItem(order)) {
            cart.setQuantity(cart.QtyRefund());
            total_item_price += ((cart.getBasePriceInclTax() - ((cart.getBaseDiscountAmount() + cart.getBaseGiftVoucherDiscount() + cart.getRewardpointsBaseDiscount()) / cart.getQtyOrdered())) * cart.QtyRefund());
            total_giftcard += cart.getBasePrice() * ratioGiftVoucher * cart.getQuantity();
        }
        ((OrderDetailPanel) mDetailView).getOrder().setMaxGiftCardRefund(total_giftcard);
        float max_refund = order.getBaseTotalPaid() - order.getWebposBaseChange() - order.getBaseTotalRefunded() + ((0 - order.getBaseGiftVoucherDiscount()) - total_giftcard);
        ((OrderDetailPanel) mDetailView).getOrder().setMaxRefunded(max_refund);
        updateMaxRefundGiftCard(total_giftcard);
        updateToTalPriceChangeQtyRefund(total_item_price);
    }

    private List<CartItem> checkParentItem(Order order) {
        List<CartItem> listCartItems = new ArrayList<>();
        for (CartItem cart : order.getOrderItems()) {
            if (cart.getOrderParentItem() == null) {
                listCartItems.add(cart);
            }
        }
        return listCartItems;
    }

    public void updateToTalPriceChangeQtyRefund(float total) {
        ((OrderDetailPanel) mDetailView).getOrder().setTotalPriceChangeQtyRefund(total);
        chaneMaxStoreCreditRefund();
    }

    public void updateMaxRefundGiftCard(float total) {
        ((OrderDetailPanel) mDetailView).getOrder().setMaxGiftCardRefund(total);
        mOrderRefundPanel.updateTotalGiftCard(total);
    }

    public void chaneMaxStoreCreditRefund() {
        Order order = ((OrderDetailPanel) mDetailView).getOrder();
        float total_price_qty_item = order.getTotalPriceChangeQtyRefund();
        float price_shipping = order.getRefundShipping();
        float adjust_refund = order.getAdjustRefund();
        float adjust_free = order.getAdjustFree();
        float max_store_credit = ((total_price_qty_item + price_shipping + adjust_refund) - (adjust_free + order.getMaxGiftCardRefund()));
        float max_refunded = order.getMaxRefunded();
        order.setMaxStoreCreditRefund(max_store_credit);
        if (max_store_credit <= max_refunded) {
            mOrderRefundPanel.updateTotalStoreCredit(max_store_credit);
            order.setStoreCreditRefund(max_store_credit);
        } else {
            mOrderRefundPanel.updateTotalStoreCredit(order.getMaxRefunded());
            order.setStoreCreditRefund(order.getMaxRefunded());
            order.setMaxStoreCreditRefund(order.getMaxRefunded());
        }
    }

    public void setTotalOrder(Order newOrder, Order oldOrder) {
        mOrderService.setTotalOrder(newOrder, oldOrder);
    }

    public Order getOrder() {
        return ((OrderDetailPanel) mDetailView).getOrder();
    }

    public void resetListChoosePayment() {
        List<CheckoutPayment> listPayment = (List<CheckoutPayment>) wraper.get("list_choose_payment");
        if (listPayment != null) {
            listPayment = new ArrayList<>();
            wraper.put("list_choose_payment", listPayment);
        }
    }
}
