package Model;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс FileManager отвечает за чтение и запись коллекции в файл.
 */
public class FileManager {
    public DragonCollection loadFromFile(String filename) {
        // ToDo: Реализация чтения из файла с использованием FileReader

        return new DragonCollection();
    }

    /**
     * Сохраняет коллекцию драконов в файл в формате JSON.
     *
     * @param filename   Имя файла, в который будут сохранены данные.
     * @param collection Коллекция драконов.
     * @throws IOException Если произошла ошибка при записи в файл.
     */
    public void saveToFile(String filename, DragonCollection collection) throws IOException {
        // Формируем JSON-строку вручную
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("["); // Начало JSON-массива

        for (int i = 1; i <= collection.size(); i++) {
            Dragon dragon = collection.findDragonById(i);
            jsonBuilder.append(toJson(dragon)); // Преобразуем дракона в JSON

            if (i < collection.size() - 1) {
                jsonBuilder.append(","); // Добавляем запятую между элементами массива
            }
        }

        jsonBuilder.append("]"); // Конец JSON-массива

        // Записываем JSON в файл с помощью BufferedOutputStream
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filename))) {
            outputStream.write(jsonBuilder.toString().getBytes()); // Преобразуем строку в байты и записываем
        }

    }

    /**
     * Преобразует объект Dragon в JSON-строку.
     *
     * @param dragon Объект Dragon.
     * @return JSON-строка, представляющая объект Dragon.
     */
    private String toJson(Dragon dragon) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        return String.format(
                "{" +
                        "\"id\": %d, " +
                        "\"name\": \"%s\", " +
                        "\"coordinates\": {\"x\": %s, \"y\": %s}, " +
                        "\"creationDate\": \"%s\", " +
                        "\"age\": %d, " +
                        "\"description\": \"%s\", " +
                        "\"color\": \"%s\", " +
                        "\"character\": \"%s\", " +
                        "\"cave\": {\"depth\": %d}" +
                        "}",
                dragon.getId(),
                escapeJson(dragon.getName()), // Экранируем строки
                String.format("%.2f", dragon.getCoordinates().getX()).replace(",", "."),
                String.format("%.2f", dragon.getCoordinates().getY()).replace(",", "."),
                dateFormat.format(dragon.getCreationDate()),
                dragon.getAge(),
                escapeJson(dragon.getDescription()), // Экранируем строки
                dragon.getColor(),
                dragon.getCharacter(),
                dragon.getCave().getDepth()
        );
    }

    /**
     * Экранирует специальные символы в строке для корректного формирования JSON.
     *
     * @param input Входная строка.
     * @return Экранированная строка.
     */
    private String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\") // Экранируем обратный слэш
                .replace("\"", "\\\"") // Экранируем кавычки
                .replace("\b", "\\b") // Экранируем backspace
                .replace("\f", "\\f") // Экранируем form feed
                .replace("\n", "\\n") // Экранируем новую строку
                .replace("\r", "\\r") // Экранируем carriage return
                .replace("\t", "\\t"); // Экранируем табуляцию
    }
}