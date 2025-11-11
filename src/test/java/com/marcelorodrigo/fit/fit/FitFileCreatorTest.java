package com.marcelorodrigo.fit.fit;

import com.garmin.fit.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class FitFileCreatorTest {

    @Test
    void execute_createsFitFile() throws IOException {
        // Arrange
        FileIdMesg fileIdMesg = new FileIdMesg();
        fileIdMesg.setType(com.garmin.fit.File.WEIGHT);
        fileIdMesg.setManufacturer(Manufacturer.GARMIN);
        fileIdMesg.setProduct(2429);
        fileIdMesg.setSerialNumber(12345L);
        fileIdMesg.setTimeCreated(new DateTime(1234567890L));

        WeightScaleMesg weightScaleMesg = new WeightScaleMesg();
        weightScaleMesg.setWeight(70.0f);

        // Act
        Path fitFilePath = new FitFileCreator().execute(fileIdMesg, weightScaleMesg);

        // Assert
        assertThat(fitFilePath).isNotNull();
        assertThat(Files.exists(fitFilePath)).isTrue();
        assertThat(Files.size(fitFilePath)).isPositive();

        // Assert the decoded file using garmin fit sdk
        final var decode = new Decode();
        final var mesgBroadcaster = new MesgBroadcaster(decode);
        final var fileIdMesgs = new ArrayList<Mesg>(1);
        final var weightScaleMesgs = new ArrayList<Mesg>(1);

        mesgBroadcaster.addListener((MesgListener) mesg -> {
            if (mesg.getNum() == MesgNum.FILE_ID) {
                fileIdMesgs.add(new FileIdMesg(mesg));
            } else if (mesg.getNum() == MesgNum.WEIGHT_SCALE) {
                weightScaleMesgs.add(new WeightScaleMesg(mesg));
            }
        });

        try (final var inputStream = Files.newInputStream(fitFilePath)) {
            decode.read(inputStream, mesgBroadcaster);
        }

        assertThat(fileIdMesgs).hasSize(1);
        assertThat(weightScaleMesgs).hasSize(1);

        FileIdMesg decodedFileIdMesg = (FileIdMesg) fileIdMesgs.getFirst();
        WeightScaleMesg decodedWeightScaleMesg = (WeightScaleMesg) weightScaleMesgs.getFirst();

        assertThat(decodedFileIdMesg.getType()).isEqualTo(com.garmin.fit.File.WEIGHT);
        assertThat(decodedFileIdMesg.getManufacturer()).isEqualTo(Manufacturer.GARMIN);
        assertThat(decodedFileIdMesg.getProduct()).isEqualTo(2429);
        assertThat(decodedFileIdMesg.getSerialNumber()).isEqualTo(12345L);
        assertThat(decodedFileIdMesg.getTimeCreated().getTimestamp()).isEqualTo(1234567890L);

        assertThat(decodedWeightScaleMesg.getWeight()).isEqualTo(70.0f);

        // Clean up
        Files.deleteIfExists(fitFilePath);
    }
}