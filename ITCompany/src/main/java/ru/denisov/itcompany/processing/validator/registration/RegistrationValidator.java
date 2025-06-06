package ru.denisov.itcompany.processing.validator.registration;

import ru.denisov.itcompany.dto.employee.view.EmployeeRegistrationViewDto;
import ru.denisov.itcompany.processing.check.DateCheck;
import ru.denisov.itcompany.processing.check.EmailCheck;
import ru.denisov.itcompany.processing.check.NameCheck;
import ru.denisov.itcompany.processing.check.PasswordCheck;
import ru.denisov.itcompany.processing.check.SurnameCheck;
import ru.denisov.itcompany.processing.validator.load.LoadError;
import ru.denisov.itcompany.processing.validator.load.LoadValidationResult;
import ru.denisov.itcompany.processing.validator.load.TypeLoadError;
import ru.denisov.itcompany.repository.EmployeeRepository;

public class RegistrationValidator {
    private final EmployeeRepository repository;
    private final NameCheck nameCheck;
    private final SurnameCheck surnameCheck;
    private final DateCheck dateCheck;
    private final EmailCheck emailCheck;
    private final PasswordCheck passwordCheck;

    public RegistrationValidator(EmployeeRepository employeeRepository,
                                 NameCheck nameCheck,
                                 SurnameCheck surnameCheck,
                                 DateCheck dateCheck,
                                 EmailCheck emailCheck,
                                 PasswordCheck passwordCheck) {
        this.repository = employeeRepository;
        this.nameCheck = nameCheck;
        this.surnameCheck = surnameCheck;
        this.dateCheck = dateCheck;
        this.emailCheck = emailCheck;
        this.passwordCheck = passwordCheck;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private LoadError checkName(EmployeeRegistrationViewDto dto) {
        String name = dto.name();
        String field = "name";

        if (isEmpty(name)) {
            return LoadError.of(field, TypeLoadError.EMPTY);
        }

        if (!nameCheck.isCorrect(name)) {
            return LoadError.of(field, TypeLoadError.INCORRECT);
        }

        return null;
    }

    private LoadError checkSurname(EmployeeRegistrationViewDto dto) {
        String surname = dto.surname();
        String field = "surname";

        if (isEmpty(surname)) {
            return LoadError.of(field, TypeLoadError.EMPTY);
        }

        if (!surnameCheck.isCorrect(surname)) {
            return LoadError.of(field, TypeLoadError.INCORRECT);
        }

        return null;
    }

    private LoadError checkDate(EmployeeRegistrationViewDto dto) {
        String date = dto.birthDate();
        String field = "date";

        if (isEmpty(date)) {
            return LoadError.of(field, TypeLoadError.EMPTY);
        }

        if (!dateCheck.isCorrect(date)) {
            return LoadError.of(field, TypeLoadError.INCORRECT);
        }

        return null;
    }

    private LoadError checkEmail(EmployeeRegistrationViewDto dto) {
        String email = dto.email();
        String field = "email";

        if (isEmpty(email)) {
            return LoadError.of(field, TypeLoadError.EMPTY);
        }

        if (!emailCheck.isCorrect(email)) {
            return LoadError.of(field, TypeLoadError.INCORRECT);
        }

        if (repository.existsByEmail(email)) {
            return LoadError.of(field, TypeLoadError.NON_UNIQUE);
        }

        return null;
    }

    private LoadError checkPassword(EmployeeRegistrationViewDto dto) {
        String password = dto.password();
        String field = "password";

        if (isEmpty(password)) {
            return LoadError.of(field, TypeLoadError.EMPTY);
        }

        if (!passwordCheck.isCorrect(password)) {
            return LoadError.of(field, TypeLoadError.INCORRECT);
        }

        return null;
    }

    public LoadValidationResult validate(EmployeeRegistrationViewDto registrationViewDto) {
        LoadValidationResult validationResult = new LoadValidationResult();

        validationResult.add(checkName(registrationViewDto));
        validationResult.add(checkSurname(registrationViewDto));
        validationResult.add(checkDate(registrationViewDto));
        validationResult.add(checkEmail(registrationViewDto));
        validationResult.add(checkPassword(registrationViewDto));

        return validationResult;
    }
}
