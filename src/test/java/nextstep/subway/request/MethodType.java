package nextstep.subway.request;

public enum MethodType {
    GET((R, W) -> {
        if (W.getPathParams() != null) {
            return R.get(W.getUri(), W.getPathParams());
        }
        return R.get(W.getUri());
    }),
    POST((R, W) -> {
        if (W.getPathParams() != null) {
            return R.post(W.getUri(), W.getPathParams());
        }
        return R.post(W.getUri());
    }),
    DELETE((R, W) -> {
        if (W.getPathParams() != null) {
            return R.delete(W.getUri(), W.getPathParams());
        }
        return R.delete(W.getUri());
    }),
    PUT((R, W) -> {
        if (W.getPathParams() != null) {
            return R.put(W.getUri(), W.getPathParams());
        }
        return R.put(W.getUri());
    });

    WhenFunction function;

    MethodType(WhenFunction function) {
        this.function = function;
    }
}
