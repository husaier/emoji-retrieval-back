package org.bupt.hse.retrieval.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

/**
 * @author Hu Saier <husaier@bupt.edu.cn>
 * Created on 2022-05-17
 */
public enum ImageTypeEnum {
    JPG("jpg", ".jpg"),
    GIF("gif", ".gif");

    @EnumValue
    private final String code;
    private final String suffix;

    ImageTypeEnum(String code, String suffix) {
        this.code = code;
        this.suffix = suffix;
    }

    public String getCode() {
        return code;
    }

    public String getSuffix() {
        return suffix;
    }

    @JsonCreator
    public static ImageTypeEnum toImageTypeEnum(String value) {
        for (ImageTypeEnum item : ImageTypeEnum.values()) {
            if (item.getCode().equals(value.toLowerCase(Locale.ROOT))) {
                return item;
            }
        }
        return null;
    }

    public static ImageTypeEnum parseTypeBySuffix(String value) {
        for (ImageTypeEnum item : ImageTypeEnum.values()) {
            if (item.getSuffix().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
