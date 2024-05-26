<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Login</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .container {
            background-color: #fff;
            padding: 40px 50px;
            border-radius: 15px;
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
            max-width: 400px;
            width: 100%;
            text-align: center;
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

        h2 {
            margin-bottom: 20px;
            color: #333;
            font-size: 28px;
        }

        .error-message {
            color: #D8000C;
            background-color: #FFD2D2;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            width: 100%;
            font-weight: bold;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #555;
            text-align: left;
        }

        input[type="email"], input[type="password"] {
            padding: 12px;
            width: calc(100% - 24px);
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }

        input[type="email"]:focus, input[type="password"]:focus {
            border-color: #5C7AEA;
            outline: none;
        }

        input[type="submit"] {
            padding: 12px;
            width: 100%;
            border: none;
            border-radius: 5px;
            background: linear-gradient(135deg, #5C7AEA 0%, #3f5bcc 100%);
            color: white;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: background 0.3s, transform 0.3s;
        }

        input[type="submit"]:hover {
            background: linear-gradient(135deg, #3f5bcc 0%, #5C7AEA 100%);
            transform: scale(1.02);
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
        <h2>Введите данные для входа</h2>
        <div class="form-group">
            <label for="email">Электронная почта:</label>
            <input type="email" id="email" name="email" required>
        </div>
        <div class="form-group">
            <label for="password">Пароль:</label>
            <input type="password" placeholder="пароль" id="password" name="password" required>
        </div>
        <div>
            <input type="submit" value="Войти">
        </div>
    </form>
</div>

</body>
</html>
