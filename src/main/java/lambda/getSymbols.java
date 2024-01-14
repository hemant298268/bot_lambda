package lambda;

import com.amazonaws.services.lambda.runtime.Context;

import java.sql.ResultSet;

import org.json.JSONArray;

public class getSymbols {

	dbHelper dbH = new dbHelper();
	JSONconverter jc = new JSONconverter();
	
	public String handleRequest(Object arg, Context context) {

		String Response="";
		if(dbH.doesTableExist("trade_history")) {
			try {
				dbH.context = context;
				ResultSet result = dbH.selectQuery("SELECT `th_sym_name`, `th_timestamp`, `th_symbol_volume`, `th_trade_price`, `th_trade_profit`, count(`th_trade_id`) c FROM `trade_history` GROUP BY `th_trade_id` HAVING c=1");
				result.first();
				JSONArray jArray = jc.convertToJSON(result);
				if(jArray.length() > 0) {
					Response = "{"
							+ "\"statusCode\": 200,"
							+ "\"body\":" + jArray.toString()+","
							+ "\"isBase64Encoded\": false"
							+ "}";
				}
				
				return Response;
				//return jArray.toString();
				}
			catch(Exception ex) {
				System.out.println("Exception in method - getPortfolio");
				ex.printStackTrace();
				Response = "{n"
						+ "statusCode: 200,"
						+ "headers: {my_header: my_value},"
						+ "body:{Result:Failure,Reason:Else condition},"
						+ "isBase64Encoded: false"
						+ "}";
				return Response;
			}
		}
		else {
			Response = "{"
					+ "statusCode: 200,"
					+ "headers: {my_header: my_value},"
					+ "body:{Result:Failure,Reason:Else condition},"
					+ "isBase64Encoded: false"
					+ "}";
		}
		return Response;
			
	}
	
public String getSymbol() {

		
		if(dbH.doesTableExist("trade_history")) {
			try {
				ResultSet result = dbH.selectQuery("SELECT `th_sym_name`, `th_timestamp`, `th_symbol_volume`, `th_trade_price`, `th_trade_profit`, count(`th_trade_id`) c FROM `trade_history` GROUP BY `th_trade_id` HAVING c=1");
				JSONArray jArray = jc.convertToJSON(result);
				//System.out.println(jArray.getJSONObject(1).toString());
				return "{"
				+ "\"statusCode\": 200,"
				+ "\"body\":" + jArray.toString()+","
				+ "\"isBase64Encoded\": false"
				+ "}";
				}
			catch(Exception ex) {
				System.out.println("Exception in method - getPortfolio");
				ex.printStackTrace();
				return "{Result:Failure,Reason:Exception}";
			}
		}
		else {
			return "{Result:Failure,Reason:Else condition}";
		}
			
	}
}
