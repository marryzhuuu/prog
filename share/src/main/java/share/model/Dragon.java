package share.model;
import java.io.Serializable;
import java.util.Date;

public class Dragon implements Serializable {
    private static int nextId=1;

    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
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
        this.creationDate = new Date();
    }

    public Dragon(int id, String name, Coordinates coordinates, Date date, Integer age, String description, Color color, DragonCharacter character, DragonCave cave) {
        this.id = id;
        nextId = Math.max(nextId, id+1);
        this.name = name;
        this.coordinates = coordinates;
        this.age = age;
        this.description = description;
        this.color = color;
        this.character = character;
        this.cave = cave;
        this.creationDate = date;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public java.util.Date getCreationDate() {
        return creationDate;
    }

    public Integer getAge() {
        return age;
    }

    public String getDescription() {
        return description;
    }

    public Color getColor() {
        return color;
    }

    public DragonCharacter getCharacter() {
        return character;
    }

    public DragonCave getCave() {
        return cave;
    }

    public Dragon updateFields(Dragon updated) {
        name = updated.name;
        coordinates = updated.coordinates;
        age = updated.age;
        description = updated.description;
        color = updated.color;
        character = updated.character;
        cave = updated.cave;

        return this;
    }

    public String toString() {
        return String.format(
                """
                 
                 id: %d,
                 name: %s,
                 coordinates: %s,
                 creationDate: %s,
                 age: %d,
                 description: %s,
                 color: %s,
                 character: %s,
                 cave: %s""",
                id,
                name, // Экранируем строки
                coordinates,
                creationDate,
                age,
                description, // Экранируем строки
                color,
                character,
                cave
        );

    }
}



