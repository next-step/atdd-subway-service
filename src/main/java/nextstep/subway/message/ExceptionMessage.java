package nextstep.subway.message;

public class ExceptionMessage {

    public static final String DUPLICATED_SECTION = "이미 등록된 구간 입니다.";
    public static final String INVALID_SECTION = "등록할 수 없는 구간 입니다.";
    public static final String EMPTY_SECTION = "지하철 구간이 비어있습니다.";
    public static final String INVALID_SECTION_DISTANCE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
    public static final String EMPTY_LINE_NAME = "지하철 노선 이름은 필수값입니다.";
    public static final String EMPTY_LINE_COLOR = "지하철 노선 색상은 필수값입니다.";

    private ExceptionMessage() {
    }
}
