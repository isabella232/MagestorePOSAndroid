package com.magestore.app.lib.service;

import com.magestore.app.lib.service.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 1/24/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ListService<TModel> extends Service {
    int count() throws ParseException, InstantiationException, IllegalAccessException, IOException;
    TModel create();
    TModel retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    List<TModel> retrieve(int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException;
    List<TModel> retrieve() throws IOException, InstantiationException, ParseException, IllegalAccessException;
    List<TModel> retrieve(String searchString, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException;
    boolean update(TModel oldModel, TModel newModel) throws IOException, InstantiationException, ParseException, IllegalAccessException;
    boolean insert(TModel... models) throws IOException, InstantiationException, ParseException, IllegalAccessException;
    boolean delete(TModel... models) throws IOException, InstantiationException, ParseException, IllegalAccessException;
}