package server.orm;

import jakarta.persistence.*;
import share.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users", uniqueConstraints={@UniqueConstraint(columnNames={"id"})})
public class UserORM implements Serializable {
    public UserORM() {
    }

    public UserORM(User user) {
        this.id = user.getId();
        this.passwordDigest = user.getPassword();
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id", nullable=false, unique=true, length=11)
    private int id;

    @Column(name="name", length=40, unique=true, nullable=false)
    private String name;

    @Column(name="password_digest", length=64, nullable=false)
    private String passwordDigest;

    @Column(name="salt", length=10, nullable=false)
    private String salt;

//    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
//    @JoinColumn(name="creator_id")
//    private List<DragonORM> dragons = new ArrayList<>();

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

    public String getPasswordDigest() {
        return passwordDigest;
    }

    public void setPasswordDigest(String passwordDigest) {
        this.passwordDigest = passwordDigest;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

//    public List<DragonORM> getDragons() {
//        return dragons;
//    }
//
//    public void setDragons(List<DragonORM> dragons) {
//        this.dragons = dragons;
//    }

}
