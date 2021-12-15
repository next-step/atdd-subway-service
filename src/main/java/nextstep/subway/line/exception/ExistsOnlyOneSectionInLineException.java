package nextstep.subway.line.exception;

public class ExistsOnlyOneSectionInLineException extends RuntimeException {
    public ExistsOnlyOneSectionInLineException(String msg) {
        super(msg);
    }
}
