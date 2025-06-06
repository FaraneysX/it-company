<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Управление</title>
    <style>
        body {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin: 0;
            font-family: Arial, sans-serif;
            background-color: #f0f2f5;
        }

        .header {
            width: 100%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            background-color: #31276f;
            padding: 10px 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            color: white;
        }

        .header form {
            margin: 0;
        }

        .header form input[type="submit"] {
            border: none;
            padding: 8px 16px;
            border-radius: 5px;
            cursor: pointer;
            background-color: #5A78E7;
            color: white;
            transition: background-color 0.3s, transform 0.3s;
        }

        .header form input[type="submit"]:hover {
            background-color: #435BAE;
            transform: scale(1.02);
        }

        .container {
            width: 90%;
            max-width: 1200px;
            margin: 20px auto;
        }

        .message {
            margin-top: 10px;
            border: 2px solid #888;
            border-radius: 8px;
            padding: 10px;
            background-color: #ffffff;
            width: 100%;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .project-list, .task-list {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
        }

        .project, .task {
            border: 1px solid #ddd;
            border-radius: 10px;
            background: #fff;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            padding: 20px;
            box-sizing: border-box;
            transition: transform 0.3s, box-shadow 0.3s;
        }

        .project:hover, .task:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }

        .project-info, .task-info {
            text-align: center;
        }

        .task-actions, .project-actions {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 10px;
            margin-top: 10px;
        }

        .task-actions form, .project-actions form {
            margin: 0;
        }

        .task-actions input[type="submit"], .project-actions input[type="submit"] {
            border: none;
            padding: 8px 16px;
            border-radius: 5px;
            cursor: pointer;
            background-color: #5A78E7;
            color: white;
            transition: background-color 0.3s, transform 0.3s;
        }

        .task-actions input[type="submit"]:hover, .project-actions input[type="submit"]:hover {
            background-color: #435BAE;
            transform: scale(1.02);
        }

        .task-actions input[type="submit"].delete, .project-actions input[type="submit"].delete {
            background-color: #e74c3c;
            transition: background-color 0.3s, transform 0.3s;
        }

        .task-actions input[type="submit"].delete:hover, .project-actions input[type="submit"].delete:hover {
            background-color: #c0392b;
            transform: scale(1.02);
        }

        a {
            color: #5A78E7;
            text-decoration: none;
            transition: color 0.3s;
        }

        a:hover {
            color: #435BAE;
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="header">
    <form action="${pageContext.request.contextPath}/logout" method="get">
        <input type="submit" value="Выйти">
    </form>
    <div style="display: flex; gap: 10px;">
        <form action="${pageContext.request.contextPath}/task/add" method="get">
            <input type="submit" value="Добавить новую задачу">
        </form>
        <form action="${pageContext.request.contextPath}/project/add" method="get">
            <input type="submit" value="Добавить новый проект">
        </form>
    </div>
</div>

<div class="container">
    <c:if test="${not empty sessionScope.message}">
        <div class="message">
                ${sessionScope.message}
        </div>
        <c:remove var="message" scope="session"/>
    </c:if>

    <h2>Задачи</h2>
    <div class="task-list">
        <c:forEach var="task" items="${taskList}">
            <div class="task">
                <div class="task-info">
                    <h3>${task.name()}</h3>
                    <p><strong>Дата начала:</strong> ${task.startDate()}</p>
                    <p><strong>Дата завершения:</strong> ${task.endDate()}</p>
                </div>
                <div class="task-actions">
                    <form action="${pageContext.request.contextPath}/task/delete" method="post">
                        <input type="hidden" name="idTask" value="${task.id()}">
                        <input type="submit" value="Удалить задачу" class="delete">
                    </form>
                </div>
            </div>
        </c:forEach>
    </div>

    <h2>Проекты</h2>
    <div class="project-list">
        <c:forEach var="project" items="${projectList}">
            <div class="project">
                <div class="project-info">
                    <h3>${project.name()}</h3>
                    <p><strong>Дата начала:</strong> ${project.startDate()}</p>
                </div>
                <div class="project-actions">
                    <form action="${pageContext.request.contextPath}/project/update" method="get">
                        <input type="hidden" name="idProject" value="${project.id()}">
                        <input type="submit" value="Обновить проект">
                    </form>
                    <form action="${pageContext.request.contextPath}/project/delete" method="post">
                        <input type="hidden" name="idProject" value="${project.id()}">
                        <input type="submit" value="Удалить проект" class="delete">
                    </form>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>

