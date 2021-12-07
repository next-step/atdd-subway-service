package nextstep.subway.member.exception;

import nextstep.subway.common.exception.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.member.domain
 * fileName : FavoriteNotFoundException
 * author : haedoang
 * date : 2021-12-07
 * description :
 */
public class FavoriteNotFoundException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public static final String message = "즐겨찾기가 존재하지 않습니다.";

    public FavoriteNotFoundException() {
        super(status, message);
    }
}
