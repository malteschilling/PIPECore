package uk.ac.imperial.pipe.visitor;

import org.junit.Before;
import org.junit.Test;
import uk.ac.imperial.pipe.dsl.*;
import uk.ac.imperial.pipe.exceptions.PetriNetComponentNotFoundException;
import uk.ac.imperial.pipe.models.petrinet.ExecutablePetriNet;
import uk.ac.imperial.pipe.models.petrinet.InboundArc;
import uk.ac.imperial.pipe.models.petrinet.IncludeHierarchy;
import uk.ac.imperial.pipe.models.petrinet.Place;
import uk.ac.imperial.pipe.models.petrinet.Transition;
import uk.ac.imperial.pipe.models.petrinet.PetriNet;
import uk.ac.imperial.pipe.models.petrinet.name.NormalPetriNetName;
import uk.ac.imperial.pipe.models.petrinet.name.PetriNetFileName;

import java.awt.Color;
import java.io.File;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClonePetriNetTest {
    PetriNet oldPetriNet;
    PetriNet clonedPetriNet;

    @Before
    public void setUp() {
        buildSimpleNet();
    }
    //TODO clones InterfacePlaces (?)
	private void buildSimpleNet() {
		oldPetriNet = APetriNet.with(AToken.called("Default").withColor(Color.BLACK)).and(
                APlace.withId("P0").and(1, "Default").token()).and(APlace.withId("P1")).and(
                ATimedTransition.withId("T0")).and(ATimedTransition.withId("T1"))
                .and(ANormalArc.withSource("P0").andTarget("T0").with("1", "Default").token())
                .and(ANormalArc.withSource("T0").andTarget("P1").with("1", "Default").token())
                .and(ANormalArc.withSource("P1").andTarget("T1").with("1", "Default").token())
                .andFinally(ANormalArc.withSource("T1").andTarget("P0").with("1", "Default").token());
	}

    @Test
    public void cloneEquality() {
        oldPetriNet.setName(new NormalPetriNetName("Petri net 0"));
        clonedPetriNet = ClonePetriNet.clone(oldPetriNet);
        assertEquals(oldPetriNet, clonedPetriNet);
    }

    @Test
    public void clonePetriNetNoName() {
        clonedPetriNet = ClonePetriNet.clone(oldPetriNet);
        assertEquals(oldPetriNet, clonedPetriNet);
    }

    @Test
    public void cloneEqualityWithFileName() {
        oldPetriNet.setName(new PetriNetFileName(new File("Petri net 0")));
        clonedPetriNet = ClonePetriNet.clone(oldPetriNet);
        assertEquals(oldPetriNet, clonedPetriNet);
    }


    @Test
    public void clonesArcWithNewSourceAndTarget() throws PetriNetComponentNotFoundException {
        oldPetriNet.setName(new NormalPetriNetName("Petri net 0"));
        clonedPetriNet = ClonePetriNet.clone(oldPetriNet);
        InboundArc arc = clonedPetriNet.getComponent("P0 TO T0", InboundArc.class);
        Place clonedP0 = clonedPetriNet.getComponent("P0", Place.class);
        Transition clonedT0 = clonedPetriNet.getComponent("T0", Transition.class);
        assertTrue(arc.getSource() == clonedP0);
        assertTrue(arc.getTarget() == clonedT0);
    }
    @Test
	public void clonePetriNetToExecutablePetriNetReplacingExistingState() throws Exception {
    	buildSimpleNet(); 
    	oldPetriNet.setIncludesForTesting(new IncludeHierarchy(oldPetriNet, "root"));
    	ExecutablePetriNet executablePetriNet = new ExecutablePetriNet(oldPetriNet); 
    	ClonePetriNet.refreshFromIncludeHierarchy(executablePetriNet); 
    	assertEquals("root.P0", executablePetriNet.getComponent("root.P0", Place.class).getId()); 
    	assertEquals("root.T0", executablePetriNet.getComponent("root.T0", Transition.class).getId()); 
    	assertEquals("root.P0 TO T0", executablePetriNet.getComponent("root.P0 TO T0", InboundArc.class).getId()); 
	}
}
