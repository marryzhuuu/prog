package share.exceptions;

/**
 * Исключения для рекурсивного вызова скрипта.
 */
public class WrongPasswordException extends Exception {
    public WrongPasswordException(String s) { super(s); }
}
