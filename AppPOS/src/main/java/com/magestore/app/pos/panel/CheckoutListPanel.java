package com.magestore.app.pos.panel;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.checkout.QuoteAddCouponParam;
import com.magestore.app.lib.model.checkout.SaveQuoteParam;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.observ.GenericState;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.pos.R;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.databinding.PanelCheckoutListBinding;
import com.magestore.app.pos.util.DialogUtil;
import com.magestore.app.pos.view.MagestoreDialog;
import com.magestore.app.util.ConfigUtil;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */
public class CheckoutListPanel extends AbstractListPanel<Checkout> {
    private PanelCheckoutListBinding mBinding;
    CheckoutAddCustomerPanel mCheckoutAddCustomerPanel;
    MagestoreDialog dialog;
    Checkout mCheckout;
    MagestoreContext mMagestoreContext;
    CustomerService mCustomerService;
    Toolbar toolbar_order;
    Customer mCustomer;
    CustomerAddNewPanel mCustomerAddNewPanel;
    FloatingActionButton bt_sales_discount, bt_custom_sales, bt_remove_discount;
    FloatingActionMenu bt_sales_menu;
    Button btn_create_customer, btn_use_guest, btn_apply_discount;
    FrameLayout fr_sales_new_customer;
    LinearLayout ll_add_new_customer, ll_new_shipping_address, ll_new_billing_address, ll_shipping_address, ll_sales_shipping, ll_add_new_address;
    View ll_plugins;
    LinearLayout ll_billing_address, ll_short_shipping_address, ll_short_billing_address, ll_sales_add_customer, ll_action_checkout, ll_sales_tax, ll_grand_total;
    ImageView btn_shipping_address, btn_billing_address;
    ImageButton btn_shipping_adrress_edit, btn_billing_adrress_edit;
    ImageButton btn_shipping_address_delete, btn_billing_address_delete;
    RelativeLayout rl_add_checkout, rl_remove_checkout, cart_background_loading;
    TextView txt_sales_discount, txt_sales_promotion, text_reward_point_earn, btn_sales_order_checkout;
    public static int NO_TYPE = -1;
    public static int CHANGE_CUSTOMER = 0;
    public static int CREATE_NEW_CUSTOMER = 1;
    public static int CREATE_NEW_ADDRESS = 2;
    public static int CHECKOUT_ADD_NEW_ADDRESS = 3;
    int typeCustomer, other_type;

    public CheckoutListPanel(Context context) {
        super(context);
    }

    public CheckoutListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCustomerService(CustomerService mCustomerService) {
        this.mCustomerService = mCustomerService;
    }

    public void setMagestoreContext(MagestoreContext mMagestoreContext) {
        this.mMagestoreContext = mMagestoreContext;
    }

    public void setToolbarOrder(Toolbar toolbar_order) {
        this.toolbar_order = toolbar_order;
    }

    @Override
    protected void bindItem(View view, Checkout item, int position) {
        mCheckout = item;
    }

    @Override
    public void initLayout() {
        super.initLayout();
        mBinding = DataBindingUtil.bind(getView());

        btn_sales_order_checkout = (TextView) findViewById(R.id.btn_sales_order_checkout);
        ll_grand_total = (LinearLayout) findViewById(R.id.ll_grand_total);
        ll_action_checkout = (LinearLayout) findViewById(R.id.ll_action_checkout);
        ll_sales_shipping = (LinearLayout) findViewById(R.id.ll_sales_shipping);
        ll_sales_tax = (LinearLayout) findViewById(R.id.ll_sales_tax);
        ll_plugins = (View) findViewById(R.id.ll_checkout_plugins);
        text_reward_point_earn = (TextView) findViewById(R.id.text_reward_point_earn);
        rl_add_checkout = (RelativeLayout) findViewById(R.id.rl_add_checkout);
        rl_remove_checkout = (RelativeLayout) findViewById(R.id.rl_remove_checkout);
        bt_sales_menu = (FloatingActionMenu) findViewById(R.id.bt_sales_menu);
        bt_sales_discount = (FloatingActionButton) findViewById(R.id.bt_sales_discount);
        bt_custom_sales = (FloatingActionButton) findViewById(R.id.bt_custom_sales);
        bt_remove_discount = (FloatingActionButton) findViewById(R.id.bt_remove_discount);
        bt_sales_menu.setClosedOnTouchOutside(true);
        bt_sales_menu.open(true);
        cart_background_loading = (RelativeLayout) findViewById(R.id.cart_background_loading);
        initValue();
    }

