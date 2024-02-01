package SiM.monitor.integrated;

import SiM.monitor.DrawingWindow;
import core.enums.TtSimulationMode;
import core.enums.TtTrustMethodology;
import SiM.profiler.config.TrustConfig;
import SiM.statistics.EpisodeStatistics;
import SiM.statistics.WorldStatistics;
import WSM.World;
import core.utils.Config;
import core.utils.Globals;
import core.utils.Point;

import java.awt.*;

public class IntTravelStatsLinearDrawingWindow extends DrawingWindow {

    private TrustConfig configBunch;

    //============================//============================  panning params

    public IntTravelStatsLinearDrawingWindow(World worlds[], TrustConfig configBunch) {
        super(worlds.length);
        this.worlds = worlds;
        this.configBunch = configBunch;
        this.prevPoints = new Point[3];
        for (int i = 0; i < this.prevPoints.length; i++) {
            prevPoints[i] = new Point(0, 0);
        }
        headerTitle = "Integrated Travel Statistics Linear Chart [#Worlds: " + worlds.length + "]";
        setName("i_tvl_stt");
        //axisYScale = 1;
    }

    @Override
    public void paint(Graphics gr) {

        if (!mainPaint(gr, headerTitle, null)) {
            return;
        }

        int ittAgentsInTarget = worlds[simulationTimer].getWdStatistics()[worldTimer].getIttAgentsInTarget();
        printStatsInfo(1, "Agents In Targets", ittAgentsInTarget, "%" + 100 * (float) ittAgentsInTarget / worlds[simulationTimer].getAgentsCount(), Globals.Color$.$curve_1);

        int ittAgentsInPitfall = worlds[simulationTimer].getWdStatistics()[worldTimer].getIttAgentsInPitfall();
        printStatsInfo(2, "Agents In Pitfall", ittAgentsInPitfall, "%" + 100 * (float) ittAgentsInPitfall / worlds[simulationTimer].getAgentsCount(), Globals.Color$.$curve_2);

        int ittRandom = worlds[simulationTimer].getWdStatistics()[worldTimer].getIttRandomTravelToNeighbors();
        printStatsInfo(3, "Random Travel", ittRandom, "%" + 100 * (float) ittRandom / worlds[simulationTimer].getAgentsCount(), Globals.Color$.$curve_3);


        printStatsInfo(4, "Avg(" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") Agents In Targets", worlds[simulationTimer].getWdStatistics()[worldTimer].getTimedAvgAgentTarget(), Globals.Color$.$curve_1);
        printStatsInfo(5, "Avg(" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") Agents In Pitfall", worlds[simulationTimer].getWdStatistics()[worldTimer].getTimedAvgAgentInPitfall(), Globals.Color$.$curve_2);
        printStatsInfo(6, "Avg(" + Config.STATISTICS_AVERAGE_TIME_WINDOW + ") Random Travel", worlds[simulationTimer].getWdStatistics()[worldTimer].getIttRandomTravelToNeighbors(), Globals.Color$.$curve_3);

        //============================//============================ INFO
        dynamicHeight = 20 + dynamicHeight;
        if (isShowSimInfo) {
            for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {

                World world = worlds[j];

                if (j > Globals.SIMULATION_TIMER || world == null) {
                    break;
                }

                //============================
                dynamicHeight += 40;
                g.setColor(Globals.Color$.$simTitle);
                g.drawString("Sim " + (j + 1) + " |", 80, dynamicHeight);

                if (showWorldsFlag[j]) {
                    if (showLineChartsFlag[0]) {
                        //============================
                        drawSymbolOnCurve(200, dynamicHeight, Globals.Color$.$curve_1, j, 20, -1);
                        g.drawString("AgentsInTarget", 220, dynamicHeight);
                    }
                    if (showLineChartsFlag[1]) {
                        //============================
                        drawSymbolOnCurve(500, dynamicHeight, Globals.Color$.$curve_2, j, 20, -1);
                        g.drawString("AgentsInPitfall", 520, dynamicHeight);
                    }
                    if (showLineChartsFlag[2]) {
                        //============================
                        drawSymbolOnCurve(800, dynamicHeight, Globals.Color$.$curve_3, j, 20, -1);
                        g.drawString("RandomTravel", 820, dynamicHeight);
                    }
                }
                //============================
                g.setColor(Globals.Color$.$configTitle);
                g.drawString("|>  " + worlds[j].getSimulationConfigInfo(1), 1100, dynamicHeight);
                TtTrustMethodology ttMethod = worlds[j].getSimulationConfig().getTtMethod();
                if (ttMethod == TtTrustMethodology.TrustMode_ShortPath || ttMethod == TtTrustMethodology.TrustMode_RandomPath) {
                    dynamicHeight += 40;
                    g.drawString("    > " + worlds[j].getSimulationConfigInfo(2), 1100, dynamicHeight);
                    dynamicHeight += 40;
                    g.drawString("    > " + worlds[j].getSimulationConfigInfo(3), 1100, dynamicHeight);
                    dynamicHeight += 40;
                    g.drawString("    > " + worlds[j].getSimulationConfigInfo(4), 1100, dynamicHeight);
                }
                //============================
            }
        }

        //drawInfoSplitterLine();

        //============================//============================//============================ Diagram drawing

        reverseNormalizeCoordination();

        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        if (showChartsFlag[0]) {
            prepareChartPosition(0);


            for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {

                if (!showWorldsFlag[j]) {
                    continue;
                }

                World world = worlds[j];

                if (j > Globals.SIMULATION_TIMER || world == null) {
                    break;
                }

                loAxisX = j;

                worldTimer = j < Globals.SIMULATION_TIMER ? Config.WORLD_LIFE_TIME : Globals.WORLD_TIMER;

                WorldStatistics[] statistics = world.getWdStatistics();

                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getIttAgentsInTarget());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getIttAgentsInPitfall());
                maxAxisY[0] = Math.max(maxAxisY[0], statistics[worldTimer - 1].getIttRandomTravelToNeighbors());

                for (int sttIdx = 0, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {
                    WorldStatistics stat = statistics[sttIdx];

                    int curveIndex = -1;

                    if (sttIdx == 0 || stat.getEpisode() != statistics[sttIdx - 1].getEpisode()) {
                        loAxisX += _hs;
                        prevPoints[0].y = (int) (0.1 * _vs * stat.getIttAgentsInTarget());
                        prevPoints[1].y = (int) (0.1 * _vs * stat.getIttAgentsInPitfall());
                        prevPoints[2].y = (int) (0.1 * _vs * stat.getIttRandomTravelToNeighbors());
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;

                    } else {

                        prevPoints[0].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getIttAgentsInTarget());
                        prevPoints[1].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getIttAgentsInPitfall());
                        prevPoints[2].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getIttRandomTravelToNeighbors());
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;
                        loAxisX += _hs;
                    }

