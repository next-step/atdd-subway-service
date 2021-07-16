package nextstep.subway.line.exception;

public class UndeletableStationInSectionException extends IllegalArgumentException {
    public UndeletableStationInSectionException(String message) {
        super(message);
    }
}
