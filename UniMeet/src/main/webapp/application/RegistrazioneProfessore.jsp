<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<!doctype html>
<html lang="it">

<title>Registrazione Professore - UniMeet</title>

<body>
<!-- Collegamento all'Header -->
<jsp:include page="/application/Header.jsp" />

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="registration-card">
                <h2 class="text-center mb-4">Registrazione Professore</h2>
                <form action="<%= request.getContextPath() %>/RegistrazioneServlet" method="post">
                    <div class="form-group">
                        <label for="email">E-Mail</label>
                        <input type="text" class="form-control" name="email" id="email" placeholder="Inserisci l'E-MAIL" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" class="form-control" name="password" id="password" placeholder="Inserisci la Password" required>
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
                        <label for="codice">Codice Professore</label>
                        <input type="text" class="form-control" name="codiceProfessore" id="codice" placeholder="Inserisci il codice professore" required>
                    </div>
                    <div class="form-group">
                        <label for="ufficio">Ufficio</label>
                        <input type="text" class="form-control" name="ufficio" id="ufficio" placeholder="Inserisci l'ufficio" required>
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

<script>
    let emailField = document.getElementById('email');
    let defaultDomain = '@professori.unisa.it';

    // Funzione per aggiungere il dominio se necessario
    function ensureDomain(value) {
        return value.includes('@') ? value.split('@')[0] + defaultDomain : value + defaultDomain;
    }

    // Aggiungi il dominio quando l'utente scrive
    emailField.addEventListener('input', function() {
        emailField.value = ensureDomain(emailField.value);
        emailField.setSelectionRange(emailField.value.indexOf('@'), emailField.value.indexOf('@'));
    });

    // Gestisci i tasti speciali (invio, spazio, @)
    emailField.addEventListener('keydown', function(event) {
        if (['Enter', ' ', '@'].includes(event.key)) {
            emailField.value = ensureDomain(emailField.value);
            emailField.setSelectionRange(emailField.value.indexOf('@'), emailField.value.indexOf('@'));
            event.preventDefault();  // Previeni l'azione predefinita
        }
    });
</script>

<jsp:include page="Footer.jsp" />

</body>
</html>
