package nextstep.subway.path.ui;

public class NoStationInListException extends RuntimeException {
    public NoStationInListException() {
        super("노선에 등록되어 있지 않은 역입니다");
    }
}
