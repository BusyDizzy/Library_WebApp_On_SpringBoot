package anton.springcourse.services;

import anton.springcourse.models.Book;
import anton.springcourse.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import anton.springcourse.models.Person;
import anton.springcourse.repositories.PeopleRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Anton Tkatch
 */

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(PeopleRepository peopleRepository, BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(Pageable pageable){
        return booksRepository.findAll(pageable).getContent();
    }

    public List<Book> searchByTitle(String name){
        return booksRepository.findByTitleStartingWith(name);
    }

    public Book findOne(int id){
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }
    @Transactional
    public void save(Book book){
       booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook){
        Book bookToBeUpdated = booksRepository.findById(id).get();

        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());
        booksRepository.save(updatedBook);
    }


    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }


    public Optional<Person> getBookOwner(int book_id){
       Optional<Book> book = booksRepository.findById(book_id);

       if (book.get().getOwner() !=null){
             return booksRepository.findById(book_id).map(Book::getOwner);
       }
       else
           return Optional.ofNullable(book.get().getOwner());

    }

    /**
     * Метод необходимый для назначения книги определенному человеку
     */
    @Transactional
    public void setReader(int person_id, int book_id) {
        Optional<Book> foundBook = booksRepository.findById(book_id);
        if (foundBook.isPresent()) {
            foundBook.get().setBookedAt(new Date());
            foundBook.get().setOwner(peopleRepository.findById(person_id).get());
        }

    }

    /**
     * Метод необходимый для освобождения книги, отвязки от человека
     */
    @Transactional
    public void removeReader(int book_id) {
        booksRepository.findById(book_id).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setBookedAt(null);

        });
    }

}
