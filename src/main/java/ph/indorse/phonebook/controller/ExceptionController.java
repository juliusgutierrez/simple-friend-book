package ph.indorse.phonebook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ph.indorse.phonebook.exception.PhoneBookException;
import ph.indorse.phonebook.model.ResponseDTO;

@ControllerAdvice
public class ExceptionController {

  /**
   * Exception Handler for Exception
   */
  @ExceptionHandler(PhoneBookException.class)
  public ResponseEntity<ResponseDTO> handleException(PhoneBookException ex) {
    ResponseDTO responseDTO = new ResponseDTO();
    responseDTO.setMessage(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
  }

}
