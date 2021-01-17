package nextstep.subway.advice.exception;

import nextstep.subway.station.domain.Station;

public class MemberBadRequestException extends RuntimeException {

    public MemberBadRequestException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }
}
