<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Добавление задачи</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #483D8B;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            color: #fff;
        }

        .form-container {
            background-color: #f9f9f9;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            width: 100%;
            max-width: 400px;
            text-align: center;
        }

        h1 {
            margin-bottom: 20px;
            color: #333;
        }

        form {
            display: flex;
            flex-direction: column;
            align-items: stretch;
        }

        label {
            margin: 10px 0 5px;
            text-align: left;
            color: #333;
            font-weight: bold;
        }

        input[type="text"], input[type="date"], input[type="submit"] {
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 16px;
            transition: border-color 0.3s, box-shadow 0.3s;
        }

        input[type="text"]:focus, input[type="date"]:focus {
            border-color: #5A78E7;
            box-shadow: 0 0 5px rgba(90, 120, 231, 0.5);
            outline: none;
        }

        input[type="submit"] {
            background-color: #5A78E7;
            color: white;
            cursor: pointer;
            transition: background-color 0.3s;
            border: none;
        }

        input[type="submit"]:hover {
            background-color: #435BAE;
        }

        .error_message, .add_project_message {
            margin-top: 20px;
            background-color: #ffe6e6;
            border: 1px solid #ff6666;
            border-radius: 4px;
            padding: 15px;
            width: 100%;
            box-sizing: border-box;
            text-align: center;
            color: #cc0000;
        }

        .back-button {
            background-color: #5A78E7;
            color: white;
            cursor: pointer;
            padding: 10px 20px;
            margin-top: 20px;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            transition: background-color 0.3s;
            text-decoration: none;
            display: inline-block;
        }

        .back-button:hover {
            background-color: #435BAE;
        }
    </style>
</head>
<body>

<div class="form-container">
    <% if (request.getAttribute("add_task_error") != null) { %>
    <div class="error_message">
        <%= request.getAttribute("add_task_error") %>
    </div>
    <% } %>
    <% if (request.getAttribute("add_task_name") != null) { %>
    <div class="add_task_message">
        <%= request.getAttribute("add_task_name") %>
    </div>
    <% } %>

    <h1>Добавить новую задачу</h1>
    <form action="${pageContext.request.contextPath}/task/add" method="post">
        <label for="name">Название</label>
        <input type="text" id="name" name="name" required>

        <label for="projectName">Название проекта, к которому прикреплена задача</label>
        <input type="text" id="projectName" name="projectName" required/>

        <label for="startDate">Дата начала</label>
        <input type="date" id="startDate" name="startDate" required>

        <label for="endDate">Планируемая дата завершения</label>
        <input type="date" id="endDate" name="endDate" required>

        <input type="submit" value="Добавить">
    </form>
    <a class="back-button" href="<c:url value="/tasks/list"/>">Назад</a>
</div>
</body>
</html>