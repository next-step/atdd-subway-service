package nextstep.subway.line.application;


public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 366181682649818876L;

    public NotFoundException(Long id) {
        super(String.format("Not found with given id %d", id));
    }
}
