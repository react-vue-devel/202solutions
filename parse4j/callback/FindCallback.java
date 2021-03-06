package parse4j.callback;

import java.util.List;

import parse4j.ParseException;
import parse4j.ParseObject;

public abstract class FindCallback<T extends ParseObject> extends ParseCallback<List<T>> {

	public abstract void done(List<T> list, ParseException parseException);
	
	@Override
	void internalDone(List<T> list, ParseException parseException) {
		done(list, parseException);
	}
	
}
