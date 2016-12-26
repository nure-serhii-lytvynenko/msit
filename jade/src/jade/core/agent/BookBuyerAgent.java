package jade.core.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olena_Batura on 31.10.2016.
 */
public class BookBuyerAgent extends Agent {

    private String targetBookTitle;

    private List<AID> sellerAgent = new ArrayList<AID>() {{
        add(new AID("seller1", AID.ISLOCALNAME));
        add(new AID("seller2", AID.ISLOCALNAME));
        add(new AID("seller3", AID.ISLOCALNAME));
    }};

    protected void setUp() {
        System.out.format("Hello! Buyer-agent {0} is ready!", getAID().getName());
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            targetBookTitle = (String) args[0];
            System.out.format("Target book is %s", targetBookTitle);
            addBehaviour(new TickerBehaviour(this, 60000) {
                protected void onTick() {
                    System.out.format("Trying to buy %s", targetBookTitle);
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("book-selling");
                    template.addServices(sd);
                    try {
                        ArrayList<DFAgentDescription> results = DFService.search(myAgent, template);
                        System.out.println("Found the following seller agents:");
                        for (AID seller : sellerAgent) {
                            for (DFAgentDescription result : results) {
                                seller = result.getName();
                                System.out.println(seller.getName());
                            }
                        }
                    } catch (FIPAException fe) {
                        fe.printStackTrace();
                    }
                    myAgent.addBehaviour(new RequestPerformer());
                }
            });
        } else {
            System.out.println("No target book title specified");
            doDelete();
        }
    }

    private class RequestPerformer extends Behaviour {
        private AID bestSeller;
        private int bestPrice;
        private int repliesCnt = 0;
        private MessageTemplate mt;
        private int step = 0;

        @Override
        public void action() {
            switch (step) {
                case 0:
                    ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                    for (AID seller : sellerAgent) {
                        cfp.addReceiver(seller);
                    }
                    cfp.setContent(targetBookTitle);
                    cfp.setConversationId("book-trade");
                    cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
                    myAgent.send(cfp);
                    mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
                            MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                    step = 1;
                    break;
                case 1:
                    ACLMessage reply = myAgent.receive(mt);
                    if (reply != null) {
                        if (reply.getPerformative() == ACLMessage.PROPOSE) {
                            int price = Integer.parseInt(reply.getContent());
                            if (bestSeller == null || price < bestPrice) {
                                bestPrice = price;
                                bestSeller = reply.getSender();
                            }
                        }
                        repliesCnt++;
                        if (repliesCnt >= sellerAgent.size()) {
                            step = 2;
                        }
                    } else {
                        block();
                    }
                    break;
                case 2:
                    ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    order.addReceiver(bestSeller);
                    order.setContent(targetBookTitle);
                    order.setConversationId("book-trade");
                    order.setReplyWith("order" + System.currentTimeMillis());
                    myAgent.send(order);
                    mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
                            MessageTemplate.MatchInReplyTo(order.getReplyWith()));
                    step = 3;
                    break;
                case 3:
                    reply = myAgent.receive(mt);
                    if (reply != null) {
                        if (reply.getPerformative() == ACLMessage.INFORM) {
                            System.out.println(targetBookTitle + " successfully purchased from agent " + reply.getSender().getName());
                            System.out.println("Price = " + bestPrice);
                            myAgent.doDelete();
                        } else {
                            System.out.println("Attempt failed: requested book already sold.");
                        }

                        step = 4;
                    } else {
                        block();
                    }
                    break;
            }
        }

        @Override
        public boolean done() {
            if (step == 2 && bestSeller == null) {
                System.out.println("Attempt failed: " + targetBookTitle + " not available for sale");
            }
            return ((step == 2 && bestSeller == null) || step == 4);
        }
    }
}
