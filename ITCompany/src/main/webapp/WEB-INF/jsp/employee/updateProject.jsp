<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Изменение данных проекта</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #483D8B;
            color: white;
            margin: 0;
            padding: 0;
        }

        .forms {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin: 0;
            padding: 20px;
        }

        .form-container {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            margin-top: 20px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
            width: 40%;
            color: black;
        }

        form {
            display: flex;
            flex-direction: column;
        }

        label {
            margin: 10px 0 5px;
        }

        input[type="text"], input[type="submit"], select {
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        input[type="submit"] {
            background-color: #5A78E7;
            color: white;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #435BAE;
        }

        .project_update_message {
            margin-top: 35px;
            background-color: white;
            border: 1px solid #ccc;
            border-radius: 4px;
            padding: 8px;
            color: black;
        }

        .back {
            display: inline-block;
            background-color: #5A78E7;
            color: white;
            cursor: pointer;
            padding: 8px;
            margin-top: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            text-decoration: none;
        }

        .back:hover {
            background-color: #435BAE;
        }
    </style>
</head>
<body>
<div class="forms">
    <c:if test="${not empty sessionScope.message}">
        <div class="project_update_message">
            <p>${sessionScope.message}</p>
        </div>
        <c:remove var="message" scope="session"/>
    </c:if>
    <div class="form-container">
        <h1>Изменение данных проекта</h1>
        <form action="${pageContext.request.contextPath}/project/update" method="post">
            <input type="hidden" name="idProject" value="${project.id()}">

            <label for="name">Название</label>
            <input type="text" id="name" name="name" value="${project.name()}" required>

            <label for="startDate">Дата начала</label>
            <input type="date" id="startDate" name="startDate" value="${project.startDate()}" required>

            <input type="submit" value="Изменить">
        </form>
    </div>
    <a class="back" href="<c:url value="/tasks/list"/>">Назад</a>
</div>
</body>
</html>
