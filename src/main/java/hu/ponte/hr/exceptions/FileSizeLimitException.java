package hu.ponte.hr.exceptions;

public class FileSizeLimitException extends RuntimeException{
    public FileSizeLimitException(String message) {
        super(message);
    }
}
