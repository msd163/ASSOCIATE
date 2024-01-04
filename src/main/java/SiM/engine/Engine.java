package SiM.engine;

import SiM.monitor.IntDrawingWindowRunner;
import SiM.profiler.Profiler;
import SiM.report.ImageBuilder;
import SGM.config.PopulationBunch;
import SiM.profiler.config.TrustConfig;
import SiM.statistics.WorldStatistics;
import WSM.World;
import core.utils.*;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Engine {

    private IntDrawingWindowRunner intDrawingWindowRunner;

    Profiler profiler = new Profiler(this);

    private World[] worlds;

    public World[] getWorlds() {
        return worlds;
    }

    public void setWorlds(World[] worlds) {
        this.worlds = worlds;
    }

    public TrustConfig getTrustConfigBunch() {
        return profiler.getTrustConfig();
    }

    //============================//============================

    long startTime;
    long endTime;

    //============================//============================//============================


    public void simulate() throws Exception {

        MakeSound sound = new MakeSound();

        // playing start sound
        Thread.sleep(900);
        sound.playSound(ProjectPath.instance().alertStartFile());

        try {
            profiler.init();

            if (Config.INT_DRAWING_SHOW_ENABLED()) {
                intDrawingWindowRunner = new IntDrawingWindowRunner(worlds, getTrustConfigBunch());
                intDrawingWindowRunner.initDrawingWindows();
                intDrawingWindowRunner.start();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        startTime = new Date().getTime();

        for (World world : worlds) {
            if (Globals.SIMULATION_TIMER > 0) {
                profiler.reloadEnvironmentFromFile();
            }
            Globals.reset();
            world.init(profiler.getLoadedSocietyFromJson());
            world.run();
            Globals.SIMULATION_TIMER++;
            if (Globals.SIMULATION_ROUND <= Globals.SIMULATION_TIMER) {
                break;
            }
            world.destroy();
            OutLog____.pl("s");
            sound.playSound(ProjectPath.instance().alertMidFile());

        }

        Globals.SIMULATION_TIMER--;
        if (Config.STATISTICS_IS_GENERATE && Config.STATISTICS_IS_SAVE_IMAGE && Config.INT_DRAWING_SHOW_ENABLED()) {
            System.out.print("Simulator images are generating...");
            new ImageBuilder().generateSimulationImages(
                    intDrawingWindowRunner.getIntTravelStatsLinearDrawingWindow(),
                    intDrawingWindowRunner.getIntTrustAnalysisLinearDrawingWindow(),
                    intDrawingWindowRunner.getIntTrustStatsLinearDrawingWindow()
            );
            System.out.print("Simulator images are generated.");
        }
        //============================//============================ Closing statistics file
        if (Config.STATISTICS_IS_GENERATE) {
            Globals.statsEnvGenerator.close();
            Globals.statsTrustGenerator.close();
        }

        System.out.print("\n");
        System.out.print("Simulator run is finished.");

        endTime = new Date().getTime();

        timeReport();

        improvementAnalysis();

        sound.playSound(ProjectPath.instance().alertEndFile());
        Thread.sleep(200);
        sound.playSound(ProjectPath.instance().alertEndFile());
        Thread.sleep(200);
        sound.playSound(ProjectPath.instance().alertEndFile());
        Thread.sleep(200);

    }

    private void timeReport() {
        System.out.println("\n  > simulation duration: [" + new DecimalFormat().format((endTime - startTime) / 1000) + "] seconds");
    }


    //============================//============================//============================

    public void improvementAnalysis() {

        //============================//============================ 1
        World baseWorld = worlds[0];
        float coeff = Config.IMPROVEMENT_ANALYSIS_COEFFICIENT;

        int overallAgentInTarget = 0;
        int overallAgentInPitfall = 0;
        int overallTrustAccuracy = 0;
        int overallTrustSensitivity = 0;
        int overallTrustSpecificity = 0;
        int overallTrustToHonest = 0;
        int overallTrustToHypocrite = 0;
        int overallHonestCollab = 0;
        int overallHypocriteCollab = 0;
        int overallResistance = 0;
        int overallIgnorePos = 0;
        int overallIgnoreNeg = 0;

        int[] diffAgentInTarget = new int[worlds.length];
        int[] diffAgentInPitfall = new int[worlds.length];
        int[] diffTrustAccuracy = new int[worlds.length];
        int[] diffTrustSensitivity = new int[worlds.length];
        int[] diffTrustSpecificity = new int[worlds.length];
        int[] diffTrustToHonest = new int[worlds.length];
        int[] diffTrustToHypocrite = new int[worlds.length];
        int[] diffHonestCollab = new int[worlds.length];
        int[] diffHypocriteCollab = new int[worlds.length];
        int[] diffResistance = new int[worlds.length];
        int[] diffIgnorePos = new int[worlds.length];
        int[] diffIgnoreNeg = new int[worlds.length];


        Arrays.fill(diffAgentInTarget, 0);
        Arrays.fill(diffAgentInPitfall, 0);
        Arrays.fill(diffTrustAccuracy, 0);
        Arrays.fill(diffTrustSensitivity, 0);
        Arrays.fill(diffTrustSpecificity, 0);
        Arrays.fill(diffTrustToHonest, 0);
        Arrays.fill(diffTrustToHypocrite, 0);
        Arrays.fill(diffHonestCollab, 0);
        Arrays.fill(diffHypocriteCollab, 0);
        Arrays.fill(diffResistance, 0);
        Arrays.fill(diffIgnorePos, 0);
        Arrays.fill(diffIgnoreNeg, 0);


        //============================//============================ 2
        for (int worldIdx = 1, worldsLength = worlds.length; worldIdx < worldsLength; worldIdx++) {
            World thisWorld = worlds[worldIdx];
            for (int runIdx = 0, len = Config.WORLD_LIFE_TIME; runIdx < len; runIdx++) {
                float effect = 1 + (coeff * runIdx);

                WorldStatistics thisWorldStat = thisWorld.getWdStatistics()[runIdx];
                WorldStatistics baseWorldStat = baseWorld.getWdStatistics()[runIdx];

                diffAgentInTarget[worldIdx] += (int) (effect *
                        (thisWorldStat.getTimedAvgAgentTarget() - baseWorldStat.getTimedAvgAgentTarget())
                );

                diffAgentInPitfall[worldIdx] += (int) (-effect *
                        (thisWorldStat.getTimedAvgAgentInPitfall() - baseWorldStat.getTimedAvgAgentInPitfall())
                );

                diffTrustAccuracy[worldIdx] += (int) (effect *
                        (thisWorldStat.getAllTrustAccuracyI100() - baseWorldStat.getAllTrustAccuracyI100())
                );

                diffTrustSensitivity[worldIdx] += (int) (effect *
                        (thisWorldStat.getAllTrustSensitivityI100() - baseWorldStat.getAllTrustSensitivityI100())
                );

                diffTrustSpecificity[worldIdx] += (int) (effect *
                        (thisWorldStat.getAllTrustSpecificityI100() - baseWorldStat.getAllTrustSpecificityI100())
                );

                diffTrustToHonest[worldIdx] += (int) (effect *
                        (thisWorldStat.getTimeAvgTrustToHonest() - baseWorldStat.getTimeAvgTrustToHonest())
                );

                diffTrustToHypocrite[worldIdx] += (int) (-effect *
                        (thisWorldStat.getTimeAvgTrustToHypocrite() - baseWorldStat.getTimeAvgTrustToHypocrite())
                );

                diffHonestCollab[worldIdx] += (int) (effect *
                        (thisWorldStat.getStatisticsCollab().getAvgHonestTrustPerCollab100() - baseWorldStat.getStatisticsCollab().getAvgHonestTrustPerCollab100())
                );

                diffHypocriteCollab[worldIdx] += (int) (-effect *
                        (thisWorldStat.getStatisticsCollab().getAvgHypocriteTrustPerCollab100() - baseWorldStat.getStatisticsCollab().getAvgHypocriteTrustPerCollab100())
                );

                diffResistance[worldIdx] += (int) (effect *
                        (thisWorldStat.getStatisticsHypo().getAvgHypoResistanceOfAllNumberAgainstAll() - baseWorldStat.getStatisticsHypo().getAvgHypoResistanceOfAllNumberAgainstAll())
                );

                diffIgnorePos[worldIdx] += (int) (effect *
                        (thisWorldStat.getStatisticsHypo().getAvgHypoIgnoredPos() - baseWorldStat.getStatisticsHypo().getAvgHypoIgnoredPos())
                );

                diffIgnoreNeg[worldIdx] += (int) (effect *
                        (thisWorldStat.getStatisticsHypo().getAvgHypoIgnoredNeg() - baseWorldStat.getStatisticsHypo().getAvgHypoIgnoredNeg())
                );

            }
        }

        //============================//============================ 3

        System.out.println("\n==================================================");
        System.out.println("Improvements Analysis\n");


        overallAgentInTarget = printImprovementInfo("AgentInTarget", diffAgentInTarget);
        overallAgentInPitfall = printImprovementInfo("AgentInPitfall", diffAgentInPitfall);
        overallTrustAccuracy = printImprovementInfo("TrustAccuracy", diffTrustAccuracy);
        overallTrustSensitivity = printImprovementInfo("TrustSensitivity", diffTrustSensitivity);
        overallTrustSpecificity = printImprovementInfo("TrustSpecificity", diffTrustSpecificity);
        overallTrustToHonest = printImprovementInfo("TrustToHonest", diffTrustToHonest);
        overallTrustToHypocrite = printImprovementInfo("TrustToHypocrite", diffTrustToHypocrite);
        overallHonestCollab = printImprovementInfo("HonestCollab", diffHonestCollab);
        overallHypocriteCollab = printImprovementInfo("HypocriteCollab", diffHypocriteCollab);
        overallResistance = printImprovementInfo("Resistance", diffResistance);
        overallIgnorePos = printImprovementInfo("IgnorePos", diffIgnorePos);
        overallIgnoreNeg = printImprovementInfo("IgnoreNeg", diffIgnoreNeg);

        System.out.println("\n ------------------------------------");

        ArrayList<PopulationBunch> bunches = profiler.getLoadedSocietyProfileFromJson().getBunches();

        for (PopulationBunch bunch : bunches) {
            System.out.print(bunch.getBehavior().getHonestPercent() + ",");
            System.out.print(bunch.getBehavior().getAdversaryPercent() + ",");
            System.out.print(bunch.getBehavior().getMischiefPercent() + ",");
            System.out.print(bunch.getBehavior().getHypocriteBehavior().getHonestPercent() + ",");
            System.out.print(bunch.getBehavior().getHypocriteBehavior().getAdversaryPercent() + ",");
            System.out.print(bunch.getBehavior().getHypocriteBehavior().getMischiefPercent() + ",");
            System.out.print(bunch.getBunchCount() + "\n");
        }

        printImprovementInfoBrief(diffAgentInTarget);
        printImprovementInfoBrief(diffAgentInPitfall);
        printImprovementInfoBrief(diffTrustAccuracy);
        printImprovementInfoBrief(diffTrustSensitivity);
        printImprovementInfoBrief(diffTrustSpecificity);
        printImprovementInfoBrief(diffTrustToHonest);
        printImprovementInfoBrief(diffTrustToHypocrite);
        printImprovementInfoBrief(diffHonestCollab);
        printImprovementInfoBrief(diffHypocriteCollab);
        printImprovementInfoBrief(diffResistance);
        printImprovementInfoBrief(diffIgnorePos);
        printImprovementInfoBrief(diffIgnoreNeg);


        System.out.println("\n" +
                overallAgentInTarget + "," +
                overallAgentInPitfall + "," +
                overallTrustAccuracy + "," +
                overallTrustSensitivity + "," +
                overallTrustSpecificity + "," +
                overallTrustToHonest + "," +
                overallTrustToHypocrite + "," +
                overallHonestCollab + "," +
                overallHypocriteCollab + "," +
                overallResistance + "," +
                overallIgnorePos + "," +
                overallIgnoreNeg + "," +
                Globals.STATS_FILE_NAME + "," +
                ProjectPath.instance().getTarget()
        );


        //============================//============================ 4

    }

    private int printImprovementInfo(String title, int[] diff) {
        int overall = 0;
        System.out.println("\n" + title);
        for (int i = 0; i < worlds.length; i++) {
            overall += diff[i];
            System.out.print(diff[i] + " > ");
        }
        System.out.println("|>> [" + overall + "]");

        return overall;

    }

    private void printImprovementInfoBrief(int[] diff) {
        for (int i = 0; i < worlds.length; i++) {
            System.out.print((i > 0 ? "|" : "") + diff[i]);
        }
        System.out.print(",");
    }


}
