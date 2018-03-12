package com.scott.app.OptionsScraper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Set;

public interface IStockOptionFetcher {

    public IStockOptionFetcher setStock(Stock stock);
    public ArrayList<Option> fetchData();

}
