<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<!-- Collegamento all'Header -->
<jsp:include page="/application/Header.jsp" />

<!doctype html>
<html lang="it">

<title>Registrazione Studente - UniMeet</title>

<body>
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="registration-card">
                <h2 class="text-center mb-4">Registrazione Studente</h2>
                <form action="<%= request.getContextPath() %>/RegistrazioneStudenteServlet" method="post" onsubmit="return validateForm()">
                    <div class="form-group">
                        <label for="email">E-Mail</label>
                        <input type="text" class="form-control" name="email" id="email" placeholder="Inserisci l'E-MAIL" required>
                    </div>
                    <div class="form-group">
                            <label for="inputPassword">Password</label>
                            <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Password" required>
                        </div>
                        
                        <div class="form-group">
    						<label for="inputConfirmPassword">Conferma Password</label>
    						<input type="password" name="confirmPassword" id="inputConfirmPassword" class="form-control" placeholder="Conferma Password" required>
    						<small id="passwordError" class="text-danger" style="display:none;">Le password non corrispondono!</small>
						</div>
       					<div class="form-group form-check">
                        <input type="checkbox" class="form-check-input" id="showPassword" onclick="togglePasswordVisibility()">
                        <label class="form-check-label" for="showPassword">Mostra Password</label>
                    </div>
                    <div class="form-group">
                        <label for="nome">Nome</label>
                        <input type="text" class="form-control" name="nome" id="nome" placeholder="Inserisci il tuo nome" required>
                    </div>
                    <div class="form-group">
                        <label for="cognome">Cognome</label>
                        <input type="text" class="form-control" name="cognome" id="cognome" placeholder="Inserisci il tuo cognome" required>
                    </div>
                    <div class="form-group">
                        <label for="matricola">Matricola</label>
                        <input type="text" class="form-control" name="matricola" id="matricola" placeholder="Inserisci la matricola" required>
                    </div>
                    <div class="form-group">
                        <label for="domandaSicurezza">Domanda di sicurezza</label>
                        <select class="form-control" name="domanda" id="domandaSicurezza" required>
                            <option selected disabled>Scegli domanda di sicurezza</option>
                            <option>Qual è il nome del tuo primo animale domestico?</option>
                            <option>Qual è la tua città natale?</option>
                            <option>Qual è il cognome di tua madre da nubile?</option>
                            <option>Qual è il nome del tuo migliore amico?</option>
                            <option>Qual è il nome del tuo cantante preferito?</option>
                            <option>Qual era il tuo soprannome da piccolo?</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="risposta">Risposta</label>
                        <input type="text" class="form-control" name="risposta" id="risposta" placeholder="Risposta" required>
                    </div>
                    <div class="custom-btn-container">
                        <button type="submit" class="btn btn-primary">Registrati!</button>
                        <a href="Registrazione.jsp" class="btn btn-danger" role="button">Torna alle opzioni</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="<%= request.getContextPath() %>/scripts/ValidazionePass.js"></script>
<script src="<%= request.getContextPath() %>/scripts/RegistrazioneStudente.js"></script>

</body>
<jsp:include page="Footer.jsp" />
</html>
