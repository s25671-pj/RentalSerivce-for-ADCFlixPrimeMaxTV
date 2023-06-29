package pl.pjatk.RentalService.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.net.ConnectException;

@RestControllerAdvice
public class RentalRTE {
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<Void> handleNotFound(HttpClientErrorException.NotFound ex){
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<Void> handleBadRequest(HttpClientErrorException.BadRequest ex){
        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<Void> handleInternalServerError(HttpServerErrorException.InternalServerError ex){
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<Void> handleConnectException(ConnectException ex){
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
    }
}