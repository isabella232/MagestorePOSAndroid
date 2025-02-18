package com.magestore.app.lib.service.catalog;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.service.ChildListService;
import com.magestore.app.lib.service.ListService;
import com.magestore.app.lib.service.Service;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Mike on 3/1/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ProductOptionService extends Service {
    ProductOption retrieve(Product product) throws IOException, InstantiationException, ParseException, IllegalAccessException;
}