    @Override
    public void initValue() {
        ll_action_checkout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String place_order = getContext().getString(R.string.sales_place_holder);
                String mark_as_partial = getContext().getString(R.string.sales_mark_as_partial);
                if (btn_sales_order_checkout.getText().equals(place_order) || btn_sales_order_checkout.getText().equals(mark_as_partial)) {
                    ((CheckoutListController) getController()).doInputPlaceOrder();
                } else {
                    ((CheckoutListController) getController()).setCheckSwitch(false);
                    ((CheckoutListController) getController()).doInputSaveCart();
                }
            }
        });

        rl_add_checkout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CheckoutListController) mController).addNewOrder();
            }
        });

        rl_remove_checkout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConfigUtil.isEnableDeleteOrder()) {
//                    if (((CheckoutListController) mController).checkItemInOrder()) {
                    com.magestore.app.util.DialogUtil.confirm(getContext(),
                            R.string.checkout_delete_order,
                            R.string.confirm,
                            R.string.no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ((CheckoutListController) mController).removeOrder();
                                }
                            });
//                    }
                } else {
                    ((CheckoutListController) mController).removeOrder();
                }
            }
        });

        bt_sales_discount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final CheckoutDiscountPanel mCheckoutDiscountPanel = new CheckoutDiscountPanel(getContext());
                mCheckoutDiscountPanel.setCheckoutListController(((CheckoutListController) mController));
                mCheckoutDiscountPanel.initValue();

                final int maxumum_discount = (int) ((CheckoutListController) mController).getMaximumDiscount();

                final MagestoreDialog dialog = DialogUtil.dialog(getContext(), "", mCheckoutDiscountPanel);
                dialog.setDialogWidth(getContext().getResources().getDimensionPixelSize(R.dimen.card_item_show_discount));
                dialog.setDialogTitle(getContext().getString(R.string.checkout_discount_all, String.valueOf(maxumum_discount)));
                dialog.setGoneButtonSave(true);
                dialog.show();

                btn_apply_discount = (Button) dialog.findViewById(R.id.btn_apply_discount);
                txt_sales_discount = (TextView) dialog.findViewById(R.id.txt_sales_discount);
                txt_sales_promotion = (TextView) dialog.findViewById(R.id.txt_sales_promotion);

                if (ConfigUtil.isDiscountPerCart()) {
                    dialog.getDialogTitle().setText(getContext().getString(R.string.checkout_discount_all, String.valueOf(maxumum_discount)));
                    mCheckoutDiscountPanel.onClickDiscount();
                } else {
                    dialog.getDialogTitle().setText("");
                    mCheckoutDiscountPanel.onClickPromotion();
                }

                txt_sales_discount.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ConfigUtil.isDiscountPerCart()) {
                            dialog.getDialogTitle().setText(getContext().getString(R.string.checkout_discount_all, String.valueOf(maxumum_discount)));
                            mCheckoutDiscountPanel.onClickDiscount();
                        } else {
                            showError(getContext().getString(R.string.error_permisson_discount_cart));
                        }
                    }
                });

                txt_sales_promotion.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ConfigUtil.isApplyCoupon()) {
                            dialog.getDialogTitle().setText("");
                            mCheckoutDiscountPanel.onClickPromotion();
                        } else {
                            showError(getContext().getString(R.string.error_permisson_apply_coupon));
                        }
                    }
                });

                btn_apply_discount.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mCheckoutDiscountPanel.checkViewDiscount()) {
                            SaveQuoteParam saveQuoteParam = mCheckoutDiscountPanel.bindSaveQuoteItem();
                            if (saveQuoteParam.getDiscountValue() > 0) {
                                ((CheckoutListController) mController).doInputSaveCartDiscount(0, saveQuoteParam, null);
                            }
                        } else {
                            QuoteAddCouponParam quoteAddCouponParam = mCheckoutDiscountPanel.binQuoteAddCouponItem();
                            ((CheckoutListController) mController).doInputSaveCartDiscount(1, null, quoteAddCouponParam);
                        }
                        bt_sales_menu.toggle(false);
                        dialog.dismiss();
                    }
                });
            }
        });

        bt_custom_sales.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CheckoutListController) getController()).onShowCustomSale();
            }
        });

        bt_remove_discount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CheckoutListController) mController).doInputRemoveDiscount(getContext().getString(R.string.sales_discount_currency));
            }
        });

        bt_sales_menu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    ((CheckoutListController) mController).checkShowRemoveDiscount();
                }
            }
        });
    }

    private void showError(String message) {
        // Tạo dialog và hiển thị
        com.magestore.app.util.DialogUtil.confirm(getContext(), message, R.string.ok);
    }

    public void useDefaultGuestCheckout(Customer customer) {
        // config guest checkout default add to order
        mCustomer = customer;
        updateCustomerToOrder(mCustomer);
    }

    public void showSalesShipping(boolean isShow) {
        ll_sales_shipping.setVisibility(isShow ? VISIBLE : GONE);
        ll_plugins.setVisibility(isShow ? VISIBLE : GONE);
//        ll_sales_tax.setVisibility(isShow ? VISIBLE : GONE);
        text_reward_point_earn.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void changeActionButton(boolean ischange) {
        if (ischange) {
            btn_sales_order_checkout.setText(getContext().getString(R.string.sales_place_holder));
        } else {
            btn_sales_order_checkout.setText(getContext().getString(R.string.checkout));
        }
    }

    public void changeTitlePlaceOrder(boolean ischange) {
        if (ischange) {
            btn_sales_order_checkout.setText(getContext().getString(R.string.sales_mark_as_partial));
        } else {
            btn_sales_order_checkout.setText(getContext().getString(R.string.sales_place_holder));
        }
    }

    public void setEnableBtCheckout(boolean isEnable) {
        ll_action_checkout.setEnabled(isEnable ? true : false);
    }

    public void showButtonRemoveDiscount(boolean isShow) {
        bt_remove_discount.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void showButtonCheckout(boolean isShow) {
        ll_action_checkout.setEnabled(isShow ? true : false);
    }

    public void showSalesMenuDiscount(boolean isShow) {
        bt_sales_menu.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void showSalesMenuToggle(boolean isShow) {
        bt_sales_menu.toggle(isShow ? true : false);
    }

    public void showSalesMenuToCheckout(boolean isShow) {
        bt_custom_sales.setVisibility(isShow ? VISIBLE : GONE);
        if (checkShowButtonDiscount()) {
            bt_sales_discount.setVisibility(((CheckoutListController) mController).checkListCartItem() ? VISIBLE : GONE);
        } else {
            bt_sales_discount.setVisibility(GONE);
        }
    }

    public void showButtonCustomSales(boolean isShow) {
        bt_custom_sales.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void showButtonDiscount(boolean isShow) {
        if (checkShowButtonDiscount()) {
            bt_sales_discount.setVisibility(isShow ? VISIBLE : GONE);
        } else {
            bt_sales_discount.setVisibility(GONE);
        }
    }

    private boolean checkShowButtonDiscount() {
        if (ConfigUtil.isManageAllDiscount()) {
            return true;
        } else {
            if (ConfigUtil.isDiscountPerCart() || ConfigUtil.isApplyCoupon()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void showDiscountWithPermisson() {
        if (checkShowButtonDiscount()) {
            bt_sales_discount.setVisibility(((CheckoutListController) mController).checkListCartItem() ? VISIBLE : GONE);
        } else {
            bt_sales_discount.setVisibility(GONE);
        }
    }

    public void showCheckoutPermisson() {
        ll_grand_total.setVisibility(ConfigUtil.isCreateOrder() ? GONE : VISIBLE);
        ll_action_checkout.setVisibility(ConfigUtil.isCreateOrder() ? VISIBLE : GONE);
    }

    /**
     * Cập nhật bảng giá tổng
     */
    public void updateTotalPrice(Checkout checkout) {
        mBinding.setCheckout(checkout);
        text_reward_point_earn.setText(getContext().getString(R.string.plugin_reward_point_earn, ConfigUtil.formatNumber(checkout.getRewardPointEarnPointValue())));
    }

    public void showPopUpAddCustomer(int type, int other_type) {
        // TODO: check cơ chế customer dialog
        Customer guest_customer = ((CheckoutListController) mController).getGuestCheckout();
        if (mCustomer != null && !((CheckoutListController) mController).checkCustomerID(mCustomer, guest_customer)) {
            type = CHANGE_CUSTOMER;
        }
        this.other_type = other_type;
        typeCustomer = type;
        if (mCheckoutAddCustomerPanel == null) {
            mCheckoutAddCustomerPanel = new CheckoutAddCustomerPanel(getContext());
            mCheckoutAddCustomerPanel.setMagestoreContext(mMagestoreContext);
            mCheckoutAddCustomerPanel.setCustomerService(mCustomerService);
            mCheckoutAddCustomerPanel.setCheckoutListPanel(this);
            mCheckoutAddCustomerPanel.bindItem(mCheckout);
            mCheckoutAddCustomerPanel.setController(mController);
            mCheckoutAddCustomerPanel.initModel();
            mCheckoutAddCustomerPanel.initValue();

            mCustomerAddNewPanel = (CustomerAddNewPanel) mCheckoutAddCustomerPanel.findViewById(R.id.sales_new_customer);
            mCustomerAddNewPanel.setController(mCheckoutAddCustomerPanel.getCustomerListController());
            initLayoutPanel();
            if (type == CHANGE_CUSTOMER) {
                mCustomerAddNewPanel.bindItem(null);
                ll_add_new_address.setVisibility(GONE);
            } else {
                mCustomerAddNewPanel.bindItem(mCustomer);
                ll_add_new_address.setVisibility(VISIBLE);
            }
        } else {
            if (mCustomer != null) {
                if (type == CHANGE_CUSTOMER) {
                    mCustomerAddNewPanel.bindItem(null);
                    ll_add_new_address.setVisibility(VISIBLE);
                } else {
                    mCustomerAddNewPanel.bindItem(mCustomer);
                    ll_add_new_address.setVisibility(VISIBLE);
                }
            } else {
                ll_add_new_address.setVisibility(GONE);
            }
        }
        mCheckoutAddCustomerPanel.scrollToTop();

        if (dialog == null) {
            dialog = DialogUtil.dialog(getContext(), "", mCheckoutAddCustomerPanel);
            dialog.setDialogWidth(getContext().getResources().getDimensionPixelSize(R.dimen.dialog_width));
            dialog.setGoneButtonSave(true);
        }

        dialog.show();

        if (mCustomer == null) {
            fr_sales_new_customer.setVisibility(GONE);
            ll_sales_add_customer.setVisibility(VISIBLE);
            dialog.getButtonSave().setVisibility(GONE);
            dialog.getDialogTitle().setText("");
        } else {
            if (type == CHANGE_CUSTOMER) {
                fr_sales_new_customer.setVisibility(GONE);
                ll_sales_add_customer.setVisibility(VISIBLE);
                dialog.getButtonSave().setVisibility(GONE);
                dialog.getDialogTitle().setText("");
            } else {
                fr_sales_new_customer.setVisibility(VISIBLE);
                ll_sales_add_customer.setVisibility(GONE);
                dialog.getButtonSave().setVisibility(VISIBLE);
                dialog.getDialogTitle().setText(mCustomer.getName());
            }
        }

        actionPanel();
        actionDialog();
    }

    /**
     * khởi tạo layout
     */
    private void initLayoutPanel() {
        btn_create_customer = (Button) mCheckoutAddCustomerPanel.findViewById(R.id.btn_create_customer);
        btn_use_guest = (Button) mCheckoutAddCustomerPanel.findViewById(R.id.btn_use_guest);
        fr_sales_new_customer = (FrameLayout) mCheckoutAddCustomerPanel.findViewById(R.id.fr_sales_new_customer);
        ll_sales_add_customer = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_sales_add_customer);
        ll_add_new_customer = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_add_new_customer);
        ll_new_shipping_address = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_new_shipping_address);
        ll_new_billing_address = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_new_billing_address);
        ll_shipping_address = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_shipping_address);
        ll_billing_address = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_billing_address);
        ll_short_shipping_address = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_short_shipping_address);
        ll_short_billing_address = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_short_billing_address);
        btn_shipping_address = (ImageView) mCheckoutAddCustomerPanel.findViewById(R.id.btn_shipping_address);
        btn_billing_address = (ImageView) mCheckoutAddCustomerPanel.findViewById(R.id.btn_billing_address);
        btn_shipping_adrress_edit = (ImageButton) mCheckoutAddCustomerPanel.findViewById(R.id.btn_shipping_adrress_edit);
        btn_billing_adrress_edit = (ImageButton) mCheckoutAddCustomerPanel.findViewById(R.id.btn_billing_adrress_edit);
        btn_shipping_address_delete = (ImageButton) mCheckoutAddCustomerPanel.findViewById(R.id.btn_shipping_address_delete);
        btn_billing_address_delete = (ImageButton) mCheckoutAddCustomerPanel.findViewById(R.id.btn_billing_address_delete);
        ll_add_new_address = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_add_new_address);
    }

    private void actionPanel() {
        btn_use_guest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCustomer = null;
                mCustomer = ((CheckoutListController) getController()).getGuestCheckout();
                updateCustomerToOrder(mCustomer);
                dialog.dismiss();
            }
        });

        btn_create_customer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.getButtonSave().setVisibility(VISIBLE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
                fr_sales_new_customer.setVisibility(VISIBLE);
                ll_sales_add_customer.setVisibility(GONE);
                mCustomerAddNewPanel.bindItem(null);
                mCustomerAddNewPanel.deleteBillingAddress();
                mCustomerAddNewPanel.deleteShippingAddress();
                typeCustomer = CREATE_NEW_CUSTOMER;
                ll_add_new_address.setVisibility(GONE);
            }
        });

        ll_add_new_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.getButtonSave().setVisibility(VISIBLE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new_address));
                typeCustomer = CREATE_NEW_ADDRESS;
                ll_add_new_customer.setVisibility(GONE);
                mCustomerAddNewPanel.deleteBillingAddress();
                ll_new_billing_address.setVisibility(VISIBLE);
                ll_new_shipping_address.setVisibility(GONE);
                dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
                mCustomerAddNewPanel.insertCustomerNameToAddress();
            }
        });
    }

    private void actionDialog() {
        ll_shipping_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_new_shipping_address.setVisibility(VISIBLE);
                ll_add_new_customer.setVisibility(GONE);
                ll_new_billing_address.setVisibility(GONE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_shipping_address));
                if (mCustomer == null) {
                    if (mCustomerAddNewPanel.getShippingAddress() != null) {
                        dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                    }
                } else {
                    if (typeCustomer == CREATE_NEW_CUSTOMER) {
                        if (mCustomerAddNewPanel.getShippingAddress() != null) {
                            dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                        } else {
                            dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
                        }
                    }
                }
                mCustomerAddNewPanel.insertCustomerNameToAddress();
            }
        });

        ll_billing_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_new_billing_address.setVisibility(VISIBLE);
                ll_add_new_customer.setVisibility(GONE);
                ll_new_shipping_address.setVisibility(GONE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_billing_address));
                if (mCustomer == null) {
                    if (mCustomerAddNewPanel.getBillingAddress() != null) {
                        dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                    }
                } else {
                    if (typeCustomer == CREATE_NEW_CUSTOMER) {
                        if (mCustomerAddNewPanel.getBillingAddress() != null) {
                            dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                        } else {
                            dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
                        }
                    }
                }
                mCustomerAddNewPanel.insertCustomerNameToAddress();
            }
        });

        dialog.getButtonCancel().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDialogCancel();
            }
        });

        dialog.getButtonSave().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDialogSave();
            }
        });

        btn_shipping_adrress_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_new_shipping_address.setVisibility(VISIBLE);
                ll_add_new_customer.setVisibility(GONE);
                ll_new_billing_address.setVisibility(GONE);
                if (mCustomer == null) {
                    dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_shipping_address));
                    dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                } else {
                    if (typeCustomer == CREATE_NEW_CUSTOMER) {
                        dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_shipping_address));
                        dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                    } else {
                        dialog.getDialogTitle().setText(getContext().getString(R.string.customer_edit_shipping_address));
                    }
                }
            }
        });

        btn_billing_adrress_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_new_billing_address.setVisibility(VISIBLE);
                ll_add_new_customer.setVisibility(GONE);
                ll_new_shipping_address.setVisibility(GONE);
                if (mCustomer == null) {
                    dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_billing_address));
                    dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                } else {
                    if (typeCustomer == CREATE_NEW_CUSTOMER) {
                        dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_billing_address));
                        dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                    } else {
                        dialog.getDialogTitle().setText(getContext().getString(R.string.customer_edit_billing_address));
                    }
                }
            }
        });

        btn_shipping_address_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCustomer != null) {
                    if (typeCustomer == CREATE_NEW_CUSTOMER) {
                        ll_short_shipping_address.setVisibility(GONE);
                        btn_shipping_address.setVisibility(VISIBLE);
                        mCustomerAddNewPanel.deleteShippingAddress();
                    } else {
                        ((CheckoutListController) getController()).doInputDeleteAddress(0, mCustomer, mCustomerAddNewPanel.getChangeshippingAddress());
                    }
                } else {
                    ll_short_shipping_address.setVisibility(GONE);
                    btn_shipping_address.setVisibility(VISIBLE);
                    mCustomerAddNewPanel.deleteShippingAddress();
                }
            }
        });

        btn_billing_address_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCustomer != null) {
                    if (typeCustomer == CREATE_NEW_CUSTOMER) {
                        ll_short_billing_address.setVisibility(GONE);
                        btn_billing_address.setVisibility(VISIBLE);
                        mCustomerAddNewPanel.deleteBillingAddress();
                    } else {
                        ((CheckoutListController) getController()).doInputDeleteAddress(1, mCustomer, mCustomerAddNewPanel.getChangebillingAddress());
                    }
                } else {
                    ll_short_billing_address.setVisibility(GONE);
                    btn_billing_address.setVisibility(VISIBLE);
                    mCustomerAddNewPanel.deleteBillingAddress();
                }
            }
        });
    }

    private void onClickDialogSave() {
        if (ll_new_shipping_address.getVisibility() == VISIBLE) {
            mCustomerAddNewPanel.insertShippingAddress();
            if (mCustomer != null) {
                if (!mCustomerAddNewPanel.checkRequiedShippingAddress()) {
                    return;
                }
                if (typeCustomer == CREATE_NEW_CUSTOMER) {
                    if (mCustomerAddNewPanel.checkSameBillingAndShipping()) {
                        ll_short_billing_address.setVisibility(VISIBLE);
                        mCustomerAddNewPanel.showNewShortBillingAddress();
                        btn_billing_address.setVisibility(GONE);
                    }
                    dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
                } else {
                    ((CheckoutListController) getController()).doInputEditAddress(0, mCustomer, mCustomerAddNewPanel.getChangeshippingAddress(), mCustomerAddNewPanel.getShippingAddress());
                }
            } else {
                if (!mCustomerAddNewPanel.checkRequiedShippingAddress()) {
                    return;
                }
                if (mCustomerAddNewPanel.checkSameBillingAndShipping()) {
                    ll_short_billing_address.setVisibility(VISIBLE);
                    mCustomerAddNewPanel.showShortBillingAddress();
                    btn_billing_address.setVisibility(GONE);
                }
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
            }
            btn_shipping_address.setVisibility(GONE);
            ll_short_shipping_address.setVisibility(VISIBLE);
            ll_add_new_customer.setVisibility(VISIBLE);
            ll_new_shipping_address.setVisibility(GONE);
            ll_new_billing_address.setVisibility(GONE);
            if (typeCustomer == CREATE_NEW_CUSTOMER) {
                mCustomerAddNewPanel.showNewShortShippingAddress();
            } else {
                mCustomerAddNewPanel.showShortShippingAddress();
            }
            dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
        } else if (ll_new_billing_address.getVisibility() == VISIBLE) {
            mCustomerAddNewPanel.insertBillingAddress();
            if (mCustomer != null) {
                if (typeCustomer == CREATE_NEW_CUSTOMER) {
                    if (!mCustomerAddNewPanel.checkRequiedBillingAddress()) {
                        return;
                    }
                    dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
                } else if (typeCustomer == CREATE_NEW_ADDRESS) {
                    if (!mCustomerAddNewPanel.checkRequiedBillingAddress()) {
                        return;
                    }
                    int type_new_address = 0;
                    if (other_type == CHECKOUT_ADD_NEW_ADDRESS) {
                        type_new_address = 1;
                    }
                    ((CheckoutListController) getController()).doInputNewAddress(mCustomer, mCustomerAddNewPanel.getBillingAddress(), type_new_address);
                    if (other_type == CHECKOUT_ADD_NEW_ADDRESS) {
                        dialog.dismiss();
                        return;
                    }
                } else {
                    ((CheckoutListController) getController()).doInputEditAddress(1, mCustomer, mCustomerAddNewPanel.getChangebillingAddress(), mCustomerAddNewPanel.getBillingAddress());
                }
            } else {
                if (!mCustomerAddNewPanel.checkRequiedBillingAddress()) {
                    return;
                }
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
            }
            btn_billing_address.setVisibility(GONE);
            ll_add_new_customer.setVisibility(VISIBLE);
            ll_new_shipping_address.setVisibility(GONE);
            ll_new_billing_address.setVisibility(GONE);
            ll_short_billing_address.setVisibility(VISIBLE);
            if (typeCustomer != CREATE_NEW_ADDRESS) {
                if (typeCustomer == CREATE_NEW_CUSTOMER) {
                    mCustomerAddNewPanel.showNewShortBillingAddress();
                } else {
                    mCustomerAddNewPanel.showShortBillingAddress();
                }
            }
            dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
        } else {
            Customer customer = mCustomerAddNewPanel.returnCustomer();
            if (mCustomer != null) {
                if (typeCustomer == CREATE_NEW_CUSTOMER) {
                    if (!mCustomerAddNewPanel.checkRequiedCustomer()) {
                        return;
                    }
                    mCheckoutAddCustomerPanel.getCustomerListController().doInsert(customer);
                } else {
                    CustomerAddress shippingAddress = mCustomerAddNewPanel.getChangeshippingAddress();
                    CustomerAddress billingAddress = mCustomerAddNewPanel.getChangebillingAddress();
                    mCustomer.getAddress().remove(shippingAddress);
                    mCustomer.getAddress().add(0, shippingAddress);
                    if (shippingAddress.getID().equals(billingAddress.getID())) {
                        mCustomer.setUseOneAddress(true);
                    } else {
                        mCustomer.setUseOneAddress(false);
                        mCustomer.getAddress().remove(billingAddress);
                        mCustomer.getAddress().add(1, billingAddress);
                    }
                    if (checkChangeCustomer(customer)) {
                        mCheckoutAddCustomerPanel.getCustomerListController().doUpdate(mCustomer, customer);
                    }
                }
                dialog.dismiss();
            } else {
                if (!mCustomerAddNewPanel.checkRequiedCustomer()) {
                    return;
                }
                mCheckoutAddCustomerPanel.getCustomerListController().doInsert(customer);
                dialog.dismiss();
            }
        }
    }

    private boolean checkChangeCustomer(Customer customer) {
        if (!mCustomer.getFirstName().equals(customer.getFirstName())) {
            return true;
        }
        if (!mCustomer.getLastName().equals(customer.getLastName())) {
            return true;
        }
        if (!mCustomer.getEmail().equals(customer.getEmail())) {
            return true;
        }
        if (!mCustomer.getGroupID().equals(customer.getGroupID())) {
            return true;
        }
        return false;
    }

    private void onClickDialogCancel() {
        if (ll_new_shipping_address.getVisibility() == VISIBLE || ll_new_billing_address.getVisibility() == VISIBLE) {
            if (dialog.getButtonCancel().getText().toString().equals(getContext().getString(R.string.delete)) && ll_new_shipping_address.getVisibility() == VISIBLE) {
                mCustomerAddNewPanel.deleteShippingAddress();
                btn_shipping_address.setVisibility(VISIBLE);
                ll_short_shipping_address.setVisibility(GONE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
            } else if (ll_new_shipping_address.getVisibility() == VISIBLE) {
                if (typeCustomer == CREATE_NEW_CUSTOMER) {
                    mCustomerAddNewPanel.deleteShippingAddress();
                    btn_shipping_address.setVisibility(VISIBLE);
                    ll_short_shipping_address.setVisibility(GONE);
                    dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
                } else {
                    btn_shipping_address.setVisibility(GONE);
                    ll_short_shipping_address.setVisibility(VISIBLE);
                    dialog.getDialogTitle().setText(mCustomer.getName());
                }
            }
            if (dialog.getButtonCancel().getText().toString().equals(getContext().getString(R.string.delete)) && ll_new_billing_address.getVisibility() == VISIBLE) {
                mCustomerAddNewPanel.deleteBillingAddress();
                btn_billing_address.setVisibility(VISIBLE);
                ll_short_billing_address.setVisibility(GONE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
            } else if (ll_new_billing_address.getVisibility() == VISIBLE) {
                if (other_type == CHECKOUT_ADD_NEW_ADDRESS) {
                    dialog.dismiss();
                    return;
                }
                if (typeCustomer == CREATE_NEW_CUSTOMER) {
                    mCustomerAddNewPanel.deleteBillingAddress();
                    btn_billing_address.setVisibility(VISIBLE);
                    ll_short_billing_address.setVisibility(GONE);
                    dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
                } else {
                    btn_billing_address.setVisibility(GONE);
                    ll_short_billing_address.setVisibility(VISIBLE);
                    dialog.getDialogTitle().setText(mCustomer.getName());
                }
            }
            ll_add_new_customer.setVisibility(VISIBLE);
            ll_new_shipping_address.setVisibility(GONE);
            ll_new_billing_address.setVisibility(GONE);
            dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
        } else {
            if (fr_sales_new_customer.getVisibility() == VISIBLE) {
                if (mCustomer != null) {
                    dialog.dismiss();
                } else {
                    dialog.getButtonSave().setVisibility(GONE);
                    dialog.getDialogTitle().setText("");
                    fr_sales_new_customer.setVisibility(GONE);
                    ll_sales_add_customer.setVisibility(VISIBLE);
                }
            } else {
                dialog.dismiss();
            }
        }
    }

    public void updateCustomerToOrder(Customer customer) {
        mCustomer = customer;
        ((TextView) toolbar_order.findViewById(R.id.text_customer_name)).setText(customer.getName());
        ((CheckoutListController) getController()).bindCustomer(customer);
        if (dialog != null)
            dialog.dismiss();
    }

    public void changeCustomerInToolBar(Customer customer) {
        mCustomer = customer;
        ((TextView) toolbar_order.findViewById(R.id.text_customer_name)).setText(customer.getName());
    }

    public void updateAddress(int typeAction, int typeAddress, CustomerAddress customerAddress) {
        if (typeAction == 0) {
            mCustomerAddNewPanel.updateAddress(typeAddress, mCustomer.getAddress(), customerAddress);
        } else {
            mCustomerAddNewPanel.updateAddress(typeAddress, mCustomer.getAddress(), mCustomer.getAddress().get(0));
        }
    }

    public void updateCheckoutAddress() {
        updateCustomerToOrder(mCustomer);
    }

    public void checkoutAddNewAddress() {
        showPopUpAddCustomer(NO_TYPE, CHECKOUT_ADD_NEW_ADDRESS);
        dialog.getButtonSave().setVisibility(VISIBLE);
        dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new_address));
        typeCustomer = CREATE_NEW_ADDRESS;
        ll_add_new_customer.setVisibility(GONE);
        mCustomerAddNewPanel.deleteBillingAddress();
        ll_new_billing_address.setVisibility(VISIBLE);
        ll_new_shipping_address.setVisibility(GONE);
        dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
    }

    public void showLoading(boolean isShow) {
        cart_background_loading.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void showToastMessage(int type) {
        String message = "";
        if (type == 0) {
            message = getContext().getString(R.string.customer_create_success);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        } else if (type == 1) {
            message = getContext().getString(R.string.customer_create_success);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
