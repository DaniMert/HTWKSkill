package com.amazon.asksdk.mensaleipzig.speechlet;

import com.amazon.asksdk.mensaleipzig.helper.SpeechTextHelper;
import com.amazon.asksdk.mensaleipzig.model.Gericht;
import com.amazon.asksdk.mensaleipzig.network.MensaLeipzigRequest;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class MensaLeipzigSpeechlet implements SpeechletV2 {

    private static final String SLOT_MENSA = "Mensa";
    private static final String SLOT_DATUM = "Datum";
    private static final String SLOT_KATEGORIE = "Kategorie";
    private static final Logger log = LoggerFactory.getLogger(MensaLeipzigSpeechlet.class);

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
        log.info("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
                requestEnvelope.getSession().getSessionId());
    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
        log.info("onLaunch requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
                requestEnvelope.getSession().getSessionId());
        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        IntentRequest request = requestEnvelope.getRequest();
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                requestEnvelope.getSession().getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if ("SpeiseplanIntent".equals(intentName)) {
            return getSpeiseplanResponse(intent);
        } else if ("GreetingIntent".equals(intentName)) {
            return getGreetingResponse();
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse();
        } else {
            return getAskResponse("Mensa Leipzig", "Ich weiß nicht, was du meinst.");
        }
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
        log.info("onSessionEnded requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
                requestEnvelope.getSession().getSessionId());
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getWelcomeResponse() {
        String speechText = "Willkommen bei Mensa Leipzig.";
        return getAskResponse("Mensa Leipzig", speechText);
    }

    /**
     * Creates a {@code SpeechletResponse} for the Speiseplan intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     * @param intent
     */
    private SpeechletResponse getSpeiseplanResponse(Intent intent) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String datum = dateFormat.format(new Date());
        Slot datumSlot = intent.getSlot(SLOT_DATUM);
        if (datumSlot != null && datumSlot.getValue() != null) {
            datum = datumSlot.getValue();
        }
        Slot mensaSlot = intent.getSlot(SLOT_MENSA);
        Slot kategorieSlot = intent.getSlot(SLOT_KATEGORIE);
        if(mensaSlot == null){
            String speechText = "Bitte eine Mensa in Leipzig angeben.";
            SimpleCard card = getSimpleCard("Mensa Leipzig", speechText);
            PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
            return SpeechletResponse.newTellResponse(speech, card);
        } else {
            String mensa = intent.getSlot(SLOT_MENSA).getValue();
            String kategorie = null;
            if(kategorieSlot != null) {
                kategorie = kategorieSlot.getValue();
            }

            LinkedList<Gericht> gerichte = MensaLeipzigRequest.getSpeiseplan(mensa, datum);
            String speechText = SpeechTextHelper.getSpeechTextFromSpeiseplan(gerichte, kategorie);

            // Create the Simple card content.
            SimpleCard card = getSimpleCard("Mensa Leipzig", speechText);

            // Create the plain text output.
            PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);

            return SpeechletResponse.newTellResponse(speech, card);
        }
    }
    /**
     * Creates a {@code SpeechletResponse} for the Greeting intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getGreetingResponse() {
        String speechText = "Geht klar. Herzlich Willkommen zu diesem Vortrag im Oberseminar Chatterbots. Daniel Mertens, Minh Pham und Phil Taubert werden euch nun ein paar Dinge über mich erzählen.";

        SimpleCard card = getSimpleCard("Mensa Leipzig", speechText);
        PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getHelpResponse() {
        String speechText = "Du kannst mich fragen was es heute in den Leipziger Mensen zu Essen gibt.";
        SimpleCard card = getSimpleCard("Mensa Leipzig", speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    /**
     * Helper method that creates a card object.
     * @param title title of the card
     * @param content body of the card
     * @return SimpleCard the display card to be sent along with the voice response.
     */
    private SimpleCard getSimpleCard(String title, String content) {
        SimpleCard card = new SimpleCard();
        card.setTitle(title);
        card.setContent(content);

        return card;
    }

    /**
     * Helper method for retrieving an OutputSpeech object when given a string of TTS.
     * @param speechText the text that should be spoken out to the user.
     * @return an instance of SpeechOutput.
     */
    private PlainTextOutputSpeech getPlainTextOutputSpeech(String speechText) {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return speech;
    }

    /**
     * Helper method that returns a reprompt object. This is used in Ask responses where you want
     * the user to be able to respond to your speech.
     * @param outputSpeech The OutputSpeech object that will be said once and repeated if necessary.
     * @return Reprompt instance.
     */
    private Reprompt getReprompt(OutputSpeech outputSpeech) {
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(outputSpeech);

        return reprompt;
    }

    /**
     * Helper method for retrieving an Ask response with a simple card and reprompt included.
     * @param cardTitle Title of the card that you want displayed.
     * @param speechText speech text that will be spoken to the user.
     * @return the resulting card and speech text.
     */
    private SpeechletResponse getAskResponse(String cardTitle, String speechText) {
        SimpleCard card = getSimpleCard(cardTitle, speechText);
        PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
        Reprompt reprompt = getReprompt(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
}