                    if (showLineChartsFlag[0]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getIttAgentsInTarget()), Globals.Color$.$curve_1, j, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getIttAgentsInTarget(), sttIdx, ++curveIndex);
                    }
                    if (showLineChartsFlag[1]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getIttAgentsInPitfall()), Globals.Color$.$curve_2, j, sttIdx);
                        drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getIttAgentsInPitfall(), sttIdx, ++curveIndex);
                    }
                    if (showLineChartsFlag[2]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getIttRandomTravelToNeighbors()), Globals.Color$.$curve_3, j, sttIdx);
                        drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getIttRandomTravelToNeighbors(), sttIdx, ++curveIndex);
                    }
                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }

            }
        }

        //============================//============================//============================ Timed Average Chart

        if (showChartsFlag[1] && maxAxisY.length > 1) {
            prepareChartPosition(0);

            for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {
                if (!showWorldsFlag[j]) {
                    continue;
                }

                World world = worlds[j];

                if (j > Globals.SIMULATION_TIMER || world == null) {
                    break;
                }

                loAxisX = j;

                worldTimer = j < Globals.SIMULATION_TIMER ? Config.WORLD_LIFE_TIME : Globals.WORLD_TIMER;

                WorldStatistics[] statistics = world.getWdStatistics();

                maxAxisY[1] = Math.max(maxAxisY[1], statistics[worldTimer - 1].getTimedAvgAgentTarget());
                maxAxisY[1] = Math.max(maxAxisY[1], statistics[worldTimer - 1].getTimedAvgAgentInPitfall());
                maxAxisY[1] = Math.max(maxAxisY[1], statistics[worldTimer - 1].getTimedAvgRandomTravel());


                for (int sttIdx = 0, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {
                    WorldStatistics stat = statistics[sttIdx];

                    int curveIndex = 2;

                    if (sttIdx == 0 || stat.getEpisode() != statistics[sttIdx - 1].getEpisode()) {
                        loAxisX += _hs;
                        prevPoints[0].y = (int) (0.1 * _vs * stat.getTimedAvgAgentTarget());
                        prevPoints[1].y = (int) (0.1 * _vs * stat.getTimedAvgAgentInPitfall());
                        prevPoints[2].y = (int) (0.1 * _vs * stat.getTimedAvgRandomTravel());
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;

                    } else {

                        prevPoints[0].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTimedAvgAgentTarget());
                        prevPoints[1].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTimedAvgAgentInPitfall());
                        prevPoints[2].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getTimedAvgRandomTravel());
                        prevPoints[0].x = prevPoints[1].x = prevPoints[2].x = loAxisX;
                        loAxisX += _hs;
                    }

                    if (showLineChartsFlag[0]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getTimedAvgAgentTarget()), Globals.Color$.$curve_1, j, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getTimedAvgAgentTarget(), sttIdx, ++curveIndex);
                    }

                    if (showLineChartsFlag[1]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getTimedAvgAgentInPitfall()), Globals.Color$.$curve_2, j, sttIdx);
                        drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getTimedAvgAgentInPitfall(), sttIdx, ++curveIndex);
                    }

                    if (showLineChartsFlag[2]) {
                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getTimedAvgRandomTravel()), Globals.Color$.$curve_3, j, sttIdx);
                        drawLine(prevPoints[2].x, prevPoints[2].y, loAxisX, stat.getTimedAvgRandomTravel(), sttIdx, ++curveIndex);
                    }

                    if (axisX < loAxisX) {
                        axisX = loAxisX;
                    }
                }
            }
        }
        //============================//============================//============================ Episode Drawing
        if (Config.SIMULATION_MODE == TtSimulationMode.Episodic) {
            if (showChartsFlag[2]) {
                g.translate(0, -1200);
                g.setColor(Color.ORANGE);
                g.drawLine(0, 0, getRealWith(), 0);


                for (int j = 0, worldsLength = worlds.length; j < worldsLength; j++) {
                    if (!showWorldsFlag[j]) {
                        continue;
                    }

                    World world = worlds[j];

                    if (j > Globals.SIMULATION_TIMER || world == null) {
                        break;
                    }

                    loAxisX = j;
                    axisY = 0;

                    worldTimer = j < Globals.SIMULATION_TIMER ? world.getEpStatistics().length : Globals.EPISODE - 1;

                    EpisodeStatistics[] statistics = world.getEpStatistics();
                    for (int sttIdx = 0, statisticsLength = statistics.length; sttIdx < worldTimer && sttIdx < statisticsLength; sttIdx++) {
                        EpisodeStatistics stat = statistics[sttIdx];

                        if (stat.getToTime() == 0) {
                            break;
                        }
                        int curveIndex = 5;


                        if (sttIdx > 0) {
                            prevPoints[0].x = prevPoints[1].x = loAxisX;
                            prevPoints[0].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getMidAgentsInTarget());
                            prevPoints[1].y = (int) (0.1 * _vs * statistics[sttIdx - 1].getMidAgentsInPitfall());
                            loAxisX += 100;

                        } else {
                            loAxisX += 100;
                            prevPoints[0].x = prevPoints[1].x = loAxisX;
                            prevPoints[0].y = (int) (0.1 * _vs * stat.getMidAgentsInTarget());
                            prevPoints[1].y = (int) (0.1 * _vs * stat.getMidAgentsInPitfall());
                        }

                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getMidAgentsInTarget()), Globals.Color$.$curve_1, j, 20, sttIdx);
                        drawLine(prevPoints[0].x, prevPoints[0].y, loAxisX, stat.getMidAgentsInTarget(), sttIdx, ++curveIndex);

                        drawSymbolOnCurve(loAxisX, (int) (0.1 * _vs * stat.getMidAgentsInPitfall()), Globals.Color$.$curve_2, j, 20, sttIdx);
                        drawLine(prevPoints[1].x, prevPoints[1].y, loAxisX, stat.getMidAgentsInPitfall(), sttIdx, ++curveIndex);

                    }
                }
            }
        }

        //drawBottomSlitterLine();
    }
}