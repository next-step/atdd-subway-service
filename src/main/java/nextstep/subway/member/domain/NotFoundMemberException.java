package nextstep.subway.member.domain;

public class NotFoundMemberException extends RuntimeException {

    private static final long serialVersionUID = 3834810736444681365L;

    public NotFoundMemberException() {
        this("입력하신 계정 정보를 찾을 수 없습니다.");
    }

    public NotFoundMemberException(String message) {
        super(message);
    }
}
