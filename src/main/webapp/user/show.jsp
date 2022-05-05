<%--
  Created by IntelliJ IDEA.
  User: dorota
  Date: 04.05.2022
  Time: 11:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/header.jsp" %>

<div class="container-fluid">
    <!-- Page Heading -->
    <div class="d-sm-flex align-items-center justify-content-between mb-4">
        <h1 class="h3 mb-0 text-gray-800">UsersCRUD</h1>
        <a href="<c:url value="/user/list"/>" class="d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm">
            <i class="fas fa-download fa-sm text-white-50"></i> Lista użytkowników</a>
    </div>
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-primary">Dane użytkownika</h6>
    </div>

    <div class="card-body">
        <table class="table">
            <form method="post">
                <input type="hidden" name="id" value="${user.id}"/>

                <tr>
                    <td><label for="userId">ID</label></td>
                    <td><input value="${user.id}" name="userId" type="text" class="form-control" id="userId"
                               placeholder="Id użytkownika"></td>
                </tr>
                <tr>
                    <td><label for="userName">Nazwa</label></td>
                    <td><input value="${user.username}" name="userName" type="text" class="form-control" id="userName"
                               placeholder="Nazwa użytkownika"></td>
                </tr>
                <tr>
                    <td><label for="userEmail">Email</label></td>
                    <td><input value="${user.email}" name="userEmail" type="email" class="form-control" id="userEmail"
                               placeholder="Email użytkownika"></td>
                </tr>

            </form>
        </table>
    </div>

<%@ include file="/footer.jsp" %>
