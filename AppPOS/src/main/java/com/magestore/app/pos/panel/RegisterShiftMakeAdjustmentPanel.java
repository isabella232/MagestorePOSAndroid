package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.RegisterShiftListController;
import com.magestore.app.pos.databinding.PanelRegisterShiftMakeAdjustmentBinding;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.view.EditTextFloat;

/**
 * Created by Johan on 1/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class RegisterShiftMakeAdjustmentPanel extends AbstractDetailPanel<RegisterShift> {
    PanelRegisterShiftMakeAdjustmentBinding mBinding;
    RegisterShift registerShift;
    TextView tv_add, staff_name;
    TextView tv_remove;
    EditText edt_note;
    EditTextFloat edt_amount;
    public static String ADD_MAKE_ADJUSTMENT = "add";
    private static String REMOVE_MAKE_ADJUSTMENT = "remove";
    private String selectMakeAdjustment = ADD_MAKE_ADJUSTMENT;

    public RegisterShiftMakeAdjustmentPanel(Context context) {
        super(context);
    }

    public RegisterShiftMakeAdjustmentPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegisterShiftMakeAdjustmentPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setType(boolean isType) {
        if (isType) {
            selectMakeAdjustment = ADD_MAKE_ADJUSTMENT;
        } else {
            selectMakeAdjustment = REMOVE_MAKE_ADJUSTMENT;
        }
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_register_shift_make_adjustment, null);
        addView(view);

        tv_add = (TextView) view.findViewById(R.id.add);
        tv_remove = (TextView) view.findViewById(R.id.remove);
        edt_note = (EditText) view.findViewById(R.id.note);
        edt_amount = (EditTextFloat) view.findViewById(R.id.amount);
        edt_amount.setPriceFormat(true);
        staff_name = (TextView) view.findViewById(R.id.staff_name);
        mBinding = DataBindingUtil.bind(view);
    }

    @Override
    public void bindItem(RegisterShift item) {
        // Bind từ object sang view
        if (item == null) return;
        super.bindItem(item);
        staff_name.setText((getContext().getString(R.string.open_session_login, ConfigUtil.getStaff().getStaffName())));
        if (mBinding == null) mBinding = DataBindingUtil.bind(getView());
        mBinding.setRegisterShift(item);
        registerShift = item;
        actionChangeAmount(item, edt_amount);
        tv_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAddMakeAdjusment();
            }
        });

        tv_remove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectRemoveMakeAdjustment();
            }
        });
    }

    private void actionChangeAmount(final RegisterShift item, final EditTextFloat edt_amount) {
        edt_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (selectMakeAdjustment == REMOVE_MAKE_ADJUSTMENT) {
                    if (edt_amount.getValueFloat() > ConfigUtil.convertToPrice(item.getBaseBalance())) {
                        edt_amount.setError(String.format(getResources().getString(R.string.register_shift_make_adjusment_error_remove)));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public RegisterShift bind2Item() {
        float value = edt_amount.getValueFloat();
        String note = "";
        if (!TextUtils.isEmpty(edt_note.getText().toString().trim())) {
            note = edt_note.getText().toString().trim();
        }
        CashTransaction cashTransaction = ((RegisterShiftListController) mController).createCashTransaction();
        cashTransaction.setType(selectMakeAdjustment);
        cashTransaction.setValue(value);
        cashTransaction.setNote(note);
        cashTransaction.setCreateAt(ConfigUtil.getCurrentDateTime());
        cashTransaction.setBaseCurrencyCode(ConfigUtil.getBaseCurrencyCode());
        cashTransaction.setTransactionCurrencyCode(ConfigUtil.getCurrentCurrency().getCode());
        cashTransaction.setParamShiftId(registerShift.getID());
        registerShift.setParamCash(cashTransaction);
        return registerShift;
    }

    private void selectAddMakeAdjusment() {
        selectMakeAdjustment = ADD_MAKE_ADJUSTMENT;
        tv_add.setBackgroundResource(R.drawable.make_adjusment_push_select_bg);
        tv_add.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        tv_remove.setBackgroundResource(R.drawable.make_adjusment_take_not_select_bg);
        tv_remove.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
    }

    private void selectRemoveMakeAdjustment() {
        selectMakeAdjustment = REMOVE_MAKE_ADJUSTMENT;
        if (edt_amount.getValueFloat() > ConfigUtil.convertToPrice(registerShift.getBaseBalance())) {
            edt_amount.setError(String.format(getResources().getString(R.string.register_shift_make_adjusment_error_remove)));
        }
        tv_add.setBackgroundResource(R.drawable.make_adjusment_push_not_select_bg);
        tv_add.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
        tv_remove.setBackgroundResource(R.drawable.make_adjusment_take_select_bg);
        tv_remove.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    public String getSelectMakeAdjustment() {
        return selectMakeAdjustment;
    }

    public void showErrorAmountLess() {
        edt_amount.requestFocus();
        edt_amount.setError(String.format(getResources().getString(R.string.register_shift_make_adjusment_error_remove)));
    }

    public void showErrorAmountGreat() {
        edt_amount.requestFocus();
        edt_amount.setError(String.format(getResources().getString(R.string.err_field_must_greater_than_zero)));
    }
}
