package nextstep.subway.common.domain;

public class FindFailedException extends RuntimeException {

    public static final String MESSAGE = "조회를 실패하였습니다.";

    public FindFailedException(Class<?> entityClass) {
        super(String.format("%s %s", entityClass.getSimpleName(), MESSAGE));
    }
}
