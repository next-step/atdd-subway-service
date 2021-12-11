package nextstep.subway.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(DistanceTooLongException.class)
    protected ResponseEntity handleDistanceTooLongException(DistanceTooLongException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(LineNotFoundException.class)
    protected ResponseEntity handleDistanceTooLongException(LineNotFoundException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(SectionNotRemovableException.class)
    protected ResponseEntity handleSectionNotRemovableException(SectionNotRemovableException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(DuplicateSectionException.class)
    protected ResponseEntity handleDuplicationSectionException(DuplicateSectionException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(CannotSectionAddException.class)
    protected ResponseEntity handleCannotSectionAddException(CannotSectionAddException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(IllegalStationException.class)
    protected ResponseEntity handleIllegalStationException(IllegalStationException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
