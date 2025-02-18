package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 1/14/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ConfigRegion extends Model {
    String getCode();
    void setCode(String strCode);
    String getName();
    void setName(String strName);
    void setID(String id);
}
