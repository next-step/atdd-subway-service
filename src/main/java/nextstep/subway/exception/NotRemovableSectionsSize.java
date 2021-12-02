package nextstep.subway.exception;

public class NotRemovableSectionsSize extends BadRequestException {
    private static final String EXCEPTION_MESSAGE = "구간이 하나 이하인 노선은 제거할 수 없습니다.";

    public NotRemovableSectionsSize() {
        super(EXCEPTION_MESSAGE);
    }
}
