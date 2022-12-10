package anton.springcourse.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import anton.springcourse.models.Book;
import anton.springcourse.models.Person;
import anton.springcourse.repositories.BooksRepository;
import anton.springcourse.repositories.PeopleRepository;
import java.util.*;

/**
 * @author Anton Tkatch
 */
@Service
@Transactional(readOnly = true)
public class PeopleService {

    private  final static Integer BOOK_EXPIRATION_DAYS = 10;

    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
    }

    public List<Person> findAll (){
        return peopleRepository.findAll();
    }


    public Person findOne(int id){
        Optional<Person> foundPerson = peopleRepository.findById(id);

        return foundPerson.orElse(null);
    }

    Optional<Person> findByName(String fullName){
        return peopleRepository.findByName(fullName);
    }

    /**
     * Возвращаем книги определенного человека: при этом если время, когда он взял книгу
     * больше определенного переменной BOOK_EXPIRATION_DAYS у книги выставляется флаг expired
     */
    public List<Book>  getPersonBooks(int id){

        Person person = findOne(id);
        if (person !=null) {
            person.getBooks().forEach(book -> {
                long diffInMillies = Math.abs(book.getBookedAt().getTime() - new Date().getTime());

                if (diffInMillies > BOOK_EXPIRATION_DAYS * 60 * 60 * 24 * 1000) {
                    book.setExpired(true);
                }
            });
        }

        return person.getBooks();
    }

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete (int id){
        peopleRepository.deleteById(id);
    }

}
