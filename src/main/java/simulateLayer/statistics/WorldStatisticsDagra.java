package simulateLayer.statistics;

public class WorldStatisticsDagra {

    public WorldStatisticsDagra(WorldStatisticsDagra xPrevStatisticsDagra, WorldStatisticsDagra prevStatisticsDagra) {

        this.xPrevStatDagra = xPrevStatisticsDagra;
        this.prevStatDagra = prevStatisticsDagra;

        noContractCount
                = expiredContractCount
                = requestNewCount
                = requestSingingCount
                = requestVerifyingCount
                = acceptNewCount
                = acceptSigningCount
                = acceptVerifyingCount
                = acceptAcceptCount
                = 0;

    }

    public void init(WorldStatisticsDagra hypo) {
        noContractCount = hypo.noContractCount;
        expiredContractCount = hypo.expiredContractCount;
        requestNewCount = hypo.requestNewCount;
        requestSingingCount = hypo.requestSingingCount;
        requestVerifyingCount = hypo.requestVerifyingCount;
        acceptNewCount = hypo.acceptNewCount;
        acceptSigningCount = hypo.acceptSigningCount;
        acceptVerifyingCount = hypo.acceptVerifyingCount;
        acceptAcceptCount = hypo.acceptAcceptCount;
    }

    private final WorldStatisticsDagra xPrevStatDagra;
    private final WorldStatisticsDagra prevStatDagra;

    private int noContractCount;
    private int expiredContractCount;

    private int requestNewCount;
    private int requestSingingCount;
    private int requestVerifyingCount;

    private int acceptNewCount;
    private int acceptSigningCount;
    private int acceptVerifyingCount;
    private int acceptAcceptCount;

    private int worldTime;

    //============================//============================
    private int calcAverage(int coeff, int currentVal, Integer xPrevVal, int average) {

        if (xPrevVal == null) {
            return (int) ((float) currentVal / (worldTime == 0 ? 1 : worldTime));
        } else {
            return (int) ((coeff) * (float) (currentVal - xPrevVal) / average);
        }
    }

    private int calcAverage(int currentVal, Integer xPrevVal, int average) {
        return calcAverage(1, currentVal, xPrevVal, average);
    }

    //============================//============================//============================

    public void setWorldTime(int worldTime) {
        this.worldTime = worldTime;
    }

    //============================//============================//============================


    //============================


    //============================//============================

    public void add_noContractCount() {
        noContractCount++;
    }

    public void add_expiredContractCount() {
        expiredContractCount++;
    }

    public void add_requestNewCount() {
        requestNewCount++;
    }

    public void add_requestSingingCount() {
        requestSingingCount++;
    }

    public void add_requestVerifyingCount() {
        requestVerifyingCount++;
    }

    public void add_acceptNewCount() {
        acceptNewCount++;
    }

    public void add_acceptSigningCount() {
        acceptSigningCount++;
    }

    public void add_acceptVerifyingCount() {
        acceptVerifyingCount++;
    }

    public void add_acceptAcceptCount() {
        acceptAcceptCount++;
    }


    //============================//============================


    public int getNoContractCount() {
        if (prevStatDagra == null) {
            return noContractCount;
        }
        return noContractCount - prevStatDagra.noContractCount;
    }

    public int getExpiredContractCount() {
        if (prevStatDagra == null) {
            return expiredContractCount;
        }
        return expiredContractCount - prevStatDagra.expiredContractCount;
    }

    public int getRequestNewCount() {
        if (prevStatDagra == null) {
            return requestNewCount;
        }
        return requestNewCount - prevStatDagra.requestNewCount;
    }

    public int getRequestSingingCount() {
        if (prevStatDagra == null) {
            return requestSingingCount;
        }
        return requestSingingCount - prevStatDagra.requestSingingCount;
    }

    public int getRequestVerifyingCount() {
        if (prevStatDagra == null) {
            return requestVerifyingCount;
        }
        return requestVerifyingCount - prevStatDagra.requestVerifyingCount;
    }

    public int getAcceptNewCount() {
        if (prevStatDagra == null) {
            return acceptNewCount;
        }
        return acceptNewCount - prevStatDagra.acceptNewCount;
    }

    public int getAcceptSigningCount() {
        if (prevStatDagra == null) {
            return acceptSigningCount;
        }
        return acceptSigningCount - prevStatDagra.acceptSigningCount;
    }

    public int getAcceptVerifyingCount() {
        if (prevStatDagra == null) {
            return acceptVerifyingCount;
        }
        return acceptVerifyingCount - prevStatDagra.acceptVerifyingCount;
    }

    public int getAcceptAcceptCount() {
        if (prevStatDagra == null) {
            return acceptAcceptCount;
        }
        return acceptAcceptCount - prevStatDagra.acceptAcceptCount;
    }
}
