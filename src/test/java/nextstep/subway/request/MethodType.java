package nextstep.subway.request;

public enum MethodType {
    GET((R, W) -> {
        return R.get(W.getUri(), W.getPathParams());
    }),
    POST((R, W) -> {
        return R.post(W.getUri(), W.getPathParams());
    }),
    DELETE(((R, W) -> {
        return R.delete(W.getUri(), W.getPathParams());
    }));

    WhenFunction function;

    MethodType(WhenFunction function) {
        this.function = function;
    }
}
