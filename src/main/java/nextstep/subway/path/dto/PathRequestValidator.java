package nextstep.subway.path.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PathRequestValidator implements ConstraintValidator<DifferentSourceAndTarget, PathRequest> {
    @Override
    public boolean isValid(PathRequest pathRequest, ConstraintValidatorContext context) {
        return pathRequest.isDifferentSourceAndTarget();
    }
}
