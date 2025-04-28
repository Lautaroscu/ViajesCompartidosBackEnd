//package com.viajes.viajesCompartidos.services;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class EmailService {
//
//    private final JavaMailSender mailSender;
//    @Value("${spring.mail.username}")
//    private  String from;
//
//    @Autowired
//    public EmailService(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }
//
//    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//        helper.setTo(to);
//        helper.setSubject(subject);
//        helper.setText(htmlContent, true); // El "true" indica que es HTML.
//        helper.setFrom(from); // Cambia por tu correo.
//
//        mailSender.send(message);
//    }
//}
//
