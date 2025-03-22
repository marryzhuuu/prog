package Model;


/**
 * Класс FileManager отвечает за чтение и запись коллекции в файл.
 */
public class FileManager {
    public DragonCollection loadFromFile(String filename) {
        // ToDo: Реализация чтения из файла с использованием FileReader

        return new DragonCollection();
    }

    public void saveToFile(String filename, DragonCollection collection) {
        // ToDo: Реализация записи в файл с использованием BufferOutputStream
    }
}