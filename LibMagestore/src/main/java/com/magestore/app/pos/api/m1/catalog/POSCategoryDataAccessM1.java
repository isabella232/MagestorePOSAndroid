package com.magestore.app.pos.api.m1.catalog;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.catalog.CategoryDataAccess;
import com.magestore.app.pos.api.m1.POSAPIM1;
import com.magestore.app.pos.api.m1.POSDataAccessSessionM1;
import com.magestore.app.pos.api.m1.POSAbstractDataAccessM1;
import com.magestore.app.pos.model.catalog.PosCategory;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListCategory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 8/7/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSCategoryDataAccessM1 extends POSAbstractDataAccessM1 implements CategoryDataAccess {
    public static List<Category> mListCategory;
    public static List<Category> mListDefaultCategory;

    @Override
    public List<Category> getListCategory(Category category) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mListCategory == null) mListCategory = new ArrayList<Category>();
        if (mListDefaultCategory == null) mListDefaultCategory = new ArrayList<Category>();
        PosCategory cateFirst = new PosCategory();
        cateFirst.setName("All Products");
        mListDefaultCategory.add(cateFirst);
        for (Category c : mListCategory) {
            if (c.getLevel() == 1) {
                mListDefaultCategory.add(c);
                if (c.getChildren() != null) {
                    for (String IdChild : c.getChildren()) {
                        mListDefaultCategory = findChildLv2(mListDefaultCategory, IdChild, mListCategory, 2, c.getSubCategory());
                    }
                }
            }
        }
        return mListDefaultCategory;
    }

    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return 0;
    }

    @Override
    public List<Category> retrieve() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<Category> retrieve(int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_GET_CATEGORY_LISTING);
            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListCategory.class);
            Gson2PosListCategory listCategory = (Gson2PosListCategory) rp.doParse();
            mListCategory = (List<Category>) (List<?>) (listCategory.items);
            return getListCategory(null);
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
    public List<Category> retrieve(String searchString, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public Category retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public boolean update(Category oldModel, Category newModel) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean insert(Category... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean delete(Category... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    private List<Category> findChildLv2(List<Category> resultList, String idChild, List<Category> categories, int level, List<Category> sub_category) {
        for (Category category : categories) {
            if (category.getLevel() == level && category.getID().equals(idChild)) {
                sub_category.add(category);
                if (category.getChildren() != null) {
                    for (int i = 0; i < category.getChildren().size(); i++) {
                        category.setID(category.getChildren().get(i) + ",");
                        if (i == category.getChildren().size() - 1) {
                            category.setID(category.getChildren().get(i));
                        }
                    }
                    resultList.add(category);
                    level++;
                    for (String idSubChild : category.getChildren()) {
                        resultList = findChildLv2(resultList, idSubChild, categories, level, category.getSubCategory());
                    }
                } else {
                    resultList.add(category);
                }
            }
        }
        return resultList;
    }
}
