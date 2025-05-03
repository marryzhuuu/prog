package server.collection;
import share.exceptions.SaveToFileException;
import share.model.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Класс FileManager отвечает за чтение и запись коллекции в файл.
 */
public class FileManager {
    private final String filename;

    public FileManager() {
        String filename = System.getenv("DRAGON_FILE");

        if (filename == null || filename.isEmpty()) {
            filename = "dragon_collection.json";
        }

        this.filename = filename;
    }

    public DragonCollection loadFromFile() {
        try {
            // Читаем JSON из файла
            String json = readFile(filename);

            // Разбираем JSON вручную
            return parseJson(json);

        }
        catch (Exception e) {
            return new DragonCollection();
        }
    }

    /**
     * Читает содержимое файла в строку.
     *
     * @return Содержимое файла в виде строки.
     * @throws IOException Если произошла ошибка при чтении файла.
     */
    private String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        try (FileReader reader = new FileReader(filename)) {
            int character;
            while ((character = reader.read()) != -1) {
                content.append((char) character);
            }
        }
        return content.toString();
    }

    /**
     * Разбирает JSON-строку в коллекцию объектов Dragon.
     *
     * @param json JSON-строка.
     * @return Коллекция объектов Dragon.
     * @throws ParseException Если произошла ошибка при разборе JSON.
     */
    private DragonCollection parseJson(String json) throws ParseException {
        DragonCollection dragons = new DragonCollection();
        json = json.trim();

        // Убедимся, что JSON начинается с '[' и заканчивается ']'
        if (!json.startsWith("[") || !json.endsWith("]")) {
            throw new ParseException("Некорректный формат JSON: ожидается массив", 0);
        }

        // Удаляем внешние квадратные скобки
        json = json.substring(1, json.length() - 1).trim();

        // Разделяем объекты в массиве
        String[] objects = splitJsonObjects(json);

        // Обрабатываем каждый объект
        for (String obj : objects) {
            dragons.addDragon(parseDragon(obj));
        }

        return dragons;
    }

    /**
     * Разделяет JSON-массив на отдельные объекты.
     *
     * @param json JSON-строка, представляющая массив.
     * @return Массив строк, каждая из которых представляет объект.
     */
    private String[] splitJsonObjects(String json) {
        List<String> objects = new ArrayList<>();
        int braceCount = 0;
        int start = 0;

        for (int i = 0; i < json.length(); i++) {
            char ch = json.charAt(i);
            if (ch == '{') {
                braceCount++;
            } else if (ch == '}') {
                braceCount--;
            } else if (ch == ',' && braceCount == 0) {
                objects.add(json.substring(start, i).trim());
                start = i + 1;
            }
        }

        if (start < json.length()) {
            objects.add(json.substring(start).trim());
        }

        return objects.toArray(new String[0]);
    }

    /**
     * Разбирает JSON-объект в объект Dragon.
     *
     * @param json JSON-строка, представляющая объект Dragon.
     * @return Объект Dragon.
     * @throws ParseException Если произошла ошибка при разборе JSON.
     */
    private Dragon parseDragon(String json) throws ParseException {
        Map<String, String> fields = parseJsonObject(json);

        int id = Integer.parseInt(fields.get("id"));
        String name = fields.get("name").replace("\"", "");
        Coordinates coordinates = parseCoordinates(fields.get("coordinates"));
        Date creationDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(fields.get("creationDate").replace("\"", ""));
        int age = Integer.parseInt(fields.get("age"));
        String description = fields.get("description").replace("\"", "");
        Color color = Color.valueOf(fields.get("color").replace("\"", ""));
        DragonCharacter character = DragonCharacter.valueOf(fields.get("character").replace("\"", ""));
        DragonCave cave = parseDragonCave(fields.get("cave"));

        return new Dragon(id, name, coordinates, creationDate, age, description, color, character, cave);
    }

    /**
     * Разбирает JSON-объект в карту ключ-значение.
     *
     * @param json JSON-строка, представляющая объект.
     * @return Карта ключ-значение.
     * @throws ParseException Если произошла ошибка при разборе JSON.
     */
    private Map<String, String> parseJsonObject(String json) throws ParseException {
        Map<String, String> fields = new HashMap<>();
        json = json.trim();

        // Убедимся, что JSON начинается с '{' и заканчивается '}'
        if (!json.startsWith("{") || !json.endsWith("}")) {
            throw new ParseException("Некорректный формат JSON: ожидается объект", 0);
        }

        // Удаляем внешние фигурные скобки
        json = json.substring(1, json.length() - 1).trim();

        // Разделяем пары ключ-значение
        String[] pairs = splitJsonPairs(json);

        // Обрабатываем каждую пару
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length != 2) {
                throw new ParseException("Некорректный формат JSON: ожидается пара ключ-значение", 0);
            }

            String key = keyValue[0].trim().replace("\"", "");
            String value = keyValue[1].trim();

            fields.put(key, value);
        }

        return fields;
    }

    /**
     * Разделяет JSON-объект на пары ключ-значение.
     *
     * @param json JSON-строка, представляющая объект.
     * @return Массив строк, каждая из которых представляет пару ключ-значение.
     */
    private String[] splitJsonPairs(String json) {
        List<String> pairs = new ArrayList<>();
        int braceCount = 0;
        int start = 0;

        for (int i = 0; i < json.length(); i++) {
            char ch = json.charAt(i);
            if (ch == '{' || ch == '[') {
                braceCount++;
            } else if (ch == '}' || ch == ']') {
                braceCount--;
            } else if (ch == ',' && braceCount == 0) {
                pairs.add(json.substring(start, i).trim());
                start = i + 1;
            }
        }

        if (start < json.length()) {
            pairs.add(json.substring(start).trim());
        }

        return pairs.toArray(new String[0]);
    }

    /**
     * Разбирает JSON-строку, представляющую координаты.
     *
     * @param json JSON-строка, представляющая координаты.
     * @return Объект Coordinates.
     * @throws ParseException Если произошла ошибка при разборе JSON.
     */
    private Coordinates parseCoordinates(String json) throws ParseException {
        Map<String, String> fields = parseJsonObject(json);
        float x = Float.parseFloat(fields.get("x"));
        float y = Float.parseFloat(fields.get("y"));
        return new Coordinates(x, y);
    }

    /**
     * Разбирает JSON-строку, представляющую пещеру дракона.
     *
     * @param json JSON-строка, представляющая пещеру.
     * @return Объект DragonCave.
     * @throws ParseException Если произошла ошибка при разборе JSON.
     */
    private DragonCave parseDragonCave(String json) throws ParseException {
        Map<String, String> fields = parseJsonObject(json);
        int depth = Integer.parseInt(fields.get("depth"));
        Long treasures = Long.parseLong(fields.get("treasures"));
        return new DragonCave(depth, treasures);
    }

    /**
     * Сохраняет коллекцию драконов в файл в формате JSON.
     *
     * @param collection Коллекция драконов.
     */
    public void saveToFile(DragonCollection collection) throws SaveToFileException {
        // Формируем JSON-строку вручную
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("["); // Начало JSON-массива

        int i = 0;
        for (Dragon dragon: collection.getDragons()) {
            jsonBuilder.append(toJson(dragon)); // Преобразуем дракона в JSON

            if (i++ <= collection.size()) {
                jsonBuilder.append(","); // Добавляем запятую между элементами массива
            }
        }

       jsonBuilder.append("]"); // Конец JSON-массива

        // Записываем JSON в файл с помощью BufferedOutputStream
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filename))) {
            outputStream.write(jsonBuilder.toString().getBytes()); // Преобразуем строку в байты и записываем
        }  catch (IOException e) {
            throw new SaveToFileException();
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
                        "\"cave\": {\"depth\": %d, \"treasures\": %d}" +
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
                dragon.getCave().getDepth(),
                dragon.getCave().getTreasures()
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