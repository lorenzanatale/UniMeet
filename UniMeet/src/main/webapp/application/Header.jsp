<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>UniMeet</title>
    <!-- Link al file CSS della navbar -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/navbar.css">
</head>
<body>
    <%
        // Controllo sessione
        String ruolo = (String) session.getAttribute("ruolo");
        String email = (String) session.getAttribute("email");

        if (session == null || email == null) {
            ruolo = null;
            email = null;
        }
    %>
    <nav class="navbar">
        <div class="navbar-container">
            <div class="navbar-content">
                <!-- Logo con link alla home -->
                <div class="logo">
                 <a href="Home.jsp" style="text-decoration: none; color: var(--primary-color); font-size: 1.5rem; font-weight: bold;">UniMeet</a>
              
                   
                </div>

                <!-- Barra di ricerca -->
                <div class="search-container">
                    <div class="search-box">
                        <input type="text" placeholder="Cerca">
                        <svg class="search-icon" width="20" height="20" fill="none" stroke="currentColor">
                            <path d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </div>
                </div>

                <!-- Menu utente -->
                <div class="user-menu">
                    <% if (email != null && ruolo != null) { %>
                        <!-- Utente loggato -->
                        <button class="user-button">
                            <%= email %>
                            <svg width="16" height="16" fill="none" stroke="white">
                                <path d="M5 10l5-5 5 5" stroke-linecap="round" stroke-linejoin="round"/>
                            </svg>
                        </button>
                        <div class="dropdown">
                            <% if ("professore".equals(ruolo)) { %>
                                <a href="<%= request.getContextPath() %>/GestisciRicevimenti.jsp">Gestisci ricevimenti</a>
                                <a href="<%= request.getContextPath() %>/RiepilogoRicevimenti.jsp">Riepilogo ricevimenti</a>
                                <a href="<%= request.getContextPath() %>/RicevimentiInProgramma.jsp">Ricevimenti in programma</a>
                                <hr>
                            <% } else if ("studente".equals(ruolo)) { %>
                                <a href="<%= request.getContextPath() %>/PrenotaRicevimento.jsp">Prenota ricevimento</a>
                                <a href="<%= request.getContextPath() %>/StoricoPrenotazioni.jsp">Storico prenotazioni</a>
                                <a href="<%= request.getContextPath() %>/RicevimentiDisponibili.jsp">Ricevimenti disponibili</a>
                                <hr>
                            <% } %>
                            <a href="<%= request.getContextPath() %>/Logout.jsp" class="logout">Logout</a>
                        </div>
                    <% } else { %>
                        <!-- Utente non autenticato -->
                        <div class="auth-buttons">
                            <a href="<%= request.getContextPath() %>/application/Login.jsp"><button>ACCEDI</button></a>
                            <a href="<%= request.getContextPath() %>/application/Registrazione.jsp"><button>REGISTRATI</button></a>
                        </div>
                    <% } %>
                </div>

                <!-- Bottone menu mobile -->
                <button class="mobile-menu-button">
                    <svg width="24" height="24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M4 6h16M4 12h16M4 18h16" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                </button>
            </div>
        </div>
        <div class="menu-dropdown"></div>
    </nav>
</body>
</html>
