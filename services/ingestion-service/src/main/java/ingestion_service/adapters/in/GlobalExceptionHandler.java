package ingestion_service.adapters.in;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ingestion_service.domain.exception.InvalidSensorReadingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSensorReadingException.class)
    public ProblemDetail handleInvalidSensorReading(InvalidSensorReadingException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY,
                exception.getMessage());
        problemDetail.setTitle("Invalid sensor reading");
        problemDetail.setType(URI.create("/problems/invalid-sensor-reading"));
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException exception) {
        String detail = exception.getBindingResult().getFieldErrors().stream().findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Request validation failed");
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        problemDetail.setTitle("Invalid request");
        return problemDetail;
    }
}
