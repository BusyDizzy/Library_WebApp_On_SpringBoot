package anton.springcourse.models;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author Anton Tkatch
 */
@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "book_id")
    // Говорит Hibernate о том что поле генерирутеся на стороне постгреса
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name ="person_id", referencedColumnName = "person_id", columnDefinition="integer")
    private Person owner;

    @NotEmpty (message="Поле не может быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2х до 100 символов")
    @Column(name="book_title")
    private String title;

    @NotEmpty (message="Поле не может быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть от 2х до 30 символов")
    @Column(name = "author_name")
    private String author;
    @NotNull (message="Поле не может быть пустым")
    @Range(min = 1500, max =2022 , message = "Год издания должен быть в диапазоне от 1500 до 2022")
    @Column(name = "published_date")
    private int publishedDate;

    @Column(name ="booked_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookedAt;

    @Transient
    private Boolean expired;

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }
    public Book (){

    }

    public Book(String title, String author, int publishedDate) {
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;

    }

    public void setBookedAt(Date bookedAt) {
        this.bookedAt = bookedAt;
    }

    public Date getBookedAt() {
        return bookedAt;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String book_title) {
        this.title = book_title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author_name) {
        this.author = author_name;
    }

    public int getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(int publishedDate) {
        this.publishedDate = publishedDate;
    }

}
