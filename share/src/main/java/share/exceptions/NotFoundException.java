package share.exceptions;

/**
 * Исключения для неправильного количества аргументов.
 */
public class NotFoundException extends Throwable {
    public NotFoundException() {
        super("Элемент не найден!");
    }
}
