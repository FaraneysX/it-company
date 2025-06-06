<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Регистрация</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background: linear-gradient(135deg, #34495E 0%, #2C3E50 100%);
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            color: #ECF0F1;
            overflow: hidden;
        }

        .container {
            background: rgba(236, 240, 241, 0.95);
            padding: 40px 30px;
            border-radius: 15px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
            width: 100%;
            max-width: 400px;
            text-align: center;
            color: #2C3E50;
            animation: fadeIn 1s ease-in-out;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(-20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .error-message {
            color: #D8000C;
            background-color: #FFD2D2;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-weight: bold;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            text-align: left;
        }

        h2 {
            margin-bottom: 20px;
            color: #2C3E50;
        }

        form {
            display: flex;
            flex-direction: column;
            width: 100%;
        }

        label {
            margin-bottom: 5px;
            font-weight: bold;
            text-align: left;
            display: block;
        }

        input[type="text"], input[type="password"], input[type="tel"], input[type="email"], input[type="date"], input[type="submit"] {
            padding: 12px;
            width: 100%;
            margin-bottom: 20px;
            border: 1px solid #BDC3C7;
            border-radius: 5px;
            box-sizing: border-box;
            font-size: 16px;
            transition: border-color 0.3s, box-shadow 0.3s;
        }

        input[type="text"]:focus, input[type="password"]:focus, input[type="tel"]:focus, input[type="email"]:focus, input[type="date"]:focus {
            border-color: #5C7AEA;
            outline: none;
            box-shadow: 0 0 5px rgba(92, 122, 234, 0.5);
        }

        input[type="submit"] {
            background: linear-gradient(135deg, #5C7AEA 0%, #3f5bcc 100%);
            color: white;
            border: none;
            cursor: pointer;
            transition: background 0.3s, transform 0.3s, box-shadow 0.3s;
        }

        input[type="submit"]:hover {
            background: linear-gradient(135deg, #3f5bcc 0%, #5C7AEA 100%);
            transform: scale(1.02);
            box-shadow: 0 4px 8px rgba(92, 122, 234, 0.3);
        }

        input[type="submit"]:active {
            transform: scale(0.98);
        }

        button {
            padding: 10px 20px;
            background: linear-gradient(135deg, #5C7AEA 0%, #3f5bcc 100%);
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background 0.3s, transform 0.3s, box-shadow 0.3s;
            width: 100%;
        }

        button:hover {
            background: linear-gradient(135deg, #3f5bcc 0%, #5C7AEA 100%);
            transform: scale(1.02);
            box-shadow: 0 4px 8px rgba(92, 122, 234, 0.3);
        }

        button:active {
            transform: scale(0.98);
        }

        .buttons {
            display: flex;
            justify-content: center;
        }

        .buttons a {
            text-decoration: none;
            width: 100%;
        }

        .buttons a button {
            width: 100%;
        }

        @media (max-width: 600px) {
            .container {
                padding: 20px;
            }
        }
    </style>
</head>
<body>

<c:if test="${not empty requestScope.errors}">
    <div class="error-message container">
        <c:forEach var="error" items="${requestScope.errors}">
            <div><span>${error.field}: </span><span>${error.type.toString()}</span></div>
        </c:forEach>
    </div>
</c:if>

<div class="container">
    <form method="post" action="<c:url value='/registration'/>">
        <h2>Регистрация</h2>
        <div class="form-group">
            <label for="name">Имя</label>
            <input type="text" placeholder="Иван" id="name" name="name" required>
        </div>
        <div class="form-group">
            <label for="surname">Фамилия</label>
            <input type="text" placeholder="Иванов" id="surname" name="surname" required>
        </div>
        <div class="form-group">
            <label for="birthDate">Дата рождения</label>
            <input type="date" id="birthDate" name="birthDate" required>
        </div>
        <div class="form-group">
            <label for="email">Электронная почта</label>
            <input type="email" placeholder="my@email.ru" id="email" name="email" required>
        </div>
        <div class="form-group">
            <label for="password">Пароль</label>
            <input type="password" placeholder="Пароль" id="password" name="password" required>
        </div>
        <div class="form-group">
            <input type="submit" value="Зарегистрироваться">
        </div>
        <div class="buttons">
            <a href="<c:url value='/login'/>">
                <button type="button">Есть аккаунт?</button>
            </a>
        </div>
    </form>
</div>

</body>
</html>
