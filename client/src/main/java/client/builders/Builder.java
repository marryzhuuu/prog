package client.builders;

/**
 * Абстрактный класс для ввода пользовательских данных.
 * @param <T> создаваемый объект
 */
public abstract class Builder<T> {
    public abstract T build();
}
