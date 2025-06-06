<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Вход</title>
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

        input[type="email"], input[type="password"] {
            padding: 12px;
            width: 100%;
            margin-bottom: 20px;
            border: 1px solid #BDC3C7;
            border-radius: 5px;
            box-sizing: border-box;
            font-size: 16px;
            transition: border-color 0.3s, box-shadow 0.3s;
        }

        input[type="email"]:focus, input[type="password"]:focus {
            border-color: #5C7AEA;
            outline: none;
            box-shadow: 0 0 5px rgba(92, 122, 234, 0.5);
        }

        input[type="submit"] {
            padding: 14px;
            width: 100%;
            border: none;
            border-radius: 5px;
            background: linear-gradient(135deg, #5C7AEA 0%, #3f5bcc 100%);
            color: white;
            font-size: 16px;
            font-weight: bold;
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

        a {
            text-decoration: none;
            color: #5C7AEA;
            display: inline-block;
            margin-top: 20px;
        }

        a:hover {
            text-decoration: underline;
        }

        .form-group {
            text-align: left;
            margin-bottom: 15px;
        }

        @media (max-width: 600px) {
            .container {
                padding: 20px;
            }
        }
    </style>
</head>
<body>

<div class="container">
    <c:if test="${not empty requestScope.error}">
        <div class="error-message">
            <span>${requestScope.error.toString()}</span>
        </div>
    </c:if>

    <form method="post" action="<c:url value='/login'/>">
        <h2>Вход</h2>
        <div class="form-group">
            <label for="email">Электронная почта</label>
            <input type="email" placeholder="my@email.ru" id="email" name="email" required>
        </div>
        <div class="form-group">
            <label for="password">Пароль</label>
            <input type="password" placeholder="Пароль" id="password" name="password" required>
        </div>
        <div>
            <input type="submit" value="Войти">
        </div>
        <a href="<c:url value='/registration'/>">Создать аккаунт</a>
    </form>
</div>

</body>
</html>
