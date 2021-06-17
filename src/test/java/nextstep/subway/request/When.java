package nextstep.subway.request;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class When {
    private MethodType type;
    private String uri;
    private Object []pathParams;

    public When() {
    }

    public When(Builder builder) {
        this.uri = builder.uri;
        this.pathParams = builder.pathParams;
    }

    public When(MethodType type, String uri, Object[] pathParams) {
        this.type = type;
        this.uri = uri;
        this.pathParams = pathParams;
    }

    public Response append(RequestSpecification requestSpecification) {
        return type.function.apply(requestSpecification, this);
    }

    public void setType(MethodType type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public Object[] getPathParams() {
        return pathParams;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String uri;
        private Object []pathParams;

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder pathParams(Object ...pathParams) {
            this.pathParams = pathParams;
            return this;
        }

        public When build() {
            return new When(this);
        }
    }
}
