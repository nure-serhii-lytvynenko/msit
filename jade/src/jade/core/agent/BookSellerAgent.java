package jade.core.agent;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.sam.Alphabet;
import jade.domain.introspection.ACLMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Olena_Batura on 10.11.2016.
 */
public class BookSellerAgent extends Agent {
    private Map<Integer,Map<Alphabet, List<String>>> catalogue;
    private BookSellerGui myGui;

    protected void setup(){
        catalogue = new HashMap<>();
        myGui = new BookSellerGui();
        myGui.show();
        addBehaviour(new OfferRequestsServer());
        addBehaviour(new PurchaseOrdersServer());
        myGui.dispose();
        System.out.format("Seller-agent %s terminating.", getAID().getName());
    }

    public Map<Alphabet, List<String>> addBook(final Alphabet letter, final List<String> title){
        Map<Alphabet, List<String>> map = new HashMap<>();
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                map.put(letter, title);
            }
        });
        return map;
    }

    public void updateCatalogur(Map<Alphabet, List<String>> book, final int price){
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                catalogue.put(price, book);
            }
        });
    }


    private class OfferRequestsServer extends Behaviour {
        @Override
        public void action() {
            ACLMessage message = myAgent.receive();
            if(message!=null){
              //TODO implementation
            }else {
                block();
            }
        }

        @Override
        public boolean done() {
            return false;
        }
    }

    private class PurchaseOrdersServer extends Behaviour {
        @Override
        public void action() {

        }

        @Override
        public boolean done() {
            return false;
        }
    }
}
