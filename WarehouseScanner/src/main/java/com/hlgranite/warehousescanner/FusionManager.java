package com.hlgranite.warehousescanner;

import com.google.api.client.util.Key;

import java.util.List;

/**
 * Created by yeang-shing.then on 9/18/13.
 * source: http://code.google.com/p/google-api-java-client/source/browse/fusiontables-cmdline-sample/src/main/java/com/google/api/services/samples/fusiontables/cmdline/FusionTablesSample.java?repo=samples
 */
public class FusionManager {

    @Key("rows")
    private List<Stock> stocks;
    public List<Stock> getStocks() {
        return this.stocks;
    }

//    public FusionManager() {
//
//    }
}
