package system;

import utils.Config;
import utils.Globals;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Agent {

    public Agent(World parentWorld, int id) {
        this.world = parentWorld;
        this.id = id;
        currentDoingServiceSize = 0;
        simConfigTraceable =
                simConfigShowWatchRadius =
                        simConfigShowRequestedService =
                                simConfigLinkToWatchedAgents = false;
    }

    //============================
    private boolean simConfigShowWatchRadius;
    private boolean simConfigLinkToWatchedAgents;
    private boolean simConfigTraceable;
    private boolean simConfigShowRequestedService;

    //============================

    private int id;

    private int loc_x;
    private int loc_y;

    private int velocity_x;
    private int velocity_y;

    //============================ processing variables
    private int currentDoingServiceSize;

    //============================
    private World world;
    //============================

    private AgentCapacity capacity;

    private AgentTrust trust;

    private AgentBehavior behavior;

    // agents that are watched by this agent
    private List<Agent> watchedAgents;

    // list of services that can request with this agent
    private List<ServiceType> requestingServiceTypes;

    // list of services that can done with this agent
    private List<ServiceType> doingServiceTypes;

    // list of services that requested by agent
    private List<Service> requestedServices;

    // list of services that done by agent
    private List<Service> doneServices;

    //============================//============================//============================


    public void init() {
        capacity = new AgentCapacity(this);
        trust = new AgentTrust(this, capacity.getHistoryCap(), capacity.getHistoryServiceRecordCap());
        behavior = new AgentBehavior();
        watchedAgents = new ArrayList<Agent>();
        loc_x = Globals.RANDOM.nextInt(world.getWidth());
        loc_y = Globals.RANDOM.nextInt(world.getHeight());

        //todo: [policy] : assigning requested services
        requestingServiceTypes = new ArrayList<ServiceType>();
        requestingServiceTypes.addAll(Arrays.asList(world.getServiceTypes()));

        //todo: [policy] : assigning possible services
        doingServiceTypes = new ArrayList<ServiceType>();
        doingServiceTypes.addAll(Arrays.asList(world.getServiceTypes()));

        requestedServices = new ArrayList<Service>();
        doneServices = new ArrayList<Service>();


    }

    public void setAsTraceable() {
        simConfigTraceable = true;
        simConfigShowWatchRadius = true;
        simConfigLinkToWatchedAgents = true;
        simConfigShowRequestedService = true;
    }

    public final void updateLocation() {

        updateVelocity();

        // System.out.println("  current loc : "+ loc_x+","+ loc_y);

        //todo: [policy] : Considering nonlinear movement of nodes
        loc_x += velocity_x;
        loc_y += velocity_y;

        if (loc_x > world.getWidth()) {
            loc_x = world.getWidth();
        }
        if (loc_y > world.getHeight()) {
            loc_y = world.getHeight();
        }
        if (loc_x < 0) {
            loc_x = 0;
        }
        if (loc_y < 0) {
            loc_y = 0;
        }
       /* System.out.println(world.getCurrentRunTime() + "]  ===============\nAgent [" + id + "]  velocity: "
                + velocity_x + "," + velocity_y
                + "  current loc : " + loc_x + "," + loc_y
        );
*/
        //System.out.println("  current loc : "+ loc_x+","+ loc_y);

    }

    private void updateVelocity() {

        //todo: [policy] : define all kinds of updating velocity

        velocity_x = Globals.RANDOM.nextInt(world.getMaxVelocityOfAgents_x()) - (world.getMaxVelocityOfAgents_x() / 2);
        velocity_y = Globals.RANDOM.nextInt(world.getMaxVelocityOfAgents_y()) - (world.getMaxVelocityOfAgents_y() / 2);

    }

    public void updateProfile() {

        behavior.updateHonestState();
    }

    public void resetParams() {
        currentDoingServiceSize = 0;
    }

    //============================ Watching

    public void updateWatchList() {
        watchedAgents.clear();

        Agent[] agents = world.getAgents();
        for (int i = 0; i < agents.length; i++) {
            if (watchedAgents.size() >= capacity.getWatchListCapacity()) {
                break;
            }
            if (canWatch(agents[i]) && agents[i].getId() != this.id) {
                watchedAgents.add(agents[i]);
            }
        }

    }


    public boolean canWatch(int x, int y) {
        return Math.sqrt(Math.pow((double) x - (double) loc_x, 2) + Math.pow((double) y - (double) loc_y, 2)) < (double) capacity.getWatchRadius();
    }


    public boolean canWatch(Agent agent) {
        return canWatch(agent.getLoc_x(), agent.getLoc_y());
    }

    //============================ Requesting

    public Service selectRequestedService() {

        ServiceType st = requestingServiceTypes.get(Globals.RANDOM.nextInt(requestingServiceTypes.size()));

        Service rs = new Service();
        rs.setRequester(this);
        rs.setServiceType(st);

        requestedServices.add(rs);

        return rs;
    }

    public Agent findDoerOfRequestedService(Service service) {
        //todo: [policy] : selecting doer of service

        int watchSize = watchedAgents.size();
        if (watchSize == 0) {
            return null;
        }

        trust.sortHistoryByTrustLevel();

        for (int index : trust.getHistoriesSortedIndex()) {
            AgentHistory history = trust.getHistories()[index];

            for (Agent watchedAgent : watchedAgents) {
                if (history != null
                        && history.getDoerAgent().getId() == watchedAgent.getId()  // if the watched agent is in history
                        //todo: [policy] : set threshold to trustee selection
                        && history.getEffectiveTrustLevel() > 0  // if the watched agent is not dishonest
                ) {

                    boolean serviceAcceptance = watchedAgent.canDoService(this, service);
                    if (serviceAcceptance) {
                        return watchedAgent;
                    }
                }
            }
        }

        //============================  Random selection if there is no agent in trust history

        int i;
        int tryCount = 0;

        while (++tryCount < 10) {
            i = Globals.RANDOM.nextInt(watchSize);
            if (trust.getTrustScore(watchedAgents.get(i)) >= 0) {
                return watchedAgents.get(i);
            }
        }

        return null;

    }


    //============================ Doing

    public boolean canDoService(Agent requester, Service service) {

        //todo: adding limit to count of concurrent doing service
        if (currentDoingServiceSize < capacity.getConcurrentDoingServiceCap() && watchedAgents.contains(requester)) {
            if (doingServiceTypes.contains(service.getServiceType())) {
                //todo: [policy] : bidirectional trust
                return true;
            }
        }
        return false;
    }

    public Service doService(Service service) {

        currentDoingServiceSize++;

        service.setDoer(this);
        /*Globals.random.nextFloat() * */
        float res = capacity.getCapPower() * (behavior.getIsHonest() ? 0.1f : -0.1f);
        service.setResult(res);

        doneServices.add(service);

        return service;

    }

    public void shareExperienceWith(Agent agent) {
        for (AgentHistory history : trust.getHistories()) {
            if (history != null) {
                for (ServiceMetaInfo info : history.getServiceMetaInfos()) {
                    if (info != null) {
                        // Only direct observation
                        if (info.getPublisher().getId() == this.id) {
                            agent.getTrust().recordExperience(info);
                        }
                    }
                }
            }
        }
    }

    //============================//============================//============================

    BasicStroke stroke3 = new BasicStroke(3);
    BasicStroke stroke1 = new BasicStroke(1);
    Font font = new Font("Tahoma", Font.PLAIN, 20);
    Color honestColor;

    private boolean isCapCandid = false;

    public void draw(Graphics2D g) {
        honestColor = behavior.getIsHonest() ? Color.GREEN : Color.RED;
        isCapCandid = Config.DRAWING_SHOW_POWERFUL_AGENTS_RADIUS && capacity.getCapPower() > Config.DRAWING_POWERFUL_AGENTS_THRESHOLD;
        // Drawing watch radius
        if (isCapCandid || isSimConfigShowWatchRadius()) {
            g.setColor(isCapCandid ? (behavior.getIsHonest() ? Color.GREEN : Color.RED) : simConfigTraceable ? Color.CYAN : Color.lightGray);
            g.drawOval(
                    loc_x - capacity.getWatchRadius(),
                    loc_y - capacity.getWatchRadius(),
                    capacity.getWatchRadius() * 2,
                    capacity.getWatchRadius() * 2
            );
        }

        // Drawing links to watched agents
        if (simConfigLinkToWatchedAgents) {
            g.setColor(Color.GRAY);
            for (Agent wa : watchedAgents) {
                g.drawLine(loc_x, loc_y, wa.getLoc_x(), wa.getLoc_y());
            }
        }

        if (simConfigShowRequestedService && requestedServices.size() > 0) {
            Service service = requestedServices.get(requestedServices.size() - 1);
            if (service != null) {
                g.setStroke(stroke3);
                if (service.getDoer() != null) {
                    g.setColor(service.getDoer().getBehavior().getIsHonest() ? Color.GREEN : Color.RED);
                    g.drawLine(loc_x, loc_y, service.getDoer().getLoc_x(), service.getDoer().getLoc_y());
                } else {
                    g.setColor(Color.GREEN);
                    g.drawLine(loc_x, loc_y, loc_x + 40, loc_y + 40);
                }
                g.setStroke(stroke1);
            }
        }

        // Set color of node with honest strategy
        g.setColor(honestColor);
        // Draw node according to it's capacity
        int agentBound = capacity.getCapPower() / 5;
        g.fillOval(loc_x - agentBound, loc_y - agentBound, agentBound * 2, agentBound * 2);


        // Drawing id of the node
        g.setFont(font);

        // Set color of node with honest strategy
        g.drawString(id + "", loc_x, loc_y + capacity.getCapPower() + 10);

    }
    //============================//============================//============================


    @Override
    public String toString() {
        return "\nAgent{" +
                "\n\tsimConfigShowWatchRadius=" + simConfigShowWatchRadius +
                ", \n\tsimConfigLinkToWatchedAgents=" + simConfigLinkToWatchedAgents +
                ", \n\tsimConfigTraceable=" + simConfigTraceable +
                ", \n\tsimConfigShowRequestedService=" + simConfigShowRequestedService +
                ", \n\tid=" + id +
                ", \n\tloc_x=" + loc_x +
                ", \n\tloc_y=" + loc_y +
                ", \n\tvelocity_x=" + velocity_x +
                ", \n\tvelocity_y=" + velocity_y +
                ", \n\tcurrentDoingServiceSize=" + currentDoingServiceSize +
                ", \n\tcap=" + capacity.toString() +
                ", \n\ttrust=" + trust.toString() +
                ", \n\tprofile=" + behavior.toString() +
                '}';
    }

    public int getId() {
        return id;
    }

    public int getLoc_x() {
        return loc_x;
    }

    public int getLoc_y() {
        return loc_y;
    }

    public int getVelocity_x() {
        return velocity_x;
    }

    public int getVelocity_y() {
        return velocity_y;
    }

    public World getWorld() {
        return world;
    }

    public AgentCapacity getCapacity() {
        return capacity;
    }

    public List<Agent> getWatchedAgents() {
        return watchedAgents;
    }

    public boolean isSimConfigLinkToWatchedAgents() {
        return simConfigLinkToWatchedAgents;
    }

    public boolean isSimConfigShowWatchRadius() {
        return simConfigShowWatchRadius;
    }

    public boolean isSimConfigTraceable() {
        return simConfigTraceable;
    }

    public List<Service> getDoneServices() {
        return doneServices;
    }

    public void setDoneServices(List<Service> doneServices) {
        this.doneServices = doneServices;
    }

    public List<Service> getRequestedServices() {
        return requestedServices;
    }

    public void setRequestedServices(List<Service> requestedServices) {
        this.requestedServices = requestedServices;
    }


    public List<ServiceType> getRequestingServiceTypes() {
        return requestingServiceTypes;
    }

    public void setRequestingServiceTypes(List<ServiceType> requestingServiceTypes) {
        this.requestingServiceTypes = requestingServiceTypes;
    }

    public List<ServiceType> getDoingServiceTypes() {
        return doingServiceTypes;
    }

    public void setDoingServiceTypes(List<ServiceType> doingServiceTypes) {
        this.doingServiceTypes = doingServiceTypes;
    }

    public AgentBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(AgentBehavior behavior) {
        this.behavior = behavior;
    }

    public AgentTrust getTrust() {
        return trust;
    }

}
