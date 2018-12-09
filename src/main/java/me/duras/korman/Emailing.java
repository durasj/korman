package me.duras.korman;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;

import me.duras.korman.dao.SettingDao;

public class Emailing {
    Mailer mailer;

    String smtpHost, smtpUser, smtpPassword, emailFrom;
    int smtpPort;

    public Emailing() {
        SettingDao settingDao = DaoFactory.INSTANCE.getSettingDao();

        smtpHost = settingDao.getByKey("smtpHost").getValue();
        smtpPort = Integer.parseInt(settingDao.getByKey("smtpPort").getValue());
        smtpUser = settingDao.getByKey("smtpUser").getValue();
        smtpPassword = settingDao.getByKey("smtpPassword").getValue();
        emailFrom = settingDao.getByKey("emailFrom").getValue();

        // SMTP is not set up
        if (smtpHost == null || smtpHost.equals("")) {
            return;
        }

        if (smtpUser == null || smtpUser.equals("") || smtpPassword == null || smtpPassword.equals("")) {
            // Anonymous SMTP
            mailer = MailerBuilder
                .withSMTPServer(smtpHost, smtpPort)
                .buildMailer();
            return;
        }

        mailer = MailerBuilder
            .withSMTPServer(smtpHost, smtpPort, smtpUser, smtpPassword)
            .buildMailer();
    }

    public void sendEmail(String recipientEmail, String subject, String htmlContent) {
        // We can't send emails if mailer was not initialized
        if (mailer == null) {
            return;
        }

        Email email = EmailBuilder.startingBlank()
            .from("Korman", emailFrom)
            .to(recipientEmail)
            .withSubject(subject)
            .withHTMLText(htmlContent)
            .buildEmail();

        mailer.sendMail(email);
    }

    
}
