package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractChildListController;
import com.magestore.app.lib.controller.ChildListController;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.directory.Region;
import com.magestore.app.lib.observ.State;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.lib.service.customer.CustomerAddressService;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.pos.panel.CustomerAddressListPanel;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * Task cho danh sách địa chỉ khách hàng
 * Magestore
 * mike@trueplus.vn
 */

public class CustomerAddressListController
        extends AbstractChildListController<Customer, CustomerAddress>
        implements ChildListController<Customer, CustomerAddress> {

    ConfigService mConfigService;

    /**
     * Thiết lập service
     *
     * @param service
     */
    public void setCustomerService(CustomerService service) {
        try {
            setChildListService(ServiceFactory.getFactory(getMagestoreContext()).generateCustomerAddressService());
            mConfigService = ServiceFactory.getFactory(getMagestoreContext()).generateConfigService();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gán customer cho address list
     *
     * @param customer
     */
    public void bindCustomer(Customer customer) {
        bindParent(customer);
    }

    /**
     * Gán customer cho address list thông qua cơ chế state observe
     *
     * @param state
     */
    public void bindCustomer(State state) {
        bindCustomer(((CustomerListController) state.getController()).getSelectedItem());
    }

    /**
     * Khởi tạo 1 customer address
     *
     * @return
     */
    public CustomerAddress createNewCustomerAddress() {
        try {
            return getChildListService().create(getParent());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, ConfigCountry> getCountry() {
        try {
            return mConfigService.getCountry();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Region createRegion() {
        return ((CustomerAddressService) getChildListService()).createRegion();
    }

    @Override
    public void onInsertPostExecute(Boolean success, CustomerAddress... models) {
        super.onInsertPostExecute(success, models);
        ((CustomerAddressListPanel) mView).showToastNotify();
    }

    @Override
    public void onUpdatePostExecute(Boolean success, CustomerAddress oldModel, CustomerAddress newModels) {
        super.onUpdatePostExecute(success, oldModel, newModels);
        ((CustomerAddressListPanel) mView).showToastNotify();
    }
}
