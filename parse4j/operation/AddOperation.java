package parse4j.operation;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;
import parse4j.ParseObject;
import parse4j.encode.ParseObjectEncodingStrategy;
import parse4j.util.ParseEncoder;

public class AddOperation implements ParseFieldOperation {

	protected final ArrayList<Object> objects = new ArrayList<Object>();

	public AddOperation(Collection<?> coll) {
		this.objects.addAll(coll);
	}

	public AddOperation(Object o) {
		this.objects.add(o);
	}

	@Override
	public Object apply(Object oldValue, ParseObject parseObject, String key) {
		throw new IllegalArgumentException("not implemented!");
	}

	@Override
	public Object encode(ParseObjectEncodingStrategy objectEncoder)
			throws JSONException {
	    JSONObject output = new JSONObject();
	    output.put("__op", "Add");
	    output.put("objects", ParseEncoder.encode(this.objects, objectEncoder));
	    return output;
	}

}
