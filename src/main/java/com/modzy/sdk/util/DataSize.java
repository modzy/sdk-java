package com.modzy.sdk.util;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class to convert size units that comes as string on the yaml file.
 *
 * @see <a href="https://github.com/spring-projects/spring-framework/blob/master/spring-core/src/main/java/org/springframework/util/unit/DataSize.java">Spring implementation</a>
 */
public class DataSize {

    /**
     * The pattern for parsing.
     */
    private static final Pattern PATTERN = Pattern.compile("^(\\d+(\\.\\d+)?)([a-zA-Z]{0,2})$");

    private Long bytes;

    private String strSize;

    public DataSize(Long bytesSize){
        this.setBytes(bytesSize);
    }

    public DataSize(String stringSize){
        this.setBytes(stringSize);
    }

    public DataSize(Object objectSize){
        this.setBytes(objectSize);
    }

    public void setBytes(Long bytesSize){
        this.bytes = bytesSize;
    }

    public void setBytes(Number bytesSize){
        this.bytes = bytesSize.longValue();
    }

    public void setBytes(String stringSize){
        Matcher matcher = DataSize.PATTERN.matcher(stringSize);
        if( matcher.matches() ){
            DataUnit dataUnit = DataUnit.findValueOf(matcher.group(3));
            this.bytes = (long)((double)dataUnit.size()*Double.parseDouble(matcher.group(1)));
            this.strSize = stringSize;
        }
    }

    public void setBytes(Object objectSize){
        if( objectSize instanceof Number ){
            this.setBytes((Number)objectSize);
        }
        else{
            this.setBytes(String.valueOf(objectSize));
        }
    }

    public Long getBytes(){
        return this.bytes;
    }

    @Override
    public String toString(){
        if(this.strSize != null){
            return this.strSize;
        }
        Double   size = null;
        DataUnit unit = null;
        if( this.bytes != null ) {
            for (DataUnit dataUnit : DataUnit.values()) {
                long div = this.bytes / dataUnit.size();
                if (div > 0) {
                    double div2 = (double)this.bytes / (double)dataUnit.size();
                    if (size == null || div2 < size) {
                        size = div2;
                        unit = dataUnit;
                    }
                }
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(size)+unit.suffix();
    }

}

enum DataUnit{

    /**
     * Bytes, represented by suffix {@code i}.
     */
    BYTES("i", 1l),

    /**
     * Kilobytes, represented by suffix {@code K}.
     */
    KILOBYTES("K", 1000l),

    /**
     * Megabytes, represented by suffix {@code M}.
     */
    MEGABYTES("M", 1000l*1000l),

    /**
     * Gigabytes, represented by suffix {@code G}.
     */
    GIGABYTES("G", 1000l*1000l*1000l),

    /**
     * Terabytes, represented by suffix {@code T}.
     */
    TERABYTES("T", 1000l*1000l*1000l*1000l),

    /**
     * Kibibytes, represented by suffix {@code Ki}.
     */
    KIBIBYTES("Ki", 1024l),

    /**
     * Mebibytes, represented by suffix {@code Mi}.
     */
    MEBIBYTES("Mi", 1024l*1024l),

    /**
     * Gibibytes, represented by suffix {@code Gi}.
     */
    GIBIBYTES("Gi", 1024l*1024l*1024l),

    /**
     * Tebibytes, represented by suffix {@code Ti}.
     */
    TEBIBYTES("Ti", 1024l*1024l*1024l*1024l),

    /**
     * Kibibytes (non decimal Kilobytes), represented by suffix {@code KB}.
     */
    KIBIBYTES_LEGACY("KB", 1024l),

    /**
     * Mebibytes (non decimal Megabytes), represented by suffix {@code MB}.
     */
    MEBIBYTES_LEGACY("MB", 1024l*1024l),

    /**
     * Gibibytes (non decimal Gigabytes), represented by suffix {@code GB}.
     */
    GIBIBYTES_LEGACY("GB", 1024l*1024l*1024l),

    /**
     * Tebibytes (non decimal Terabytes), represented by suffix {@code TB}.
     */
    TEBIBYTES_LEGACY("TB", 1024l*1024l*1024l*1024l);

    private final String suffix;
    private final long size;

    DataUnit(String suffix, long size){
        this.suffix = suffix;
        this.size = size;
    }

    String suffix(){return this.suffix;};

    long size(){return this.size;}

    public static DataUnit findValueOf(String suffix){
        for(DataUnit dataUnit : DataUnit.values() ){
            if( dataUnit.suffix().compareToIgnoreCase(suffix) == 0 ){
                return dataUnit;
            }
        }
        return null;
    }
}
