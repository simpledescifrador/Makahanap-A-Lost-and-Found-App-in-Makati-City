package com.makatizen.makahanap.utils.converters;

import android.arch.persistence.room.TypeConverter;
import com.makatizen.makahanap.utils.enums.District;

public class DistrictConverter {

    @TypeConverter
    public static District toDistrict(int code) {
        if (code == District.ONE.getCode()) {
            return District.ONE;
        } else {
            return District.TWO;
        }
    }

    @TypeConverter
    public static int toInt(District district) {
        return district.getCode();
    }
}
