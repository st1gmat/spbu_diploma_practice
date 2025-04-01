// package com.diploma.customer_service.handlers;

// import java.util.HashMap;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.FieldError;
// import org.springframework.web.ErrorResponse;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;

// import com.diploma.customer_service.excpetions.CustomerNotFoundException;

// import lombok.var;

// @RestControllerAdvice
// public class GlobalExceptionHandler {
//     @ExceptionHandler(CustomerNotFoundException.class)
//     public ResponseEntity<String> handle(CustomerNotFoundException err) {
//         return ResponseEntity
//                 .status(HttpStatus.NOT_FOUND)
//                 .body(err.getMessage());
//     }

//     @ExceptionHandler(MethodArgumentNotValidException.class)
//     public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException err) {
//         var errors = new HashMap<String, String>();
//         err.getBindingResult().getAllErrors().forEach(error -> {
//             var fieldName = ((FieldError)error).getField();
//             var errorMessage = error.getDefaultMessage();
//         });
//         return ResponseEntity
//                 .status(HttpStatus.BAD_REQUEST)
//                 .body(new ErrorResponse(errors));
//     }
// }
