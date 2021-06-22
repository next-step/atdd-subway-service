package nextstep.subway.request;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Given {
    private ContentType contentType;
    private ContentType accept;
    private Object body;
    private String bearer;
    private List<Map.Entry<String, Object>> params = new ArrayList<>();

    public Given() {
    }

    public Given(Builder builder) {
        this(builder.contentType, builder.accept, builder.body, builder.bearer, builder.params);
    }

    public Given(ContentType contentType, ContentType accept, Object body, String bearer, List<Map.Entry<String, Object>> params) {
        this.contentType = contentType;
        this.accept = accept;
        this.body = body;
        this.bearer = bearer;
        this.params = params;
    }

    public RequestSpecification append(RequestSpecification requestSpecification) {
        if (this.contentType != null) {
            requestSpecification = requestSpecification.contentType(this.contentType);
        }
        if (this.accept != null) {
            requestSpecification = requestSpecification.accept(this.accept);
        }
        if (this.body != null) {
            requestSpecification = requestSpecification.body(this.body);
        }
        if (this.bearer != null) {
            requestSpecification.auth().oauth2(this.bearer);
        }
        if (params != null && !params.isEmpty()) {
            addParams(requestSpecification);
        }

        return requestSpecification;
    }

    private RequestSpecification addParams(RequestSpecification requestSpecification) {
        for (Map.Entry<String, Object> param : params) {
            requestSpecification = requestSpecification.param(param.getKey(), param.getValue());
        }
        return requestSpecification;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ContentType contentType;
        private ContentType accept;
        private Object body;
        private String bearer;
        private List<Map.Entry<String, Object>> params = new ArrayList<>();


        public Builder contentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder accept(ContentType accept) {
            this.accept = accept;
            return this;
        }

        public Builder body(Object body) {
            this.body = body;
            return this;
        }

        public Builder bearer(String bearer) {
            this.bearer = bearer;
            return this;
        }

        public Builder param(String source, Object value) {
            params.add(new AbstractMap.SimpleEntry<>(source, value));
            return this;
        }

        public Given build() {
            return new Given(this);
        }
    }
}
