package nextstep.subway.fare.exception;

import nextstep.subway.fare.domain.DistanceFare;

public class DistanceFareException extends RuntimeException{
    public DistanceFareException(DistanceFareExceptionCode code){
        super(code.getMessage());
    }
}
