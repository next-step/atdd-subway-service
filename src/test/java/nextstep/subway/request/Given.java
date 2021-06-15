package nextstep.subway.request;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Given {
    private ContentType contentType;
    private ContentType accept;
    private Object body;

    public Given() {
    }

    public Given(Builder builder) {
        this(builder.contentType, builder.accept, builder.body);
    }

    public Given(ContentType contentType, ContentType accept, Object body) {
        this.contentType = contentType;
        this.accept = accept;
        this.body = body;
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

        return requestSpecification;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ContentType contentType;
        private ContentType accept;
        private Object body;

        public Builder body(Object body) {
            this.body = body;
            return this;
        }

        public Builder accept(ContentType accept) {
            this.accept = accept;
            return this;
        }

        public Builder contentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public Given build() {
            return new Given(this);
        }
    }
}
