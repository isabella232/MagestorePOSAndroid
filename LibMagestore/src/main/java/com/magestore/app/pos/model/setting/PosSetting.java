package com.magestore.app.pos.model.setting;

import com.magestore.app.lib.model.setting.Setting;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 2/27/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosSetting extends PosAbstractModel implements Setting {
    String name;
    int type;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String strName) {
        name = strName;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int intType) {
        type = intType;
    }
}
