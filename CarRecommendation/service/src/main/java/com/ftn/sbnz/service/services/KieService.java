package com.ftn.sbnz.service.services;

import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.drools.template.DataProvider;
import org.drools.template.DataProviderCompiler;
import org.drools.template.objects.ArrayDataProvider;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class KieService {

    public KieService() {
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

    public KieSession createKieSessionFromTemplate() {
        ClassPathResource classPathResource = new ClassPathResource("/rules/recommendation/car_recommendation.drt");
        InputStream template;
        try {
            template = classPathResource.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DataProvider dataProvider = new ArrayDataProvider(new String[][]{
                new String[]{"CarPreferenceType.SPORT", "engineHp", ">=", "250", "20"},
                new String[]{"CarPreferenceType.SPORT", "acceleration0100KmHS", "<=", "6.0", "20"},
                new String[]{"CarPreferenceType.SPORT", "transmission", "==", "TransmissionType.MANUAL", "5"},
                new String[]{"CarPreferenceType.SPORT", "driveWheels", "==", "DriveWheels.RWD", "10"},
                new String[]{"CarPreferenceType.SPORT", "bodyType", "in", "(BodyType.CABRIOLET, BodyType.COUPE, BodyType.ROADSTER, BodyType.SEDAN)", "5"},

                new String[]{"CarPreferenceType.FAMILY_FRIENDLY", "numberOfSeats", ">=", "5", "10"},
                new String[]{"CarPreferenceType.FAMILY_FRIENDLY", "bodyType", "in", "(BodyType.CROSSOVER, BodyType.HATCHBACK, BodyType.MINIVAN, BodyType.SEDAN, BodyType.WAGON)", "20"},
                new String[]{"CarPreferenceType.FAMILY_FRIENDLY", "curbWeightKg", ">=", "1500", "15"},
                new String[]{"CarPreferenceType.FAMILY_FRIENDLY", "mixedFuelConsumptionPer100KmL", "<=", "8.0", "15"},
                new String[]{"CarPreferenceType.FAMILY_FRIENDLY", "yearFrom", ">=", "2015", "20"},

                new String[]{"CarPreferenceType.OFF_ROAD", "heightMm", ">=", "1500", "15"},
                new String[]{"CarPreferenceType.OFF_ROAD", "driveWheels", "in", "(DriveWheels.AWD, DriveWheels.FOURWD)", "20"},
                new String[]{"CarPreferenceType.OFF_ROAD", "mixedFuelConsumptionPer100KmL", "<=", "8.0", "15"},
                new String[]{"CarPreferenceType.OFF_ROAD", "curbWeightKg", ">", "2000", "10"},
                new String[]{"CarPreferenceType.OFF_ROAD", "engineType", ">", "EngineType.DIESEL", "10"},
        });

        DataProviderCompiler converter = new DataProviderCompiler();
        String drl = converter.compile(dataProvider, template);

        return createKieSessionFromDRL(drl);
    }
}
