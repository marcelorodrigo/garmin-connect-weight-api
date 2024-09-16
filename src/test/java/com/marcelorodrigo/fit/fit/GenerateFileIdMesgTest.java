package com.marcelorodrigo.fit.fit;

import com.garmin.fit.DateTime;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenerateFileIdMesgTest {
    @Test
    void execute() {
        // Arrange
        val generateFileIdMesg = new GenerateFileIdMesg();
        val timeCreated = new DateTime(1234567890L);

        // Act
        val fileIdMesg = generateFileIdMesg.execute(timeCreated);

        // Assert
        assertThat(fileIdMesg).isNotNull();
        assertThat(fileIdMesg.getType()).isEqualTo(com.garmin.fit.File.WEIGHT);
        assertThat(fileIdMesg.getManufacturer()).isEqualTo(com.garmin.fit.Manufacturer.GARMIN);
        assertThat(fileIdMesg.getProduct()).isEqualTo(2429);
        assertThat(fileIdMesg.getSerialNumber()).isEqualTo(12345L);
        assertThat(fileIdMesg.getTimeCreated().getTimestamp()).isEqualTo(timeCreated.getTimestamp());
    }
}