package org.grp2.unitTest;

import org.grp2.domain.BatchStatistics;
import org.grp2.domain.MeasurementsStatistics;
import org.grp2.shared.Batch;
import org.grp2.shared.MeasurementLog;
import org.grp2.shared.Measurements;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PlantStatisticsTest {
    @Test
    public void measurementStatisticsTest() {
        Object[] data = setupMeasurementsData();
        MeasurementsStatistics measurementsStatistics = new MeasurementsStatistics((List<MeasurementLog>) data[0]);
        MeasurementsStatistics expected = (MeasurementsStatistics) data[1];

        assertEquals(expected.getAvgTemp(), measurementsStatistics.getAvgTemp(), 0);
        assertEquals(expected.getLowestTemp(), measurementsStatistics.getLowestTemp(), 0);
        assertEquals(expected.getHighestTemp(), measurementsStatistics.getHighestTemp(), 0);
    }

    @Test
    public void batchStatisticsTest() {
        Object[] data = setupBatchData();
        BatchStatistics batchStatistics = new BatchStatistics((List<Batch>) data[0]);
        BatchStatistics expected = (BatchStatistics) data[1];

        assertEquals(expected.getAvgAccepted(), batchStatistics.getAvgAccepted(), 0);
        assertEquals(expected.getAvgDefects(), batchStatistics.getAvgDefects(), 0);
        assertEquals(expected.getAvgProductionSeconds(), batchStatistics.getAvgProductionSeconds(), 0);

        assertEquals(expected.getSumAccepted(), batchStatistics.getSumAccepted(), 0);
        assertEquals(expected.getSumDefects(), batchStatistics.getSumDefects(), 0);
        assertEquals(expected.getSumProductionSeconds(), batchStatistics.getSumProductionSeconds(), 0);

    }

    private Object[] setupMeasurementsData() {
        List<MeasurementLog> measurements = new ArrayList<>();

        Collections.addAll(measurements,
                new MeasurementLog(-1, null, new Measurements(24, 52, 0.2)),
                new MeasurementLog(-1, null, new Measurements(22, 42, -0.1)),
                new MeasurementLog(-1, null, new Measurements(25, 57, 0.7)),
                new MeasurementLog(-1, null, new Measurements(19, 32, -0.5)));

        MeasurementsStatistics expected = new MeasurementsStatistics(25.0, 19.0, 22.5);

        return new Object[]{measurements, expected};
    }

    private Object[] setupBatchData() {
        List<Batch> batches = new ArrayList<>();

        Collections.addAll(batches,
                new Batch("testName", -1, -1, LocalDateTime.now(), LocalDateTime.now().plusSeconds(30), 142, 32, -1),
                new Batch("testName", -1, -1, LocalDateTime.now(), LocalDateTime.now().plusSeconds(52), 500, 122, -1),
                new Batch("testName", -1, -1, LocalDateTime.now(), LocalDateTime.now().plusSeconds(25), 250, 58, -1),
                new Batch("testName", -1, -1, LocalDateTime.now(), LocalDateTime.now().plusSeconds(59), 600, 98, -1));

        BatchStatistics expected = new BatchStatistics(373.0, 77.5, 41.5, 1492, 310, 166, null);

        return new Object[]{batches, expected};
    }
}
