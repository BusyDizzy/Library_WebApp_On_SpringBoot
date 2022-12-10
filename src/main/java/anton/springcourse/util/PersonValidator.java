package anton.springcourse.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import anton.springcourse.models.Person;
import anton.springcourse.repositories.PeopleRepository;


@Component
public class PersonValidator implements Validator {

    private final PeopleRepository peopleRepository;

    public PersonValidator(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Person person = (Person) target;
        if (peopleRepository.findByName(person.getName()).isPresent())
            errors.rejectValue("name","", "Человек с таким ФИО уже существует");

    }
}
