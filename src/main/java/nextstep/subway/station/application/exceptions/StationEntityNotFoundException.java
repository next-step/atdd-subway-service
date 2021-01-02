package nextstep.subway.station.application.exceptions;

import nextstep.subway.exceptions.EntityNotFoundException;

public class StationEntityNotFoundException extends EntityNotFoundException {
    public StationEntityNotFoundException(final String message) {
        super(message);
    }
}
