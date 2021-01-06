package nextstep.subway.member.exception;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-07
 */
public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException() {
        super("권한이 없습니다.");
    }
}
