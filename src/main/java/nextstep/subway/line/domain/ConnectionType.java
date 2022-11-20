package nextstep.subway.line.domain;

import java.util.function.BiConsumer;

public enum ConnectionType {
    FIRST((current, request) -> {}),
    MIDDLE(Section::divideSection),
    LAST((current, request) -> {}),
    NONE((current, request) -> {});

    private BiConsumer<Section, Section> expression;

    ConnectionType(BiConsumer<Section, Section> expression) {
        this.expression = expression;
    }

    public static ConnectionType match(Section current, Section request) {
        if(judgeMiddle(current, request)) {
            return ConnectionType.MIDDLE;
        }

        if(judgeFirst(current, request)) {
            return ConnectionType.FIRST;
        }

        if(judgeLast(current, request)) {
            return ConnectionType.LAST;
        }

        return ConnectionType.NONE;
    }

    private static boolean judgeFirst(Section current, Section request) {
        return current.getUpStation().equals(request.getDownStation());
    }

    private static boolean judgeMiddle(Section current, Section request) {
        return current.getUpStation().equals(request.getUpStation()) ||
                current.getDownStation().equals(request.getDownStation());
    }

    private static boolean judgeLast(Section current, Section request) {
        return current.getDownStation().equals(request.getUpStation());
    }

    public void connect(Section current, Section request) {
        expression.accept(current, request);
    }
}
