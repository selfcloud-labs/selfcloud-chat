package pl.selfcloud.chat.domain.model.exception;

public class CustomerNotFoundException extends RuntimeException{

  public CustomerNotFoundException(String email){
    super("Customer with email " + email + " not found");
  }

}
