package ru.denisov.itcompany.manager;

import lombok.Getter;
import ru.denisov.itcompany.processing.validator.registration.RegistrationValidator;

public class ValidationManager {
    @Getter
    private static final RegistrationValidator registrationValidator;

    static {
        registrationValidator = new RegistrationValidator(
                RepositoryManager.getEmployeeRepository(),
                CheckManager.getNameCheck(),
                CheckManager.getSurnameCheck(),
                CheckManager.getDateCheck(),
                CheckManager.getEmailCheck(),
                CheckManager.getPasswordCheck()
        );
    }
}
