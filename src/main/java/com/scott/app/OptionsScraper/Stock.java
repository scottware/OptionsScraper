package com.scott.app.OptionsScraper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Stock {
	private JSONObject jsonObject = null;
	private String symbol;
	private String underlying_price;
	private boolean DEBUG;
	final boolean LEAP = true;

	public Stock(String symbol) {
		this(symbol, false);
	}

	public Stock(String symbol, boolean debug) {
		this.DEBUG = debug;
		this.symbol = symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public String getLeapURL() {
		return "http://www.google.com/finance/option_chain?q=" + this.getSymbol() + "&output=json";
	}

	public String getDateURL(String year, String month, String day) {
		return "http://www.google.com/finance/option_chain?q=" + this.getSymbol() + "&expd=" + day + "&expm=" + month
				+ "&expy=" + year + "&output=json";
	}

	public JSONObject getJsonObject() {
		if (this.jsonObject == null)
			this.fetchData();
		return jsonObject;

	}

	public void fetchData() {
		fetchLeapData();
		Object oExpirations = (Object) this.jsonObject.get("expirations");
		JSONArray expirations = (JSONArray) oExpirations;
		Iterator<String> iterator = expirations.iterator();
		while (iterator.hasNext()) {
			Object o = iterator.next();
			JSONObject jo = (JSONObject) o;
			String day = jo.get("d").toString();
			String month = jo.get("m").toString();
			String year = jo.get("y").toString();
			JSONObject d = this.fetchDateData(year, month, day);
			System.out.println(year+"/"+month+"/"+day);
			Object oPuts = (Object) d.get("puts");
			JSONArray puts= (JSONArray) oPuts;
			Iterator<String> putsIterator = puts.iterator();
			while(putsIterator.hasNext()) {
				Object oP = putsIterator.next();
				JSONObject put = (JSONObject) oP;
				System.out.println("   " + put.get("strike").toString() + " " + put.get("p").toString());
			}
		}

	}

	public JSONObject fetchDateData(String year, String month, String day) {
		String scrapedURL;
		String dateUrl = this.getDateURL(year, month, day);
		String localDateURL = "{expiry:{y:2014,m:4,d:4},expirations:[{y:2017,m:1,d:20},{y:2017,m:3,d:17},{y:2017,m:4,d:21},{y:2017,m:6,d:16},{y:2017,m:11,d:17},{y:2018,m:1,d:19}],underlying_;id:\"22144\",underlying_price:111.79}";

		if (!DEBUG)
			scrapedURL = Scraper.getURL(dateUrl);
		else
			scrapedURL = localDateURL;

		scrapedURL = Scraper.cleanJSON(scrapedURL);
		if (false && DEBUG) {
			StockOptionPrinter.printJSON(scrapedURL);
		}
		return this.parseData(scrapedURL);
	}

	public void fetchLeapData() {
		String scrapedURL;
		String dateurl = this.getDateURL("2014", "4", "4");
		String leapurl = this.getLeapURL();
		String localDateURL = "{expiry:{y:2014,m:4,d:4},expirations:[{y:2017,m:1,d:20},{y:2017,m:3,d:17},{y:2017,m:4,d:21},{y:2017,m:6,d:16},{y:2017,m:11,d:17},{y:2018,m:1,d:19}],underlying_;id:\"22144\",underlying_price:111.79}";
		String localLeapURL = "	{expiry:{y:2017,m:1,d:20},expirations:[{y:2017,m:1,d:20},{y:2017,m:3,d:17},{y:2017,m:4,d:21},{y:2017,m:6,d:16},{y:2017,m:11,d:17},{y:2018,m:1,d:19}],puts:[{cid:\"597774406980930\",name:\"\",s:\"AAPL170120P00015000\",e:\"OPRA\",p:\"0.02\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.02\",oi:\"10\",vol:\"-\",strike:\"15.00\",expiry:\"Jan 20, 2017\"},{cid:\"786362103926241\",name:\"\",s:\"AAPL170120P00017500\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.01\",oi:\"34\",vol:\"-\",strike:\"17.50\",expiry:\"Jan 20, 2017\"},{cid:\"1052748866201870\",name:\"\",s:\"AAPL170120P00020000\",e:\"OPRA\",p:\"0.02\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.02\",oi:\"100\",vol:\"-\",strike:\"20.00\",expiry:\"Jan 20, 2017\"},{cid:\"189999896672868\",name:\"\",s:\"AAPL170120P00022500\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.02\",oi:\"5033\",vol:\"-\",strike:\"22.50\",expiry:\"Jan 20, 2017\"},{cid:\"172771666401921\",name:\"\",s:\"AAPL170120P00025000\",e:\"OPRA\",p:\"0.02\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.02\",oi:\"237\",vol:\"-\",strike:\"25.00\",expiry:\"Jan 20, 2017\"},{cid:\"105111802473710\",name:\"\",s:\"AAPL170120P00030000\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.02\",oi:\"646\",vol:\"-\",strike:\"30.00\",expiry:\"Jan 20, 2017\"},{cid:\"245034947121505\",name:\"\",s:\"AAPL170120P00035000\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.02\",oi:\"988\",vol:\"-\",strike:\"35.00\",expiry:\"Jan 20, 2017\"},{cid:\"1077432118044741\",name:\"\",s:\"AAPL170120P00040000\",e:\"OPRA\",p:\"0.04\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.02\",oi:\"2107\",vol:\"-\",strike:\"40.00\",expiry:\"Jan 20, 2017\"},{cid:\"163842351905119\",name:\"\",s:\"AAPL170120P00042500\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.02\",oi:\"412\",vol:\"-\",strike:\"42.50\",expiry:\"Jan 20, 2017\"},{cid:\"288153283835091\",name:\"\",s:\"AAPL170120P00045000\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.02\",oi:\"2006\",vol:\"-\",strike:\"45.00\",expiry:\"Jan 20, 2017\"},{cid:\"742817243142634\",name:\"\",s:\"AAPL170120P00047500\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.01\",oi:\"8334\",vol:\"-\",strike:\"47.50\",expiry:\"Jan 20, 2017\"},{cid:\"835657733521141\",name:\"\",s:\"AAPL170120P00050000\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.01\",oi:\"8293\",vol:\"-\",strike:\"50.00\",expiry:\"Jan 20, 2017\"},{cid:\"1042859053499452\",name:\"\",s:\"AAPL170120P00055000\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.01\",oi:\"12888\",vol:\"10\",strike:\"55.00\",expiry:\"Jan 20, 2017\"},{cid:\"478060985082248\",name:\"\",s:\"AAPL170120P00060000\",e:\"OPRA\",p:\"0.02\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.01\",oi:\"14234\",vol:\"-\",strike:\"60.00\",expiry:\"Jan 20, 2017\"},{cid:\"157461442005011\",name:\"\",s:\"AAPL170120P00065000\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.01\",oi:\"20277\",vol:\"-\",strike:\"65.00\",expiry:\"Jan 20, 2017\"},{cid:\"28747393314419\",name:\"\",s:\"AAPL170120P00070000\",e:\"OPRA\",p:\"0.02\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"0.01\",a:\"0.02\",oi:\"37351\",vol:\"6\",strike:\"70.00\",expiry:\"Jan 20, 2017\"},{cid:\"653843371407844\",name:\"\",s:\"AAPL170120P00075000\",e:\"OPRA\",p:\"0.03\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"0.02\",a:\"0.03\",oi:\"40106\",vol:\"1\",strike:\"75.00\",expiry:\"Jan 20, 2017\"},{cid:\"211452522853988\",name:\"\",s:\"AAPL170120P00080000\",e:\"OPRA\",p:\"0.06\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"0.05\",a:\"0.06\",oi:\"56562\",vol:\"15\",strike:\"80.00\",expiry:\"Jan 20, 2017\"},{cid:\"17720790315931\",name:\"\",s:\"AAPL170120P00082500\",e:\"OPRA\",p:\"0.08\",cs:\"chr\",c:\"-0.02\",cp:\"-20.00\",b:\"0.07\",a:\"0.08\",oi:\"12198\",vol:\"2\",strike:\"82.50\",expiry:\"Jan 20, 2017\"},{cid:\"1097723922663259\",name:\"\",s:\"AAPL170120P00085000\",e:\"OPRA\",p:\"0.10\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"0.08\",a:\"0.09\",oi:\"41268\",vol:\"5\",strike:\"85.00\",expiry:\"Jan 20, 2017\"},{cid:\"279484784421854\",name:\"\",s:\"AAPL170120P00087500\",e:\"OPRA\",p:\"0.12\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"0.10\",a:\"0.11\",oi:\"23638\",vol:\"10\",strike:\"87.50\",expiry:\"Jan 20, 2017\"},{cid:\"239655715911680\",name:\"\",s:\"AAPL170120P00090000\",e:\"OPRA\",p:\"0.14\",cs:\"chr\",c:\"-0.01\",cp:\"-6.67\",b:\"0.13\",a:\"0.14\",oi:\"49011\",vol:\"108\",strike:\"90.00\",expiry:\"Jan 20, 2017\"},{cid:\"755896801262103\",name:\"\",s:\"AAPL170120P00092500\",e:\"OPRA\",p:\"0.18\",cs:\"chr\",c:\"-0.02\",cp:\"-10.00\",b:\"0.17\",a:\"0.18\",oi:\"25517\",vol:\"20\",strike:\"92.50\",expiry:\"Jan 20, 2017\"},{cid:\"366726883581902\",name:\"\",s:\"AAPL170120P00095000\",e:\"OPRA\",p:\"0.24\",cs:\"chr\",c:\"-0.02\",cp:\"-7.69\",b:\"0.23\",a:\"0.24\",oi:\"41731\",vol:\"78\",strike:\"95.00\",expiry:\"Jan 20, 2017\"},{cid:\"257495565356606\",name:\"\",s:\"AAPL170120P00097500\",e:\"OPRA\",p:\"0.31\",cs:\"chr\",c:\"-0.06\",cp:\"-16.22\",b:\"0.31\",a:\"0.33\",oi:\"20823\",vol:\"327\",strike:\"97.50\",expiry:\"Jan 20, 2017\"},{cid:\"980536480322939\",name:\"\",s:\"AAPL170120P00100000\",e:\"OPRA\",p:\"0.47\",cs:\"chr\",c:\"-0.05\",cp:\"-9.62\",b:\"0.45\",a:\"0.47\",oi:\"69531\",vol:\"249\",strike:\"100.00\",expiry:\"Jan 20, 2017\"},{cid:\"560660706316376\",name:\"\",s:\"AAPL170120P00105000\",e:\"OPRA\",p:\"1.05\",cs:\"chr\",c:\"-0.14\",cp:\"-11.76\",b:\"1.03\",a:\"1.04\",oi:\"66669\",vol:\"478\",strike:\"105.00\",expiry:\"Jan 20, 2017\"},{cid:\"683470946853234\",name:\"\",s:\"AAPL170120P00110000\",e:\"OPRA\",p:\"2.38\",cs:\"chr\",c:\"-0.28\",cp:\"-10.53\",b:\"2.35\",a:\"2.38\",oi:\"97927\",vol:\"505\",strike:\"110.00\",expiry:\"Jan 20, 2017\"},{cid:\"776696173962165\",name:\"\",s:\"AAPL170120P00115000\",e:\"OPRA\",p:\"5.00\",cs:\"chr\",c:\"-0.40\",cp:\"-7.41\",b:\"4.85\",a:\"5.00\",oi:\"56031\",vol:\"187\",strike:\"115.00\",expiry:\"Jan 20, 2017\"},{cid:\"200327955779951\",name:\"\",s:\"AAPL170120P00120000\",e:\"OPRA\",p:\"8.85\",cs:\"chr\",c:\"-0.40\",cp:\"-4.32\",b:\"8.60\",a:\"8.85\",oi:\"60936\",vol:\"12\",strike:\"120.00\",expiry:\"Jan 20, 2017\"},{cid:\"123901616499741\",name:\"\",s:\"AAPL170120P00125000\",e:\"OPRA\",p:\"13.70\",cs:\"chr\",c:\"-0.15\",cp:\"-1.08\",b:\"13.20\",a:\"13.50\",oi:\"21774\",vol:\"6\",strike:\"125.00\",expiry:\"Jan 20, 2017\"},{cid:\"100547417324939\",name:\"\",s:\"AAPL170120P00130000\",e:\"OPRA\",p:\"18.25\",cs:\"chr\",c:\"-0.88\",cp:\"-4.60\",b:\"18.15\",a:\"18.40\",oi:\"13034\",vol:\"24\",strike:\"130.00\",expiry:\"Jan 20, 2017\"},{cid:\"143686226992074\",name:\"\",s:\"AAPL170120P00135000\",e:\"OPRA\",p:\"22.90\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"23.05\",a:\"23.45\",oi:\"10460\",vol:\"-\",strike:\"135.00\",expiry:\"Jan 20, 2017\"},{cid:\"637782334211295\",name:\"\",s:\"AAPL170120P00140000\",e:\"OPRA\",p:\"29.12\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"28.05\",a:\"28.45\",oi:\"21074\",vol:\"-\",strike:\"140.00\",expiry:\"Jan 20, 2017\"},{cid:\"1081350483735587\",name:\"\",s:\"AAPL170120P00145000\",e:\"OPRA\",p:\"34.03\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"32.95\",a:\"33.45\",oi:\"1286\",vol:\"-\",strike:\"145.00\",expiry:\"Jan 20, 2017\"},{cid:\"766414227716845\",name:\"\",s:\"AAPL170120P00150000\",e:\"OPRA\",p:\"39.00\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"37.95\",a:\"38.45\",oi:\"7408\",vol:\"-\",strike:\"150.00\",expiry:\"Jan 20, 2017\"},{cid:\"228162470751009\",name:\"\",s:\"AAPL170120P00155000\",e:\"OPRA\",p:\"43.40\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"42.95\",a:\"43.45\",oi:\"426\",vol:\"-\",strike:\"155.00\",expiry:\"Jan 20, 2017\"},{cid:\"447610413023961\",name:\"\",s:\"AAPL170120P00160000\",e:\"OPRA\",p:\"48.40\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"47.95\",a:\"48.45\",oi:\"1\",vol:\"-\",strike:\"160.00\",expiry:\"Jan 20, 2017\"},{cid:\"352684976495981\",name:\"\",s:\"AAPL170120P00165000\",e:\"OPRA\",p:\"53.40\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"52.95\",a:\"53.45\",oi:\"82\",vol:\"-\",strike:\"165.00\",expiry:\"Jan 20, 2017\"},{cid:\"375639546557921\",name:\"\",s:\"AAPL170120P00170000\",e:\"OPRA\",p:\"58.40\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"57.95\",a:\"58.45\",oi:\"38\",vol:\"-\",strike:\"170.00\",expiry:\"Jan 20, 2017\"},{cid:\"737998196215292\",name:\"\",s:\"AAPL170120P00175000\",e:\"OPRA\",p:\"63.40\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"62.95\",a:\"63.45\",oi:\"62\",vol:\"-\",strike:\"175.00\",expiry:\"Jan 20, 2017\"},{cid:\"921483872947776\",name:\"\",s:\"AAPL170120P00180000\",e:\"OPRA\",p:\"68.40\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"67.95\",a:\"68.45\",oi:\"3\",vol:\"-\",strike:\"180.00\",expiry:\"Jan 20, 2017\"},{cid:\"69475530800447\",name:\"\",s:\"AAPL170120P00185000\",e:\"OPRA\",p:\"73.40\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"72.95\",a:\"73.45\",oi:\"24\",vol:\"-\",strike:\"185.00\",expiry:\"Jan 20, 2017\"},{cid:\"660039574229066\",name:\"\",s:\"AAPL170120P00190000\",e:\"OPRA\",p:\"78.40\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"77.95\",a:\"78.45\",oi:\"35\",vol:\"-\",strike:\"190.00\",expiry:\"Jan 20, 2017\"},{cid:\"523238137481036\",name:\"\",s:\"AAPL170120P00195000\",e:\"OPRA\",p:\"88.20\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"82.95\",a:\"83.45\",oi:\"20\",vol:\"-\",strike:\"195.00\",expiry:\"Jan 20, 2017\"},{cid:\"451577598322974\",name:\"\",s:\"AAPL170120P00200000\",e:\"OPRA\",p:\"90.00\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"87.95\",a:\"88.45\",oi:\"71\",vol:\"-\",strike:\"200.00\",expiry:\"Jan 20, 2017\"},{cid:\"22690448057920\",name:\"\",s:\"AAPL170120P00205000\",e:\"OPRA\",p:\"95.26\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"92.95\",a:\"93.45\",oi:\"78\",vol:\"-\",strike:\"205.00\",expiry:\"Jan 20, 2017\"}],calls:[{cid:\"878535206671614\",name:\"\",s:\"AAPL170120C00015000\",e:\"OPRA\",p:\"95.50\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"96.55\",a:\"97.05\",oi:\"4\",vol:\"-\",strike:\"15.00\",expiry:\"Jan 20, 2017\"},{cid:\"258297643738901\",name:\"\",s:\"AAPL170120C00017500\",e:\"OPRA\",p:\"88.00\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"94.05\",a:\"94.55\",oi:\"0\",vol:\"-\",strike:\"17.50\",expiry:\"Jan 20, 2017\"},{cid:\"1115259807800765\",name:\"\",s:\"AAPL170120C00020000\",e:\"OPRA\",p:\"92.06\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"91.55\",a:\"92.05\",oi:\"0\",vol:\"-\",strike:\"20.00\",expiry:\"Jan 20, 2017\"},{cid:\"593024378982375\",name:\"\",s:\"AAPL170120C00022500\",e:\"OPRA\",p:\"-\",c:\"-\",b:\"89.10\",a:\"89.60\",oi:\"0\",vol:\"-\",strike:\"22.50\",expiry:\"Jan 20, 2017\"},{cid:\"371477004233402\",name:\"\",s:\"AAPL170120C00025000\",e:\"OPRA\",p:\"86.97\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"86.60\",a:\"87.10\",oi:\"0\",vol:\"-\",strike:\"25.00\",expiry:\"Jan 20, 2017\"},{cid:\"168322905858261\",name:\"\",s:\"AAPL170120C00030000\",e:\"OPRA\",p:\"79.18\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"81.60\",a:\"82.10\",oi:\"0\",vol:\"-\",strike:\"30.00\",expiry:\"Jan 20, 2017\"},{cid:\"853086126134950\",name:\"\",s:\"AAPL170120C00035000\",e:\"OPRA\",p:\"70.60\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"76.60\",a:\"77.10\",oi:\"0\",vol:\"-\",strike:\"35.00\",expiry:\"Jan 20, 2017\"},{cid:\"893854245773305\",name:\"\",s:\"AAPL170120C00040000\",e:\"OPRA\",p:\"65.61\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"71.60\",a:\"72.10\",oi:\"0\",vol:\"-\",strike:\"40.00\",expiry:\"Jan 20, 2017\"},{cid:\"604334709266758\",name:\"\",s:\"AAPL170120C00042500\",e:\"OPRA\",p:\"-\",c:\"-\",b:\"69.10\",a:\"69.60\",oi:\"0\",vol:\"-\",strike:\"42.50\",expiry:\"Jan 20, 2017\"},{cid:\"978381305650921\",name:\"\",s:\"AAPL170120C00045000\",e:\"OPRA\",p:\"67.05\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"66.65\",a:\"67.15\",oi:\"0\",vol:\"-\",strike:\"45.00\",expiry:\"Jan 20, 2017\"},{cid:\"586353864883003\",name:\"\",s:\"AAPL170120C00047500\",e:\"OPRA\",p:\"64.55\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"64.15\",a:\"64.65\",oi:\"0\",vol:\"-\",strike:\"47.50\",expiry:\"Jan 20, 2017\"},{cid:\"741357270623760\",name:\"\",s:\"AAPL170120C00050000\",e:\"OPRA\",p:\"61.10\",cs:\"chr\",c:\"-0.58\",cp:\"-0.94\",b:\"61.65\",a:\"62.15\",oi:\"1573\",vol:\"1\",strike:\"50.00\",expiry:\"Jan 20, 2017\"},{cid:\"135360561554387\",name:\"\",s:\"AAPL170120C00055000\",e:\"OPRA\",p:\"55.15\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"56.65\",a:\"57.15\",oi:\"217\",vol:\"-\",strike:\"55.00\",expiry:\"Jan 20, 2017\"},{cid:\"25098878057316\",name:\"\",s:\"AAPL170120C00060000\",e:\"OPRA\",p:\"52.00\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"51.65\",a:\"52.15\",oi:\"1516\",vol:\"-\",strike:\"60.00\",expiry:\"Jan 20, 2017\"},{cid:\"708701912003081\",name:\"\",s:\"AAPL170120C00065000\",e:\"OPRA\",p:\"45.20\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"46.65\",a:\"47.15\",oi:\"281\",vol:\"-\",strike:\"65.00\",expiry:\"Jan 20, 2017\"},{cid:\"956691324762372\",name:\"\",s:\"AAPL170120C00070000\",e:\"OPRA\",p:\"41.90\",cs:\"chg\",c:\"+0.85\",cp:\"2.07\",b:\"41.65\",a:\"42.15\",oi:\"2764\",vol:\"4\",strike:\"70.00\",expiry:\"Jan 20, 2017\"},{cid:\"927100139707104\",name:\"\",s:\"AAPL170120C00075000\",e:\"OPRA\",p:\"36.05\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"36.75\",a:\"37.20\",oi:\"953\",vol:\"-\",strike:\"75.00\",expiry:\"Jan 20, 2017\"},{cid:\"510780943256452\",name:\"\",s:\"AAPL170120C00080000\",e:\"OPRA\",p:\"31.02\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"31.75\",a:\"32.10\",oi:\"8507\",vol:\"-\",strike:\"80.00\",expiry:\"Jan 20, 2017\"},{cid:\"281633030759023\",name:\"\",s:\"AAPL170120C00082500\",e:\"OPRA\",p:\"27.92\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"29.25\",a:\"29.70\",oi:\"40\",vol:\"-\",strike:\"82.50\",expiry:\"Jan 20, 2017\"},{cid:\"932798051981365\",name:\"\",s:\"AAPL170120C00085000\",e:\"OPRA\",p:\"26.87\",cs:\"chr\",c:\"-0.23\",cp:\"-0.85\",b:\"26.85\",a:\"27.10\",oi:\"4301\",vol:\"203\",strike:\"85.00\",expiry:\"Jan 20, 2017\"},{cid:\"371509092421029\",name:\"\",s:\"AAPL170120C00087500\",e:\"OPRA\",p:\"24.50\",cs:\"chr\",c:\"-0.01\",cp:\"-0.04\",b:\"24.40\",a:\"24.65\",oi:\"865\",vol:\"12\",strike:\"87.50\",expiry:\"Jan 20, 2017\"},{cid:\"848928301878301\",name:\"\",s:\"AAPL170120C00090000\",e:\"OPRA\",p:\"21.30\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"21.90\",a:\"22.25\",oi:\"19297\",vol:\"-\",strike:\"90.00\",expiry:\"Jan 20, 2017\"},{cid:\"978627956676504\",name:\"\",s:\"AAPL170120C00092500\",e:\"OPRA\",p:\"19.60\",cs:\"chg\",c:\"+0.79\",cp:\"4.20\",b:\"19.45\",a:\"19.75\",oi:\"5103\",vol:\"15\",strike:\"92.50\",expiry:\"Jan 20, 2017\"},{cid:\"215304180898\",name:\"\",s:\"AAPL170120C00095000\",e:\"OPRA\",p:\"17.15\",cs:\"chg\",c:\"+0.90\",cp:\"5.54\",b:\"17.00\",a:\"17.30\",oi:\"26759\",vol:\"148\",strike:\"95.00\",expiry:\"Jan 20, 2017\"},{cid:\"851055984452723\",name:\"\",s:\"AAPL170120C00097500\",e:\"OPRA\",p:\"13.96\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"14.60\",a:\"14.90\",oi:\"15469\",vol:\"-\",strike:\"97.50\",expiry:\"Jan 20, 2017\"},{cid:\"271623201200121\",name:\"\",s:\"AAPL170120C00100000\",e:\"OPRA\",p:\"12.38\",cs:\"chg\",c:\"+0.63\",cp:\"5.36\",b:\"12.35\",a:\"12.50\",oi:\"105304\",vol:\"496\",strike:\"100.00\",expiry:\"Jan 20, 2017\"},{cid:\"469905718024193\",name:\"\",s:\"AAPL170120C00105000\",e:\"OPRA\",p:\"7.85\",cs:\"chg\",c:\"+0.31\",cp:\"4.11\",b:\"7.85\",a:\"8.05\",oi:\"49027\",vol:\"174\",strike:\"105.00\",expiry:\"Jan 20, 2017\"},{cid:\"227041082782758\",name:\"\",s:\"AAPL170120C00110000\",e:\"OPRA\",p:\"4.31\",cs:\"chg\",c:\"+0.31\",cp:\"7.75\",b:\"4.25\",a:\"4.35\",oi:\"129865\",vol:\"3430\",strike:\"110.00\",expiry:\"Jan 20, 2017\"},{cid:\"57728260456098\",name:\"\",s:\"AAPL170120C00115000\",e:\"OPRA\",p:\"1.85\",cs:\"chg\",c:\"+0.16\",cp:\"9.47\",b:\"1.84\",a:\"1.87\",oi:\"125279\",vol:\"2689\",strike:\"115.00\",expiry:\"Jan 20, 2017\"},{cid:\"171292952005756\",name:\"\",s:\"AAPL170120C00120000\",e:\"OPRA\",p:\"0.64\",cs:\"chg\",c:\"+0.04\",cp:\"6.67\",b:\"0.64\",a:\"0.66\",oi:\"166013\",vol:\"1452\",strike:\"120.00\",expiry:\"Jan 20, 2017\"},{cid:\"44868495662740\",name:\"\",s:\"AAPL170120C00125000\",e:\"OPRA\",p:\"0.24\",cs:\"chg\",c:\"+0.02\",cp:\"9.09\",b:\"0.23\",a:\"0.24\",oi:\"82287\",vol:\"302\",strike:\"125.00\",expiry:\"Jan 20, 2017\"},{cid:\"84113314199927\",name:\"\",s:\"AAPL170120C00130000\",e:\"OPRA\",p:\"0.11\",cs:\"chr\",c:\"-0.01\",cp:\"-8.33\",b:\"0.11\",a:\"0.12\",oi:\"108934\",vol:\"123\",strike:\"130.00\",expiry:\"Jan 20, 2017\"},{cid:\"183468140620900\",name:\"\",s:\"AAPL170120C00135000\",e:\"OPRA\",p:\"0.08\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"0.07\",a:\"0.08\",oi:\"41158\",vol:\"4\",strike:\"135.00\",expiry:\"Jan 20, 2017\"},{cid:\"109540967198459\",name:\"\",s:\"AAPL170120C00140000\",e:\"OPRA\",p:\"0.06\",cs:\"chg\",c:\"+0.01\",cp:\"20.00\",b:\"0.04\",a:\"0.05\",oi:\"97120\",vol:\"1\",strike:\"140.00\",expiry:\"Jan 20, 2017\"},{cid:\"322833911373322\",name:\"\",s:\"AAPL170120C00145000\",e:\"OPRA\",p:\"0.03\",cs:\"chr\",c:\"-0.01\",cp:\"-25.00\",b:\"0.03\",a:\"0.04\",oi:\"16450\",vol:\"4\",strike:\"145.00\",expiry:\"Jan 20, 2017\"},{cid:\"119119735537926\",name:\"\",s:\"AAPL170120C00150000\",e:\"OPRA\",p:\"0.02\",cs:\"chr\",c:\"-0.01\",cp:\"-33.33\",b:\"0.02\",a:\"0.03\",oi:\"69871\",vol:\"1014\",strike:\"150.00\",expiry:\"Jan 20, 2017\"},{cid:\"256354567622311\",name:\"\",s:\"AAPL170120C00155000\",e:\"OPRA\",p:\"0.02\",cs:\"chr\",c:\"-0.01\",cp:\"-33.33\",b:\"0.01\",a:\"0.02\",oi:\"15260\",vol:\"1\",strike:\"155.00\",expiry:\"Jan 20, 2017\"},{cid:\"590017013906374\",name:\"\",s:\"AAPL170120C00160000\",e:\"OPRA\",p:\"0.02\",cs:\"chg\",c:\"+0.01\",cp:\"100.00\",b:\"0.01\",a:\"0.02\",oi:\"12630\",vol:\"1\",strike:\"160.00\",expiry:\"Jan 20, 2017\"},{cid:\"23764706627668\",name:\"\",s:\"AAPL170120C00165000\",e:\"OPRA\",p:\"0.02\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"0.01\",a:\"0.02\",oi:\"10331\",vol:\"-\",strike:\"165.00\",expiry:\"Jan 20, 2017\"},{cid:\"6382099831326\",name:\"\",s:\"AAPL170120C00170000\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.01\",oi:\"7809\",vol:\"-\",strike:\"170.00\",expiry:\"Jan 20, 2017\"},{cid:\"241873647728230\",name:\"\",s:\"AAPL170120C00175000\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.01\",oi:\"10783\",vol:\"10\",strike:\"175.00\",expiry:\"Jan 20, 2017\"},{cid:\"1073566485187567\",name:\"\",s:\"AAPL170120C00180000\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.02\",oi:\"7728\",vol:\"-\",strike:\"180.00\",expiry:\"Jan 20, 2017\"},{cid:\"553875836479945\",name:\"\",s:\"AAPL170120C00185000\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.01\",oi:\"6106\",vol:\"-\",strike:\"185.00\",expiry:\"Jan 20, 2017\"},{cid:\"94703379890001\",name:\"\",s:\"AAPL170120C00190000\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.02\",oi:\"9376\",vol:\"-\",strike:\"190.00\",expiry:\"Jan 20, 2017\"},{cid:\"299193149792440\",name:\"\",s:\"AAPL170120C00195000\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.01\",oi:\"29982\",vol:\"-\",strike:\"195.00\",expiry:\"Jan 20, 2017\"},{cid:\"915043290844004\",name:\"\",s:\"AAPL170120C00200000\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.02\",oi:\"1154\",vol:\"-\",strike:\"200.00\",expiry:\"Jan 20, 2017\"},{cid:\"783524268298000\",name:\"\",s:\"AAPL170120C00205000\",e:\"OPRA\",p:\"0.01\",cs:\"chb\",c:\"0.00\",cp:\"0.00\",b:\"-\",a:\"0.02\",oi:\"800\",vol:\"-\",strike:\"205.00\",expiry:\"Jan 20, 2017\"}],underlying_id:\"22144\",underlying_price:111.77}";

		if (!DEBUG)
			scrapedURL = Scraper.getURL(leapurl);
		else
			scrapedURL = localLeapURL;

		scrapedURL = Scraper.cleanJSON(scrapedURL);
		if (false && DEBUG) {
			StockOptionPrinter.printJSON(scrapedURL);
		}
		this.jsonObject = this.parseData(scrapedURL);
		this.underlying_price = this.jsonObject.get("underlying_price").toString();
	}

	private JSONObject parseData(String data) {
		JSONParser parser = new JSONParser();
		Object obj = null;
		try {
			obj = parser.parse(data);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (JSONObject) obj;
	}

}