package com.magestore.app.pos.api.m2.user;

import com.google.gson.Gson;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.connection.http.MagestoreConnection;
import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.lib.model.store.Store;
import com.magestore.app.lib.model.user.User;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.user.UserDataAccess;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.model.store.PosStore;
import com.magestore.app.pos.model.user.PosUser;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListPointOfSales;
import com.magestore.app.pos.parse.gson2pos.Gson2PosStoreParseImplement;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.io.ObjectInput;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Thực thi các API kết nối đến Magestore Server
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSUserDataAccess extends POSAbstractDataAccess implements UserDataAccess {
    // wrap object lại và chuyênr thành json
    // Cache config đầu tiên
    private static List<Store> mListStore;
//    private static List<PointOfSales> mListPos;

    private class Wrap {
        User staff;
    }

    public POSUserDataAccess() {

    }

    private class POSListStoreDataAccess {
        List<PosStore> stores;
    }

    private class Pos {
        String pos_id;
        String staff_id;
    }

    private class POSCheckPlatformDataAccess {
        String platform;
    }

    @Override
    public String checkPlatform(String domain, String username, String password) throws ParseException, ConnectionException, DataAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        try {
            // Khởi tạo connection
            connection = ConnectionFactory.generateConnection(getContext(), domain, username, password);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CHECK_PLATFORM);

            rp = statement.execute();

            String respone = rp.readResult2String();
            Gson2PosStoreParseImplement implement = new Gson2PosStoreParseImplement();
            Gson gson = implement.createGson();
            POSCheckPlatformDataAccess checkPlatformClass = gson.fromJson(respone, POSCheckPlatformDataAccess.class);
            return checkPlatformClass.platform;
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    /**
     * Login, xem đúng user name và password
     * Nếu đúng trả lại session id, nếu k0 trả lại "false"
     * HTTP POST
     * URL =
     * Param =
     *
     * @param domain
     * @return
     * @throws ParseException
     * @throws ConnectionException
     * @throws DataAccessException
     * @throws IOException
     */
    @Override
    public String login(String domain, String proxyUser, String proxyPassword, final User user) throws ParseException, ConnectionException, DataAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        try {
            // Khởi tạo connection
            connection = ConnectionFactory.generateConnection(getContext(), domain, proxyUser, proxyPassword);
//            connection = MagestoreConnection.getConnection(domain, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_LOGIN);

            Wrap wrap = new Wrap();
            wrap.staff = user;
            rp = statement.execute(wrap);
            return rp.readResult2String();
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public List<Store> retrieveStore() throws ParseException, ConnectionException, DataAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_STORE_GET_LISTING);
//            statement.setEnableCache("POSUSerDataAccess.getListStore");

            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            rp = statement.execute();
            String json = StringUtil.truncateJson(rp.readResult2String());
            Gson2PosStoreParseImplement implement = new Gson2PosStoreParseImplement();
            Gson gson = implement.createGson();
            POSListStoreDataAccess store = gson.fromJson(json, POSListStoreDataAccess.class);
            if (store != null && store.stores.size() > 0) {
                mListStore = (List<Store>) (List<?>) store.stores;
                return mListStore;
            }
            return null;
        } catch (Exception ex) {
            statement.getCacheConnection().deleteCache();
            throw new DataAccessException(ex);
        } finally {
//            // đóng result reading
//            if (rp != null) rp.close();
//            rp = null;
//
//            if (paramBuilder != null) paramBuilder.clear();
//            paramBuilder = null;
//
//            // đóng statement
//            if (statement != null) statement.close();
//            statement = null;
//
//            // đóng connection
//            if (connection != null) connection.close();
//            connection = null;
        }
    }

    @Override
    public List<Store> getListStore() throws ParseException, ConnectionException, DataAccessException, IOException {
        if (mListStore == null) {
            mListStore = new ArrayList<>();
        }
        return mListStore;
    }

    @Override
    public List<PointOfSales> retrievePos() throws ParseException, ConnectionException, DataAccessException, IOException {
//        if (mListPos != null) {
//            return mListPos;
//        }
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_REGISTER_SHIFTS_GET_LISTING_POS);

            paramBuilder = statement.getParamBuilder()
                    .setSortOrderASC("pos_name")
                    .setFilter("staff_id", ConfigUtil.getStaff().getID())
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListPointOfSales.class);
            Gson2PosListPointOfSales listPos = (Gson2PosListPointOfSales) rp.doParse();
            List<PointOfSales> mListPos = (List<PointOfSales>) (List<?>) (listPos.items);
            return mListPos;
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        } finally {
//            // đóng result reading
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
    public void resetListPos() throws ParseException, ConnectionException, DataAccessException, IOException {
//        mListPos = null;
    }

    @Override
    public boolean requestAssignPos(String pos_id) throws ParseException, ConnectionException, DataAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_POS_ASSIGN);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            Pos posEntity = new Pos();
            posEntity.pos_id = pos_id;
            posEntity.staff_id = ConfigUtil.getStaff().getID();

            rp = statement.execute(posEntity);
            return true;
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
}
