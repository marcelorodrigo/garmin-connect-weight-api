package com.marcelorodrigo.fit.fit;

import com.garmin.fit.DateTime;
import com.garmin.fit.File;
import com.garmin.fit.FileIdMesg;
import com.garmin.fit.Manufacturer;
import org.springframework.stereotype.Component;

@Component
public class GenerateFileIdMesg {
    public FileIdMesg execute(final DateTime timeCreated) {
        var fileIdMesg = new FileIdMesg();
        fileIdMesg.setType(File.WEIGHT);
        fileIdMesg.setManufacturer(Manufacturer.GARMIN);
        fileIdMesg.setProduct(2429);
        fileIdMesg.setSerialNumber(12345L);
        fileIdMesg.setTimeCreated(timeCreated);
        return fileIdMesg;
    }
}
