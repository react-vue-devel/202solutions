package app.parseclient.support.jackson;

import app.parseclient.ParsePointer;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.Map;

public class ParsePointerFromMapConverter extends StdConverter<Map, ParsePointer> {

	@Override
	public ParsePointer convert(Map map) {
		return new ParsePointer((String) map.get("className"), (String) map.get("objectId"));
	}
}
