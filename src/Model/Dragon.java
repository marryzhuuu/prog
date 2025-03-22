package Model;

public class Dragon {
    private static long nextId=1;

    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer age; //Значение поля должно быть больше 0, Поле не может быть null
    private String description; //Поле не может быть null
    private Color color; //Поле не может быть null
    private DragonCharacter character; //Поле не может быть null
    private DragonCave cave; //Поле не может быть null

    public Dragon(String name, Coordinates coordinates, Integer age, String description, Color color, DragonCharacter character, DragonCave cave) {
        this.id = nextId++;
        this.name = name;
        this.coordinates = coordinates;
        this.age = age;
        this.description = description;
        this.color = color;
        this.character = character;
        this.cave = cave;
    }

    public Dragon() {}
}



