package alma.obops.aqua.qa0.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import alma.obops.aqua.qa0.domain.Account;
import alma.obops.utils.DatetimeUtils;

public class SnooPIUtils {
	private static final String outputDateFormat = "yyyy-MM-dd HH:mm:ss";
	
	public static String convertToUserFriendlyDateString(String dateString){
		String inputFormatString = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		SimpleDateFormat sdf = new SimpleDateFormat(inputFormatString); 
		sdf.setTimeZone(DatetimeUtils.UT);
		try {
			Date date = sdf.parse(dateString);
			SimpleDateFormat sdfOut = new SimpleDateFormat(outputDateFormat); 
			sdfOut.setTimeZone(DatetimeUtils.UT);
			return sdfOut.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String composeStringFromMap(Map<String, Object> opMap){
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (String key : opMap.keySet()){
			String value = (String) opMap.get(key);
			sb.append(value);
			if( i != opMap.size() - 1 )
				sb.append(", ");
		}
		return sb.toString();
	}

	public static void checkIfDefined( final String propName, final String url ) {
		// Make sure the property is defined
		if( url == null || url.length() == 0 ) {
			String msg = "ObOps configuration property " + propName +
					" is not defined";
			throw new RuntimeException( msg );
		}
	}

	public static List<String> accountListToAccountIdList(List<Account> accounts){
		List<String> accountIds = new ArrayList<>();
		if ( accounts != null ) {
			for (Account acc : accounts) {
				accountIds.add(acc.getAccountId());
			}
		}
		return accountIds;
	}

}
