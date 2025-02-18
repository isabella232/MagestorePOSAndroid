package com.magestore.app.pos.panel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;

import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.model.setting.Setting;
import com.magestore.app.lib.model.staff.Staff;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.view.SimpleSpinner;
import com.magestore.app.pos.LoginActivity;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.SettingListController;
import com.magestore.app.pos.util.EditTextUtil;
import com.magestore.app.pos.util.PrinterSetting;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.DataUtil;
import com.magestore.app.util.DialogUtil;
import com.magestore.app.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2/27/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class SettingDetailPanel extends AbstractDetailPanel<Setting> {
    LinearLayout ll_setting_account, ll_setting_currency, ll_setting_store, ll_setting_print;
    static int TYPE_ACCOUNT = 0;
    static int TYPE_PRINT = 1;
    static int TYPE_CURRENCY = 2;
    static int TYPE_STORE = 3;
    List<LinearLayout> listLayout;
    SimpleSpinner sp_currency, sp_print;
    EditText edt_name, edt_current_password, edt_new_password, edt_confirm_password;
    Button btn_save;
    RelativeLayout setting_background_loading;
    boolean checkFirst;
    LinearLayout ll_print_area, ll_print_copy;
    Spinner sp_print_area, sp_print_copy;
    Switch sw_open_cash, sw_auto_print;

    public SettingDetailPanel(Context context) {
        super(context);
    }

    public SettingDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SettingDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        listLayout = new ArrayList<>();
        ll_setting_account = (LinearLayout) findViewById(R.id.ll_setting_account);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_current_password = (EditText) findViewById(R.id.edt_current_password);
        edt_new_password = (EditText) findViewById(R.id.edt_new_password);
        edt_confirm_password = (EditText) findViewById(R.id.edt_confirm_password);
        ll_setting_currency = (LinearLayout) findViewById(R.id.ll_setting_currency);
        sp_currency = (SimpleSpinner) findViewById(R.id.sp_currency);
        ll_setting_print = (LinearLayout) findViewById(R.id.ll_setting_print);
        sp_print = (SimpleSpinner) findViewById(R.id.sp_print);
        ll_print_area = (LinearLayout) findViewById(R.id.ll_print_area);
        sw_open_cash = (Switch) findViewById(R.id.sw_open_cash);
        sw_auto_print = (Switch) findViewById(R.id.sw_auto_print);
        sp_print_area = (Spinner) findViewById(R.id.sp_print_area);
        ll_print_copy = (LinearLayout) findViewById(R.id.ll_print_copy);
        sp_print_copy = (Spinner) findViewById(R.id.sp_print_copy);
        ll_setting_store = (LinearLayout) findViewById(R.id.ll_setting_store);
        btn_save = (Button) findViewById(R.id.btn_save);
        listLayout.add(ll_setting_account);
        listLayout.add(ll_setting_print);
        listLayout.add(ll_setting_currency);
        listLayout.add(ll_setting_store);

        setting_background_loading = (RelativeLayout) findViewById(R.id.setting_background_loading);

        initValue();
    }

    @Override
    public void initValue() {
        btn_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkRequied()) {
                    return;
                }
                Staff staff = ((SettingListController) getController()).createStaff();
                staff.setStaffName(edt_name.getText().toString().trim());
                staff.setCurrentPassword(edt_current_password.getText().toString().trim());
                staff.setNewPassword(edt_new_password.getText().toString().trim());
                staff.setConfirmPassword(edt_confirm_password.getText().toString().trim());
                ((SettingListController) getController()).doInputChangeInformation(staff);
            }
        });

        sp_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (checkFirst) {
                    String code = sp_currency.getSelection();
                    ((SettingListController) getController()).doInputChangeCurrency(code);
                } else {
                    checkFirst = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_print.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ConfigUtil.setTypePrint(sp_print.getSelection());
                if (sp_print.getSelection().equals(getContext().getString(R.string.print_type_star_print))) {
                    ll_print_area.setVisibility(VISIBLE);
                    sw_open_cash.setVisibility(VISIBLE);
                    sw_auto_print.setVisibility(VISIBLE);
                    ll_print_copy.setVisibility(VISIBLE);
                } else {
                    ll_print_area.setVisibility(GONE);
                    sw_open_cash.setVisibility(GONE);
                    sw_auto_print.setVisibility(GONE);
                    ll_print_copy.setVisibility(GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SpinnerAdapter ad_print_area = new ArrayAdapter<String>(getContext(), R.layout.simple_textview_row1, new String[]{getContext().getResources().getString(R.string.printArea2inch), getContext().getResources().getString(R.string.printArea3inch), getContext().getResources().getString(R.string.printArea4inch)});
        final PrinterSetting printerSetting = new PrinterSetting(getContext());
        sp_print_area.setAdapter(ad_print_area);
        sp_print_area.setSelection(printerSetting.getPrintArea());
        sp_print_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                printerSetting.writePrintArea(i);
                if (i == 0) {
                    ConfigUtil.setStarPrintArea(384);
                } else if (i == 1) {
                    ConfigUtil.setStarPrintArea(576);
                } else {
                    ConfigUtil.setStarPrintArea(832);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SpinnerAdapter ad_print_copy = new ArrayAdapter<String>(getContext(), R.layout.simple_textview_row1, new String[]{"1", "2", "3"});
        sp_print_copy.setAdapter(ad_print_copy);
        final SharedPreferences pref = getContext().getSharedPreferences("pref", getContext().MODE_PRIVATE);
        String print_copy = pref.getString("copy", "1");
        if (print_copy.equals("1")) {
            sp_print_copy.setSelection(0);
        } else if (print_copy.equals("2")) {
            sp_print_copy.setSelection(1);
        } else {
            sp_print_copy.setSelection(2);
        }
        sp_print_copy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String copy;
                if (i == 0) {
                    copy = "1";
                } else if (i == 1) {
                    copy = "2";
                } else {
                    copy = "3";
                }
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("copy", copy);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sw_open_cash.setChecked(printerSetting.getOpenCashAfterPrint());
        sw_open_cash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ConfigUtil.setOpenCash(b);
                printerSetting.writeOpenCashAfterPrint(b);
            }
        });

        sw_auto_print.setChecked(printerSetting.getAutoPrint());
        sw_auto_print.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ConfigUtil.setAutoPrint(b);
                printerSetting.writeAutoPrint(b);
            }
        });
    }

    @Override
    public void bindItem(Setting item) {
        super.bindItem(item);
        if (item.getType() == TYPE_ACCOUNT) {
            selectLinearLayout(ll_setting_account, listLayout);
        } else if (item.getType() == TYPE_CURRENCY) {
            selectLinearLayout(ll_setting_currency, listLayout);
        } else if (item.getType() == TYPE_STORE) {
            ((SettingListController) getController()).backToLoginActivity();
        } else if (item.getType() == TYPE_PRINT) {
            selectLinearLayout(ll_setting_print, listLayout);
        }
    }

    public void setCurrencyDataSet(List<Currency> currencyList) {
        sp_currency.bind(currencyList.toArray(new Currency[0]));
        sp_currency.setSelection(ConfigUtil.getCurrentCurrency().getCode());
    }

    public void setPrintDataSet() {
        String[] list = {getContext().getString(R.string.print_type_star_print), getContext().getString(R.string.print_type_receipt), getContext().getString(R.string.print_type_a4)};
        sp_print.bind(list);
        ConfigUtil.setTypePrint(sp_print.getSelection());
    }

    public void setStaffDataSet(Staff staff) {
        if (staff != null) {
            edt_name.setText(staff.getStaffName());
        }
    }

    private void selectLinearLayout(LinearLayout linearLayout, List<LinearLayout> listLayout) {
        for (LinearLayout layout : listLayout) {
            if (layout == linearLayout) {
                layout.setVisibility(VISIBLE);
            } else {
                layout.setVisibility(GONE);
            }
        }
    }

    public boolean checkRequied() {
        if (!isRequied(edt_name)) {
            return false;
        }

        if (!isRequied(edt_current_password)) {
            return false;
        }

        return true;
    }

    public boolean isRequied(EditText editText) {
        return EditTextUtil.checkRequied(getContext(), editText);
    }

    public void isShowLoading(boolean isShow) {
        setting_background_loading.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void showAlertRespone(String message) {
        if (StringUtil.STRING_EMPTY.equals(message)) {
            message = getContext().getString(R.string.err_request);
        }
        // Tạo dialog và hiển thị
        DialogUtil.confirm(getContext(), message, R.string.ok);
    }
}
