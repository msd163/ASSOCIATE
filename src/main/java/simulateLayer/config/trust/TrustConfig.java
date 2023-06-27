package simulateLayer.config.trust;

public class TrustConfig {

    private int configCount;

    private int currentIndex = 0;

    private TrustConfigItem configs[];


    public int getConfigCount() {
        return configCount;
    }

    public int getValidConfigCount() {
        return configCount <= 0 ? configs.length : configCount;
    }

    public TrustConfigItem getNextConfig() {
        int validConfigCount = configCount <= 0 ? configs.length : configCount;
        if (currentIndex >= validConfigCount) {
            currentIndex = 0;
        }
        return configs[currentIndex++];
    }

    public TrustConfigItem getByIndex(int index) {
        return configs[index];
    }
}
