package uk.ac.imperial.pipe.io.adapters.modelAdapter;

import uk.ac.imperial.pipe.exceptions.PetriNetComponentException;
import uk.ac.imperial.pipe.io.adapters.model.AdaptedPetriNet;
import uk.ac.imperial.pipe.models.petrinet.PetriNetComponent;
import uk.ac.imperial.pipe.models.petrinet.PetriNet;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Marshals a Petri net into the verbose format needed for PNML
 */
public final class PetriNetAdapter extends XmlAdapter<AdaptedPetriNet, PetriNet> {
    /**
     *
     * @param v
     * @return unmarshaled Petri net
     * @throws PetriNetComponentException
     */
    @Override
    public PetriNet unmarshal(AdaptedPetriNet v) throws PetriNetComponentException {
        PetriNet petriNet = new PetriNet();
        addToPetriNet(v.tokens, petriNet);
        addToPetriNet(v.annotations, petriNet);
        addToPetriNet(v.rateParameters, petriNet);
        addToPetriNet(v.places, petriNet);
        addToPetriNet(v.transitions, petriNet);
        addToPetriNet(v.arcs, petriNet);
        return petriNet;
    }

    /**
     *
     * @param v
     * @return marshaled Petri net
     */
    @Override
    public AdaptedPetriNet marshal(PetriNet v) {
        AdaptedPetriNet petriNet = new AdaptedPetriNet();
        petriNet.tokens = v.getTokens();
        petriNet.annotations = v.getAnnotations();
        petriNet.rateParameters = v.getRateParameters();
        petriNet.places = v.getPlaces();
        petriNet.transitions = v.getTransitions();
        petriNet.arcs = v.getArcs();
        return petriNet;
    }

    /**
     * Adds components to the Petri net
     * @param components
     * @param petriNet
     * @throws PetriNetComponentException
     */
    private void addToPetriNet(Iterable<? extends PetriNetComponent> components, PetriNet petriNet)
            throws PetriNetComponentException {
        if (components != null) {
            for (PetriNetComponent component : components) {
                petriNet.add(component);
            }
        }
    }
}
