package com.royalblades.servicio;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class NotificacionEmailService {

    public void enviarConfirmacionCita(String destinatario, String nombreCliente, String fechaHora) {
        String remitente = System.getenv("SMTP_USER");
        String passwordApp = System.getenv("SMTP_PASS");

        if (remitente == null || passwordApp == null || remitente.isEmpty()) {
            System.out.println("[AVISO SMTP] Variables SMTP_USER/SMTP_PASS no configuradas. Correo simulado para: " + destinatario);
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "://gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, passwordApp);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Confirmación de Cita - Royal Blades Barber Shop");
            message.setText("Hola " + nombreCliente + ",\n\n" +
                    "Tu cita en Royal Blades Barber Shop ha sido confirmada para el: " + fechaHora.replace("T", " ") + ".\n\n" +
                    "¡Te esperamos!");
            Transport.send(message);
        } catch (MessagingException e) {
            System.err.println("[ERROR SMTP] No se pudo enviar el correo: " + e.getMessage());
        }
    }
}
