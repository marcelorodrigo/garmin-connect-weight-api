package com.marcelorodrigo.fit;

import com.garmin.fit.FileIdMesg;
import com.garmin.fit.WeightScaleMesg;
import com.marcelorodrigo.fit.domain.WeightMeasurement;
import com.marcelorodrigo.fit.fit.FitFileCreator;
import com.marcelorodrigo.fit.fit.FitWeight;
import com.marcelorodrigo.fit.fit.GenerateFileIdMesg;
import com.marcelorodrigo.fit.fit.GenerateWeightScaleMesg;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FitWeightTest {
    @Mock
    private GenerateFileIdMesg generateFileIdMesg;

    @Mock
    private GenerateWeightScaleMesg generateWeightScaleMesg;

    @Mock
    private FitFileCreator fitFileCreator;

    @InjectMocks
    private FitWeight fitWeight;

    @Test
    void create_createsFitFile() throws IOException {
        // Arrange
        WeightMeasurement weightMeasurement = WeightMeasurement.builder().weight(70f).build();
        val instant = Instant.now();
        var fileIdMesg = new FileIdMesg();
        var weightScaleMesg = new WeightScaleMesg();
        Path expectedPath = Path.of("test.fit");

        when(generateFileIdMesg.execute(any())).thenReturn(fileIdMesg);
        when(generateWeightScaleMesg.execute(any(), any())).thenReturn(weightScaleMesg);
        when(fitFileCreator.execute(fileIdMesg, weightScaleMesg)).thenReturn(expectedPath);

        // Act
        Path result = fitWeight.create(instant, weightMeasurement);

        // Assert
        assertThat(result).isEqualTo(expectedPath);
        verify(generateFileIdMesg).execute(any());
        verify(generateWeightScaleMesg).execute(any(), any());
        verify(fitFileCreator).execute(fileIdMesg, weightScaleMesg);
    }
}