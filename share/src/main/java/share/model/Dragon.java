package share.model;
import java.io.Serializable;
import java.time.LocalDate;

public class Dragon implements Serializable, Comparable<Dragon> {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private final LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer age; //Значение поля должно быть больше 0, Поле не может быть null
    private String description; //Поле не может быть null
    private Color color; //Поле не может быть null
    private DragonCharacter character; //Поле не может быть null
    private DragonCave cave; //Поле не может быть null
    private int creatorId;

    public Dragon(int id, String name, Coordinates coordinates, Integer age, String description, Color color, DragonCharacter character, DragonCave cave, LocalDate creationDate) {
        this(id, name, coordinates, age, description, color, character, cave, creationDate, 0);
    }

    public Dragon(int id, String name, Coordinates coordinates, Integer age, String description, Color color, DragonCharacter character, DragonCave cave, LocalDate creationDate, int creatorId) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.age = age;
        this.description = description;
        this.color = color;
        this.character = character;
        this.cave = cave;
        this.creationDate = creationDate;
        this.creatorId = creatorId;
    }

    public Dragon copy(int id) {
        return new Dragon(id, this.name, this.coordinates, this.age,
                this.description, this.color, this.character, this.cave, this.creationDate
        );
    }

    public Dragon copy(int id, int creatorId) {
        return new Dragon(id, this.name, this.coordinates, this.age,
                this.description, this.color, this.character, this.cave, this.creationDate, this.creatorId
        );
    }
    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id;}

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDate getCreationDate() {
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

    public DragonCharacter getTemper() {
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

    /**
     * Необходим для Comparable
     * Сортировка по умолчанию - по id
     */
    @Override
    public int compareTo(Dragon d) {
        return (this.id - d.getId());
    }
}



