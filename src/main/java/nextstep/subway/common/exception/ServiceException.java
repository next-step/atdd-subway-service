package nextstep.subway.common.exception;

import org.springframework.http.HttpStatus;

/**
 * packageName : nextstep.subway.common.exception
 * fileName : ServiceException
 * author : haedoang
 * date : 2021-12-02
 * description : CustomException 추상클래스
 */
public abstract class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final HttpStatus status;

    public ServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
