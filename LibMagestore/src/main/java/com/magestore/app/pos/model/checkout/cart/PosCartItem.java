package com.magestore.app.pos.model.checkout.cart;

import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.model.catalog.ProductOptionCustom;
import com.magestore.app.lib.model.catalog.ProductOptionCustomValue;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.catalog.PosProductOption;
import com.magestore.app.pos.model.catalog.PosProductOptionCustom;
import com.magestore.app.pos.model.checkout.PosQuoteItems;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;
import com.magestore.app.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 12/26/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosCartItem extends PosAbstractModel implements CartItem {
    String discount;
    String discout_type;
    float custom_price;
    String custom_price_type;

    public float getCustomPrice() {
        return custom_price;
    }

    public void setCustomPrice(float custom_price) {
        this.custom_price = custom_price;
    }

    public String getCustomPriceType() {
        return custom_price_type;
    }

    public void setCustomPriceType(String custom_price_type) {
        this.custom_price_type = custom_price_type;
    }

    public void setCustomPriceTypePercent() {
        setCustomPriceType(StringUtil.TYPE_PERCENT);
    }

    public void setCustomPriceTypeFixed() {
        setCustomPriceType(StringUtil.TYPE_FIXED);
    }

    public boolean isCustomPriceTypePercent() {
        return StringUtil.TYPE_PERCENT.equals(custom_price_type);
    }

    public boolean isDCustomPriceTypeFixed() {
        return !isCustomPriceTypePercent();
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscoutType() {
        return discout_type;
    }

    public void setDiscoutType(String discout_type) {
        this.discout_type = discout_type;
    }

    public void setDiscoutTypePercent() {
        setDiscoutType(StringUtil.TYPE_PERCENT);
    }

    public void setDiscoutTypeFixed() {
        setDiscoutType(StringUtil.TYPE_FIXED);
    }

    public boolean isDiscountTypePercent() {
        return StringUtil.TYPE_PERCENT.equals(discout_type);
    }

    public boolean isDiscountTypeFixed() {
        return !isDiscountTypePercent();
    }

    //    public class ChooseProductOption {
//        public List<ProductOptionCustomValue> productOptionCustomValueList;
//        public int qty;
//    }
//    Map<ProductOptionCustom, ChooseProductOption> choose_product_options;

    public class OptionsValue {
        public String code;
        public String value;
    }

    List<OptionsValue> options;
    List<OptionsValue> super_attribute;
    List<OptionsValue> bundle_option;
    List<OptionsValue> bundle_option_qty;

    @Override
    public List<OptionsValue> getOptions() {
        return options;
    }

    @Override
    public List<OptionsValue> getSuperAttribute() {
        return super_attribute;
    }

    @Override
    public List<OptionsValue> getBundleOption() {
        return bundle_option;
    }

    @Override
    public List<OptionsValue> getBundleOptionQuantity() {
        return bundle_option_qty;
    }

    @Override
    public void clearOption() {
        options = null;
        super_attribute = null;
        bundle_option = null;
        bundle_option_qty = null;
    }

    @Override
    public void clearSuperAtribute() {
        super_attribute = null;
    }

    @Override
    public void clearBundleOption() {
        bundle_option = null;
    }

    @Override
    public void clearBundleOptionQuantity() {
        bundle_option_qty = null;
    }

    @Override
    public void insertOption(String code, String value) {
        if (options == null) options = new ArrayList<>();
        OptionsValue quoteCustomeOption = new OptionsValue();
        quoteCustomeOption.code = code;
        quoteCustomeOption.value = value;
        options.add(quoteCustomeOption);
    }

    @Override
    public void insertSuperAttribute(String code, String value) {
        if (super_attribute == null) super_attribute = new ArrayList<>();
        OptionsValue quoteCustomeOption = new OptionsValue();
        quoteCustomeOption.code = code;
        quoteCustomeOption.value = value;
        super_attribute.add(quoteCustomeOption);
    }

    @Override
    public void insertBundleOption(String code, String value) {
        if (bundle_option == null) bundle_option = new ArrayList<>();
        OptionsValue quoteCustomeOption = new OptionsValue();
        quoteCustomeOption.code = code;
        quoteCustomeOption.value = value;
        bundle_option.add(quoteCustomeOption);
    }

    @Override
    public void insertBundleOptionQuantity(String code, String value) {
        if (bundle_option_qty == null) bundle_option_qty = new ArrayList<>();
        OptionsValue quoteCustomeOption = new OptionsValue();
        quoteCustomeOption.code = code;
        quoteCustomeOption.value = value;
        bundle_option_qty.add(quoteCustomeOption);
    }

    Product product;
    int qty;
    @Gson2PosExclude
    String child_id;
    //    @Gson2PosExclude
//    String bundle_option;
    float price;
    float base_price;
    @Gson2PosExclude
    float unit_price;

    // Order history
    String name;
    String sku;
    String item_description;

    float row_total = 0;
    float row_total_incl_tax = 0;
    float tax_amount = 0;
    float discount_amount = 0;
    @Gson2PosExclude
    int qty_ordered;
    @Gson2PosExclude
    int qty_canceled;
    @Gson2PosExclude
    int qty_invoiced;
    @Gson2PosExclude
    int qty_refunded;
    @Gson2PosExclude
    int qty_shipped;
    @Gson2PosExclude
    String product_type;
    @Gson2PosExclude
    String parent_item_id;

    @Gson2PosExclude
    float base_original_price;
    @Gson2PosExclude
    float base_unit_price;
    //    @Gson2PosExclude
//    int bundle_option_qty;
    @Gson2PosExclude
    String is_virtual;
    @Gson2PosExclude
    String item_id;
    @Gson2PosExclude
    float original_price;

    // Params Order Shipment
    String orderItemId;

    // Params Order Refund
    String return_to_stock;

    @Override
    public void setQuantity(int param_quantity) {
        qty = param_quantity;
        price = unit_price * param_quantity;
    }

    @Override
    public void setProduct(Product param_product) {
        product = param_product;
    }

    @Override
    public void setPrice(float param_price) {
        price = param_price;
    }

    @Override
    public void setOriginalPrice(float param_price) {
        original_price = param_price;
    }

    @Override
    public void setUnitPrice(float param_price) {
        this.unit_price = param_price;
        price = unit_price * qty;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public int getQuantity() {
        return qty;
    }

    @Override
    public float getUnitPrice() {
        return unit_price;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public float getOriginalPrice() {
        return original_price;
    }

    @Override
    public String getItemId() {
        return item_id;
    }

    @Override
    public void setItemId(String strItemId) {
        item_id = strItemId;
    }

    // Order history
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSku() {
        return (sku != null ? sku : getProduct().getSKU());
    }

    @Override
    public float getSubtotal() {
        return row_total;
    }

    @Override
    public float getRowTotal() {
        return row_total_incl_tax;
    }

    @Override
    public float getTaxAmount() {
        return tax_amount;
    }

    @Override
    public float getDiscountAmount() {
        return discount_amount;
    }

    @Override
    public void setDiscountAmount(float discount_amount) {
        this.discount_amount = discount_amount;
    }

    @Override
    public int getQtyOrdered() {
        return qty_ordered;
    }

    @Override
    public int getQtyCanceled() {
        return qty_canceled;
    }

    @Override
    public int getQtyInvoiced() {
        return qty_invoiced;
    }

    @Override
    public int getQtyRefunded() {
        return qty_refunded;
    }

    @Override
    public int getQtyShipped() {
        return qty_shipped;
    }

    @Override
    public int QtyShip() {
        return qty_ordered - qty_shipped;
    }

    @Override
    public int QtyRefund() {
        return qty_ordered - qty_refunded;
    }

    @Override
    public int QtyInvoice() {
        return qty_ordered - qty_invoiced;
    }

    @Override
    public String getProductType() {
        return product_type;
    }

    @Override
    public String getIsVirtual() {
        return is_virtual;
    }

    @Override
    public String getParentItemId() {
        return parent_item_id;
    }

    @Override
    public void setId(String strID) {
        id = strID;
    }

    @Override
    public void setOrderItemId(String strOrderItemId) {
        orderItemId = strOrderItemId;
    }

    @Override
    public String getOrderItemId() {
        return orderItemId;
    }

    @Override
    public void setReturnToStock(String strReturnToStock) {
        return_to_stock = strReturnToStock;
    }

    @Override
    public String getReturnToStock() {
        return return_to_stock;
    }
//
//    @Override
//    public Map<ProductOptionCustom, ChooseProductOption> getChooseProductOptions() {
//        return choose_product_options;
//    }
//
//    @Override
//    public void setChooseProductOptions(Map<ProductOptionCustom, ChooseProductOption> choose_product_options) {
//        this.choose_product_options = choose_product_options;
//    }
//
//    /**
//     * Khởi tạo ChooseProductOption
//     * @return
//     */
//    @Override
//    public ChooseProductOption createChooseProductOption() {
//        ChooseProductOption chooseProductOption = new ChooseProductOption();
//        chooseProductOption.productOptionCustomValueList = new ArrayList<ProductOptionCustomValue>();
//        chooseProductOption.qty = 1;
//        return chooseProductOption;
//    }

    @Override
    public void setItemDescription(String description) {
        this.item_description = description;
    }

    @Override
    public String getItemDescription() {
        return (item_description != null ? item_description : getSku());
    }
}
