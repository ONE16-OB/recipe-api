package be.one16.cerbainterview.core.exceptions;

public class RecipeNotFoundException extends RuntimeException{
    public RecipeNotFoundException(String message) {
        super(message);
    }
}
