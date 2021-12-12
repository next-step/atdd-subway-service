package nextstep.subway.favorite.exception;

import nextstep.subway.common.exception.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.member.exception
 * fileName : FavoriteDuplicatedException
 * author : haedoang
 * date : 2021-12-07
 * description :
 */
public class FavoriteDuplicatedException extends ServiceException {
    private static final long serialVersionUID = 1L;

    public static final HttpStatus status = HttpStatus.BAD_REQUEST;
    public static final String message = "즐겨찾기가 이미 등록되어 있습니다.";

    public FavoriteDuplicatedException() {
        super(status, message);
    }
}
