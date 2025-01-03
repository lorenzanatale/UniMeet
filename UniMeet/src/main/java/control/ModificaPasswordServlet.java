package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import Utils.PasswordHasher;
import model.Professore;
import model.ProfessoreService;
import model.Studente;
import model.StudenteService;


@WebServlet("/ModificaPasswordServlet")
public class ModificaPasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        String userEmail = request.getParameter("userEmail");
        String rispostaUtente = request.getParameter("risposta");
        String domandaUtente = request.getParameter("domanda");
        String newPassword = request.getParameter("newPassword");

        	Professore p=null;
        	Studente s = null;
        
        	try {
        		
        		
                s = StudenteService.cercaStudenteEmail(userEmail);
                
                if (s != null) {
                    String domandaSicurezza = s.getDomanda();
                    String rispostaRegistrata = s.getRisposta();

                    if (rispostaUtente.trim().equalsIgnoreCase(rispostaRegistrata.trim()) && 
                        domandaUtente.trim().equalsIgnoreCase(domandaSicurezza.trim())) {
                        
                        String hashedPassword = PasswordHasher.hashPassword(newPassword);
                        s.setPassword(hashedPassword);

                        boolean isUpdated = StudenteService.modificaStudente(s);
                        
                        
                        if (isUpdated) {
                            response.getWriter().write("Password aggiornata con successo!");
                            response.sendRedirect(request.getContextPath()+"/application/CambioPassword.jsp?error=passwordCambiata");
                            }
                         else {
                             response.getWriter().write("Errore durante l'aggiornamento della password.");
                             response.sendRedirect(request.getContextPath()+"/application/CambioPassword.jsp?error=formErrore");
                          }
                        
                        
                    } else {
                        response.getWriter().write("Risposta di sicurezza errata.");
                        response.sendRedirect(request.getContextPath()+"/application/CambioPassword.jsp?error=formErrore");
                    }
                    
                }
            
			} catch (Exception e) {
				System.out.println("Studente non trovato");
			}
        	
        	//verifica per il professore
        	try {

        		
                 p = ProfessoreService.cercaProfessoreEmail(userEmail);
                if (p != null) {
                    String domandaSicurezza = p.getDomanda();
                    String rispostaRegistrata = p.getRisposta();

                    if (rispostaUtente.trim().equalsIgnoreCase(rispostaRegistrata.trim()) && 
                        domandaUtente.trim().equalsIgnoreCase(domandaSicurezza.trim())) {
                        
                        String hashedPassword = PasswordHasher.hashPassword(newPassword);
                        p.setPassword(hashedPassword);

                        boolean isUpdated = ProfessoreService.modificaProfessore(p);
                        
                        
                        if (isUpdated) {
                            response.getWriter().write("Password aggiornata con successo!");
                            response.sendRedirect(request.getContextPath()+"/application/CambioPassword.jsp?error=passwordCambiata");
                            }
                         else {
                            response.getWriter().write("Errore durante l'aggiornamento della password.");
                            response.sendRedirect(request.getContextPath()+"/application/CambioPassword.jsp?error=formErrore");
                         }
                        
                    } else {
                        response.getWriter().write("Risposta di sicurezza errata.");
                        response.sendRedirect(request.getContextPath()+"/application/CambioPassword.jsp?error=formErrore");
                    }
                }
            
			} catch (Exception e) {
				System.out.println("Professore non trovato");
			}
        	
        
        	if(s==null && p==null) {
				System.out.println("Utente non trovato");
				response.sendRedirect(request.getContextPath()+"/application/CambioPassword.jsp?error=userNotFound");
        	}
        
    }
}