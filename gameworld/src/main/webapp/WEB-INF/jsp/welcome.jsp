<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>

    <!-- Access the bootstrap Css like this,
        Spring boot will handle the resource mapping automcatically -->
    <link rel="stylesheet" type="text/css" href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" />


    <spring:url value="/css/main.css" var="springCss"/>
    <link href="${springCss}" rel="stylesheet"/>


</head>
<body>

<div class="container">

    <div class="starter-template">
        <label for="robots">Launched robots:</label>
        <select id="robots" multiple>
        </select>
        <br/>
        <label for="commands">Possible commands:</label>
        <select id="commands">
            <c:forEach items="${commands}" var="command">
                <option value="${command}">${command}</option>
            </c:forEach>
        </select>
        <br/>
        <input type="button" value="Set command" id="setCommand" onclick="setCommand(false)">
        <input type="button" value="Set command to all" id="setCommandToAll" onclick="setCommand(true)">
        <br/>
        <label for="logs">LOGS:</label>
        <br/>
        <textarea id="logs"></textarea>
    </div>

</div>

</body>


<script type="text/javascript"
        src="webjars/jquery/2.2.4/jquery.min.js"></script>

<script type="text/javascript" src="js/main.js"></script>

</html>