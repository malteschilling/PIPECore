package uk.ac.imperial.pipe.models.petrinet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import uk.ac.imperial.pipe.dsl.ANormalArc;
import uk.ac.imperial.pipe.dsl.APetriNet;
import uk.ac.imperial.pipe.dsl.APlace;
import uk.ac.imperial.pipe.dsl.AToken;
import uk.ac.imperial.pipe.dsl.AnImmediateTransition;
import uk.ac.imperial.pipe.exceptions.PetriNetComponentException;

public class ExecutablePetriNetTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private PetriNet net;
    private ExecutablePetriNet epn;

    @Mock
    private PropertyChangeListener mockListener;



    @Before
    public void setUp() {
        net = new PetriNet();
        epn = net.makeExecutablePetriNet();  
    }
    @Test
    public void equalsAndHashCodeLawsWhenEqual() {
    	net = buildTestNet();
    	epn = net.makeExecutablePetriNet(); 
    	PetriNet net2 = buildTestNet();
    	ExecutablePetriNet epn2 = net2.makeExecutablePetriNet(); 
        assertTrue(epn.equals(epn2));
        assertEquals(epn.hashCode(), epn2.hashCode());
    }

    @Test
    public void equalsAndHashCodeLawsWhenNotEqual() throws PetriNetComponentException {
    	net = buildTestNet();
    	epn = net.makeExecutablePetriNet(); 
    	PetriNet net2 = buildTestNet();
    	net2.add(new DiscreteTransition("T99")); 
    	ExecutablePetriNet epn2 = net2.makeExecutablePetriNet(); 
    	assertFalse(epn.equals(epn2));
    	assertNotEquals(epn.hashCode(), epn2.hashCode());
    }

    @Test
    public void collectionsMatchOriginalPetriNet() {
        net = buildTestNet();
        epn = net.makeExecutablePetriNet();  
        assertThat(epn.getAnnotations()).hasSize(0); 
        assertThat(epn.getTokens()).hasSize(1); 
        assertThat(epn.getTransitions()).hasSize(2); 
        assertThat(epn.getInboundArcs()).hasSize(1); 
        assertThat(epn.getOutboundArcs()).hasSize(1); 
        assertThat(epn.getArcs()).hasSize(2); 
        assertThat(epn.getPlaces()).hasSize(2); 
        assertThat(epn.getRateParameters()).hasSize(0); 
    }
	protected PetriNet buildTestNet() {
		PetriNet net = APetriNet.with(AToken.called("Default").withColor(Color.BLACK)).and(APlace.withId("P0")).and(
                        APlace.withId("P1")).and(AnImmediateTransition.withId("T0")).and(
                        AnImmediateTransition.withId("T1")).and(
                        ANormalArc.withSource("P1").andTarget("T1")).andFinally(
                        ANormalArc.withSource("T0").andTarget("P0").with("#(P0)", "Default").token());
		return net; 
	}
    @Test
	public void componentsFound() throws Exception
	{
    	net = buildTestNet();
    	epn = net.makeExecutablePetriNet();
    	assertTrue(epn.containsComponent("T0")); 
    	assertFalse(epn.containsComponent("FRED")); 
    	
    	Transition t0 = epn.getComponent("T0", Transition.class);
    	Transition t1 = epn.getComponent("T1", Transition.class);
    	assertThat(epn.inboundArcs(t1)).hasSize(1); 
    	assertThat(epn.inboundArcs(t0)).hasSize(0); 
    	assertThat(epn.outboundArcs(t0)).hasSize(1); 
    	InboundArc arc = epn.getComponent("P1 TO T1", InboundArc.class);
    	assertTrue(epn.inboundArcs(t1).contains(arc));
    	//TODO outboundArcs(Place place) s
	}

}
