package app.parseclient;

import app.parseclient.support.Utils;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

class QueryImpl<T> implements Query<T> {

	private final ResourcesImpl<T> resources;
	private final MultivaluedMap<String, String> parameters;

	QueryImpl(ResourcesImpl<T> resources) {
		this.resources = resources;
		parameters = new MultivaluedHashMap<String, String>(10);
	}

	QueryImpl(QueryImpl<T> that) {
		this.resources = that.resources;
		this.parameters = new MultivaluedHashMap<String, String>(that.parameters);
	}

	public Query<T> select(String... keys) {
		return addParameters("keys", keys);
	}

	public Query<T> where(String where) {
		return setParameters("where", where);
	}

	public Query<T> constrain(QueryConstraint queryConstraint) {
		if (queryConstraint instanceof Map) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				resources.getPerspective().getApplication().getParseClient().getMessageBodyWriter()
						.writeTo(queryConstraint, Map.class, null, null,
								MediaType.APPLICATION_JSON_TYPE, null, out);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			String where = new String(out.toByteArray(), Charset.forName("UTF-8"));
			return where(where);
		}
		return this;
	}

	public Query<T> ascending(String ascending) {
		return addParameters("order", ascending);
	}

	public Query<T> descending(String descending) {
		return addParameters("order", "-" + descending);
	}

	public Query<T> limit(int limit) {
		return setParameters("limit", String.valueOf(limit));
	}

	public Query<T> skip(int skip) {
		return setParameters("skip", String.valueOf(skip));
	}

	public Query<T> count() {
		return setParameters("count", "1");
	}

	public QueryResults<T> find() {
		return findOperation().now();
	}

	public Future<QueryResults<T>> findAsync() {
		return findOperation().later();
	}

	public Operation<QueryResults<T>> findOperation() {
		return new OperationImpl<QueryResults<T>>(getInvocationBuilder(),
				OperationImpl.Method.GET, null, getQueryResultsType());
	}

	protected Query<T> addParameters(String name, String... values) {
		QueryImpl<T> clone = new QueryImpl<T>(this);
		clone.parameters.addAll(name, Arrays.asList(values));
		return clone;
	}

	protected Query<T> setParameters(String name, String... values) {
		QueryImpl<T> clone = new QueryImpl<T>(this);
		clone.parameters.put(name, Arrays.asList(values));
		return clone;
	}

	protected Invocation.Builder getInvocationBuilder() {
		WebTarget webTarget = resources.getResourceWebTarget();
		for (Map.Entry<String, List<String>> parameter : parameters.entrySet()) {
			String value;
			if (parameter.getValue().size() > 1) {
				StringBuilder sb = new StringBuilder();
				for (String s : parameter.getValue()) {
					if (sb.length() != 0)
						sb.append(",");
					sb.append(s);
				}
				value = sb.toString();
			} else if (parameter.getValue().size() == 1) {
				value = parameter.getValue().get(0);
			} else {
				continue;
			}
			webTarget = webTarget.queryParam(
					Utils.queryParamSpaceEncoded(parameter.getKey()),
					Utils.queryParamSpaceEncoded(value));
		}
		return webTarget.request().headers(resources.getHeaders());
	}

	protected GenericType<QueryResults<T>> getQueryResultsType() {
		return new GenericType<QueryResults<T>>(new ParameterizedType() {

			public Type[] getActualTypeArguments() {
				return new Type[]{resources.getType()};
			}

			public Type getRawType() {
				return QueryResultsImpl.class;
			}

			public Type getOwnerType() {
				return null;
			}
		});
	}
}
