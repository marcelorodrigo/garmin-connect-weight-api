package com.marcelorodrigo.fit;

import com.garmin.fit.DateTime;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FitWeightTest {
    @Mock
    Clock clock;

    @Captor
    ArgumentCaptor<DateTime> dateTimeCaptor;

    @Mock
    GenerateFileIdMesg generateFileIdMesg;

    @Mock
    GenerateWeightScaleMesg generateWeightScaleMesg;

    @Mock
    FitFileCreator fitFileCreator;

    @InjectMocks
    FitWeight fitWeight;

    @Test
    void create_createsFitFile() throws IOException {
        // Given
        val givenInstant = Instant.parse("2021-01-01T00:00:00Z");
        val weightMeasurement = WeightMeasurement.builder().weight(70f).build();
        val fileIdMesg = new FileIdMesg();
        val weightScaleMesg = new WeightScaleMesg();
        val expectedPath = Path.of("test.fit");

        // When
        when(clock.instant()).thenReturn(givenInstant);
        when(generateFileIdMesg.execute(any())).thenReturn(fileIdMesg);
        when(generateWeightScaleMesg.execute(any(), any())).thenReturn(weightScaleMesg);
        when(fitFileCreator.execute(fileIdMesg, weightScaleMesg)).thenReturn(expectedPath);
        val result = fitWeight.create(weightMeasurement);

        // Then
        assertThat(result).isEqualTo(expectedPath);
        verify(generateWeightScaleMesg).execute(any(), any());
        verify(fitFileCreator).execute(fileIdMesg, weightScaleMesg);
        verify(generateFileIdMesg).execute(dateTimeCaptor.capture());

        val dateTime = dateTimeCaptor.getValue();
        assertThat(dateTime.getTimestamp()).isEqualTo(new DateTime(Date.from(givenInstant)).getTimestamp());
    }
}