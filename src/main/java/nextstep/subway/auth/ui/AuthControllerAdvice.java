package nextstep.subway.auth.ui;

import nextstep.subway.auth.exception.UnapprovedException;
import nextstep.subway.favorite.ui.FavoriteController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice(basePackageClasses = FavoriteController.class)
public class AuthControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(AuthControllerAdvice.class);

    @ExceptionHandler(UnapprovedException.class)
    @ResponseStatus(UNAUTHORIZED)
    public UnapprovedException unapprovedException(HttpServletRequest request, UnapprovedException e) {
        logger.error(e.getMessage(), e);

        return new UnapprovedException(e.getMessage());
    }
}
