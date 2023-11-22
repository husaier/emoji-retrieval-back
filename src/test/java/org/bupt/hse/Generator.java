package org.bupt.hse;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.sql.Types;
import java.util.Collections;

public class Generator {

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        FastAutoGenerator.create("jdbc:mysql://10.112.67.227:3306/emoji_retrieval", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("Hu Saier") // 设置作者
                            .outputDir(projectPath + "/src/main/java")
                            .enableSwagger(); // 开启 swagger 模式
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);

                }))
                .packageConfig(builder -> {
                    builder.parent("org.bupt.hse") // 设置父包名
                            .moduleName("retrieval") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + "/src/main/resources")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("image")
                            .entityBuilder()
                            .naming(NamingStrategy.underline_to_camel);
                })
                 // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
