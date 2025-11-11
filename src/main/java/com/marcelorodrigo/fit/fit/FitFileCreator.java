package com.marcelorodrigo.fit.fit;

import com.garmin.fit.FileEncoder;
import com.garmin.fit.FileIdMesg;
import com.garmin.fit.Fit;
import com.garmin.fit.WeightScaleMesg;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class FitFileCreator {
    public Path execute(final FileIdMesg fileIdMesg, final WeightScaleMesg weightScale) throws IOException {
        final var generatedFile = Files.createTempFile("weight-", ".fit");
        final var fileEncoder = new FileEncoder(generatedFile.toFile(), Fit.ProtocolVersion.V2_0);
        fileEncoder.write(List.of(fileIdMesg, weightScale));
        fileEncoder.close();
        return generatedFile;
    }
}
