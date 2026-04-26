package exceptions;

public class UserNotFoundException extends Exception{
	
	private String login;
		
		public UserNotFoundException(String login) {
			this.login = login;
		}
		
		@Override
		public String getMessage(){
	        return "El " + login + " no existe.";
	    }
}
