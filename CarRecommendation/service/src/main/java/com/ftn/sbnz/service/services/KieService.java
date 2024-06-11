package com.ftn.sbnz.service.services;

import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.Globals;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class KieService {
    public final KieContainer kieContainer;

    public static KieSession cepKsession;
    public static KieSession backwardKsession;

    @Autowired
    public KieService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;

        cepKsession = kieContainer.newKieSession("cepKsession");
        backwardKsession = kieContainer.newKieSession("bwKsession");
    }

    private KieSession createKieSessionFromDRL(String drl) {
        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);

        Results results = kieHelper.verify();

        if (results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)) {
            List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);
            for (Message message : messages) {
                System.out.println("Error: " + message.getText());
            }

            throw new IllegalStateException("Compilation errors were found. Check the logs.");
        }

        return kieHelper.build().newKieSession();
    }

    /**
     * Tests customer-classification-simple.drt template by manually creating
     * the corresponding DRL using a spreadsheet as the data source.
     */
    public KieSession createKieSessionFromSpreadsheet(String template_path, String table_path) {
        ClassPathResource templateResource = new ClassPathResource(template_path);
        ClassPathResource dataResource = new ClassPathResource(table_path);

        try {
            InputStream template = templateResource.getInputStream();
            InputStream data = dataResource.getInputStream();

            ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();
            String drl = converter.compile(data, template, 3, 2);

            return this.createKieSessionFromDRL(drl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
