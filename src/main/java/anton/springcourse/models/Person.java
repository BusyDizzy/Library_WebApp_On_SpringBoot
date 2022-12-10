package anton.springcourse.models;


import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;


/**
 * @author Anton Tkatch
 */
@Entity
@Table(name="Person")
public class Person {
    @Id
    @Column(name ="person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть от 2х до 30 символов")
    @Column(name = "person_name")
    private String name;

    //(pattern="dd.MM.yyyy", message="Введите дату рождения в формате ДД.ММ.ГГГГ")
    @NotNull(message = "Дата рождения не должны быть пустой")
    @DateTimeFormat(pattern="dd.MM.yyyy")
    @Column(name = "birth_date")
    private LocalDate birthDate;



    public Person() {

    }

    public Person(int id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    @OneToMany(mappedBy = "owner" )
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
