package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 1/24/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderItemParams extends Model {
    void setOrderItemId(String strOrderItemId);

    String getOrderItemId();

    float getQty();
    void setQty(float strQty);
    void setQty(String strQty);

    String getAdditionalData();

    void setAdditionalData(String strAdditionalData);
}
