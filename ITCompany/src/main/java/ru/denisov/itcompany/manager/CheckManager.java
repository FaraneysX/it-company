package ru.denisov.itcompany.manager;

import lombok.Getter;
import ru.denisov.itcompany.processing.check.DateCheck;
import ru.denisov.itcompany.processing.check.EmailCheck;
import ru.denisov.itcompany.processing.check.NameCheck;
import ru.denisov.itcompany.processing.check.PasswordCheck;
import ru.denisov.itcompany.processing.check.SurnameCheck;

public class CheckManager {
    @Getter
    private static final NameCheck nameCheck;

    @Getter
    private static final SurnameCheck surnameCheck;

    @Getter
    private static final DateCheck dateCheck;

    @Getter
    private static final EmailCheck emailCheck;

    @Getter
    private static final PasswordCheck passwordCheck;

    static {
        nameCheck = new NameCheck();
        surnameCheck = new SurnameCheck();
        dateCheck = new DateCheck();
        emailCheck = new EmailCheck();
        passwordCheck = new PasswordCheck();
    }
}
