package server.orm;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import share.model.Color;
import share.model.Dragon;
import share.model.DragonCharacter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity(name="dragons")
@Table(name="dragons", uniqueConstraints={@UniqueConstraint(columnNames={"id"})})
public class DragonORM implements Serializable {
  public DragonORM() {
  }

  public DragonORM(Dragon dragon) {
    this.name = dragon.getName();
    this.x = dragon.getCoordinates().getX();
    this.y = dragon.getCoordinates().getY();
    this.creationDate = dragon.getCreationDate();
    this.age = dragon.getAge();
    this.description = dragon.getDescription();
    this.color = dragon.getColor();
    this.temper = dragon.getTemper();
    this.caveDepth = dragon.getCave().getDepth();
    this.caveTreasures = dragon.getCave().getTreasures();
  }

  public void update(Dragon dragon) {
    this.name = dragon.getName();
    this.x = dragon.getCoordinates().getX();
    this.y = dragon.getCoordinates().getY();
    this.creationDate = dragon.getCreationDate();
    this.age = dragon.getAge();
    this.description = dragon.getDescription();
    this.color = dragon.getColor();
    this.temper = dragon.getTemper();
    this.caveDepth = dragon.getCave().getDepth();
    this.caveTreasures = dragon.getCave().getTreasures();
  }

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="id", nullable=false, unique=true, length=11)
  private int id;

  @NotBlank(message = "Имя не должно быть пустым.")
  @Column(name="name", nullable=false)
  private String name; // Поле не может быть null, Строка не может быть пустой

  @Column(name="x", nullable=false)
  private double x;

  @Min(value = -947, message = "Координата Y должна быть больше -948")
  @Column(name="y", nullable=false)
  private float y; //Значение поля должно быть больше -948

  @Column(name="creation_date", nullable=false)
  private LocalDate creationDate; // Поле не может быть null, Значение этого поля должно генерироваться автоматически

  @Min(value = 1, message = "Возраст должен быть больше нуля.")
  @Column(name="age", nullable=false)
  private int age; // Поле не может быть null, Значение поля должно быть больше 0

  @Column(name="description", nullable=false)
  private String description; // Поле не может быть null

  @Column(name="color", nullable=false)
  @Enumerated(EnumType.STRING)
  private Color color; // Поле не может быть null

  @Column(name="temper", nullable=false)
  @Enumerated(EnumType.STRING)
  private DragonCharacter temper; // Поле не может быть null

  @Column(name="cave_depth", nullable=false)
  private int caveDepth; // Поле не может быть null

  @Min(value = 1L, message = "Количество сокровищ должно быть больше 0")
  @Column(name="cave_treasures", nullable=false)
  private long caveTreasures; //Поле не может быть null, Значение поля должно быть больше 0


  @ManyToOne
  @JoinColumn(name="creator_id", nullable=false)
  private UserORM creator;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getX() { return x; }

  public void setX(double x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public int getAge() { return age; }

  public void setAge(int age) { this.age = age; }

  public String getDescription() { return description; }

  public void setDescription(String description) { this.description = description; }

  public Color getColor() { return color; }

  public void setColor(Color color) { this.color = color; }

  public DragonCharacter getTemper() { return temper; }

  public void setTemper(DragonCharacter temper) { this.temper = temper; }

  public int getCaveDepth() { return caveDepth; }

  public void setCaveDepth(int caveDepth) { this.caveDepth = caveDepth; }

  public long getCaveTreasures() { return caveTreasures; }

  public void setCaveTreasures(long caveTreasures) { this.caveTreasures = caveTreasures; }

  public UserORM getCreator() { return creator; }

  public void setCreator(UserORM creator) {
    this.creator = creator;
  }
}
