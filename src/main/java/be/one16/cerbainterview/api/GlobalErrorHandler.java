package be.one16.cerbainterview.api;

import be.one16.cerbainterview.core.exceptions.IngredientNotFoundException;
import be.one16.cerbainterview.core.exceptions.RecipeException;
import be.one16.cerbainterview.core.exceptions.RecipeNotFoundException;
import be.one16.cerbainterview.model.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String fieldName = fieldError.getField();
        Object rejectedValue = fieldError.getRejectedValue();
        String errorMessage = String.format("Invalid value '%s' for field '%s'. Field must not be null.", rejectedValue, fieldName);
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(errorMessage);
        errorDTO.setErrorCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format("Invalid type for parameter '%s'. " , ex.getName());
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(errorMessage);
        errorDTO.setErrorCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    @ExceptionHandler({IngredientNotFoundException.class, RecipeNotFoundException.class, RecipeException.class})
    public ResponseEntity<ErrorDTO> handleResourceNotFoundException(IngredientNotFoundException ex) {
        ErrorDTO error = new ErrorDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
