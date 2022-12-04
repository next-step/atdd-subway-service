package nextstep.subway.line.exception;

public class NoSuchDataException extends RuntimeException {

    private static String NO_SUCH_DATA_EXCEPTION = "존재하지 않는 데이터입니다.";
    public NoSuchDataException() {
        super(NO_SUCH_DATA_EXCEPTION);
    }

}
