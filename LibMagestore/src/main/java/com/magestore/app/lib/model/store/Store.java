package com.magestore.app.lib.model.store;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 1/6/2017.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface Store extends Model {
    String getCode();
    String getWebsiteId();
    String getGroupId();
    String getName();
    String getSortOrder();
    String getIsActive();
}
