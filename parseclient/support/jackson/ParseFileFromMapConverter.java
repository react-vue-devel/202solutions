package app.parseclient.support.jackson;

import app.parseclient.ParseFile;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.Map;

public class ParseFileFromMapConverter extends StdConverter<Map, ParseFile> {

	@Override
	public ParseFile convert(Map map) {
		return new ParseFile((String) map.get("name"), (String) map.get("url"));
	}
}
