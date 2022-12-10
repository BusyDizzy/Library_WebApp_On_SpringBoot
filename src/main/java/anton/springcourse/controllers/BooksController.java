package anton.springcourse.controllers;

import anton.springcourse.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import anton.springcourse.models.Book;
import anton.springcourse.models.Person;
import anton.springcourse.services.BooksService;


import javax.validation.Valid;
import java.util.Optional;

/**
 * @author Anton Tkatch
 */
@Controller
@RequestMapping("/books")
public class BooksController {

    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public BooksController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }


    @GetMapping()
    public String index(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                        @RequestParam(value = "books_per_page", required = false, defaultValue = "5") Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false, defaultValue = "false") Boolean sortByYear,
                        Model model) {

        Pageable pageable =null;
        if (sortByYear) {
             pageable = PageRequest.of(page, booksPerPage, Sort.by("publishedDate"));
        }
        else
             pageable = PageRequest.of(page, booksPerPage);

        model.addAttribute("books", booksService.findAll(pageable));

        return "books/index";
    }

    @GetMapping("/{book_id}")
    public String show(@PathVariable("book_id") int book_id, @ModelAttribute("person") Person person, Model model) {

        model.addAttribute("book", booksService.findOne(book_id));

        Optional<Person> bookOwner = booksService.getBookOwner(book_id);

        if (bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @PatchMapping("/add/{book_id}")
    public String setReader(@ModelAttribute("person") Person person, @PathVariable("book_id") int book_id){

        booksService.setReader(person.getId(), book_id);
        return "redirect:/books/" + book_id;
    }

    @PatchMapping("/remove/{book_id}")
    public String removeReader(@ModelAttribute("person") Person person, @PathVariable("book_id") int book_id){

        booksService.removeReader(book_id);
        return "redirect:/books/" + book_id;
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{book_id}/edit")
    public String edit(Model model, @PathVariable("book_id") int book_id) {
        model.addAttribute("book", booksService.findOne(book_id));
        return "books/edit";
    }

    @PatchMapping("/{book_id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("book_id") int book_id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(book_id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{book_id}")
    public String delete(@PathVariable("book_id") int book_id) {
        booksService.delete(book_id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String newQuery(){
        return "books/search";
    }

    @PostMapping("/search")
    public String search(@RequestParam(value = "search_request", required = false) String searchLine, Model model){

        model.addAttribute("books", booksService.searchByTitle(searchLine));

        return "books/search";
    }

}
