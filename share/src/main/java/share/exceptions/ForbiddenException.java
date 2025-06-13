package share.exceptions;

public class ForbiddenException extends Throwable {
    public ForbiddenException() {
        super("Нет прав доступа!");
    }
}
