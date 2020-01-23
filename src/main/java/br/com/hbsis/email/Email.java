package br.com.hbsis.email;

import br.com.hbsis.pedido.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
public class Email {


        @Autowired
        private final JavaMailSender mailSender;

    public Email(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RequestMapping(path = "/email-send", method = RequestMethod.GET)
        public String sendMail(Pedido pedido) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setText("Olá, Seu pedido segue em data:" + "\r\n" + pedido.getPeriodo().getDataRetiradaPedidos());
            message.setTo("danielranghetti48@gmail.com");
            message.setFrom("gabedevfoda@gmail.com");

            try {
                mailSender.send(message);
                return "Email enviado com sucesso!";
            } catch (Exception e) {
                e.printStackTrace();
                return "Erro ao enviar email.";
            }
        }
    }

