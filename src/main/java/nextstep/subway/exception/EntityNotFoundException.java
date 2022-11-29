package nextstep.subway.exception;

import nextstep.subway.ErrorMessage;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entity, Long id) {
        super(ErrorMessage.notFoundEntity(entity, id));
    }
}
