package com.magestore.app.pos.api.odoo.customer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.customer.DataCustomer;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.customer.CustomerDataAccess;
import com.magestore.app.pos.api.odoo.POSAPIOdoo;
import com.magestore.app.pos.api.odoo.POSAbstractDataAccessOdoo;
import com.magestore.app.pos.api.odoo.POSDataAccessSessionOdoo;
import com.magestore.app.pos.model.customer.PosCustomer;
import com.magestore.app.pos.model.customer.PosCustomerAddress;
import com.magestore.app.pos.model.customer.PosDataCustomer;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListCustomer;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 9/15/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSCustomerDataAccessOdoo extends POSAbstractDataAccessOdoo implements CustomerDataAccess {

    public class Gson2PosCustomerParseModelOdoo extends Gson2PosAbstractParseImplement {
        @Override
        protected Gson createGson() {
            GsonBuilder builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization();
            builder.registerTypeAdapter(new TypeToken<List<PosCustomer>>() {
            }
                    .getType(), new CustomerConverter());
            return builder.create();
        }

        private String CUSTOMER_ID = "id";
        private String CUSTOMER_CREATE_AT = "create_date";
        private String CUSTOMER_EMAIL = "email";
        private String CUSTOMER_NAME = "name";
        private String CUSTOMER_PHONE = "phone";
        private String CUSTOMER_STATE_ID = "state_id";
        private String CUSTOMER_STATE_NAME = "state_name";
        private String CUSTOMER_STATE_CODE = "state_code";
        private String CUSTOMER_COMPANY = "company_name";
        private String CUSTOMER_COMPANY_ID = "company_id";
        private String CUSTOMER_COMPANY_TYPE = "company_type";
        private String CUSTOMER_POSCODE = "zip";
        private String CUSTOMER_COUNTRY_ID = "country_id";
        private String CUSTOMER_STREET = "street";
        private String CUSTOMER_STREET2 = "street2";
        private String CUSTOMER_VAT = "vat";
        private String CUSTOMER_CITY = "city";

        public class CustomerConverter implements JsonDeserializer<List<PosCustomer>> {

            @Override
            public List<PosCustomer> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                List<PosCustomer> listCustomer = new ArrayList<>();
                if (json.isJsonArray()) {
                    JsonArray arr_customer = json.getAsJsonArray();
                    if (arr_customer != null && arr_customer.size() > 0) {
                        for (JsonElement el_customer : arr_customer) {
                            List<String> listStreet = new ArrayList<>();
                            JsonObject obj_customer = el_customer.getAsJsonObject();
                            PosCustomer customer = new PosCustomer();
                            String id = obj_customer.remove(CUSTOMER_ID).getAsString();
                            customer.setID(id);
                            String create_at = obj_customer.remove(CUSTOMER_CREATE_AT).getAsString();
                            customer.setCreateAt(create_at);
                            String customer_email = obj_customer.remove(CUSTOMER_EMAIL).getAsString();
                            String customer_name = obj_customer.remove(CUSTOMER_NAME).getAsString();
                            String customer_phone = obj_customer.remove(CUSTOMER_PHONE).getAsString();
                            String state_id = "";
                            if (obj_customer.has(CUSTOMER_STATE_ID)) {
                                state_id = obj_customer.remove(CUSTOMER_STATE_ID).getAsString();
                            }
                            String state_name = "";
                            if (obj_customer.has(CUSTOMER_STATE_NAME)) {
                                state_name = obj_customer.remove(CUSTOMER_STATE_NAME).getAsString();
                            }
                            String state_code = "";
                            if (obj_customer.has(CUSTOMER_STATE_CODE)) {
                                state_code = obj_customer.remove(CUSTOMER_STATE_CODE).getAsString();
                            }
                            String company_name = obj_customer.remove(CUSTOMER_COMPANY).getAsString();
                            String company_type = obj_customer.remove(CUSTOMER_COMPANY_TYPE).getAsString();
                            String company_id = obj_customer.remove(CUSTOMER_COMPANY_ID).getAsString();
                            String poscode = obj_customer.remove(CUSTOMER_POSCODE).getAsString();
                            String country_id = obj_customer.remove(CUSTOMER_COUNTRY_ID).getAsString();
                            String street = "";
                            if (obj_customer.has(CUSTOMER_STREET)) {
                                street = obj_customer.remove(CUSTOMER_STREET).getAsString();
                            }
                            String street2 = "";
                            if (obj_customer.has(CUSTOMER_STREET2)) {
                                street2 = obj_customer.remove(CUSTOMER_STREET2).getAsString();
                            }
                            String vat = "";
                            if (obj_customer.has(CUSTOMER_VAT)) {
                                vat = obj_customer.remove(CUSTOMER_VAT).getAsString();
                            }
                            String city = "";
                            if (obj_customer.has(CUSTOMER_CITY)) {
                                city = obj_customer.remove(CUSTOMER_CITY).getAsString();
                            }
                            if (StringUtil.checkJsonData(street)) {
                                listStreet.add(street);
                            }
                            if (StringUtil.checkJsonData(street2)) {
                                listStreet.add(street2);
                            }
                            customer.setEmail(customer_email);
                            customer.setFirstName(customer_name);
                            customer.setLastName("");
                            customer.setTelephone(customer_phone);
                            customer.setGroupID(company_type);
                            PosCustomerAddress customerAddress = new PosCustomerAddress();
                            customerAddress.setCustomer(id);
                            customerAddress.setFirstName(customer_name);
                            customerAddress.setLastName("");
                            customerAddress.setRegionID(state_id);
                            customerAddress.setCountry(country_id);
                            customerAddress.setStreet1(street);
                            customerAddress.setStreet1(street2);
                            customerAddress.setTelephone(customer_phone);
                            customerAddress.setPostCode(poscode);
                            customerAddress.setCity(city);
                            customerAddress.setVAT(vat);
                            customerAddress.setCompany(company_name);
                            customerAddress.setDefaultBilling("true");
                            List<CustomerAddress> listCustomerAddress = new ArrayList<>();
                            listCustomerAddress.add(customerAddress);
                            customer.setAddressList(listCustomerAddress);
                            listCustomer.add(customer);
                        }
                    }
                }
                return listCustomer;
            }
        }
    }

    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_CUSOMTER_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(1);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosCustomerParseModelOdoo());
            rp.setParseModel(PosDataCustomer.class);
            DataCustomer listCustomer = (DataCustomer) rp.doParse();

            // return
            return listCustomer.getTotalCount();
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public List<Customer> retrieve() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return retrieve(1, 100);
    }

    @Override
    public List<Customer> retrieve(int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_CUSOMTER_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setSortOrderASC("name");

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosCustomerParseModelOdoo());
            rp.setParseModel(PosDataCustomer.class);
            DataCustomer listCustomer = (DataCustomer) rp.doParse();

            // return
            return listCustomer.getItems();
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public List<Customer> retrieve(String searchString, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        String finalSearch = "%" + searchString + "%";
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_CUSOMTER_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setFilterOrLike("email", finalSearch)
                    .setFilterOrLike("name", finalSearch)
                    .setFilterOrLike("telephone", finalSearch)
                    .setSortOrderASC("name");

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosCustomerParseModelOdoo());
            rp.setParseModel(PosDataCustomer.class);
            DataCustomer listCustomer = (DataCustomer) rp.doParse();

            // return
            return listCustomer.getItems();
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public Customer retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public boolean update(Customer oldModel, Customer newModel) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean insert(Customer... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean delete(Customer... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }
}
