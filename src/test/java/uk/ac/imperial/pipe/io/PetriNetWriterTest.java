package uk.ac.imperial.pipe.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;
import uk.ac.imperial.pipe.dsl.*;
import uk.ac.imperial.pipe.exceptions.InvalidRateException;
import uk.ac.imperial.pipe.exceptions.PetriNetComponentException;
import uk.ac.imperial.pipe.models.petrinet.*;
import uk.ac.imperial.pipe.models.petrinet.name.NormalPetriNetName;
import uk.ac.imperial.pipe.visitor.TransitionCloner;
import utils.FileUtils;

import javax.xml.bind.JAXBException;
import java.awt.Color;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

public class PetriNetWriterTest extends XMLTestCase {
    PetriNetWriter writer;

    @Override
    public void setUp() throws JAXBException {
        XMLUnit.setIgnoreWhitespace(true);
        writer = new PetriNetIOImpl();
    }

    public void testMarshalsPlace() throws IOException, SAXException, JAXBException {
        PetriNet petriNet = new PetriNet();
        Token token = new ColoredToken("Red", new Color(255, 0, 0));
        Place place = new DiscretePlace("P0", "P0");
        place.setX(255);
        place.setY(240);
        place.setNameXOffset(5);
        place.setNameYOffset(26);
        place.setTokenCount(token.getId(), 1);
        petriNet.addToken(token);
        petriNet.addPlace(place);

        assertResultsEqual(FileUtils.fileLocation(XMLUtils.getSinglePlacePath()), petriNet);
    }

    private void assertResultsEqual(String expectedPath, PetriNet petriNet)
            throws IOException, SAXException, JAXBException {
        StringWriter stringWriter = new StringWriter();
        writer.writeTo(stringWriter, petriNet);

        String expected = XMLUtils.readFile(expectedPath, Charset.defaultCharset());

        String actual = stringWriter.toString();
        assertXMLEqual(expected, actual);
    }

    public void testMarshalsTransition() throws IOException, SAXException, JAXBException {
        PetriNet petriNet = new PetriNet();
        Transition transition = new DiscreteTransition("T0", "T0");
        transition.setX(375);
        transition.setY(225);
        transition.setNameXOffset(-5.0);
        transition.setNameYOffset(35.0);
        transition.setRate(new NormalRate("1.0"));
        transition.setTimed(false);
        transition.setInfiniteServer(false);
        transition.setPriority(1);
        petriNet.addTransition(transition);
        assertResultsEqual(FileUtils.fileLocation(XMLUtils.getTransitionFile()), petriNet);
    }

    public void testMarshalsTransitionWithRateParameter()
            throws IOException, SAXException, InvalidRateException, JAXBException {
        PetriNet petriNet = new PetriNet();
        FunctionalRateParameter rateParameter = new FunctionalRateParameter("6.0", "foo", "foo");

        Transition transition = new DiscreteTransition("T0", "T0");
        transition.setX(435);
        transition.setY(180);
        transition.setNameXOffset(-5.0);
        transition.setNameYOffset(35.0);
        transition.setRate(rateParameter);
        transition.setTimed(true);
        transition.setInfiniteServer(false);
        transition.setPriority(1);

        petriNet.addTransition(transition);
        petriNet.addRateParameter(rateParameter);

        assertResultsEqual(FileUtils.fileLocation(XMLUtils.getTransitionRateParameterFile()), petriNet);
    }
    public void testMarshalsExternalTransitionWithRateParameter()
    		throws IOException, SAXException, InvalidRateException, JAXBException {
    	PetriNet petriNet = new PetriNet();
    	FunctionalRateParameter rateParameter = new FunctionalRateParameter("6.0", "foo", "foo");
    	
    	Transition transition = new DiscreteExternalTransition("T0", "T0","uk.ac.imperial.pipe.models.petrinet.TestingExternalTransition");
    	transition.setX(435);
    	transition.setY(180);
    	transition.setNameXOffset(-5.0);
    	transition.setNameYOffset(35.0);
    	transition.setRate(rateParameter);
    	transition.setTimed(true);
    	transition.setInfiniteServer(false);
    	transition.setPriority(1);
    	
    	petriNet.addTransition(transition);
    	petriNet.addRateParameter(rateParameter);
    	
    	assertResultsEqual(FileUtils.fileLocation(XMLUtils.getExternalTransitionRateParameterFile()), petriNet);
    }

    public void testMarshalsArc() throws IOException, SAXException, JAXBException {
        PetriNet petriNet = APetriNet.with(AToken.called("Default").withColor(Color.BLACK)).and(
                APlace.withId("P0").locatedAt(0, 0)).and(AnImmediateTransition.withId("T0").locatedAt(0, 0)).andFinally(
                ANormalArc.withSource("P0").andTarget("T0").and("4", "Default").tokens());


        assertResultsEqual(FileUtils.fileLocation(XMLUtils.getNormalArcWithWeight()), petriNet);
    }


    public void testMarshalsToken() throws IOException, SAXException, PetriNetComponentException, JAXBException {
        PetriNet petriNet = new PetriNet();
        Token token = new ColoredToken("red", new Color(255, 0, 0));
        petriNet.add(token);
        assertResultsEqual(FileUtils.fileLocation(XMLUtils.getTokenFile()), petriNet);
    }

    public void testMarshalsAnnotation() throws IOException, SAXException, JAXBException {
        PetriNet petriNet = new PetriNet();
        AnnotationImpl annotation = new AnnotationImpl(93, 145, "#P12s", 48, 20, false);
        petriNet.addAnnotation(annotation);
        assertResultsEqual(FileUtils.fileLocation(XMLUtils.getAnnotationFile()), petriNet);
    }
}


