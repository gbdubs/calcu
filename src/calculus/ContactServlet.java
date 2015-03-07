package calculus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calculus.api.UserContextAPI;
import calculus.utilities.Settings;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ContactServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException, ServletException {
		
		UserContextAPI.addUserContextToRequest(req, "/contact");
			
		resp.setContentType("text/html");
		
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contact.jsp");
		jsp.forward(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		sendEmailFromContactRequest(req);
		
		UserContextAPI.addUserContextToRequest(req, "/contact");
		resp.setContentType("text/html");
		RequestDispatcher jsp = req.getRequestDispatcher("/WEB-INF/pages/contactSubmitted.jsp");
		jsp.forward(req, resp);
	}
	
	private void sendEmailFromContactRequest(HttpServletRequest req){
		String messageText = constructEmailMessage(req);
		boolean anonymous = req.getParameter("anonymousSubmit") != null;
		
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
		
		if (anonymous){
			try{
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("anonymouselephant@calcu.com", "Anonymous Elephant"));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(Settings.ADMIN_EMAIL));
				message.setSubject("Feedback from Anonymous");
				message.setText(messageText);
				Transport.send(message);
			} catch (AddressException e){
				// We won't send it if there is an issue with the Admin address, which shouldn't be an issue.
			} catch (MessagingException e){
				// We can't send in this instance.  We log this problem.
			} catch (UnsupportedEncodingException e) {
				// Not sure what to do in this case. Better to abort.
			}
		} else {
			try{
				User user = UserServiceFactory.getUserService().getCurrentUser();
				String username = req.getParameter("username");
				String email = req.getParameter("email");
				
				Message message = new MimeMessage(session);
				if (user == null || !email.equals(user.getEmail())){
					message.setFrom(new InternetAddress("mailing@calcu-us.appspot.com", username));
				} else {
					message.setFrom(new InternetAddress(email, username));
				}
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(Settings.ADMIN_EMAIL));
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(email));
				message.setText(messageText);
				message.setSubject("Feedback from " + username);
				Transport.send(message);
			} catch (AddressException e){
				// We won't send it if there is an issue with the Admin address, which shouldn't be an issue.
			} catch (MessagingException e){
				// We can't send in this instance.  We log this problem.
			} catch (UnsupportedEncodingException e) {
				// Not sure what to do in this case. Better to abort.
			}
		}
	}
	
	private String constructEmailMessage(HttpServletRequest req){
		String responsePreference = req.getParameter("responsePreference");
		
		String messageBody = req.getParameter("body");
		
		Date now = new Date();
		String dateAndTime = DateFormat.getDateInstance().format(now);
		
		String message = "Hey Grady, \n\n";
		
		if (req.getParameter("anonymousSubmit") != null){

			message += "Someone submitted some anonymous feedback on " + dateAndTime + ".\n\n";			
			
		} else if (req.getParameter("regularSubmit") != null){
			String username = req.getParameter("username");
			String email = req.getParameter("email");
			
			String realEmail = email;
			User user = UserServiceFactory.getUserService().getCurrentUser();
			if (user != null){
				realEmail = user.getEmail();
			}
			
			message += "User " + username + " (email="+ realEmail + ") submitted some feedback on " + dateAndTime + ".\n\n";
			
			message += "Their reply preference was ' " + responsePreference + " '.\n\n";
			
			if (!email.equals(realEmail)){
				message += "If desired, the user would prefer to be contacted at " + email + ".\n\n";
			}
			
		} else {
			
			message += "Someone submitted the form without doing a propper post on " + dateAndTime + ".  This is bad news bears.\n\n";
		
		}
		
		message += "'"+ messageBody +"'\n\n";
		
		message += "Best,\n\nCalcEmailBot";
		
		return message;
	}
	
}
