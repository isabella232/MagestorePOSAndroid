package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 1/24/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderCommentParams extends PosAbstractModel implements OrderCommentParams {
    String comment;
    String createdAt;
    String isVisibleOnFront;

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String strComment) {
        comment = strComment;
    }

    @Override
    public String getIsVisibleOnFront() {
        return isVisibleOnFront;
    }

    @Override
    public void setIsVisibleOnFront(String strIsVisibleOnFront) {
        isVisibleOnFront = strIsVisibleOnFront;
    }
}
