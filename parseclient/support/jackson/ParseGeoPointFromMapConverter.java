package app.parseclient.support.jackson;

import app.parseclient.ParseGeoPoint;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.Map;

public class ParseGeoPointFromMapConverter extends StdConverter<Map, ParseGeoPoint> {

	@Override
	public ParseGeoPoint convert(Map map) {
		return new ParseGeoPoint(
				((Number) map.get("latitude")).doubleValue(),
				((Number) map.get("longitude")).doubleValue());
	}
}
