package com.scott.app.OptionsScraper;

import java.util.Set;

public interface IStockOptionFetcher {

    public IStockOptionFetcher setStock(Stock stock);
    public Set<Option> fetchData();


}
