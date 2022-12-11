package nextstep.subway.line.excpetion;

import static nextstep.subway.exception.ExceptionMessage.*;

import nextstep.subway.exception.BadRequestException;

public class DistanceException extends BadRequestException {

    public DistanceException() {
        super(DISTANCE_ERROR);
    }

}
