package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.model.plugins.GiftCardRespone;
import com.magestore.app.lib.model.plugins.RewardPoint;
import com.magestore.app.lib.model.plugins.StoreCredit;
import com.magestore.app.lib.model.sales.Order;

import java.util.List;

/**
 * Created by Mike on 1/6/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface Checkout extends Model {
    String getCustomerID();
    void setCustomerID(String strCustomerID);

    List<CartItem> getCartItem();
    void setCartItem(List<CartItem> items);

    List<CheckoutShipping> getCheckoutShipping();
    void setCheckoutShipping(List<CheckoutShipping> shiping);

    List<CheckoutPayment> getCheckoutPayment();
    void setCheckoutPayment(List<CheckoutPayment> payment);

    String getCouponCode();
    void setCouponCode(String strCouponCode);

    String getSubTitle();
    void setSubTitle(String strSubTitle);

    float getSubTotal();
    void setSubTotal(float total);
    float getSubTotalView();
    void setSubTotalView(float total);

    String getShippingTitle();
    void setShippingTitle(String strShippingTitle);

    float getShippingTotal();
    void setShippingTotal(float shipping);

    String getTaxTitle();
    void setTaxTitle(String strTaxTitle);

    float getTaxTotal();
    void setTaxTotal(float total);

    String getDiscountTitle();
    void setDiscountTitle(String strDiscountTitle);

    float getDiscountTotal();
    void setDiscountTotal(float total);

    String getGrandTitle();
    void setGrandTitle(String strGrandTitle);

    float getGrandTotalView();
    void setGrandTotalView(float total);

    float getGrandTotal();
    void setGrandTotal(float total);

    Customer getCustomer();
    void setCustomer(Customer customer);

    List<CheckoutTotals> getTotals();
    void setTotals(List<CheckoutTotals> checkoutTotals);

    Quote getQuote();
    void setQuote(Quote quote);

    String getCreateShip();
    void setCreateShip(String strCreateShip);

    String getCreateInvoice();
    void setCreateInvoice(String strCreateInvoice);

    String getNote();
    void setNote(String strNote);

    float getRealAmount();
    void setRealAmount(float fRealAmount);

    float getRemainMoney();
    void setRemainMoney(float fRemainMoney);

    float getExchangeMoney();
    void setExchangeMoney(float fExchangeMoney);

    String getCreateAt();
    void setCreateAt(String strCreateAt);

    int getStatus();
    void setStatus(int intStatus);

    Order getOrderSuccess();
    void setOrderSuccess(Order orderSuccess);

    String getQuoteId();
    void setQuoteId(String quoteId);

    String getStoreId();
    void setStoreId(String strStoreId);

    String getDeliveryDate();
    void setDeliveryDate(String strDeliveryDate);

    String getGiftCardTitle();
    void setGiftCardTitle(String strGiftCardTitle);

    float getGiftCardDiscount();
    void setGiftCardDiscount(float fGiftCardDiscount);

    String getRewardPointUsePointTitle();
    void setRewardPointUsePointTitle(String strRewardPointUsePointTitle);

    float getRewardPointUsePointValue();
    void setRewardPointUsePointValue(float fRewardPointUsePointValue);

    int getRewardPointEarnPointValue();
    void setRewardPointEarnPointValue(int fRewardPointEarnPointValue);

    GiftCardRespone getGiftCard();

    RewardPoint getRewardPoint();
    void setRewardPoint(RewardPoint rewardPoint);

    StoreCredit getStoreCredit();

    boolean isPickAtStore();
    void setIsPickAtStore(boolean bIsPickAtStore);

    List<GiftCard> getListGiftCardUse();
    void setListGiftCardUse(List<GiftCard> listListGiftCardUse);
}
