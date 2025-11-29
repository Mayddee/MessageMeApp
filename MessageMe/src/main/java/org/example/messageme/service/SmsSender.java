package org.example.messageme.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Component;

@Component
public class SmsSender {

    // Twilio credentials (получаешь из twilio.com/console)
    private static final String ACCOUNT_SID = "YOUR_ACCOUNT_SID";
    private static final String AUTH_TOKEN = "YOUR_AUTH_TOKEN";
    private static final String TWILIO_PHONE_NUMBER = "+1234567890"; // твой номер из Twilio

    public SmsSender() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void sendSms(String toPhoneNumber, String messageBody) {
        Message.creator(
                new PhoneNumber(toPhoneNumber),         // кому отправляем
                new PhoneNumber(TWILIO_PHONE_NUMBER),   // от кого (Twilio номер)
                messageBody
        ).create();
        System.out.println("SMS sent to " + toPhoneNumber);
    }
}


