package parse4j.operation;

import org.json.JSONException;
import parse4j.ParseObject;
import parse4j.encode.ParseObjectEncodingStrategy;

public interface ParseFieldOperation {
	
	abstract Object apply(Object oldValue, ParseObject parseObject, String key);
	
	abstract Object encode(ParseObjectEncodingStrategy objectEncoder) throws JSONException;

}
