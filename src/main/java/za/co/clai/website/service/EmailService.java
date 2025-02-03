package za.co.clai.website.service;

import za.co.clai.website.model.EmailModel;

public interface EmailService {
    void sendSimpleMessage(EmailModel emailModel);
}
