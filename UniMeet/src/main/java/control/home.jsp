<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UniMeet - Home</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css">
     <link rel="stylesheet" href="<%= request.getContextPath() %>/css/home.css">
    
    
</head>
<body>
<%@ include file="Header.jsp" %>

<!-- Hero Section -->
<section class="hero">
    <div class="hero-text">
        <h1>Benvenuti</h1>
        <p>UniMeet è una piattaforma che facilita la modalità di interazione tra gli studenti ed i docenti fornendo un metodo semplice ed efficace per la prenotazione del ricevimento studenti.</p>
        <button onclick="location.href='prenotazione.jsp'">Prenota un ricevimento</button>
    </div>
    <div class="hero-container">
        <img src="<%= request.getContextPath() %>/images/LOGO1.PNG" alt="UniMeet Logo">
    </div>
</section>

<%@ include file="Footer.jsp" %>
</body>
</html>
