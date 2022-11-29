package nextstep.subway.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entity, Long id) {
        super(String.format("해당 엔티티 데이터를 찾을 수 없습니다. Entity 이름 [%s], 아이디 [%s]", entity, id));
    }
}
